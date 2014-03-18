package org.ownprofile.profile.entity;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class ProfileRepository {
	
	@Inject Provider<EntityManager> em;
	
	@Transactional
	public List<ProfileEntity> getAllOwnerProfiles() {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NULL");
		final List queryResult = q.getResultList();
		return (List<ProfileEntity>) queryResult;
	}
	
	@Transactional
	public Optional<ProfileEntity> getOwnerProfileById(long id) {
		final Query q = this.em.get().createQuery("SELECT p FROM " + ProfileEntity.class.getName() + " AS p WHERE p.contact IS NULL AND p.id = " + id);
		final List queryResult = q.getResultList();
		if (queryResult.isEmpty()) {
			return Optional.empty();
		} else if (queryResult.size() == 1) {
			return Optional.of( (ProfileEntity) queryResult.get(0));
		} else {
			throw new IllegalStateException();
		}
	}
	
	@Transactional
	public void addProfile(ProfileEntity profile) {
		em.get().persist(profile);
	}

}
