package org.ownprofile.boundary.owner;

import static org.ownprofile.testutil.JsonTestUtil.assertDeSerializationRoundtrip;
import static org.ownprofile.testutil.TestdataReader.readJsonForThisMethod;

import org.junit.Test;

public class JsonDeSerializationTest {

	@Test
	public void shouldDeSerializeContactNewDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ContactNewDTO.class, expected);
	}

	@Test
	public void shouldDeSerializeContactHeaderDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ContactHeaderDTO.class, expected);
	}

	@Test
	public void shouldDeSerializeContactDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ContactDTO.class, expected);
	}

	@Test
	public void shouldDeSerializeContactAggregateDTO() throws Exception {
		final String expected = readJsonForThisMethod();
		assertDeSerializationRoundtrip(ContactAggregateDTO.class, expected);
	}

}
