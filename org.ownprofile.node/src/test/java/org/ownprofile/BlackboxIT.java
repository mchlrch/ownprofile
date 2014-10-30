package org.ownprofile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.client.OwnerClient;

/**
 * Tests an already running, possibly empty, ownprofile-node from the client-side.
 * - invokes for each resource one operation with persistent side-effect and affirms that side-effect is visible to the client
 * 
 */
@Ignore
public class BlackboxIT {

	// TODO: externalize
	private static final int port = 9080;

	private final OwnerClient client;

	public BlackboxIT() {
		this.client = new OwnerClient("http", "localhost", port);
	}

	@Test
	public void shouldCreateAndGetContact() {
		final List<ContactDTO> prevContacts = client.getContacts();

		final ContactNewDTO newContact = new ContactNewDTO("homer");
		client.addNewContact(newContact);

		// TODO: retrieve ID upon creation & also fetch Contact by ID
		final List<ContactDTO> createdContacts = client.getContacts();
		createdContacts.removeAll(prevContacts);

		Assert.assertEquals(1, createdContacts.size());
		final ContactDTO createdContact = createdContacts.get(0);

		Assert.assertEquals(newContact.petname, createdContact.header.petname);
	}

	// TODO: test proper exception handling
	// @Test(expected=SomeExceptionType)
	// public void connectShouldFail() {
	// }
	
	@Test
	public void shouldCreateAndGetOwnerProfile() {
		final List<ProfileDTO> prevProfiles = client.getOwnerProfiles();

		final ProfileNewDTO newProfile = this.createProfileOfHomerSimpson();
		client.addNewOwnerProfile(newProfile);

		// TODO: retrieve ID upon creation & also fetch Profile by ID
		final List<ProfileDTO> createdProfiles = client.getOwnerProfiles();
		createdProfiles.removeAll(prevProfiles);

		Assert.assertEquals(1, createdProfiles.size());
		final ProfileDTO createdProfile = createdProfiles.get(0);

		Assert.assertEquals(newProfile.profileName, createdProfile.header.profileName);
		Assert.assertEquals(newProfile.body, createdProfile.body);
	}

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


}
