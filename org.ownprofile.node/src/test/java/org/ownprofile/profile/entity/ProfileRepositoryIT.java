package org.ownprofile.profile.entity;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.IntegrationTestSession;
import org.ownprofile.setup.GuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

/**
 * test DB queries
 */
public class ProfileRepositoryIT {

	private static Injector injector;
	private static ProfileRepository repo;

	@BeforeClass
	public static void before() {
		injector = Guice.createInjector(IntegrationTestSession.defaultJpaModule, new GuiceModule() {
			@Override
			protected void configure() {
				bind(ProfileRepository.class);
			}
		});

		repo = injector.getInstance(ProfileRepository.class);
		injector.getInstance(PersistService.class).start();
	}

	@AfterClass
	public static void after() {
		injector.getInstance(PersistService.class).stop();
	}

	// =============================================================

	@Test
	public void shouldQueryForAllOwnerProfiles() {
		final List<ProfileEntity> ownerProfiles = repo.getAllOwnerProfiles();
		Assert.assertNotNull(ownerProfiles);
	}

	@Test
	public void shouldQueryForOwnerProfileById() {
		repo.getOwnerProfileById(1L);
	}
	
	@Test
	public void shouldAddProfile() {
		final ProfileEntity profileEntity = new ProfileEntity(ProfileSource.createLocalSource(), "private", ProfileBody.createEmptyBody());
		repo.addProfile(profileEntity);
	}

}
