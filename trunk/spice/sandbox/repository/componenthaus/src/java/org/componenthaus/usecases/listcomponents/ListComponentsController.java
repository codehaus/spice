package org.componenthaus.usecases.listcomponents;

import org.componenthaus.repository.api.ComponentRepository;
import org.prevayler.Prevayler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ListComponentsController extends AbstractController {
    private Prevayler prevayler = null;

    public void setPrevayler(Prevayler prevayler) {
        this.prevayler = prevayler;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final ComponentRepository repository = (ComponentRepository) prevayler.system();
        final Collection modelObject = repository.listComponents();
        return new ModelAndView("listComponentsView","components",modelObject);
    }


}
