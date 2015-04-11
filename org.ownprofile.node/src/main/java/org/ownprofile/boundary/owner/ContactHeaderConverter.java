package org.ownprofile.boundary.owner;

import java.net.URI;

import org.ownprofile.profile.entity.ContactEntity;

public class ContactHeaderConverter {
	
	public ContactHeaderDTO convertToHeaderView(ContactEntity in, OwnerUriBuilder uriBuilder) {
		final URI href = uriBuilder.resolveContactURI(in.getId().get());
		final ContactHeaderDTO out = new ContactHeaderDTO(in.getId().get(), href, in.getPetname());		
		return out;
	}
	
}
