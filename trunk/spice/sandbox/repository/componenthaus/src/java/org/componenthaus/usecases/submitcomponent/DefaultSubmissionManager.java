package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.repository.api.Component;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.util.file.FileManager;
import org.prevayler.Prevayler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class DefaultSubmissionManager implements SubmissionManager {
    private final CommandRegistry commandRegistry;
    private final ComponentFactory componentFactory;
    private final Prevayler prevayler;
    private final FileManager fileManager;
    private final SubmissionMonitor submissionMonitor;

    public DefaultSubmissionManager(CommandRegistry commandRegistry,
                                    ComponentFactory componentFactory,
                                    FileManager fileManager,
                                    SubmissionMonitor submissionMonitor,
                                    Prevayler prevayler) {
        this.commandRegistry = commandRegistry;
        this.componentFactory = componentFactory;
        this.prevayler = prevayler;
        this.fileManager = fileManager;
        this.submissionMonitor = submissionMonitor;
    }

    public void submit(File filename) throws Exception {
        JarFile jarFile = new JarFile(filename);
        ZipEntry metadataFile = jarFile.getEntry(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        if ( metadataFile == null ) {
            throw new MissingJarEntryException(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        }
        ZipEntry distribution = getDistributionEntry(jarFile);
        if ( distribution == null ) {
            throw new MissingJarEntryException(SubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME);
        }
        Component component = handleMetadata(jarFile, metadataFile);
        File jarInRepository = handleDistribution(jarFile,distribution);
        prevayler.executeCommand(commandRegistry.createRegisterDownloadableCommand(component.getId(),jarInRepository));
        submissionMonitor.componentSubmitted(component,jarInRepository);
    }

    private File handleDistribution(JarFile jarFile, ZipEntry distribution) throws IOException {
        String jarName = getJarName(distribution);
        String projectName = getProjectName(jarName);
        File destDirectory = new File(SubmissionManager.JAR_REPOSITORY_DIRECTORY + "/" + projectName + "/jars");
        destDirectory.mkdirs();
        File target = new File(destDirectory,jarName);
        fileManager.copy(jarFile.getInputStream(distribution),target);
        return target;
    }

    private String getProjectName(String jarName) {
        int indexOfMinus = jarName.indexOf("-");
        if ( indexOfMinus == -1 ) {
            throw new IllegalJarNameException(jarName);
        }
        return jarName.substring(0,indexOfMinus);
    }

    private String getJarName(ZipEntry distribution) {
        final String name = distribution.getName();
        return name.substring(SubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME.length(),name.length());
    }


    private Component handleMetadata(JarFile jarFile, ZipEntry metadataFile) throws Exception {
        jarFile.getInputStream(metadataFile);
        final byte[] bytes = getJarEntryBytes(jarFile, metadataFile);

        Component result = null;
        final String metadata = new String(bytes);
        final ComponentMetadata componentMetadata = ComponentMetadata.fromXml(metadata);
        for(Iterator i=componentMetadata.getInterfaces();i.hasNext();) {
            final InterfaceMetadata interfaceMetadata = (InterfaceMetadata) i.next();
            result = componentFactory.createComponent(interfaceMetadata.getFullyQualifiedName(),"1.0",Collections.EMPTY_LIST,interfaceMetadata.getShortDescription(),interfaceMetadata.getJavadoc(),interfaceMetadata.getSource());
            prevayler.executeCommand(commandRegistry.createSubmitComponentCommand(result));
            assert result.getId() != null && Integer.parseInt(result.getId()) >= 0 : "Component should have been given an id";
        }
        return result;
    }

    private ZipEntry getDistributionEntry(JarFile jarFile) {
        ZipEntry result = null;
        Enumeration entries = jarFile.entries();
        final String directory = SubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME;
        while(result == null && entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if ( entry.getName().startsWith(directory) && entry.getName().length() > directory.length()) {
                result = entry;
            }
        }
        return result;
    }

    private byte[] getJarEntryBytes(JarFile jarFile, ZipEntry entry) throws IOException {
        final InputStream in = jarFile.getInputStream(entry);
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream(in.available());
        copyStream(in, bytes);
        in.close();
        bytes.close();
        return bytes.toByteArray();
    }

    public void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int i = 0;
        while ((i = in.read(buf)) != -1) {
            out.write(buf, 0, i);
        }
    }

    public static interface SubmissionMonitor {
        void componentSubmitted(Component component, File distribution);
    }

    public static final class NullSubmissionMonitor implements SubmissionMonitor {
        public void componentSubmitted(Component component, File distribution) {
            System.out.println("Submitted component " + component.getId() + ", jar file is at " + distribution.getAbsolutePath());
        }
    }
}
