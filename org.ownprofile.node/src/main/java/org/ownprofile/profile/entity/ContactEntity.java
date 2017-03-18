package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;
import org.ownprofile.boundary.owner.ContactHeaderDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;

@Entity
public class ContactEntity {
	
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	protected Long id;

	private String petname;
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="contact")
	private List<ProfileEntity> profiles = new ArrayList<ProfileEntity>();
	
	protected ContactEntity() {
	}
	
	protected ContactEntity(Builder b) {
		this.petname = b.petname;
	}

	public Optional<Long> getId() {
		return Optional.ofNullable(this.id);
	}
	
	public String getPetname() {
		return this.petname;
	}
	
	public List<ProfileEntity> getProfiles() {
		return Collections.unmodifiableList(this.profiles);
	}
	
	protected void addProfile0(ProfileEntity profile) {
		checkNotNull(profile, "profile is null");
		this.profiles.add(profile);
	}
	
	protected void updateFromDto(ContactHeaderDTO dto) {
		this.petname = dto.petname;
	}
	
	@Override
	public String toString() {
		return String.format("ContactEntity: %s", this.petname);
	}
	
	// ----------------------------------------
	public static class Builder extends EntityBuilder<ContactEntity> {
		
		private String petname;
		
        public Builder fromDto(ContactNewDTO dto) {
        	withPetname(dto.petname);
        	return this;        	
        }
        
        public Builder withPetname(String petname) {
            this.petname = petname;
            return this;
        }
        
        @Override
        protected ContactEntity create() {
        	final ContactEntity result = new ContactEntity(this);
        	return result;
        }
	}
	
}
