package org.ownprofile.boundary.owner.client;

import java.net.URI;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.ownprofile.boundary.BoundaryConstants;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.owner.ContactAggregateDTO;
import org.ownprofile.boundary.owner.ContactDTO;
import org.ownprofile.boundary.owner.ContactNewDTO;

// TODO: http://cxf.apache.org/docs/jax-rs-client-api.html
// TODO: http://www.theotherian.com/2013/08/jersey-client-2.0-httpclient-timeouts-max-connections.html
public class OwnerClient {
	
	private final Client client;
	private final String urlOfAddressbookResource;
	private final String urlOfProfileResource;
	
	public OwnerClient(String host, int port) {
		this.client = ClientBuilder.newClient();
		this.urlOfAddressbookResource = String.format("http://%s:%d%s%s", host, port, BoundaryConstants.API_BASE_PATH, BoundaryConstants.RESOURCEPATH_OWNER_ADDRESSBOOK);
		this.urlOfProfileResource = String.format("http://%s:%d%s%s", host, port, BoundaryConstants.API_BASE_PATH, BoundaryConstants.RESOURCEPATH_OWNER_PROFILES); 
	}

	public List<ContactDTO> getContacts() {
		final WebTarget webTarget = this.client.target(urlOfAddressbookResource);
		
		final GenericType<List<ContactDTO>> contactListType = new GenericType<List<ContactDTO>>() {};
		final List<ContactDTO> result = webTarget.request().get(contactListType);
		
		return result;		
	}
	
	public ContactAggregateDTO getContactById(long id) {
		final String url = String.format("%s/contact/%d", urlOfAddressbookResource, id);
		final WebTarget webTarget = this.client.target(url);
		final ContactAggregateDTO result = webTarget.request().get(ContactAggregateDTO.class);
		return result;
	}
	
	public URI addNewContact(ContactNewDTO contact) {
		final String url = String.format("%s/contact", urlOfAddressbookResource);
		final WebTarget webTarget = this.client.target(url);		
		
		final Response response = webTarget.request().post(Entity.json(contact));
		return response.getLocation();		
	}

	public URI addNewContactProfile(long contactId, ProfileNewDTO newProfile) {
		final String url = String.format("%s/contact/%d/profile", urlOfAddressbookResource, contactId);
		final WebTarget webTarget = this.client.target(url);		
		
		final Response response = webTarget.request().post(Entity.json(newProfile));
		return response.getLocation();		
	}
	
	// -------------------------------
	
	public List<ProfileDTO> getOwnerProfiles() {
		final WebTarget webTarget = this.client.target(urlOfProfileResource);
		
		final GenericType<List<ProfileDTO>> profileListType = new GenericType<List<ProfileDTO>>() {};
		final List<ProfileDTO> result = webTarget.request().get(profileListType);
		
		return result;		
	}

	public ProfileDTO getOwnerProfileById(long id) {
		final String url = String.format("%s/%d", urlOfProfileResource, id);
		final WebTarget webTarget = this.client.target(url);
		final ProfileDTO result = webTarget.request().get(ProfileDTO.class);
		return result;
	}
	
	public URI addNewOwnerProfile(ProfileNewDTO newProfile) {
		final WebTarget webTarget = this.client.target(urlOfProfileResource);		
		final Response response = webTarget.request().post(Entity.json(newProfile));
		return response.getLocation();
	}
	
	public ProfileDTO getContactProfileById(long id) {
		final String url = String.format("%s/contact/0/profile/%d", urlOfAddressbookResource, id);
		final WebTarget webTarget = this.client.target(url);
		final ProfileDTO result = webTarget.request().get(ProfileDTO.class);
		return result;
	}
	
	/// ========================================================
	public static void main(String[] args) {
		final OwnerClient client = new OwnerClient("localhost", 9080);
		
		for (ContactDTO contact : client.getContacts()) {
			System.out.println(contact);
		}
		
//		for (Profile p : client.getProfiles()) {
//			System.out.println(p);
//		}
	}

}
