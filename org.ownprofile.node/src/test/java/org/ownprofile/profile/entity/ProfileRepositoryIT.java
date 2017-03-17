package org.ownprofile.profile.entity;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ownprofile.boundary.SystemTestSession;
import org.ownprofile.setup.GuiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;

/**
 * test DB queries
 */
public class ProfileRepositoryIT {

	private static Injector injector;
	private static MyProfileRepositoryJPA repo;

	@BeforeClass
	public static void before() {
		injector = Guice.createInjector(SystemTestSession.createDefaultJpaModule(), new GuiceModule() {
			@Override
			protected void configure() {
				bind(MyProfileRepositoryJPA.class);
			}
		});

		repo = injector.getInstance(MyProfileRepositoryJPA.class);
		injector.getInstance(PersistService.class).start();
	}

	@AfterClass
	public static void after() {
		injector.getInstance(PersistService.class).stop();
	}

	// =============================================================

	@Test
	public void shouldQueryForAllMyProfiles() {
		final List<ProfileEntity> myProfiles = repo.getMyProfiles();
		Assert.assertNotNull(myProfiles);
	}

	@Test
	public void shouldQueryForMyProfileById() {
		repo.getMyProfileById(1L);
	}
	
	@Test
	public void shouldAddProfile() {
		final ProfileEntity profileEntity = new ProfileEntity(ProfileSource.createLocalSource(), "private", ProfileBody.createEmptyBody());
		repo.addMyProfile(profileEntity);
	}

}
