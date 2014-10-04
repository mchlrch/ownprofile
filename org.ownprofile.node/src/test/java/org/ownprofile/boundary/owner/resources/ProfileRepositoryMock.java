package org.ownprofile.boundary.owner.resources;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;
import org.ownprofile.profile.entity.ProfileSource;

public class ProfileRepositoryMock implements ProfileRepository {

	private final List<ProfileEntity> ownerProfiles = Arrays.asList(new ProfileEntity[] { new TestProfileEntity(17L, ProfileSource.createLocalSource(), "private") });

	private final List<ProfileEntity> profiles = Arrays.asList(new ProfileEntity[] { new TestProfileEntity(92L, ProfileSource.createRemoteSource("http://localhost"), "professional") });

	public ProfileEntity addedProfile;

	private final Field profileIdField;
	private long profileEntityCount;
	
	public ProfileRepositoryMock() {
		try {
			this.profileIdField = ProfileEntity.class.getDeclaredField("id");
			this.profileIdField.setAccessible(true);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<ProfileEntity> getAllOwnerProfiles() {
		return Collections.unmodifiableList(this.ownerProfiles);
	}
	
	@Override
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		return Optional.of(ownerProfiles.get(0));
	}
	
	// TODO: legacy? still needed?
	public List<ProfileEntity> getProfiles() {
		return Collections.unmodifiableList(this.profiles);
	}

	@Override
	public void addProfile(ProfileEntity profile) {
		initializeIdIfNull(profile);
		
		this.addedProfile = profile;
	}
	
	private void initializeIdIfNull(ProfileEntity profile) {
		try {
			if (this.profileIdField.get(profile) == null) {
				this.profileIdField.set(profile, profileEntityCount++);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}

}
