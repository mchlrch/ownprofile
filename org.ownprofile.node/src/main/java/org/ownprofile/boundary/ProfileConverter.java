package org.ownprofile.boundary;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.ownprofile.boundary.owner.ContactHeaderConverter;
import org.ownprofile.boundary.owner.ContactHeaderDTO;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
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
	private final ContactHeaderConverter contactHeaderConverter;	
	
	@Inject
	public ProfileConverter(ContactHeaderConverter contactHeaderConverter, ObjectMapper jsonMapper) {
		this.contactHeaderConverter = contactHeaderConverter;
		this.jsonMapper = jsonMapper;
	}
	
	public ProfileHeaderDTO convertMyProfileToHeaderView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final URI href = uriBuilder.resolveMyProfileURI(in.getId().get());
		final ProfileHeaderDTO out = ProfileHeaderDTO.createMyProfile(in.getId().get(), in.getHandle().get(), href, in.getProfileName());
		return out;
	}
	
	public ProfileHeaderDTO convertContactProfileToHeaderView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final ContactEntity contact = in.getContact().get();
		final URI href = uriBuilder.resolveContactProfileURI(contact.getId().get(), in.getId().get());
		final ContactHeaderDTO container = contactHeaderConverter.convertToHeaderView(contact, uriBuilder);
		final org.ownprofile.boundary.ProfileSource source = in.getSource().isRemote() ? org.ownprofile.boundary.ProfileSource.Remote : org.ownprofile.boundary.ProfileSource.Local;
		final ProfileHeaderDTO out = ProfileHeaderDTO.createContactProfile(source, in.getHandle().get(), href, in.getProfileName(), container);
		return out;
	}
			
	public ProfileDTO convertMyProfileToView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final ProfileHeaderDTO header = convertMyProfileToHeaderView(in, uriBuilder);
		return convertToView(in, header);
	}
	
	public ProfileDTO convertContactProfileToView(ProfileEntity in, OwnerUriBuilder uriBuilder) {
		final ProfileHeaderDTO header = convertContactProfileToHeaderView(in, uriBuilder);
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
	public ProfileEntity createEntityForMyProfile(ProfileNewDTO in) {
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
