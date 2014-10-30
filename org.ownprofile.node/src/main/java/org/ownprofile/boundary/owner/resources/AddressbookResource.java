package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;
import static org.ownprofile.boundary.BoundaryConstants.RESOURCEPATH_OWNER_ADDRESSBOOK;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

@Path(RESOURCEPATH_OWNER_ADDRESSBOOK)
public class AddressbookResource {

	// TODO: sub resource (locator) for addressbook-bound profiles?
	
	@Inject
	private AddressbookDomainService addressbookService;

	@Inject
	private ContactConverter contactConverter;
	
	@Inject
	private ProfileConverter profileConverter;
	
	@GET
	@Path("/contact")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContactDTO> getContacts(@Context final UriInfo uriInfo) {
		final List<ContactEntity> contacts = this.addressbookService.getContacts();

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final List<ContactDTO> result = contacts.stream()
				.map(c -> contactConverter.convertToView(c, uriBuilder))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/contact/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactAggregateDTO getContactById(@PathParam(CONTACT_ID) long id, @Context final UriInfo uriInfo) {
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(id).get();

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final ContactAggregateDTO result = contactConverter.convertToAggregateView(contact, uriBuilder); 

		return result;
	}
	
	@POST
	@Path("/contact")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewContact(ContactNewDTO contact, @Context final UriInfo uriInfo) {
		// TODO: input-validation
		
		final ContactEntity newContact = this.contactConverter.createEntity(contact);		
		this.addressbookService.addNewContact(newContact);
		
		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final URI location = uriBuilder.resolveContactURI(newContact.getId().get());		
		return Response.created(location).build();
	}

	@GET
	@Path("/contact/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getContactProfileById(@PathParam(PROFILE_ID) long profileId, @Context final UriInfo uriInfo) {

		// TODO: handle unknown ID gracefully
		final ProfileEntity contact = this.addressbookService.getContactProfileById(profileId).get();

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final ProfileDTO result = profileConverter.convertToView(contact, uriBuilder); 

		// TODO: return Profile with header-link to contact -> how do we test this on client-side?
		return result;
	}
	
	@POST
	@Path("/contact/{" + CONTACT_ID + "}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewContactProfile(@PathParam(CONTACT_ID) long contactId, ProfileNewDTO profile, @Context final UriInfo uriInfo) {
		// TODO: input-validation
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(contactId).get();
		
		// TODO: handle remote source
		final ProfileSource profileSource = ProfileSource.createLocalSource();
		
		final ProfileEntity newContactProfile = this.profileConverter.createEntityForContactProfile(contact, profile, profileSource);
		this.addressbookService.addNewContactProfile(newContactProfile);
		
		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final URI location = uriBuilder.resolveContactProfileURI(newContactProfile.getId().get());		
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

}
