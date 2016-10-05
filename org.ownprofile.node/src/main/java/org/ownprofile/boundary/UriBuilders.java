package org.ownprofile.boundary;

import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.owner.OwnerUriBuilder;

public class UriBuilders {

	private boolean initialized;

	private OwnerUriBuilder ownerUriBuilder;

	public UriBuilders init(UriInfo uriInfo) {
		if (initialized) {
			throw new IllegalStateException("already initialized");
		}
		
		this.ownerUriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);

		this.initialized = true;
		
		return this;
	}

	public OwnerUriBuilder owner() {
		assertInitialized();
		return ownerUriBuilder;
	}

	protected void assertInitialized() {
		if (!initialized) {
			throw new IllegalStateException("builders not initialized");
		}		
	}

}
