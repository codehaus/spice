package org.componenthaus.ant;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Given a QDox JavaClass representing a class (not an interface), provide
 * a string representation of the source code for that class, but containing
 * only the public method signatures.  All non-public members are removed, as are
 * all method bodies.
 */
public interface ClassAbbreviator {
    /**
     * Given a QDox JavaClass representing a class (not an interface), provide
     * a string representation of the source code for that class, but containing
     * only the public method signatures.  All non-public members are removed,
     * as are all method bodies.
     *
     * @param aClass the class to abbreviate
     * @param collector will contain the public methods as a formatted block
     * @return the number of public members in the result
     */
    int abbreviate(JavaClass aClass, StringBuffer collector);
}
