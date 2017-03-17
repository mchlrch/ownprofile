package org.ownprofile.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ownprofile.boundary.owner.ContactNewDTO;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class DemoProfileFactory {

	public Iterable<ProfileNewDTO> createMyProfiles() {
		final List<ProfileNewDTO> result = new ArrayList<ProfileNewDTO>();
		result.add(createHomersProfile());
		return result;
	}
	
	public Multimap<ContactNewDTO, ProfileNewDTO> createContactProfiles() {
		final Multimap<ContactNewDTO, ProfileNewDTO> result = ArrayListMultimap.create();
		
		final ContactNewDTO moesContact = createMoesContact();
		final ProfileNewDTO moesProfile = createMoesProfile();
		result.put(moesContact, moesProfile);
		
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
	
	private ContactNewDTO createMoesContact() {
		final ContactNewDTO result = new ContactNewDTO("moe");
		return result;
	}
	
	private ProfileNewDTO createMoesProfile() {
		final Map<String, Object> body = new HashMap<String, Object>();

		body.put("firstName", "Moe");
		body.put("company", "Moes Tavern");
		
		final ProfileNewDTO result = new ProfileNewDTO("professional", body);
		return result;
	}

}
