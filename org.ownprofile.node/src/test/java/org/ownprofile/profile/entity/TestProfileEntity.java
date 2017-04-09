package org.ownprofile.profile.entity;

public class TestProfileEntity extends ProfileEntity {
	
	public static TestProfileEntity createContactProfile(Long id, ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName) {
		return createContactProfile(id, contact, handle, source, profileName, ProfileBody.createEmptyBody());
	}
	
	public static TestProfileEntity createContactProfile(Long id, ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		final ProfileEntity.Builder b = new ProfileEntity.Builder()
				.withContact(contact)
				.withHandle(handle)
				.withSource(source)
				.withName(profileName)
				.withBody(body);
		return new TestProfileEntity(id, b);
	}

	public static TestProfileEntity createOwnProfile(Long id, ProfileSource source, String profileName) {
		return createOwnProfile(id, source, profileName, ProfileBody.createEmptyBody());		
	}
	
	public static TestProfileEntity createOwnProfile(Long id, ProfileSource source, String profileName, ProfileBody body) {
		final ProfileEntity.Builder b = new ProfileEntity.Builder()
				.withHandle(ProfileHandle.createRandomHandle())
				.withSource(source)
				.withName(profileName)
				.withBody(body);
		return new TestProfileEntity(id, b);		
	}
	
	// ----------------------------------------------------------------
	
	private TestProfileEntity(Long id, ProfileEntity.Builder b) {
		super(b);
		this.id = id;
	}
	
}
