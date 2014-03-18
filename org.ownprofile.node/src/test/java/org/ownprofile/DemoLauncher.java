package org.ownprofile;

import org.ownprofile.boundary.IntegrationTestSession;
import org.ownprofile.setup.GuiceModule;

public class DemoLauncher {

	public static void main(String[] args) throws Exception {
		final IntegrationTestSession session = new IntegrationTestSession(IntegrationTestSession.defaultJpaModule, new GuiceModule());
		session.server.start();
	}

}
