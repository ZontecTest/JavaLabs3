package app.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class AddServlet extends  HttpServlet {

    public class Person{
        private String count;
        private int uId;
        private String firstName;
        private String secondName;
        private String middleName;

        public Person(){
            count = "0";
            uId = 0;
            firstName = secondName = middleName = "NAN";
        }

        public Person(int uId, String firstName, String secondName, String middleName, String count){
            uId = uId;
            firstName = firstName;
            secondName = secondName;
            middleName = middleName;
            count = count;
        }

        public int getUid(){ return uId;}
        public String getCount(){ return count;}
        public String getFirstName(){ return firstName;}
        public String getSecondName(){ return secondName;}
        public String getMiddleName(){ return middleName;}
        public void setCount(String count) { this.count = count;}
        public void setUid(int uId) { this.uId = uId; }
        public void setFirstName(String name) { firstName = name;}
        public void setSecondName(String name) { secondName = name;}
        public void setMiddleName(String name) { middleName = name;}
    }

    private ArrayList<Person> persons;

    private final String xmlPath = "<xml path>";
    private final String xsdPath = "<xsd path>";

    public class SAXparser extends DefaultHandler {
        Person tmp = null;
        private StringBuilder data = null;
        boolean bFirstName = false;
        boolean bSecondName = false;
        boolean bMiddleName = false;
        boolean bCount = false;
        boolean bInteger = false;

        SAXparser(){
            persons = new ArrayList<Person>();
            tmp = new Person();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equalsIgnoreCase("user")) {
                tmp = new Person();
            }
            if (qName.equalsIgnoreCase("firstName")) {
                bFirstName = true;
            } else if (qName.equalsIgnoreCase("middleName")) {
                bMiddleName = true;
            } else if (qName.equalsIgnoreCase("secondName")) {
                bSecondName = true;
            } else if (qName.equalsIgnoreCase("uId")) {
                bInteger = true;
            }else if (qName.equalsIgnoreCase("count")) {
                bCount = true;
            }
            data = new StringBuilder();

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (bInteger) {
                // age element, set Employee age
                tmp.setUid(Integer.parseInt(data.toString()));
                bInteger = false;
            } else if (bFirstName) {
                tmp.setFirstName(data.toString());
                bFirstName = false;
            } else if (bSecondName) {
                tmp.setSecondName(data.toString());
                bSecondName = false;
            } else if (bMiddleName) {
                tmp.setMiddleName(data.toString());
                bMiddleName = false;
            }else if (bCount) {
                tmp.setCount(data.toString());
                bCount = false;
            }
            if (qName.equalsIgnoreCase("user")) {
                // add Employee object to list
                persons.add(tmp);
            }
        }

        @Override
        public void characters(char ch[], int start, int length) throws SAXException {
            data.append(new String(ch, start, length));
        }
    }


    public AddServlet() {

    }

    private boolean validateXMLSchema(String xsdPath, String xmlPath) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (IOException e) {
            //logger.error("Exception: " + e.getMessage());
            return false;
        } catch (org.xml.sax.SAXException e1) {
            //logger.error("SAX Exception: " + e1.getMessage());
            return false;
        }
        return true;
    }

    private void parseStAX(String xmlPath) throws XMLStreamException, FileNotFoundException {
        boolean bFirstName = false;
        boolean bSecondName = false;
        boolean bMiddleName = false;
        boolean bCount = false;
        boolean bInteger = false;
        Person tmp = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader =
                factory.createXMLEventReader(new FileReader(xmlPath));

        while(eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            switch(event.getEventType()) {

                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    if (qName.equalsIgnoreCase("user")) {
                        tmp = new Person();
                    }
                    if (qName.equalsIgnoreCase("firstName")) {
                        bFirstName = true;
                    } else if (qName.equalsIgnoreCase("middleName")) {
                        bMiddleName = true;
                    } else if (qName.equalsIgnoreCase("secondName")) {
                        bSecondName = true;
                    } else if (qName.equalsIgnoreCase("uId")) {
                        bInteger = true;
                    }else if (qName.equalsIgnoreCase("count")) {
                        bCount = true;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (bInteger) {
                        // age element, set Employee age
                        tmp.setUid(Integer.parseInt(characters.toString()));
                        bInteger = false;
                    } else if (bFirstName) {
                        tmp.setFirstName(characters.toString());
                        bFirstName = false;
                    } else if (bSecondName) {
                        tmp.setSecondName(characters.toString());
                        bSecondName = false;
                    } else if (bMiddleName) {
                        tmp.setMiddleName(characters.toString());
                        bMiddleName = false;
                    }else if (bCount) {
                        tmp.setCount(characters.toString());
                        bCount = false;
                    }
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();

                    if(endElement.getName().getLocalPart().equalsIgnoreCase("user")) {
                        persons.add(tmp);
                    }
                    break;
            }
        }
}

    private void parseDOM(String xmlPath){

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if(!validateXMLSchema(xsdPath, xmlPath)) {
            req.setAttribute("xmlStatus", "XML is incorrect!");
        }else {
            req.setAttribute("xmlStatus", "XML is correct!");
        }

        persons = new ArrayList<Person>();

        Random rand = new Random();
        int parserType = rand.nextInt();
        String []parserName = {"SAX", "StAX", "DOM"};
        parserType = parserType > 0 ? parserType : parserType * -1;
        parserType %= 3;
        req.setAttribute("parserType", parserName[parserType]);
        //SAX
        if(parserType == 0) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = null;
            try {
                parser = factory.newSAXParser();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            SAXparser handler = new SAXparser();
            try {
                parser.parse(new File(xmlPath), handler);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        //StAX
        if(parserType == 1){
            try {
                parseStAX(xmlPath);
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        //DOM
        if(parserType == 2){
            parseDOM(xmlPath);
        }

        req.setAttribute("persons", persons);
        req.getRequestDispatcher("table.jsp").forward(req, resp);
    }
}
