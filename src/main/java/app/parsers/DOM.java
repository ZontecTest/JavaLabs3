package app.parsers;

import app.data.Person;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

public class DOM {

    private static ArrayList<Person> persons;

    public DOM(){
        persons = new ArrayList<Person>();
    }

    public void parseDOM(String xmlPath){
        try {
            File inputFile = new File(xmlPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("user");
            Person tmp = new Person();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                tmp = new Person();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    tmp.setFirstName(eElement.getElementsByTagName("firstName")
                            .item(0)
                            .getTextContent());
                    tmp.setMiddleName(eElement.getElementsByTagName("middleName")
                            .item(0)
                            .getTextContent());
                    tmp.setSecondName(eElement.getElementsByTagName("secondName")
                            .item(0)
                            .getTextContent());
                    tmp.setUid(Integer.parseInt(eElement.getElementsByTagName("uId")
                            .item(0)
                            .getTextContent()));
                    tmp.setCount(eElement.getElementsByTagName("count")
                            .item(0)
                            .getTextContent());
                }
                persons.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Person> getParsedData(){
        return persons;
    }

}
