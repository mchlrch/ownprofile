package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public interface MyProfileRepository {

	public List<ProfileEntity> getMyProfiles();

	public Optional<ProfileEntity> getMyProfileById(long id);

	public Optional<ProfileEntity> getMyProfileByHandle(ProfileHandle handle);

	public void addMyProfile(ProfileEntity profile);

}
