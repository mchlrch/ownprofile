package org.ownprofile.boundary.peer.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.ownprofile.boundary.AbstractClient;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.peer.PeerUriBuilder;
import org.ownprofile.profile.entity.ProfileHandle;

public class PeerClient extends AbstractClient {
	
	protected final PeerUriBuilder peerUriBuilder;
	
	public PeerClient(String scheme, String host, int port) {
		try {
			this.peerUriBuilder = PeerUriBuilder.fromScratch(scheme, host, port);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public List<ProfileDTO> getProfiles() {
		final URI uri = this.peerUriBuilder.getProfileURI();
		return doGet(new GenericType<List<ProfileDTO>>() {}, uri);
	}
	
	public ProfileDTO getProfileByHandle(ProfileHandle handle) {
		final URI uri = this.peerUriBuilder.resolveProfileURI(handle);
		return doGet(ProfileDTO.class, uri);
	}
	
	/// ========================================================
	public static void main(String[] args) {
		final PeerClient client = new PeerClient("http", "localhost", 9080);
		
		for (ProfileDTO p : client.getProfiles()) {
			System.out.println(p);
		}
	}

}
