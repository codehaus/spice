package org.componenthaus.ant;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.componenthaus.ant.metadata.ComponentDefinition;
import org.componenthaus.ant.metadata.ComponentImplementation;
import org.componenthaus.ant.metadata.ComponentMetadata;
import org.componenthaus.ant.metadata.Resource;
import org.componenthaus.util.text.TextUtils;
import org.xml.sax.SAXException;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.nio.channels.FileChannel;

public class ComponentHausSubmitTask extends Task {
    private String serviceInterface = null;
    private String staging = null;
    private String source = null;
    private String binary = null;

    public void setBinary(String binary) {
        this.binary = binary;
    }

    public void execute() throws BuildException {
        notNull(serviceInterface, "serviceInterface");
        notNull(staging, "staging");
        notNull(source, "source");
        notNull(source, "binary");
        assertDirExists(staging);
        assertDirExists(source);
        assertFileExists(binary);

        compileMetadata();

        File newDir = new File(staging + File.separator + "binary");
        if ( ! newDir.exists() ) {
            if ( ! newDir.mkdir()) {
                throw new BuildException("Could not create dir " + newDir.getAbsoluteFile());
            }
        }
        String sourceFilename = binary;
        final int lastIndexOfSlash = Math.max(binary.lastIndexOf("/"),binary.lastIndexOf("\\"));
        if ( lastIndexOfSlash != -1 ) {
            sourceFilename = binary.substring(lastIndexOfSlash + 1);
        }
        File binaryFile = new File(binary);
        File newBinaryFile = new File(newDir,sourceFilename);
        try {
            copyFile(binaryFile,newBinaryFile);
        } catch (Exception e) {
            throw new BuildException("Exception trying to copy " + binaryFile.getAbsolutePath() + " to " + newBinaryFile.getAbsolutePath(),e);
        }
    }

    private void compileMetadata() {
        final JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSourceTree(new File(source));
        final JavaSource[] sources = builder.getSources();
        final JavaClass serviceInterfaceClass = getServiceInterfaceClass(sources);
        if (serviceInterfaceClass == null) {
            throw new BuildException("Could not find a public interface called " + serviceInterface);
        }
        final ComponentDefinition componentDefinition = extractComponentMetadata(serviceInterfaceClass);
        final Collection impls = findImplementations(serviceInterfaceClass, sources, builder);
        final ComponentMetadata componentMetadata = new ComponentMetadata(componentDefinition, impls);
        writeComponentMetaData(componentMetadata);
    }

    private void assertFileExists(String name) {
        File f = new File(name);
        if (!f.exists()) {
            throw new BuildException("File " + name + " does not exist");
        }
    }

    public void copyFile(File in, File out) throws Exception {
        FileChannel sourceChannel = new
                FileInputStream(in).getChannel();
        FileChannel destinationChannel = new
                FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }

    private void assertDirExists(final String dirname) {
        File stagingDir = new File(dirname);
        if (!stagingDir.exists() || !stagingDir.isDirectory()) {
            throw new BuildException(dirname + " either does not exist or is not a directory");
        }
    }

    private JavaClass getServiceInterfaceClass(final JavaSource[] sources) {
        JavaClass result = null;
        for (int i = 0; i < sources.length; i++) {
            final JavaClass[] classes = sources[i].getClasses();
            for (int j = 0; j < classes.length; j++) {
                final JavaClass javaClass = classes[j];
                if (javaClass.isInterface() && javaClass.isPublic() && serviceInterface.equals(javaClass.getFullyQualifiedName())) {
                    result = javaClass;
                }
            }
        }
        return result;
    }

    private Collection findImplementations(JavaClass intf, JavaSource[] sources, JavaDocBuilder builder) {
        final Collection result = new HashSet();
        for (int s = 0; s < sources.length; s++) {
            findImplementations(intf, sources[s].getClasses(), result, builder);
        }
        return result;
    }

    private void findImplementations(JavaClass intf, JavaClass[] classes, Collection result, JavaDocBuilder builder) {
        for (int c = 0; c < classes.length; c++) {
            if (implementsType(intf.asType(), classes[c].getImplements())) {
                result.add(createComponentImplementation(classes[c], builder));
            }
        }
    }

    private ComponentImplementation createComponentImplementation(JavaClass aClass, JavaDocBuilder builder) {
        final String fullDesc = format(assertFullDescription(aClass));
        return new ComponentImplementation(aClass.getFullyQualifiedName(),
                assertTagValyeByName(aClass, "version"),
                assertTagValuesByName(aClass, "author"),
                assertOneLineDescription(aClass),
                fullDesc,
                getServiceDependencies(aClass, builder),
                getResourceDependencies(aClass)
        );
    }

    private String format(String text) {
        text = TextUtils.replace("\\s*/\\**\\s*","",text); // Opening javadoc quote
        text = TextUtils.replace("\\s*\\*+\\s*","",text); // Opening javadoc quote
        text = TextUtils.replace("\\s*\\**/\\s*","",text); //closing javadoc
        text = TextUtils.replace("<p>","",text);
        text = TextUtils.replace("<P>","",text);
        text = TextUtils.replace("</P>","",text);
        text = TextUtils.replace("</p>","",text);
        text = TextUtils.replace("\n","",text);
        return TextUtils.fmt(text);
    }

    /**
     * TODO fill this out to return what jars this impl needs
     */
    private Resource[] getResourceDependencies(JavaClass aClass) {
        return new Resource[0];
    }

    /**
     * Find out what other interface's this impl depends on
     */
    private ComponentDefinition[] getServiceDependencies(final JavaClass impl, final JavaDocBuilder builder) {
        final JavaMethod constructor = getDependencyConstructor(impl);
        final JavaParameter[] params = constructor.getParameters();
        final Collection result = new ArrayList();
        for (int i = 0; i < params.length; i++) {
            result.add(extractComponentMetadata(builder.getClassByName(params[i].getType().getValue())));
        }
        return (ComponentDefinition[]) result.toArray(new ComponentDefinition[result.size()]);
    }

    private JavaMethod getDependencyConstructor(final JavaClass impl) {
        final JavaMethod[] methods = impl.getMethods();
        JavaMethod result = null;
        int maxConstructorParams = -1;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isConstructor() && methods[i].getParameters().length > maxConstructorParams) {
                maxConstructorParams = methods[i].getParameters().length;
                result = methods[i];
            }
        }
        return result;
    }

    private boolean implementsType(Type type, Type[] types) {
        boolean result = false;
        int index = 0;
        while (!result && index < types.length) {
            result = type.equals(types[index++]);
        }
        return result;
    }


    private ComponentDefinition extractComponentMetadata(final JavaClass intf) {
        return new ComponentDefinition(intf.getFullyQualifiedName(),
                assertTagValyeByName(intf, "version"),
                assertTagValuesByName(intf, "author"),
                assertOneLineDescription(intf),
                format(assertFullDescription(intf)),
                format(intf.toString()));
    }

    private String assertFullDescription(final JavaClass intf) {
        final String fullDescription = getFullDescription(intf);
        if (fullDescription == null || "".equals(fullDescription)) {
            throw new BuildException("Could not find javadoc that explains the purpose of the file " + intf.getFullyQualifiedName());
        }
        return fullDescription;
    }

    private String assertOneLineDescription(final JavaClass intf) {
        final String oneLineDescription = getOneLineDescription(intf);
        if (oneLineDescription == null || "".equals(oneLineDescription)) {
            throw new BuildException("Could not find javadoc that explains the purpose of the file " + intf.getFullyQualifiedName());
        }
        return oneLineDescription;
    }

    private List assertTagValuesByName(final JavaClass intf, final String tagName) {
        final List authors = getTagValuesByName(intf, tagName);
        if (authors.size() == 0) {
            throw new BuildException("Could not find any @" + tagName + " javadoc tags in file " + intf.getFullyQualifiedName());
        }
        return authors;
    }

    private String assertTagValyeByName(final JavaClass intf, final String tagName) {
        final String version = getTagValueByName(intf, tagName);
        if (version == null) {
            throw new BuildException("Could not find a @" + tagName + " javadoc tag in file " + intf.getFullyQualifiedName());
        }
        return version;
    }

    private String getFullDescription(JavaClass intf) {
        return intf.getComment();
    }

    private String getOneLineDescription(JavaClass intf) {
        String comment = intf.getComment();
        final int indexOfSentenceEnd = comment.indexOf(".");
        if (indexOfSentenceEnd != -1) {
            comment = comment.substring(0, indexOfSentenceEnd);
        }
        return comment;
    }

    private List getTagValuesByName(JavaClass intf, String name) {
        final DocletTag[] tags = intf.getTagsByName(name);
        final List result = new ArrayList();
        for (int i = 0; i < tags.length; i++) {
            result.add(tags[i].getValue());
        }
        return result;
    }

    private String getTagValueByName(JavaClass intf, String name) {
        final DocletTag tag = intf.getTagByName(name);
        return tag != null ? tag.getValue() : null;
    }

    private void writeComponentMetaData(final Object object) {
        final String metadataFilename = staging + File.separator + "metadata.xml";
        FileWriter writer = null;
        try {
            writer = new FileWriter(metadataFilename);
        } catch (IOException e) {
            throw new BuildException("Exception trying to open metadata file for writing " + metadataFilename, e);
        }
        try {
            writer.write("<?xml version='1.0' ?>");
            BeanWriter beanWriter = new BeanWriter(writer);
            beanWriter.getXMLIntrospector().setAttributesForPrimitives(false);
            beanWriter.setWriteIDs(false);
            beanWriter.enablePrettyPrint();
            beanWriter.write("component", object);
            writer.close();
        } catch (IOException e) {
            throw new BuildException("Exception writing to metadata file " + metadataFilename, e);
        } catch (SAXException e) {
            throw new BuildException("XML exception while writing to metadata file " + metadataFilename, e);
        } catch (IntrospectionException e) {
            throw new BuildException("Reflection exception while writing to metadata file " + metadataFilename, e);
        }
    }

    private void notNull(Object s, String name) {
        if (s == null) {
            throw new BuildException("Did not specify parameter " + name + " to ant task " + this.getClass());
        }
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setStaging(String staging) {
        this.staging = staging;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
