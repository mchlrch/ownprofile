package org.ownprofile.boundary;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;

public class ProfileConverter {

	private final ObjectMapper jsonMapper;

	@Inject
	public ProfileConverter(ObjectMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	public ProfileHeaderDTO convertToHeaderView(ProfileEntity in, UriBuilderCallback uriBuilderCallback) {
		final URI href = uriBuilderCallback.buildProfileUri(in);
		final ProfileHeaderDTO out = new ProfileHeaderDTO(in.getId().get(), href, in.getProfileName());
		return out;
	}
	
	public ProfileDTO convertToView(ProfileEntity in, UriBuilderCallback uriBuilderCallback) {
		final ProfileHeaderDTO header = convertToHeaderView(in, uriBuilderCallback);
		
		Map<String, Object> body = Collections.emptyMap();
		try {
			body = jsonMapper.readValue(in.getBody().getValueAsJson(), new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			// TODO:
			throw new RuntimeException(ex);
		}
		final ProfileDTO out = new ProfileDTO(header, body);
		return out;
	}

	// TODO: make sure, this method ist ONLY used for creating new profiles !!!
	public ProfileEntity createEntityForOwnerProfile(ProfileNewDTO in) {
		final ProfileBody body = serializeBodyToJSON(in.body);
		final ProfileSource src = ProfileSource.createLocalSource();
		final ProfileEntity out = new ProfileEntity(src, in.profileName, body);
		return out;
	}
	
	// TODO: make sure, this method ist ONLY used for creating new profiles !!!
	public ProfileEntity createEntityForContactProfile(ContactEntity contact, ProfileNewDTO in, ProfileSource src) {
		final ProfileBody body = serializeBodyToJSON(in.body);
		final ProfileEntity out = new ProfileEntity(contact, src, in.profileName, body);
		return out;
	}
	
	private ProfileBody serializeBodyToJSON(Map<String, Object> body) {
		try {
			final String bodyAsJson = jsonMapper.writeValueAsString(body);
			return ProfileBody.createBody(bodyAsJson);
			
		} catch (Exception ex) {
			// TODO:
			throw new RuntimeException(ex);
		}		
	}
	
	// ------------------------------------------------
	public static interface UriBuilderCallback {
		public URI buildProfileUri(ProfileEntity profile);
	}

}
