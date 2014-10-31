package org.ownprofile;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.SystemTestSession;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.client.TestOwnerClient;
import org.ownprofile.setup.GuiceModule;

/**
 * Invokes operations with persistent side-effect from the client-side and
 * affirms that side-effect is visible to the client.
 */
public class BlackboxIT {

	private static SystemTestSession session;
	
	private TestOwnerClient client;

	@BeforeClass
	public static void startJetty() throws Exception {
		session = new SystemTestSession(SystemTestSession.defaultJpaModule, new GuiceModule());
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
	}

	@Test
	public void shouldCreateAndGetContact() {
		final List<ContactDTO> prevContacts = client.getContacts();

		final ContactNewDTO newContact = new ContactNewDTO("homer");
		final URI newContactLocation = client.addNewContact(newContact);

		// fetch Contact by ID
		final ContactDTO createdContact = client.doGet(ContactDTO.class, newContactLocation);
		assertContentIsEqual(newContact, createdContact);

		// fetch all Contacts
		final List<ContactDTO> createdContacts = client.getContacts();
		createdContacts.removeAll(prevContacts);

		Assert.assertEquals(1, createdContacts.size());
		assertContentIsEqual(newContact, createdContacts.get(0));
	}

	// TODO: test proper exception handling
	// @Test(expected=SomeExceptionType)
	// public void connectShouldFail() {
	// }

	@Test
	public void shouldCreateAndGetOwnerProfile() {
		final List<ProfileDTO> prevProfiles = client.getOwnerProfiles();

		final ProfileNewDTO newProfile = this.createProfileOfHomerSimpson();
		final URI newProfileLocation = client.addNewOwnerProfile(newProfile);

		// fetch Profile by ID
		final ProfileDTO createdProfile = client.doGet(ProfileDTO.class, newProfileLocation);
		assertContentIsEqual(newProfile, createdProfile);

		// fetch all Profiles
		final List<ProfileDTO> createdProfiles = client.getOwnerProfiles();
		createdProfiles.removeAll(prevProfiles);

		Assert.assertEquals(1, createdProfiles.size());
		assertContentIsEqual(newProfile, createdProfiles.get(0));
	}

	// ---------------------------------------------------

	private ProfileNewDTO createProfileOfHomerSimpson() {
		final Map<String, Object> body = new HashMap<String, Object>();

		body.put("firstName", "Homer");
		body.put("lastName", "Simpson");

		final Map<String, Object> address = new HashMap<String, Object>();
		address.put("street", "742 Evergreen Terrace");
		address.put("city", "Springfield");

		body.put("address", address);

		final ProfileNewDTO result = new ProfileNewDTO("private", body);
		return result;
	}

	private static void assertContentIsEqual(ContactNewDTO expected, ContactDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		// TODO: use ContactDtoOutCompareUtil ... or similar
		Assert.assertEquals(expected.petname, actual.header.petname);
	}

	private static void assertContentIsEqual(ProfileNewDTO expected, ProfileDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		Assert.assertEquals(expected.profileName, actual.header.profileName);
		Assert.assertEquals(expected.body, actual.body);
	}

}
