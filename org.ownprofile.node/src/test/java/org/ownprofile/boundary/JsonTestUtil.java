package org.ownprofile.boundary;

import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTestUtil {
	
	public static final ObjectMapper mapper = new ObjectMapper();

	public static <T> void assertDeSerializationRoundtrip(Class<T> dtoClass, String expectedJSON) throws Exception {
		final T dto = mapper.readValue(expectedJSON, dtoClass);
		final String actualJSON = mapper.writeValueAsString(dto);
		
		Assert.assertEquals(expectedJSON, actualJSON);
	}
	
}
