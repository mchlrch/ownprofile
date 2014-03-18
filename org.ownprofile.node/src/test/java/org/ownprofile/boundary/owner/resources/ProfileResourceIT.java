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
import org.ownprofile.boundary.owner.client.OwnerClient;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;
import org.ownprofile.setup.GuiceModule;

// each testmethod invokes at most one method on the resource
public class ProfileResourceIT {

	private static final MockProfileRepository mockProfileRepo = new MockProfileRepository();
	private static IntegrationTestSession session;

	private final OwnerClient client;

	@BeforeClass
	public static void startJetty() throws Exception {
		session = new IntegrationTestSession(IntegrationTestSession.defaultJpaModule, new GuiceModule() {
			@Override
			protected void bindProfileRepository() {
				bind(ProfileRepository.class).toInstance(mockProfileRepo);
			};
		});
		session.server.start();
	}

	@AfterClass
	public static void stopJetty() throws Exception {
		session.server.stop();
	}

	// =============================================================

	public ProfileResourceIT() {
		this.client = session.getOrCreateOwnerClient();
	}

	@Test
	public void shouldGetOwnerProfiles() {
		final List<ProfileDTO> profiles = client.getOwnerProfiles();

		Assert.assertEquals(mockProfileRepo.getAllOwnerProfiles().size(), profiles.size());

		final Iterator<ProfileEntity> expectedIt = mockProfileRepo.getAllOwnerProfiles().iterator();
		final Iterator<ProfileDTO> actualIt = profiles.iterator();
		while (expectedIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContentIsEqual(expectedIt.next(), actualIt.next());
		}
	}
	
	@Test
	public void shouldGetOwnerProfileById() {
		final ProfileDTO profile = client.getOwnerProfileById(1L);

		Assert.assertNotNull(profile);

		final ProfileEntity expected = mockProfileRepo.getOwnerProfileById(1L).get();				
		ProfileDtoOutCompareUtil.assertContentIsEqual(expected, profile);
	}

	@Test
	public void shouldAddNewOwnerProfile() {
		final Map<String, Object> body = Collections.emptyMap();
		final ProfileNewDTO newProfile = new ProfileNewDTO("public", body);

		final URI location = client.addNewOwnerProfile(newProfile);

		final ProfileEntity actual = mockProfileRepo.addedProfile;
		Assert.assertNotNull(actual);
		Assert.assertEquals(newProfile.profileName, actual.getProfileName());
		
		Assert.assertNotNull(location);
	}

}
