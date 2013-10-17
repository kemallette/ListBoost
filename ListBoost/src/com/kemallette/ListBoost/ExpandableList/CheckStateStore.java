package com.kemallette.ListBoost.ExpandableList;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import android.util.SparseArray;
import android.view.View.BaseSavedState;

import com.kemallette.ListBoost.ExpandableList.BoostExpandableListView.BoostExpandableState;
import com.kemallette.ListBoost.Util.ListDataUtil;


/**
 * This class is used to store {@link BoostExpandableListView} checked states.
 * 
 * @author kemallette
 */
class CheckStateStore	implements
						Parcelable{

	private static final String							TAG	= "CheckStateStore";

	/**
	 * Keys are groupIds. Values are last known groupPosition. If a key
	 * (groupId) is present, that group is checked.
	 * 
	 * Note: I decided not to trust last known positions for most things, since
	 * a call to mList.get..Position(id) gets delegated to the client's adapter
	 * which should have the most up to date positions.
	 */
	private LongSparseArray<Integer>					mCheckedGroups;
	/**
	 * This maps a groupId to a {@link LongSparseArray}s which store the groups
	 * child states. The child states are also stored in a
	 * {@link LongSparseArray} where checked children ids are mapped to the
	 * children's last known position within the group.
	 * 
	 * Caution: the presence of a groupId does not mean that group is checked
	 * itself, just that there are checked children in that group.
	 * 
	 * Note: I decided not to trust last known positions for most things, since
	 * a call to mList.get..Position(id) gets delegated to the client's adapter
	 * which should have the most up to date positions.
	 */
	private LongSparseArray<LongSparseArray<Integer>>	mCheckedChildren;


	/**
	 * For lists backed by adapters with unstable ids only.
	 * 
	 * Index is groupPosition and bit values are true if checked false if not.
	 */
	private BitSet										mUnstableCheckedGroups;
	/**
	 * For lists backed by adapters with unstable ids only.
	 * 
	 * This maps a groupPosition to a {@link BitSet}. The {@link BitSet} indexes
	 * are childPositions and the bit values are true if checked, false if not.
	 */
	private SparseArray<BitSet>							mCheckedUnstableChildren;

	private final BoostExpandableListView				mList;


	CheckStateStore(final BoostExpandableListView mList){

		this.mList = mList;

		if (mList.hasStableIds()){
			mCheckedGroups = new LongSparseArray<Integer>();
			mCheckedChildren = new LongSparseArray<LongSparseArray<Integer>>();
		}else{
			mUnstableCheckedGroups = new BitSet(mList.getGroupCount());
			mCheckedUnstableChildren = new SparseArray<BitSet>();
		}
	}
	
	CheckStateStore(final Parcel in, final BoostExpandableListView mList){
		
		this.mList = mList;
		
		Bundle ss = in.readBundle();
		if(ss != null && !ss.isEmpty()) {
			mCheckedGroups = (LongSparseArray<Integer>) ss.get("cGroups");
			ss.putParcelable("test", mCheckedChildren);
		}
	}

	/*********************************************************************
	 * Group State Setters
	 **********************************************************************/

	/**
	 * Stores a group's checked state (checked/unchecked) regardless of if the
	 * list's adapter has stable ids or not. Since the params don't include a
	 * groupId, the unstable id storage is completed here. If the list's adapter
	 * does have stable ids, storage is delegated to its sibling method with
	 * groupId in its params.
	 * 
	 * @param groupPosition
	 * @param isChecked
	 * @param checkChildrenWithGroup
	 */
	void setGroupState(final int groupPosition,
						final boolean isChecked,
						final boolean checkChildrenWithGroup){

		if (mList.hasStableIds()){

			final long groupId = mList.getGroupId(groupPosition);
			if (isChecked)
				mCheckedGroups.put(	groupId,
									groupPosition);
			else
				mCheckedGroups.remove(groupId);

		}else
			mUnstableCheckedGroups.clear(groupPosition);

		if (checkChildrenWithGroup)
			setGroupsChildrenState(	groupPosition,
									isChecked);
	}


	/*********************************************************************
	 * Groups Children State Setters
	 **********************************************************************/
	/**
	 * Sets all of the groups children to isChecked.
	 * 
	 * @param groupPosition
	 * @param isChecked
	 *            - should the children be checked
	 */
	private void setGroupsChildrenState(final int groupPosition,
										final boolean isChecked){

		final int childCount = mList.getChildrenCount(groupPosition);
		for (int i = 0; i < childCount; i++)
			setChildState(	groupPosition,
							i,
							isChecked);
	}


	/*********************************************************************
	 * Child State Setters
	 **********************************************************************/
	void setChildState(final int groupPosition,
						final int childPosition,
						final boolean isChecked){

		if (mList.hasStableIds()){

			final long groupId = mList.getGroupId(groupPosition);
			final long childId = mList.getChildId(	groupPosition,
													childPosition);

			if (isChecked)
				putCheckedChild(groupId,
								childPosition,
								childId);
			else
				removeCheckedChild(	groupId,
									childId);

		}else if (isChecked)
			putUnstableIdCheckedChild(	groupPosition,
										childPosition);
		else
			removeUnstableIdCheckedChild(	groupPosition,
											childPosition);


	}


	/*********************************************************************
	 * Checked States
	 **********************************************************************/

	boolean isGroupChecked(final int groupPosition){

		if (mList.hasStableIds())
			return mCheckedGroups.indexOfKey(mList.getGroupId(groupPosition)) >= 0;
		else
			return mUnstableCheckedGroups.get(groupPosition);
	}


	/**
	 * Will check to see if the specified child is checked.
	 * 
	 * @param groupPosition
	 * @param childPosition
	 * @return true if checked, false if not
	 */
	boolean isChildChecked(final int groupPosition, final int childPosition){

		if (mList.hasStableIds()){
			final long groupId = mList.getGroupId(groupPosition);
			final long childId = mList.getChildId(	groupPosition,
													childPosition);

			if (mCheckedChildren.get(groupId) == null)
				return false;
			else
				return mCheckedChildren.get(groupId)
										.indexOfKey(childId) >= 0;

		}else if (mCheckedUnstableChildren.get(groupPosition) == null)
			return false;
		else
			return mCheckedUnstableChildren.get(groupPosition)
											.get(childPosition);
	}


	/*********************************************************************
	 * Data store helpers (internal use)
	 **********************************************************************/

	private void putCheckedChild(final long groupId,
									final int childPosition,
									final long childId){

		if (mCheckedChildren.get(groupId) != null)
			mCheckedChildren.get(groupId)
							.put(	childId,
									childPosition);
		else{
			final LongSparseArray<Integer> mChildStates = new LongSparseArray<Integer>();
			mChildStates.put(	childId,
								childPosition);
			mCheckedChildren.put(	groupId,
									mChildStates);
		}

	}


	private void
		putUnstableIdCheckedChild(final int groupPosition,
									final int childPosition){


		if (mCheckedUnstableChildren.get(groupPosition) != null)

			mCheckedUnstableChildren.get(groupPosition)
									.set(	childPosition,
											true);
		else{
			final BitSet mBitSet = new BitSet(mList.getChildrenCount(groupPosition));
			mBitSet.set(childPosition,
						true);
			mCheckedUnstableChildren.put(	groupPosition,
											mBitSet);
		}

	}


	private void removeCheckedChild(final long groupId, final long childId){

		if (mCheckedChildren.get(groupId) == null
			|| mCheckedChildren.get(groupId)
								.get(childId) == null)
			return;

		mCheckedChildren.get(groupId)
						.remove(childId);

	}


	private void removeUnstableIdCheckedChild(final int groupPosition,
												final int childPosition){

		if (mCheckedUnstableChildren.get(groupPosition) == null
			|| mCheckedUnstableChildren.get(groupPosition)
										.get(childPosition) == false)
			return;

		mCheckedUnstableChildren.get(groupPosition)
								.clear(childPosition);
	}


	/*********************************************************************
	 * Checked Item id/position getters for MULTIPLE choice mode options
	 **********************************************************************/

	long[] getCheckedGroupIds(){

		if (!mList.hasStableIds()
			|| mCheckedGroups == null){
			Log.w(	TAG,
					"The adapter backing this list does not have stable ids. Please ensure your adapter implements stable ids and returns true for hasStableIds. If that's not possible, you will need to use getCheckedGroupPositions instead.");
			return new long[0];
		}

		return ListDataUtil.keys(mCheckedGroups);
	}


	List<Long> getCheckedChildIds(){

		if (!mList.hasStableIds()){
			Log.w(	TAG,
					"The adapter backing this list does not have stable ids. Please ensure your adapter implements stable ids and returns true for hasStableIds. If that's not possible, you will need to use getCheckedGroupPositions instead.");
			return new ArrayList<Long>();
		}

		final LongSparseArray<LongSparseArray<Integer>> checkedChildren = mCheckedChildren;
		final int count = checkedChildren.size();
		final ArrayList<LongSparseArray<Integer>> groupChildren = new ArrayList<LongSparseArray<Integer>>(count);

		for (int i = 0; i < count; i++)
			groupChildren.add(checkedChildren.valueAt(i));


		final ArrayList<Long> ids = new ArrayList<Long>();

		for (final LongSparseArray<Integer> groupCheckedChildren : groupChildren)
			for (final Long id : ListDataUtil.keys(groupCheckedChildren))
				ids.add(id);

		return ids;
	}


	/**
	 * Convenience for {@link getCheckedChildIds(long groupId)}
	 * 
	 * 
	 * @param groupPosition
	 * @return
	 */
	List<Long> getCheckedChildIds(final int groupPosition){

		if (!mList.hasStableIds()){
			Log.w(	TAG,
					"The adapter backing this list does not have stable ids. Please ensure your adapter implements stable ids and returns true for hasStableIds. If that's not possible, you will need to use getCheckedGroupPositions instead.");
			return new ArrayList<Long>();
		}

		final LongSparseArray<LongSparseArray<Integer>> checkedChildren = mCheckedChildren;
		final long groupId = mList.getGroupId(groupPosition);

		final int count = checkedChildren.size();
		long[] childIds = null;

		for (int i = 0; i < count; i++)
			if (checkedChildren.keyAt(i) == groupId){
				childIds = ListDataUtil.keys(checkedChildren.valueAt(i));
				break;
			}


		final ArrayList<Long> ids = new ArrayList<Long>();

		if (childIds != null)
			for (final long id : childIds)
				ids.add(id);
		else
			Log.w(	TAG,
					"childIds was null! Check to see if groupId actually matched a key in checkedChildren");

		return ids;
	}


	int[] getCheckedGroupPositions(){

		final int[] positions;
		if (mList.hasStableIds()){

			final long[] checkedGroupIds = getCheckedGroupIds();
			positions = new int[checkedGroupIds.length];

			for (int i = 0; i < checkedGroupIds.length; i++)
				positions[i] = mList.getGroupPosition(checkedGroupIds[i]);

		}else
			positions = ListDataUtil.truePositions(mUnstableCheckedGroups);

		return positions;
	}


	/**
	 * 
	 * 
	 * @return a SparseArray<int[]> where keys are groupPositions and each value
	 *         (int[]) are positions of that group's checked children.
	 */
	SparseArray<int[]> getCheckedChildPositions(){

		final SparseArray<int[]> positions = new SparseArray<int[]>();


		if (mList.hasStableIds()){


			final LongSparseArray<LongSparseArray<Integer>> checkedChildren = mCheckedChildren;

			final long[] groupIds = ListDataUtil.keys(checkedChildren);
			final int[] gPositions = new int[groupIds.length];

			for (int i = 0; i < gPositions.length; i++)
				gPositions[i] = mList.getGroupPosition(groupIds[i]);


			LongSparseArray<Integer> children;
			for (int i = 0; i < gPositions.length; i++){

				children = checkedChildren.get(groupIds[i]);

				if (children != null
					&& children.size() > 0){

					final int[] cPositions = ListDataUtil.values(children);

					if (cPositions != null
						&& cPositions.length > 0)
						positions.put(	gPositions[i],
										cPositions);
				}
			}


		}else{

			final SparseArray<BitSet> children = mCheckedUnstableChildren;
			int[] checked;
			for (int i = 0; i < children.size(); i++){
				checked = ListDataUtil.truePositions(children.get(i));
				if (checked != null
					&& checked.length > 0)
					positions.put(	i,
									checked);
			}

		}
		return positions;
	}


	int[] getCheckedChildPositions(final int groupPosition){

		if (mList.hasStableIds()){
			final long groupId = mList.getGroupId(groupPosition);
			return ListDataUtil.values(mCheckedChildren.get(groupId));

		}else
			return ListDataUtil.truePositions(mCheckedUnstableChildren.get(groupPosition));
	}


	/*********************************************************************
	 * Checked Counts
	 **********************************************************************/
	int getCheckedGroupCount(){

		if (mList.hasStableIds())
			return mCheckedGroups.size();
		else
			return mUnstableCheckedGroups.length();
	}


	int getCheckedChildCount(){

		int count = 0;
		if (mList.hasStableIds()){
			final LongSparseArray<LongSparseArray<Integer>> children = mCheckedChildren;

			for (int i = 0; i < children.size(); i++)
				count += children.get(i)
									.size();
		}else{
			final SparseArray<BitSet> children = mCheckedUnstableChildren;
			for (int i = 0; i < children.size(); i++)
				count += children.get(i)
									.cardinality();
		}
		return count;
	}


	int getCheckedChildCount(final int groupPosition){

		if (mList.hasStableIds()){
			final LongSparseArray<Integer> groupChildren = mCheckedChildren.get(groupPosition);
			return groupChildren.size();
		}else{
			final BitSet groupChildren = mCheckedUnstableChildren.get(groupPosition);
			return groupChildren.cardinality();
		}
	}


	/*********************************************************************
	 * Clearing
	 **********************************************************************/
	void clearGroups(final boolean isCheckChildreonOnGroupCheckEnabled){

		if (mList.hasStableIds())
			mCheckedGroups.clear();
		else
			mUnstableCheckedGroups.clear();
	}


	void clearChildren(){

		if (mList.hasStableIds())
			mCheckedChildren.clear();
		else
			mCheckedUnstableChildren.clear();
	}


	void clearChildren(final int groupPosition){

		if (mList.hasStableIds()){

			final long groupId = mList.getGroupId(groupPosition);

			if (mCheckedChildren.get(groupId) != null)
				mCheckedChildren.get(groupId)
								.clear();
		}else if (mCheckedUnstableChildren.get(groupPosition) != null)
			mCheckedUnstableChildren.get(groupPosition)
									.clear();

	}


	void clearAll(){

		if (mList.hasStableIds()){
			mCheckedChildren.clear();
			mCheckedGroups.clear();
		}else{
			mCheckedUnstableChildren.clear();
			mUnstableCheckedGroups.clear();
		}
	}


	/*********************************************************************
	 * Parcelable Tedious Nonsense
	 **********************************************************************/
	 static class CheckStoreSavedState extends BaseSavedState {
		 
		 
		 LongSparseArray<Integer> mCheckedGroups;	
		 
		 LongSparseArray<LongSparseArray<Integer>> mCheckedChildren;
		 
		 BitSet mUnstableCheckedGroups;
		 SparseArray<BitSet> mCheckedUnstableChildren;

		 
		 CheckStoreSavedState(final Parcel in){
			 
			 super(in);
			 
			 mUnstableCheckedGroups = (BitSet) in.readSerializable();
			 
			
			}
		 
		 
		 @Override
			public void writeToParcel(final Parcel dest, final int flags){
				
				 ListDataUtil.writeToParcel(dest, mCheckedGroups);
					
				
		         
			}
		public static final Parcelable.Creator<CheckStoreSavedState>	
									CREATOR	= new Parcelable.Creator<CheckStoreSavedState>(){
			

			@Override
			public CheckStoreSavedState
				createFromParcel(final Parcel in){
	
				return new CheckStoreSavedState(in);
			}
	
	
			@Override
			public CheckStoreSavedState[]
				newArray(final int size){
	
				return new CheckStoreSavedState[size];
			}
		};
	
	
		@Override
		public int describeContents(){
	
			return 0;
		}
	 }


}
