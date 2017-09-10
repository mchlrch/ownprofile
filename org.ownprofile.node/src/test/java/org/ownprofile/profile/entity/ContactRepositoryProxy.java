package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ProfileEntity.Struct;

public class ContactRepositoryProxy implements ContactRepository {

	public ContactRepository delegate;

	@Override
	public List<ContactEntity> getAllContacts() {
		return this.delegate.getAllContacts();
	}

	@Override
	public Optional<ContactEntity> getContactById(long id) {
		return this.delegate.getContactById(id);
	}

	@Override
	public void addContact(ContactEntity contact) {
		this.delegate.addContact(contact);
	}
	
	@Override
	public void deleteContact(ContactEntity contact) {
		this.delegate.deleteContact(contact);
	}
	
	@Override
	public void updateContact(ContactEntity contact, ContactEntity.Struct update) {
		this.delegate.updateContact(contact, update);
	}
	
	// -----------------------------

	@Override
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return this.delegate.getContactProfileById(id);
	}
	
	@Override
	public void addContactProfile(ProfileEntity profile) {
		delegate.addContactProfile(profile);
	}
	
	@Override
	public void updateProfile(ProfileEntity profile, Struct update) {
		delegate.updateProfile(profile, update);
	}

	@Override
	public void deleteContactProfile(ProfileEntity profile) {
		delegate.deleteContactProfile(profile);
	}
	
}
