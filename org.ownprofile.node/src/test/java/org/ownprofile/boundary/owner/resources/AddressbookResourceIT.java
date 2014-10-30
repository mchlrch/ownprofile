package org.ownprofile.boundary.owner.resources;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.ServiceIntegrationTestSession;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactDtoOutCompareUtil;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.client.TestOwnerClient;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

// each testmethod invokes at most one method on the resource
public class AddressbookResourceIT {

	// TODO: http://www.petervannes.nl/files/084d1067451c4f9a56f9b865984f803d-52.php
	
	private static final RepoProxies repoProxies = new RepoProxies();
	private static ServiceIntegrationTestSession session;

	private TestOwnerClient client;
	private ContactRepositoryMock contactRepoMock;
	private ProfileRepositoryMock profileRepoMock;
	
	private ContactEntity kottan;

	@BeforeClass
	public static void startJetty() throws Exception {
		session = new ServiceIntegrationTestSession(repoProxies.createCustomModule());
		session.server.start();
	}

	@AfterClass
	public static void stopJetty() throws Exception {
		session.server.stop();
	}

	// =============================================================

	@Before
	public void setup() {
		this.client = session.getOrCreateOwnerClient();
		// TODO: once we have shared API <if>, we could dynamically proxy OwnerClient
		// .. and assert that each method got invoked exactly once after all testcases have run
		
		this.contactRepoMock = new ContactRepositoryMock();
		this.profileRepoMock = new ProfileRepositoryMock();
		repoProxies.setContactRepository(contactRepoMock);
		repoProxies.setProfileRepository(profileRepoMock);
		
		this.kottan = createContactWithProfileForKottan();
		this.contactRepoMock.addContact(kottan);
	}
	
	@After
	public void tearDown() {
		repoProxies.clearDelegates();
	}
	
	private ContactEntity createContactWithProfileForKottan() {
		final TestContactEntity result = new TestContactEntity(this.contactRepoMock.contactIdSource.nextId(), "kottan");
		
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"Alfred\",\"lastName\":\"Kottan\",\"address\":{\"city\":\"Wien\"}}");
		final TestProfileEntity profile = new TestProfileEntity(this.profileRepoMock.profileIdSource.nextId(), result, ProfileSource.createLocalSource(), "privat", body);
		
		return result;
	}
	
	// -------------------------------------------------------------
	
	@Test
	public void shouldGetContacts() {
		final List<ContactDTO> contacts = client.getContacts();

		Assert.assertEquals(contactRepoMock.getAllContacts().size(), contacts.size());

		final Iterator<ContactEntity> expectedIt = contactRepoMock.getAllContacts().iterator();
		final Iterator<ContactDTO> actualIt = contacts.iterator();
		while (expectedIt.hasNext()) {
			ContactDtoOutCompareUtil.assertContentIsEqual(expectedIt.next(), actualIt.next(), client.getUriBuilder());
		}		
	}

	@Test
	public void shouldGetContactById() {
		final Long contactId = this.kottan.getId().get();
		
		final ContactAggregateDTO contact = client.getContactById(contactId);

		Assert.assertNotNull(contact);

		final ContactEntity expected = contactRepoMock.getContactById(contactId).get();
		ContactDtoOutCompareUtil.assertContentIsEqual(expected, contact, client.getUriBuilder());
	}
	
	@Test
	public void shouldAddNewContact() {
		final ContactNewDTO newContact = new ContactNewDTO("schrammel");
		
		final URI location = client.addNewContact(newContact);
		
		final ContactEntity actual = contactRepoMock.addedContact;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newContact.petname, actual.getPetname());
		
		final URI expectedLocation = client.getUriBuilder().resolveContactURI(actual.getId().get());
		Assert.assertEquals(expectedLocation, location);
	}

	@Test
	public void shouldGetContactProfileById() {
		final Long profileId = this.kottan.getProfiles().iterator().next().getId().get();
		
		final ProfileDTO profile = client.getContactProfileById(profileId);
		
		Assert.assertNotNull(profile);
		
		final ProfileEntity expected = contactRepoMock.getContactProfileById(profileId).get();
		ProfileDtoOutCompareUtil.assertContentIsEqual(expected, profile, client.getUriBuilder());
	}
	
	@Test
	public void shouldAddNewContactProfile() {
		final Long contactId = this.kottan.getId().get();
		Assert.assertNotNull(contactId);
		
		final Map<String, Object> body = Collections.emptyMap();
		final ProfileNewDTO newProfile = new ProfileNewDTO("work", body);

		final URI location = client.addNewContactProfile(contactId, newProfile);

		final ProfileEntity actual = profileRepoMock.addedProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newProfile.profileName, actual.getProfileName());
		
		final URI expectedLocation = client.getUriBuilder().resolveContactProfileURI(actual.getId().get());
		Assert.assertEquals(expectedLocation, location);
	}
	
}
