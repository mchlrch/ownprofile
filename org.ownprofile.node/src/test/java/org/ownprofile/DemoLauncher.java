package org.ownprofile;

import org.ownprofile.boundary.SystemTestSession;
import org.ownprofile.setup.GuiceModule;

public class DemoLauncher {

	public static void main(String[] args) throws Exception {
		final SystemTestSession session = new SystemTestSession(SystemTestSession.defaultJpaModule, new GuiceModule());
		session.server.start();
	}

}
