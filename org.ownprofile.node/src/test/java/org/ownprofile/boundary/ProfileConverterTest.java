package org.ownprofile.boundary;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;
import org.ownprofile.profile.entity.TestProfileEntity;

public class ProfileConverterTest {

	private final ProfileConverter converter = new ProfileConverter(JsonTestUtil.mapper);
	private final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromDummyBase();

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
		final ProfileEntity entity = TestProfileEntity.createOwnProfile(42L, ProfileSource.createLocalSource(), "professional");

		final ProfileHeaderDTO target = this.converter.convertToHeaderView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertContentIsEqual(entity, target, this.uriBuilder);
	}
	
	@Test
	public void shouldConvertEntity2Dto() throws Exception {
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"adolf\",\"lastName\":\"kottan\"}");
		final ProfileEntity entity = TestProfileEntity.createOwnProfile(42L, ProfileSource.createLocalSource(), "professional", body);

		final ProfileDTO target = this.converter.convertToView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertContentIsEqualOnOwnerAPI(entity, target, this.uriBuilder);
	}

}
