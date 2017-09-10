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
		final Builder b = new Builder()
			.withContact(contact)
			.withHandle(handle)
			.withSource(source)
			.withName(profileName)
			.withBody(body);
		
		final ProfileEntity result = b.build();
		return result;
	}
	
	public static ProfileEntity createMyProfile(ProfileSource source, String profileName, ProfileBody body) {
		final Builder b = new Builder()
				.withHandle(ProfileHandle.createRandomHandle())
				.withSource(source)
				.withName(profileName)
				.withBody(body);
			
		final ProfileEntity result = b.build();
		return result;
	}
	
	protected ProfileEntity() {
	}
	
	protected ProfileEntity(Builder b) {
		if (b.contact != null) {
			this.contact = b.contact;
			this.contact.addProfile0(this);
		}
		
		this.handle = checkNotNull(b.handle, "handle is null");
		this.source = checkNotNull(b.source, "source is null");
		this.body = checkNotNull(b.body, "body is null");

		this.profileName = b.profileName;
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
	
	public ProfileBody getBody() {
		return this.body;
	}
	
	protected void updateFromStruct(Struct update) {
		this.profileName = update.profileName;
		this.body = update.body;
	}

	@Override
	public String toString() {
		return String.format("ProfileEntity: %s [%s]", this.profileName, this.source);
	}
	
	// ----------------------------------------
	public static abstract class AbstractStruct<T extends AbstractStruct<T>> {

		protected ContactEntity contact;
		protected ProfileHandle handle;
		protected ProfileSource source;
		protected String profileName;
		protected ProfileBody body;

		protected abstract T self();

		public T withContact(ContactEntity contact) {
			this.contact = contact;
			return self();
		}

		public T withHandle(ProfileHandle handle) {
			this.handle = handle;
			return self();
		}

		public T withSource(ProfileSource source) {
			this.source = source;
			return self();
		}

		public T withName(String profileName) {
			this.profileName = profileName;
			return self();
		}

		public T withBody(ProfileBody body) {
			this.body = body;
			return self();
		}
	}

	public static class Struct extends AbstractStruct<Struct> {

		@Override
		protected Struct self() {
			return this;
		}
	}

	public static class Builder extends AbstractStruct<Builder> {

		@Override
		protected Builder self() {
			return this;
		}

		public ProfileEntity build() {
			final ProfileEntity result = new ProfileEntity(this);
			return result;
		}

	}

}
