package org.ownprofile.boundary.owner.resources;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

public class TestProfileEntity extends ProfileEntity {

	public TestProfileEntity(Long id, ProfileSource source, String profileName) {
		this(id, source, profileName, ProfileBody.createEmptyBody());
	}
	
	public TestProfileEntity(Long id, ProfileSource source, String profileName, ProfileBody body) {
		super(source, profileName, body);
		this.id = id;
	}

	public TestProfileEntity(Long id, ContactEntity contact, ProfileSource source, String profileName) {
		this(id, contact, source, profileName, ProfileBody.createEmptyBody());
	}
	
	public TestProfileEntity(Long id, ContactEntity contact, ProfileSource source, String profileName, ProfileBody body) {
		super(contact, source, profileName, body);
		this.id = id;
	}
}
