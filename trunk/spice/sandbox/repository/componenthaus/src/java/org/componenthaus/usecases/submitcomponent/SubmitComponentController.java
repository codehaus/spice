package org.componenthaus.usecases.submitcomponent;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class SubmitComponentController extends SimpleFormController {
    /**
     * The name of the form field containing the jar file being submitted.
     */
    public static final String MULTIPART_FIELD_NAME = "submission";

    /**
     * The name of the directory into which submitted jars are copied
     */
    public static final String SUBMISSIONS_DIRECTORY_NAME = "submissions";

    private final SubmissionManager submissionManager;

    public SubmitComponentController(ViewConfiguration viewConfiguration,
                                     SubmissionManager submissionManager) {
        setCommandClass(Object.class);
        setSuccessView(viewConfiguration.getSuccessView());
        setFormView(viewConfiguration.getFormView());
        this.submissionManager = submissionManager;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
                                    Object command, BindException errors)
            throws ServletException, IOException {
        if ( ! (request instanceof MultipartHttpServletRequest) ) {
            throw new NotAMultipartRequestServletException();
        }
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        final MultipartFile submission = multipartRequest.getFile(MULTIPART_FIELD_NAME);
        if ( submission == null ) {
            throw new RequiredMultipartFieldMissingServletException(MULTIPART_FIELD_NAME);
        }
        final File dest = new File(SUBMISSIONS_DIRECTORY_NAME, submission.getOriginalFileName());
        submission.transferTo(dest);
        try {
            submissionManager.submit(dest);
        } catch (Exception e) {
            throw new ServletException("Exception submitting component",e);
        }
        return new ModelAndView("welcomeView");
    }

    /**
     * Small plug interface to allow the application configuration tell
     * this controller what view to show initially (form view) and what view
     * to show on success.  I made this an interface as its easier to do than
     * providing parameters for the constructor of this controller explicitly
     * in Pico.  Better to let Pico figure out what needs to be passed.
     */
    public static interface ViewConfiguration {
        String getFormView();
        String getSuccessView();
    }
}
