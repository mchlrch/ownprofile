package org.ownprofile.boundary;

public class IntegrationTestConfig {

	public final String scheme;
	public final String host;
	public final int port;

	public IntegrationTestConfig(String scheme, String host, int port) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("%s://%s:%d", this.scheme, this.host, this.port);
	}

}
