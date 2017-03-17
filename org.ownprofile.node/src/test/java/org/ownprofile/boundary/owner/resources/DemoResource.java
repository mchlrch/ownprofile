package org.ownprofile.boundary.owner.resources;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.ownprofile.boundary.DemoProfileFactory;
import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactConverter;
import org.ownprofile.boundary.owner.ContactNewDTO;
import org.ownprofile.boundary.owner.OwnerUriBuilder;
import org.ownprofile.profile.control.AddressbookDomainService;
import org.ownprofile.profile.control.ProfileDomainService;
import org.ownprofile.profile.entity.ContactEntity;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileHandle;
import org.ownprofile.profile.entity.ProfileSource;

import com.google.common.collect.Multimap;

@Path("/demo")
public class DemoResource {

	@Inject
	private DemoProfileFactory demoProfileFactory;

	@Inject
	private ProfileDomainService profileService;

	@Inject
	private AddressbookDomainService addressbookService;

	@Inject
	private ProfileConverter profileConverter;

	@Inject
	private ContactConverter contactConverter;

	private void addNewMyProfile(ProfileNewDTO profile) {
		final ProfileEntity newProfile = this.profileConverter.createEntityForMyProfile(profile);
		this.profileService.addNewMyProfile(newProfile);
	}

	@POST
	@Path("/init-myprofiles")
	public Response initDemoProfiles(@Context UriInfo uriInfo) {
		for (ProfileNewDTO p : this.demoProfileFactory.createMyProfiles()) {
			this.addNewMyProfile(p);
		}

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		URI uri = uriBuilder.getMyProfileURI();
		return Response.seeOther(uri).build();
	}
	
	@POST
	@Path("/init-addressbook")
	public Response initAddressbook(@Context UriInfo uriInfo) {
		final Multimap<ContactNewDTO, ProfileNewDTO> contactProfiles = this.demoProfileFactory.createContactProfiles(); 
		
		for (ContactNewDTO contact : contactProfiles.keySet()) {
			final ContactEntity newContact = this.contactConverter.createEntity(contact);		
			this.addressbookService.addNewContact(newContact);
			
			for (ProfileNewDTO p : contactProfiles.get(contact)) {
				final ProfileSource profileSource = ProfileSource.createLocalSource();
				final ProfileHandle handle = ProfileHandle.createRandomHandle();
				
				final ProfileEntity newContactProfile = this.profileConverter.createEntityForContactProfile(newContact, p, handle, profileSource);
				this.addressbookService.addNewContactProfile(newContactProfile);
			}
		}

		final OwnerUriBuilder uriBuilder = OwnerUriBuilder.fromUriInfo(uriInfo);
		URI uri = uriBuilder.getContactsURI();
		return Response.seeOther(uri).build();
	}


	@GET
	@Path("/test")
	public String test(@Context HttpHeaders httpHeaders, @Context Request request, @Context UriInfo uriInfo, @Context SecurityContext securityContext,
			@HeaderParam("X-header") String xHeader) {

		final URI requestUri = uriInfo.getRequestUri();
		return requestUri.toString();
	}

}
