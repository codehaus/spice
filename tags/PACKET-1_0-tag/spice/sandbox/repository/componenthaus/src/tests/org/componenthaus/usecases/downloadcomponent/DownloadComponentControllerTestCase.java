package org.componenthaus.usecases.downloadcomponent;

import junit.framework.TestCase;
import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletOutputStream;

import javax.servlet.ServletException;
import java.io.IOException;

import org.componenthaus.util.file.MockFileManager;
import org.componenthaus.util.file.MockFile;
import org.componenthaus.tests.MockComponentRepository;
import org.componenthaus.usecases.common.MissingRequestParameterServletException;

public class DownloadComponentControllerTestCase extends TestCase {
    private static final String ID = "345";
    private MockHttpServletRequest request = null;
    private MockHttpServletResponse response = null;
    private MockFileManager fileManager = null;
    private MockComponentRepository componentRepository = null;
    private DownloadComponentController controller = null;

    protected void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        fileManager = new MockFileManager();
        componentRepository = new MockComponentRepository();
        controller = new DownloadComponentController(fileManager, componentRepository);
    }

    public void testThrowsExceptionIfDownloadableIdMissing() throws ServletException, IOException {
        MissingRequestParameterServletException expected = null;
        try {
            request.setupAddParameter(DownloadComponentController.DOWNLOAD_ID_PARAMETER_NAME, (String) null);
            controller.handleRequestInternal(request, null);
            fail("Did not get expected exception");
        } catch (MissingRequestParameterServletException e) {
            expected = e;
        }
        assertEquals(DownloadComponentController.DOWNLOAD_ID_PARAMETER_NAME, expected.getParameterName());
    }

    public void testThrowsExceptionIfDownloadableMissing() throws ServletException, IOException {
        NoSuchDownloadableServletException expected = null;
        try {
            request.setupAddParameter(DownloadComponentController.DOWNLOAD_ID_PARAMETER_NAME, ID);
            controller.handleRequestInternal(request, null);
            fail("Did not get expected exception");
        } catch (NoSuchDownloadableServletException e) {
            expected = e;
        }
        assertEquals(ID, expected.getDownloadableId());
    }

    public void testDownloadHappyPath() throws ServletException, IOException {
        request.setupAddParameter(DownloadComponentController.DOWNLOAD_ID_PARAMETER_NAME, ID);
        MockFile mockFile = new MockFile();
        int length = 345;
        mockFile.setupLength(length);
        response.setExpectedContentLength(length);
        response.setExpectedContentType(DownloadComponentController.CONTENT_TYPE);
        MockServletOutputStream outputStream = new MockServletOutputStream();
        outputStream.setExpectedCloseCalls(1);
        response.setupOutputStream(outputStream);

        componentRepository.setupDownloadable(ID,mockFile);
        controller.handleRequestInternal(request, response);
        outputStream.verify();
    }
}
