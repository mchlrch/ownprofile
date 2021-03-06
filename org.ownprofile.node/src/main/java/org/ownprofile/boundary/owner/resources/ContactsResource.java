package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.CONTACT_ID;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactCreateAndUpdateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactHeaderDTO;
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
	public Response addContact(ContactCreateAndUpdateDTO contact) {
		final Long contactId = contactService.addContact(contact);
		final URI location = uriBuilders.owner().resolveContactURI(contactId);
		return Response.created(location).build();
	}

	@GET
	@Path("addContactHtmlForm")
	@Produces(MediaType.TEXT_HTML)
	public String addContactHtmlForm() {
		return template.addContactForm().toString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addContactFormSubmit(MultivaluedMap<String, String> formParams) {
		ContactCreateAndUpdateDTO in = new ContactCreateAndUpdateDTO(formParams.getFirst(ContactHeaderDTO.P_PETNAME));

		Response r = addContact(in);
		return Response.seeOther(r.getLocation()).build();
	}
	
	@PUT
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContact(@PathParam(CONTACT_ID) long id, ContactCreateAndUpdateDTO updateDto) {
		final boolean contactUpdated = contactService.updateContact(id, updateDto);
		
		if (contactUpdated) {
			return Response.ok().build();			
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@DELETE
	@Path("/{" + CONTACT_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContact(@PathParam(CONTACT_ID) long id) {
		final boolean contactDeleted = contactService.deleteContact(id);
		
		if (contactDeleted) {
			return Response.ok().build();			
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}/editContactHtmlForm")
	@Produces(MediaType.TEXT_HTML)
	public Response editContactHtmlForm(@PathParam(CONTACT_ID) long id) {
		final Optional<ContactAggregateDTO> contact = contactService.getContactById(id);
		return contact
				.map(c -> Response.ok(template.editContactForm(c).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@POST
	@Path("/{" + CONTACT_ID + "}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response formSubmit(@PathParam(CONTACT_ID) long id, MultivaluedMap<String, String> formParams) {

		// edit-button, delete-button, edit-form-submit, edit-form-cancel
		final String actionValue = formParams.getFirst(BoundaryConstants.ContactForm.ACTION_INPUT_NAME);
		
		switch (actionValue) {
		case BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_EDIT:
			{
				// TODO: return Status.NOT_FOUND if necessary
				final URI location = uriBuilders.owner().resolveEditContactHtmlFormURI(id);
				return Response.seeOther(location).build();
			}
			// break;
			
		case BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_DELETE:
			final boolean contactDeleted = contactService.deleteContact(id);
			
			if (contactDeleted) {
				final URI location = uriBuilders.owner().getContactsURI();
				return Response.seeOther(location).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
			// break;

		case BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_SUBMIT_EDIT:
			final String petname = formParams.getFirst(ContactHeaderDTO.P_PETNAME);
			final ContactCreateAndUpdateDTO updateDto = new ContactCreateAndUpdateDTO(petname);
			
			final boolean contactUpdated = contactService.updateContact(id, updateDto);
			
			if (contactUpdated) {
				final URI location = uriBuilders.owner().resolveContactURI(id);
				return Response.seeOther(location).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
			// break;

		case BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_CANCEL_EDIT:
			{
				final URI location = uriBuilders.owner().resolveContactURI(id);
				return Response.seeOther(location).build();
			}
			// break;
		
		default:
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/{" + CONTACT_ID + "}/profiles")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addContactProfile(@PathParam(CONTACT_ID) long contactId, ProfileCreateAndUpdateDTO profile) {
		final Optional<Long> profileId = contactService.addContactProfile(contactId, profile);
		return profileId
				.map(pid -> Response.created(uriBuilders.owner().resolveContactProfileURI(pid)).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@GET
	@Path("/{" + CONTACT_ID + "}/profiles/addContactProfileHtmlForm")
	@Produces(MediaType.TEXT_HTML)
	public String addContactProfileHtmlForm(@PathParam(CONTACT_ID) long contactId) {
		return template.addContactProfileForm(contactId).toString();
	}
	
	@POST
	@Path("/{" + CONTACT_ID + "}/profiles")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addContactProfileFormSubmit(@PathParam(CONTACT_ID) long contactId, MultivaluedMap<String, String> formParams) {
		final String profileName = formParams.getFirst(ProfileHeaderDTO.P_PROFILENAME);
		final ProfileCreateAndUpdateDTO in = new ProfileCreateAndUpdateDTO(profileName, null);

		Response r = addContactProfile(contactId, in);
		return Response.seeOther(r.getLocation()).build();
	}

}
