package org.ownprofile.boundary.owner.resources;

import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;

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
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return this.delegate.getContactProfileById(id);
	}

}
