package jcontainer.web;

import com.atlassian.components.ComponentProviderSingleton;
import com.atlassian.components.ComponentProvider;
import com.atlassian.components.DependencyStrategy;
import com.atlassian.seraph.filter.ComponentProvidingFilter;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;

public class BootstrapServlet extends HttpServlet {
    private ServletConfig config = null;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        this.config = servletConfig;
        configureComponentProvider();
        configureDependencyStrategy();
    }

    private void configureComponentProvider() throws ServletException {
        Object impl = getImpl("componentProviderImpl");
        ComponentProviderSingleton.setProvider((ComponentProvider) impl);
    }

    private void configureDependencyStrategy() throws ServletException {
        Object impl = getImpl("dependencyStrategyImpl");
        ComponentProviderSingleton.setDependencyStrategy((DependencyStrategy) impl);
    }

    private Object getImpl(final String s) throws ServletException {
        final String providerImplName = config.getInitParameter(s);
        if (providerImplName == null || "".equals(providerImplName)) {
            throw new ServletException("Init-param \"" + s + "\" not provided for filter ComponentProvidingFilter");
        }
        Object impl = null;
        try {
            final Class c = loadClass(providerImplName, this.getClass());
            impl = c.newInstance();
        } catch (Exception e) {
            throw new ServletException("Exception trying to load or instantiateclass " + providerImplName, e);
        }
        return impl;
    }

    private Class loadClass(String className, Class callingClass) throws ClassNotFoundException {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ComponentProvidingFilter.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return callingClass.getClassLoader().loadClass(className);
                }
            }
        }
    }
}
