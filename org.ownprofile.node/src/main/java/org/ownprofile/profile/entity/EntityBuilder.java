package org.ownprofile.profile.entity;

public abstract class EntityBuilder<E> {

	public E build() {
		final E entity = create();
		return entity;
	}
	
	protected abstract E create();

}
