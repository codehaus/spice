package org.componenthaus.usecases.submitcomponent;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.componenthaus.ant.metadata.ComponentDefinition;
import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.ComponentImplementation;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.repository.impl.ServiceImplementationFactory;
import org.componenthaus.repository.services.RegisterDownloadableComponentCommand;
import org.componenthaus.repository.services.SubmitComponentCommand;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.api.ServiceImplementation;
import org.componenthaus.util.source.CodeFormatter;
import org.prevayler.Prevayler;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SubmitComponentController extends SimpleFormController implements InitializingBean {
    private ServletException bootstrapException = null;
    private Prevayler prevayler;
    private ComponentFactory componentFactory;
    private ServiceImplementationFactory serviceImplementationFactory;
    private CodeFormatter codeFormatter;
    private final ViewConfiguration viewConfiguration;

    public SubmitComponentController(final ViewConfiguration viewConfiguration) {
        this.viewConfiguration = viewConfiguration;
        setCommandClass(Object.class);
        File submissionsDir = new File("submissions");
        if (submissionsDir.exists() && !submissionsDir.isDirectory()) {
            bootstrapException = new ServletException(submissionsDir.getAbsolutePath() + " exists but is not a directory.  Please remove so a directory with this name can be created");
        }
        if (!submissionsDir.exists()) {
            if (!submissionsDir.mkdir()) {
                bootstrapException = new ServletException("Failed to create directory '" + submissionsDir.getAbsolutePath() + "'");
            }
        }
    }

    public void setPrevayler(Prevayler prevayler) {
        this.prevayler = prevayler;
    }

    public void setComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    public void setServiceImplementationFactory(ServiceImplementationFactory serviceImplementationFactory) {
        this.serviceImplementationFactory = serviceImplementationFactory;
    }

    public void setCodeFormatter(final CodeFormatter codeFormatter) {
        this.codeFormatter = codeFormatter;
    }

    public void afterPropertiesSet() throws Exception {
        assertSet("prevayler",prevayler);
        assertSet("componentFactory",componentFactory);
        assertSet("serviceImplementationFactory",serviceImplementationFactory);
        assertSet("codeFormatter",codeFormatter);
    }

    private void assertSet(String name, Object property) {
        if ( property == null ) {
            throw new ApplicationContextException("Must set property '" + name + "' on " + getClass());
        }
    }


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object o, BindException e) throws ServletException, IOException {
        if (bootstrapException != null) {
            throw bootstrapException;
        }
        final DiskFileUpload upload = new DiskFileUpload();
        List items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e1) {
            throw new ServletException("Exception during file upload", e1);
        }
        for (Iterator i = items.iterator(); i.hasNext();) {
            final FileItem item = (FileItem) i.next();
            if (item.getFieldName().equalsIgnoreCase("submission")) {
                try {
                    handleSubmission(item);
                } catch (Exception e1) {
                    logger.error("Error handling component submission", e1);
                    throw new ServletException("Error handling component submission", e1);
                }
            }
        }
        response.setContentType("text/html");
        return new ModelAndView("welcomeView");
    }

    private void handleSubmission(final FileItem submission) throws Exception {
        String submissionName = submission.getName();
        final int indexOfLastForwardSlash = submissionName.lastIndexOf("/");
        final int indexOfLastBackSlash = submissionName.lastIndexOf("\\");
        final int indexOfLastSlash = Math.max(indexOfLastBackSlash, indexOfLastForwardSlash);

        if (indexOfLastSlash != -1) {
            submissionName = submissionName.substring(indexOfLastSlash + 1);
        }

        File uploadedFile = new File("submissions" + File.separator + submissionName);
        submission.write(uploadedFile);
        JarFile jarFile = new JarFile(uploadedFile);
        final String componentId = handleMetadata(jarFile);
        handleBinary(componentId, jarFile);
    }

    private String handleMetadata(JarFile jarFile) throws Exception {
        final Enumeration entries = jarFile.entries();
        String componentId = null;
        while (componentId == null && entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            if ("metadata.xml".equals(entry.getName())) {
                componentId = handleMetadata(jarFile, entry);
            }
        }
        return componentId;
    }

    private void handleBinary(String componentId, JarFile jarFile) throws Exception {
        final Enumeration entries = jarFile.entries();
        boolean handled = false;
        while (!handled && entries.hasMoreElements()) {
            JarEntry entry = (JarEntry) entries.nextElement();
            if (entry.getName().startsWith("binary/") && entry.getName().length() > "binary/".length()) {
                // This should be the jar file containing the component release.
                final String jarName = entry.getName().substring("binary/".length() + 1);
                final File dest = new File("submissions" + File.separator + jarName);
                final FileOutputStream out = new FileOutputStream(dest);
                final InputStream in = jarFile.getInputStream(entry);
                copyStream(in, out);
                in.close();
                out.close();
                handled = true;
                prevayler.executeCommand(new RegisterDownloadableComponentCommand(componentId, dest));
            }
        }
    }

    public void copyStream(InputStream in, OutputStream out) throws Exception {
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = in.read(buf)) != -1) {
            out.write(buf, 0, i);
        }
    }

    private String handleMetadata(JarFile jarFile, JarEntry entry) throws Exception {
        final byte[] bytes = getJarEntryBytes(jarFile, entry);

        final String metadata = new String(bytes);
        final StringReader xmlReader = new StringReader(metadata);
        final BeanReader beanReader = new BeanReader();
        beanReader.getXMLIntrospector().setAttributesForPrimitives(false);
        beanReader.setMatchIDs(false);
        beanReader.registerBeanClass("component", ComponentMetadata.class);

        final ComponentMetadata component = (ComponentMetadata) beanReader.parse(xmlReader);

        return submitComponent(component);
    }

    private byte[] getJarEntryBytes(JarFile jarFile, JarEntry entry) throws Exception {
        final InputStream in = jarFile.getInputStream(entry);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream(in.available());
        copyStream(in, bytes);
        in.close();
        bytes.close();
        return bytes.toByteArray();
    }

    private String submitComponent(final ComponentMetadata metadata) throws Exception {
        final ComponentDefinition definition = metadata.getDefinition();
        final Component component = componentFactory.createComponent(definition.getFullyQualifiedName(),
                definition.getVersion(),
                definition.getAuthors(),
                definition.getOneLineDescription(),
                definition.getFullDescription(),
                definition.getServiceInterface());
        for (Iterator i=metadata.getImplementations().iterator();i.hasNext();) {
            final ComponentImplementation implMetadata = (ComponentImplementation) i.next();
            final ServiceImplementation impl = serviceImplementationFactory.createServiceImplementation(
                    implMetadata.getFullyQualifiedName(),
                    implMetadata.getVersion(),
                    implMetadata.getAuthors(),
                    implMetadata.getOneLineDescription(),
                    implMetadata.getFullDescription());
            component.addImplementation(impl);
            for(Iterator j=implMetadata.getServiceDependencies().iterator();j.hasNext();) {
                final ComponentDefinition def = (ComponentDefinition) j.next();
                final Component plug = componentFactory.createComponent(def.getFullyQualifiedName(),
                                def.getVersion(),
                                def.getAuthors(),
                                def.getOneLineDescription(),
                                def.getFullDescription(),
                                def.getServiceInterface());
                impl.addPlug(plug);
            }
        }
        return (String) prevayler.executeCommand(new SubmitComponentCommand(component));
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
