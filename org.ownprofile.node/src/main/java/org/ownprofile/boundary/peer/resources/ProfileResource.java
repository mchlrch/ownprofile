package org.ownprofile.boundary.peer.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ProfileEntity;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Path(BoundaryConstants.RESOURCEPATH_PEER_PROFILES)
public class ProfileResource {

	@Inject
	ProfileDomainService profileService;

	@Inject
	private ProfileConverter converter;

//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<Profile> getProfiles() {
//		final List<ProfileEntity> profiles = this.profileService.getOwnerProfiles();
//
//		final List<Profile> result = Lists.transform(profiles, new Function<ProfileEntity, Profile>() {
//			@Override
//			public Profile apply(ProfileEntity in) {
//				return converter.convertToView(in);
//			}
//		});
//
//		return result;
//	}

}
