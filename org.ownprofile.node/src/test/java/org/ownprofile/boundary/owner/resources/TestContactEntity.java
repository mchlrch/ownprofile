package org.ownprofile.boundary.owner.resources;

import org.ownprofile.profile.entity.ContactEntity;

public class TestContactEntity extends ContactEntity {

	public TestContactEntity(Long id, String petname) {
		super(petname);
		this.id = id;
	}
}