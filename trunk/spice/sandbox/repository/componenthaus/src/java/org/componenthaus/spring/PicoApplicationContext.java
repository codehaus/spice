package org.componenthaus.spring;

import org.componenthaus.prevayler.PrevaylerBean;
import org.componenthaus.repository.api.ComponentRepository;
import org.componenthaus.repository.api.RepositoryImpl;
import org.componenthaus.repository.impl.ComponentFactory;
import org.componenthaus.repository.impl.ServiceImplementationFactory;
import org.componenthaus.repository.services.CommandRegistryImpl;
import org.componenthaus.search.AddComponentMonitor;
import org.componenthaus.search.LuceneSearchService;
import org.componenthaus.search.SearchService;
import org.componenthaus.usecases.downloadcomponent.DownloadComponentController;
import org.componenthaus.usecases.listcomponents.ListComponentsController;
import org.componenthaus.usecases.searchcomponents.SearchComponentsController;
import org.componenthaus.usecases.showcomponent.ShowComponentController;
import org.componenthaus.usecases.showimplementation.ShowImplementationController;
import org.componenthaus.usecases.submitcomponent.SubmitComponentController;
import org.componenthaus.usecases.submitcomponent.DefaultSubmissionManager;
import org.componenthaus.usecases.welcomeuser.WelcomeUserController;
import org.componenthaus.util.file.FileManager;
import org.componenthaus.util.file.FileManagerImpl;
import org.componenthaus.util.source.CodeFormatter;
import org.componenthaus.util.source.JalopyCodeFormatter;
import org.prevayler.Prevayler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;
import org.springframework.web.servlet.view.tiles.TilesConfigurer;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

//TODO Return real views instead of view names, coming from a properties file.

public class PicoApplicationContext extends AbstractPicoApplicationContext {
    private static final String VIEW_FILE = "views";

    private final Map urlMap;

    public PicoApplicationContext(ApplicationContext parent, String nameSpace) {
        super(parent, nameSpace);
        urlMap = new HashMap();
    }

    public void setServletContext(ServletContext servletContext) throws ApplicationContextException {
        this.servletContext = servletContext;
        registerBeans();
    }

    protected void registerBeans() throws BeansException {
        assert servletContext != null : servletContext; //Must be called after setServletContext()

        addController(WelcomeUserController.class, "/welcome.htm");
        addController(ListComponentsController.class, "/listComponents.action");
        addController(ShowImplementationController.class, "/showImplementation.action");
        addController(ShowComponentController.class, "/componentDetails.action");
        addController(DownloadComponentController.class, "/downloadComponent.action");

        addController(SubmitComponentController.class, "/submitComponent.action");
        pico.registerComponentInstance(SubmitComponentController.ViewConfiguration.class, new SubmitComponentController.ViewConfiguration(){
            public String getFormView() {
                return "submitComponentView";
            }

            public String getSuccessView() {
                return "welcomeView";
            }
        });

        addController(SearchComponentsController.class, "/searchComponents.action");
        pico.registerComponentInstance(SearchComponentsController.ViewConfiguration.class, new SearchComponentsController.ViewConfiguration() {
            //Return the name of the form to show when the controller is first invoked.
            public String getFormView() {
                return "searchCriteriaView";
            }
        });

        pico.registerComponentImplementation(SearchService.class, LuceneSearchService.class);
        pico.registerComponentImplementation(CodeFormatter.class, JalopyCodeFormatter.class);
        pico.registerComponentImplementation(ComponentRepository.Monitor.class, AddComponentMonitor.class); //Will need multiples here soon
        pico.registerComponentImplementation(ComponentRepository.class, RepositoryImpl.class);
        //pico.registerComponentImplementation(PrevalentSystem.class, RepositoryImpl.class); //This causes a second instance of repository to exist.  Yuck.
        pico.registerComponentImplementation(FileManager.class, FileManagerImpl.class);
        pico.registerComponentImplementation(ComponentFactory.class);
        pico.registerComponentImplementation(ServiceImplementationFactory.class);
        pico.registerComponentImplementation(Prevayler.class, PrevaylerBean.class);
        pico.registerComponentImplementation(DefaultSubmissionManager.class);
        pico.registerComponentImplementation(DefaultSubmissionManager.NullSubmissionMonitor.class);
        pico.registerComponentImplementation(CommandRegistryImpl.class);
        pico.registerComponentImplementation(LuceneSearchService.DefaultLuceneObjectFactory.class);

        addSpringStuff();
        //This must be done last, because it reads things from Pico
        final SimpleUrlHandlerMapping urlMapping = createUrlMapping();
        pico.registerComponentInstance("urlMapping",urlMapping);
        urlMapping.setApplicationContext(this);
    }

    private void addController(Class controllerClass, String urlMapping) {
        addController(controllerClass, new String[]{urlMapping});
    }

    private void addSpringStuff() {
        ResourceBundleViewResolver viewResolver = new ResourceBundleViewResolver();
        viewResolver.setBasename(VIEW_FILE);
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[]{"/WEB-INF/defs/definitions.xml"});
        pico.registerComponentInstance("tilesConfigurer",tilesConfigurer);
        pico.registerComponentInstance(DispatcherServlet.VIEW_RESOLVER_BEAN_NAME,viewResolver);
        pico.registerComponentImplementation(DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME,CommonsMultipartResolver.class);
        viewResolver.setApplicationContext(this);
        tilesConfigurer.setApplicationContext(this);
    }

    private SimpleUrlHandlerMapping createUrlMapping() {
        assert urlMap != null && urlMap.size() > 0;
        final SimpleUrlHandlerMapping result = new SimpleUrlHandlerMapping();
        result.setUrlMap(urlMap);
        return result;
    }

    private void addController(Class controllerClass, String[] urlMappings) {
        final String controllerName = controllerClass.getName();
        assert pico.hasComponent(controllerName) == false : controllerName;
        assert urlMappings.length > 0;

        pico.registerComponentImplementation(controllerName,controllerClass);
        for(int i=0;i<urlMappings.length;i++) {
            assert !urlMap.containsKey(urlMappings[i]) : urlMappings[i];
            urlMap.put(urlMappings[i],controllerName);
        }
    }
}
