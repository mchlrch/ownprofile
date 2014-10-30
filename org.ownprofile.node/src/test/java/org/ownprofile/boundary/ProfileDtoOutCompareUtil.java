package org.ownprofile.boundary;

import java.net.URI;
import java.util.Map;

import org.junit.Assert;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProfileDtoOutCompareUtil {


//	public static void assertContentIsEqual(ProfileEntity expected, ProfileNewDTO actual) {
//		Assert.assertNotNull(expected);
//		Assert.assertNotNull(actual);
//		
//		Assert.assertEquals(expected.getProfileName(), actual.profileName);
//
//		assertContentIsEqual(expected.getBody(), actual.body);
//	}

	public static void assertContentIsEqual(ProfileEntity expected, ProfileHeaderDTO actual, OwnerUriBuilder uriBuilder) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		Assert.assertEquals(expected.getId().get(), actual.id);
		Assert.assertEquals(expected.getProfileName(), actual.profileName);
		
		final URI expectedHref = uriBuilder.resolveOwnerProfileURI(expected.getId().get());
		final URI actualHref = actual.href;
		Assert.assertEquals(expectedHref, actualHref);
		
		// TODO: String sourceLocation
	}
	
	public static void assertContentIsEqual(ProfileEntity expected, ProfileDTO actual, OwnerUriBuilder uriBuilder) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		assertContentIsEqual(expected, actual.header, uriBuilder);		
		assertContentIsEqual(expected.getBody(), actual.body);
	}
	
	public static void assertContentIsEqual(ProfileBody expected, Map<String, Object> actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		try {
			final String actualBodyAsJson = JsonTestUtil.mapper.writeValueAsString(actual);
			Assert.assertEquals(expected.getValueAsJson(), actualBodyAsJson);
		} catch (JsonProcessingException ex) {
			throw new RuntimeException(ex);
		}		
	}

}
