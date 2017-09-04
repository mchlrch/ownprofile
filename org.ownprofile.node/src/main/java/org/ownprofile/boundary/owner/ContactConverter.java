package org.ownprofile.boundary.owner;

import java.util.ArrayList;
import java.util.List;

import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;

import com.google.inject.Inject;

public class ContactConverter {
	
	private final ContactHeaderConverter contactHeaderConverter;	
	private final ProfileConverter profileConverter;
	
	@Inject
	public ContactConverter(ContactHeaderConverter contactHeaderConverter, ProfileConverter profileConverter) {
		this.contactHeaderConverter = contactHeaderConverter;
		this.profileConverter = profileConverter;
	}
	
	public ContactDTO convertToView(ContactEntity in, OwnerUriBuilder uriBuilder) {
		final List<ProfileHeaderDTO> profileHeaders = new ArrayList<ProfileHeaderDTO>();
		for (ProfileEntity p : in.getProfiles()) {
			final ProfileHeaderDTO header = this.profileConverter.convertContactProfileToHeaderView(p, uriBuilder);
			profileHeaders.add(header);
		}
		
		final ContactHeaderDTO header = contactHeaderConverter.convertToHeaderView(in, uriBuilder);
		final ContactDTO out = new ContactDTO(header, profileHeaders);
		
		return out;
	}
	
	public ContactAggregateDTO convertToAggregateView(ContactEntity in, OwnerUriBuilder uriBuilder) {
		final List<ProfileDTO> profiles = new ArrayList<ProfileDTO>();
		for (ProfileEntity p : in.getProfiles()) {
			final ProfileDTO profile = this.profileConverter.convertContactProfileToView(p, uriBuilder);
			profiles.add(profile);
		}
		
		final ContactHeaderDTO header = contactHeaderConverter.convertToHeaderView(in, uriBuilder);;
		final ContactAggregateDTO out = new ContactAggregateDTO(header, profiles);
		
		return out;
	}

	public ContactEntity createEntity(ContactCreateAndUpdateDTO in) {
		final ContactEntity out = new ContactEntity.Builder().fromDto(in).build();
		return out;
	}
	
}
