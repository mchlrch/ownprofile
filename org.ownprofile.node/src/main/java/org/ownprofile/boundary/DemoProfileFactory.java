package org.ownprofile.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoProfileFactory {

	public Iterable<ProfileNewDTO> createDemoProfiles() {
		final List<ProfileNewDTO> result = new ArrayList<ProfileNewDTO>();
		result.add(createHomersProfile());
		return result;
	}

	private ProfileNewDTO createHomersProfile() {
		final Map<String, Object> body = new HashMap<String, Object>();

		body.put("firstName", "Homer");
		body.put("lastName", "Simpson");

		final Map<String, Object> address = new HashMap<String, Object>();
		address.put("street", "742 Evergreen Terrace");
		address.put("city", "Springfield");

		body.put("address", address);
		
		body.put("web", "http://www.thesimpsons.com");

		final ProfileNewDTO result = new ProfileNewDTO("private", body);
		return result;
	}

}
