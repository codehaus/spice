package org.componenthaus.spring;

import com.mockobjects.servlet.MockServletContext;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;

public class PicoApplicationContextTestCase extends TestCase {

    public void testAllComponentsAreResolvable() {
        PicoApplicationContext context = new PicoApplicationContextSubClass(null,"");
        context.setServletContext(new MockServletContext());
    }

    private static final class PicoApplicationContextSubClass extends PicoApplicationContext {

        public PicoApplicationContextSubClass(ApplicationContext parent, String nameSpace) {
            super(parent, nameSpace);
        }

        protected void addSpringStuff() {
            //Don't do this, as it requires web.xml.  Too much hassle!
        }
    }


}
