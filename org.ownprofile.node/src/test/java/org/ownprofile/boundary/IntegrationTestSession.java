package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;

import org.eclipse.jetty.server.Server;
import org.ownprofile.JettyLauncher;
import org.ownprofile.boundary.owner.client.OwnerClient;
import org.ownprofile.boundary.peer.client.PeerClient;
import org.ownprofile.setup.GuiceSetup;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.persist.jpa.JpaPersistModule;

public class IntegrationTestSession {

	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = JettyLauncher.DEFAULT_PORT;
	
	private static final String persistenceUnit = "ownProfilePU_" + DEFAULT_PORT;
	public static final Module defaultJpaModule = new JpaPersistModule(persistenceUnit);

	public final Server server;
	public final IntegrationTestConfig testConfig;

	private OwnerClient ownerClient;
	private PeerClient peerClient;

	public IntegrationTestSession(Module... modules) {
		this(new IntegrationTestConfig(DEFAULT_HOST, DEFAULT_PORT), modules);
	}

	public IntegrationTestSession(IntegrationTestConfig testConfig, Module... modules) {
		this.testConfig = checkNotNull(testConfig, "testConfig is null");

		final Injector guiceInjector = GuiceSetup.createInjectorAndSetAsSingleton(true, modules);
		this.server = JettyLauncher.createAndSetupServer(testConfig.port, guiceInjector);
	}

	public OwnerClient getOrCreateOwnerClient() {
		if (this.ownerClient == null) {
			this.ownerClient = new OwnerClient(this.testConfig.host, this.testConfig.port);
		}
		return this.ownerClient;
	}

	public PeerClient getOrCreatePeerClient() {
		if (this.peerClient == null) {
			this.peerClient = new PeerClient(this.testConfig.host, this.testConfig.port);
		}
		return this.peerClient;
	}

}