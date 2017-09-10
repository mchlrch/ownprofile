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
	
	protected void updateFromStruct(Struct update) {
		this.petname = update.petname;
	}
	
	@Override
	public String toString() {
		return String.format("ContactEntity: %s", this.petname);
	}
	
	// ----------------------------------------
	public static abstract class AbstractStruct<T extends AbstractStruct<T>> {
		protected String petname;
		
		protected abstract T self();

		public T withPetname(String petname) {
			this.petname = petname;
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
		
        public ContactEntity build() {
        	final ContactEntity result = new ContactEntity(this);
        	return result;
        }
        
	}
	
}
