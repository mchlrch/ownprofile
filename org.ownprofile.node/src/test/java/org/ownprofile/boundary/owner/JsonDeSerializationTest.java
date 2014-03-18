package org.ownprofile.boundary.owner;

import org.junit.Test;
import org.ownprofile.boundary.JsonTestUtil;

public class JsonDeSerializationTest {
	
	@Test
	public void shouldDeSerializeContactNewDTO() throws Exception {
		// {"petname":"kottan"}
		final String expected = "{\"petname\":\"kottan\"}";
		
		JsonTestUtil.assertDeSerializationRoundtrip(ContactNewDTO.class, expected);
	}
	
	@Test
	public void shouldDeSerializeContactHeaderDTO() throws Exception {
		/* {"id": 0,
    		"href": "http://localhost:9080/webapi/owner/addressbook/contact/0",
    		"petname": "kottan" }
		 */
		final String expected = "{\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0\","
				+ "\"petname\":\"kottan\"}";

		JsonTestUtil.assertDeSerializationRoundtrip(ContactHeaderDTO.class, expected);
	}
	
	@Test
	public void shouldDeSerializeContactDTO() throws Exception {
		/* { "profiles": [
	            { "id": 0,
	              "href": "http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0",
	              "profileName": "privat"
	            }
	        ],
	        "id": 0,
	        "href": "http://localhost:9080/webapi/owner/addressbook/contact/0",
	        "petname": "kottan"  }
	    */
		final String expected = "{\"profiles\":[{\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0\","
				+ "\"profileName\":\"privat\"}],\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0\","
				+ "\"petname\":\"kottan\"}";
		
		JsonTestUtil.assertDeSerializationRoundtrip(ContactDTO.class, expected);
	}
	
	@Test
	public void shouldDeSerializeContactAggregateDTO() throws Exception {
		/* { "profiles": [
				{"body":{"firstname":"adolf","lastname":"kottan"},
				 "id": 0,
				 "href": "http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0",
				 "profileName": "privat"
				}        
		    ],
		    "id": 0,
		    "href": "http://localhost:9080/webapi/owner/addressbook/contact/0",
		    "petname": "kottan"  }
		 */
		final String expected = "{\"profiles\":[{\"body\":{\"firstname\":\"adolf\",\"lastname\":\"kottan\"},"
				+ "\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0/profile/0\","
				+ "\"profileName\":\"privat\"}],\"id\":0,"
				+ "\"href\":\"http://localhost:9080/webapi/owner/addressbook/contact/0\","
				+ "\"petname\":\"kottan\"}";

		JsonTestUtil.assertDeSerializationRoundtrip(ContactAggregateDTO.class, expected);
	}
	
}
