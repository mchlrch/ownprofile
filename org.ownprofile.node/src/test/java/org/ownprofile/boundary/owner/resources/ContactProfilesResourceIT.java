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
public class ContactProfilesResourceIT {

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
	public void shouldGetContactProfileById() {
		final Long profileId = kottan.getProfiles().iterator().next().getId().get();
		
		final ProfileDTO profile = client.getContactProfileById(profileId);
		
		Assert.assertNotNull(profile);
		
		final ProfileEntity expected = contactRepoMock.getContactProfileById(profileId).get();
		ProfileDtoOutCompareUtil.assertContactProfileContentIsEqualOnOwnerAPI(expected, profile, client.getUriBuilder());
	}
	
}
