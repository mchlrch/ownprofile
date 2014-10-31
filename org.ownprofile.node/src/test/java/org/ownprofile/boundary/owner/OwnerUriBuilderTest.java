package org.ownprofile.boundary.owner;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class OwnerUriBuilderTest {

	private final OwnerUriBuilder builder;
	private final Method method;
	private final Object[] params;

	public OwnerUriBuilderTest(Method m, Object[] params) {
		this.method = m;
		this.params = params;
		this.builder = OwnerUriBuilder.fromDummyBase();
	}

	@Test
	public void shouldInvokeBuilderMethod() throws Exception {
		URI location = (URI) method.invoke(builder, params);
		Assert.assertNotNull(String.format("%s returned null", method), location);
	}

	@Parameterized.Parameters
	public static List<Object[]> data() {
		final List<Object[]> data = new ArrayList<Object[]>();

		for (Method m : OwnerUriBuilder.class.getMethods()) {
			if (URI.class.equals(m.getReturnType())) {
				if (m.getParameterTypes().length == 0) {
					data.add(new Object[] { m, new Object[] {} });

				} else if (Arrays.equals(new Class[] { Long.class }, m.getParameterTypes())) {
					data.add(new Object[] { m, new Object[] { 1L } });

				} else if (Arrays.equals(new Class[] { Long.class, Long.class }, m.getParameterTypes())) {
					data.add(new Object[] { m, new Object[] { 1L, 1L } });
					
				} else {
					throw new IllegalStateException();
				}
				
			} else if (m.isDefault()) {
				throw new IllegalStateException();
				
			} else {
				// ignore static methods
			}
		}
		return data;
	}
}
