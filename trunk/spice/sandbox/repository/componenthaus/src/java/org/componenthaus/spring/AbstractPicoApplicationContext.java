package org.componenthaus.spring;

import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventMulticaster;
import org.springframework.context.ContextOptions;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.event.ApplicationEventMulticasterImpl;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.ui.context.Theme;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

public abstract class AbstractPicoApplicationContext implements WebApplicationContext {
    private ApplicationEventMulticaster eventMulticaster = new ApplicationEventMulticasterImpl();

    private ApplicationContext parent;
    private BeanFactory parentBeanFactory = null;
    private long startupTime = 0;
    protected MutablePicoContainer pico = new DefaultPicoContainer();
    private MessageSource messageSource = new StaticMessageSource();
    private ContextOptions contextOptions = new ContextOptions();
    protected ServletContext servletContext = null;
    private Map sharedObjects = new HashMap();

    public AbstractPicoApplicationContext (ApplicationContext parent, String nameSpace) {
        this.parent = parent;
    }

    public ApplicationContext getParent() {
        return parent;
    }

    public String getDisplayName() {
        return "Display name";
    }

    public long getStartupDate() {
        return startupTime;
    }

    public ContextOptions getOptions() {
        return contextOptions;
    }

    public void refresh() throws ApplicationContextException, BeansException {
        contextOptions.setReloadable(false);
        registerBeans();
        this.startupTime = System.currentTimeMillis();
        messageSource = new StaticMessageSource();
    }

    protected abstract void registerBeans() throws BeansException;

    public void close() throws ApplicationContextException {
    }

    public void publishEvent(ApplicationEvent event) {
        this.eventMulticaster.onApplicationEvent(event);
    }

    public InputStream getResourceAsStream(String location) throws IOException {
        try {
			// try URL
			URL url = new URL(location);
			return url.openStream();
		} catch (MalformedURLException ex) {
			// no URL -> try (file) path
			InputStream in = getResourceByPath(location);
			if (in == null) {
				throw new FileNotFoundException("Location '" + location + "' isn't a URL and cannot be interpreted as (file) path");
			}
			return in;
		}
    }

    protected InputStream getResourceByPath(String path) throws IOException {
		return new FileInputStream(path);
	}

    public String getResourceBasePath() {
        return (new File("")).getAbsolutePath() + File.separatorChar;
    }

    public String getMessage(String code, Object args[], String defaultMessage, Locale locale) {
		return this.messageSource.getMessage(code, args, defaultMessage, locale);
	}

	public String getMessage(String code, Object args[], Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(code, args, locale);
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		return this.messageSource.getMessage(resolvable, locale);
	}

    public int getBeanDefinitionCount() {
        return pico.getComponentKeys().size();
    }

    public String[] getBeanDefinitionNames() {
        return toStringArray(pico.getComponentKeys());
    }

    public String[] getBeanDefinitionNames(Class type) {
        System.out.println("getBeanDefinitionNames");
        final Collection names = new ArrayList();
        for(Iterator i=pico.getComponentKeys().iterator();i.hasNext();) {
            final Object key = i.next();
            if ( type.isAssignableFrom(pico.getComponentInstance(key).getClass())) {
                names.add(key);
            }
        }
        return toStringArray(names);
    }

    private String[] toStringArray(Collection strings) {
        final String []result = new String[strings.size()];
        return (String[]) strings.toArray(result);
    }

    public Object getBean(String name) throws BeansException {
        System.out.println("getBean(" + name + ")");
        final Object instance = pico.getComponentInstance(name);
        if ( instance == null ) {
            throw new NoSuchBeanDefinitionException(name,"not registered");
        }
        return instance;
    }

    public Object getBean(String name, Class requiredType) throws BeansException {
        System.out.println("getBean2");
        final Object componentInstance = pico.getComponentInstance(name);
        if ( componentInstance == null ) {
            throw new NoSuchBeanDefinitionException(name,"not available");
        }
        if ( ! requiredType.isAssignableFrom(componentInstance.getClass())) {
            throw new BeanNotOfRequiredTypeException(name, requiredType,componentInstance);
        }
        return componentInstance;
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return true;
    }

    public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return new String[0];
    }

    public BeanFactory getParentBeanFactory() {
        return parentBeanFactory;
    }

    public synchronized void shareObject(String key, Object o) {
		this.sharedObjects.put(key, o);
	}

    public Object sharedObject(String s) {
        return sharedObjects.get(s);
    }

    public Object removeSharedObject(String s) {
        return sharedObjects.remove(s);
    }

    public abstract void setServletContext(ServletContext servletContext) throws ApplicationContextException;

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Theme getTheme(String themeName) {
        return null;
    }
}