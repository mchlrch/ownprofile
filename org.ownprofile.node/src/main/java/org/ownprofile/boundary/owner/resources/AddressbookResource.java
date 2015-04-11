package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

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
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;

@Path("/owner/addressbook")
public class AddressbookResource {

	// TODO: sub resource (locator) for addressbook-bound profiles?
	
	@Inject
	private AddressbookDomainService addressbookService;

	@Inject
	private ContactConverter contactConverter;
	
	@Inject
	private ProfileConverter profileConverter;
	
	@Inject
	private AddressbookTemplate template;
	
	private final UriBuilders uriBuilders;
	
	@Inject
	public AddressbookResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}
	
	@GET
	@Path("/contact")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContactDTO> getContacts() {
		final List<ContactEntity> contacts = this.addressbookService.getContacts();

		final List<ContactDTO> result = contacts.stream()
				.map(c -> contactConverter.convertToView(c, uriBuilders.owner()))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/contact")
	@Produces(MediaType.TEXT_HTML)
	public String getContactsAsHtml() {
		final List<ContactDTO> contacts = getContacts();
		return template.addressbookOverviewPage(contacts).toString();
	}
	
	@GET
	@Path("/contact/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ContactAggregateDTO getContactById(@PathParam(CONTACT_ID) long id) {
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(id).get();

		final ContactAggregateDTO result = contactConverter.convertToAggregateView(contact, uriBuilders.owner()); 

		return result;
	}
	
	@GET
	@Path("/contact/{" + CONTACT_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public String getContactByIdAsHtml(@PathParam(CONTACT_ID) long id) {
		final ContactAggregateDTO contact = getContactById(id);
		return template.addressbookContactPage(contact).toString();
	}
	
	@POST
	@Path("/contact")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewContact(ContactNewDTO contact) {
		// TODO: input-validation
		
		final ContactEntity newContact = this.contactConverter.createEntity(contact);		
		this.addressbookService.addNewContact(newContact);
		
		final URI location = uriBuilders.owner().resolveContactURI(newContact.getId().get());		
		return Response.created(location).build();
	}

	@GET
	@Path("/contact/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getContactProfileById(@PathParam(PROFILE_ID) long profileId) {

		// TODO: handle unknown ID gracefully
		final ProfileEntity contact = this.addressbookService.getContactProfileById(profileId).get();

		final ProfileDTO result = profileConverter.convertContactProfileToView(contact, uriBuilders.owner()); 

		// TODO: return Profile with header-link to contact -> how do we test this on client-side?
		return result;
	}
	
	@GET
	@Path("/contact/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public String getContactProfileByIdAsHtml(@PathParam(PROFILE_ID) long profileId) {
		final ProfileDTO profile = getContactProfileById(profileId);
		return template.addressbookContactProfilePage(profile).toString();
	}
	
	@POST
	@Path("/contact/{" + CONTACT_ID + "}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewContactProfile(@PathParam(CONTACT_ID) long contactId, ProfileNewDTO profile) {
		// TODO: input-validation
		
		// TODO: handle unknown ID gracefully
		final ContactEntity contact = this.addressbookService.getContactById(contactId).get();
		
		// TODO: handle remote source
		final ProfileSource profileSource = ProfileSource.createLocalSource();
		final ProfileHandle handle = ProfileHandle.createRandomHandle();
		
		final ProfileEntity newContactProfile = this.profileConverter.createEntityForContactProfile(contact, profile, handle, profileSource);
		this.addressbookService.addNewContactProfile(newContactProfile);
		
		final URI location = uriBuilders.owner().resolveContactProfileURI(contact.getId().get(), newContactProfile.getId().get());		
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
