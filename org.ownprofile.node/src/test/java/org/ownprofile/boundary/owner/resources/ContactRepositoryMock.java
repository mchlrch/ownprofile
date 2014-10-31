package org.ownprofile.boundary.owner.resources;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;

public class ContactRepositoryMock implements ContactRepository {
	public final IdSource contactIdSource = new IdSource();

	private final Map<Long, ContactEntity> contacts = new HashMap<Long, ContactEntity>();
	private final Map<Long, ProfileEntity> contactProfiles = new HashMap<Long, ProfileEntity>();

	public ContactEntity addedContact;

	private final Field contactIdField;

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
		return new ArrayList<ContactEntity>(contacts.values());
	}

	@Override
	public Optional<ContactEntity> getContactById(long id) {
		return Optional.ofNullable(contacts.get(id));
	}

	@Override
	public void addContact(ContactEntity contact) {
		final Long id = initializeIdIfNull(contact);
		if (contacts.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ContactEntity with id[%d]", id));
		}

		this.contacts.put(id, contact);
		this.addedContact = contact;

		contact.getProfiles().forEach(p -> addContactProfile(p));
	}

	private Long initializeIdIfNull(ContactEntity contact) {
		try {
			Long id = (Long) contactIdField.get(contact);
			if (id == null) {
				id = this.contactIdSource.nextId();
				this.contactIdField.set(contact, id);
			}
			return id;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private void addContactProfile(ProfileEntity profile) {
		final Long id = checkNotNull(profile.getId().get());
		if (contactProfiles.containsKey(id)) {
			throw new IllegalStateException(String.format("Repo already contains ProfileEntity with id[%d]", id));
		}
		
		this.contactProfiles.put(id, profile);
	}

	@Override
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return Optional.ofNullable(contactProfiles.get(id));
	}

}
