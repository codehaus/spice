package org.componenthaus.usecases.listcomponents;

import org.componenthaus.repository.api.ComponentRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ListComponentsController extends AbstractController {
    private final ComponentRepository repository;
    final static String MODEL_NAME = "components";

    public ListComponentsController(ComponentRepository repository) {
        this.repository = repository;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Collection modelObject = repository.listComponents();
        return new ModelAndView("listComponentsView",MODEL_NAME,modelObject);
    }
}
