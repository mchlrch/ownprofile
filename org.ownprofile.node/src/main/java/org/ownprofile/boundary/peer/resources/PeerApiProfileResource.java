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
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;

@Path("/peer/profiles")
public class PeerApiProfileResource {

	@Inject
	ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;

	private final UriBuilders uriBuilders;
	
	@Inject
	public PeerApiProfileResource(@Context final UriInfo uriInfo, UriBuilders uriBuilders) {
		this.uriBuilders = uriBuilders.init(uriInfo);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProfileDTO> getProfiles() {
		final List<ProfileEntity> profiles = this.profileService.getOwnerProfiles();
		
		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertOwnerProfileToPeerView(p, uriBuilders.peer()))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/{" + PROFILE_HANDLE + "}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getProfileById(@PathParam(PROFILE_HANDLE) String handleValue) {
		
		// TODO: handle unknown handles gracefully
		final ProfileHandle handle = ProfileHandle.fromString(handleValue);
		final ProfileEntity profile = this.profileService.getOwnerProfileByHandle(handle).get();

		final ProfileDTO result = converter.convertOwnerProfileToPeerView(profile, uriBuilders.peer()); 

		return result;
	}

}
