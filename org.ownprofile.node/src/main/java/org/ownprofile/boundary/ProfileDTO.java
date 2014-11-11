package org.ownprofile.boundary;

import static org.ownprofile.boundary.ProfileDTO.P_BODY;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_HANDLE;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_HREF;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_ID;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_PROFILENAME;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_SOURCE;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_TYPE;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonPropertyOrder({ P_TYPE, P_SOURCE, P_ID, P_HANDLE, P_HREF, P_PROFILENAME, P_BODY })
public class ProfileDTO {

	public static final String P_BODY = "body";

	@JsonUnwrapped
	public final ProfileHeaderDTO header;

	// TODO: /owner - String sourceLocation
	
	@JsonProperty(P_BODY)
	public final Map<String, Object> body;

	@SuppressWarnings("unused")
	private ProfileDTO() {
		header = null;  // Jackson deserialization will overwrite 'header' and 'body' fields even when its final
		body = null;
	}
	
	public ProfileDTO(ProfileHeaderDTO header, Map<String, Object> body) {
		this.header = header;

		if (body != null) {
			this.body = Collections.unmodifiableMap(body);
		} else {
			this.body = Collections.emptyMap();
		}
	}

	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if ( ! (other instanceof ProfileDTO)) {
        	return false;        	
        } 

        final ProfileDTO that = (ProfileDTO) other;
		if ( ! that.canEqual(this)) {
			return false;
		}

		return this.header.equals(that.header);
	}
	
	public boolean canEqual(Object other) {
		return other instanceof ProfileDTO;
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
