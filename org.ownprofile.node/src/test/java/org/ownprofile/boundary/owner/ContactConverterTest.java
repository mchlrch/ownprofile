package org.ownprofile.boundary.owner;

import org.junit.Assert;
import org.junit.Test;
import org.ownprofile.boundary.JsonTestUtil;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.owner.resources.TestContactEntity;
import org.ownprofile.boundary.owner.resources.TestProfileEntity;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileSource;

public class ContactConverterTest {

	private final ContactConverter converter = new ContactConverter(new ProfileConverter(JsonTestUtil.mapper));
	private final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromDummyBase();

	@Test
	public void shouldCreateEntityFromDto() throws Exception {
		final ContactNewDTO dto = new ContactNewDTO("kottan");

		final ContactEntity target = converter.createEntity(dto);
		
		Assert.assertNotNull(target);
		Assert.assertEquals(dto.petname, target.getPetname());
	}

	@Test
	public void shouldConvertEntity2HeaderDto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, "kottan+");
		final ContactHeaderDTO target = this.converter.convertToHeaderView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}
	
	@Test
	public void shouldConvertEntity2Dto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, "kottan+");
		new TestProfileEntity(27L, entity, ProfileSource.createLocalSource(), "test2");

		final ContactDTO target = this.converter.convertToView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}
	
	@Test
	public void shouldConvertEntity2AggregateDto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, "kottan+");
		new TestProfileEntity(27L, entity, ProfileSource.createLocalSource(), "test2");

		final ContactAggregateDTO target = this.converter.convertToAggregateView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}

}
