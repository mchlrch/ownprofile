package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Provider;

public class MyProfileRepositoryJPA implements MyProfileRepository {
	
	@Inject Provider<EntityManager> em;
	
	public List<ProfileEntity> getMyProfiles() {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NULL");
		final List<?> queryResult = q.getResultList();
		return (List<ProfileEntity>) queryResult;
	}
	
	public Optional<ProfileEntity> getMyProfileById(long id) {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NULL AND p.id = " + id);
		return extractSingleResult(q);
	}
	
	
	public Optional<ProfileEntity> getMyProfileByHandle(ProfileHandle handle) {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NULL AND p.handle = " + handle);
		return extractSingleResult(q);
	}
	
	private Optional<ProfileEntity> extractSingleResult(Query q) {
		final List<?> queryResult = q.getResultList();
		if (queryResult.isEmpty()) {
			return Optional.empty();
		} else if (queryResult.size() == 1) {
			return Optional.of( (ProfileEntity) queryResult.get(0));
		} else {
			throw new IllegalStateException();
		}
	}
	
	public void addMyProfile(ProfileEntity profile) {
		em.get().persist(profile);
	}

}
