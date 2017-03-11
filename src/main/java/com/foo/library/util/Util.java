package com.foo.library.util;

import java.util.Date;

public class Util {
	public static boolean isDueDatePassed(Date dueDate, Date returnDate) {
		return returnDate.after(dueDate);
	}
}
