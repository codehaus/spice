package jcontainer.web;

import com.interface21.beans.factory.InitializingBean;
import com.interface21.web.servlet.ModelAndView;
import com.interface21.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JContainerController extends MultiActionController implements InitializingBean {
    
    public void afterPropertiesSet() throws Exception {
    }
    
    public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        return new ModelAndView("welcomeView");
    }
}