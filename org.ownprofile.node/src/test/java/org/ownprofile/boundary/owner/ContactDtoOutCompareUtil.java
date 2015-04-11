package org.ownprofile.boundary.owner;

import java.net.URI;
import java.util.Iterator;

import org.junit.Assert;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileDtoOutCompareUtil;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;

public class ContactDtoOutCompareUtil {

	public static void assertContentIsEqual(ContactEntity expected, ContactDTO actual, OwnerUriBuilder uriBuilder) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		assertContentIsEqual(expected, actual.header, uriBuilder);

		final Iterator<ProfileEntity> expectedProfileIt = expected.getProfiles().iterator();
		final Iterator<ProfileHeaderDTO> actualProfileIt = actual.getProfiles().iterator();
		while (expectedProfileIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContactProfileContentIsEqual(expectedProfileIt.next(), actualProfileIt.next(), uriBuilder);
		}
	}
	
	public static void assertContentIsEqual(ContactEntity expected, ContactAggregateDTO actual, OwnerUriBuilder uriBuilder) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		assertContentIsEqual(expected, actual.header, uriBuilder);

		final Iterator<ProfileEntity> expectedProfileIt = expected.getProfiles().iterator();
		final Iterator<ProfileDTO> actualProfileIt = actual.getProfiles().iterator();
		while (expectedProfileIt.hasNext()) {
			ProfileDtoOutCompareUtil.assertContactProfileContentIsEqualOnOwnerAPI(expectedProfileIt.next(), actualProfileIt.next(), uriBuilder);
		}
	}
	
	public static void assertContentIsEqual(ContactEntity expected, ContactHeaderDTO actual, OwnerUriBuilder uriBuilder) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		Assert.assertEquals(expected.getId().get(), actual.id);
		Assert.assertEquals(expected.getPetname(), actual.petname);
		
		final URI expectedHref = uriBuilder.resolveContactURI(expected.getId().get());
		final URI actualHref = actual.href;
		Assert.assertEquals(expectedHref, actualHref);
	}
}
