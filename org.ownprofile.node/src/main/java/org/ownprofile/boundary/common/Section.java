package org.ownprofile.boundary.common;

import java.net.URI;

import org.ownprofile.boundary.owner.OwnerUriBuilder;

public enum Section {

	Addressbook("addressbook"), OwnerProfiles("owner profiles");

	public final String title;

	private Section(String title) {
		this.title = title;
	}

	public URI getLocation(OwnerUriBuilder uriBuilder) {
		switch (this) {
		case Addressbook:
			return uriBuilder.getContactURI();
		case OwnerProfiles:
			return uriBuilder.getOwnerProfileURI();
		default:
			throw new IllegalStateException();
		}
	}

}
