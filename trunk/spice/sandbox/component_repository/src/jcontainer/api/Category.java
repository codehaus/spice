package jcontainer.api;

import java.io.Serializable;
import java.util.Collection;

public interface Category extends Serializable {
    void addComponent(Component component);
    String getName();
    Collection getMatchingComponents(String componentNameRegexp);
}
