package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ownprofile.profile.entity.IdInitializer.IdSource;
import org.ownprofile.profile.entity.ProfileEntity.Struct;

public class ContactRepositoryMock implements ContactRepository {
	private final IdInitializer<ContactEntity> contactIdInitializer = new IdInitializer<>(ContactEntity.class);
	private final IdInitializer<ProfileEntity> profileIdInitializer;

	private final Map<Long, ContactEntity> contacts = new HashMap<Long, ContactEntity>();
	private final Map<Long, ProfileEntity> contactProfiles = new HashMap<Long, ProfileEntity>();

	public ContactEntity addedContact;
	public ContactEntity deletedContact;
	public ContactEntity updatedContact;
	
	public ProfileEntity addedContactProfile;
	public ProfileEntity deletedContactProfile;
	public ProfileEntity updatedContactProfile;

	public ContactRepositoryMock(IdInitializer<ProfileEntity> profileIdInitializer) {
		this.profileIdInitializer = checkNotNull(profileIdInitializer);
	}
	
	public IdSource profileIdSource() {
		return profileIdInitializer.idSource;
	}
	
	public IdSource contactIdSource() {
		return contactIdInitializer.idSource;
	}

	@Override
	public List<ContactEntity> getAllContacts() {
		return new ArrayList<ContactEntity>(contacts.values());
	}

	@Override
	public Optional<ContactEntity> getContactById(long id) {
		return Optional.ofNullable(contacts.get(id));
	}

	@Override
	public void addContact(ContactEntity contact) {
		final Long id = contactIdInitializer.initIdIfNull(contact);
		if (contacts.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ContactEntity with id[%d]", id));
		}

		this.contacts.put(id, contact);
		this.addedContact = contact;

		contact.getProfiles().forEach(p -> addContactProfile(p));
	}
	
	@Override
	public void deleteContact(ContactEntity contact) {
		this.deletedContact = this.contacts.remove(contact.getId().get());
		
		this.deletedContact.getProfiles().forEach(p -> deleteContactProfile(p));
	}
	
	@Override
	public void updateContact(ContactEntity contact, ContactEntity.Struct update) {
		contact.updateFromStruct(update);
		this.updatedContact = contact;
	}

	// ----------------------
	
	@Override
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return Optional.ofNullable(contactProfiles.get(id));
	}
	
	@Override
	public void addContactProfile(ProfileEntity profile) {
		final Long id = profileIdInitializer.initIdIfNull(profile);
		if (contactProfiles.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with id[%d]", id));
		}
		
		this.contactProfiles.put(id, profile);
		this.addedContactProfile = profile;
	}
	
	@Override
	public void updateProfile(ProfileEntity profile, Struct update) {
		profile.updateFromStruct(update);
		this.updatedContactProfile = profile;
	}
	
	@Override
	public void deleteContactProfile(ProfileEntity profile) {
		this.contactProfiles.remove(profile.getId().get());
		this.deletedContactProfile = profile;
	}

}
