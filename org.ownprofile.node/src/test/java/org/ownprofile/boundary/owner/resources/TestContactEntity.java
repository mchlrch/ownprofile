package org.ownprofile.boundary.owner.resources;

import org.ownprofile.profile.entity.ContactEntity;

public class TestContactEntity extends ContactEntity {

	public TestContactEntity(Long id, ContactEntity.Builder b) {
		super(b);
		this.id = id;
	}
}