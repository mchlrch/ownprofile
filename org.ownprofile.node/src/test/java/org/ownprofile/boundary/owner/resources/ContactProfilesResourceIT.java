package org.ownprofile.boundary.owner.resources;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ProfileCreateAndUpdateDTO;
import org.ownprofile.boundary.ServiceIntegrationTestSession;
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
public class ContactProfilesResourceIT {

	private static final RepoProxies repoProxies = new RepoProxies();
	private static ServiceIntegrationTestSession session;

	private TestOwnerClient client;
	private ContactRepositoryMock contactRepoMock;
	private MyProfileRepositoryMock profileRepoMock;
	
	private ContactEntity kottan;
	private ProfileEntity kottansProfile;

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
		
		final IdInitializer<ProfileEntity> profileIdInitializer = new IdInitializer<>(ProfileEntity.class);
		this.contactRepoMock = new ContactRepositoryMock(profileIdInitializer);
		this.profileRepoMock = new MyProfileRepositoryMock(profileIdInitializer);
		repoProxies.setContactRepository(contactRepoMock);
		repoProxies.setProfileRepository(profileRepoMock);
		
		this.kottan = createContactWithProfileForKottan();
		this.kottansProfile = kottan.getProfiles().iterator().next();
		
		this.contactRepoMock.addContact(kottan);
		
		// sanity checks
		Assert.assertNotNull(this.kottan);
		Assert.assertNotNull(this.kottansProfile);
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
	public void shouldGetContactProfileById() {
		final Long profileId = kottansProfile.getId().get();
		
		final ProfileDTO profile = client.getContactProfileById(profileId);
		
		Assert.assertNotNull(profile);
		
		final ProfileEntity expected = contactRepoMock.getContactProfileById(profileId).get();
		ProfileDtoOutCompareUtil.assertContactProfileContentIsEqualOnOwnerAPI(expected, profile, client.getUriBuilder());
	}
	
	@Test
	public void shouldDeleteContactProfile() {
		final Long profileId = kottansProfile.getId().get();
		
		final Result<Void> profileDeleted = client.deleteContactProfile(profileId);
		
		Assert.assertTrue(profileDeleted.isSuccess());
		final ProfileEntity actual = contactRepoMock.deletedContactProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(kottansProfile, actual);
	}
	
	@Test
	public void deleteInexistentContactProfile() {
		contactRepoMock.deleteContact(kottan);		
		final Long profileId = kottansProfile.getId().get();
		
		final Result<Void> profileDeleted = client.deleteContactProfile(profileId);
		
		Assert.assertTrue(profileDeleted.isFail());
		Assert.assertTrue("HTTP 404 expected", profileDeleted.getFail().getMessage().contains("404"));
	}
	
	@Test
	public void shouldUpdateContactProfile() {
		final Long profileId = kottansProfile.getId().get();
		
		final String profileNameUpdate = "dienstlich";
		final Map<String, Object> bodyUpdate = new HashMap<>();
		bodyUpdate.put("rank", "Polizeimajor");
		final ProfileCreateAndUpdateDTO profileUpdate = new ProfileCreateAndUpdateDTO(profileNameUpdate, bodyUpdate);
		
		final Result<Void> profileUpdated = client.updateContactProfile(profileId, profileUpdate);
		
		Assert.assertTrue(profileUpdated.isSuccess());
		final ProfileEntity actual = contactRepoMock.updatedContactProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(kottansProfile, actual);
		Assert.assertEquals(profileNameUpdate, kottansProfile.getProfileName());
		Assert.assertTrue(kottansProfile.getBody().getValueAsJson().contains("Polizeimajor"));
	}
	
	@Test
	public void updateInexistentContactProfile() {
		contactRepoMock.deleteContact(kottan);		
		final Long profileId = kottansProfile.getId().get();
		
		final String profileNameUpdate = "dienstlich";
		final ProfileCreateAndUpdateDTO profileUpdate = new ProfileCreateAndUpdateDTO(profileNameUpdate, null);
		
		final Result<Void> profileUpdated = client.updateContactProfile(profileId, profileUpdate);
		
		Assert.assertTrue(profileUpdated.isFail());
		Assert.assertTrue("HTTP 404 expected", profileUpdated.getFail().getMessage().contains("404"));
	}
	
}
