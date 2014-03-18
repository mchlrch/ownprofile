package org.ownprofile.boundary.owner.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Path(BoundaryConstants.RESOURCEPATH_OWNER_ADDRESSBOOK)
public class AddressbookResource {

	// TODO: sub resource (locator) for addressbook-bound profiles?
	
	@Inject
	private AddressbookDomainService addressbookService;

	@Inject
	private ContactConverter contactConverter;
	
	@Inject
	private ProfileConverter profileConverter;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContactDTO> getContacts(@Context final UriInfo uriInfo) {
		final List<ContactEntity> contacts = this.addressbookService.getContacts();

		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		
		// TODO: use lambdas instead, once eclipse supports Java8 properly: http://wiki.eclipse.org/JDT_Core/Java8
//		final List<Contact> result = contacts.stream().map(c -> converter.convertToView(c));
		final List<ContactDTO> result = Lists.transform(contacts, new Function<ContactEntity, ContactDTO>() {
			@Override
			public ContactDTO apply(ContactEntity in) {
				return contactConverter.convertToView(in, uriBuilderCallback);
			}
		});
		
		return result;
	}
	
	@GET
	@Path("/contact/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactAggregateDTO getContactById(@PathParam("id") long id, @Context final UriInfo uriInfo) {
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(id).get();

		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final ContactAggregateDTO result = contactConverter.convertToAggregateView(contact, uriBuilderCallback); 

		return result;
	}
	
	@POST
	@Path("/contact")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewContact(ContactNewDTO contact, @Context final UriInfo uriInfo) {
		// TODO: input-validation
		
		final ContactEntity newContact = this.contactConverter.createEntity(contact);		
		this.addressbookService.addNewContact(newContact);
		
		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final URI location = uriBuilderCallback.buildContactUri(newContact);		
		return Response.created(location).build();
	}

	@GET
	@Path("/contact/{contactId}/profile/{profileId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getContactProfileById(@PathParam("profileId") long profileId, @Context final UriInfo uriInfo) {

		// TODO: handle unknown ID gracefully
		final ProfileEntity contact = this.addressbookService.getContactProfileById(profileId).get();

		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final ProfileDTO result = profileConverter.convertToView(contact, uriBuilderCallback); 

		// TODO: return Profile with header-link to contact -> how do we test this on client-side?
		return result;
	}
	
	@POST
	@Path("/contact/{id}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewContactProfile(@PathParam("id") long contactId, ProfileNewDTO profile, @Context final UriInfo uriInfo) {
		// TODO: input-validation
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(contactId).get();
		
		// TODO: handle remote source
		final ProfileSource profileSource = ProfileSource.createLocalSource();
		
		final ProfileEntity newContactProfile = this.profileConverter.createEntityForContactProfile(contact, profile, profileSource);
		this.addressbookService.addNewContactProfile(newContactProfile);
		
		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final URI location = uriBuilderCallback.buildProfileUri(newContactProfile);		
		return Response.created(location).build();
	}
	
	
	// TODO: getRemoteProfile(URI) into client, then normal procedure to addNewContactProfile(profile)
	// TODO: node acts as a proxy for the client, who first retrieves and looks at the profile before adding it to the addressbook
	// TODO: in case remote-node is not up at the time client requests the profile, profile could be polled, fetched and stored in temporary stash on node ... 

	//	@POST
//	@Path("/contact/{id}/remote-profile")
//	@Produces(MediaType.APPLICATION_JSON)
//	public void addNewRemoteProfile(@PathParam("id") long contactId, URI profileURI) {
//		// TODO: input-validation
//		// TODO: implement this
//		// TODO: return created Profile URI
//		// return Response.created(uri)
//	}


	// --------------------------------------------------
	private static class MyUriBuilderCallback implements ContactConverter.UriBuilderCallback {
		private final UriInfo uriInfo;
		private MyUriBuilderCallback(UriInfo uriInfo) {
			this.uriInfo = checkNotNull(uriInfo);
		}
		
		@Override
		public URI buildContactUri(ContactEntity contact) {
			final UriBuilder contactUriTemplate = this.uriInfo.getAbsolutePathBuilder().path("/contact/{id}");
			contactUriTemplate.resolveTemplate("id", contact.getId().get());
			return contactUriTemplate.build();
		}
		
		@Override
		public URI buildProfileUri(ProfileEntity profile) {
			final ContactEntity contact = profile.getContact().get();
			final UriBuilder profileUriTemplate = this.uriInfo.getAbsolutePathBuilder().path("/contact/{contactId}/profile/{profileId}");
			profileUriTemplate.resolveTemplate("contactId", contact.getId().get());
			profileUriTemplate.resolveTemplate("profileId", profile.getId().get());
			return profileUriTemplate.build();
		}
	};
}
