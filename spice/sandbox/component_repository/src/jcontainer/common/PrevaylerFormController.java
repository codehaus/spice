package jcontainer.common;

import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.SimpleFormController;
import org.prevayler.Prevayler;

import javax.servlet.ServletException;

import jcontainer.impl.JContainerFactory;

public abstract class PrevaylerFormController extends SimpleFormController {

    protected ModelAndView onSubmit(Object formData) throws ServletException {
        final Prevayler p = (Prevayler) getApplicationContext().getBean("prevayler");
        final JContainerFactory factory = (JContainerFactory) getApplicationContext().getBean("factory");
        ModelAndView modelAndView = null;
        try {
            modelAndView = onSubmit(p, factory, formData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Exception trying to " + formPurpose(),e);
        }
        if ( modelAndView == null ) {
            modelAndView = super.onSubmit(formData);
        }
        return modelAndView;
    }

    protected abstract ModelAndView onSubmit(final Prevayler prevayler, JContainerFactory factory, final Object formData) throws Exception;
    protected abstract String formPurpose();
}
