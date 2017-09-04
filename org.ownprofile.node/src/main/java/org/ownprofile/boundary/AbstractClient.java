package org.ownprofile.boundary;

import java.net.URI;
import javax.ws.rs.core.Response.Status;

import org.ownprofile.boundary.owner.client.Result;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
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
		final T result = webTarget.request(MediaType.APPLICATION_JSON).get(responseType);
		return result;
	}

	protected <T> T doGet(GenericType<T> responseType, URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final T result = webTarget.request(MediaType.APPLICATION_JSON).get(responseType);
		return result;
	}

	protected URI doPost(Object obj, URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(obj));
		return response.getLocation();
	}

	protected Result<Void> doPut(Object obj, URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final Response response = webTarget.request(MediaType.APPLICATION_JSON).put(Entity.json(obj));
		
		switch (Status.fromStatusCode(response.getStatus())) {
		case OK:
			return Result.success();

		default:
			return Result.fail("PUT failed. Server responded with HTTP %d on %s",
							response.getStatus(), uri);
		}
	}
	
	
	protected Result<Void> doDelete(URI uri) {
		final WebTarget webTarget = this.client.target(uri);
		final Response response = webTarget.request(MediaType.APPLICATION_JSON).delete();
		
		switch (Status.fromStatusCode(response.getStatus())) {
		case OK:
			return Result.success();

		default:
			return Result.fail("DELETE failed. Server responded with HTTP %d on %s",
							response.getStatus(), uri);
		}
	}

}
