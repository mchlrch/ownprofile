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
public class ContactRepositoryIT {

	private static Injector injector;
	private static ContactRepository repo;

	@BeforeClass
	public static void before() {
		injector = Guice.createInjector(IntegrationTestSession.defaultJpaModule, new GuiceModule() {
			@Override
			protected void configure() {
				bind(ContactRepository.class);
			}
		});

		repo = injector.getInstance(ContactRepository.class);
		injector.getInstance(PersistService.class).start();
	}

	@AfterClass
	public static void after() {
		injector.getInstance(PersistService.class).stop();
	}
	
	// =============================================================

	@Test
	public void shouldQueryForAllContacts() {
		final List<ContactEntity> contacts = repo.getAllContacts();
		Assert.assertNotNull(contacts);
	}

	@Test
	public void shouldQueryForContactById() {
		repo.getContactById(1L);
	}
	
	@Test
	public void shouldAddContact() {
		final ContactEntity contactEntity = new ContactEntity("kottan");
		repo.addContact(contactEntity);
	}
	
	@Test
	public void shouldQueryForContactProfileById() {
		repo.getContactProfileById(1L);
	}

}
