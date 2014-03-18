package org.ownprofile.setup;

import org.ownprofile.boundary.DemoProfileFactory;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ProfileRepository;

import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletScopes;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GuiceFilter.class);
		bind(PersistFilter.class);

		bindContactRepository();
		bindProfileRepository();
		
		bind(AddressbookDomainService.class).in(ServletScopes.REQUEST);
		bind(ProfileDomainService.class).in(ServletScopes.REQUEST);
		
		bind(DemoProfileFactory.class).in(ServletScopes.REQUEST);

		bind(ContactConverter.class);
		bind(ProfileConverter.class);
		bind(com.fasterxml.jackson.databind.ObjectMapper.class);

		// TODO: http://code.google.com/p/google-guice/wiki/JPA -> comment about
		// bindIntercepter() to autoclear jpa session
	}

	protected void bindContactRepository() {
		bind(ContactRepository.class).in(ServletScopes.REQUEST);		
	}
	
	protected void bindProfileRepository() {
		bind(ProfileRepository.class).in(ServletScopes.REQUEST);		
	}
}
