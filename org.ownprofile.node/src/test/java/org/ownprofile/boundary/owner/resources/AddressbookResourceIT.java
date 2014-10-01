package org.ownprofile.boundary.owner.resources;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.IntegrationTestSession;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactDtoOutCompareUtil;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.client.OwnerClient;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;
import org.ownprofile.setup.GuiceModule;

// each testmethod invokes at most one method on the resource
public class AddressbookResourceIT {

	// TODO: http://www.petervannes.nl/files/084d1067451c4f9a56f9b865984f803d-52.php

	private static final MockContactRepository mockContactRepo = new MockContactRepository();
	private static final MockProfileRepository mockProfileRepo = new MockProfileRepository();
	
	private static IntegrationTestSession session;

	private final OwnerClient client;

	@BeforeClass
	public static void startJetty() throws Exception {
		session = new IntegrationTestSession(IntegrationTestSession.defaultJpaModule, new GuiceModule() {
			@Override
			protected void bindContactRepository() {
				bind(ContactRepository.class).toInstance(mockContactRepo);
			};
			
			@Override
			protected void bindProfileRepository() {
				bind(ProfileRepository.class).toInstance(mockProfileRepo);
			}
		});
		session.server.start();
	}

	@AfterClass
	public static void stopJetty() throws Exception {
		session.server.stop();
	}

	// =============================================================

	public AddressbookResourceIT() {
		this.client = session.getOrCreateOwnerClient();
	}

	@Test
	public void shouldGetContacts() {
		final List<ContactDTO> contacts = client.getContacts();

		Assert.assertEquals(mockContactRepo.getAllContacts().size(), contacts.size());

		final Iterator<ContactEntity> expectedIt = mockContactRepo.getAllContacts().iterator();
		final Iterator<ContactDTO> actualIt = contacts.iterator();
		while (expectedIt.hasNext()) {
			ContactDtoOutCompareUtil.assertContentIsEqual(expectedIt.next(), actualIt.next());
		}		
	}

	@Test
	public void shouldGetContactById() {
		final ContactAggregateDTO contact = client.getContactById(1L);

		Assert.assertNotNull(contact);

		final ContactEntity expected = mockContactRepo.getContactById(1L).get();
		ContactDtoOutCompareUtil.assertContentIsEqual(expected, contact);
	}
	
	@Test
	public void shouldAddNewContact() {
		final ContactNewDTO newContact = new ContactNewDTO("schrammel");
		
		final URI location = client.addNewContact(newContact);
		
		final ContactEntity actual = mockContactRepo.addedContact;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newContact.petname, actual.getPetname());
		Assert.assertNotNull(location);
	}

	@Test
	public void shouldGetContactProfileById() {
		final ProfileDTO profile = client.getContactProfileById(1L);
		
		Assert.assertNotNull(profile);
		
		final ProfileEntity expected = mockContactRepo.getContactProfileById(1L).get();
		ProfileDtoOutCompareUtil.assertContentIsEqual(expected, profile);
	}
	
	@Test
	public void shouldAddNewContactProfile() {
		final ContactEntity contact = mockContactRepo.getAllContacts().get(0);
		final Long contactId = contact.getId().get();		
		Assert.assertNotNull(contactId);
		
		final Map<String, Object> body = Collections.emptyMap();
		final ProfileNewDTO newProfile = new ProfileNewDTO("work", body);

		final URI location = client.addNewContactProfile(contactId, newProfile);

		final ProfileEntity actual = mockProfileRepo.addedProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newProfile.profileName, actual.getProfileName());
		
		Assert.assertNotNull(location);
	}
}