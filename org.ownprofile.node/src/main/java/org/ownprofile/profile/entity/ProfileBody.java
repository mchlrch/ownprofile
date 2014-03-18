package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class ProfileBody {

	@Lob
	private String bodyAsJson;
	
	public static ProfileBody createEmptyBody() {
		return createBody("{}");
	}
			
	public static ProfileBody createBody(String bodyAsJson) {
		checkNotNull(bodyAsJson, "bodyAsJson is null");
		
		final ProfileBody result = new ProfileBody();
		result.bodyAsJson = bodyAsJson;
		return result;
	}
	
	public String getValueAsJson() {
		return this.bodyAsJson;
	}
	
	@Override
	public String toString() {
		return this.bodyAsJson;
	}
}
