package com.kemallette.ListBoost.ExpandableList;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.SparseArrayCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.kemallette.ListBoost.R;
import com.kemallette.ListBoost.ExpandableList.BoostExpandableListAdapter.Holder;
import com.kemallette.ListBoost.ExpandableList.CheckStateStore.CheckStoreSavedState;

public class BoostExpandableListView extends
									ExpandableListView	implements
														BoostExpandableList,
														ExpandableListCheckListener{

	private static final String			TAG						= "BoostExpandableListView";


	/**
	 * Flag indicating if an item's checked state change is from a user actually
	 * touching the screen
	 */
	private boolean						ignoreCheckChange		= false;
	private boolean						isOneItemChoice			= false;
	private boolean						isChoiceOn				= true;
	/**
	 * If true, on a group check change, that group's children will match the
	 * group's check state. In other words, if you check a group, all its
	 * children will also be checked and the reverse. If a group is unchecked,
	 * all its children will be unchecked.
	 */
	private boolean						checkChildrenWithGroup	= false;


	private int							groupChoiceMode			= CHECK_MODE_MULTI;
	private int							childChoiceMode			= CHECK_MODE_MULTI;

	private CheckStateStore				mCheckStore;
	private ExpandableListCheckListener	mClientCheckListener;

	private BoostExpandableListAdapter	mAdapterWrapper;


	public BoostExpandableListView(	final Context context,
									final AttributeSet attrs,
									final int defStyle){

		super(	context,
				attrs,
				defStyle);

		if (attrs != null){
			final TypedArray a = getContext()
												.obtainStyledAttributes(attrs,
																		R.styleable.BoostExpandableListView,
																		0,
																		0);

			groupChoiceMode = a.getInt(	R.styleable.BoostExpandableListView_groupChoiceMode,
										CHECK_MODE_MULTI);

			childChoiceMode = a.getInt(	R.styleable.BoostExpandableListView_childChoiceMode,
										CHECK_MODE_MULTI);

			checkChildrenWithGroup = a.getBoolean(	R.styleable.BoostExpandableListView_checkChildrenWithGroup,
													false);

			isOneItemChoice = a.getBoolean(	R.styleable.BoostExpandableListView_oneItemChoice,
											false);

			// default on instantiation is true so if both modes are NONE we
			// need to set to false
			if (groupChoiceMode == CHECK_MODE_NONE
				&& childChoiceMode == CHECK_MODE_NONE)
				isChoiceOn = false;


			if (isOneItemChoice){
				groupChoiceMode = CHECK_MODE_ONE_ALL;
				childChoiceMode = CHECK_MODE_ONE_ALL;
				checkChildrenWithGroup = false;
				isChoiceOn = true;
			}

			a.recycle();
		}

	}


	public BoostExpandableListView(	final Context context,
									final AttributeSet attrs){

		this(	context,
				attrs,
				-1);
	}


	public BoostExpandableListView(final Context context){

		this(	context,
				null);
	}


	@Override
	public void setAdapter(final ExpandableListAdapter adapter){

		if (adapter == null)
			throw new NullPointerException("The adapter you passed was null");


		if (adapter instanceof BoostExpandableListAdapter)
			this.mAdapterWrapper = (BoostExpandableListAdapter) adapter;

		else if (mAdapterWrapper == null)
			mAdapterWrapper = new BoostExpandableListAdapter(	adapter,
																this);
		else
			mAdapterWrapper.setWrappedAdapter(adapter);

		super.setAdapter(mAdapterWrapper);

		mCheckStore = new CheckStateStore(this); // Must do this to ensure
													// hasStableIds stays
													// current
	}


	@Override
	public ExpandableListAdapter getExpandableListAdapter(){

		return mAdapterWrapper.getWrappedAdapter();
	}


	@Override
	public BoostExpandableListAdapter getBoostAdapter(){

		return mAdapterWrapper;
	}


	/***********************************************************
	 * ExpandableListCheckListener Callbacks
	 ************************************************************/
	@Override
	public void onGroupCheckChange(final Checkable checkedView,
									final int groupPosition,
									final long groupId,
									final boolean isChecked){

		if (isChoiceOn
			&& !ignoreCheckChange){

			if (groupChoiceMode != CHOICE_MODE_NONE)
				setGroupCheckedState(	groupPosition,
										isChecked);
			else
				Log.i(	TAG
							+ "\n onGroupCheckChange",
						"groupChoice mode is NONE");

			if (mClientCheckListener != null)
				mClientCheckListener.onGroupCheckChange(checkedView,
														groupPosition,
														groupId,
														isChecked);
		}
	}


	@Override
	public void onChildCheckChange(final Checkable checkedView,
									final int groupPosition,
									final long groupId,
									final int childPosition,
									final long childId,
									final boolean isChecked){

		if (isChoiceOn
			&& !ignoreCheckChange){

			if (childChoiceMode != CHOICE_MODE_NONE)
				setChildCheckedState(	groupPosition,
										childPosition,
										isChecked);
			else
				Log.i(	TAG
							+ "\n onChildCheckChange",
						"childChoice mode is NONE");

			if (mClientCheckListener != null)
				mClientCheckListener.onChildCheckChange(checkedView,
														groupPosition,
														groupId,
														childPosition,
														childId,
														isChecked);
		}
	}


	/*********************************************************************
	 * Choice Mode Related
	 **********************************************************************/

	@Override
	public BoostExpandableList
		enableChoice(final int groupChoiceMode, final int childChoiceMode){

		isOneItemChoice = false;
		isChoiceOn = true;

		setGroupChoiceMode(groupChoiceMode);
		setChildChoiceMode(childChoiceMode);

		mAdapterWrapper.enableChoice();

		refreshVisibleItems();
		return this;
	}


	@Override
	public BoostExpandableList disableChoice(){

		isChoiceOn = false;
		isOneItemChoice = false;

		groupChoiceMode = CHOICE_MODE_NONE;
		childChoiceMode = CHOICE_MODE_NONE;

		mAdapterWrapper.disableChoice();

		refreshVisibleItems();
		return this;
	}


	@Override
	public boolean isChoiceOn(){

		return isChoiceOn;
	}


	@Override
	public BoostExpandableList setGroupChoiceMode(final int choiceMode){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nsetGroupChoiceMode",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}

		groupChoiceMode = choiceMode;

		if (choiceMode != CHECK_MODE_MULTI)
			clearGroups();

		if (choiceMode == CHOICE_MODE_NONE
			|| choiceMode == CHOICE_MODE_MULTIPLE)
			refreshVisibleItems();

		return this;
	}


	@Override
	public BoostExpandableList setChildChoiceMode(final int choiceMode){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nsetChildChoiceMode",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}

		childChoiceMode = choiceMode;

		if (choiceMode != CHECK_MODE_MULTI)
			clearChildren();

		if (choiceMode == CHOICE_MODE_NONE
			|| choiceMode == CHOICE_MODE_MULTIPLE)
			refreshVisibleItems();

		return this;
	}


	@Override
	public BoostExpandableList
		checkChildrenWithGroup(final boolean checkChildrenWithGroup){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n checkChildrenWithGroup",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}

		this.checkChildrenWithGroup = checkChildrenWithGroup;

		return this;
	}


	@Override
	public boolean checkChildrenWithGroup(){

		return checkChildrenWithGroup;
	}


	@Override
	public BoostExpandableList enableOnlyOneItemChoice(boolean enable){


		if (enable){
			isOneItemChoice = true;
			isChoiceOn = true;

			checkChildrenWithGroup = false;

			groupChoiceMode = CHECK_MODE_ONE_ALL;
			childChoiceMode = CHECK_MODE_ONE_ALL;

			mAdapterWrapper.enableChoice();

			clearAll();
		}else{
			isOneItemChoice = false;
			isChoiceOn = false;

			groupChoiceMode = CHOICE_MODE_NONE;
			childChoiceMode = CHOICE_MODE_NONE;

			mAdapterWrapper.disableChoice();

			refreshVisibleItems();
		}


		return this;
	}


	@Override
	public boolean isOneItemChoiceOn(){

		return isOneItemChoice;
	}


	@Override
	public int getGroupChoiceMode(){

		return groupChoiceMode;
	}


	@Override
	public int getChildChoiceMode(){

		return childChoiceMode;
	}


	/*********************************************************************
	 * Internal utils
	 **********************************************************************/

	protected void refreshVisibleItems(){

		View listItem;
		Holder mHolder;

		Checkable checkableView;
		Bundle mCheckableData;

		final int firstVis = getFirstVisiblePosition();
		final int lastVis = getLastVisiblePosition();
		int count = lastVis
					- firstVis;

		while (count >= 0){ // looping through visible list items which
							// are the only items that will need to be
							// refreshed. The adapter's getView will
							// take care of all non-visible items when
							// the list is scrolled

			listItem = getChildAt(count);

			if (listItem != null){

				mHolder = (Holder) listItem.getTag(R.id.view_holder_key);
				checkableView = mHolder.mBox;
				mCheckableData = mHolder.getBoxData();

				if (!mCheckableData.isEmpty()
					&& mCheckableData.containsKey(BoostExpandableListAdapter.IS_GROUP))

					if (mCheckableData.getBoolean(BoostExpandableListAdapter.IS_GROUP))
						refreshVisibleGroup(listItem,
											checkableView,
											mCheckableData);
					else
						refreshVisibleChild(listItem,
											checkableView,
											mCheckableData);

			}else
				Log.d(	TAG
							+ "\n refreshVisibleCheckableViews",
						"getChildAt didn't retrieve a non-null view");


			count--;
		}

	}


	protected void refreshVisibleGroup(final View listItem,
										final Checkable checkView,
										final Bundle mCheckableData){

		if (isChoiceOn
			&& getGroupChoiceMode() != CHECK_MODE_NONE){

			final int groupPosition = mCheckableData.getInt(BoostExpandableListAdapter.GROUP_POSITION,
															-1);

			enableCheckable(checkView);


			ignoreCheckChange = true;
			if (groupPosition > -1)
				checkView.setChecked(isGroupChecked(groupPosition));
			else
				checkView.setChecked(false);
			ignoreCheckChange = false;

		}else
			disableCheckable(checkView);
	}


	protected void refreshVisibleChild(final View listItem,
										final Checkable checkView,
										final Bundle mCheckableData){

		if (isChoiceOn
			&& getChildChoiceMode() != CHECK_MODE_NONE){

			final int groupPosition = mCheckableData.getInt(BoostExpandableListAdapter.GROUP_POSITION,
															-1);
			final int childPosition = mCheckableData.getInt(BoostExpandableListAdapter.CHILD_POSITION,
															-1);

			enableCheckable(checkView);


			ignoreCheckChange = true;

			if (childPosition > -1
				&& groupPosition > -1)
				checkView.setChecked(isChildChecked(groupPosition,
													childPosition));
			else
				checkView.setChecked(false);

			ignoreCheckChange = false;

		}else
			disableCheckable(checkView);
	}


	private void enableCheckable(final Checkable checkableView){

		((View) checkableView).setVisibility(View.VISIBLE);
	}


	private void disableCheckable(final Checkable checkableView){

		((View) checkableView).setVisibility(View.INVISIBLE);
	}


	/*********************************************************************
	 * Public utils
	 **********************************************************************/

	/**
	 * This will return the group position for a groupId.
	 * 
	 * <b>Caution:</b> This has to loop through all group items in the list
	 * which could raise performance issues
	 * 
	 * @param groupId
	 *            - id for the group you want a position for
	 * @return group position in the list or a negative number if one was not
	 *         found
	 */
	public int getGroupPosition(final long groupId){

		// loop through group positions to match groupId
		for (int i = 0; i < getExpandableListAdapter()
														.getGroupCount(); i++)
			if (getExpandableListAdapter()
											.getGroupId(i) == groupId)
				return i;

		return -1;
	}


	/**
	 * This will find the child position within the a group at groupPosition
	 * who's id matches childId.
	 * 
	 * <b>Caution:</b> This has to loop through all of this groups child items
	 * which could raise performance issues
	 * 
	 * @param groupPosition
	 *            - the group position the child falls under
	 * @param childId
	 * @return the child's position within the group at groupPosition or a
	 *         negative number if one was not found
	 */
	public int getChildPosition(final int groupPosition, final long childId){

		// loop through group's child positions to match child id
		for (int i = 0; i < getExpandableListAdapter()
														.getChildrenCount(groupPosition); i++)
			if (getExpandableListAdapter()
											.getChildId(groupPosition,
														i) == childId)
				return i;

		return -1;
	}


	/**
	 * This is a convenience for to avoid having to call getGroupPosition(long
	 * groupId) and getChildPosition(int groupPosition, long childId).
	 * 
	 * <b>Caution:</b> These methods have to loop through all of this groups
	 * child items which could raise performance issues
	 * 
	 * @param groupId
	 *            - id of the group the child falls under
	 * @param childId
	 * @return the child's position within the group with the specified groupId
	 *         or a negative number if one was not found
	 */
	public int getChildPosition(final long groupId, final long childId){

		final int groupPosition = getGroupPosition(groupId);

		if (!(groupPosition < 0))
			return getChildPosition(groupPosition,
									childId);

		return -1;
	}


	/*********************************************************************
	 * Group and Child check state getters/setters
	 **********************************************************************/
	@Override
	public BoostExpandableList
		setGroupCheckedState(final int groupPosition,
								final boolean isChecked){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nsetGroupCheckedState",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");
			return this;
		}

		if (isOneItemChoice){

			clearAll();

			if (checkChildrenWithGroup)
				Log.e(	TAG
							+ "\nsetGroupCheckedState",
						"One Item Choice Mode is on, but checkChildrenWithGroup is true. ");
		}

		switch(groupChoiceMode){

			case CHECK_MODE_NONE:
				return this;

			case CHECK_MODE_ONE:
				clearGroups();
				break;
		}


		mCheckStore.setGroupState(	groupPosition,
									isChecked,
									checkChildrenWithGroup);

		refreshVisibleItems();

		return this;
	}


	@Override
	public BoostExpandableList
		setChildCheckedState(final int groupPosition,
								final int childPosition,
								final boolean isChecked){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nsetChildCheckedState",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");
			return this;
		}


		if (isOneItemChoice){

			clearAll();

			if (checkChildrenWithGroup)
				Log.e(	TAG
							+ "\nsetChildCheckedState",
						"One Item Choice Mode is on, but checkChildrenWithGroup is true. ");
		}

		switch(childChoiceMode){

			case CHECK_MODE_NONE:
				return this;

			case CHECK_MODE_ONE:
				clearChildren();
				break;

			case CHILD_CHECK_MODE_ONE_PER_GROUP:
				clearChildren(groupPosition);
				break;
		}

		mCheckStore.setChildState(	groupPosition,
									childPosition,
									isChecked);

		refreshVisibleItems();

		return this;
	}


	/*********************************************************************
	 * Checked State
	 **********************************************************************/
	@Override
	public boolean isGroupChecked(final int groupPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nisGroupChecked",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return false;
		}

		return mCheckStore.isGroupChecked(groupPosition);
	}


	@Override
	public boolean isChildChecked(final int groupPosition,
									final int childPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\nisChildChecked",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return false;
		}

		return mCheckStore.isChildChecked(	groupPosition,
											childPosition);
	}


	/*********************************************************************
	 * Checked Item Counts
	 **********************************************************************/

	/**
	 * Gives a count of ALL checked items in the list (groups and children
	 * both).
	 * 
	 * @return total checked items
	 */
	@Override
	public int getCheckedItemCount(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+
						"getCheckedItemCount",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return 0;
		}

		return getCheckedGroupCount()
				+ getCheckedChildCount();
	}


	/**
	 * Gives a count of checked groups in the list
	 * 
	 * @return the number of checked groups
	 */
	@Override
	public int getCheckedGroupCount(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+ "getCheckedGroupCount",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return 0;
		}

		return mCheckStore.getCheckedGroupCount();
	}


	/**
	 * Gives a count of ALL checked children in the list regardless of group
	 * parent.
	 * 
	 * @return total checked children
	 */
	@Override
	public int getCheckedChildCount(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+ "getCheckedChildCount",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return 0;
		}

		return mCheckStore.getCheckedChildCount();
	}


	/**
	 * Gives a count of all the checked children for the parent group at a
	 * specified group at groupPosition.
	 * 
	 * @param groupPosition
	 *            - position in the list of the group you want to gather the
	 *            checked child count from
	 * @return total checked child count for the specified group at
	 *         groupPosition
	 */
	@Override
	public int getCheckedChildCount(final int groupPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+ "getCheckedChildCount(groupPosition)",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return 0;
		}

		return mCheckStore.getCheckedChildCount(groupPosition);
	}


	/*********************************************************************
	 * Checked Item id/position getters
	 **********************************************************************/

	@Override
	public long[] getCheckedGroupIds(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+ "getCheckedGroupIds",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new long[0];
		}
		return mCheckStore.getCheckedGroupIds();
	}


	@Override
	public List<Long> getCheckedChildIds(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n"
						+ "getCheckedChildIds",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new ArrayList<Long>();
		}
		return mCheckStore.getCheckedChildIds();
	}


	@Override
	public List<Long> getCheckedChildIds(final int groupPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n getCheckedChildIds",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new ArrayList<Long>();
		}
		return mCheckStore.getCheckedChildIds(groupPosition);
	}


	@Override
	public int[] getCheckedGroupPositions(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n getCheckedGroupPositions",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new int[0];
		}
		return mCheckStore.getCheckedGroupPositions();
	}


	@Override
	public SparseArrayCompat<int[]> getCheckedChildPositions(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n getCheckedChildPositions",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new SparseArrayCompat<int[]>();
		}
		return mCheckStore.getCheckedChildPositions();
	}


	@Override
	public int[] getCheckedChildPositions(final int groupPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n getCheckedChildPositions(groupPosition)",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return new int[0];
		}
		return mCheckStore.getCheckedChildPositions(groupPosition);
	}


	/*********************************************************************
	 * Clearing
	 **********************************************************************/

	/**
	 * Clears all checked items in the list and resets the all checked counts.
	 */
	@Override
	public BoostExpandableList clearAll(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n clearAll",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}
		mCheckStore.clearAll();

		refreshVisibleItems();

		return this;
	}


	@Override
	public BoostExpandableList clearGroups(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n clearGroups",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}
		mCheckStore.clearGroups(checkChildrenWithGroup);

		refreshVisibleItems();

		return this;
	}


	@Override
	public BoostExpandableList clearChildren(){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n clearChildren",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}
		mCheckStore.clearChildren();

		refreshVisibleItems();

		return this;
	}


	/**
	 * Clears all checked children for the specified parent group at
	 * groupPosition.
	 * 
	 * @param groupPosition
	 *            - the group position for the children you want to clear
	 */
	@Override
	public BoostExpandableList
		clearChildren(final int groupPosition){

		if (!isChoiceOn){

			Log.e(	TAG
						+ "\n clearChildren(groupPosition)",
					"Choice is not enabled. Try using enableChoice(groupChoiceMode, childChoiceMode) or enableOnlyOneItemChoice()");

			return this;
		}
		mCheckStore.clearChildren(groupPosition);

		refreshVisibleItems();

		return this;
	}


	/***********************************************************
	 * Get/Set/Remove ExpandableListCheckListener
	 ************************************************************/
	@Override
	public BoostExpandableList
		setExpandableCheckListener(
									final ExpandableListCheckListener listener){

		mClientCheckListener = listener;
		return this;
	}


	@Override
	public BoostExpandableList
		removeExpandableCheckListener(){

		mClientCheckListener = null;

		return this;
	}


	@Override
	public ExpandableListCheckListener getExpandableListCheckListener(){

		return mClientCheckListener;
	}


	/*********************************************************************
	 * Adapter delegates - internal use
	 **********************************************************************/
	protected boolean hasStableIds(){

		return mAdapterWrapper.hasStableIds();
	}


	protected long getChildId(final int groupPosition, final int childPosition){

		return mAdapterWrapper.getChildId(	groupPosition,
											childPosition);
	}


	protected long getGroupId(final int groupPosition){

		return mAdapterWrapper.getGroupId(groupPosition);
	}


	protected int getGroupCount(){

		return mAdapterWrapper.getGroupCount();
	}


	protected int getChildrenCount(final int groupPosition){

		return mAdapterWrapper.getChildrenCount(groupPosition);
	}


	protected long[] getGroupChildrenIds(final int groupPosition){

		long[] ids;
		final int childCount = getChildrenCount(groupPosition);

		if (hasStableIds()){
			ids = new long[childCount];
			for (int i = 0; i < childCount; i++)
				ids[i] = getChildId(groupPosition,
									i);
		}else
			ids = new long[0];

		return ids;
	}


	/*********************************************************************
	 * Save/Restore State
	 **********************************************************************/
	@Override
	public void onRestoreInstanceState(final Parcelable state){

		if (!(state instanceof BoostExpandableState)){
			super.onRestoreInstanceState(state);
			return;
		}

		BoostExpandableState ss = (BoostExpandableState) state;

		super.onRestoreInstanceState(ss.getSuperState());

		if (ss.mCheckedStoreState != null)
			mCheckStore = new CheckStateStore(	this,
												ss.mCheckedStoreState);

	}


	@Override
	public Parcelable onSaveInstanceState(){

		final Parcelable superState = super.onSaveInstanceState();

		return new BoostExpandableState(superState,
										mCheckStore,
										isOneItemChoice,
										isChoiceOn,
										checkChildrenWithGroup,
										groupChoiceMode,
										childChoiceMode);
	}

	static class BoostExpandableState	extends
										BaseSavedState{

		boolean					isOneItemChoice;
		boolean					isChoiceOn;
		boolean					checkChildrenWithGroup;

		int						groupChoiceMode;
		int						childChoiceMode;

		CheckStoreSavedState	mCheckedStoreState;


		/**
		 * Constructor called from
		 * {@link BoostExpandableListView#onSaveInstanceState()}
		 */
		BoostExpandableState(	final Parcelable superState,
								final CheckStateStore mCheckStore,
								boolean isOneItemChoice,
								boolean isChoiceOn,
								boolean checkChildrenWithGroup,
								int groupChoiceMode,
								int childChoiceMode){

			super(superState);

			this.isOneItemChoice = isOneItemChoice;
			this.isChoiceOn = isChoiceOn;
			this.checkChildrenWithGroup = checkChildrenWithGroup;
			this.groupChoiceMode = groupChoiceMode;
			this.childChoiceMode = childChoiceMode;

			mCheckedStoreState = mCheckStore.getSavedState(superState);
		}


		/**
		 * Constructor called from {@link #CREATOR}
		 */
		private BoostExpandableState(final Parcel in){

			super(in);

			mCheckedStoreState = in.readParcelable(CheckStoreSavedState.class.getClassLoader());

			isOneItemChoice = in.readByte() == 1;
			isChoiceOn = in.readByte() == 1;
			checkChildrenWithGroup = in.readByte() == 1;

			groupChoiceMode = in.readInt();
			childChoiceMode = in.readInt();
		}


		@Override
		public void writeToParcel(final Parcel out, final int flags){

			super.writeToParcel(out,
								flags);

			out.writeParcelable(mCheckedStoreState,
								0);

			out.writeByte((byte) (isOneItemChoice ? 1 : 0));
			out.writeByte((byte) (isChoiceOn ? 1 : 0));
			out.writeByte((byte) (checkChildrenWithGroup ? 1 : 0));

			out.writeInt(groupChoiceMode);
			out.writeInt(childChoiceMode);
		}

		public static final Parcelable.Creator<BoostExpandableState>	CREATOR	= new Parcelable.Creator<BoostExpandableState>(){

																					@Override
																					public BoostExpandableState
																						createFromParcel(final Parcel in){

																						return new BoostExpandableState(in);
																					}


																					@Override
																					public BoostExpandableState[]
																						newArray(final int size){

																						return new BoostExpandableState[size];
																					}
																				};
	}


	/*********************************************************************
	 * Overrides from underlying ListView and AbsListView
	 **********************************************************************/

	/*
	 * For {@link BoostExpandableListView}, use clearAllChoices() instead
	 */
	@Override
	public void clearChoices(){

		Log.w(	TAG,
				"For BoostExpandableListView, use clearAllChoices() instead of clearChoices()");
	}


	/*
	 * For {@link BoostExpandableListView}, use getCheckedChildIds or
	 * getCheckedGroupIds instead.
	 */
	@Override
	public long[] getCheckedItemIds(){

		Log.e(	TAG,
				"For BoostExpandableListView, use getCheckedChildIds or getCheckedGroupIds instead.");
		return null;
	}


	/*
	 * For {@link BoostExpandableListView}, use getCheckedChildPositions or
	 * getCheckedGroupPositions instead
	 */
	@Override
	public int getCheckedItemPosition(){

		Log.e(	TAG,
				"For BoostExpandableListView, use getCheckedChildPositions or getCheckedGroupPositions instead");
		return -1;
	}


	/*
	 * For {@link BoostExpandableListView}, use getCheckedChildPositions or
	 * getCheckedGroupPositions instead
	 */
	@Override
	public SparseBooleanArray getCheckedItemPositions(){

		Log.e(	TAG,
				"For BoostExpandableListView, use getCheckedChildPositions or getCheckedGroupPositions instead");
		return null;
	}


	/*
	 * For {@link BoostExpandableListView}, use getGroupChoiceMode or
	 * getChildChoiceMode instead
	 */
	@Override
	public int getChoiceMode(){

		Log.e(	TAG,
				"For BoostExpandableListView, use getGroupChoiceMode or getChildChoiceMode instead");
		return -1;
	}

}
