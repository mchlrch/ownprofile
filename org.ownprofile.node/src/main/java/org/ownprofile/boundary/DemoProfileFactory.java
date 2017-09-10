package org.ownprofile.boundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ownprofile.boundary.owner.ContactCreateAndUpdateDTO;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class DemoProfileFactory {

	public Iterable<ProfileCreateAndUpdateDTO> createMyProfiles() {
		final List<ProfileCreateAndUpdateDTO> result = new ArrayList<ProfileCreateAndUpdateDTO>();
		result.add(createHomersProfile());
		return result;
	}
	
	public Multimap<ContactCreateAndUpdateDTO, ProfileCreateAndUpdateDTO> createContactProfiles() {
		final Multimap<ContactCreateAndUpdateDTO, ProfileCreateAndUpdateDTO> result = ArrayListMultimap.create();
		
		final ContactCreateAndUpdateDTO moesContact = createMoesContact();
		final ProfileCreateAndUpdateDTO moesProfile = createMoesProfile();
		result.put(moesContact, moesProfile);
		
		return result;
	}

	private ProfileCreateAndUpdateDTO createHomersProfile() {
		final Map<String, Object> body = new HashMap<String, Object>();

		body.put("firstName", "Homer");
		body.put("lastName", "Simpson");

		final Map<String, Object> address = new HashMap<String, Object>();
		address.put("street", "742 Evergreen Terrace");
		address.put("city", "Springfield");

		body.put("address", address);
		
		body.put("web", "http://www.thesimpsons.com");

		final ProfileCreateAndUpdateDTO result = new ProfileCreateAndUpdateDTO("private", body);
		return result;
	}
	
	private ContactCreateAndUpdateDTO createMoesContact() {
		final ContactCreateAndUpdateDTO result = new ContactCreateAndUpdateDTO("moe");
		return result;
	}
	
	private ProfileCreateAndUpdateDTO createMoesProfile() {
		final Map<String, Object> body = new HashMap<String, Object>();

		body.put("firstName", "Moe");
		body.put("company", "Moes Tavern");
		
		final ProfileCreateAndUpdateDTO result = new ProfileCreateAndUpdateDTO("professional", body);
		return result;
	}

}
