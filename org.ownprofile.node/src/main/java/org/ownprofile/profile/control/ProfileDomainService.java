package org.ownprofile.profile.control;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.MyProfileRepository;

public class ProfileDomainService {

	@Inject
	private MyProfileRepository profileRepo;
	
	public List<ProfileEntity> getMyProfiles() {
		
		// TODO: profile filtering based on permissions
		
		final List<ProfileEntity> result = this.profileRepo.getMyProfiles();
		return result;
	}
	
	public Optional<ProfileEntity> getMyProfileById(long id) {
		
		// TODO: profile filtering based on permissions
		
		final Optional<ProfileEntity> result = this.profileRepo.getMyProfileById(id);
		return result;
	}
	
	public Optional<ProfileEntity> getMyProfileByHandle(ProfileHandle handle) {
		
		// TODO check permissions
		
		final Optional<ProfileEntity> result = this.profileRepo.getMyProfileByHandle(handle);
		return result;
		
	}

	public void addNewMyProfile(ProfileEntity newProfile) {
		checkArgument(newProfile.isMyProfile(), "newProfile not a myProfile");		
		this.profileRepo.addMyProfile(newProfile);
	}

}
