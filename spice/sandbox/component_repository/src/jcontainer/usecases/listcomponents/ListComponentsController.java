package jcontainer.usecases.listcomponents;

import com.interface21.web.servlet.ModelAndView;
import jcontainer.Repository;
import jcontainer.common.SimplePrevaylerController;
import org.prevayler.Prevayler;

import javax.servlet.http.HttpServletRequest;

public class ListComponentsController extends SimplePrevaylerController {

    protected ModelAndView handleRequest(Prevayler p, HttpServletRequest httpServletRequest) {
        return new ModelAndView("listComponentsView","components",((Repository)p.system()).getComponents());
    }

    protected String formPurpose() {
        return "List all components in repository";
    }
}