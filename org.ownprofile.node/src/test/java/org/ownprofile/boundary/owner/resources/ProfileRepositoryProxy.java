package org.ownprofile.boundary.owner.resources;

import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;

public class ProfileRepositoryProxy implements ProfileRepository {

	public ProfileRepository delegate;

	@Override
	public List<ProfileEntity> getAllOwnerProfiles() {
		return this.delegate.getAllOwnerProfiles();
	}

	@Override
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		return this.delegate.getOwnerProfileById(id);
	}

	@Override
	public void addProfile(ProfileEntity profile) {
		this.delegate.addProfile(profile);
	}

}
