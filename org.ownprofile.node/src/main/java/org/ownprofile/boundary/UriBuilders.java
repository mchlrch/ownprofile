package org.ownprofile.boundary;

import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.boundary.peer.PeerUriBuilder;

public class UriBuilders {

	private boolean initialized;

	private OwnerUriBuilder ownerUriBuilder;
	private PeerUriBuilder peerUriBuilder;

	public UriBuilders init(UriInfo uriInfo) {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		}
		
		this.ownerUriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		this.peerUriBuilder = PeerUriBuilder.fromUriInfo(uriInfo);

		this.initialized = true;
		
		return this;
	}

	public OwnerUriBuilder owner() {
		assertInitialized();
		return ownerUriBuilder;
	}

	public PeerUriBuilder peer() {
		assertInitialized();
		return peerUriBuilder;
	}
	
	protected void assertInitialized() {
		if (!initialized) {
			throw new IllegalStateException("builders not initialized");
		}		
	}

}
