package com.kemallette.ListBoost.Util;


import java.util.BitSet;

import android.os.Parcel;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;


public class ParcelUtil{


	/*********************************************************************
	 * Writes
	 **********************************************************************/

	/**
	 * TODO: write docs for this
	 * 
	 * 
	 * @param parcel
	 * @param mArray
	 * @return
	 */
	public static Parcel
		writeLongSparseArray(final Parcel parcel,
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
		writeNestedLongSparseArray(	final Parcel parcel,
									final LongSparseArray<LongSparseArray<Integer>> mArray){

		final long[] keys = ListDataUtil.keys(mArray);

		parcel.writeLongArray(keys);

		for (final long l : keys)
			writeLongSparseArray(	parcel,
									mArray.get(l));

		return parcel;
	}


	public static Parcel
		writeSparseArrayBitSet(Parcel parcel,
								SparseArrayCompat<BitSet> mArray){

		final int N = mArray != null ? mArray.size() : 0;
		parcel.writeInt(N);
		for (int i = 0; i < N; i++){
			parcel.writeInt(mArray.keyAt(i));
			parcel.writeSerializable(mArray.valueAt(i));
		}

		return parcel;
	}


	/*********************************************************************
	 * Reads
	 **********************************************************************/

	/**
	 * TODO: Write docs - remember that storing N (array's size) in the Parcel
	 * argument is mandatory! Using {@link writeLongSparseArray(Parcel,
	 * LongSparseArray<Integer>)} will do it for you
	 * 
	 * @param parcel
	 * @return
	 */
	public static LongSparseArray<Integer>
		readLongSparseArray(final Parcel parcel){

		final LongSparseArray<Integer> mArray = new LongSparseArray<Integer>();

		// Nastiness for reading LongSparseArray (this is how AbsListView
		// handles it)
		final int N = parcel.readInt();

		if (N > 0)
			for (int i = 0; i < N; i++){
				final long key = parcel.readLong();
				final int value = parcel.readInt();
				mArray.put(	key,
							value);
			}

		return mArray;
	}


	public static LongSparseArray<LongSparseArray<Integer>>
		readNestedLongSparseArray(final Parcel parcel){

		final LongSparseArray<LongSparseArray<Integer>> mArray = new LongSparseArray<LongSparseArray<Integer>>();

		long[] keys;
		keys = parcel.createLongArray();

		for (final long l : keys)
			mArray.put(	l,
						readLongSparseArray(parcel));

		return mArray;
	}


	public static SparseArrayCompat<BitSet>
		readSparseArrayBitSet(Parcel parcel){

		SparseArrayCompat<BitSet> mArray = new SparseArrayCompat<BitSet>();

		final int N = parcel.readInt();

		if (N > 0)
			for (int i = 0; i < N; i++){
				final int key = parcel.readInt();
				final BitSet value = (BitSet) parcel.readSerializable();
				mArray.put(	key,
							value);
			}
		return mArray;
	}
}
