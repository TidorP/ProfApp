package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import javafx.util.Pair;
import jdk.internal.org.xml.sax.InputSource;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import repository.NoteRepository;
import repository.StudentRepository;
import repository.TemaRepository;
import utils.*;
import utils.Observable;
import utils.Observer;
import validate.ValidationException;
import validate.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ServiceNote implements Observable<GradeEvent> {
    private StudentRepository studentRepository;
    private TemaRepository temaRepository;
    private NoteRepository noteRepository=new NoteRepository();
    private ArrayList<Observer<GradeEvent>> observers;

    /**
     *
     * @param studentRepository -repository for students
     * @param temaRepository - repository for homeworks
     */
    public ServiceNote(StudentRepository studentRepository, TemaRepository temaRepository) {
        this.studentRepository = studentRepository;
        this.temaRepository = temaRepository;
        observers=new ArrayList<>();

        try {
            readFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }
//    public void readFromFile() throws IOException, ValidationException {
//        File file = new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\Note");
//        Scanner sc = new Scanner(file);
//        while (sc.hasNextLine()) {
//            String[] splited = sc.nextLine().split(",");
//            saveNota(Integer.parseInt(splited[0]),Integer.parseInt(splited[1]),Integer.parseInt(splited[2]),Integer.parseInt(splited[3]),splited[4]);
//        }
//
//    }
    public void readFromFile() throws IOException, ValidationException {
        String fName="C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML";
        if(new File(fName).length()==0)
        {
            return;
        }
        try {
            File inputFile = new File(fName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Nota");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String studentId=eElement.getElementsByTagName("StudentId").item(0).getTextContent();
                    String temaId=eElement.getElementsByTagName("TemaId").item(0).getTextContent();
                    String valoare=eElement.getElementsByTagName("Valoare").item(0).getTextContent();
                    String saptamana=eElement.getElementsByTagName("Saptamana").item(0).getTextContent();
                    String feedback=eElement.getElementsByTagName("Feedback").item(0).getTextContent();

                    saveNota(Integer.parseInt(studentId),Integer.parseInt(temaId),Integer.parseInt(valoare),Integer.parseInt(saptamana),feedback);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    public void writeToFile(Nota n,String descriere) throws IOException {
//        Writer output=new BufferedWriter(new FileWriter("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\Note", true));
//        String s=n.getStudentId()+","+n.getTemaId()+","+n.getValoare()+","+n.getDate()+","+descriere;
//        output.append("\n");
//        output.append(s);
//        output.close();
//    }

    /**
     *
     * @param n -a grade
     * @param descriere -description
     */
    public void writeToFile(Nota n,String descriere)
    {
        eraseLast();
        String fName="C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML";
        String text="<Nota>\n" +
                "<StudentId>"+n.getStudentId()+"</StudentId>\n" +
                "<TemaId>"+n.getTemaId()+"</TemaId>\n" +
                "<Valoare>"+n.getValoare()+"</Valoare>\n" +
                "<Saptamana>"+n.getDate()+"</Saptamana>\n" +
                "<Feedback>"+descriere+"</Feedback>\n" +
                "</Nota>\n</Note>";
        Writer output= null;
        try {
            output = new BufferedWriter(new FileWriter(fName, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            output.append("\n");
            output.append(text);
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * deleting the last line from a file
     */
    public void eraseLast() {
        File file = new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML");
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte b;
        long length = 0;
        try {
            length = randomAccessFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (length != 0) {
            b=0;
            do {
                length -= 1;
                try {
                    randomAccessFile.seek(length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    b = randomAccessFile.readByte();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (b != 10 && length > 0);
            try {
                randomAccessFile.setLength(length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param id - id of a student
     * @return - student
     */
    public Student findOneS(int id) {

        return studentRepository.findOne(id);
    }

    /**
     *
     * @param name -name of a student
     * @return -the student
     */
    public Student findOneByNameS(String name) {

        Iterable<Student> students=findAllS();
        for(Student st:students)
        {
            if(st.getNume().equals(name))
            {
                return st;
            }
        }
        return null;
    }

    /**
     *
     * @param id -id of a homework
     * @return -homework
     */
    public Tema findOneT(int id) {
        return temaRepository.findOne(id);
    }

    /**
     *
     * @return all the students
     */
    public Iterable<Student> findAllS() {
        return studentRepository.findAll();

    }

    public Iterable<Nota> findAllNote() {
        return noteRepository.getNote();

    }

    /**
     *
     * @return all the homeworks
     */
    public Iterable<Tema> findAllT() {
        return temaRepository.findAll();
    }

    /**
     *
     * @param id -id
     * @param nume -name
     * @param grupa -group
     * @param email -email
     * @param cadruDidactic -name of the professor
     * @return the new student
     * @throws ValidationException -in case the data in invalid
     */
    public Student saveS(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException {
        Student st=studentRepository.save(new Student(id, nume, grupa, email, cadruDidactic));
        studentRepository.writeToFile();
        return st;
    }



    /**
     *
     * @param id -id
     * @param descrierere -description
     * @param due -due date
     * @param given -given date
     * @return -the new homework
     * @throws ValidationException -in case the data in invalid
     */
    public Tema saveT(int id, String descrierere, int due, int given) throws ValidationException {
        Tema t=temaRepository.save(new Tema(id, descrierere, due, given));
        temaRepository.writeToFile();
        return t;
    }

    /**
     *
     * @param id -the id
     * @return the deleted student
     */
    public Student deleteS(int id) {
        Student st=studentRepository.delete(id);
        studentRepository.writeToFile();
        //notifyObservers(new StudentEvent(null, st, ChangeEventType.DELETE));
        return st;
    }

    /**
     *
     * @param id -the id
     * @return -the deleted homework
     */
    public Tema deleteT(int id) {
        Tema t=temaRepository.delete(id);
        temaRepository.writeToFile();
        return t;
    }
    public void deleteGradeByStudent(Integer studentId)
    {
        List<Nota> l=new ArrayList<>();
        findAllNote().forEach(l::add);
        for(Nota n: l)
        {
            if(n.getStudentId()==studentId)
            {
                noteRepository.deleteNota(n);
                notifyObservers(new GradeEvent(null, n, ChangeEventType.DELETE));
            }
        }
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = docBuilder.parse (new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML"));
            // <person>
            NodeList nodes = doc.getElementsByTagName("Nota");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element person = (Element)nodes.item(i);
                // <name>
                Element name = (Element)person.getElementsByTagName("StudentId").item(0);
                String pName = name.getTextContent();
                if (pName.equals(Integer.toString(studentId))) {
                    System.out.println("1");
                    person.getParentNode().removeChild(person);
                }
            }
            printXML(doc);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    public static void printXML(Document doc) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();
        System.out.println(xmlString);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            Document doc2= builder.parse(new org.xml.sax.InputSource(new StringReader(xmlString)));

            // Write the parsed document to an xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer2 = transformerFactory.newTransformer();
            DOMSource source2 = new DOMSource(doc2);

            StreamResult result2 =  new StreamResult(new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML"));
            transformer2.transform(source2, result2);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void deleteGradeByHM(Integer temaId)
    {
        List<Nota> l=new ArrayList<>();
        findAllNote().forEach(l::add);
        for(Nota n: l)
        {
            if(n.getTemaId()==temaId)
            {
                noteRepository.deleteNota(n);
                notifyObservers(new GradeEvent(null, n, ChangeEventType.DELETE));
            }
        }
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            Document doc = docBuilder.parse (new File("C:\\Users\\ptido\\IdeaProjects\\Lab3\\src\\repository\\NoteXML"));
            // <person>
            NodeList nodes = doc.getElementsByTagName("Nota");

            for (int i = 0; i < nodes.getLength(); i++) {
                Element person = (Element)nodes.item(i);
                // <name>
                Element name = (Element)person.getElementsByTagName("TemaId").item(0);
                String pName = name.getTextContent();
                if (pName.equals(Integer.toString(temaId))) {
                    person.getParentNode().removeChild(person);
                }
            }
            printXML(doc);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * @param id -the id
     * @param nume -new name
     * @param grupa -new group
     * @param email -new email
     * @param cadruDidactic -new prof mail
     * @return -new Student
     * @throws ValidationException -in case the data in invalid
     */
    public Student updateS(int id, String nume, int grupa, String email, String cadruDidactic) throws ValidationException
    {
        Student st= studentRepository.update(new Student(id, nume, grupa, email, cadruDidactic));
        studentRepository.writeToFile();
        return st;
    }
    /**
     *
     * @param id -the id
     * @param descrierere -new description
     * @param due -new due date
     * @param given -new given date
     * @return -new homework
     * @throws ValidationException -in case the data in invalid
     */
    public Tema updateT(int id, String descrierere, int due, int given) throws ValidationException
    {
        Tema t=temaRepository.update(new Tema(id, descrierere, due, given));
        temaRepository.writeToFile();
        return t;
    }

    /**
     *
     * @param id -the id of the homework
     */
    public void prelungireTema(int id)
    {
        Tema tema=findOneT(id);
        tema.prelungire();
        temaRepository.writeToFile();
    }

    /**
     *
     * @param studentId  -the student id
     * @param temaId - the homework id
     * @param valoare -the mark
     * @param saptamana -the current week
     * @param FeedBack -the feedback
     * @return nota_maxima - the max mark the student with the id studentId can take
     * @throws IOException -in case something goes wrong with the file
     * @throws ValidationException -in case of invalid data
     */
    public int saveNota(int studentId, int temaId, int valoare, int saptamana, String FeedBack) throws IOException, ValidationException {

        if(!noteRepository.findNota_forStudent(studentId,temaId))
            return -1;

        Tema tema=findOneT(temaId);
        Student student=findOneS(studentId);

        if(tema==null || student==null)
            throw new ValidationException("Tema sau student cu acest id nu exista!!!");

        Nota nota;
        int nota_maxima=10;
        if(tema.getDue()-saptamana>=0)
            nota=new Nota(temaId,studentId,valoare,saptamana);
        else if(saptamana-tema.getDue()>2) {
            nota = new Nota(temaId, studentId, 1, saptamana);
            nota_maxima=1;
        }
        else {
            nota = new Nota(temaId, studentId, valoare - 2 * (-(tema.getDue() - saptamana)), saptamana);
            nota_maxima=nota_maxima-2 * (-(tema.getDue() - saptamana));
        }

        noteRepository.addNota(nota);
        String path="C:"+"\\"+"Users"+"\\"+"ptido"+"\\"+"IdeaProjects"+"\\"+"Lab3"+"\\"+"student's folders"+"\\";
        path=path+student.getNume()+".txt";
        String text="Tema:"+tema.getId()+System.lineSeparator()+
                "Nota:"+Integer.toString(nota.getValoare())+System.lineSeparator()+
                "Predata in saptamana:"+Integer.toString(saptamana)+System.lineSeparator()+
                "Deadline:"+Integer.toString(tema.getDue())+System.lineSeparator()+
                "Feedback:"+FeedBack+System.lineSeparator();
        createFileFolder(student, path, text, noteRepository);
        notifyObservers(new GradeEvent(null, nota, ChangeEventType.ADD));
        return nota_maxima;

    }
    public int saveNotaWrite(int studentId, int temaId, int valoare, int saptamana, String FeedBack) throws IOException, ValidationException {

        if(!noteRepository.findNota_forStudent(studentId,temaId))
            return -1;

        Tema tema=findOneT(temaId);
        Student student=findOneS(studentId);

        if(tema==null || student==null)
            throw new ValidationException("Tema sau student cu acest id nu exista!!!");

        Nota nota;
        int nota_maxima=10;
        if(tema.getDue()-saptamana>=0)
            nota=new Nota(temaId,studentId,valoare,saptamana);
        else if(saptamana-tema.getDue()>2) {
            nota = new Nota(temaId, studentId, 1, saptamana);
            nota_maxima=1;
        }
        else {
            nota = new Nota(temaId, studentId, valoare - 2 * (-(tema.getDue() - saptamana)), saptamana);
            nota_maxima=nota_maxima-2 * (-(tema.getDue() - saptamana));
        }

        noteRepository.addNota(nota);
        writeToFile(nota,FeedBack);
        String path="C:"+"\\"+"Users"+"\\"+"ptido"+"\\"+"IdeaProjects"+"\\"+"Lab3"+"\\"+"student's folders"+"\\";
        path=path+student.getNume()+".txt";
        String text="Tema:"+tema.getId()+System.lineSeparator()+
                "Nota:"+Integer.toString(nota.getValoare())+System.lineSeparator()+
                "Predata in saptamana:"+Integer.toString(saptamana)+System.lineSeparator()+
                "Deadline:"+Integer.toString(tema.getDue())+System.lineSeparator()+
                "Feedback:"+FeedBack+System.lineSeparator();
        createFileFolder(student, path, text, noteRepository);
        notifyObservers(new GradeEvent(null, nota, ChangeEventType.ADD));
        return nota_maxima;

    }

    static void createFileFolder(Student student, String path, String text, NoteRepository noteRepository) throws IOException {
        if(!noteRepository.findFile(student.getNume())) {
            noteRepository.addFile(student.getNume());
            File f = new File(path);
            f.getParentFile().mkdirs();
            f.createNewFile();
            try {
                if(!FileUtils.readFileToString(new File(path),UTF_8).contains(text))
                    Files.write(Paths.get(path), text.getBytes(UTF_8), StandardOpenOption.APPEND);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                if(!FileUtils.readFileToString(new File(path),UTF_8).contains(text))
                    Files.write(Paths.get(path), text.getBytes(UTF_8), StandardOpenOption.APPEND);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendEmailToStudent(Integer studentId, Nota nota,Integer nota_maxima)
    {
        if (nota_maxima < nota.getValoare()) {
            SendEmailAttach sendEmail=new SendEmailAttach(findOneS(studentId).getEmail(),"Your grade for the homework "+nota.getTemaId()+" was at first glance evaluated to "+nota.getValoare()+".\n"+"However, you did not respect the deadline homework so it was later changed to " + nota_maxima,"You have just been assigned a grade!","C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\"+findOneS(studentId).getNume()+".txt");
            sendEmail.start();
        } else {
            SendEmailAttach sendEmail=new SendEmailAttach(findOneS(studentId).getEmail(),"Your grade for the homework "+nota.getTemaId()+" is "+nota.getValoare()+".\n"+ "Nota studentului a fost predata la timp deci nu a fost diminuata!","You have just been assigned a grade!","C:\\Users\\ptido\\IdeaProjects\\Lab3\\student's folders\\"+findOneS(studentId).getNume()+".txt");
            sendEmail.start();
        }


    }
    public void sendCodeToStudent(String studentEmail,String code)
    {
        SendEmail sendEmail=new SendEmail(studentEmail,code,"Validation code");
        sendEmail.start();
    }
    public Iterable<Nota> filtByHM(int temaId)
    {
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getTemaId()==temaId) l.add(nota);});
        return l;
    }
    public Iterable<Nota> filtBySt(String studentName)
    {
        int id=findOneByNameS(studentName).getId();
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getStudentId()==id) l.add(nota);});
        return l;
    }
    public Iterable<Nota> filtByWeek(int start,int end)
    {
        List<Nota> l=new ArrayList<>();
        noteRepository.getNote().forEach((nota)->{if(nota.getDate()>=start && nota.getDate()<=end) l.add(nota);});
        return l;
    }
    public List<Student> filtByGroup(int grupa)
    {
        List<Student> l=new ArrayList<>();
        findAllS().forEach((student)->{if(student.getGrupa()==grupa) l.add(student);});
        return l;
    }
    public boolean stHmExists(int temaId,int studentId)
    {
        for(Nota nota: findAllNote())
        {
            if(nota.getStudentId()==studentId && nota.getTemaId()==temaId)
                return true;
        }
        return false;
    }
    public Nota findNotaByStHM(int studentId,int temaId)
    {
        for(Nota n:findAllNote())
        {
            if(n.getId().getFirst()==studentId &&n.getId().getSecond()==temaId)
                return n;
        }
        return null;
    }
    public Map<String,Double> medii()
    {
        List<Nota> l=new ArrayList<>();
        findAllNote().forEach(l::add);
        Function<Nota,Integer> filtSt = s -> s.getStudentId();
        Map<Integer,List<Nota>> map=l.stream().collect(Collectors.groupingBy(filtSt));
        Map<String,Double> result=new HashMap<>();
        for (Map.Entry<Integer,List<Nota>> entry : map.entrySet()) {
            double medie=0;
            int pondere=0;
            Integer key = entry.getKey();
            List<Nota> li=new ArrayList<>(entry.getValue());
            for(Tema t:findAllT())
            {
                if(!stHmExists(t.getId(), key)) {
                    if(t.getDue()==t.getGiven())
                        medie = medie + 1;
                    else
                        medie = medie + 1 * (t.getDue() - t.getGiven());
                    pondere=pondere+t.getDue()-t.getGiven();
                }
                else
                {
                    for(Nota n:li)
                    {
                        if(n.getTemaId()==t.getId())
                        {
                            medie = medie + n.getValoare() * (t.getDue() - t.getGiven());
                            pondere=pondere+t.getDue()-t.getGiven();
                            break;
                        }

                    }
                }
            }
            result.put(findOneS(key).getNume(),medie/pondere);
        }
        return  result;

    }
    public List<Pair<Integer,Integer>> getNoteForStudent(String studentName) {
        List<Nota> l = new ArrayList<>();
        findAllNote().forEach(l::add);
        Function<Nota, String> filtSt = s -> findOneS(s.getStudentId()).getNume();
        Map<String, List<Nota>> map = l.stream().collect(Collectors.groupingBy(filtSt));
        List<Pair<Integer,Integer>> result = new ArrayList<>();
        for (Map.Entry<String, List<Nota>> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key.equals(studentName)) {
                List<Nota> li = new ArrayList<>(entry.getValue());
                for (Tema t : findAllT()) {
                    if (!stHmExists(t.getId(), findOneByNameS(key).getId())) {
                        result.add(new Pair<>(1,t.getId()));
                    } else {
                        for (Nota n : li) {
                            if (n.getTemaId() == t.getId()) {
                                result.add(new Pair<>(n.getValoare(),t.getId()));
                                break;
                            }

                        }
                    }
                }

            }

        }
        return result;
    }
    public Pair<Integer,Double> hardestHM()
    {
        double min=100;
        int temaId=1;
        for(Tema t:findAllT())
        {
            double current=0;
            for(Student st:findAllS())
            {
                if(!stHmExists(t.getId(), st.getId())) {
                    current=current+1;//studentul a luat 1 pe tema asta ptc n a predat o
                }
                else
                {
                    current=current+findNotaByStHM(st.getId(),t.getId()).getValoare();
                }
            }
            if(min>=current/studentRepository.size()) {
                min = current / studentRepository.size();
                temaId=t.getId();
            }
        }
        return new Pair<Integer,Double>(temaId,min);
    }

    public Map<String,Double> studentsPassed() {

        Map<String, Double> allStudents = new HashMap<>(medii());
        Map<String,Double> result=new HashMap<>();
        for (Map.Entry<String,Double> entry : allStudents.entrySet()) {
            String key = entry.getKey();
            Double medie=entry.getValue();
            if(medie>=5)
                result.put(key,medie);

        }
        return result;

    }
    public List<Student> goodStudents()
    {
        List<Student> result=new ArrayList<>();

        List<Nota> l=new ArrayList<>();
        findAllNote().forEach(l::add);
        Function<Nota,Integer> filtSt = s -> s.getStudentId();
        Map<Integer,List<Nota>> map=l.stream().collect(Collectors.groupingBy(filtSt));
        for (Map.Entry<Integer,List<Nota>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            List<Nota> li=new ArrayList<>(entry.getValue());
            if(li.size()==temaRepository.size())
            {
                int ok=1;
                for(Nota n:li)
                {
                    if(n.getDate()>findOneT(n.getTemaId()).getDue())
                        ok=0;
                }
                if(ok==1)
                    result.add(findOneS(key));

            }

        }
        return result;
    }
    public Map<Integer,Double> groupsReport()
    {
        Map<Integer,Double> result=new HashMap<>();

        List<Student> l=new ArrayList<>();
        findAllS().forEach(l::add);
        Function<Student,Integer> filtSt = s -> s.getGrupa();//filt by group
        Map<Integer,List<Student>> map=l.stream().collect(Collectors.groupingBy(filtSt));

        Map<String,Double> scores=medii();
        for (Map.Entry<Integer,List<Student>> entry : map.entrySet()) {
            Integer key = entry.getKey();
            List<Student> li=new ArrayList<>(entry.getValue());
            double medie=0;
            for(Student st:li)
            {
                if(scores.get(st.getNume())==null)
                    medie=medie+1;
                else
                    medie=medie+scores.get(st.getNume());
            }
            result.put(key,medie/li.size());

        }
        return result;
    }


    @Override
    public void addObserver(Observer<GradeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<GradeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(GradeEvent gradeEvent) {
        observers.forEach(obs -> obs.update(gradeEvent));
    }

    public ArrayList<Observer<GradeEvent>> getObservers() {
        return observers;
    }
}
