package org.componenthaus.usecases.downloadcomponent;

import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.util.file.FileManager;
import org.prevayler.Prevayler;
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
    private Prevayler prevayler = null;
    private FileManager fileManager = null;

    public void setPrevayler(Prevayler prevayler) {
        this.prevayler = prevayler;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final ComponentRepository repository = (ComponentRepository) prevayler.system();
        final OutputStream out = new BufferedOutputStream(response.getOutputStream());
        final File f = repository.getDownloadable(request.getParameter("id"));

        response.setContentType("application/x-java-archive");
        response.setContentLength((int) f.length());
        fileManager.copy(f,out);

        out.flush();
        out.close();

        return null;
    }

}
