package org.ownprofile.boundary;

public class IntegrationTestConfig {

	public final String host;
	public final int port;

	public IntegrationTestConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("%s:%d", this.host, this.port);
	}

}
