package org.ownprofile.boundary.owner.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.GenericType;

import org.ownprofile.boundary.AbstractClient;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
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
		final URI uri = this.ownerUriBuilder.getContactURI();
		return doGet(new GenericType<List<ContactDTO>>() {}, uri);
	}
	
	public ContactAggregateDTO getContactById(long contactId) {
		final URI uri = this.ownerUriBuilder.resolveContactURI(contactId);
		return doGet(ContactAggregateDTO.class, uri);
	}
	
	public URI addNewContact(ContactNewDTO contact) {
		final URI uri = this.ownerUriBuilder.getContactURI();
		return doPost(contact, uri);
	}

	// -------------------------------
	
	public ProfileDTO getContactProfileById(long profileId) {		
		final URI uri = this.ownerUriBuilder.resolveContactProfileURI(-1L, profileId);
		return doGet(ProfileDTO.class, uri);
	}

	public URI addNewContactProfile(long contactId, ProfileNewDTO newProfile) {
		final URI uri = this.ownerUriBuilder.resolveAddNewContactProfileURI(contactId);
		return doPost(newProfile, uri);
	}
	
	// -------------------------------
	
	public List<ProfileDTO> getOwnerProfiles() {
		final URI uri = this.ownerUriBuilder.getOwnerProfileURI();
		return doGet(new GenericType<List<ProfileDTO>>() {}, uri);
	}

	public ProfileDTO getOwnerProfileById(long profileId) {
		final URI uri = this.ownerUriBuilder.resolveOwnerProfileURI(profileId);
		return doGet(ProfileDTO.class, uri);
	}
	
	public URI addNewOwnerProfile(ProfileNewDTO newProfile) {
		final URI uri = this.ownerUriBuilder.getOwnerProfileURI();
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
