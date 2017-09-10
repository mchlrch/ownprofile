package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

public interface ContactRepository {

	public List<ContactEntity> getAllContacts();

	public Optional<ContactEntity> getContactById(long id);

	public void addContact(ContactEntity contact);

	public void deleteContact(ContactEntity contact);
	
	public void updateContact(ContactEntity contact, ContactEntity.Struct update);
	
	// ----------------------------
	
	public Optional<ProfileEntity> getContactProfileById(long id);
	
	public void addContactProfile(ProfileEntity profile);
	
	public void updateProfile(ProfileEntity profile, ProfileEntity.Struct update);

	public void deleteContactProfile(ProfileEntity profile);

}
