package org.componenthaus.ant.metadata;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.xml.dom.DomXMLReader;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComponentMetadata {
    private Map interfaces = null;

    public ComponentMetadata() {
        this.interfaces = new HashMap();
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
        return (InterfaceMetadata) interfaces.get(name);
    }

    public int numInterfaces() {
        return interfaces.size();
    }

    public void addInterface(InterfaceMetadata interfaceMetadata) {
        interfaces.put(interfaceMetadata.getFullyQualifiedName(), interfaceMetadata);
    }

    public Iterator getInterfaces() {
        return interfaces.values().iterator();
    }

    public String toXml() {
        return ((new XStream()).toXML(this));
    }

    public boolean equals(Object o) {
        if (!(o instanceof ComponentMetadata)) {
            return false;
        }
        ComponentMetadata rhs = (ComponentMetadata) o;
        return new EqualsBuilder()
                .append(interfaces, rhs.interfaces)
                .isEquals();
    }
}
