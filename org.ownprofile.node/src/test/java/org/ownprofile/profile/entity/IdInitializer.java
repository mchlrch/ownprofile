package org.ownprofile.profile.entity;

import java.lang.reflect.Field;

public class IdInitializer<T> {

	final IdSource idSource = new IdSource();
	private final Field idField;

	public IdInitializer(Class<? extends T> entityClass) {
		try {
			this.idField = entityClass.getDeclaredField("id");
			this.idField.setAccessible(true);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public Long initIdIfNull(T entity) {
		try {
			Long id = (Long) idField.get(entity);
			if (id == null) {
				id = this.idSource.nextId();
				this.idField.set(entity, id);
			}
			return id;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	
	public static class IdSource {
		
		private long id = 1L;
		
		public long nextId() {
			return this.id++;
		}

	}

}
