package org.componenthaus.usecases.searchcomponents;

import org.componenthaus.search.SearchService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class SearchComponentsController extends SimpleFormController implements InitializingBean {
    private SearchService searchService;

    public SearchComponentsController() {
        setCommandClass(Object.class);
    }

    public void afterPropertiesSet() throws Exception {
        if ( searchService == null ) {
            throw new ApplicationContextException("Must set property 'searchService' on " + getClass());
        }
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws ServletException, IOException {
        Collection results = null;
        try {
            results = searchService.search(request.getParameter("query"));
        } catch (SearchService.Exception sse) {
            throw new ServletException("Exception performing search",sse);
        }
        System.out.println("Search result size = " + results.size());
        return new ModelAndView("searchResultsView","results",results);
    }
}
