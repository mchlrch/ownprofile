package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.tuple.Pair;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactHeaderDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;

@Path("/contacts")
public class ContactsResource {

	@Inject
	private AddressbookDomainService addressbookService;

	@Inject
	private ContactConverter contactConverter;
	
	@Inject
	private ProfileConverter profileConverter;
	
	@Inject
	private ContactsTemplate template;
	
	private final UriBuilders uriBuilders;
	
	@Inject
	public ContactsResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ContactDTO> getContacts() {
		final List<ContactEntity> contacts = this.addressbookService.getContacts();

		final List<ContactDTO> result = contacts.stream()
				.map(c -> contactConverter.convertToView(c, uriBuilders.owner()))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getContactsAsHtml() {
		final List<ContactDTO> contacts = getContacts();
		return template.addressbookOverviewPage(contacts).toString();
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContactById(@PathParam(CONTACT_ID) long id) {
		final Optional<Pair<ContactEntity, ContactAggregateDTO>> contact = contactById(id);
		return contact.map(c -> Response.ok(c.getRight()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public Response getContactByIdAsHtml(@PathParam(CONTACT_ID) long id) {
		final Optional<Pair<ContactEntity, ContactAggregateDTO>> contact = contactById(id);
		return contact.map(c -> Response.ok(template.addressbookContactPage(c.getRight()).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	private Optional<Pair<ContactEntity, ContactAggregateDTO>> contactById(long id) {
		final Optional<ContactEntity> contact = this.addressbookService.getContactById(id);
		
		if (contact.isPresent()) {
			final ContactAggregateDTO contactDto = contactConverter.convertToAggregateView(contact.get(), uriBuilders.owner()); 
			return Optional.of(Pair.of(contact.get(), contactDto));
		} else {
			return Optional.empty();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewContact(ContactNewDTO contact) {
		// TODO: input-validation
		
		final ContactEntity newContact = this.contactConverter.createEntity(contact);		
		this.addressbookService.addNewContact(newContact);
		
		final URI location = uriBuilders.owner().resolveContactURI(newContact.getId().get());		
		return Response.created(location).build();
	}
	
	@GET
	@Path("addNewContactHtmlForm")
	@Produces(MediaType.TEXT_HTML)
	public String addNewContactHtmlForm() {
		return template.addNewContactForm().toString();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addNewContactFormSubmit(MultivaluedMap<String, String> formParams) {
		ContactNewDTO in = new ContactNewDTO(formParams.getFirst(ContactHeaderDTO.P_PETNAME));
		
		Response r = addNewContact(in);
		return Response.seeOther(r.getLocation()).build();
	}
	
	@DELETE
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContact(@PathParam(CONTACT_ID) long id) {
		final Optional<Pair<ContactEntity, ContactAggregateDTO>> contact = contactById(id);
		if (contact.isPresent()) {
			addressbookService.deleteContact(contact.get().getLeft());
			return Response.ok().build();
			
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getContactProfileById(@PathParam(PROFILE_ID) long profileId) {

		// TODO: handle unknown ID gracefully
		final ProfileEntity contact = this.addressbookService.getContactProfileById(profileId).get();

		final ProfileDTO result = profileConverter.convertContactProfileToView(contact, uriBuilders.owner()); 

		// TODO: return Profile with header-link to contact -> how do we test this on client-side?
		return result;
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public String getContactProfileByIdAsHtml(@PathParam(PROFILE_ID) long profileId) {
		final ProfileDTO profile = getContactProfileById(profileId);
		return template.addressbookContactProfilePage(profile).toString();
	}
	
	@POST
	@Path("/{" + CONTACT_ID + "}/profile")
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

}
