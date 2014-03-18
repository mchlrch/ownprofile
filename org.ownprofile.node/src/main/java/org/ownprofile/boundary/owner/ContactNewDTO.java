package org.ownprofile.boundary.owner;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContactNewDTO {

	@JsonProperty(ContactHeaderDTO.P_PETNAME)
	public final String petname;

	public ContactNewDTO(@JsonProperty(ContactHeaderDTO.P_PETNAME) String petname) {
		this.petname = petname;
	}

	@Override
	public String toString() {
		return this.petname;
	}
}
