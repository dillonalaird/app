package com.android.alcoholpriceapp.util;

import java.util.Comparator;

import android.util.Log;

import com.android.alcoholpriceapp.models.PriceInfo;

/**
 * Simple Comparator class that compares two PriceInfo objects distance fields.
 */
public class PriceInfoDistanceComparator implements Comparator<PriceInfo> {

	/**
	 * Returns 1 if arg0.dist > arg1.dist, -1 if arg0.dist < arg1.dist and
	 * 0 otherwise.
	 */
	@Override
	public int compare(PriceInfo arg0, PriceInfo arg1) {
		double compare = arg0.getDist() - arg1.getDist();
		if (compare < 0) return -1;
		if (compare > 0) return 1;
		return 0;
	}

}
