package org.componenthaus.ant;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.InterfaceMetadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO Accept multiple interface names
public class ComponentMetadataTask extends Task {
    static final String metadataFilename = "metadata.xml";

    private String sourceDirectory;
    private List interfaceNames;
    private String targetDirectory;

    public ComponentMetadataTask() {
        interfaceNames = new ArrayList();
    }

    public ComponentMetadataTask(String []interfaceNames, String sourceDirectory, String targetdirectory) {
        this();
        this.interfaceNames = Arrays.asList(interfaceNames);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetdirectory;
    }

    public ComponentMetadataTask(String interfacename, String sourceDirectory, String targetdirectory) {
        this(new String[]{interfacename},sourceDirectory,targetdirectory);
    }

    public InterfaceSpec createInterface() {
        return new InterfaceSpec(this);
    }

    public void setSourceDirectory(String dir) {
        this.sourceDirectory = dir;
    }

    public void execute() throws BuildException {
        assertParametersAreOk();
        doExecute();
    }

    public void addInterfaceName(String name) {
        interfaceNames.add(name);
    }

    private void assertParametersAreOk() {
        if ( interfaceNames.size() == 0 || interfaceNames.get(0) == null) {
            throw new NoInterfacesSpecifiedBuildException();
        }
        assertNotNull(sourceDirectory,"sourceDirectory");
        assertNotNull(targetDirectory,"targetDirectory");
        assertDirectoryIsOk(new File(sourceDirectory), sourceDirectory);
        assertDirectoryIsOk(new File(targetDirectory), targetDirectory);
    }

    private void assertNotNull(String property, String propertyName) {
        if ( property == null ) {
            throw new ParameterNotSpecifiedBuildException(propertyName);
        }
    }

    private void assertDirectoryIsOk(File dir, String directoryName) {
        if ( !dir.exists()) {
            throw new DirectoryDoesNotExistBuildException(directoryName);
        }
        if ( ! dir.isDirectory()) {
            throw new NotADirectoryBuildException(directoryName);
        }
    }

    private void doExecute() {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(sourceDirectory));
        final String interfaceName = interfaceNames.size() > 0 ? (String) interfaceNames.iterator().next() : null;  //Just for now, until I deal properly with many interfaces
        final JavaClass interfaceAsClass = builder.getClassByName(interfaceName);
        if ( interfaceAsClass == null ) {
            throw new NoSuchJavaSourceFileBuildException(interfaceName);
        }
        final String javadoc = interfaceAsClass.getComment();
        if ( javadoc == null || "".equals(javadoc)) {
            throw new NoJavadocBuildException(interfaceName);
        }
        if ( ! interfaceAsClass.isInterface() ) {
            throw new NotAnInterfaceBuildException(interfaceName);
        }

        File metadataFile = new File(targetDirectory,metadataFilename);
        try {
            metadataFile.createNewFile();
        } catch (IOException e) {
            throw new IOBuildException(e);
        }

        String source = null;
        try {
            source = loadFile(new File(sourceDirectory,asDirectory(interfaceName) + ".java"));
        } catch (IOException e) {
            throw new IOBuildException(e);
        }
        InterfaceMetadata interfaceMetadata = new InterfaceMetadata(
                interfaceAsClass.getPackage(),
                interfaceAsClass.getName(),
                interfaceAsClass.getComment(),
                source);
        ComponentMetadata componentMetadata = new ComponentMetadata();
        componentMetadata.addInterface(interfaceMetadata);
        try {
            FileWriter writer = new FileWriter(metadataFile);
            writer.write(componentMetadata.toXml());
            writer.close();
        } catch (IOException e) {
            throw new IOBuildException(e);
        }
    }

    private String asDirectory(String javaPackageName) {
        return javaPackageName.replaceAll("\\.","/"); //File.separator does not work here for some reason
    }

    private String loadFile(File file) throws IOException {
        StringWriter writer = new StringWriter();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while ( line != null ) {
            writer.write(line + "\n");
            line = reader.readLine();
        }
        writer.close();
        reader.close();
        return writer.toString();
    }

    public void setTargetDirectory(String targetdirectory) {
        this.targetDirectory = targetdirectory;
    }

    public int numInterfaces() {
        return interfaceNames.size();
    }

    public boolean containsInterface(String interfacename) {
        return interfaceNames.contains(interfacename);
    }

    public static final class InterfaceSpec {
        private final ComponentMetadataTask task;

        public InterfaceSpec(ComponentMetadataTask task) {
            this.task = task;
        }

        public void setName(final String name) {
            task.addInterfaceName(name);
        }

    }
}
