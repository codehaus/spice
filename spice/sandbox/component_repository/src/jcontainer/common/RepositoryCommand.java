package jcontainer.common;

import org.prevayler.Command;
import org.prevayler.PrevalentSystem;

import java.io.Serializable;

import jcontainer.Repository;

public abstract class RepositoryCommand implements Command {
    public Serializable execute(PrevalentSystem prevalentSystem) throws Exception {
        return execute((Repository) prevalentSystem);
    }

    protected abstract Serializable execute(final Repository repository) throws Exception;
}
