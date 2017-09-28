package org.ownprofile.boundary;

import java.net.URI;
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
		final long profileId = in.getId().get();
		final URI href = uriBuilder.resolveContactProfileURI(profileId);
		final ContactHeaderDTO container = contactHeaderConverter.convertToHeaderView(contact, uriBuilder);
		final org.ownprofile.boundary.ProfileSource source = in.getSource().isRemote() ? org.ownprofile.boundary.ProfileSource.Remote : org.ownprofile.boundary.ProfileSource.Local;
		final ProfileHeaderDTO out = ProfileHeaderDTO.createContactProfile(profileId, source, in.getHandle().get(), href, in.getProfileName(), container);
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
		final String bodyAsJson = in.getBody().getValueAsJson();
		final Map<String, Object> body = json2map(bodyAsJson);
		final ProfileDTO out = new ProfileDTO(header, body);
		out.bodyAsJson = bodyAsJson;
		return out;
	}

	// TODO: make sure, this method ist ONLY used for creating new profiles !!!
	public ProfileEntity createEntityForMyProfile(ProfileCreateAndUpdateDTO in) {
		final ProfileBody body = serializeBodyToJSON(in.body);
		final ProfileSource src = ProfileSource.createLocalSource();
		final ProfileEntity out = ProfileEntity.createMyProfile(src, in.profileName, body);
		return out;
	}
	
	// TODO: make sure, this method ist ONLY used for creating new profiles !!!
	public ProfileEntity createEntityForContactProfile(ContactEntity contact, ProfileCreateAndUpdateDTO in, ProfileHandle handle, ProfileSource src) {
		final ProfileBody body = serializeBodyToJSON(in.body);
		final ProfileEntity out = ProfileEntity.createContactProfile(contact, handle, src, in.profileName, body);
		return out;
	}
	
	public ProfileEntity.Struct dto2struct(ProfileCreateAndUpdateDTO in) {
		final ProfileEntity.Struct out = new ProfileEntity.Struct();
		out.withName(in.profileName);
		out.withBody(serializeBodyToJSON(in.body));
		return out;
	}
	
	private ProfileBody serializeBodyToJSON(Map<String, Object> body) {
		final String bodyAsJson = map2json(body);
		return ProfileBody.createBody(bodyAsJson);
	}
	
	public Map<String, Object> json2map(String json) {
		try {
			final Map<String, Object> result = jsonMapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
			return result;

		} catch (Exception ex) {
			// TODO:
			throw new RuntimeException(ex);
		}
	}

	public String map2json(Map<String, Object> map) {
		try {
			final String result = jsonMapper.writeValueAsString(map);
			return result;

		} catch (Exception ex) {
			// TODO:
			throw new RuntimeException(ex);
		}
	}
	
}
