package jcontainer.web;

import com.interface21.beans.factory.InitializingBean;
import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.prevayler.Prevayler;
import jcontainer.Repository;

public class RepositoryController extends MultiActionController implements InitializingBean {
    public void afterPropertiesSet() throws Exception {
    }
    
    public ModelAndView listComponentsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        final Prevayler prevayler = (Prevayler) getApplicationContext().getBean("prevayler");
        return new ModelAndView("listComponentsView","components",((Repository)prevayler.system()).getComponents());
    }
}