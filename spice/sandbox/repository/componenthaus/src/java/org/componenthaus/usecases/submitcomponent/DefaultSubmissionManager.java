package org.componenthaus.usecases.submitcomponent;

import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.repository.services.CommandRegistry;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.repository.api.Component;
import org.prevayler.PrevalentSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.Iterator;
import java.util.Collections;

public class DefaultSubmissionManager implements SubmissionManager {
    private final CommandRegistry commandRegistry;
    private final ComponentFactory componentFactory;
    private final PrevalentSystem prevalentSystem;

    public DefaultSubmissionManager(CommandRegistry commandRegistry,
                                    ComponentFactory componentFactory,
                                    PrevalentSystem prevalentSystem) {
        this.commandRegistry = commandRegistry;
        this.componentFactory = componentFactory;
        this.prevalentSystem = prevalentSystem;
    }

    public void submit(File filename) throws Exception {
        JarFile jarFile = new JarFile(filename);
        ZipEntry metadataFile = jarFile.getEntry(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        if ( metadataFile == null ) {
            throw new MissingJarEntryException(SubmissionManager.METADATA_FILE_JAR_ENTRY_NAME);
        }
        jarFile.getInputStream(metadataFile);
        final byte[] bytes = getJarEntryBytes(jarFile, metadataFile);

        final String metadata = new String(bytes);
        final ComponentMetadata componentMetadata = ComponentMetadata.fromXml(metadata);
        for(Iterator i=componentMetadata.getInterfaces();i.hasNext();) {
            final InterfaceMetadata interfaceMetadata = (InterfaceMetadata) i.next();
            Component component = componentFactory.createComponent(interfaceMetadata.getFullyQualifiedName(),"1.0",Collections.EMPTY_LIST,interfaceMetadata.getShortDescription(),interfaceMetadata.getJavadoc(),interfaceMetadata.getSource());
            commandRegistry.createSubmitComponentCommand(component).execute(prevalentSystem);
        }
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
}
