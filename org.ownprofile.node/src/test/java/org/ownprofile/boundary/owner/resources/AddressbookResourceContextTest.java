package org.ownprofile.boundary.owner.resources;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.SystemTestSession;
import org.ownprofile.setup.GuiceModule;

public class AddressbookResourceContextTest {

	private static SystemTestSession session;

	private final TestClient client;

	@BeforeClass
	public static void startJetty() throws Exception {
		session = new SystemTestSession(SystemTestSession.defaultJpaModule, new GuiceModule());
		session.server.start();
	}

	@AfterClass
	public static void stopJetty() throws Exception {
		session.server.stop();
	}

	// =============================================================

	public AddressbookResourceContextTest() {
		this.client = new TestClient(session.testConfig.host, session.testConfig.port);
	}

	@Ignore @Test
	public void shouldCallTestOperation() {
		this.client.doTest();
		
		// TODO: refactor to use org.ownprofile.boundary.owner.resources.DemoResource.test()
	}	
	
	// =============================================================

	private class TestClient {
		private final Client client;
		private final String urlOfAddressbookTestResource;

		public TestClient(String host, int port) {
			this.client = ClientBuilder.newClient();
			this.urlOfAddressbookTestResource = String.format("http://%s:%d%s%s/%s", host, port, BoundaryConstants.API_BASE_PATH, BoundaryConstants.RESOURCEPATH_OWNER_ADDRESSBOOK, "test");
		}

		public String doTest() {
			final WebTarget webTarget = this.client.target(urlOfAddressbookTestResource);
			final String result = webTarget.request().get(String.class);
			return result;
		}
	}

}
