package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;
import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactHeaderDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.ContactService;

@Path("/contacts")
public class ContactsResource {

	@Inject
	private ContactService contactService;

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
		final List<ContactDTO> result = contactService.getContacts();
		return result;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getContactsAsHtml() {
		final List<ContactDTO> contacts = contactService.getContacts();
		return template.contactsOverviewPage(contacts).toString();
	}

	@GET
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContactById(@PathParam(CONTACT_ID) long id) {
		final Optional<ContactAggregateDTO> contact = contactService.getContactById(id);
		return contact
				.map(c -> Response.ok(c).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@GET
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public Response getContactByIdAsHtml(@PathParam(CONTACT_ID) long id) {
		final Optional<ContactAggregateDTO> contact = contactService.getContactById(id);
		return contact
				.map(c -> Response.ok(template.contactPage(c).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewContact(ContactNewDTO contact) {
		final Long contactId = contactService.addNewContact(contact);
		final URI location = uriBuilders.owner().resolveContactURI(contactId);
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
		contactService.deleteContact(id);
		return Response.ok().build();

		// TODO: handle unknown ID gracefully
		// return Response.status(Status.NOT_FOUND).build();
	}

	@GET
	@Path("/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getContactProfileById(@PathParam(PROFILE_ID) long profileId) {
		final Optional<ProfileDTO> profile = contactService.getContactProfileById(profileId);

		// TODO: return Profile with header-link to contact -> how do we test
		// this on client-side?
		return profile
				.map(p -> Response.ok(p).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@GET
	@Path("/{" + CONTACT_ID + "}/profile/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public Response getContactProfileByIdAsHtml(@PathParam(PROFILE_ID) long profileId) {
		final Optional<ProfileDTO> profile = contactService.getContactProfileById(profileId);
		return profile
				.map(p -> Response.ok(template.contactProfilePage(p).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@POST
	@Path("/{" + CONTACT_ID + "}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewContactProfile(@PathParam(CONTACT_ID) long contactId, ProfileNewDTO profile) {
		final Optional<Long> profileId = contactService.addNewContactProfile(contactId, profile);
		return profileId
				.map(pid -> Response.created(uriBuilders.owner().resolveContactProfileURI(contactId, pid)).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

}
