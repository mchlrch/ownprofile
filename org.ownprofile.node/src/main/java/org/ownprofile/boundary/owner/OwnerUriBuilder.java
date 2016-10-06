package org.ownprofile.boundary.owner;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.AbstractUriBuilder;
import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.owner.resources.ContactsResource;
import org.ownprofile.boundary.owner.resources.OwnprofilesResource;

public class OwnerUriBuilder extends AbstractUriBuilder {
	
	public static OwnerUriBuilder fromUriInfo(UriInfo uriInfo) {
		final URI baseUri = uriInfo.getBaseUri();
		return new OwnerUriBuilder(baseUri);
	}
	
	public static OwnerUriBuilder fromScratch(String scheme, String host, int port) throws URISyntaxException {
		final URI baseUri = new URI(scheme, null, host, port, BoundaryConstants.API_BASE_PATH, null, null);
		return new OwnerUriBuilder(baseUri);
	}
	
	public static OwnerUriBuilder fromDummyBase() {
		try {
			return fromScratch("http", "localhost", 8080);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	private OwnerUriBuilder(URI baseUri) {
		super(baseUri);
	}

	// --------------------------------------------
	
	public URI getContactsURI() {
		final UriBuilder builder = createUriBuilder(ContactsResource.class);
		return builder.build();
	}
	
	public URI resolveContactURI(Long contactId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "getContactById");
		return builder.resolveTemplate(CONTACT_ID, contactId).build();
	}

	public URI resolveOwnerProfileURI(Long profileId) {
		final UriBuilder builder = createUriBuilder(OwnprofilesResource.class, "getOwnerProfileById"); 
		return builder.resolveTemplate(PROFILE_ID, profileId).build();
	}
	
	public URI getOwnerProfileURI() {
		final UriBuilder builder = createUriBuilder(OwnprofilesResource.class); 
		return builder.build();
	}
	
	public URI resolveContactProfileURI(Long contactId, Long profileId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "getContactProfileById");
		return builder.resolveTemplate(CONTACT_ID, contactId).resolveTemplate(PROFILE_ID, profileId).build();
	}
	
	public URI resolveAddNewContactProfileURI(Long contactId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "addNewContactProfile");
		return builder.resolveTemplate(CONTACT_ID, contactId).build();
	}
	
	public URI getAddNewContactHtmlFormURI() {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "addNewContactHtmlForm");
		return builder.build();
	}
	
}
