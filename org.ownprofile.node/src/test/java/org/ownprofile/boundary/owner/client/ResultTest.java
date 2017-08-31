package org.ownprofile.boundary.owner.client;

import org.junit.Assert;
import org.junit.Test;

public class ResultTest {
	
	@Test
	public void shouldWrapSuccess() {
		String successValue = "foo";
		
		Result<String> result = Result.success(successValue);
		
		Assert.assertTrue(result.isSuccess());
		Assert.assertSame(successValue, result.getSuccessValue());
	}
	
	@Test
	public void shouldWrapSuccessNullValue() {
		Result<String> result = Result.success(null);
		
		Assert.assertTrue(result.isSuccess());
		Assert.assertNull(result.getSuccessValue());
	}
	
	@Test
	public void shouldWrapSuccessVoidValue() {
		Result<Void> result = Result.success();
		
		Assert.assertTrue(result.isSuccess());
		Assert.assertNull(result.getSuccessValue());
	}
	
	@Test(expected=IllegalStateException.class)
	public void failsWithIllegalStateExceptionOnAttemptToGetSuccessFromFail() {
		Result.fail("oops").getSuccessValue();
	}

	@Test(expected=IllegalStateException.class)
	public void failsWithIllegalStateExceptionOnAttemptToGetFailFromSuccess() {
		Result.success().getFail();
	}
	
	@Test
	public void shouldWrapFailMessage() {
		String failMessage = "oops";
		
		Result<?> result = Result.fail(failMessage);
		
		Assert.assertTrue(result.isFail());
		Assert.assertNull(result.getFail().getCause());
		Assert.assertEquals(failMessage, result.getFail().getMessage());
	}
	
	@Test
	public void shouldWrapFailMessageWithCause() {
		String failMessage = "oops";
		Throwable cause = new RuntimeException("technical failue in subystem ABCD");
		
		Result<?> result = Result.fail(cause, failMessage);
		
		Assert.assertTrue(result.isFail());
		Assert.assertEquals(cause, result.getFail().getCause());
		Assert.assertEquals(failMessage, result.getFail().getMessage());
	}
	
	@Test
	public void shouldWrapFailMessageFormat() {
		Result<?> result = Result.fail("%s %s", "foo", "bar");
		
		Assert.assertTrue(result.isFail());
		Assert.assertNull(result.getFail().getCause());
		Assert.assertEquals("foo bar", result.getFail().getMessage());
	}
	
	@Test
	public void shouldWrapFailMessageFormatWithCause() {
		Throwable cause = new RuntimeException("technical failue in subystem ABCD");

		Result<?> result = Result.fail(cause, "%s %s", "foo", "bar");
		
		Assert.assertTrue(result.isFail());
		Assert.assertEquals(cause, result.getFail().getCause());
		Assert.assertEquals("foo bar", result.getFail().getMessage());
	}

}
