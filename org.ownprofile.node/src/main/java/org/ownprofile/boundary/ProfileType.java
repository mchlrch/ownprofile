package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;

public enum ProfileType {

	Owner, Peer;

	public static void dispatch(ProfileType type, Dispatch dispatch) {
		checkNotNull(type);

		if (type == Owner) {
			dispatch.typeIsOwner();
		} else if (type == Peer) {
			dispatch.typeIsPeer();

		} else {
			throw new IllegalStateException(String.format("Unexpected ProfileType: %s", type));
		}
	}

	public static interface Dispatch {
		public void typeIsOwner();

		public void typeIsPeer();
	}

}
