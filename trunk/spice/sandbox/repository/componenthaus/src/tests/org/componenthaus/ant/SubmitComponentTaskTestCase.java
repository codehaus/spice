package org.componenthaus.ant;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildException;
import org.componenthaus.util.file.MockFile;
import org.componenthaus.util.file.MockFileManager;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.DocletTag;

import java.util.Map;
import java.util.HashMap;

public class SubmitComponentTaskTestCase extends TestCase {
    private static final String VALID_BINARY = "valid binary";
    private static final String VALID_DIR = ".";
    private static final String VALID_SERVICE_INTERFACE = "valid service interface";

    public void testCanCreateMetadataFile() {
        final SubmitComponentTask task = createValidTask();
        final MockFileManager mockFileManager = new MockFileManager();
        final MockFile mockFile = new MockFile();
        mockFile.setupExists(true);
        mockFileManager.setupNewFile(mockFile);
        task.setFileManager(mockFileManager);
        final JavaSource javaSource = new JavaSource();
        final String javadoc = "/**\n" +
                                "*\n" +
                                "* @version 1.0\n" +
                                "* @author An Author\n" +
                                "*/";
        final MockJavaClass javaClass = new MockJavaClass(VALID_SERVICE_INTERFACE, javadoc);
        javaClass.setModifiers(new String[]{"public"});
        javaClass.setInterface(true);
        javaClass.setName(VALID_SERVICE_INTERFACE);
        javaClass.addTag("version","1.0");
        javaClass.addTag("author","An Author");
        javaSource.addClass(javaClass);
        final JavaSource []sources = {javaSource};
        final MockJavadocBuilder mockJavadocBuilder = new MockJavadocBuilder(sources);
        final MockJavadocBuilderFactory mockJavadocBuilderFactory = new MockJavadocBuilderFactory(mockJavadocBuilder);
        task.setJavadocBuilderFactory(mockJavadocBuilderFactory);
        task.execute();
    }

    private static final class MockJavaClass extends JavaClass {
        private String fullyQualifiedName;
        private String javadoc;
        private final Map tags;

        public MockJavaClass(String fullyQualifiedName, String javadoc) {
            this.fullyQualifiedName = fullyQualifiedName;
            this.javadoc = javadoc;
            tags = new HashMap();
        }

        public void addTag(final String name, final String value) {
            tags.put(name,value);
        }

        public DocletTag getTagByName(String s) {
            return new DocletTag(s,(String) tags.get(s));
        }

        public String getFullyQualifiedName() {
            return fullyQualifiedName;
        }

        public String getComment() {
            return javadoc;
        }
    }

    private static final class MockJavadocBuilder extends JavaDocBuilder {
        private final JavaSource[] preparedSources;

        public MockJavadocBuilder(JavaSource[] preparedSources) {
            this.preparedSources = preparedSources;
        }

        public JavaSource[] getSources() {
            return preparedSources;
        }
    }

    public void testWillRejectNonExistantBinaryFile() {
        final String crazyBinaryName = "binary that does not exist";
        final SubmitComponentTask task = createTask(crazyBinaryName,VALID_DIR,VALID_DIR,VALID_SERVICE_INTERFACE);
        BuildException expected = null;
        try {
            task.execute();
            fail("Should have failed due to non-existant binary file");
        } catch (BuildException e) {
            expected = e;
        }
        assertTrue(expected.getMessage().matches("File " + crazyBinaryName + " does not exist"));
    }

    public void testWillRejectNonExistantStagingDir() {
        final SubmitComponentTask task = createTask(VALID_BINARY,VALID_DIR,"dir that does not exist",VALID_SERVICE_INTERFACE);
        BuildException expected = null;
        try {
            task.execute();
            fail("Should have failed due to non-existant staging directory");
        } catch (BuildException e) {
            expected = e;
        }
        assertTrue(expected.getMessage().indexOf("does not exist") != -1);
    }

    public void testWillRejectNonExistantSourceDir() {
        final SubmitComponentTask task = createTask(VALID_BINARY,"dir that does not exist",VALID_DIR,VALID_SERVICE_INTERFACE);
        BuildException expected = null;
        try {
            task.execute();
            fail("Should have failed due to non-existant staging directory");
        } catch (BuildException e) {
            expected = e;
        }
        assertTrue(expected.getMessage().indexOf("does not exist") != -1);
    }

    public void testWillRejectNullServiceInterface() {
        tryWithANull(VALID_BINARY, VALID_DIR, VALID_DIR, null, "serviceInterface");
    }

    public void testWillRejectNullBinary() {
        tryWithANull(null, VALID_DIR, VALID_DIR, VALID_SERVICE_INTERFACE, "binary");
    }

    public void testWillRejectNullSource() {
        tryWithANull(VALID_BINARY, null, VALID_DIR, VALID_SERVICE_INTERFACE, "source");
    }

    public void testWillRejectNullStaging() {
        tryWithANull(VALID_BINARY, VALID_DIR, null, VALID_SERVICE_INTERFACE, "staging");
    }

    private void tryWithANull(final String binary, final String source, final String staging, final String serviceInterface, final String fieldName) {
        SubmitComponentTask task = createTask(binary, source, staging, serviceInterface);
        BuildException expected = null;
        try {
            task.execute();
            fail("Should have failed due to " + fieldName + " being null");
        } catch (BuildException e) {
            expected = e;
        }
        assertTrue(expected.getMessage().indexOf(fieldName) != -1);
    }

    private SubmitComponentTask createTask(final String binary, final String source, final String staging, final String serviceInterface) {
        final SubmitComponentTask task = new SubmitComponentTask();
        task.setBinary(binary);
        task.setSource(source);
        task.setStaging(staging);
        task.setServiceInterface(serviceInterface);
        return task;
    }

    private SubmitComponentTask createValidTask() {
        return createTask(VALID_BINARY,VALID_DIR,VALID_DIR,VALID_SERVICE_INTERFACE);
    }
}
