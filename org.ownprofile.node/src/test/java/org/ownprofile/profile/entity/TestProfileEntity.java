package org.ownprofile.profile.entity;

public class TestProfileEntity extends ProfileEntity {
	
	public static TestProfileEntity createContactProfile(Long id, ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName) {
		return createContactProfile(id, contact, handle, source, profileName, ProfileBody.createEmptyBody());
	}
	
	public static TestProfileEntity createContactProfile(Long id, ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		return new TestProfileEntity(id, contact, handle, source, profileName, body);
	}

	public static TestProfileEntity createOwnProfile(Long id, ProfileSource source, String profileName) {
		return createOwnProfile(id, source, profileName, ProfileBody.createEmptyBody());		
	}
	
	public static TestProfileEntity createOwnProfile(Long id, ProfileSource source, String profileName, ProfileBody body) {
		return new TestProfileEntity(id, source, profileName, body);		
	}
	
	// ----------------------------------------------------------------
	
	private TestProfileEntity(Long id, ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		super(contact, handle, source, profileName, body);
		this.id = id;
	}
	
	private TestProfileEntity(Long id, ProfileSource source, String profileName, ProfileBody body) {
		super(source, profileName, body);
		this.id = id;
	}
}
