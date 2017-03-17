package org.ownprofile.profile.control;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.MyProfileRepository;

public class AddressbookDomainService {

	@Inject
	private MyProfileRepository profileRepo;
	
	@Inject
	private ContactRepository contactRepo;

	public List<ContactEntity> getContacts() {
		
		// TODO: profile filtering based on permissions
		
		final List<ContactEntity> result = this.contactRepo.getAllContacts();
		return result;
	}
	
	public Optional<ContactEntity> getContactById(long id) {
		
		// TODO: profile filtering based on permissions
		
		final Optional<ContactEntity> result = this.contactRepo.getContactById(id);
		return result;
	}
	
	public void addNewContact(ContactEntity newContact) {
		this.contactRepo.addContact(newContact);
	}

	public Optional<ProfileEntity> getContactProfileById(long profileId) {
		// TODO: profile filtering based on permissions
		
		final Optional<ProfileEntity> result = this.contactRepo.getContactProfileById(profileId);
		return result;
	}
	
	public void addNewContactProfile(ProfileEntity newProfile) {
		checkArgument(newProfile.isContactProfile(), "newProfile not a contactProfile");
		
		// TODO: rethink if ContactProfiles shouldn't be added via ContactRepository.addContactProfile()
		this.profileRepo.addMyProfile(newProfile);
	}
	
	public void deleteContact(ContactEntity contact) {
		this.contactRepo.deleteContact(contact);
	}
	
}
