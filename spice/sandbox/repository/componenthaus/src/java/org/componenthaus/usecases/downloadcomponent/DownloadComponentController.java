package org.componenthaus.usecases.downloadcomponent;

import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.util.file.FileManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadComponentController extends AbstractController {
    private final FileManager fileManager;
    private final ComponentRepository repository;
    public static final String DOWNLOAD_ID_PARAMETER_NAME = "id";
    public static final String CONTENT_TYPE = "application/x-java-archive";

    public DownloadComponentController(FileManager fileManager, ComponentRepository repository) {
        this.fileManager = fileManager;
        this.repository = repository;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final String downloadId = request.getParameter(DOWNLOAD_ID_PARAMETER_NAME);
        if ( downloadId == null ) {
            throw new MissingRequestParameterServletException(DOWNLOAD_ID_PARAMETER_NAME);
        }
        final File f = repository.getDownloadable(downloadId);
        if ( f == null ) {
            throw new NoSuchDownloadableServletException(downloadId);
        }
        final OutputStream out = new BufferedOutputStream(response.getOutputStream());


        response.setContentType(CONTENT_TYPE);
        response.setContentLength((int) f.length());
        fileManager.copy(f,out);

        out.flush();
        out.close();

        return null;
    }

}
