package org.ownprofile.profile.entity;

import org.ownprofile.setup.GuiceModule;

/**
 * Use proxy repositories in IntegrationTests to be able to replace/clear its
 * delegate for each testcase
 */
public class RepoProxies {
	private final ContactRepositoryProxy contactRepoProxy = new ContactRepositoryProxy();
	private final MyProfileRepositoryProxy profileRepoProxy = new MyProfileRepositoryProxy();

	public void clearDelegates() {
		this.contactRepoProxy.delegate = null;
		this.profileRepoProxy.delegate = null;
	}
	
	public void setContactRepository(ContactRepository contactRepo) {
		this.contactRepoProxy.delegate = contactRepo;
	}

	public void setProfileRepository(MyProfileRepository profileRepo) {
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
				bind(MyProfileRepository.class).toInstance(profileRepoProxy);
			}
		};
	}

}