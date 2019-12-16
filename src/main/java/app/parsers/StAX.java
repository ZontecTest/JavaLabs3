package app.parsers;

import app.data.Person;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class StAX {

    private static ArrayList<Person> persons;

    public StAX(){
        persons = new ArrayList<Person>();
    }

    public ArrayList<Person> getParsedData(){
        return persons;
    }

    public static void parseStAX(String xmlPath) throws XMLStreamException, FileNotFoundException {
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
}
