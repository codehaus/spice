package org.nanocontainer.script.groovy

import org.picocontainer.defaults.ComponentParameter
import org.picocontainer.defaults.UnsatisfiableDependenciesException

import org.nanocontainer.script.groovy.NanoContainerBuilder
import org.nanocontainer.testmodel.DefaultWebServerConfig
import org.nanocontainer.testmodel.WebServer
import org.nanocontainer.testmodel.WebServerConfig
import org.nanocontainer.testmodel.WebServerConfigBean
import org.nanocontainer.testmodel.WebServerImpl
import java.io.File

class ThinkingOutLoudNanoJervletTestCase extends GroovyTestCase {



    void testSomething() {

        pc = new DefaultPicoContainer()
        pc.reg(ADependency.class)

        builder = new NanoJervletBuilder()

        webServer = builder.picoEnabledWebServer(pico:pc, host:"foo.com", port:8080) {
            webApp(warPath:"foo/bar")
            webApp {
              servlet(context:"/foo", class:"foo.bar.AServlet")
            }
        }

        webServer.start()

        assertEquals("?","?")
    }


}