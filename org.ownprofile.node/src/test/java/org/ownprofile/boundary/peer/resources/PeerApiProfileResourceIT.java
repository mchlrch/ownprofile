package org.ownprofile.boundary.peer.resources;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ServiceIntegrationTestSession;
import org.ownprofile.boundary.peer.client.TestPeerClient;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileRepositoryMock;
import org.ownprofile.profile.entity.ProfileSource;
import org.ownprofile.profile.entity.RepoProxies;
import org.ownprofile.profile.entity.TestProfileEntity;

// each testmethod invokes at most one method on the resource
public class PeerApiProfileResourceIT {

	private static final RepoProxies repoProxies = new RepoProxies();
	private static ServiceIntegrationTestSession session;

	private TestPeerClient client;
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
		this.client = session.getOrCreatePeerClient();
		
		this.profileRepoMock = new ProfileRepositoryMock();
		repoProxies.setProfileRepository(profileRepoMock);
		
		this.profileRepoMock.addProfile(createOwnerProfile());
	}

	@After
	public void tearDown() {
		repoProxies.clearDelegates();
	}
	
	private ProfileEntity createOwnerProfile() {
		return TestProfileEntity.createOwnProfile(this.profileRepoMock.profileIdSource.nextId(), ProfileSource.createLocalSource(), "private");
		// new TestProfileEntity(92L, ProfileSource.createRemoteSource("http://localhost"), "professional");
	}

	// -------------------------------------------------------------

	@Test
	public void shouldGetProfiles() {
		final List<ProfileDTO> profiles = client.getProfiles();

		Assert.assertEquals(profileRepoMock.getAllOwnerProfiles().size(), profiles.size());

		final Iterator<ProfileEntity> expectedIt = profileRepoMock.getAllOwnerProfiles().iterator();
		final Iterator<ProfileDTO> actualIt = profiles.iterator();
		while (expectedIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContentIsEqualOnPeerAPI(expectedIt.next(), actualIt.next(), client.getUriBuilder());
		}
	}
	
	@Test
	public void shouldGetProfileById() {
		final ProfileHandle handle = profileRepoMock.addedProfile.getHandle().get();
		final ProfileDTO profile = client.getProfileByHandle(handle);

		Assert.assertNotNull(profile);

		final ProfileEntity expected = profileRepoMock.getOwnerProfileByHandle(handle).get();				
		ProfileDtoOutCompareUtil.assertContentIsEqualOnPeerAPI(expected, profile, client.getUriBuilder());
	}

}
