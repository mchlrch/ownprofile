package org.ownprofile.boundary.owner.resources;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

public class MockContactRepository extends ContactRepository {
	private long contactEntityCount;
	private long profileEntityCount;   // TODO: possible conflict with MockProfileRepository?

	private final List<ContactEntity> contacts = new ArrayList<ContactEntity>();
	public ContactEntity addedContact;

	private final Field contactIdField;
	
	public MockContactRepository() {
		this.contacts.add(createContactForKottan());
		
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
		
		this.addedContact = contact;
	}
	
	private void initializeIdIfNull(ContactEntity contact) {
		try {
			if (this.contactIdField.get(contact) == null) {
				this.contactIdField.set(contact, contactEntityCount++);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	public Optional<ProfileEntity> getContactProfileById(long id) {
		return Optional.of(this.contacts.get(0).getProfiles().get(0));
	}
	
	private ContactEntity createContactForKottan() {
		final TestContactEntity result = new TestContactEntity(contactEntityCount++, "kottan");
		
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"Alfred\",\"lastName\":\"Kottan\",\"address\":{\"city\":\"Wien\"}}");
		final TestProfileEntity profile = new TestProfileEntity(profileEntityCount++, result, ProfileSource.createLocalSource(), "privat", body);
		
		return result;
	}

}
