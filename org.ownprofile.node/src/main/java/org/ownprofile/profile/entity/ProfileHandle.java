package org.ownprofile.profile.entity;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Embeddable;

@Embeddable
public class ProfileHandle {
	
	// TODO: handles are not globally unique. only unique within a node if they were created locally
	// @Column(unique=true)
	private String handle;
	
	public static ProfileHandle createRandomHandle() {
		final UUID uuid = UUID.randomUUID();
		
		final ByteBuffer buffer = ByteBuffer.allocate(2*Long.BYTES);
	    buffer.putLong(uuid.getMostSignificantBits());
	    buffer.putLong(uuid.getLeastSignificantBits());
	    
	    final String base64handle = Base64.getUrlEncoder().encodeToString(buffer.array());
	    
		final ProfileHandle result = new ProfileHandle();
		result.handle = base64handle;
		return result;
	}
	
	public static ProfileHandle fromString(String value) {
		final ProfileHandle result = new ProfileHandle();
		result.handle = value;
		return result;
	}
	
	public String asString() {
		return handle;
	}
	
	@Override
	public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if ( ! (other instanceof ProfileHandle)) {
        	return false;        	
        } 

        final ProfileHandle that = (ProfileHandle) other;
		if ( ! that.canEqual(this)) {
			return false;
		}
		
		return Objects.equals(this.handle, that.handle);
	}
	
	public boolean canEqual(Object other) {
		return other instanceof ProfileHandle;
	}
	
	@Override
	public int hashCode() {
		return handle.hashCode();
	}
	
	@Override
	public String toString() {
		return handle;
	}

}
