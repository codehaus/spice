package org.componenthaus.maven;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.util.List;

public class PomReaderImpl {

    public Pom readPom(InputSource inputsource) throws DocumentException {
        final PomImpl result = new PomImpl();
        SAXReader xmlReader = new SAXReader();
        final Document doc = xmlReader.read(inputsource);
        result.setId(getString("/project/id", doc));
        result.setCurrentVersion(getString("/project/currentVersion", doc));
        return result;
    }

    private String getString(final String xpath, final Document doc) {
        XPath xpathSelector = DocumentHelper.createXPath(xpath);
        List results = xpathSelector.selectNodes(doc);
        assert  results.size() == 1;
        final Element element = (Element) results.get(0);
        final String stringValue = element.node(0).getStringValue();
        return stringValue;
    }

    private static final class PomImpl implements Pom {
        private String id = null;
        private String currentVersion = null;

        public String getId() {
            return id;
        }

        public void setCurrentVersion(String currentVersion) {
            this.currentVersion = currentVersion;
        }

        public String getCurrentVersion() {
            return currentVersion;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
