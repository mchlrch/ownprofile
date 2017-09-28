package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.util.Map;
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
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactService;

@Path("/contactprofiles")
public class ContactProfilesResource {
	
	@Inject
	private ContactService contactService;

	@Inject
	private ContactsTemplate template;
	
	// TODO: remove this dependency
	@Inject
	private ProfileConverter profileConverter;

	private final UriBuilders uriBuilders;

	@Inject
	public ContactProfilesResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
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
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public Response getContactProfileByIdAsHtml(@PathParam(PROFILE_ID) long profileId) {
		final Optional<ProfileDTO> profile = contactService.getContactProfileById(profileId);
		return profile
				.map(p -> Response.ok(template.contactProfilePage(p).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@PUT
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateContactProfile(@PathParam(PROFILE_ID) long id, ProfileCreateAndUpdateDTO updateDto) {
		final boolean profileUpdated = contactService.updateProfile(id, updateDto);
		
		if (profileUpdated) {
			return Response.ok().build();			
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@DELETE
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteContactProfile(@PathParam(PROFILE_ID) long id) {
		final boolean profileDeleted = contactService.deleteContactProfile(id);
		
		if (profileDeleted) {
			return Response.ok().build();			
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}/editContactProfileHtmlForm")
	@Produces(MediaType.TEXT_HTML)
	public Response editContactProfileHtmlForm(@PathParam(PROFILE_ID) long id) {
		final Optional<ProfileDTO> profile = contactService.getContactProfileById(id);
		return profile
				.map(p -> Response.ok(template.editContactProfileForm(p).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@POST
	@Path("/{" + PROFILE_ID + "}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response formSubmit(@PathParam(PROFILE_ID) long id, MultivaluedMap<String, String> formParams) {

		// edit-button, delete-button, edit-form-submit, edit-form-cancel
		final String actionValue = formParams.getFirst(BoundaryConstants.ContactProfileForm.ACTION_INPUT_NAME);
		
		switch (actionValue) {
		case BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_EDIT:
			{
				// TODO: return Status.NOT_FOUND if necessary
				final URI location = uriBuilders.owner().resolveEditContactProfileHtmlFormURI(id);
				return Response.seeOther(location).build();
			}
			// break;
			
		case BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_DELETE:
			final boolean profileDeleted = contactService.deleteContactProfile(id);
			
			if (profileDeleted) {
				final URI location = uriBuilders.owner().getContactsURI();
				return Response.seeOther(location).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
			// break;

		case BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_SUBMIT_EDIT:
			final String profileName = formParams.getFirst(ProfileHeaderDTO.P_PROFILENAME);
			final String bodyAsJson = formParams.getFirst(ProfileDTO.P_BODY);
			
			// TODO: return to edit-form, if json parsing fails
			final Map<String, Object> body = profileConverter.json2map(bodyAsJson);
			final ProfileCreateAndUpdateDTO updateDto = new ProfileCreateAndUpdateDTO(profileName, body);
			
			final boolean profileUpdated = contactService.updateProfile(id, updateDto);
			
			if (profileUpdated) {
				final URI location = uriBuilders.owner().resolveContactProfileURI(id);
				return Response.seeOther(location).build();
			} else {
				return Response.status(Status.NOT_FOUND).build();
			}
			// break;

		case BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_CANCEL_EDIT:
			{
				final URI location = uriBuilders.owner().resolveContactProfileURI(id);
				return Response.seeOther(location).build();
			}
			// break;
		
		default:
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

}
