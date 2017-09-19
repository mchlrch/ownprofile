package org.ownprofile.boundary.owner;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.AbstractUriBuilder;
import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.owner.resources.ContactProfilesResource;
import org.ownprofile.boundary.owner.resources.ContactsResource;
import org.ownprofile.boundary.owner.resources.MyProfilesResource;

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
	
	public URI getAddContactHtmlFormURI() {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "addContactHtmlForm");
		return builder.build();
	}
	
	public URI resolveEditContactHtmlFormURI(Long contactId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "editContactHtmlForm");
		return builder.resolveTemplate(CONTACT_ID, contactId).build();
	}

	public URI resolveContactProfilesCollectionURI(Long contactId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "addContactProfile");
		return builder.resolveTemplate(CONTACT_ID, contactId).build();
	}
	
	public URI resolveAddContactProfileHtmlFormURI(Long contactId) {
		final UriBuilder builder = createUriBuilder(ContactsResource.class, "addContactProfileHtmlForm");
		return builder.resolveTemplate(CONTACT_ID, contactId).build();
	}

	// --------------------------------------------
	
	public URI resolveContactProfileURI(Long profileId) {
		final UriBuilder builder = createUriBuilder(ContactProfilesResource.class, "getContactProfileById");
		return builder.resolveTemplate(PROFILE_ID, profileId).build();
	}
	
	// --------------------------------------------
	
	public URI resolveMyProfileURI(Long profileId) {
		final UriBuilder builder = createUriBuilder(MyProfilesResource.class, "getMyProfileById"); 
		return builder.resolveTemplate(PROFILE_ID, profileId).build();
	}
	
	public URI getMyProfileURI() {
		final UriBuilder builder = createUriBuilder(MyProfilesResource.class); 
		return builder.build();
	}
	
}
