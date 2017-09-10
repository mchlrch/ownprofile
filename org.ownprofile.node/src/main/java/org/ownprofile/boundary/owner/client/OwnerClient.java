package org.ownprofile.boundary.owner.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.ownprofile.boundary.AbstractClient;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactCreateAndUpdateDTO;
import org.ownprofile.boundary.owner.OwnerUriBuilder;

public class OwnerClient extends AbstractClient {
	
	protected final OwnerUriBuilder ownerUriBuilder;
	
	public OwnerClient(String scheme, String host, int port) {
		try {
			this.ownerUriBuilder = OwnerUriBuilder.fromScratch(scheme, host, port);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public List<ContactDTO> getContacts() {
		final URI uri = this.ownerUriBuilder.getContactsURI();
		return doGet(new GenericType<List<ContactDTO>>() {}, uri);
	}
	
	public ContactAggregateDTO getContactById(long contactId) {
		final URI uri = this.ownerUriBuilder.resolveContactURI(contactId);
		return doGet(ContactAggregateDTO.class, uri);
	}
	
	public URI addNewContact(ContactCreateAndUpdateDTO contact) {
		final URI uri = this.ownerUriBuilder.getContactsURI();
		return doPost(contact, uri);
	}
	
	public Result<Void> updateContact(long contactId, ContactCreateAndUpdateDTO contactUpdate) {
		final URI uri = this.ownerUriBuilder.resolveContactURI(contactId);
		return doPut(contactUpdate, uri);
	}
	
	public Result<Void> deleteContact(long contactId) {
		final URI uri = this.ownerUriBuilder.resolveContactURI(contactId);
		return doDelete(uri);
	}

	// -------------------------------
	
	public ProfileDTO getContactProfileById(long profileId) {		
		final URI uri = this.ownerUriBuilder.resolveContactProfileURI(profileId);
		return doGet(ProfileDTO.class, uri);
	}

	public URI addNewContactProfile(long contactId, ProfileCreateAndUpdateDTO newProfile) {
		final URI uri = this.ownerUriBuilder.resolveAddNewContactProfileURI(contactId);
		return doPost(newProfile, uri);
	}
	
	public Result<Void> deleteContactProfile(long profileId) {
		final URI uri = this.ownerUriBuilder.resolveContactProfileURI(profileId);
		return doDelete(uri);
	}
	
	public Result<Void> updateContactProfile(long profileId, ProfileCreateAndUpdateDTO profileUpdate) {
		final URI uri = this.ownerUriBuilder.resolveContactProfileURI(profileId);
		return doPut(profileUpdate, uri);
	}
	
	// -------------------------------
	
	public List<ProfileDTO> getMyProfiles() {
		final URI uri = this.ownerUriBuilder.getMyProfileURI();
		return doGet(new GenericType<List<ProfileDTO>>() {}, uri);
	}

	public ProfileDTO getMyProfileById(long profileId) {
		final URI uri = this.ownerUriBuilder.resolveMyProfileURI(profileId);
		return doGet(ProfileDTO.class, uri);
	}
	
	public URI addNewMyProfile(ProfileCreateAndUpdateDTO newProfile) {
		final URI uri = this.ownerUriBuilder.getMyProfileURI();
		return doPost(newProfile, uri);
	}
	
	
	/// ========================================================
	public static void main(String[] args) {
		final OwnerClient client = new OwnerClient("http", "localhost", 9080);
		
		for (ContactDTO contact : client.getContacts()) {
			System.out.println(contact);
		}
		
//		for (Profile p : client.getProfiles()) {
//			System.out.println(p);
//		}
	}

}
