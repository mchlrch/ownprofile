package org.ownprofile.boundary;

import static org.ownprofile.testutil.JsonTestUtil.assertDeSerializationRoundtrip;
import static org.ownprofile.testutil.TestdataReader.readJsonForThisMethod;

import org.junit.Test;

public class JsonDeSerializationTest {

	@Test
	public void shouldDeSerializeProfileHeaderDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ProfileHeaderDTO.class, expected);
	}

	@Test
	public void shouldDeSerializeProfileNewDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ProfileNewDTO.class, expected);
	}

	@Test
	public void shouldDeSerializeProfileDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ProfileDTO.class, expected);
	}

}
