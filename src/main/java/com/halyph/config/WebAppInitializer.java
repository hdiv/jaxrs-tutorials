package com.halyph.config;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.hdiv.filter.ValidatorFilter;
import org.hdiv.listener.InitListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		servletContext.addListener(new ContextLoaderListener(createWebAppContext()));
		addApacheCxfServlet(servletContext);
	}

	private void addApacheCxfServlet(final ServletContext servletContext) {

		// Hdiv Filter
		servletContext.addFilter("ValidatorFilter", ValidatorFilter.class).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),
				false, "/*");

		// Hdiv Listener
		servletContext.addListener(new InitListener());

		CXFServlet cxfServlet = new CXFServlet();

		ServletRegistration.Dynamic appServlet = servletContext.addServlet("CXFServlet", cxfServlet);
		appServlet.setLoadOnStartup(1);

		Set<String> mappingConflicts = appServlet.addMapping(AppConfig.API_BASE);
	}

	private WebApplicationContext createWebAppContext() {
		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(DelegateConfig.class);
		appContext.register(AppConfig.class);
		return appContext;
	}

}