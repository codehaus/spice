package jcontainer.usecases.addcomponent;

import com.interface21.web.servlet.ModelAndView;
import jcontainer.Repository;
import jcontainer.common.SimplePrevaylerController;
import org.prevayler.Prevayler;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class ShowComponentRegistrationFormController extends SimplePrevaylerController {

    protected ModelAndView handleRequest(Prevayler p, HttpServletRequest httpServletRequest) {
        final Collection categories = ((Repository)p.system()).getCategories();
        return new ModelAndView("addComponentForm","categories",categories);
    }

    protected String formPurpose() {
        return "Show the form that allows registration of a component";
    }
}