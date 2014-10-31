package org.ownprofile.boundary.owner.client;

import java.net.URI;

import org.ownprofile.boundary.owner.OwnerUriBuilder;

public class TestOwnerClient extends OwnerClient {
	
	public TestOwnerClient(String scheme, String host, int port) {
		super(scheme, host, port);
	}

	public OwnerUriBuilder getUriBuilder() {
		return this.ownerUriBuilder;
	}
	
	public <T> T doGet(Class<T> responseType, URI uri) {
		return super.doGet(responseType, uri);
	}

}
