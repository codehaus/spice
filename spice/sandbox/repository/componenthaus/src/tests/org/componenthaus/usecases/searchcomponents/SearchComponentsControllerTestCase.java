package org.componenthaus.usecases.searchcomponents;

import junit.framework.TestCase;
import com.mockobjects.servlet.MockHttpServletRequest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Map;

import org.componenthaus.usecases.common.MissingRequestParameterServletException;
import org.componenthaus.tests.MockSearchService;
import org.componenthaus.tests.MockComponentRepository;
import org.componenthaus.tests.MockComponent;
import org.componenthaus.search.SearchService;
import org.springframework.web.servlet.ModelAndView;

public class SearchComponentsControllerTestCase extends TestCase {
    private static final String QUERY_PARAMETER_NAME = SearchComponentsController.QUERY_PARAMETER_NAME;
    private static final String BEGIN_INDEX_PARAMETER_NAME = SearchComponentsController.BEGIN_INDEX_PARAMETER_NAME;
    private static final String END_INDEX_PARAMETER_NAME = SearchComponentsController.END_INDEX_PARAMETER_NAME;
    private static final String RESULTS_BEAN_NAME = SearchComponentsController.RESULTS_BEAN_NAME;
    private static final String TOTAL_MATCHES_BEAN_NAME = SearchComponentsController.TOTAL_MATCHES_BEAN_NAME;
    private static final String PAGES_BEAN_NAME = SearchComponentsController.PAGES_BEAN_NAME;
    private static final String CURRENT_PAGE_BEAN_NAME = SearchComponentsController.CURRENT_PAGE_BEAN_NAME;
    private static final int HITS_PER_PAGE = SearchComponentsController.HITS_PER_PAGE;

    private static final String queryParameterValue = "queryParameterValue";
    private static final String formView = "formView";
    private MockViewConfiguration viewConfiguration = null;
    private MockSearchService searchService = null;
    private MockComponentRepository repository = null;
    private MockHttpServletRequest request = null;
    private SearchComponentsController controller = null;


    protected void setUp() throws Exception {
        viewConfiguration = new MockViewConfiguration(formView);
        searchService = new MockSearchService();
        repository = new MockComponentRepository();
        controller = new SearchComponentsController(viewConfiguration, searchService, repository);
        request = new MockHttpServletRequest();
    }

    public void testThrowsExceptionWhenQueryParameterMissing() throws IOException, ServletException {
        MissingRequestParameterServletException expected = null;
        request.setupAddParameter(QUERY_PARAMETER_NAME, (String) null);
        try {
            controller.onSubmit(request, null, null, null);
            fail("Did not get expected exception");
        } catch (MissingRequestParameterServletException e) {
            expected = e;
        }
        assertEquals(QUERY_PARAMETER_NAME, expected.getParameterName());
    }

    public void testThrowsExceptionWhenSearchServiceFails() throws IOException, ServletException {
        request.setupAddParameter(QUERY_PARAMETER_NAME, queryParameterValue);
        request.setupAddParameter(BEGIN_INDEX_PARAMETER_NAME, (String) null);
        request.setupAddParameter(END_INDEX_PARAMETER_NAME, (String) null);
        final SearchService.Exception actualException = new SearchService.Exception("who cares", new RuntimeException());
        searchService.setToBeThrown(actualException);
        SearchServiceFailedServletException expected = null;
        try {
            controller.onSubmit(request, null, null, null);
            fail("Did not get expected exception");
        } catch (SearchServiceFailedServletException e) {
            expected = e;
        }
        assertSame(actualException,expected.getRootCause());
    }

    public void testCanDealWithZeroMatches() throws IOException, ServletException {
        request.setupAddParameter(QUERY_PARAMETER_NAME, queryParameterValue);
        final String beginIndex = null;
        final String endIndex = null;
        request.setupAddParameter(BEGIN_INDEX_PARAMETER_NAME, beginIndex);
        request.setupAddParameter(END_INDEX_PARAMETER_NAME, endIndex);
        final List mockResults = Collections.EMPTY_LIST;
        searchService.setupResults(mockResults);
        searchService.setupExpectedQueryParams(queryParameterValue,0,HITS_PER_PAGE - 1);

        final ModelAndView view = controller.onSubmit(request,null,null,null);

        final Map model = view.getModel();
        assertModel(model, mockResults,0,0,0);

        request.verify();
        searchService.verify();
    }

    public void testCanDealWithOneMatch() throws IOException, ServletException {
        final String componentId = "0";
        request.setupAddParameter(QUERY_PARAMETER_NAME, queryParameterValue);
        final String beginIndex = null;
        final String endIndex = null;
        request.setupAddParameter(BEGIN_INDEX_PARAMETER_NAME, beginIndex);
        request.setupAddParameter(END_INDEX_PARAMETER_NAME, endIndex);
        final List mockResults = Collections.singletonList(componentId);
        searchService.setupResults(mockResults);
        searchService.setupExpectedQueryParams(queryParameterValue,0,HITS_PER_PAGE - 1);
        final MockComponent component = new MockComponent(componentId);
        repository.setupComponent(component);

        final ModelAndView view = controller.onSubmit(request,null,null,null);

        final Map model = view.getModel();
        assertModel(model,mockResults,1,1,1);

        request.verify();
        searchService.verify();
    }

    //TODO
    public void todo_testCanDealWithOneExactlyFullPage() {
        fail("Not yet implemented");
    }

    //TODO
    public void todo_testCanDealWithOnePageAndABit() {
        fail("Not yet implemented");
    }

    //TODO
    public void todo_testCanDisplaySecondPageOfTwo() {
        fail("Not yet implemented");
    }

    private void assertModel(final Map model, final List mockResults, final int expectedEndIndex, final int expectedTotalMatches, final int expectedNumPages) {
        final Collection actualResults = (Collection) model.get(RESULTS_BEAN_NAME);
        assertNotNull(actualResults);
        assertEquals(mockResults.size(),actualResults.size());

        assertEquals(new Integer(1), model.get(BEGIN_INDEX_PARAMETER_NAME));
        assertEquals(new Integer(expectedEndIndex), model.get(END_INDEX_PARAMETER_NAME));
        assertEquals(new Integer(expectedTotalMatches), model.get(TOTAL_MATCHES_BEAN_NAME));
        final Collection pages = (Collection) model.get(PAGES_BEAN_NAME);
        assertEquals(expectedNumPages,pages.size());
        final SearchComponentsController.Page currentPage = (SearchComponentsController.Page) model.get(CURRENT_PAGE_BEAN_NAME);
        assertEquals(1,currentPage.getHitBeginIndex());
        assertEquals(HITS_PER_PAGE,currentPage.getHitEndIndex());
        assertEquals(1,currentPage.getPageIndex());
    }

    private static final class MockViewConfiguration implements SearchComponentsController.ViewConfiguration {
        private final String formView;

        public MockViewConfiguration(String formView) {
            this.formView = formView;
        }

        public String getFormView() {
            return formView;
        }
    }
}
