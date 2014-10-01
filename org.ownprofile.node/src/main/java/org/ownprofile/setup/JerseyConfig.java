package org.ownprofile.setup;

import javax.inject.Inject;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import com.google.inject.Injector;

public class JerseyConfig extends ResourceConfig {

	@Inject
	public JerseyConfig(ServiceLocator serviceLocator) {
		this.init(serviceLocator);
	}
	
	protected void init(ServiceLocator serviceLocator) {
		register(new Binder());
		register(JacksonFeature.class);
		
		packages(true,
				org.ownprofile.boundary.owner.resources.AddressbookResource.class.getPackage().getName(),
//				org.ownprofile.boundary.owner.resources.ProfileResource.class.getPackage().getName(),
				org.ownprofile.boundary.peer.resources.ProfileResource.class.getPackage().getName());
		
		// Instantiate Guice Bridge
		GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
		
		GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
		Injector injector = GuiceSetup.getInjector();
		guiceBridge.bridgeGuiceInjector(injector);
	}

	public static class Binder extends AbstractBinder {
		protected void configure() {
//			bind(DummyProfileRepository.class).to(ProfileRepository.class).in(Singleton.class);

			// that doesn't work !
			// bindFactory(ProfileRepositoryFactory.class).to(ProfileRepository.class).in(RequestScoped.class);
		}
	}

}
