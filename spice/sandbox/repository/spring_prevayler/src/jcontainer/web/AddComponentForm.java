package jcontainer.web;

import com.interface21.web.servlet.mvc.SimpleFormController;
import com.interface21.web.servlet.ModelAndView;
import jcontainer.Component;
import jcontainer.ComponentImpl;
import jcontainer.AddComponentCommand;

import javax.servlet.ServletException;

import org.prevayler.Prevayler;

public class AddComponentForm extends SimpleFormController {
    public AddComponentForm() {
        setCommandClass(ComponentImpl.class);
    }

    protected ModelAndView onSubmit(Object command) throws ServletException {
        final Prevayler p = (Prevayler) getApplicationContext().getBean("prevayler");
        try {
            p.executeCommand(new AddComponentCommand((Component) command));
        } catch (Exception e) {
            throw new ServletException("Exception adding component to repository",e);
        }
        return super.onSubmit(command);
    }
}