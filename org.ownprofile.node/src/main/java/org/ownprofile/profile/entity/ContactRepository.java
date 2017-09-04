package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

import org.ownprofile.boundary.owner.ContactCreateAndUpdateDTO;

public interface ContactRepository {

	public List<ContactEntity> getAllContacts();

	public Optional<ContactEntity> getContactById(long id);

	public void addContact(ContactEntity contact);

	public Optional<ProfileEntity> getContactProfileById(long id);
	
	public void addContactProfile(ProfileEntity profile);
	
	public void deleteContact(ContactEntity contact);
	
	public void updateContact(ContactEntity contact, ContactCreateAndUpdateDTO updateDto);

}
