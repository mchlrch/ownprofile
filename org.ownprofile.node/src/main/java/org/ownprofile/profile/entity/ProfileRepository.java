package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {

	// TODO: rename to myprofiles
	public List<ProfileEntity> getAllOwnerProfiles();

	public Optional<ProfileEntity> getOwnerProfileById(long id);

	public Optional<ProfileEntity> getOwnerProfileByHandle(ProfileHandle handle);

	public void addProfile(ProfileEntity profile);

}
