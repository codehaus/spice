package jcontainer.usecases.addcomponent;

import com.interface21.web.servlet.ModelAndView;
import jcontainer.Repository;
import jcontainer.api.Component;
import jcontainer.common.PrevaylerFormController;
import jcontainer.common.RepositoryCommand;
import jcontainer.exceptions.ActorException;
import jcontainer.impl.JContainerFactory;
import org.prevayler.Prevayler;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.StringTokenizer;

public class AddComponentController extends PrevaylerFormController {
    public AddComponentController() {
        setCommandClass(FormData.class);
    }

    protected ModelAndView onSubmit(Prevayler prevayler, JContainerFactory factory, Object o) throws Exception {
        final FormData formData = (FormData) o;
        final Component component = factory.createComponent(formData.getComponentName(), formData.getVersion(), formData.getTheInterface());
        prevayler.executeCommand(new AddComponentCommand(formData.getCategory(), component));
        return null;
    }

    protected String formPurpose() {
        return "Add a component to the repository";
    }

    public static class FormData {
        private String theInterface = null;
        private String category = null;
        private String version = null;

        public String getTheInterface() {
            return theInterface;
        }

        public void setTheInterface(String theInterface) {
            this.theInterface = theInterface;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getComponentName() {
            String result = null;
            if (theInterface != null) {
                result = extractInterfaceName(theInterface);
            }
            return result;
        }

        private String extractInterfaceName(String javadoc) {
            String result = null;
            Pattern p = Pattern.compile(".* interface (\\w+).*\\r*\\n*");
            final StringTokenizer lines = new StringTokenizer(javadoc,"\n");
            while(result == null && lines.hasMoreTokens()) {
                final String line = lines.nextToken();
                System.out.println("Looking at line #" + line + "#");
                final Matcher matcher = p.matcher(line);
                System.out.println("matcher.matches() = " + matcher.matches());
                if ( matcher.matches() && matcher.groupCount() == 1) {
                    result = matcher.group(1);
                }
            }
            return result;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    private static class AddComponentCommand extends RepositoryCommand {
        private final Component component;
        private final String categoryName;

        public AddComponentCommand(final String categoryName, final Component component) {
            this.component = component;
            this.categoryName = categoryName;
        }

        public Serializable execute(final Repository rep) throws Exception {
            if (!rep.containsCategory(categoryName)) {
                throw new ActorException(categoryName + " is not a valid category name");
            }
            if (rep.contains(component)) {
                throw new ActorException("Component " + component.getName() + " already registered.");
            }
            rep.add(categoryName, component);
            return null;
        }
    }
}

