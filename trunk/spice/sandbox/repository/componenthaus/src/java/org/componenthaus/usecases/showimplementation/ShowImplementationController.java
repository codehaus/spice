package org.componenthaus.usecases.showimplementation;

import org.componenthaus.repository.api.ComponentRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowImplementationController extends AbstractController {
    private final ComponentRepository repository;

    public ShowImplementationController(final ComponentRepository repository) {
        this.repository = repository;
    }
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new ModelAndView("showImplementationView","impl",
                repository.getImplementation(request.getParameter("id"),request.getParameter("implId")));
    }
}