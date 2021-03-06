package org.ownprofile.setup;

import org.ownprofile.boundary.DemoProfileFactory;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactService;
import org.ownprofile.boundary.owner.MyProfileService;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ContactRepository;
import org.ownprofile.profile.entity.ContactRepositoryJPA;
import org.ownprofile.profile.entity.MyProfileRepository;
import org.ownprofile.profile.entity.MyProfileRepositoryJPA;

import com.google.inject.AbstractModule;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletScopes;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GuiceFilter.class);
		bindPersistFilter();

		bindContactRepository();
		bindProfileRepository();
		
		bind(ContactService.class).in(ServletScopes.REQUEST);
		bind(MyProfileService.class).in(ServletScopes.REQUEST);
		bind(AddressbookDomainService.class).in(ServletScopes.REQUEST);
		bind(ProfileDomainService.class).in(ServletScopes.REQUEST);
		
		bind(DemoProfileFactory.class).in(ServletScopes.REQUEST);

		bind(ContactConverter.class);
		bind(ProfileConverter.class);
		bind(com.fasterxml.jackson.databind.ObjectMapper.class);
		
		bind(UriBuilders.class).in(ServletScopes.REQUEST);
		
		bind(org.ownprofile.boundary.owner.resources.ContactsTemplate.class);
		bind(org.ownprofile.boundary.owner.resources.MyProfilesTemplate.class);
		
		bind(org.ownprofile.boundary.common.PageHeaderExt.class);
		bind(org.ownprofile.boundary.common.HtmlExt.class);
		bind(org.ownprofile.boundary.common.DomainExt.class);

		// TODO: http://code.google.com/p/google-guice/wiki/JPA -> comment about
		// bindIntercepter() to autoclear jpa session
	}
	
	protected void bindPersistFilter() {
		bind(PersistFilter.class);
	}

	protected void bindContactRepository() {
		bind(ContactRepository.class).to(ContactRepositoryJPA.class).in(ServletScopes.REQUEST);		
	}
	
	protected void bindProfileRepository() {
		bind(MyProfileRepository.class).to(MyProfileRepositoryJPA.class).in(ServletScopes.REQUEST);		
	}
}
