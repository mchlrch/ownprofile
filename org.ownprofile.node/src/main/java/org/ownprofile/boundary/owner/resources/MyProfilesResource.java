package org.ownprofile.boundary.owner.resources;

import static org.ownprofile.boundary.BoundaryConstants.PROFILE_ID;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.MyProfileService;

@Path("/myprofiles")
public class MyProfilesResource {
	
	@Inject
	private MyProfileService pService;
	
	@Inject
	private MyProfilesTemplate template;
	
	private final UriBuilders uriBuilders;
	
	@Inject
	public MyProfilesResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProfileDTO> getMyProfiles() {
		final List<ProfileDTO> result = pService.getMyProfiles();
		return result;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getMyProfilesAsHtml() {
		final List<ProfileDTO> myProfiles = getMyProfiles();
		return template.myProfilesOverviewPage(myProfiles).toString();
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMyProfileById(@PathParam(PROFILE_ID) long id) {
		final Optional<ProfileDTO> profile = pService.getMyProfileById(id);
		return profile
				.map(p -> Response.ok(p).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public Response getMyProfileByIdAsHtml(@PathParam(PROFILE_ID) long id) {
		final Optional<ProfileDTO> profile = pService.getMyProfileById(id);
		return profile
				.map(p -> Response.ok(template.myProfilePage(p).toString()).build())
				.orElse(Response.status(Status.NOT_FOUND).build());
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewMyProfile(ProfileNewDTO profile) {
		final Long profileId = pService.addNewMyProfile(profile);
		final URI location = uriBuilders.owner().resolveMyProfileURI(profileId);
		return Response.created(location).build();
	}
		
}
