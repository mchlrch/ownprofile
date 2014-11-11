package org.ownprofile.boundary.peer.client;

import java.net.URI;

import org.ownprofile.boundary.peer.PeerUriBuilder;

public class TestPeerClient extends PeerClient {
	
	public TestPeerClient(String scheme, String host, int port) {
		super(scheme, host, port);
	}

	public PeerUriBuilder getUriBuilder() {
		return this.peerUriBuilder;
	}
	
	public <T> T doGet(Class<T> responseType, URI uri) {
		return super.doGet(responseType, uri);
	}

}
