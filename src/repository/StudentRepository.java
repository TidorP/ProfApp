package repository;

import domain.Student;
import javafx.util.Pair;
import org.w3c.dom.*;
import validate.ValidationException;
import validate.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.HashMap;


public class StudentRepository extends AbstractRepository<Integer, Student> {

    private String fName;

    /**
     *
     * @param v -the validator
     * @param fName -the file name for students
     * @throws FileNotFoundException -maybe we enter an incorrect file name
     * @throws ValidationException -maybe the data in not fit
     */
    public StudentRepository(Validator<Student> v, String fName) throws FileNotFoundException, ValidationException {
        super(v);
        this.fName=fName;
        readFromFile();
    }

    /**
     * other constructor
     * @param v -validator
     */
    public StudentRepository(Validator<Student> v) {
        super(v);
    }

//    /**
//     *
//     * @throws FileNotFoundException -maybe we enter an incorrect file name
//     * @throws ValidationException -maybe the data in not fit
//     */
//
//    public void readFromFile() throws FileNotFoundException, ValidationException {
//        File file = new File(fName);
//        Scanner sc = new Scanner(file);
//        while (sc.hasNextLine()) {
//            String[] splited = sc.nextLine().split(" ");
//            save(new Student(Integer.parseInt(splited[0]),splited[1],Integer.parseInt(splited[2]),splited[3],splited[4]));
//        }
//    }
//    public void writeToFile()
//    {
//        if(fName!=null) {
//            try {
//                PrintWriter pw = new PrintWriter(new File(fName));
//                for (Student st : findAll()) {
//                    pw.println(st.getId() + " " + st.getNume() + " " + st.getGrupa() + " " + st.getEmail() + " " + st.getCadruDidactic());
//                }
//                pw.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * reaf from file
     */
    public void readFromFile()  {
        if(new File(this.fName).length()==0)
        {
            return;
        }
        try {
            File inputFile = new File(fName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Student");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String id=eElement.getAttribute("id");
                    String nume=eElement.getElementsByTagName("Nume").item(0).getTextContent();
                    String grupa=eElement.getElementsByTagName("Grupa").item(0).getTextContent();
                    String email=eElement.getElementsByTagName("Email").item(0).getTextContent();
                    String cadru=eElement.getElementsByTagName("Cadru_didactic").item(0).getTextContent();

                    save(new Student(Integer.parseInt(id),nume,Integer.parseInt(grupa),email,cadru));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * write to file
     */
    public void writeToFile()
    {
        try
        {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("Studenti");
            doc.appendChild(rootElement);

            for (Student st : findAll()) {
                // supercars element
                Element studenti = doc.createElement("Student");
                rootElement.appendChild(studenti);

                // setting attribute to element
                Attr attr = doc.createAttribute("id");
                attr.setValue(Integer.toString(st.getId()));
                studenti.setAttributeNode(attr);

                // carname element
                Element studentName = doc.createElement("Nume");
                studentName.appendChild(doc.createTextNode(st.getNume()));
                studenti.appendChild(studentName);

                Element studentGrupa = doc.createElement("Grupa");
                studentGrupa.appendChild(doc.createTextNode(Integer.toString(st.getGrupa())));
                studenti.appendChild(studentGrupa);

                Element studentEmail = doc.createElement("Email");
                studentEmail.appendChild(doc.createTextNode(st.getEmail()));
                studenti.appendChild(studentEmail);

                Element studentCadru = doc.createElement("Cadru_didactic");
                studentCadru.appendChild(doc.createTextNode(st.getCadruDidactic()));
                studenti.appendChild(studentCadru);

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fName));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
