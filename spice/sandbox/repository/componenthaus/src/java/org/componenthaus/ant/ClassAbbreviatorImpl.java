package org.componenthaus.ant;

import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.AbstractJavaEntity;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.DocletTag;

import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

public class ClassAbbreviatorImpl implements ClassAbbreviator {
    private static final String JAVA_LANG_PACKAGE = "java.lang.";
    private static final String DOT = ".";
    private final JavadocFormatter javadocFormatter;
    private final LineIndenter indenter;

    public ClassAbbreviatorImpl(JavadocFormatter javadocFormatter, LineIndenter indenter) {
        this.javadocFormatter = javadocFormatter;
        this.indenter = indenter;
    }

    public int abbreviate(JavaClass aClass, StringBuffer collector) {
        if (aClass.isInterface()) {
            throw new NotAClassException(aClass.getFullyQualifiedName());
        }
        if (!aClass.isPublic()) {
            throw new NotPublicException(aClass.getFullyQualifiedName());
        }
        Set importsCollector = new HashSet();
        StringBuffer bodyCollector = new StringBuffer();

        int publicMethodCount = 0;
        JavaMethod[] methods = aClass.getMethods();
        for (int i = 0; i < methods.length; i++) {
            JavaMethod method = methods[i];
            if (method.isPublic()) {
                if (publicMethodCount > 0) {
                    bodyCollector.append("\n");
                }
                System.out.println("About to call method text on method " + method.getName());
                bodyCollector.append(createMethodText(method, importsCollector));
                publicMethodCount++;
            }
        }

        System.out.println("About to do package text");
        collector.append(createPackageText(aClass));
        System.out.println("About to do imports text");
        collector.append(createImportsText(importsCollector));
        System.out.println("About to do class begnnning text");
        collector.append(createClassBeginningText(aClass));
        System.out.println("About to do body text");
        collector.append(bodyCollector);
        collector.append("}");

        return publicMethodCount;
    }

    private String createPackageText(JavaClass aClass) {
        return "package " + aClass.getPackage() + ";\n\n";
    }

    private String createImportsText(Collection imports) {
        StringBuffer result = new StringBuffer();
        for (Iterator i = imports.iterator(); i.hasNext();) {
            result.append("import ").append(i.next()).append(";\n");
        }
        result.append("\n");
        return result.toString();
    }

    private String createMethodText(JavaMethod method, Collection importsCollector) {
        final StringBuffer result = new StringBuffer();
        String unformattedJavadoc = "";
        if (method.getComment() != null) {
            unformattedJavadoc = method.getComment() + "\n";
        }
        if (method.getTags().length != 0) {
            unformattedJavadoc += createJavadocTagsText(method.getTags());
        }
        if (unformattedJavadoc.length() > 0) {
            final String javadoc = javadocFormatter.format(unformattedJavadoc);
            result.append(indenter.indent(javadoc, 1)).append("\n");
        }
        result.append("\t");
        appendModifiers(method, result);
        result.append(getType(method.getReturns(), importsCollector)).append(" ");
        result.append(method.getName()).append("(");
        result.append(createParameterListText(method, importsCollector));
        result.append(")");
        if (method.getExceptions().length > 0) {
            result.append(" throws ");
            result.append(createExceptionDeclarationText(method, importsCollector));
        }
        result.append(";\n");
        return result.toString();
    }

    private String createJavadocTagsText(DocletTag[] tags) {
        System.out.println("Entering createjavadoctagstext");
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < tags.length; i++) {
            DocletTag tag = tags[i];
            System.out.println("Working on tag " + tag.getName());
            result.append("@").append(tag.getName());
            System.out.println("Appended name");
            final String[] params = tag.getParameters();
            System.out.println("Num params " + params.length);
            for (int j = 0; j < params.length; j++) {
                System.out.println("Working on param " + params[j]);
                //TODO remove this check for * when QDox is fixed - it includes the * here for some reason
                if (!"*".equals(params[j])) {
                    result.append(" ");
                    result.append(params[j]);
                }
            }
            System.out.println("Finished with params");
            //result.append(" ").append(tag.getValue()).append("\n");
            result.append("\n");
        }
        System.out.println("Leaving createjavadoctagstext");
        return result.toString();
    }

    private String createExceptionDeclarationText(JavaMethod method, Collection importsCollector) {
        StringBuffer result = new StringBuffer();
        Type[] exceptions = method.getExceptions();
        for (int i = 0; i < exceptions.length; i++) {
            Type exception = exceptions[i];
            if (i > 0) {
                result.append(", ");
            }
            result.append(getType(exception, importsCollector));
        }
        return result.toString();
    }

    private String getType(Type type, Collection importsCollector) {
        final String fullTypeName = type.getValue();

        String result = fullTypeName;

        if (!fullTypeName.startsWith(JAVA_LANG_PACKAGE)) {
            importsCollector.add(fullTypeName);
        }
        int indexOfLastDot = fullTypeName.lastIndexOf(DOT);
        if (indexOfLastDot != -1) {
            result = fullTypeName.substring(indexOfLastDot + 1);
        }
        return result;
    }

    private String createParameterListText(JavaMethod method, Collection importsCollector) {
        JavaParameter[] parameters = method.getParameters();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < parameters.length; i++) {
            JavaParameter parameter = parameters[i];
            if (i > 0) {
                result.append(", ");
            }
            result.append(getType(parameter.getType(), importsCollector)).append(" ").append(parameter.getName());
        }
        return result.toString();
    }

    private String createClassBeginningText(JavaClass aClass) {
        final StringBuffer result = new StringBuffer();
        if ( aClass.getComment() != null) {
            result.append(javadocFormatter.format(aClass.getComment())).append("\n");
        }
        appendModifiers(aClass, result);
        result.append("class ");
        result.append(aClass.getName()).append(" {\n");
        return result.toString();
    }

    private void appendModifiers(AbstractJavaEntity entity, final StringBuffer result) {
        final String[] modifiers = entity.getModifiers();
        for (int i = 0; i < modifiers.length; i++) {
            result.append(modifiers[i]).append(" ");
        }
    }
}
