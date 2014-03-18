package org.ownprofile.boundary.owner;

import static org.ownprofile.boundary.owner.ContactDTO.P_PROFILES;
import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_HREF;
import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_ID;
import static org.ownprofile.boundary.owner.ContactHeaderDTO.P_PETNAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ownprofile.boundary.ProfileDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonPropertyOrder({P_ID, P_HREF, P_PETNAME, P_PROFILES})
public class ContactAggregateDTO {

	@JsonUnwrapped
	public final ContactHeaderDTO header;
	
	@JsonProperty(P_PROFILES)
	private final List<ProfileDTO> profiles = new ArrayList<ProfileDTO>();	

	@SuppressWarnings("unused")
	private ContactAggregateDTO() { 
		header = null; 	// Jackson deserialization will overwrite 'header' field even when its final
	}
	
	public ContactAggregateDTO(ContactHeaderDTO header, List<ProfileDTO> profiles) {
		this.header = header;
		this.profiles.addAll(profiles);
	}
	

	@JsonProperty(P_PROFILES)
	public List<ProfileDTO> getProfiles() {
		return Collections.unmodifiableList(this.profiles);
	}
	
	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if ( ! (other instanceof ContactAggregateDTO)) {
        	return false;        	
        } 
        
		final ContactAggregateDTO that = (ContactAggregateDTO) other;
		if ( ! that.canEqual(this)) {
			return false;
		}
		
		return this.header.equals(that.header);
	}
	
	public boolean canEqual(Object other) {
		return other instanceof ContactAggregateDTO;
	}

	@Override
	public int hashCode() {
			return this.header.hashCode();
	}

	@Override
	public String toString() {
		return this.header.toString();
	}

}
