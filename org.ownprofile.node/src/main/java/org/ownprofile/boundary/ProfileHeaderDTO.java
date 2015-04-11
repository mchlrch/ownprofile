package org.ownprofile.boundary;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_CONTAINER;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_HANDLE;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_HREF;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_ID;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_PROFILENAME;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_SOURCE;
import static org.ownprofile.boundary.ProfileHeaderDTO.P_TYPE;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

import org.ownprofile.boundary.owner.ContactHeaderDTO;
import org.ownprofile.profile.entity.ProfileHandle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({P_TYPE, P_SOURCE, P_ID, P_HANDLE, P_HREF, P_PROFILENAME, P_CONTAINER})
@JsonInclude(Include.NON_NULL) 
public class ProfileHeaderDTO {

	public static final String P_TYPE = "type";
	public static final String P_SOURCE = "source";
	public static final String P_ID = "id";
	public static final String P_HANDLE = "handle";
	
	public static final String P_HREF = "href";
	public static final String P_PROFILENAME = "profileName";
	
	public static final String P_CONTAINER = "container";

	@JsonProperty(P_TYPE)
	public final String type;
	@JsonProperty(P_ID)
	public final Long id;
	@JsonProperty(P_HANDLE)
	public final String handle;
	@JsonProperty(P_SOURCE)
	public final String source;
	
	@JsonProperty(P_HREF)
	public final URI href;	
	@JsonProperty(P_PROFILENAME)
	public final String profileName;
	
	@JsonProperty(P_CONTAINER)
	public final ContactHeaderDTO container;
	
	public static ProfileHeaderDTO createOwnerProfile(Long id, ProfileHandle handle, URI href, String profileName) {
		return new ProfileHeaderDTO(
				ProfileType.Owner,
				ProfileSource.Local,
				checkNotNull(id),
				checkNotNull(handle.asString()),
				checkNotNull(href),
				profileName,
				null);
	}
	
	public static ProfileHeaderDTO createOwnerProfilePeerView(ProfileHandle handle, URI href, String profileName) {
		return new ProfileHeaderDTO(
				ProfileType.Peer,
				ProfileSource.Remote,
				null,
				checkNotNull(handle.asString()),
				checkNotNull(href),
				profileName,
				null);
	}
	
	public static ProfileHeaderDTO createContactProfile(ProfileSource source, ProfileHandle handle, URI href, String profileName, ContactHeaderDTO container) {
		return new ProfileHeaderDTO(
				ProfileType.Peer,
				source,
				null,
				checkNotNull(handle.asString()),
				checkNotNull(href),
				profileName,
				checkNotNull(container));
	}

	public ProfileHeaderDTO(
			@JsonProperty(P_TYPE) String typeName,
			@JsonProperty(P_SOURCE) String sourceName,
			@JsonProperty(P_ID) Long id,
			@JsonProperty(P_HANDLE) String handle,
			@JsonProperty(P_HREF) URI href,
			@JsonProperty(P_PROFILENAME) String profileName,
			@JsonProperty(P_CONTAINER) ContactHeaderDTO container) {		
		this.type = ProfileType.valueOf(typeName).name();       // TODO: really an enum? really fixed semantics?
		this.source = ProfileSource.valueOf(sourceName).name(); // TODO: really an enum? really fixed semantics?
		
		this.id = id;
		this.handle = handle;
				
		this.href = href;
		this.profileName = profileName;
		
		this.container = container;
	}
	
	
	private ProfileHeaderDTO(ProfileType type, ProfileSource source, Long id, String handle, URI href, String profileName, ContactHeaderDTO container) {		
		this.type = type.name();
		this.source = source.name();
		this.id = id;
		this.handle = handle;
		
		this.href = href;
		this.profileName = profileName;
		
		this.container = container;
	}
	
	public ProfileType getType() {
		return ProfileType.valueOf(type);
	}
	
	public ProfileSource getSource() {
		return ProfileSource.valueOf(source);
	}
	
	/**
	 * only set, if this is a contactProfile and if this DTO is not already structurally contained in a container
	 */
	@JsonIgnore
	public Optional<ContactHeaderDTO> getContainer() {
		return Optional.ofNullable(container);
	}
	

	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if ( ! (other instanceof ProfileHeaderDTO)) {
        	return false;        	
        } 

        final ProfileHeaderDTO that = (ProfileHeaderDTO) other;
		if ( ! that.canEqual(this)) {
			return false;
		}
		
		final boolean idEq = Objects.equals(this.id, that.id);
		final boolean handleEq = Objects.equals(this.handle, that.handle);
		final boolean hrefEq = Objects.equals(this.href, that.href);
		final boolean containerEq = Objects.equals(this.container, that.container);

		return idEq && handleEq && hrefEq && containerEq;
	}
	
	public boolean canEqual(Object other) {
		return other instanceof ProfileHeaderDTO;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		
		if (id != null) {
			hash = 31*hash + id.hashCode();
		}
		
		if (handle != null) {
			hash = 31*hash + handle.hashCode();
		}
		
		if (href != null) {
			hash = 31*hash + href.hashCode();
		}
		
		if (container != null) {
			hash = 31*hash + container.hashCode();
		}
		
		return hash;
	}

	@Override
	public String toString() {
		return String.format("ProfileHeader: %s [%s]", this.profileName, this.href);
	}

}
