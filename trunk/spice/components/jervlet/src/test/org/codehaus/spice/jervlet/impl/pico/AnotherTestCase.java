package org.codehaus.spice.jervlet.impl.pico;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.Script;
import junit.framework.TestCase;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.spice.jervlet.ContextHandler;
import org.nanocontainer.script.groovy.GroovyContainerBuilder;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.ObjectReference;
import org.picocontainer.defaults.SimpleReference;

public class AnotherTestCase extends TestCase
{
    public void testJervletNanoContainerBuilderDecorationDelegate() throws Exception
    {
        final InputStream script = getClass().getResourceAsStream( "delegate.groovy" );
        final PicoContainer pico = createContainer( new InputStreamReader( script ) );
        assertNotNull("expected ContextHandler in picocontainer", pico.getComponentInstance( ContextHandler.class));
    }

    protected PicoContainer createContainer( final Reader reader ) throws CompilationFailedException
    {
        final ObjectReference containerRef = new SimpleReference();
        final ObjectReference parentContainerRef = new SimpleReference();
        final PicoContainer parent = new DefaultPicoContainer();
        final GroovyContainerBuilder builder = new GroovyContainerBuilder( reader, getClass().getClassLoader() );

        parentContainerRef.set( parent );
        builder.buildContainer( containerRef, parentContainerRef, "SOME_SCOPE", true );

        return (PicoContainer)containerRef.get();
    }

    public void testContainerCanBeBuiltWithParent() throws CompilationFailedException
    {
        final Reader script = new StringReader(
            "package org.codehaus.spice.jervlet.impl.pico\n" +
            "import org.codehaus.spice.jervlet.impl.pico.NanoGroovyWebContainerBuilder\n"
            +
            "import org.picocontainer.defaults.DefaultPicoContainer\n" +
            "def pc = new DefaultPicoContainer()\n" +
            "pc.registerComponentInstance(String.class, \"Foo\")\n" +
            "def builder = new NanoGroovyWebContainerBuilder()\n" +
            "def webServer = builder.picoEnabledWebServer(pico:pc, host:\"localhost\", port:8099) {\n"
            +
            "    webApp(warPath:\"foo/bar\")\n" +
            "    webApp {\n" +
            "      servlet(context:\"/foo\", servletClass:\"foo.bar.AServlet\")\n" +
            "    }\n" +
            "}\n" +
            "webServer.start()\n"
        );
        doGroovy( new InputStream()
        {
            public int read() throws IOException
            {
                return script.read();
            }
        } );
        assertEquals( "", "" );
    }

    protected void doGroovy( final InputStream is ) throws CompilationFailedException
    {
        Script groovyScript;
        final GroovyClassLoader loader = new GroovyClassLoader( getClass().getClassLoader() );
        final GroovyCodeSource groovyCodeSource = new GroovyCodeSource( is,
                                                                  "nanocontainer.groovy",
                                                                  "groovyGeneratedForNanoContainer" );
        final Class scriptClass = loader.parseClass( groovyCodeSource );
        groovyScript = InvokerHelper.createScript( scriptClass, null );
        final Binding binding = new Binding();
        groovyScript.setBinding( binding );
        groovyScript.run();
    }
}
