package org.componenthaus.spring;

import junit.framework.TestCase;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.MessageSourceResolvable;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ArrayList;

import com.mockobjects.servlet.MockServletContext;

public class AbstractPicoApplicationContextTestCase extends TestCase {

    public void testCanSetAndGetParent() {
        PicoApplicationContextForTest parent = new PicoApplicationContextForTest(null, "");
        PicoApplicationContextForTest child = new PicoApplicationContextForTest(parent, "");
        assertSame(parent, child.getParent());
    }

    public void testDisplayNameReturnsSomething() {
        assertNotNull(new PicoApplicationContextForTest(null, "").getDisplayName());
    }

    public void testStartupTimeIsSetOnConstruction() {
        long startupTime = new PicoApplicationContextForTest(null, "").getStartupDate();
        long now = System.currentTimeMillis();
        assertTrue((now - startupTime) < 1000);
    }

    public void testContextOptionsIsNotNull() {
        assertNotNull(new PicoApplicationContextForTest(null, "").getOptions());
    }

    public void testRefreshWorks() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        context.setExpectedRegisterBeanCalls(1);
        context.refresh();
        context.verify();
    }

    public void testCanClose() {
        new PicoApplicationContextForTest(null, "").close();
    }

    public void testGetResourceAsStreamDealsWithDodgyUrl() throws IOException {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);

        try {
            context.getResourceAsStream("lkjlkj");
            fail("Did not get expected exception");
        } catch (FileNotFoundException expected) {
            //expected
        }
    }

    public void testGetResourceAsStreamDealsWithProperUrl() throws IOException {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        InputStream in = context.getResourceAsStream("file:///.");
        assertNotNull(in);
        in.close();
    }

    public void testGetResourceBasePath() {
        assertNotNull(new PicoApplicationContextForTest(null, null).getResourceBasePath());
    }

    public void testCanGetDefaultMessage() {
        String defaultMessage = "default";
        assertSame(defaultMessage, new PicoApplicationContextForTest(null, null).getMessage("", new Object[0], defaultMessage, new Locale("en")));
    }

    public void testGetMessageThrowsNoSuchMessageException() {
        try {
            new PicoApplicationContextForTest(null, null).getMessage("", new Object[0], new Locale("en"));
            fail("Did not get expected exception");
        } catch (NoSuchMessageException expected) {
            //expected
        }
    }

    public void testGetMessageThrowsNoSuchMessageExceptionUsingMessageResolver() throws NoSuchMessageException {
        String message = new PicoApplicationContextForTest(null, null).getMessage(new MessageSourceResolvable() {
            public String[] getCodes() {
                return new String[]{""};
            }

            public Object[] getArgs() {
                return new Object[0];
            }

            public String getDefaultMessage() {
                return "";
            }
        }, new Locale("en"));
        assertEquals("", message);
    }

    public void testCanCountBeanDefinitions() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        assertEquals(0, context.getBeanDefinitionCount());
        context.registerBean(new Object());
        assertEquals(1,context.getBeanDefinitionCount());
    }

    public void testCanGetComponentDefinitionNames() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        assertEquals(0,context.getBeanDefinitionNames().length);
        String name = "name";
        context.registerBean(name,new Object());
        final String[] beanDefinitionNames = context.getBeanDefinitionNames();
        assertEquals(1,beanDefinitionNames.length);
        assertEquals(name,beanDefinitionNames[0]);
    }

    public void testCanGetComponentDefinitionNamesForType() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        assertEquals(0,context.getBeanDefinitionNames(Object.class).length);
        String name = "name";
        context.registerBean(name,new ArrayList());
        String[] beanDefinitionNames = context.getBeanDefinitionNames(ArrayList.class);
        assertEquals(1,beanDefinitionNames.length);
        assertEquals(name,beanDefinitionNames[0]);
        context.registerBean("who cares", new Object());
        beanDefinitionNames = context.getBeanDefinitionNames(ArrayList.class);
        assertEquals(1,beanDefinitionNames.length);
    }

    public void testCanGetBeanByName() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        String name = "name";
        final Object object = new Object();
        context.registerBean(name,object);
        assertSame(object,context.getBean(name));
    }

    public void testThrowsNoSuchBeanDefinitionExceptionWhenNoBean() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        try {
            context.getBean("Will not exist");
            fail("Did not get expected exception");
        } catch (NoSuchBeanDefinitionException expected) {
            //expected
        }
    }

    public void testThrowsNoSuchBeanDefinitionExceptionWhenNoBeanByType() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        try {
            context.getBean("Will not exist", Object.class);
            fail("Did not get expected exception");
        } catch (NoSuchBeanDefinitionException expected) {
            //expected
        }
    }

    public void testThrowsBeanNotOfRequiredTypeException() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        final String name = "name";
        final Object object = new Object();
        context.registerBean(name,object);
        try {
            context.getBean(name, ArrayList.class);
            fail("Did not get expected exception");
        } catch (BeanNotOfRequiredTypeException expected) {
            //expected
        }
    }

    public void testCanGetBeanByNameAndType() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        final String name = "name";
        final ArrayList object = new ArrayList();
        context.registerBean(name,object);
        assertSame(object,context.getBean(name, ArrayList.class));
    }

    public void testBeansAreAlwaysSingletons() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        assertTrue(context.isSingleton(null));
    }

    public void testNeverUsesAliases() {
        assertEquals(0,(new PicoApplicationContextForTest(null, null)).getAliases(null).length);
    }

    public void testHasNoParentBeanFactory() {
        assertNull((new PicoApplicationContextForTest(null, null)).getParentBeanFactory());
    }

    public void testCanShareObjects() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        assertNull(context.sharedObject(null));
        Object object = new Object();
        context.shareObject("key", object);
        assertSame(object,context.sharedObject("key"));
        context.removeSharedObject("key");
        assertNull(context.sharedObject("key"));
    }

    public void testCanSetAndGetServletContext() {
        PicoApplicationContextForTest context = new PicoApplicationContextForTest(null, null);
        ServletContext servletContext = new MockServletContext();
        assertNull(context.getServletContext());
        context.setServletContext(servletContext);
        assertSame(servletContext,context.getServletContext());
    }

    public void testDoesNotUseATheme() {
        assertNull(new PicoApplicationContextForTest(null, null).getTheme(null));
    }

    private static final class PicoApplicationContextForTest extends AbstractPicoApplicationContext {
        private int actualRegisterBeansCalls = 0;
        private int actualSetServletContextCalls = 0;
        private int expectedRegisterBeanCalls = 0;
        private int expectedSetServletContextCalls = 0;

        public PicoApplicationContextForTest(ApplicationContext parent, String nameSpace) {
            super(parent, nameSpace);
        }

        public void registerBean(Object o) {
            pico.registerComponentInstance(o);
        }

        public void registerBean(String name,Object o) {
            pico.registerComponentInstance(name,o);
        }

        protected void registerBeans() throws BeansException {
            actualRegisterBeansCalls++;
        }

        public void setServletContext(ServletContext servletContext) throws ApplicationContextException {
            actualSetServletContextCalls++;
            this.servletContext = servletContext;
        }

        public void setExpectedRegisterBeanCalls(int expectedRegisterBeanCalls) {
            this.expectedRegisterBeanCalls = expectedRegisterBeanCalls;
        }

        public void setExpectedSetServletContextCalls(int expectedSetServletContextCalls) {
            this.expectedSetServletContextCalls = expectedSetServletContextCalls;
        }

        public void verify() {
            assertEquals(expectedRegisterBeanCalls, actualRegisterBeansCalls);
            assertEquals(expectedSetServletContextCalls, actualSetServletContextCalls);
        }
    }

}
