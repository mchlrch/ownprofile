package org.ownprofile.boundary.owner.client;

public class Result<T> {

	private boolean isSuccess;
	private T successValue;
	private Fail fail;

	public static Result<Void> success() {
		return success(null);
	}

	public static <S> Result<S> success(S successValue) {
		return new Result<S>(successValue);
	}

	public static <S> Result<S> fail(String message) {
		return fail((Throwable)null, "%s", message);
	}

	public static <S> Result<S> fail(Throwable cause, String message) {
		return fail(cause, "%s", message);
	}
	
	public static <S> Result<S> fail(String format, Object... args) {
		return fail(null, format, args);
	}
	
	public static <S> Result<S> fail(Throwable cause, String format, Object... args) {
		final Fail f = new Fail(cause, format, args);
		return new Result<S>(f);
	}

	private Result(T successValue) {
		this.isSuccess = true;
		this.successValue = successValue;
	}

	private Result(Fail fail) {
		this.isSuccess = false;
		this.fail = fail;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public boolean isFail() {
		return !isSuccess;
	}

	public T getSuccessValue() {
		if (isSuccess) {
			return successValue;
		} else {
			throw new IllegalStateException(String.format("Result is Fail: %s", fail));
		}
	}

	public Fail getFail() {
		if (isSuccess) {
			throw new IllegalStateException(String.format("Result is Success"));
		} else {
			return fail;
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s",
				isSuccess ? "SUCCESS" : "FAIL",
				isSuccess ? successValue : fail);
	}

	// ------------------------------
	public static class Fail {
		private String message;
		private Throwable cause;

		private Fail(Throwable cause, String message) {
			this.cause = cause;
			this.message = message;
		}

		private Fail(Throwable cause, String format, Object... args) {
			this(cause, String.format(format, args));
		}

		public String getMessage() {
			return message;
		}

		public Throwable getCause() {
			return cause;
		}

		@Override
		public String toString() {
			return String.format("%s - %s", message, cause);
		}
	}
}
