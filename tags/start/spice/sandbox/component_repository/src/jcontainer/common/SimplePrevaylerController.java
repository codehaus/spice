package jcontainer.common;

import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.AbstractController;
import org.prevayler.Prevayler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class SimplePrevaylerController extends AbstractController {

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        final Prevayler p = (Prevayler) getApplicationContext().getBean("prevayler");
        return handleRequest(p,httpServletRequest);
    }

    protected abstract ModelAndView handleRequest(Prevayler p, HttpServletRequest httpServletRequest);
}
