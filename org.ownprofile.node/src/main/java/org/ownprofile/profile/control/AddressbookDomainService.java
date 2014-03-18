package org.ownprofile.profile.control;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;

public class AddressbookDomainService {

	@Inject
	private ProfileRepository profileRepo;
	
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
		this.profileRepo.addProfile(newProfile);
	}
	
//	public ContactEntity getContact(Long id) {
//		
//		// TODO: profile filtering based on permissions
//		
//		final List<ProfileEntity> result = this.profileRepo.getAllProfiles();
//		return result;
//	}


	
//	public List<ProfileEntity> getProfiles() {
//		
//		// TODO: profile filtering based on permissions
//		
//		final List<ProfileEntity> result = this.profileRepo.getAllProfiles();
//		return result;
//	}
//
//	public void createNewProfile(ProfileEntity newProfile) {
//		
//		// TODO: avoid duplicates, assert uniqueID
//		
//		this.profileRepo.addProfile(newProfile);
//	}

}
