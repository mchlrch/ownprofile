package org.ownprofile.boundary;

import static org.ownprofile.boundary.ProfileDTO.P_BODY;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_PROFILENAME;

import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ P_PROFILENAME, P_BODY })
public class ProfileCreateAndUpdateDTO {

	@JsonProperty(P_PROFILENAME)
	public final String profileName;

	@JsonProperty(P_BODY)
	public final Map<String, Object> body;

	public ProfileCreateAndUpdateDTO(@JsonProperty(P_PROFILENAME) String profileName, @JsonProperty(P_BODY) Map<String, Object> body) {
		this.profileName = profileName;

		if (body != null) {
			this.body = Collections.unmodifiableMap(body);
		} else {
			this.body = Collections.emptyMap();
		}
	}

	@Override
	public String toString() {
		return this.profileName;
	}

}
