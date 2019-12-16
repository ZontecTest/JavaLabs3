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

import app.parsers.DOM;
import app.parsers.SAX;
import app.parsers.StAX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import app.xmlManage.xmlManage;
import app.data.Person;

public class AddServlet extends  HttpServlet {

    private ArrayList<Person> persons;

    private final String xmlPath = "d:\\out.xml";
    private final String xsdPath = "d:\\out.xsd";
    public AddServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Random rand = new Random();
        int parserType = rand.nextInt();
        String []parserName = {"SAX", "StAX", "DOM"};

        req.setAttribute("parserType", parserName[parserType = (parserType > 0 ? parserType : parserType * -1) % 3]);

        if(!xmlManage.validateXMLSchema(xsdPath, xmlPath)) {
            req.setAttribute("xmlStatus", "XML is incorrect!");
        }else {
            req.setAttribute("xmlStatus", "XML is correct!");
        }

        persons = new ArrayList<Person>();
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

            SAX handler = new SAX();
            try {
                parser.parse(new File(xmlPath), handler);
                persons = new ArrayList<Person>(handler.persons);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        //StAX
        if(parserType == 1) {
            StAX parser = new StAX();
            try {
                parser.parseStAX(xmlPath);
                persons = parser.getParsedData();
            } catch (XMLStreamException e) {
                e.printStackTrace();
            }
        }
        //DOM
        if(parserType == 2){
            DOM parser = new DOM();
            parser.parseDOM(xmlPath);
            persons = parser.getParsedData();
        }

        req.setAttribute("persons", persons);
        req.getRequestDispatcher("table.jsp").forward(req, resp);
    }
}
