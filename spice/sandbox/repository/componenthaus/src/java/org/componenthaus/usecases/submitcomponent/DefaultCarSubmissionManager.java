package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;
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
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class DefaultCarSubmissionManager implements CarSubmissionManager {
    private final CommandRegistry commandRegistry;
    private final Prevayler prevayler;
    private final FileManager fileManager;
    private final SubmissionMonitor submissionMonitor;
    private final MetadataConverter metadataConverter;

    public DefaultCarSubmissionManager(CommandRegistry commandRegistry,
                                    FileManager fileManager,
                                    SubmissionMonitor submissionMonitor,
                                    Prevayler prevayler,
                                    MetadataConverter metadataConverter) {
        this.commandRegistry = commandRegistry;
        this.prevayler = prevayler;
        this.fileManager = fileManager;
        this.submissionMonitor = submissionMonitor;
        this.metadataConverter = metadataConverter;
    }

    public void submit(File filename) throws Exception {
        System.out.println("Called");
        JarFile jarFile = new JarFile(filename);
        ZipEntry metadataFile = jarFile.getEntry(CarSubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        if ( metadataFile == null ) {
            throw new MissingJarEntryException(CarSubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        }
        ZipEntry distribution = getDistributionEntry(jarFile);
        if ( distribution == null ) {
            throw new MissingJarEntryException(CarSubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME);
        }
        System.out.println("About to handle metadata");
        Collection components = handleMetadata(jarFile, metadataFile);
        System.out.println("Meta data done");
        File jarInRepository = handleDistribution(jarFile,distribution);
        for(Iterator i=components.iterator();i.hasNext();) {
            Component component = (Component) i.next();
            prevayler.executeCommand(commandRegistry.createRegisterDownloadableCommand(component.getId(),jarInRepository));
            submissionMonitor.componentSubmitted(component,jarInRepository);
        }
    }

    private File handleDistribution(JarFile jarFile, ZipEntry distribution) throws IOException {
        String jarName = getJarName(distribution);
        String projectName = getProjectName(jarName);
        File destDirectory = new File(CarSubmissionManager.JAR_REPOSITORY_DIRECTORY + "/" + projectName + "/jars");
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
        return name.substring(CarSubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME.length(),name.length());
    }


    private Collection handleMetadata(JarFile jarFile, ZipEntry metadataFile) throws Exception {
        jarFile.getInputStream(metadataFile);
        final byte[] bytes = getJarEntryBytes(jarFile, metadataFile);

        final String metadata = new String(bytes);
        final ComponentMetadata componentMetadata = ComponentMetadata.fromXml(metadata);
        return metadataConverter.convert(componentMetadata);
    }

    private ZipEntry getDistributionEntry(JarFile jarFile) {
        ZipEntry result = null;
        Enumeration entries = jarFile.entries();
        final String directory = CarSubmissionManager.DISTRIBUTION_DIRECTORY_JAR_ENTRY_NAME;
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
        }
    }
}
