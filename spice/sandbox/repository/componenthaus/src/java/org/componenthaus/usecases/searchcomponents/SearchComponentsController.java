package org.componenthaus.usecases.searchcomponents;

import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.search.SearchService;
import org.componenthaus.usecases.common.MissingRequestParameterServletException;
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

public class SearchComponentsController extends SimpleFormController {
    static final String QUERY_PARAMETER_NAME = "query";
    static final String BEGIN_INDEX_PARAMETER_NAME = "beginIndex";
    static final String END_INDEX_PARAMETER_NAME = "endIndex";
    static final String RESULTS_BEAN_NAME = "results";
    static final String TOTAL_MATCHES_BEAN_NAME = "totalMatches";
    static final String PAGES_BEAN_NAME = "pages";
    static final String CURRENT_PAGE_BEAN_NAME = "currentPage";
    static final int HITS_PER_PAGE = 10;

    private final SearchService searchService;
    private final ComponentRepository repository;

    public SearchComponentsController(final ViewConfiguration viewConfiguration,
                                      final SearchService searchService,
                                      final ComponentRepository repository) {
        setFormView(viewConfiguration.getFormView());
        setCommandClass(Object.class);
        this.searchService = searchService;
        this.repository = repository;
    }

    protected boolean isFormSubmission(HttpServletRequest request) {
		return "POST".equals(request.getMethod()) || requestHasSearchParameters(request);
	}

    private boolean requestHasSearchParameters(HttpServletRequest request) {
        return  !isEmpty(request.getParameter(QUERY_PARAMETER_NAME)) &&
                !isEmpty(request.getParameter(BEGIN_INDEX_PARAMETER_NAME)) &&
                !isEmpty(request.getParameter(END_INDEX_PARAMETER_NAME));
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws ServletException, IOException {
        final String query = request.getParameter(QUERY_PARAMETER_NAME);
        if ( isEmpty(query)) {
            throw new MissingRequestParameterServletException(QUERY_PARAMETER_NAME);
        }
        final String begin = request.getParameter(BEGIN_INDEX_PARAMETER_NAME);
        final String end = request.getParameter(END_INDEX_PARAMETER_NAME);
        final List componentIds = new ArrayList();
        final int beginIndex = !isEmpty(begin) ? Integer.parseInt(begin) : 1;
        final int endIndex = !isEmpty(end) ? Integer.parseInt(end) : HITS_PER_PAGE;
        int totalMatches = 0;
        try {
            totalMatches = searchService.search(query, beginIndex - 1, endIndex - 1, componentIds);
        } catch (SearchService.Exception sse) {
            throw new SearchServiceFailedServletException("Exception performing search",sse);
        }
        final Collection components = getComponent(componentIds);
        final Map model = new Hashtable();
        model.put(RESULTS_BEAN_NAME,components);
        model.put(QUERY_PARAMETER_NAME,query);
        model.put(BEGIN_INDEX_PARAMETER_NAME,new Integer(beginIndex));
        model.put(END_INDEX_PARAMETER_NAME,new Integer(Math.min(totalMatches, endIndex)));
        model.put(TOTAL_MATCHES_BEAN_NAME,new Integer(totalMatches));
        model.put(PAGES_BEAN_NAME,computePages(totalMatches,HITS_PER_PAGE));
        final int currentPage = ((beginIndex+1) / HITS_PER_PAGE) + 1;
        model.put(CURRENT_PAGE_BEAN_NAME,new Page(currentPage, beginIndex,endIndex));
        return new ModelAndView("searchResultsView",model);
    }

    private Collection computePages(int totalMatches, int resultsPerPage) {
        int numPages = (totalMatches / resultsPerPage);
        if ( (totalMatches % resultsPerPage ) != 0 ) {
            numPages++;
        }
        final Collection result = new ArrayList();
        for(int i=0;i<numPages;i++) {
            result.add(new Page(i+1,(i*resultsPerPage),((i+1)*resultsPerPage)));
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
        private int pageIndex;
        private int hitBeginIndex;
        private int hitEndIndex;

        public Page(int id, int beginIndex, int endIndex) {
            this.pageIndex = id;
            this.hitBeginIndex = beginIndex;
            this.hitEndIndex = endIndex;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public int getHitBeginIndex() {
            return hitBeginIndex;
        }

        public int getHitEndIndex() {
            return hitEndIndex;
        }
    }

    public static interface ViewConfiguration {
        //Return the name of the form to show when the controller is first invoked.
        String getFormView();
    }
}
