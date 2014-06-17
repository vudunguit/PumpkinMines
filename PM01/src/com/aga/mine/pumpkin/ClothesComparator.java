package com.aga.mine.pumpkin;

import java.util.Comparator;

public class ClothesComparator implements Comparator {
	public int compare(Object array1, Object array2) {
		Integer clothes1 = ((GameScore)array1).score;
		Integer clothes2 = ((GameScore)array2).score;
		return clothes2.compareTo(clothes1);
	}
} // End class.