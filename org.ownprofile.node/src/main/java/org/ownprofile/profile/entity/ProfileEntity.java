package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;

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
		
		final ProfileEntity result = b.create();
		return result;
	}
	
	public static ProfileEntity createMyProfile(ProfileSource source, String profileName, ProfileBody body) {
		final Builder b = new Builder()
				.withHandle(ProfileHandle.createRandomHandle())
				.withSource(source)
				.withName(profileName)
				.withBody(body);
			
		final ProfileEntity result = b.create();
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
	
	// TODO: remove/resolve dependency to ProfileConverter
	protected void updateFromDto(ProfileDTO dto, ProfileConverter converter) {
		this.profileName = dto.header.profileName;
		this.body = converter.serializeBodyToJSON(dto.body);
	}

	@Override
	public String toString() {
		return String.format("ProfileEntity: %s [%s]", this.profileName, this.source);
	}
	
	// ----------------------------------------
	public static class Builder extends EntityBuilder<ProfileEntity> {

		private ContactEntity contact; 
		private ProfileHandle handle;
		private ProfileSource source;
		private String profileName;
		private ProfileBody body;
		
		// TODO: remove/resolve dependency to ProfileConverter
		public Builder fromDto(ProfileNewDTO dto, ProfileConverter converter) {
			withName(dto.profileName);
			withBody(converter.serializeBodyToJSON(dto.body));
			return this;
		}
		
		public Builder withContact(ContactEntity contact) {
			this.contact = contact;
			return this;
		}
		
		public Builder withHandle(ProfileHandle handle) {
			this.handle = handle;
			return this;
		}

		public Builder withSource(ProfileSource source) {
			this.source = source;
			return this;
		}
		
		public Builder withName(String profileName) {
			this.profileName = profileName;
			return this;
		}
		
		public Builder withBody(ProfileBody body) {
			this.body = body;
			return this;
		}
		
		@Override
		protected ProfileEntity create() {
			final ProfileEntity result = new ProfileEntity(this);
			return result;
		}
		
	}

}
