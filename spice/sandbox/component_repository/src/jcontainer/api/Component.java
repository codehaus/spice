package jcontainer.api;

import java.io.Serializable;

public interface Component extends Serializable {
    public String getName();
    public void setName(String name);

    public String getTheInterface();
    public void setTheInterface(String theInterface);

    public String getVersion();
    public void setVersion(String version);

    public Container getContainer();
    public void setContainer(Container container);
}
