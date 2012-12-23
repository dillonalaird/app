package com.android.alcoholpriceapp.util;

public class Util {

	/**
	 * This function takes the String format of size and turns it into an integer
	 * 
	 * @param size in String format
	 * @return integer format of the size
	 */
	public static int convertSize(String size) {
		if (size.equals("Single"))
			return 1;
		else if (size.equals("40 oz"))
			return 2;
		else if (size.equals("6 pack"))
			return 3;
		else if (size.equals("12 pack"))
			return 4;
		else if (size.equals("18 pack"))
			return 5;
		else if (size.equals("24 pack"))
			return 6;
		else if (size.equals("16 oz (pint)"))
			return 7;
		else if (size.equals("750mL (fifth)"))
			return 8;
		else // if (size.equals("1.5L (half gallon)"))
			return 9;
	}
	
	/**
	 * This function takes the integer format of size and turns it into a String
	 * @param size in integer format
	 * @return String format of size
	 */
	public static String convertSize(int size) {
		switch(size) {
			case 1:
				return "Single";
			case 2:
				return "40 oz";
			case 3:
				return "6 pack";
			case 4:
				return "12 pack";
			case 5:
				return "18 pack";
			case 6:
				return "24 pack";
			case 7:
				return "16 oz (pint)";
			case 8:
				return "750mL (fifth)";
			case 9:
				return "1.5L (half gallon)";
			default:
				return null;
		}
	}
}
