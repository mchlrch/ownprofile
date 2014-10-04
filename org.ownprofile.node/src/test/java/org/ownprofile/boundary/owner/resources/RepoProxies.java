package org.ownprofile.boundary.owner.resources;

import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileRepository;
import org.ownprofile.setup.GuiceModule;

/**
 * Use proxy repositories in IntegrationTests to be able to replace/clear its
 * delegate for each testcase
 */
public class RepoProxies {
	private final ContactRepositoryProxy contactRepoProxy = new ContactRepositoryProxy();
	private final ProfileRepositoryProxy profileRepoProxy = new ProfileRepositoryProxy();

	public void clearDelegates() {
		this.contactRepoProxy.delegate = null;
		this.profileRepoProxy.delegate = null;
	}
	
	public void setContactRepository(ContactRepository contactRepo) {
		this.contactRepoProxy.delegate = contactRepo;
	}

	public void setProfileRepository(ProfileRepository profileRepo) {
		this.profileRepoProxy.delegate = profileRepo;
	}

	public GuiceModule createCustomModule() {
		return new GuiceModule() {
			@Override
			protected void bindPersistFilter() {
				// no persistence in ServiceIntegrationTests
			}

			@Override
			protected void bindContactRepository() {
				bind(ContactRepository.class).toInstance(contactRepoProxy);
			};

			@Override
			protected void bindProfileRepository() {
				bind(ProfileRepository.class).toInstance(profileRepoProxy);
			}
		};
	}

}