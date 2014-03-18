package org.ownprofile.boundary.owner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileHeaderDTO;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;

import com.google.inject.Inject;

public class ContactConverter {
	
	private final ProfileConverter profileConverter;
	
	@Inject
	public ContactConverter(ProfileConverter profileConverter) {
		this.profileConverter = profileConverter;
	}
	
	public ContactDTO convertToView(ContactEntity in, UriBuilderCallback uriBuilderCallback) {
		final List<ProfileHeaderDTO> profileHeaders = new ArrayList<ProfileHeaderDTO>();
		for (ProfileEntity p : in.getProfiles()) {
			final ProfileHeaderDTO header = this.profileConverter.convertToHeaderView(p, uriBuilderCallback);
			profileHeaders.add(header);
		}
		
		final ContactHeaderDTO header = this.convertToHeaderView(in, uriBuilderCallback);;
		final ContactDTO out = new ContactDTO(header, profileHeaders);
		
		return out;
	}
	
	public ContactAggregateDTO convertToAggregateView(ContactEntity in, UriBuilderCallback uriBuilderCallback) {
		final List<ProfileDTO> profiles = new ArrayList<ProfileDTO>();
		for (ProfileEntity p : in.getProfiles()) {
			final ProfileDTO profile = this.profileConverter.convertToView(p, uriBuilderCallback);
			profiles.add(profile);
		}
		
		final ContactHeaderDTO header = this.convertToHeaderView(in, uriBuilderCallback);;
		final ContactAggregateDTO out = new ContactAggregateDTO(header, profiles);
		
		return out;
	}
	
	public ContactHeaderDTO convertToHeaderView(ContactEntity in, UriBuilderCallback uriBuilderCallback) {
		final URI href = uriBuilderCallback.buildContactUri(in);
		final ContactHeaderDTO out = new ContactHeaderDTO(in.getId().get(), href, in.getPetname());		
		return out;
	}

	public ContactEntity createEntity(ContactNewDTO in) {
		final ContactEntity out = new ContactEntity(in.petname);
		return out;
	}
	
	// ------------------------------------------------
	public static interface UriBuilderCallback extends ProfileConverter.UriBuilderCallback {
		public URI buildContactUri(ContactEntity contact);
	}

}
