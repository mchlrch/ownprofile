package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

public class AbstractUriBuilder {

	protected final UriBuilder baseUriBuilder;
	
	protected AbstractUriBuilder(URI baseUri) {
		checkNotNull(baseUri);
		this.baseUriBuilder = UriBuilder.fromUri(baseUri);
	}
	
	protected UriBuilder createUriBuilder(Class<?> resource) {
		final UriBuilder builder = this.baseUriBuilder.clone();
		builder.path(resource);
		return builder;
	}

	protected UriBuilder createUriBuilder(Class<?> resource, String method) {
		final UriBuilder builder = createUriBuilder(resource);
		builder.path(resource, method);
		return builder;
	}
}
