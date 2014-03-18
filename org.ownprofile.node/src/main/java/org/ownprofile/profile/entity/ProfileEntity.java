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

//	@Embedded
//	private ProfileId id;

	@ManyToOne
	private ContactEntity contact; 
	
	@Embedded
	private ProfileSource source;

	private String profileName;

	@Embedded
	private ProfileBody body;

	public ProfileEntity(ContactEntity contact, ProfileSource source, String profileName, ProfileBody body) {
		this(source, profileName, body);
		checkNotNull(contact, "contact is null");

		this.contact = contact;
		contact.addProfile0(this);
	}
	
	public ProfileEntity(ProfileSource source, String profileName, ProfileBody body) {
		checkNotNull(source, "source is null");
		checkNotNull(body, "body is null");

		this.source = source;
		this.profileName = profileName;
		this.body = body;
	}
	
	protected ProfileEntity() {
	}
	
	public Optional<Long> getId() {
		return Optional.ofNullable(this.id);
	}
	
	public Optional<ContactEntity> getContact() {
		return Optional.ofNullable(this.contact);
	}
	
	public boolean isOwnerProfile() {
		return this.contact == null;
	}
	
	public boolean isContactProfile() {
		return ! isOwnerProfile();
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
