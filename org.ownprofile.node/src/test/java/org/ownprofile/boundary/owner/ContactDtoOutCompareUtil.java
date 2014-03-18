package org.ownprofile.boundary.owner;

import java.util.Iterator;

import org.junit.Assert;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;

public class ContactDtoOutCompareUtil {

	public static void assertContentIsEqual(ContactEntity expected, ContactDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		assertContentIsEqual(expected, actual.header);

		final Iterator<ProfileEntity> expectedProfileIt = expected.getProfiles().iterator();
		final Iterator<ProfileHeaderDTO> actualProfileIt = actual.getProfiles().iterator();
		while (expectedProfileIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContentIsEqual(expectedProfileIt.next(), actualProfileIt.next());
		}
	}
	
	public static void assertContentIsEqual(ContactEntity expected, ContactAggregateDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		assertContentIsEqual(expected, actual.header);

		final Iterator<ProfileEntity> expectedProfileIt = expected.getProfiles().iterator();
		final Iterator<ProfileDTO> actualProfileIt = actual.getProfiles().iterator();
		while (expectedProfileIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContentIsEqual(expectedProfileIt.next(), actualProfileIt.next());
		}
	}
	
	public static void assertContentIsEqual(ContactEntity expected, ContactHeaderDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		Assert.assertEquals(expected.getId().get(), actual.id);
		Assert.assertEquals(expected.getPetname(), actual.petname);
		Assert.assertNotNull(actual.href);
	}
}
