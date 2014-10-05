package org.ownprofile.boundary.owner.resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;

public class ProfileRepositoryMock implements ProfileRepository {
	public final IdSource profileIdSource = new IdSource();
	
	private final List<ProfileEntity> ownerProfiles = new ArrayList<ProfileEntity>();
//	private final List<ProfileEntity> profiles = new ArrayList<ProfileEntity>();

	public ProfileEntity addedProfile;

	private final Field profileIdField;
	
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
//	public List<ProfileEntity> getProfiles() {
//		return Collections.unmodifiableList(this.profiles);
//	}

	@Override
	public void addProfile(ProfileEntity profile) {
		initializeIdIfNull(profile);
		
		this.ownerProfiles.add(profile);
		this.addedProfile = profile;
	}
	
	private void initializeIdIfNull(ProfileEntity profile) {
		try {
			if (this.profileIdField.get(profile) == null) {
				this.profileIdField.set(profile, this.profileIdSource.nextId());
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
	}

}
