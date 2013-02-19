package com.cif.dt.config.modules;

import javax.servlet.ServletContextEvent;

import com.cif.dt.app.websocket.MainSocket;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {
	
	@Inject
	PersistService ps;
	
	@Override
	protected Injector getInjector() {
		
		Injector injector = Guice.createInjector(new ServletModule() {
			
			@Override
		    protected void configureServlets() {
				install(new JpaPersistModule("DesktopPU"));
				serve("/socket/main/").with(MainSocket.class);
			}
		
		});		
		injector.injectMembers(this);
	    return injector;
	}
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
	    super.contextInitialized(servletContextEvent);
	    ps.start();
	}
}
