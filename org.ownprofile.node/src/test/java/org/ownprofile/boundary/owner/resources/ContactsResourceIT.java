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
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.ServiceIntegrationTestSession;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactCreateAndUpdateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactDtoOutCompareUtil;
import org.ownprofile.boundary.owner.client.Result;
import org.ownprofile.boundary.owner.client.TestOwnerClient;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ContactRepositoryMock;
import org.ownprofile.profile.entity.IdInitializer;
import org.ownprofile.profile.entity.MyProfileRepositoryMock;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;
import org.ownprofile.profile.entity.RepoProxies;
import org.ownprofile.profile.entity.TestProfileEntity;

// each testmethod invokes at most one method on the resource
public class ContactsResourceIT {

	private static final RepoProxies repoProxies = new RepoProxies();
	private static ServiceIntegrationTestSession session;

	private TestOwnerClient client;
	private ContactRepositoryMock contactRepoMock;
	private MyProfileRepositoryMock profileRepoMock;
	
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
		
		final IdInitializer<ProfileEntity> profileIdInitializer = new IdInitializer<>(ProfileEntity.class);
		this.contactRepoMock = new ContactRepositoryMock(profileIdInitializer);
		this.profileRepoMock = new MyProfileRepositoryMock(profileIdInitializer);
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
		final long contactId = contactRepoMock.contactIdSource().nextId();
		final TestContactEntity result = new TestContactEntity(contactId, new ContactEntity.Builder()
				.withPetname("kottan"));
		
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"Alfred\",\"lastName\":\"Kottan\",\"address\":{\"city\":\"Wien\"}}");
		TestProfileEntity.createContactProfile(this.profileRepoMock.profileIdSource().nextId(), result, ProfileHandle.createRandomHandle(), ProfileSource.createLocalSource(), "privat", body);
		
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
		final Long contactId = kottan.getId().get();
		
		final ContactAggregateDTO contact = client.getContactById(contactId);

		Assert.assertNotNull(contact);

		final ContactEntity expected = contactRepoMock.getContactById(contactId).get();
		ContactDtoOutCompareUtil.assertContentIsEqual(expected, contact, client.getUriBuilder());
	}
	
	@Test
	public void shouldAddNewContact() {
		final ContactCreateAndUpdateDTO newContact = new ContactCreateAndUpdateDTO("schrammel");
		
		final URI location = client.addNewContact(newContact);
		
		final ContactEntity actual = contactRepoMock.addedContact;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newContact.petname, actual.getPetname());
		
		final URI expectedLocation = client.getUriBuilder().resolveContactURI(actual.getId().get());
		Assert.assertEquals(expectedLocation, location);
	}
	
	@Test
	public void shouldUpdateContact() {
		final Long contactId = kottan.getId().get();
		final String petnameUpdate = "Polizeimajor Adolf Kottan";
		final ContactCreateAndUpdateDTO contactUpdate = new ContactCreateAndUpdateDTO(petnameUpdate);
		
		final Result<Void> contactUpdated = client.updateContact(contactId, contactUpdate);
		
		Assert.assertTrue(contactUpdated.isSuccess());
		final ContactEntity actual = contactRepoMock.updatedContact;
		Assert.assertNotNull(actual);
		Assert.assertEquals(kottan, actual);
		Assert.assertEquals(petnameUpdate, kottan.getPetname());
	}
	
	@Test
	public void updateInexistentContact() {
		contactRepoMock.deleteContact(kottan);		
		final Long contactId = kottan.getId().get();
		final ContactCreateAndUpdateDTO contactUpdate = new ContactCreateAndUpdateDTO("foo");
		
		final Result<Void> contactUpdated = client.updateContact(contactId, contactUpdate);
		
		Assert.assertTrue(contactUpdated.isFail());
	}
	
	@Test
	public void shouldDeleteContact() {
		final Long contactId = kottan.getId().get();
		
		final Result<Void> contactDeleted = client.deleteContact(contactId);
		
		Assert.assertTrue(contactDeleted.isSuccess());
		final ContactEntity actual = contactRepoMock.deletedContact;
		Assert.assertNotNull(actual);
		Assert.assertEquals(kottan, actual);
	}
	
	@Test
	public void deleteInexistentContact() {
		contactRepoMock.deleteContact(kottan);		
		final Long contactId = kottan.getId().get();
		
		final Result<Void> contactDeleted = client.deleteContact(contactId);
		
		Assert.assertTrue(contactDeleted.isFail());
		Assert.assertTrue("HTTP 404 expected",  contactDeleted.getFail().getMessage().contains("404"));
	}
	
	@Test
	public void shouldAddNewContactProfile() {
		final Long contactId = kottan.getId().get();
		Assert.assertNotNull(contactId);
		
		final Map<String, Object> body = Collections.emptyMap();
		final ProfileCreateAndUpdateDTO newProfile = new ProfileCreateAndUpdateDTO("work", body);

		final URI location = client.addNewContactProfile(contactId, newProfile);

		final ProfileEntity actual = contactRepoMock.addedContactProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newProfile.profileName, actual.getProfileName());
		
		final URI expectedLocation = client.getUriBuilder().resolveContactProfileURI(actual.getId().get());
		Assert.assertEquals(expectedLocation, location);
	}
	
}
