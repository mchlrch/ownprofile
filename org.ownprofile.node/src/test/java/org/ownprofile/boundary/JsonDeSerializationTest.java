package org.ownprofile.boundary;

import org.junit.Test;

public class JsonDeSerializationTest {
	
	@Test
	public void shouldDeSerializeProfileHeaderDTO() throws Exception {
		/* {"id": 0,
          	"href": "http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0",
          	"profileName": "privat" }
		 */
		final String expected = "{\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0\","
				+ "\"profileName\":\"privat\"}";
		
		JsonTestUtil.assertDeSerializationRoundtrip(ProfileHeaderDTO.class, expected);
	}
	
	@Test
	public void shouldDeSerializeProfileNewDTO() throws Exception {
		/* {"profileName": "privat",
		  	"body":{"firstname":"adolf","lastname":"kottan"}
		   }
		 */
		final String expected = "{\"profileName\":\"privat\","
				+ "\"body\":{\"firstname\":\"adolf\",\"lastname\":\"kottan\"}}";
		
		JsonTestUtil.assertDeSerializationRoundtrip(ProfileNewDTO.class, expected);
	}
	
	@Test
	public void shouldDeSerializeProfileDTO() throws Exception {
		/* {"body":{"firstname":"adolf","lastname":"kottan"},
		  	"id": 0,
          	"href": "http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0",
          	"profileName": "privat"
		   }
		 */
		final String expected = "{\"body\":{\"firstname\":\"adolf\",\"lastname\":\"kottan\"},"
				+ "\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0\","
				+ "\"profileName\":\"privat\"}";
		
		JsonTestUtil.assertDeSerializationRoundtrip(ProfileDTO.class, expected);
	}

}
