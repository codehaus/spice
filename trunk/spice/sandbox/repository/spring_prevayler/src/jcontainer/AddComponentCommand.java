package jcontainer;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import java.io.Serializable;

public class AddComponentCommand implements Command {
    private final Component component;

    public AddComponentCommand(Component component) {
        this.component = component;
    }

    public Serializable execute(PrevalentSystem system) throws Exception {
        final Repository rep = (Repository) system;
        if ( rep.contains(component)) {
            throw new RuntimeException("Component " + component.getName() + " already registered.");
        }
        rep.add(component);
        return null;
    }
}
