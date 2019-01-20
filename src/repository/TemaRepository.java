package repository;

import domain.Student;
import domain.Tema;
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

public class TemaRepository extends AbstractRepository<Integer, Tema> {

    private String fName;

    /**
     *
     * @param v - the validator
     * @param fName - the file for homework
     * @throws FileNotFoundException -maybe we enter an incorrect file name
     * @throws ValidationException -maybe the data in not fit
     */
    public TemaRepository(Validator<Tema> v,String fName) throws FileNotFoundException, ValidationException {
        super(v);
        this.fName=fName;
        readFromFile();
    }

    public TemaRepository(Validator<Tema> v) {
        super(v);
    }

    /**
     *
     * @throws FileNotFoundException -maybe we enter an incorrect file name
     * @throws ValidationException -maybe the data in not fit
     */
//    public void readFromFile() throws FileNotFoundException, ValidationException {
//        File file = new File(fName);
//        Scanner sc = new Scanner(file);
//        while (sc.hasNextLine()) {
//            String[] splited = sc.nextLine().split(" ");
//            save(new Tema(Integer.parseInt(splited[0]),splited[1],Integer.parseInt(splited[2]),Integer.parseInt(splited[3])));
//        }
//
//    }
    public void readFromFile() throws FileNotFoundException, ValidationException {
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
            NodeList nList = doc.getElementsByTagName("Tema");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String id=eElement.getAttribute("id");
                    String descriere=eElement.getElementsByTagName("Descriere").item(0).getTextContent();
                    String due=eElement.getElementsByTagName("Due").item(0).getTextContent();
                    String given=eElement.getElementsByTagName("Given").item(0).getTextContent();

                    save(new Tema(Integer.parseInt(id),descriere,Integer.parseInt(due),Integer.parseInt(given)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
//    public void writeToFile()
//    {
//        if(fName!=null) {
//            try {
//                PrintWriter pw = new PrintWriter(new File(fName));
//                for (Tema t : findAll()) {
//                    pw.println(t.getId() + " " + t.getDescriere() + " " + t.getDue() + " " + t.getGiven());
//                }
//                pw.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
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
            Element rootElement = doc.createElement("Teme");
            doc.appendChild(rootElement);

            for (Tema st : findAll()) {
                Element teme = doc.createElement("Tema");
                rootElement.appendChild(teme);

                // setting attribute to element
                Attr attr = doc.createAttribute("id");
                attr.setValue(Integer.toString(st.getId()));
                teme.setAttributeNode(attr);

                Element descriere = doc.createElement("Descriere");
                descriere.appendChild(doc.createTextNode(st.getDescriere()));
                teme.appendChild(descriere);

                Element due = doc.createElement("Due");
                due.appendChild(doc.createTextNode(Integer.toString(st.getDue())));
                teme.appendChild(due);

                Element given = doc.createElement("Given");
                given.appendChild(doc.createTextNode(Integer.toString(st.getGiven())));
                teme.appendChild(given);

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