package com.kemallette.ListBoost.Util;


import java.util.Arrays;
import java.util.BitSet;

import android.support.v4.util.LongSparseArray;
import android.util.Log;


public class ListDataUtil{

	private static final String	TAG	= "ListDataUtil";


	public static <T> long[]
		keys(final LongSparseArray<T> longSparseArray){

		if (longSparseArray == null){
			Log.e(	TAG,
					"LongSparseArray lsArray was null!");
			return new long[0];
		}

		final LongSparseArray<T> array = longSparseArray;
		final int count = array.size();
		final long[] keys = new long[count];

		for (int i = 0; i < count; i++){
			keys[i] = array.keyAt(i);
		}
		return keys;
	}


	public static <T> T[] concatAll(final T[] first, final T[]... rest){

		int totalLength = first.length;
		for (final T[] array : rest){
			totalLength += array.length;
		}
		final T[] result = Arrays.copyOf(	first,
											totalLength);
		int offset = first.length;
		for (final T[] array : rest){
			System.arraycopy(	array,
								0,
								result,
								offset,
								array.length);
			offset += array.length;
		}
		return result;
	}


	public static int[] truePositions(final BitSet mBS){

		final int[] positions = new int[mBS.length()];
		for (int i = 0; i < positions.length; i++){
			if (mBS.get(i))
				positions[i] = i;
		}
		return positions;
	}


	public static int[] values(final LongSparseArray<Integer> array){

		if (array == null){
			Log.e(	TAG,
					"LongSparseArray array was null!");
			return new int[0];
		}

		final int[] positions = new int[array.size()];
		for (int i = 0; i < positions.length; i++){
			positions[i] = array.get(i);
		}
		return null;

	}
}
