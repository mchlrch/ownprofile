package org.ownprofile.boundary;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class SystemTestSession extends ServiceIntegrationTestSession {

	private static final String persistenceUnit = "ownProfilePU_" + DEFAULT_PORT;
	public static final Module defaultJpaModule = new JpaPersistModule(persistenceUnit);
	
	public SystemTestSession(Module... modules) {
		super(modules);
	}

}
