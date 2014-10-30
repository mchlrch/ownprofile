package org.ownprofile.boundary.owner.resources;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.ownprofile.boundary.owner.client.TestOwnerClient;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

// each testmethod invokes at most one method on the resource
public class ProfileResourceIT {

	private static final RepoProxies repoProxies = new RepoProxies();
	private static ServiceIntegrationTestSession session;

	private TestOwnerClient client;
	private ProfileRepositoryMock profileRepoMock;

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
	public void setup() throws URISyntaxException {
		this.client = session.getOrCreateOwnerClient();
		
		this.profileRepoMock = new ProfileRepositoryMock();
		repoProxies.setProfileRepository(profileRepoMock);
		
		this.profileRepoMock.addProfile(createOwnerProfile());
	}

	@After
	public void tearDown() {
		repoProxies.clearDelegates();
	}
	
	private ProfileEntity createOwnerProfile() {
		return new TestProfileEntity(this.profileRepoMock.profileIdSource.nextId(), ProfileSource.createLocalSource(), "private");
		// new TestProfileEntity(92L, ProfileSource.createRemoteSource("http://localhost"), "professional");
	}

	// -------------------------------------------------------------

	@Test
	public void shouldGetOwnerProfiles() {
		final List<ProfileDTO> profiles = client.getOwnerProfiles();

		Assert.assertEquals(profileRepoMock.getAllOwnerProfiles().size(), profiles.size());

		final Iterator<ProfileEntity> expectedIt = profileRepoMock.getAllOwnerProfiles().iterator();
		final Iterator<ProfileDTO> actualIt = profiles.iterator();
		while (expectedIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContentIsEqual(expectedIt.next(), actualIt.next(), client.getUriBuilder());
		}
	}
	
	@Test
	public void shouldGetOwnerProfileById() {
		final Long profileId = 1L;
		final ProfileDTO profile = client.getOwnerProfileById(profileId);

		Assert.assertNotNull(profile);

		final ProfileEntity expected = profileRepoMock.getOwnerProfileById(profileId).get();				
		ProfileDtoOutCompareUtil.assertContentIsEqual(expected, profile, client.getUriBuilder());
	}

	@Test
	public void shouldAddNewOwnerProfile() {
		final Map<String, Object> body = Collections.emptyMap();
		final ProfileNewDTO newProfile = new ProfileNewDTO("public", body);

		final URI location = client.addNewOwnerProfile(newProfile);

		final ProfileEntity actual = profileRepoMock.addedProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newProfile.profileName, actual.getProfileName());
		
		final URI expectedLocation = client.getUriBuilder().resolveOwnerProfileURI(actual.getId().get());
		Assert.assertEquals(expectedLocation, location);
	}

}
