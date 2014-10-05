package org.ownprofile.boundary.owner.resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;

public class ContactRepositoryMock implements ContactRepository {
	public final IdSource contactIdSource = new IdSource();

	private final List<ContactEntity> contacts = new ArrayList<ContactEntity>();
	public ContactEntity addedContact;

	private final Field contactIdField;

	// TODO: getContactById() - searchById
	// TODO: getContactProfileById - searchById
	public ContactRepositoryMock() {
		try {
			this.contactIdField = ContactEntity.class.getDeclaredField("id");
			this.contactIdField.setAccessible(true);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<ContactEntity> getAllContacts() {
		return Collections.unmodifiableList(this.contacts);
	}

	@Override
	public Optional<ContactEntity> getContactById(long id) {
		return Optional.of(contacts.get(0));
	}

	@Override
	public void addContact(ContactEntity contact) {
		initializeIdIfNull(contact);
		
		this.contacts.add(contact);
		this.addedContact = contact;
	}

	private void initializeIdIfNull(ContactEntity contact) {
		try {
			if (this.contactIdField.get(contact) == null) {
				this.contactIdField.set(contact, this.contactIdSource.nextId());
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return Optional.of(this.contacts.get(0).getProfiles().get(0));
	}

}
