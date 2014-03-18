package org.ownprofile.profile.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import javax.persistence.Embeddable;

@Embeddable
public class ProfileSource {

	private boolean remote;
	private String remoteLocationUri;

	public static ProfileSource createRemoteSource(String remoteLocationUri) {
		checkNotNull(remoteLocationUri, "remoteLocationUri is null");

		final ProfileSource src = new ProfileSource();
		src.remote = true;
		src.remoteLocationUri = remoteLocationUri;
		return src;
	}

	public static ProfileSource createLocalSource() {
		final ProfileSource src = new ProfileSource();
		src.remote = false;
		return src;
	}

	public boolean isRemote() {
		return this.remote;
	}

	public boolean isLocal() {
		return !this.remote;
	}

	public Optional<String> getRemoteLocationUri() {
		return Optional.ofNullable(this.remoteLocationUri);
	}

	@Override
	public String toString() {
		return String.format("ProfileSource: %s", this.remote ? "remote - " + this.remoteLocationUri : "local");
	}
}
