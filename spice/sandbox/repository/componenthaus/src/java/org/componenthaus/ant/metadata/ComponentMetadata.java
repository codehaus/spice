package org.componenthaus.ant.metadata;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.xml.dom.DomXMLReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collections;

public class ComponentMetadata {
    //TODO For now I am going to deal with only one interface, until I figure out what to do with xstream
    //private InterfaceMetadata [] interfaces; //TODO Now a map because XStream cannot yet deal with Maps
    private InterfaceMetadata interfaceMetadata = null;

    public ComponentMetadata() {
        //this.interfaces = new InterfaceMetadata[0];
    }

    public static ComponentMetadata fromXml(File metadataFile) throws IOException, ParserConfigurationException, SAXException {
        final DomXMLReader xmlReader = new DomXMLReader(buildDocument(metadataFile));
        return (ComponentMetadata) new XStream().fromXML(xmlReader);
    }

    public static ComponentMetadata fromXml(String xml) {
        return (ComponentMetadata) new XStream().fromXML(xml);
    }

    private static Document buildDocument(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        return document;
    }

    public InterfaceMetadata getInterfaceMetadataForName(final String name) {
        if ( interfaceMetadata.getFullyQualifiedName().equals(name)) {
            return interfaceMetadata;
        }
        return null;
/*
        InterfaceMetadata result = null;
        int index = 0;
        while ( result == null && index < interfaces.length) {
            final InterfaceMetadata interfaceMetadata = interfaces[index++];
            if ( interfaceMetadata.getFullyQualifiedName().equals(name)) {
                result = interfaceMetadata;
            }
        }
        return result;
*/
    }

    public int numInterfaces() {
        return interfaceMetadata != null ? 1 : 0;
    }

    public void addInterface(InterfaceMetadata interfaceMetadata) {
        this.interfaceMetadata = interfaceMetadata;
        /*List temp = new ArrayList(Arrays.asList(interfaces));
        temp.add(interfaceMetadata);
        interfaces = new InterfaceMetadata[temp.size()];
        interfaces = (InterfaceMetadata []) temp.toArray(interfaces);*/
    }

    public Iterator getInterfaces() {
        return Collections.singletonList(interfaceMetadata).iterator();
    }
}
