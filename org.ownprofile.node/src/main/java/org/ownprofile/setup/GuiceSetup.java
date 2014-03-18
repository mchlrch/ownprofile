package org.ownprofile.setup;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class GuiceSetup {

	private static Injector injector;

	public static Injector createInjectorAndSetAsSingleton(Module... modules) {
		return createInjectorAndSetAsSingleton(false, modules);
	}

	public static Injector createInjectorAndSetAsSingleton(boolean overwriteExistingInjector, Module... modules) {
		if (injector == null || overwriteExistingInjector) {
			synchronized (GuiceSetup.class) {
				if (injector == null || overwriteExistingInjector) {
					injector = Guice.createInjector(modules);
					return injector;
				}
			}
		}

		throw new IllegalStateException("injector singleton already set");
	}

	public static Injector getInjector() {
		if (injector == null) {
			throw new IllegalStateException("injector singleton not set");
		}

		return injector;
	}

}
