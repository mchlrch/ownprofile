package org.ownprofile.profile.entity;

import java.util.UUID;

import javax.persistence.Embeddable;

/**
 * ProfileId IS (maybe) resolvable
 */
@Embeddable
public class ProfileId {
	
	private String id;
	
	public static ProfileId createRandomId() {
		final ProfileId result = new ProfileId();
		result.id = UUID.randomUUID().toString();
		return result;
	}
	
	public String value() {
		return id;
	}
	
	@Override
	public String toString() {
		return id;
	}

}
