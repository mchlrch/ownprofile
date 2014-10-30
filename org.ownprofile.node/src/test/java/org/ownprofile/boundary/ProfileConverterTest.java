package org.ownprofile.boundary;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.boundary.owner.resources.TestProfileEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

public class ProfileConverterTest {

	private final ProfileConverter converter = new ProfileConverter(JsonTestUtil.mapper);
	private final OwnerUriBuilder uriBuilderCallback = OwnerUriBuilder.fromDummyBase();

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
		ProfileDtoOutCompareUtil.assertContentIsEqual(entity, target, this.uriBuilderCallback);
	}
	
	@Test
	public void shouldConvertEntity2Dto() throws Exception {
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"adolf\",\"lastName\":\"kottan\"}");
		final ProfileEntity entity = new TestProfileEntity(42L, ProfileSource.createLocalSource(), "professional", body);

		final ProfileDTO target = this.converter.convertToView(entity, this.uriBuilderCallback);
		ProfileDtoOutCompareUtil.assertContentIsEqual(entity, target, this.uriBuilderCallback);
	}

}
