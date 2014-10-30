package org.ownprofile.boundary;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

//TODO: http://cxf.apache.org/docs/jax-rs-client-api.html
//TODO: http://www.theotherian.com/2013/08/jersey-client-2.0-httpclient-timeouts-max-connections.html
public abstract class AbstractClient {
	
	// TODO: dispose client properly
	protected final Client client;
	
	protected AbstractClient() {
		this.client = ClientBuilder.newClient();
	}
	
	protected <T> T doGet(Class<T> responseType, URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final T result = webTarget.request().get(responseType);
		return result;
	}
	
	protected <T> T doGet(GenericType<T> responseType, URI uri) {
		final WebTarget webTarget = this.client.target(uri);		
		final T result = webTarget.request().get(responseType);		
		return result;		
	}

	protected URI doPost(Object obj, URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final Response response = webTarget.request().post(Entity.json(obj));
		return response.getLocation();
	}

}