package org.ownprofile.boundary.owner.resources;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.DemoProfileFactory;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;

@Path(BoundaryConstants.RESOURCEPATH_DEMO)
public class DemoResource {

	@Inject
	private DemoProfileFactory demoProfileFactory;

	@Inject
	private ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;

	private void addNewOwnerProfile(ProfileNewDTO profile) {
		final ProfileEntity newProfile = this.converter.createEntityForOwnerProfile(profile);
		this.profileService.addNewOwnerProfile(newProfile);
	}

	@POST
	@Path("init-ownerprofiles")
	public Response initDemoProfiles(@Context UriInfo uriInfo) {
		for (ProfileNewDTO p : this.demoProfileFactory.createDemoProfiles()) {
			this.addNewOwnerProfile(p);
		}

		URI uri = uriInfo.getBaseUriBuilder().path(BoundaryConstants.RESOURCEPATH_OWNER_PROFILES).build();
		return Response.seeOther(uri).build();
	}

	@GET
	@Path("/test")
	public String test(@Context HttpHeaders httpHeaders, @Context Request request, @Context UriInfo uriInfo, @Context SecurityContext securityContext,
			@HeaderParam("X-header") String xHeader) {

		final URI requestUri = uriInfo.getRequestUri();
		return requestUri.toString();
	}

}
