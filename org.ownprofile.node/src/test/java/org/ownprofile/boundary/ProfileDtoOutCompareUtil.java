package org.ownprofile.boundary;

import java.net.URI;
import java.util.Map;
import java.util.function.Function;

import org.junit.Assert;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.testutil.JsonTestUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProfileDtoOutCompareUtil {

	public static void assertOwnerProfileContentIsEqual(ProfileEntity expected, ProfileHeaderDTO actual, OwnerUriBuilder uriBuilder) {
		assertContentIsEqual(expected, actual, (profile) -> uriBuilder.resolveOwnerProfileURI(profile.getId().get()));
	}
	
	public static void assertContactProfileContentIsEqual(ProfileEntity expected, ProfileHeaderDTO actual, OwnerUriBuilder uriBuilder) {
		assertContentIsEqual(expected, actual, (profile) -> {
			final long contactId = profile.getContact().get().getId().get();
			final long profileId = profile.getId().get();			
			return uriBuilder.resolveContactProfileURI(contactId, profileId);
		});
	}
	
	public static void assertContentIsEqual(ProfileEntity expected, ProfileHeaderDTO actual, Function<ProfileEntity, URI> uriResolver) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);
		
		ProfileType.dispatch(actual.getType(), new ProfileType.Dispatch() {
			public void typeIsOwner() {
				Assert.assertEquals(expected.getId().get(), actual.id);
			}
			public void typeIsPeer() {
				Assert.assertNull(actual.id);
			}
		});
		
		Assert.assertEquals(expected.getProfileName(), actual.profileName);
		
		final URI expectedHref = uriResolver.apply(expected); 
		final URI actualHref = actual.href;
		Assert.assertEquals(expectedHref, actualHref);
		
		// TODO: String sourceLocation
	}
	
	public static void assertOwnerProfileContentIsEqualOnOwnerAPI(ProfileEntity expected, ProfileDTO actual, OwnerUriBuilder uriBuilder) {
		assertContentIsEqual(expected, actual, (profile) -> uriBuilder.resolveOwnerProfileURI(profile.getId().get()));
	}
	
	public static void assertContactProfileContentIsEqualOnOwnerAPI(ProfileEntity expected, ProfileDTO actual, OwnerUriBuilder uriBuilder) {
		assertContentIsEqual(expected, actual, (profile) -> {
			final long contactId = profile.getContact().get().getId().get();
			final long profileId = profile.getId().get();			
			return uriBuilder.resolveContactProfileURI(contactId, profileId);
		});
	}

	private static void assertContentIsEqual(ProfileEntity expected, ProfileDTO actual, Function<ProfileEntity, URI> uriResolver) {
		Assert.assertNotNull(expected);
		Assert.assertNotNull(actual);

		assertContentIsEqual(expected, actual.header, uriResolver);		
		assertContentIsEqual(expected.getBody(), actual.body);
	}
	
	private static void assertContentIsEqual(ProfileBody expected, Map<String, Object> actual) {
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
