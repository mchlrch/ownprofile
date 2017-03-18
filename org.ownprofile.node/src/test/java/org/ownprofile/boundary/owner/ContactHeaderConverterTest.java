package org.ownprofile.boundary.owner;

import org.junit.Test;
import org.ownprofile.boundary.owner.resources.TestContactEntity;
import org.ownprofile.profile.entity.ContactEntity;

public class ContactHeaderConverterTest {

	private final ContactHeaderConverter converter = new ContactHeaderConverter();
	private final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromDummyBase();

	@Test
	public void shouldConvertEntity2HeaderDto() throws Exception {
		final ContactEntity entity = new TestContactEntity(42L, new ContactEntity.Builder()
				.withPetname("kottan+"));
		final ContactHeaderDTO target = this.converter.convertToHeaderView(entity, this.uriBuilder);
		ContactDtoOutCompareUtil.assertContentIsEqual(entity, target, uriBuilder);
	}

}
