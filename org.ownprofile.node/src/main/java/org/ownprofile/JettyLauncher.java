package org.ownprofile;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.setup.GuiceModule;
import org.ownprofile.setup.GuiceSetup;
import org.ownprofile.setup.JerseyConfig;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceFilter;

public class JettyLauncher {
	
	public static void main(String[] args) throws Exception {
		final Config config = args.length > 0 ? Config.parseConfigFile(args[0]) : Config.parseDefaultConfigFile();
		config.log();
		
		final Module[] modules = new Module[] {
				new JpaPersistModule(config.persistenceUnitName),
				new GuiceModule()
		};
		final Injector guiceInjector = GuiceSetup.createInjectorAndSetAsSingleton(modules);		
		
		final Server server = createAndSetupServer(config.port, guiceInjector);
		
		server.start();
		System.out.println(String.format("### http://localhost:%d%s/contacts",
				config.port, BoundaryConstants.API_BASE_PATH));

		server.join();
	}
	
	public static Server createAndSetupServer(final int port, final Injector guiceInjector) {
		final Server server = new Server(port);
		
		final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		final FilterHolder guiceFilter = new FilterHolder(guiceInjector.getInstance(GuiceFilter.class));
		
        context.setContextPath("/");
        context.addFilter(guiceFilter, "/*", EnumSet.allOf(DispatcherType.class));

        // TODO: detect environment - PersistFilter is unbound only in IntegrationTests 
        final Key<PersistFilter> persistFilterBindingKey = Key.get(PersistFilter.class);        
        if ( /* env == Test && */ guiceInjector.getExistingBinding(persistFilterBindingKey) != null) {
        	final FilterHolder persistFilter = new FilterHolder(guiceInjector.getInstance(persistFilterBindingKey));        	
        	context.addFilter(persistFilter, "/*", EnumSet.allOf(DispatcherType.class));
        }
        
        server.setHandler(context);

        final ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, String.format("%s/*", BoundaryConstants.API_BASE_PATH));
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("javax.ws.rs.Application", JerseyConfig.class.getName());

        final ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");
        staticServlet.setInitParameter("resourceBase","src/main/webapp");
        staticServlet.setInitParameter("pathInfoOnly","true");
		
		return server;
	}

}
