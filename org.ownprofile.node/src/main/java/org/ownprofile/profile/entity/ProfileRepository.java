package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository {

	public List<ProfileEntity> getAllOwnerProfiles();

	public Optional<ProfileEntity> getOwnerProfileById(long id);

	public void addProfile(ProfileEntity profile);

}
