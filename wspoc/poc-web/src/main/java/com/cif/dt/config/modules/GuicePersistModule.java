package com.cif.dt.config.modules;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;

public class GuicePersistModule extends ServletModule {

	protected void configureServlets() {

		install(new JpaPersistModule("DesktopPU"));
		filter("/*").through(PersistFilter.class);

	}
	
}
