package org.ownprofile.boundary;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.boundary.peer.PeerUriBuilder;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileBody;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
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
	
	public ProfileHeaderDTO convertToHeaderView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final URI href = uriBuilder.resolveOwnerProfileURI(in.getId().get());
		final ProfileHeaderDTO out = ProfileHeaderDTO.createOwnerProfile(in.getId().get(), in.getHandle().get(), href, in.getProfileName());
		return out;
	}
	
	public ProfileHeaderDTO convertToPeerHeaderView(ProfileEntity in, PeerUriBuilder uriBuilder) {
		final URI href = uriBuilder.resolveProfileURI(in.getHandle().get());
		final ProfileHeaderDTO out = ProfileHeaderDTO.createPeerProfile(in.getHandle().get(), href, in.getProfileName());
		return out;
	}
		
	public ProfileDTO convertToView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final ProfileHeaderDTO header = convertToHeaderView(in, uriBuilder);
		return convertToView(in, header);
	}
	
	public ProfileDTO convertToPeerView(ProfileEntity in, PeerUriBuilder uriBuilder) {
		final ProfileHeaderDTO header = convertToPeerHeaderView(in, uriBuilder);
		return convertToView(in, header);
	}
	
	private ProfileDTO convertToView(ProfileEntity in, ProfileHeaderDTO header) {
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
		final ProfileEntity out = ProfileEntity.createOwnProfile(src, in.profileName, body);
		return out;
	}
	
	// TODO: make sure, this method ist ONLY used for creating new profiles !!!
	public ProfileEntity createEntityForContactProfile(ContactEntity contact, ProfileNewDTO in, ProfileHandle handle, ProfileSource src) {
		final ProfileBody body = serializeBodyToJSON(in.body);
		final ProfileEntity out = ProfileEntity.createContactProfile(contact, handle, src, in.profileName, body);
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
	
}
