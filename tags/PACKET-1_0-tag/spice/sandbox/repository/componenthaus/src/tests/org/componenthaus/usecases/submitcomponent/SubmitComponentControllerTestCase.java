package org.componenthaus.usecases.submitcomponent;

import com.mockobjects.servlet.MockHttpServletRequest;
import junit.framework.TestCase;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SubmitComponentControllerTestCase extends TestCase {
    private static final String formView = "formView";
    private static final String successView = "successView";
    static final String simpleJarFilename = "junit.jar";
    private static final ViewConfiguration viewConfiguration = new ViewConfiguration(formView,successView);
    private MockSubmissionManager mockSubmissionManager = null;
    private SubmitComponentController controller = null;

    protected void setUp() throws Exception {
        mockSubmissionManager = new MockSubmissionManager();
        controller = new SubmitComponentController(viewConfiguration,mockSubmissionManager);
    }

    public void testViewConfigurationIsHandledCorrectly() {
        final SubmitComponentControllerSubClass controller = new SubmitComponentControllerSubClass(viewConfiguration);
        assertEquals(formView, controller.formView());
        assertEquals(successView, controller.successView());
    }

    public void testThrowsExceptionWhenNotMultipartRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        try {
            controller.onSubmit(request,null,null,null);
            fail("Did not get expected exception");
        } catch (NotAMultipartRequestServletException e) {
        }
    }

    public void testThrowsExceptionWhenNoSubmissionInRequest() throws IOException, ServletException {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        RequiredMultipartFieldMissingServletException expected = null;
        try {
            controller.onSubmit(request,null,null,null);
            fail("Did not get expected exception");
        } catch (RequiredMultipartFieldMissingServletException e) {
            expected = e;
        }
        assertEquals(SubmitComponentController.MULTIPART_FIELD_NAME,expected.getFieldName());
    }

    public void testDelegatesSubmittedJarfileCorrectly() throws IOException, ServletException {
        MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        final File expectedJarFile = new File(SubmitComponentController.SUBMISSIONS_DIRECTORY_NAME,simpleJarFilename);
        mockSubmissionManager.setupExpectedJarFile(expectedJarFile);
        final MockMultipartFile mockMultipartFile = new MockMultipartFile(simpleJarFilename);
        mockMultipartFile.setupExpectedTransferTo(expectedJarFile);
        request.setupAddMultipartFile(SubmitComponentController.MULTIPART_FIELD_NAME,mockMultipartFile);
        final ModelAndView view = controller.onSubmit(request,null,null,null);
        mockSubmissionManager.verify();
        mockMultipartFile.verify();

        assertNotNull(view);
        assertNotNull(view.getViewName());
    }

    private static final class MockSubmissionManager implements CarSubmissionManager {
        private File expectedJarFile = null;
        private File actualJarFile = null;

        public void submit(File jarFile) {
            actualJarFile = jarFile;
        }

        public void setupExpectedJarFile(File jarFile) {
            this.expectedJarFile = jarFile;
        }

        public void verify() {
            assertEquals(expectedJarFile,actualJarFile);
        }
    }

    private static final class MockMultipartFile implements MultipartFile {
        private File expectedTransferTo;
        private File actualTransferTo;

        private final String filename;

        public MockMultipartFile(String filename) {
            this.filename = filename;
        }

        public void setupExpectedTransferTo(File f) {
            expectedTransferTo = f;
        }

        public String getName() {
            throw new UnsupportedOperationException();
        }

        public String getOriginalFileName() {
            return filename;
        }

        public String getContentType() {
            throw new UnsupportedOperationException();
        }

        public long getSize() {
            throw new UnsupportedOperationException();
        }

        public byte[] getBytes() throws IOException {
            throw new UnsupportedOperationException();
        }

        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        public void transferTo(File dest) throws IOException, IllegalStateException {
            actualTransferTo = dest;
        }

        public void verify() {
            assertEquals(expectedTransferTo, actualTransferTo);
        }
    }

    private static final class MockMultipartHttpServletRequest extends MockHttpServletRequest implements MultipartHttpServletRequest {
        private Map files = new HashMap();

        public Iterator getFileNames() {
            return null;
        }

        public MultipartFile getFile(String name) {
            return (MultipartFile) files.get(name);
        }

        public Map getFileMap() {
            return null;
        }

        public void setupAddMultipartFile(String name, MultipartFile f) {
            files.put(name,f);
        }
    }


    private static final class ViewConfiguration implements SubmitComponentController.ViewConfiguration {
        private final String formView;
        private final String successView;

        public ViewConfiguration(String formView, String successView) {
            this.formView = formView;
            this.successView = successView;
        }

        public String getFormView() {
            return formView;
        }

        public String getSuccessView() {
            return successView;
        }
    }

    /**
     * Subclassing this so I can avail of some of the protected methods
     */
    private static final class SubmitComponentControllerSubClass extends SubmitComponentController {
        public SubmitComponentControllerSubClass(SubmitComponentController.ViewConfiguration viewConfiguration) {
            super(viewConfiguration,null);
        }

        public String formView() {
            return getFormView();
        }

        public String successView() {
            return getSuccessView();
        }
    }
}
