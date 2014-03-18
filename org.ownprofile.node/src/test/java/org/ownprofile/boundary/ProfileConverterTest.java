package org.ownprofile.boundary;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.owner.resources.TestProfileEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

public class ProfileConverterTest {

	private final ProfileConverter converter = new ProfileConverter(JsonTestUtil.mapper);
	private final MyUriBuilderCallback uriBuilderCallback = new MyUriBuilderCallback();

	@Test
	public void shouldCreateEntityFromDto() throws Exception {
		final Map<String, Object> body = new HashMap<String, Object>();
		body.put("firstName", "adolf");
		body.put("lastName", "kottan");
		final ProfileNewDTO dto = new ProfileNewDTO("private", body);

		final ProfileEntity target = converter.createEntityForOwnerProfile(dto);
		
		Assert.assertNotNull(target);		
		Assert.assertEquals(dto.profileName, target.getProfileName());
		
		final String expectedBodyAsJSON = JsonTestUtil.mapper.writeValueAsString(body);
		Assert.assertEquals(expectedBodyAsJSON, target.getBody().getValueAsJson());
	}

	@Test
	public void shouldConvertEntity2HeaderDto() throws Exception {
		final ProfileEntity entity = new TestProfileEntity(42L, ProfileSource.createLocalSource(), "professional");

		final ProfileHeaderDTO target = this.converter.convertToHeaderView(entity, this.uriBuilderCallback);
		ProfileDtoOutCompareUtil.assertContentIsEqual(entity, target);
	}
	
	@Test
	public void shouldConvertEntity2Dto() throws Exception {
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"adolf\",\"lastName\":\"kottan\"}");
		final ProfileEntity entity = new TestProfileEntity(42L, ProfileSource.createLocalSource(), "professional", body);

		final ProfileDTO target = this.converter.convertToView(entity, this.uriBuilderCallback);
		ProfileDtoOutCompareUtil.assertContentIsEqual(entity, target);
	}
	
	// --------------------------------------------------
	private static class MyUriBuilderCallback implements ProfileConverter.UriBuilderCallback {
		private static final String profileUriPattern = "http://foobar.org/profile/%d";
				
		@Override
		public URI buildProfileUri(ProfileEntity profile) {
			return buildUri(profileUriPattern, profile.getId().get());
		}
		
		private URI buildUri(String pattern, Long id) {
			try {
				return new URI(String.format(pattern, id));
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}
	};

}
