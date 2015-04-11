package org.ownprofile.boundary.owner;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.owner.resources.TestContactEntity;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;
import org.ownprofile.profile.entity.TestProfileEntity;
import org.ownprofile.testutil.JsonTestUtil;

public class ContactConverterTest {

	private final ContactConverter converter;
	private final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromDummyBase();

	public ContactConverterTest() {
		final ContactHeaderConverter headerConverter = new ContactHeaderConverter();
		final ProfileConverter profileConverter = new ProfileConverter(headerConverter, JsonTestUtil.mapper);
		this.converter = new ContactConverter(headerConverter, profileConverter);
	}
	
	@Test
	public void shouldCreateEntityFromDto() throws Exception {
		final ContactNewDTO dto = new ContactNewDTO("kottan");

		final ContactEntity target = converter.createEntity(dto);
		
		Assert.assertNotNull(target);
		Assert.assertEquals(dto.petname, target.getPetname());
	}
	
	@Test
	public void shouldConvertEntity2Dto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, "kottan+");
		TestProfileEntity.createContactProfile(27L, entity, ProfileHandle.createRandomHandle(), ProfileSource.createLocalSource(), "test2");

		final ContactDTO target = this.converter.convertToView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}
	
	@Test
	public void shouldConvertEntity2AggregateDto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, "kottan+");
		TestProfileEntity.createContactProfile(27L, entity, ProfileHandle.createRandomHandle(), ProfileSource.createLocalSource(), "test2");

		final ContactAggregateDTO target = this.converter.convertToAggregateView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}

}
