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

    public ListComponentsController(ComponentRepository repository) {
        this.repository = repository;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("repository = " + repository);
        final Collection modelObject = repository.listComponents();
        System.out.println("Num components " + modelObject.size());
        return new ModelAndView("listComponentsView","components",modelObject);
    }
}
