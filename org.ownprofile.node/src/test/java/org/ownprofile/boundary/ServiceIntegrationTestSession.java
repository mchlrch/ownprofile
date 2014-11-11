package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jetty.server.Server;
import org.ownprofile.JettyLauncher;
import org.ownprofile.boundary.owner.client.TestOwnerClient;
import org.ownprofile.boundary.peer.client.TestPeerClient;
import org.ownprofile.setup.GuiceSetup;

import com.google.inject.Injector;
import com.google.inject.Module;

public class ServiceIntegrationTestSession {

	public static final String DEFAULT_SCHEME = "http";
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = JettyLauncher.DEFAULT_PORT;

	public final Server server;
	public final IntegrationTestConfig testConfig;

	private TestOwnerClient ownerClient;
	private TestPeerClient peerClient;

	public ServiceIntegrationTestSession(Module... modules) {
		this(new IntegrationTestConfig(DEFAULT_SCHEME, DEFAULT_HOST, DEFAULT_PORT), modules);
	}

	public ServiceIntegrationTestSession(IntegrationTestConfig testConfig,
			Module... modules) {
		this.testConfig = checkNotNull(testConfig, "testConfig is null");

		final Injector guiceInjector = GuiceSetup
				.createInjectorAndSetAsSingleton(true, modules);
		this.server = JettyLauncher.createAndSetupServer(testConfig.port,
				guiceInjector);
	}

	public TestOwnerClient getOrCreateOwnerClient() {
		if (this.ownerClient == null) {
			this.ownerClient = new TestOwnerClient(
					this.testConfig.scheme,
					this.testConfig.host,
					this.testConfig.port);
		}
		return this.ownerClient;
	}

	public TestPeerClient getOrCreatePeerClient() {
		if (this.peerClient == null) {
			this.peerClient = new TestPeerClient(
					this.testConfig.scheme,
					this.testConfig.host,
					this.testConfig.port);
		}
		return this.peerClient;
	}

}
