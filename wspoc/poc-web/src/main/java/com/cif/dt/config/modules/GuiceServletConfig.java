package com.cif.dt.config.modules;

import com.cif.dt.app.websocket.MainSocket;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

public class GuiceServletConfig extends GuiceServletContextListener {
	
	@Override
	protected Injector getInjector() {
		
		return Guice.createInjector(new GuicePersistModule(), new ServletModule() {
			
			@Override
		    protected void configureServlets() {								
				serve("/socket/main/").with(MainSocket.class);
			}
		
		});
	}
}
