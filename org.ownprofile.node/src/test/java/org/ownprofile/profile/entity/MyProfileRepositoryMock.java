package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ownprofile.profile.entity.IdInitializer.IdSource;

public class MyProfileRepositoryMock implements MyProfileRepository {
	private final IdInitializer<ProfileEntity> profileIdInitializer;

	private final Map<Long, ProfileEntity> myProfiles = new HashMap<Long, ProfileEntity>();
	private final Map<ProfileHandle, ProfileEntity> myProfilesByHandle = new HashMap<ProfileHandle, ProfileEntity>();
	
	public ProfileEntity addedProfile;

	public MyProfileRepositoryMock(IdInitializer<ProfileEntity> profileIdInitializer) {
		this.profileIdInitializer = checkNotNull(profileIdInitializer);
	}
	
	public IdSource profileIdSource() {
		return profileIdInitializer.idSource;
	}

	@Override
	public List<ProfileEntity> getMyProfiles() {
		return new ArrayList<ProfileEntity>(myProfiles.values());
	}

	@Override
	public Optional<ProfileEntity> getMyProfileById(long id) {
		return Optional.ofNullable(myProfiles.get(id));
	}
	
	@Override
	public Optional<ProfileEntity> getMyProfileByHandle(ProfileHandle handle) {
		return Optional.ofNullable(myProfilesByHandle.get(handle));
	}

	@Override
	public void addMyProfile(ProfileEntity profile) {
		final Long id = profileIdInitializer.initIdIfNull(profile);
		if (myProfiles.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with id[%d]", id));
		}
		
		final ProfileHandle handle = profile.getHandle().get();
		if (myProfilesByHandle.containsKey(handle)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with handle[%s]", handle));
		}

		this.myProfiles.put(id, profile);
		this.myProfilesByHandle.put(handle, profile);
		this.addedProfile = profile;
	}

}
