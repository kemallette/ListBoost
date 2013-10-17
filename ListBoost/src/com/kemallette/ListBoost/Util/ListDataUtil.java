package com.kemallette.ListBoost.Util;


import java.util.Arrays;
import java.util.BitSet;

import android.os.Parcel;
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


	/**
	 * TODO: write docs for this
	 * 
	 * 
	 * @param parcel
	 * @param mArray
	 * @return
	 */
	public static Parcel
		writeToParcel(final Parcel parcel,
						final LongSparseArray<Integer> mArray){

		// Nastiness for writing LongSparseArray (this is how AbsListView
		// handles it)
		final int N = mArray != null ? mArray.size() : 0;
		parcel.writeInt(N);
		for (int i = 0; i < N; i++){
			parcel.writeLong(mArray.keyAt(i));
			parcel.writeInt(mArray.valueAt(i));
		}
		return parcel;
	}


	public static Parcel
		writeNestedArrayToParcel(	final Parcel parcel,
									final LongSparseArray<LongSparseArray<Integer>> mArray){

		final long[] keys = keys(mArray);

		parcel.writeLongArray(keys);

		for (final long l : keys){
			writeToParcel(	parcel,
							mArray.get(l));
		}

		return parcel;
	}


	/**
	 * TODO: Write docs - remember that storing N (array's size) in the Parcel
	 * argument is mandatory! Using {@link writeLongSparseArray(Parcel,
	 * LongSparseArray<Integer>)} will do it for you
	 * 
	 * @param parcel
	 * @return
	 */
	public static LongSparseArray<Integer>
		readFromParcel(final Parcel parcel){

		final LongSparseArray<Integer> mArray = new LongSparseArray<Integer>();

		// Nastiness for reading LongSparseArray (this is how AbsListView
		// handles it)
		final int N = parcel.readInt();

		if (N > 0){
			for (int i = 0; i < N; i++){
				final long key = parcel.readLong();
				final int value = parcel.readInt();
				mArray.put(	key,
							value);
			}
		}

		return mArray;
	}


	public static LongSparseArray<LongSparseArray<Integer>>
		readNestedArrayFromParcel(final Parcel parcel){

		final LongSparseArray<LongSparseArray<Integer>> mArray = new LongSparseArray<LongSparseArray<Integer>>();

		long[] keys;
		keys = parcel.createLongArray();

		for (final long l : keys){
			mArray.put(	l,
						readFromParcel(parcel));
		}

		return mArray;
	}

}
