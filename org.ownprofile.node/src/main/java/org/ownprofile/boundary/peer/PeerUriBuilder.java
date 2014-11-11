package org.ownprofile.boundary.peer;

import static org.ownprofile.boundary.BoundaryConstants.PROFILE_HANDLE;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.AbstractUriBuilder;
import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.peer.resources.PeerApiProfileResource;
import org.ownprofile.profile.entity.ProfileHandle;

// TODO: PeerUriBuilderTest .. @see OwnerUriBuilderTest
public class PeerUriBuilder extends AbstractUriBuilder {
	
	public static PeerUriBuilder fromUriInfo(UriInfo uriInfo) {
		final URI baseUri = uriInfo.getBaseUri();
		return new PeerUriBuilder(baseUri);
	}
	
	public static PeerUriBuilder fromScratch(String scheme, String host, int port) throws URISyntaxException {
		final URI baseUri = new URI(scheme, null, host, port, BoundaryConstants.API_BASE_PATH, null, null);
		return new PeerUriBuilder(baseUri);
	}
	
	public static PeerUriBuilder fromDummyBase() {
		try {
			return fromScratch("http", "localhost", 8080);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private PeerUriBuilder(URI baseUri) {
		super(baseUri);
	}
	
	// --------------------------------------
	
	public URI getProfileURI() {
		final UriBuilder builder = createUriBuilder(PeerApiProfileResource.class); 
		return builder.build();
	}
	
	public URI resolveProfileURI(ProfileHandle handle) {
		final UriBuilder builder = createUriBuilder(PeerApiProfileResource.class, "getProfileById"); 
		return builder.resolveTemplate(PROFILE_HANDLE, handle.asString()).build();
	}

	
}
