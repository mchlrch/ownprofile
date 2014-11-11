package org.ownprofile.profile.entity;

public class IdSource {
	
	private long id = 1L;
	
	public long nextId() {
		return this.id++;
	}

}
