package org.componenthaus.ant;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import junit.framework.TestCase;

import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

public class ClassAbbreviatorImplTestCase extends TestCase {
    private ClassAbbreviator abbreviator = null;
    private JavadocFormatter javadocFormatter = null;

    protected void setUp() throws Exception {
        javadocFormatter = new JavadocFormatterImpl(80);
        abbreviator = new ClassAbbreviatorImpl(javadocFormatter, new LineIndenterImpl());
    }

    public void testThrowsExceptionIfNotAClass() {
        final String interfaceName = "AnInterface";
        final String packageName = "com.acme";
        final MockJavaClass anInterface = new MockJavaClass(packageName,interfaceName);
        anInterface.setInterface(true);
        NotAClassException expected = null;
        try {
            abbreviator.abbreviate(anInterface, new StringBuffer());
            fail("Did not get expected exception");
        } catch (NotAClassException e) {
            expected = e;
        }
        assertEquals(packageName + "." + interfaceName, expected.getName());
    }

    public void testWillIgnoreNonPublicMembers() {
        final String interfaceName = "AnInterface";
        final String packageName = "com.acme";
        final MockJavaClass aClass = new MockJavaClass(packageName,interfaceName);

        final JavaMethod privateMethod = new JavaMethod();
        privateMethod.setModifiers(new String[]{"private"});
        final JavaMethod protectedMethod = new JavaMethod();
        protectedMethod.setModifiers(new String[]{"protected"});
        final JavaMethod packageMethod = new JavaMethod();
        aClass.addMethod(privateMethod);
        aClass.addMethod(protectedMethod);
        aClass.addMethod(packageMethod);
        final StringBuffer collector = new StringBuffer();
        assertEquals(0, abbreviator.abbreviate(aClass, collector));
    }

    public void testWillFindPublicMethods() throws IOException {

        final String sourceCode =
                "package com.acme.thing;\n" +
                "\n" +
                "import com.acme.OtherThing;\n" +
                "import com.acme.AcmeException;\n" +
                "import java.io.File;\n" +
                "\n" +
                "/**\n" +
                " * This class does something.  \n" +
                " * Explained in more detail.\n" +
                " * Explained in even more detail.\n" +
                " * Explained in yet more detail again.\n" +
                " **/\n" +
                "public final class AClassName {\n" +
                "   /**\n" +
                "   * Returns the basename of the supplied File.  The basename \n" +
                "   * is whats left when you take the directory away \n" +
                "   * \n" +
                "   * @param file the file to get the basename from" +
                "   * @return the basename" +
                "   **/" +
                "   public static final String basename(File file) throws AcmeException{\n" +
                "       int i = 0;\n" +
                "   }\n" +
                "   public String instanceBasename(File file){\n" +
                "       int i = 0;\n" +
                "   }\n" +
                "   private static final String privateBasename(File file) throws AcmeException{\n" +
                "       int i = 0;\n" +
                "   }\n" +
                "}";
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSource(new StringReader(sourceCode));
        final JavaClass aClass = builder.getClassByName("com.acme.thing.AClassName");
        final StringBuffer bodyCollector = new StringBuffer();

        assertEquals(2, abbreviator.abbreviate(aClass, bodyCollector));
        final String formatted = bodyCollector.toString();
        assertEquals(1,countMatches(formatted,"package com.acme.thing;"));
        assertEquals(1,countMatches(formatted,"import java.io.File;"));
        assertEquals(1,countMatches(formatted,"import com.acme.AcmeException;"));
        assertEquals(2,countMatches(formatted,"import .*")); //Only two import expected, those actually referenced
        assertEquals(1,countMatches(formatted,"public final class AClassName \\{"));
        assertEquals(1,countMatches(formatted," \\* This class does something. Explained in more detail.*"));
        assertEquals(1,countMatches(formatted, "\\s*public final static String basename\\(File file\\) throws AcmeException;"));
        assertEquals(1,countMatches(formatted, "\\s*public String instanceBasename\\(File file\\);"));
        assertEquals(1,countMatches(formatted, ".*\\* Returns the basename of the supplied File\\..*"));
        assertEquals(1,countMatches(formatted, ".*\\* @param file the file to get the basename from"));
        assertEquals(1,countMatches(formatted, ".*\\* @return the basename"));


    }

    private int countMatches(String string, String regexp) throws IOException {
        int matches = 0;
        final BufferedReader reader = new BufferedReader(new StringReader(string));
        String line = reader.readLine();
        while ( line != null ) {
            if ( line.matches(regexp) ) {
                matches++;
            }
            line = reader.readLine();
        }
        reader.close();
        return matches;
    }

    private static final class MockJavaClass extends JavaClass {
        private final String packageName;
        private final String className;

        public MockJavaClass(String packageName, String className) {
            super(null);
            this.packageName = packageName;
            this.className = className;
            setName(className);
        }

        public String getFullyQualifiedName() {
            return packageName + "." + className;
        }

        public boolean isPublic() {
            return true;
        }

        public String getPackage() {
            return packageName;
        }
    }
}
