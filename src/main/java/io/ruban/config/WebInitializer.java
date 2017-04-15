package io.ruban.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class provides setting up Spring Dispatcher Servlet
 * <p/>
 * This class allows to set up servlet config, servlet mappings
 * and other servlet configurations
 *
 * @author Andrei Ruban - software engineer.
 * @version 09.07.2015
 */
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringRootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringWebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}