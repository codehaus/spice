package org.componenthaus.ant;

import junit.framework.TestCase;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.xml.sax.SAXException;
import org.componenthaus.ant.metadata.InterfaceMetadata;
import org.componenthaus.ant.metadata.ComponentMetadata;

//TODO stick author and version into metadata
//TODO check we can deal with more than one interface, and generate proper metadata
public class ComponentMetadataTaskTestCase extends TestCase {
    private static final String sourceDirectory = "tmp/testcasesource";
    private static final String targetDirectory = "tmp/testcasetarget";
    private static final String packageName = "com.acme.software";
    private static final String firstLineOfJavadoc = "This is the first line of javadoc.";
    private static final String someJavadoc = firstLineOfJavadoc + " This is the second line of javadoc. This the third.";
    private static final String interfaceName = "AnInterface";
    private static final String interfaceBody = "//empty";
    private static final String fullyQualifiedInterfaceName = packageName + "." + interfaceName;
    private static final String nonExistantDirectory = "this directory should not exist";

    private Collection createdFiles = null;

    protected void setUp() throws Exception {
        createdFiles = new ArrayList();
        createDirectory(sourceDirectory);
        createDirectory(targetDirectory);
    }

    protected void tearDown() throws Exception {
        for (Iterator i = createdFiles.iterator(); i.hasNext();) {
            File f = (File) i.next();
            f.delete();
        }
    }

    public void testHasRequiredSetters() {
        ComponentMetadataTask task = new ComponentMetadataTask();
        task.setSourceDirectory(sourceDirectory);
        task.setTargetDirectory(targetDirectory);
    }

    public void testCanAddInterfacesToTask() {
        final String secondInterfaceName = "secondInterfaceName";

        ComponentMetadataTask task = new ComponentMetadataTask();
        task.createInterface().setName(interfaceName);

        task.createInterface().setName(secondInterfaceName);
        assertEquals(2,task.numInterfaces());
        assertTrue(task.containsInterface(interfaceName));
        assertTrue(task.containsInterface(secondInterfaceName));
    }

    public void testHasZeroArgConstructor() {
        new ComponentMetadataTask();
    }

    public void testThrowsExceptionWhenSourceDirectoryNotSpecified() {
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,null,targetDirectory);
        ParameterNotSpecifiedBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (ParameterNotSpecifiedBuildException e) {
            expected = e;
        }
        assertEquals("sourceDirectory",expected.getParameterName());
    }

    public void testThrowsExceptionWhenTargetDirectoryNotSpecified() {
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,sourceDirectory,null);
        ParameterNotSpecifiedBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (ParameterNotSpecifiedBuildException e) {
            expected = e;
        }
        assertEquals("targetDirectory",expected.getParameterName());
    }

    public void testThrowsExceptionWhenInterfaceNameNotSpecified() {
        final ComponentMetadataTask task = new ComponentMetadataTask((String)null,sourceDirectory,targetDirectory);
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NoInterfacesSpecifiedBuildException e) {
        }
    }

    public void testThrowsExceptionWhenSourceDirectoryDoesNotExist() {
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,nonExistantDirectory,targetDirectory);
        DirectoryDoesNotExistBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (DirectoryDoesNotExistBuildException e) {
            expected = e;
        }
        assertEquals(nonExistantDirectory,expected.getDirectoryName());
    }

    public void testThrowsExceptionWhenTargetDirectoryDoesNotExist() {
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,sourceDirectory,nonExistantDirectory);
        DirectoryDoesNotExistBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (DirectoryDoesNotExistBuildException e) {
            expected = e;
        }
        assertEquals(nonExistantDirectory,expected.getDirectoryName());
    }

    public void testThrowsExceptionWhenSourceDirectoryIsFile() throws IOException {
        final String filename = sourceDirectory + "/" + "file";
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,filename,targetDirectory);
        createFile(filename, createdFiles);
        NotADirectoryBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NotADirectoryBuildException e) {
            expected = e;
        }
        assertEquals(filename,expected.getDirectoryName());
    }

    public void testThrowsExceptionWhenTargetDirectoryIsFile() throws IOException {
        final String filename = sourceDirectory + "/" + "file";
        final ComponentMetadataTask task = new ComponentMetadataTask(interfaceName,filename,targetDirectory);
        createFile(filename, createdFiles);
        NotADirectoryBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NotADirectoryBuildException e) {
            expected = e;
        }
        assertEquals(filename,expected.getDirectoryName());
    }

    public void testThrowsAnExceptionWhenInterfaceDoesNotExist() {
        final ComponentMetadataTask task = new ComponentMetadataTask(fullyQualifiedInterfaceName,sourceDirectory,targetDirectory);
        NoSuchJavaSourceFileBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NoSuchJavaSourceFileBuildException e) {
            expected = e;
        }
        assertEquals(fullyQualifiedInterfaceName, expected.getInterfaceName());
    }

    public void testThrowsAnExceptionWhenNoJavadoc() throws IOException {
        createJavaSourceFile(sourceDirectory, new Interface(
                packageName,
                interfaceName,
                null,
                null), createdFiles);
        final ComponentMetadataTask task = new ComponentMetadataTask(fullyQualifiedInterfaceName,sourceDirectory,targetDirectory);
        NoJavadocBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NoJavadocBuildException e) {
            expected = e;
        }
        assertEquals(fullyQualifiedInterfaceName, expected.getInterfaceName());
    }

/*
    public void testThrowsAnExceptionWhenInterfaceIsAClass() throws IOException {
        createJavaSourceFile(sourceDirectory, new Javaclass(
                packageName,
                interfaceName,
                someJavadoc,
                null), createdFiles);
        final ComponentMetadataTask task = new ComponentMetadataTask(fullyQualifiedInterfaceName,sourceDirectory,targetDirectory);
        NotAPublicInterfaceBuildException expected = null;
        try {
            task.execute();
            fail("Did not get expected exception");
        } catch (NotAPublicInterfaceBuildException e) {
            expected = e;
        }
        assertEquals(fullyQualifiedInterfaceName, expected.getInterfaceName());
    }
*/

    public void testCanGenerateCorrectMetadataForOneInterface() throws IOException, ParserConfigurationException, SAXException {
        createJavaSourceFile(sourceDirectory, new Interface(
                packageName,
                interfaceName,
                someJavadoc,
                interfaceBody), createdFiles);
        final ComponentMetadataTask task = new ComponentMetadataTask(fullyQualifiedInterfaceName,sourceDirectory,targetDirectory);
        task.execute();
        File metadataFile = new File(targetDirectory,ComponentMetadataTask.metadataFilename);
        assertTrue("Metadata file (generated) does not exist", metadataFile.exists());
        final ComponentMetadata componentMetadata = ComponentMetadata.fromXml(metadataFile);
        final InterfaceMetadata interfaceMetadata = componentMetadata.getInterfaceMetadataForName(fullyQualifiedInterfaceName);
        assertEquals(packageName,interfaceMetadata.getPackageName());
        assertEquals(interfaceName,interfaceMetadata.getName());
        assertEquals(fullyQualifiedInterfaceName, interfaceMetadata.getFullyQualifiedName());
        assertEquals(someJavadoc,interfaceMetadata.getJavadoc());
        assertEquals(firstLineOfJavadoc, interfaceMetadata.getShortDescription());
    }

    public void testCanGenerateCorrectMetadataForTwoInterfaces() throws IOException, ParserConfigurationException, SAXException {
        createJavaSourceFile(sourceDirectory, new Interface(
                packageName,
                interfaceName,
                someJavadoc,
                interfaceBody), createdFiles);
        String secondInterfaceName = "SecondInterface";
        String firstLineOfJavadocForSecondInterface = "This is another first line.";
        String secondJavadoc = firstLineOfJavadocForSecondInterface + " " + someJavadoc;
        String secondFullyQualifiedInterfaceName = packageName + "." + secondInterfaceName;
        createJavaSourceFile(sourceDirectory, new Javaclass(
                packageName,
                secondInterfaceName,
                secondJavadoc,
                interfaceBody), createdFiles);
        final ComponentMetadataTask task = new ComponentMetadataTask(new String[]{fullyQualifiedInterfaceName, secondFullyQualifiedInterfaceName},sourceDirectory,targetDirectory);
        task.execute();
        File metadataFile = new File(targetDirectory,ComponentMetadataTask.metadataFilename);
        assertTrue("Metadata file (generated) does not exist", metadataFile.exists());
        final ComponentMetadata componentMetadata = ComponentMetadata.fromXml(metadataFile);
        assertEquals(2,componentMetadata.numInterfaces());
        InterfaceMetadata interfaceMetadata = componentMetadata.getInterfaceMetadataForName(fullyQualifiedInterfaceName);
        assertEquals(packageName,interfaceMetadata.getPackageName());
        assertEquals(interfaceName,interfaceMetadata.getName());
        assertEquals(fullyQualifiedInterfaceName, interfaceMetadata.getFullyQualifiedName());
        assertEquals(someJavadoc,interfaceMetadata.getJavadoc());
        assertEquals(firstLineOfJavadoc, interfaceMetadata.getShortDescription());

        interfaceMetadata = componentMetadata.getInterfaceMetadataForName(secondFullyQualifiedInterfaceName);
        assertEquals(packageName,interfaceMetadata.getPackageName());
        assertEquals(secondInterfaceName,interfaceMetadata.getName());
        assertEquals(secondFullyQualifiedInterfaceName, interfaceMetadata.getFullyQualifiedName());
        assertEquals(secondJavadoc,interfaceMetadata.getJavadoc());
        assertEquals(firstLineOfJavadocForSecondInterface, interfaceMetadata.getShortDescription());
    }

    private void createJavaSourceFile(String targetDirectory, JavaSourceFile javaSourceFile, final Collection collectingParameter) throws IOException {
        File file = new File(targetDirectory + File.separator + javaSourceFile.sourceFileRelativePath() + File.separator + javaSourceFile.getInterfaceName() + ".java");
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write("// this file generated by Junit test case - feel free to delete it\n");
        writer.write("package " + javaSourceFile.getPackageName() + ";\n\n");
        writer.write("/**\n");
        writer.write(" * " + javaSourceFile.getJavadoc() + "\n");
        writer.write(" */\n");
        writer.write("public " + javaSourceFile.getJavaType() + " " + javaSourceFile.getInterfaceName() + " {\n");
        writer.write(javaSourceFile.getInterfaceBody());
        writer.write("}");

        writer.close();
        collectingParameter.add(file);
    }

    private void createDirectory(String directoryName) {
        File f = new File(directoryName);
        f.mkdirs();
    }

    private void createFile(String filename, final Collection collectingParameter) throws IOException {
        File file = new File(filename);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        writer.write("// this file generated by Junit test case - feel free to delete it\n");
        writer.close();
        collectingParameter.add(file);
    }

    private static abstract class JavaSourceFile {
        protected final String packageName;
        protected final String interfaceName;
        protected final String javadoc;
        protected final String interfaceBody;

        public JavaSourceFile(String packageName, String interfaceName, String javadoc, String interfaceBody) {
            this.packageName = packageName;
            this.interfaceName = interfaceName;
            this.javadoc = javadoc != null ? javadoc : "";
            this.interfaceBody = interfaceBody != null ? interfaceBody : "//empty";
        }

        public String sourceFileRelativePath() {
            return packageName.replaceAll("\\.","/");
        }

        public String getInterfaceName() {
            return interfaceName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getJavadoc() {
            return javadoc;
        }

        public String getInterfaceBody() {
            return interfaceBody;
        }

        public abstract String getJavaType();

    }

    private static class Interface extends JavaSourceFile {
        public Interface(String packageName, String interfaceName, String javadoc, String interfaceBody) {
            super(packageName, interfaceName, javadoc, interfaceBody);
        }

        public String getJavaType() {
            return "interface";
        }
    }

    private static class Javaclass extends JavaSourceFile {
        public Javaclass(String packageName, String interfaceName, String javadoc, String interfaceBody) {
            super(packageName, interfaceName, javadoc, interfaceBody);
        }

        public String getJavaType() {
            return "class";
        }
    }
}
