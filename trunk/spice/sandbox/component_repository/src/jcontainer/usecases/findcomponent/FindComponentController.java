package jcontainer.usecases.findcomponent;

import com.interface21.web.servlet.ModelAndView;
import jcontainer.Repository;
import jcontainer.common.PrevaylerFormController;
import jcontainer.impl.JContainerFactory;
import org.prevayler.Prevayler;

import java.util.Collection;

public class FindComponentController extends PrevaylerFormController {
    public FindComponentController() {
        setCommandClass(FindParameters.class);
    }

    protected ModelAndView onSubmit(Prevayler prevayler, JContainerFactory factory, Object formData) throws Exception {
        final FindParameters findParameters = (FindParameters) formData;
        String componentNameRegexp = findParameters.componentNameRegexp;
        if ( componentNameRegexp == null || "".equals(componentNameRegexp)) {
            componentNameRegexp = ".*";
        }
        final Collection matches = ((Repository)prevayler.system()).findComponent(findParameters.categoryName, componentNameRegexp);
        return new ModelAndView("listComponentsView","components",matches);
    }

    protected String formPurpose() {
        return "Search for a component in the repository";
    }

    public static final class FindParameters {
        private String categoryName = null;
        private String componentNameRegexp = null;

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setComponentNameRegexp(String componentNameRegexp) {
            this.componentNameRegexp = componentNameRegexp;
        }
    }
}