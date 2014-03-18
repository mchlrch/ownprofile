package org.ownprofile.boundary.owner;

import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_HREF;
import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_ID;
import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_PETNAME;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({P_ID, P_HREF, P_PETNAME})
public class ContactHeaderDTO {

	public static final String P_ID = "id";
	public static final String P_HREF = "href";
	public static final String P_PETNAME = "petname";

	@JsonProperty(P_ID)
	public final Long id;

	@JsonProperty(P_HREF)
	public final URI href;

	@JsonProperty(P_PETNAME)
	public final String petname;

	
	public ContactHeaderDTO(@JsonProperty(P_ID) Long id, @JsonProperty(P_HREF) URI href, @JsonProperty(P_PETNAME) String petname) {
		this.id = id;
		this.href = href;
		this.petname = petname;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof ContactHeaderDTO)) {
			return false;
		}

		final ContactHeaderDTO that = (ContactHeaderDTO) other;
		if (!that.canEqual(this)) {
			return false;
		}

		return this.href != null && this.href.equals(that.href);
	}

	public boolean canEqual(Object other) {
		return other instanceof ContactHeaderDTO;
	}

	@Override
	public int hashCode() {
		if (this.href != null) {
			return this.href.hashCode();
		} else {
			return super.hashCode();
		}
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", this.petname, this.href);
	}

}
