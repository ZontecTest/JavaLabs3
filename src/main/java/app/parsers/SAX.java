package app.parsers;

import app.data.Person;
import app.servlets.AddServlet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SAX extends DefaultHandler {
    Person tmp = null;
    private StringBuilder data = null;
    boolean bFirstName = false;
    boolean bSecondName = false;
    boolean bMiddleName = false;
    boolean bCount = false;
    boolean bInteger = false;
    public ArrayList<Person> persons;

    public SAX(){
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