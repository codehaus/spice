package org.codehaus.spice.jervlet.impl.pico;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import junit.framework.TestCase;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.control.CompilationFailedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

public class AnotherTestCase extends TestCase {
    public void testContainerCanBeBuiltWithParent() throws CompilationFailedException {
        final Reader script = new StringReader("" +
                "package org.codehaus.spice.jervlet.impl.pico\n" +
                "import org.codehaus.spice.jervlet.impl.pico.NanoGroovyWebContainerBuilder\n" +
                "import org.picocontainer.defaults.DefaultPicoContainer\n" +
                "def pc = new DefaultPicoContainer()\n" +
                "pc.registerComponentInstance(String.class, \"Foo\")\n" +
                "def builder = new NanoGroovyWebContainerBuilder()\n" +
                "def webServer = builder.picoEnabledWebServer(pico:pc, host:\"localhost\", port:8099) {\n" +
                "    webApp(warPath:\"foo/bar\")\n" +
                "    webApp {\n" +
                "      servlet(context:\"/foo\", servletClass:\"foo.bar.AServlet\")\n" +
                "    }\n" +
                "}\n" +
                "webServer.start()\n"
                );
        doGroovy(new InputStream() {
            public int read() throws IOException {
                return script.read();
            }
        });
        assertEquals("", "");
    }

    protected void doGroovy(InputStream is) throws CompilationFailedException {
        Script groovyScript = null;
        GroovyClassLoader loader = new GroovyClassLoader(getClass().getClassLoader());
        GroovyCodeSource groovyCodeSource = new GroovyCodeSource(is, "nanocontainer.groovy", "groovyGeneratedForNanoContainer");
        Class scriptClass = loader.parseClass(groovyCodeSource);
        groovyScript = InvokerHelper.createScript(scriptClass, null);
        Binding binding = new Binding();
        groovyScript.setBinding(binding);
        Object result = groovyScript.run();
    }


}
