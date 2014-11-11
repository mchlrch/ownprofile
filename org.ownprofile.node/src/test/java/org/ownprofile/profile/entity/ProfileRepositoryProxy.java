package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public class ProfileRepositoryProxy implements ProfileRepository {

	public ProfileRepository delegate;

	@Override
	public List<ProfileEntity> getAllOwnerProfiles() {
		return delegate.getAllOwnerProfiles();
	}

	@Override
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		return delegate.getOwnerProfileById(id);
	}
	
	@Override
	public Optional<ProfileEntity> getOwnerProfileByHandle(ProfileHandle handle) {
		return delegate.getOwnerProfileByHandle(handle);
	}

	@Override
	public void addProfile(ProfileEntity profile) {
		delegate.addProfile(profile);
	}

}
