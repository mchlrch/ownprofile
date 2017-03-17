package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ProfileEntity {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	protected Long id;

	@Embedded
	private ProfileHandle handle;

	@ManyToOne
	private ContactEntity contact; 
	
	@Embedded
	private ProfileSource source;

	private String profileName;

	@Embedded
	private ProfileBody body;

	public static ProfileEntity createContactProfile(ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		final ProfileEntity result = new ProfileEntity(contact, handle, source, profileName, body);
		return result;
	}
	
	public static ProfileEntity createOwnProfile(ProfileSource source, String profileName, ProfileBody body) {
		final ProfileEntity result = new ProfileEntity(source, profileName, body);
		return result;
	}
	
	protected ProfileEntity(ContactEntity contact, ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		this(handle, source, profileName, body);
		checkNotNull(contact, "contact is null");

		this.contact = contact;
		contact.addProfile0(this);
	}
	
	protected ProfileEntity(ProfileSource source, String profileName, ProfileBody body) {
		this(ProfileHandle.createRandomHandle(), source, profileName, body);
	}
	
	protected ProfileEntity(ProfileHandle handle, ProfileSource source, String profileName, ProfileBody body) {
		this.handle = checkNotNull(handle, "handle is null");
		this.source = checkNotNull(source, "source is null");
		this.body = checkNotNull(body, "body is null");

		this.profileName = profileName;
	}
	
	protected ProfileEntity() {
	}
	
	public Optional<Long> getId() {
		return Optional.ofNullable(this.id);
	}
	
	// TODO: only optional because non-remote contact-profiles (maybe) don't need a handle ?
	public Optional<ProfileHandle> getHandle() {
		return Optional.ofNullable(this.handle);
	}
	
	public Optional<ContactEntity> getContact() {
		return Optional.ofNullable(this.contact);
	}
	
	public boolean isMyProfile() {
		return this.contact == null;
	}
	
	public boolean isContactProfile() {
		return ! isMyProfile();
	}

	public ProfileSource getSource() {
		return this.source;
	}

	public String getProfileName() {
		return this.profileName;
	}
	
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public ProfileBody getBody() {
		return this.body;
	}

	@Override
	public String toString() {
		return String.format("ProfileEntity: %s [%s]", this.profileName, this.source);
	}

}
