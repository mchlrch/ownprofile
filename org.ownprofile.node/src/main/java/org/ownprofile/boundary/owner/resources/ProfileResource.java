package org.ownprofile.boundary.owner.resources;

import static com.google.common.base.Preconditions.checkNotNull;

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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
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

		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		
		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertToView(p, uriBuilderCallback))
				.collect(Collectors.toList());
		
		return result;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileDTO getOwnerProfileById(@PathParam("id") long id, @Context final UriInfo uriInfo) {
		
		// TODO: handle unknown ID gracefully
		final ProfileEntity profile = this.profileService.getOwnerProfileById(id).get();

		// TODO: fixme - http://localhost:9080/webapi/owner/profiles/1/1
		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final ProfileDTO result = converter.convertToView(profile, uriBuilderCallback); 

		return result;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewOwnerProfile(ProfileNewDTO profile, @Context final UriInfo uriInfo) {
		final ProfileEntity newProfile = this.converter.createEntityForOwnerProfile(profile);
		this.profileService.addNewOwnerProfile(newProfile);
		
		final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback(uriInfo);
		final URI location = uriBuilderCallback.buildProfileUri(newProfile);
		return Response.created(location).build();
	}
		
	// --------------------------------------------------
	private static class MyUriBuilderCallback implements ProfileConverter.UriBuilderCallback {
		private final UriInfo uriInfo;
		private MyUriBuilderCallback(UriInfo uriInfo) {
			this.uriInfo = checkNotNull(uriInfo);
		}
		
		@Override
		public URI buildProfileUri(ProfileEntity profile) {
			final UriBuilder profileUriTemplate = this.uriInfo.getAbsolutePathBuilder().path("/{profileId}");
			profileUriTemplate.resolveTemplate("profileId", profile.getId().get());
			return profileUriTemplate.build();
		}
	};

}
