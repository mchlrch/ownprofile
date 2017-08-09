package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.ownprofile.boundary.owner.ContactHeaderDTO;

import com.google.inject.Provider;

public class ContactRepositoryJPA implements ContactRepository {
	
	@Inject Provider<EntityManager> em;
	
	public List<ContactEntity> getAllContacts() {
		final Query q = this.em.get().createQuery("SELECT o FROM " + ContactEntity.class.getName() + " AS o");
		final List queryResult = q.getResultList();
		return (List<ContactEntity>) queryResult;
	}

	// TODO: why not simply do it like this? : contact = em.get().find(ContactEntity.class, contact.getId().get());
	public Optional<ContactEntity> getContactById(long id) {
		final Query q = this.em.get().createQuery("SELECT o FROM " + ContactEntity.class.getName() + " AS o WHERE o.id = " + id);
		final List queryResult = q.getResultList();
		if (queryResult.isEmpty()) {
			return Optional.empty();
		} else if (queryResult.size() == 1) {
			return Optional.of( (ContactEntity) queryResult.get(0));
		} else {
			throw new IllegalStateException();
		}
	}
	
	public void addContact(ContactEntity contact) {
		em.get().persist(contact);
	}
	
	public void deleteContact(ContactEntity contact) {
		em.get().remove(contact);
	}

	public Optional<ProfileEntity> getContactProfileById(long id) {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NOT NULL AND p.id = " + id);
		final List queryResult = q.getResultList();
		if (queryResult.isEmpty()) {
			return Optional.empty();
		} else if (queryResult.size() == 1) {
			return Optional.of( (ProfileEntity) queryResult.get(0));
		} else {
			throw new IllegalStateException();
		}
	}
	
	public void addContactProfile(ProfileEntity profile) {
		em.get().persist(profile);
	}
	
	@Override
	public void updateContact(ContactEntity contact, ContactHeaderDTO updateDto) {
		contact.updateFromDto(updateDto);
	}
	
}
