package org.componenthaus.maven;

import junit.framework.TestCase;
import testdata.poms.PomLoader;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.xml.sax.InputSource;

public class PomReaderImplTestCase extends TestCase {

    public void testCanReadPom() throws IOException, DocumentException {
        final InputSource inputsource = PomLoader.loadPom("/picoPom.xml");
        final PomReaderImpl reader = new PomReaderImpl();
        final Pom pom = reader.readPom(inputsource);
        assertEquals("picocontainer",pom.getId());
        assertEquals("1.0-beta-2-SNAPSHOT",pom.getCurrentVersion());
    }
}
