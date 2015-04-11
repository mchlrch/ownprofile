package org.ownprofile.boundary.owner.resources;

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
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;

@Path("/owner/profiles")
public class OwnerApiProfileResource {

	@Inject
	private ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;
	
	@Inject
	private OwnerApiProfileTemplate template;
	
	private final UriBuilders uriBuilders;
	
	@Inject
	public OwnerApiProfileResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProfileDTO> getOwnerProfiles() {
		final List<ProfileEntity> profiles = this.profileService.getOwnerProfiles();

		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertOwnerProfileToView(p, uriBuilders.owner()))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getOwnerProfilesAsHtml() {
		final List<ProfileDTO> ownerProfiles = getOwnerProfiles();
		return template.ownerProfilesOverviewPage(ownerProfiles).toString();
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getOwnerProfileById(@PathParam(PROFILE_ID) long id) {
		
		// TODO: handle unknown ID gracefully
		final ProfileEntity profile = this.profileService.getOwnerProfileById(id).get();

		final ProfileDTO result = converter.convertOwnerProfileToView(profile, uriBuilders.owner()); 

		return result;
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.TEXT_HTML)
	public String getOwnerProfileByIdAsHtml(@PathParam(PROFILE_ID) long id) {
		final ProfileDTO profile = getOwnerProfileById(id);
		return template.ownerProfilePage(profile).toString();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewOwnerProfile(ProfileNewDTO profile) {
		final ProfileEntity newProfile = this.converter.createEntityForOwnerProfile(profile);
		this.profileService.addNewOwnerProfile(newProfile);
		
		final URI location = uriBuilders.owner().resolveOwnerProfileURI(newProfile.getId().get());
		return Response.created(location).build();
	}
		
}
