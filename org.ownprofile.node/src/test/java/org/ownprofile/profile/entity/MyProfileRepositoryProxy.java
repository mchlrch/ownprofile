package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public class MyProfileRepositoryProxy implements MyProfileRepository {

	public MyProfileRepository delegate;

	@Override
	public List<ProfileEntity> getMyProfiles() {
		return delegate.getMyProfiles();
	}

	@Override
	public Optional<ProfileEntity> getMyProfileById(long id) {
		return delegate.getMyProfileById(id);
	}
	
	@Override
	public Optional<ProfileEntity> getMyProfileByHandle(ProfileHandle handle) {
		return delegate.getMyProfileByHandle(handle);
	}

	@Override
	public void addMyProfile(ProfileEntity profile) {
		delegate.addMyProfile(profile);
	}

}
