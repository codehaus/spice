package org.componenthaus.usecases.searchcomponents;

import org.componenthaus.repository.api.ComponentRepository;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SearchComponentsController extends SimpleFormController implements InitializingBean {
    private static final int HITS_PER_PAGE = 10;
    private SearchService searchService;
    private ComponentRepository repository = null;

    public SearchComponentsController() {
        setCommandClass(Object.class);
    }

    public void afterPropertiesSet() throws Exception {
        if ( searchService == null ) {
            throw new ApplicationContextException("Must set property 'searchService' on " + getClass());
        }
        if ( repository == null ) {
            throw new ApplicationContextException("Must set property 'repository' on " + getClass());
        }
    }

    public void setSearchService(SearchService searchService) {
        this.searchService = searchService;
    }

    public void setRepository(ComponentRepository repository) {
        this.repository = repository;
    }

    protected boolean isFormSubmission(HttpServletRequest request) {
		return "POST".equals(request.getMethod()) || requestHasSearchParameters(request);
	}

    private boolean requestHasSearchParameters(HttpServletRequest request) {
        return  !isEmpty(request.getParameter("query")) &&
                !isEmpty(request.getParameter("beginIndex")) &&
                !isEmpty(request.getParameter("endIndex"));
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws ServletException, IOException {
        final String query = request.getParameter("query");
        final String begin = request.getParameter("beginIndex");
        final String end = request.getParameter("endIndex");
        final List componentIds = new ArrayList();
        final int beginIndex = !isEmpty(begin) ? Integer.parseInt(begin) - 1 : 0;
        final int endIndex = !isEmpty(end) ? Integer.parseInt(end) - 1 : HITS_PER_PAGE - 1;
        int totalMatches = 0;
        try {
            totalMatches = searchService.search(query, beginIndex, endIndex, componentIds);
        } catch (SearchService.Exception sse) {
            throw new ServletException("Exception performing search",sse);
        }
        final Collection components = getComponent(componentIds);
        final Map model = new Hashtable();
        model.put("results",components);
        model.put("query",query);
        model.put("beginIndex",new Integer(beginIndex + 1));
        model.put("endIndex",new Integer(Math.min(totalMatches, endIndex + 1)));
        model.put("totalMatches",new Integer(totalMatches));
        model.put("pages",computePages(totalMatches,HITS_PER_PAGE));
        final int currentPage = ((beginIndex+1) / HITS_PER_PAGE) + 1;
        model.put("currentPage",new Page(currentPage, beginIndex+1,endIndex+1));
        model.put("hitsPerPage",new Integer(HITS_PER_PAGE));

        return new ModelAndView("searchResultsView",model);
    }

    private Collection computePages(int totalMatches, int resultsPerPage) {
        int numPages = (totalMatches / resultsPerPage);
        if ( (totalMatches % resultsPerPage ) != 0 ) {
            numPages++;
        }
        final Collection result = new ArrayList();
        for(int i=0;i<numPages;i++) {
            result.add(new Page(i+1,(i*resultsPerPage)+1,((i+1)*resultsPerPage)+1));
        }
        return result;
    }

    private boolean isEmpty(String s) {
        return s == null || s.equals("");
    }

    private Collection getComponent(List componentIds) {
        final Collection results = new ArrayList();
        for(Iterator i=componentIds.iterator();i.hasNext();) {
            final String componentId = (String) i.next();
            results.add(repository.getComponent(componentId));
        }
        return results;
    }

    public static final class Page {
        private int id;
        private int beginIndex;
        private int endIndex;

        public Page(int id, int beginIndex, int endIndex) {
            this.id = id;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
        }

        public int getId() {
            return id;
        }

        public int getBeginIndex() {
            return beginIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }
    }
}
