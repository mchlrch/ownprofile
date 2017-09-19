package org.ownprofile.boundary.owner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.persist.Transactional;

public class ContactService {

	final Logger logger = LoggerFactory.getLogger(ContactService.class);

	@Inject
	private ContactRepository contactRepo;

	@Inject
	private ContactConverter contactConverter;

	@Inject
	private ProfileConverter profileConverter;

	@Inject
	private UriBuilders uriBuilders;

	@Transactional
	public List<ContactDTO> getContacts() {

		logger.info("--------- getContacts() -----------");

		final List<ContactEntity> contacts = contactRepo.getAllContacts();

		final List<ContactDTO> result = contacts.stream()
				.map(c -> contactConverter.convertToView(c, uriBuilders.owner()))
				.collect(Collectors.toList());

		return result;
	}

	@Transactional
	public Optional<ContactAggregateDTO> getContactById(long id) {
		final Optional<ContactEntity> contact = contactRepo.getContactById(id);
		final Optional<ContactAggregateDTO> result = contact
				.map(c -> Optional.of(contactConverter.convertToAggregateView(c, uriBuilders.owner())))
				.orElse(Optional.empty());
		return result;
	}

	@Transactional
	public Long addContact(ContactCreateAndUpdateDTO contact) {
		final ContactEntity newContact = contactConverter.createEntity(contact);
		contactRepo.addContact(newContact);
		return newContact.getId().get();
	}

	@Transactional
	public boolean deleteContact(long id) {
		final Optional<ContactEntity> contact = contactRepo.getContactById(id);
		contact.ifPresent(c -> contactRepo.deleteContact(c));
		return contact.isPresent();
	}
	
	@Transactional
	public boolean updateContact(long id, ContactCreateAndUpdateDTO updateDto) {
		final Optional<ContactEntity> contact = contactRepo.getContactById(id);
		
		final ContactEntity.Struct update = contactConverter.dto2struct(updateDto); 
		
		contact.ifPresent(c -> contactRepo.updateContact(c, update));
		return contact.isPresent();
	}

	@Transactional
	public Optional<ProfileDTO> getContactProfileById(long profileId) {
		final Optional<ProfileEntity> profile = contactRepo.getContactProfileById(profileId);
		final Optional<ProfileDTO> result = profile
				.map(p -> Optional.of(profileConverter.convertContactProfileToView(p, uriBuilders.owner())))
				.orElse(Optional.empty());
		return result;
	}

	@Transactional
	public Optional<Long> addContactProfile(long contactId, ProfileCreateAndUpdateDTO profile) {
		final Optional<ContactEntity> contact = contactRepo.getContactById(contactId);

		if (contact.isPresent()) {
			// TODO: handle remote source
			final ProfileSource profileSource = ProfileSource.createLocalSource();
			final ProfileHandle handle = ProfileHandle.createRandomHandle();

			final ProfileEntity newContactProfile = profileConverter.createEntityForContactProfile(contact.get(),
					profile, handle, profileSource);
			contactRepo.addContactProfile(newContactProfile);

			return newContactProfile.getId();

		} else {
			return Optional.empty();
		}
	}
	
	@Transactional
	public boolean updateProfile(long id, ProfileCreateAndUpdateDTO updateDto) {
		final Optional<ProfileEntity> profile = contactRepo.getContactProfileById(id);
		
		final ProfileEntity.Struct update = profileConverter.dto2struct(updateDto); 
		
		profile.ifPresent(p -> contactRepo.updateProfile(p, update));
		return profile.isPresent();
	}
	
	@Transactional
	public boolean deleteContactProfile(long id) {
		final Optional<ProfileEntity> profile = contactRepo.getContactProfileById(id);
		profile.ifPresent(p -> contactRepo.deleteContactProfile(p));
		return profile.isPresent();
	}

}
