package org.componenthaus.tests;

import org.componenthaus.ant.ClassAbbreviator;
import com.thoughtworks.qdox.model.JavaClass;
import junit.framework.Assert;

public class MockClassAbbreviator implements ClassAbbreviator {
    private String expectedPackageName;
    private String expectedClassName;
    private String actualPackageName;
    private String actualClassName;
    private int setupAbbreviateReturn = 0;

    public int abbreviate(JavaClass aClass, StringBuffer collector) {
        this.actualPackageName = aClass.getPackage();
        this.actualClassName = aClass.getName();
        return setupAbbreviateReturn;
    }

    public void setupAbbreviateReturn(int i) {
        setupAbbreviateReturn = i;
    }

    public void verify() {
        Assert.assertEquals(expectedPackageName,actualPackageName);
        Assert.assertEquals(expectedClassName,actualClassName);
    }

    public void setupExpectedJavaClassDetails(String packageName, String className) {
        this.expectedPackageName = packageName;
        this.expectedClassName = className;
    }
}
