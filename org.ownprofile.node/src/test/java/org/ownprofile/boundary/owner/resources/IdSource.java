package org.ownprofile.boundary.owner.resources;

public class IdSource {
	
	private long id = 1L;
	
	public long nextId() {
		return this.id++;
	}

}
