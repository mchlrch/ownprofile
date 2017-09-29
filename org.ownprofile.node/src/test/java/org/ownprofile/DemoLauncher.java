package org.ownprofile;

import java.util.Optional;

import org.ownprofile.boundary.IntegrationTestConfig;
import org.ownprofile.boundary.SystemTestSession;
import org.ownprofile.setup.GuiceModule;

import com.google.inject.Module;

public class DemoLauncher {

	public static void main(String[] args) throws Exception {
		IntegrationTestConfig config = new IntegrationTestConfig("http", "localhost", Config.DEFAULT_PORT);

		Optional<String> jdbcUrl = Optional.of("jdbc:h2:db/ownprofile_demo");
		Module jpaModule = SystemTestSession.createJpaModule(SystemTestSession.persistenceUnit, jdbcUrl);

		final SystemTestSession session = new SystemTestSession(config, jpaModule, new GuiceModule());
		session.server.start();
	}

}
