package org.brokenarrow.library.menusettings.exceptions;

public class Valid {


	public static void checkNotNull(Object checkNull) {
		if (checkNull == null)
			throw new CatchExceptions("Object is null, when it should not be null");
	}

	public static void checkNotNull(Object checkNull, String s) {
		if (checkNull == null)
			throw new CatchExceptions(s);
	}

	public static void checkBoolean(boolean bolen, String s) {
		if (!bolen)
			throw new CatchExceptions(s);
	}

	public static void exception(Throwable throwabl, String s) {
		throw new CatchExceptions(throwabl, s);
	}

	public static class CatchExceptions extends RuntimeException {
		public CatchExceptions(Throwable throwable, String message) {
			super(message, throwable);
		}

		public CatchExceptions(String message) {
			super(message);
		}
	}

}
