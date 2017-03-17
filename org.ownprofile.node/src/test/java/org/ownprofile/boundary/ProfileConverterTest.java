package org.ownprofile.boundary;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.owner.ContactHeaderConverter;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.boundary.owner.resources.TestContactEntity;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;
import org.ownprofile.profile.entity.TestProfileEntity;
import org.ownprofile.testutil.JsonTestUtil;

public class ProfileConverterTest {

	private final ProfileConverter converter = new ProfileConverter(new ContactHeaderConverter(), JsonTestUtil.mapper);
	private final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromDummyBase();

	@Test
	public void shouldCreateMyProfileEntityFromDto() throws Exception {
		final Map<String, Object> body = new HashMap<String, Object>();
		body.put("firstName", "adolf");
		body.put("lastName", "kottan");
		final ProfileNewDTO dto = new ProfileNewDTO("private", body);

		final ProfileEntity target = converter.createEntityForMyProfile(dto);
		
		assertCorrectConversion(dto, target);
	}
	
	@Test
	public void shouldCreateContactProfileEntityFromDto() throws Exception {
		final Map<String, Object> body = new HashMap<String, Object>();
		body.put("firstName", "adolf");
		body.put("lastName", "kottan");
		final ProfileNewDTO dto = new ProfileNewDTO("private", body);

		final TestContactEntity contact = new TestContactEntity(42L, "kottan");
		final ProfileHandle pHandle = ProfileHandle.createRandomHandle();
		final ProfileSource pSource = ProfileSource.createLocalSource();
		
		final ProfileEntity target = converter.createEntityForContactProfile(contact, dto, pHandle, pSource);
		
		assertCorrectConversion(dto, Optional.of(contact), Optional.of(pHandle), Optional.of(pSource), target);
	}

	@Test
	public void shouldConvertMyProfileEntity2HeaderDto() throws Exception {
		final ProfileEntity entity = TestProfileEntity.createOwnProfile(42L, ProfileSource.createLocalSource(), "professional");

		final ProfileHeaderDTO target = this.converter.convertMyProfileToHeaderView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertMyProfileContentIsEqual(entity, target, this.uriBuilder);
	}
	
	@Test
	public void shouldConvertContactProfileEntity2HeaderDto() throws Exception {
		final TestContactEntity contact = new TestContactEntity(42L, "kottan");
		final ProfileHandle pHandle = ProfileHandle.createRandomHandle();
		final ProfileEntity entity = TestProfileEntity.createContactProfile(42L, contact, pHandle, ProfileSource.createLocalSource(), "professional");

		final ProfileHeaderDTO target = this.converter.convertContactProfileToHeaderView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertContactProfileContentIsEqual(entity, target, this.uriBuilder);
	}
	
	@Test
	public void shouldConvertMyProfileEntity2Dto() throws Exception {
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"adolf\",\"lastName\":\"kottan\"}");
		final ProfileEntity entity = TestProfileEntity.createOwnProfile(42L, ProfileSource.createLocalSource(), "professional", body);

		final ProfileDTO target = this.converter.convertMyProfileToView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertMyProfileContentIsEqualOnOwnerAPI(entity, target, this.uriBuilder);
	}
	
	@Test
	public void shouldConvertContactProfileEntity2Dto() throws Exception {
		final TestContactEntity contact = new TestContactEntity(42L, "kottan");
		final ProfileHandle pHandle = ProfileHandle.createRandomHandle();
		final ProfileBody body = ProfileBody.createBody("{\"firstName\":\"adolf\",\"lastName\":\"kottan\"}");
		final ProfileEntity entity = TestProfileEntity.createContactProfile(42L, contact, pHandle, ProfileSource.createLocalSource(), "professional", body);

		final ProfileDTO target = this.converter.convertContactProfileToView(entity, this.uriBuilder);
		ProfileDtoOutCompareUtil.assertContactProfileContentIsEqualOnOwnerAPI(entity, target, this.uriBuilder);
	}
	
	
	private void assertCorrectConversion(ProfileNewDTO dto, ProfileEntity target) throws Exception {
		assertCorrectConversion(dto, Optional.empty(), Optional.empty(), Optional.empty(), target);
	}
	
	private void assertCorrectConversion(ProfileNewDTO dto, Optional<ContactEntity> contact, Optional<ProfileHandle> pHandle, Optional<ProfileSource> pSource, ProfileEntity target) throws Exception {
		Assert.assertNotNull(target);		
		Assert.assertEquals(dto.profileName, target.getProfileName());
		
		final String expectedBodyAsJSON = JsonTestUtil.mapper.writeValueAsString(dto.body);
		Assert.assertEquals(expectedBodyAsJSON, target.getBody().getValueAsJson());
		
		if (contact.isPresent()) {
			Assert.assertEquals(contact, target.getContact());
		}
		
		if (pHandle.isPresent()) {
			Assert.assertEquals(pHandle, target.getHandle());
		}
		
		if (pSource.isPresent()) {
			Assert.assertEquals(pSource.get(), target.getSource());
		}
	}

}
