package org.ownprofile.boundary.peer.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileDTO;

// TODO: http://cxf.apache.org/docs/jax-rs-client-api.html
// TODO: http://www.theotherian.com/2013/08/jersey-client-2.0-httpclient-timeouts-max-connections.html
public class PeerClient {
	
	private final Client client;
	private final String urlOfProfileResource;
	
	public PeerClient(String host, int port) {
		this.client = ClientBuilder.newClient();
		this.urlOfProfileResource = String.format("http://%s:%d%s%s", host, port, BoundaryConstants.API_BASE_PATH, BoundaryConstants.RESOURCEPATH_PEER_PROFILES); 
	}

	public List<ProfileDTO> getProfiles() {
		final WebTarget webTarget = this.client.target(urlOfProfileResource);
		
		final GenericType<List<ProfileDTO>> profileListType = new GenericType<List<ProfileDTO>>() {};
		final List<ProfileDTO> result = webTarget.request().get(profileListType);
		
		return result;		
	}
	
	/// ========================================================
	public static void main(String[] args) {
		final PeerClient client = new PeerClient("localhost", 9080);
		
		for (ProfileDTO p : client.getProfiles()) {
			System.out.println(p);
		}
	}

}
