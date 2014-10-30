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

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;

@Path(BoundaryConstants.RESOURCEPATH_OWNER_PROFILES)
public class ProfileResource {

	@Inject
	private ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProfileDTO> getOwnerProfiles(@Context final UriInfo uriInfo) {
		final List<ProfileEntity> profiles = this.profileService.getOwnerProfiles();

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);		
		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertToView(p, uriBuilder))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/{" + PROFILE_ID + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getOwnerProfileById(@PathParam(PROFILE_ID) long id, @Context final UriInfo uriInfo) {
		
		// TODO: handle unknown ID gracefully
		final ProfileEntity profile = this.profileService.getOwnerProfileById(id).get();

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final ProfileDTO result = converter.convertToView(profile, uriBuilder); 

		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewOwnerProfile(ProfileNewDTO profile, @Context final UriInfo uriInfo) {
		final ProfileEntity newProfile = this.converter.createEntityForOwnerProfile(profile);
		this.profileService.addNewOwnerProfile(newProfile);
		
		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		final URI location = uriBuilder.resolveOwnerProfileURI(newProfile.getId().get());
		return Response.created(location).build();
	}
		
}
