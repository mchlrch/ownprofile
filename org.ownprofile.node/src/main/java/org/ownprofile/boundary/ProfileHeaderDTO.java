package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_HREF;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_ID;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_PROFILENAME;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({P_ID, P_HREF, P_PROFILENAME})
public class ProfileHeaderDTO {

	public static final String P_ID = "id";
	public static final String P_HREF = "href";
	public static final String P_PROFILENAME = "profileName";

	@JsonProperty(P_ID)
	public final Long id;
	@JsonProperty(P_HREF)
	public final URI href;	
	@JsonProperty(P_PROFILENAME)
	public final String profileName;

	public ProfileHeaderDTO(@JsonProperty(P_ID) Long id, @JsonProperty(P_HREF) URI href, @JsonProperty(P_PROFILENAME) String profileName) {		
		this.id = checkNotNull(id);
		this.href = checkNotNull(href);
		this.profileName = profileName;
	}

	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if ( ! (other instanceof ProfileHeaderDTO)) {
        	return false;        	
        } 

        final ProfileHeaderDTO that = (ProfileHeaderDTO) other;
		if ( ! that.canEqual(this)) {
			return false;
		}

		return this.id.equals(that.id) && this.href.equals(that.href);
	}
	
	public boolean canEqual(Object other) {
		return other instanceof ProfileHeaderDTO;
	}

	@Override
	public int hashCode() {
		int hash = this.id.hashCode();
		hash = 31*hash + this.href.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return String.format("ProfileHeader: %s [%s]", this.profileName, this.href);
	}

}
