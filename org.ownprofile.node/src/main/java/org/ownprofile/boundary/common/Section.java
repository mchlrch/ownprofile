package org.ownprofile.boundary.common;

import java.net.URI;

import org.ownprofile.boundary.owner.OwnerUriBuilder;

public enum Section {

	Contacts("contacts"), MyProfiles("my profiles");

	public final String title;

	private Section(String title) {
		this.title = title;
	}

	public URI getLocation(OwnerUriBuilder uriBuilder) {
		switch (this) {
		case Contacts:
			return uriBuilder.getContactsURI();
		case MyProfiles:
			// TODO: rename to getMyProfilesURI() ...
			return uriBuilder.getOwnerProfileURI();
		default:
			throw new IllegalStateException();
		}
	}

}
