package org.ownprofile.boundary;

import java.util.Map;

import org.junit.Assert;
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

	public static void assertContentIsEqual(ProfileEntity expected, ProfileHeaderDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		Assert.assertEquals(expected.getId().get(), actual.id);
		Assert.assertEquals(expected.getProfileName(), actual.profileName);
		Assert.assertNotNull(actual.href);
		// TODO: String sourceLocation
	}
	
	public static void assertContentIsEqual(ProfileEntity expected, ProfileDTO actual) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		assertContentIsEqual(expected, actual.header);		
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
