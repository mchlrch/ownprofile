package org.ownprofile.boundary;

import java.util.Optional;
import java.util.Properties;

import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class SystemTestSession extends ServiceIntegrationTestSession {

	public static final String persistenceUnit = "ownProfilePU";
	
	public static JpaPersistModule createDefaultJpaModule() {
		return createJpaModule(persistenceUnit, Optional.of(String.format("jdbc:h2:db/ownprofile_%d", DEFAULT_PORT)));
	}
	
	public static JpaPersistModule createJpaModule(String persistenceUnit, Optional<String> jdbcUrl) {
		final Properties jpaProperties = new Properties();
		
		if (jdbcUrl.isPresent()) {
			jpaProperties.setProperty("javax.persistence.jdbc.url", jdbcUrl.get());
		}
		
		final JpaPersistModule jpaModule = new JpaPersistModule(persistenceUnit);		
		jpaModule.properties(jpaProperties);
		
		return jpaModule;
	}
	
	
	
	public SystemTestSession(Module... modules) {
		super(modules);
	}
	
	public SystemTestSession(IntegrationTestConfig config, Module... modules) {
		super(config, modules);
	}

}
