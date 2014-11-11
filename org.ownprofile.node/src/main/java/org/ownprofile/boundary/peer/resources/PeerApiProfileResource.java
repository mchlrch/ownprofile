package org.ownprofile.boundary.peer.resources;

import static org.ownprofile.boundary.BoundaryConstants.PROFILE_HANDLE;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.peer.PeerUriBuilder;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;

@Path("/peer/profiles")
public class PeerApiProfileResource {

	@Inject
	ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProfileDTO> getProfiles(@Context final UriInfo uriInfo) {
		final List<ProfileEntity> profiles = this.profileService.getOwnerProfiles();
		
		final PeerUriBuilder uriBuilder = PeerUriBuilder.fromUriInfo(uriInfo);
		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertToPeerView(p, uriBuilder))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/{" + PROFILE_HANDLE + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getProfileById(@PathParam(PROFILE_HANDLE) String handleValue, @Context final UriInfo uriInfo) {
		
		// TODO: handle unknown handles gracefully
		final ProfileHandle handle = ProfileHandle.fromString(handleValue);
		final ProfileEntity profile = this.profileService.getOwnerProfileByHandle(handle).get();

		final PeerUriBuilder uriBuilder = PeerUriBuilder.fromUriInfo(uriInfo);
		final ProfileDTO result = converter.convertToPeerView(profile, uriBuilder); 

		return result;
	}

}
