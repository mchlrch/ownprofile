package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ownprofile.profile.entity.IdInitializer.IdSource;

public class ProfileRepositoryMock implements ProfileRepository {
	private final IdInitializer<ProfileEntity> profileIdInitializer;

	private final Map<Long, ProfileEntity> ownerProfiles = new HashMap<Long, ProfileEntity>();
	private final Map<ProfileHandle, ProfileEntity> ownerProfilesByHandle = new HashMap<ProfileHandle, ProfileEntity>();
	
	public ProfileEntity addedProfile;

	public ProfileRepositoryMock(IdInitializer<ProfileEntity> profileIdInitializer) {
		this.profileIdInitializer = checkNotNull(profileIdInitializer);
	}
	
	public IdSource profileIdSource() {
		return profileIdInitializer.idSource;
	}

	@Override
	public List<ProfileEntity> getAllOwnerProfiles() {
		return new ArrayList<ProfileEntity>(ownerProfiles.values());
	}

	@Override
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		return Optional.ofNullable(ownerProfiles.get(id));
	}
	
	@Override
	public Optional<ProfileEntity> getOwnerProfileByHandle(ProfileHandle handle) {
		return Optional.ofNullable(ownerProfilesByHandle.get(handle));
	}

	@Override
	public void addProfile(ProfileEntity profile) {
		final Long id = profileIdInitializer.initIdIfNull(profile);
		if (ownerProfiles.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with id[%d]", id));
		}
		
		final ProfileHandle handle = profile.getHandle().get();
		if (ownerProfilesByHandle.containsKey(handle)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with handle[%s]", handle));
		}

		this.ownerProfiles.put(id, profile);
		this.ownerProfilesByHandle.put(handle, profile);
		this.addedProfile = profile;
	}

}
