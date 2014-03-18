package org.ownprofile.profile.control;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;

public class ProfileDomainService {

	@Inject
	private ProfileRepository profileRepo;
	
	public List<ProfileEntity> getOwnerProfiles() {
		
		// TODO: profile filtering based on permissions
		
		final List<ProfileEntity> result = this.profileRepo.getAllOwnerProfiles();
		return result;
	}
	
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		
		// TODO: profile filtering based on permissions
		
		final Optional<ProfileEntity> result = this.profileRepo.getOwnerProfileById(id);
		return result;
	}

	public void addNewOwnerProfile(ProfileEntity newProfile) {
		checkArgument(newProfile.isOwnerProfile(), "newProfile not an ownerProfile");		
		this.profileRepo.addProfile(newProfile);
	}

}
