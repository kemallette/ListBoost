package com.kemallette.ListBoost.ExpandableList;


import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListAdapter;

import com.kemallette.ListBoost.R;

public class BoostExpandableListAdapter	extends
											BaseExpandableListAdapter	implements
																		ExpandableListAdapterWrapper,
																		OnCheckedChangeListener{

	// TODO: To implement 'checking' a whole list item, instead of using a
	// compound button,
	// will need to implement checkable list item views with custom listener
	// interface

	private static final String	TAG	= "BoostExpandableListAdapter";

	static final String			GROUP_POSITION	= "groupPosition",
		CHILD_POSITION = "childPosition",
		GROUP_ID = "groupId",
		CHILD_ID = "childId",
		IS_GROUP = "isGroup";


	/**
	 * Listens to the client adapter for data changes.
	 */
	protected class MyDataObserver	extends
									DataSetObserver{

		// TODO: will probably need to use these to fully implement choice for
		// unstableIds

		/*
		 * Client adapter data has changed
		 * 
		 * Called when the contents of the data set have changed. The recipient
		 * will obtain the new contents the next time it queries the data set.
		 */
		@Override
		public void onChanged(){

			mDataSetObservable.notifyChanged();
		}


		/*
		 * Called when the data set is no longer valid and cannot be queried
		 * again, such as when the data set has been closed.
		 */
		@Override
		public void onInvalidated(){

			mDataSetObservable.notifyInvalidated();
		}
	}

	protected static class Holder{

		Bundle			mData	= new Bundle();
		CompoundButton	mBox;


		void tagGroupBoxData(final int groupPosition, final long groupId){

			mData.putInt(	GROUP_POSITION,
							groupPosition);
			mData.putLong(	GROUP_ID,
							groupId);
			mData.putBoolean(	IS_GROUP,
								true);
			mBox.setTag(R.id.checkable_tag_key,
						mData);
		}


		void tagChildBoxData(final int groupPosition,
								final int childPosition,
								final long groupId,
								final long childId){

			mData.putInt(	GROUP_POSITION,
							groupPosition);
			mData.putInt(	CHILD_POSITION,
							childPosition);
			mData.putLong(	GROUP_ID,
							groupId);
			mData.putLong(	CHILD_ID,
							childId);
			mData.putBoolean(	IS_GROUP,
								false);
			mBox.setTag(R.id.checkable_tag_key,
						mData);
		}


		Bundle getBoxData(){

			return (Bundle) mBox.getTag(R.id.checkable_tag_key);
		}
	}


	boolean							ignoreCheckChange	= false;
	boolean							isChoiceOn			= true;

	private final DataSetObservable	mDataSetObservable	= new DataSetObservable();
	private final MyDataObserver	mDataObserver		= new MyDataObserver();

	private ExpandableListAdapter	mWrappedAdapter;
	private final BoostExpandableList	mList;


	public BoostExpandableListAdapter(final ExpandableListAdapter mWrappedAdapter,
										final BoostExpandableList mList){

		this.mWrappedAdapter = mWrappedAdapter;
		this.mList = mList;
		isChoiceOn = mList.isChoiceOn();

		registerClientDataSetObserver(mDataObserver);
	}


	void enableChoice(){

		isChoiceOn = true;
	}


	void disableChoice(){

		isChoiceOn = false;
	}


	boolean isChoiceOn(){

		return isChoiceOn;
	}


	@Override
	public void onCheckedChanged(final CompoundButton mButton,
									final boolean isChecked){

		if (!ignoreCheckChange
			&& isChoiceOn()){

			Bundle mCheckData;

			if (mButton.getTag(R.id.checkable_tag_key) != null){

				mCheckData = (Bundle) mButton.getTag(R.id.checkable_tag_key);

				if (!mCheckData.isEmpty()
					&& mCheckData.containsKey(IS_GROUP))

					if (mCheckData.getBoolean(IS_GROUP))
						((ExpandableListCheckListener) mList).onGroupCheckChange(	mButton,
													mCheckData.getInt(GROUP_POSITION),
													mCheckData.getLong(GROUP_ID),
													isChecked);
					else
						((ExpandableListCheckListener) mList).onChildCheckChange(	mButton,
													mCheckData.getInt(GROUP_POSITION),
													mCheckData.getLong(GROUP_ID),
													mCheckData.getInt(CHILD_POSITION),
													mCheckData.getLong(CHILD_ID),
													isChecked);

			}else
				Log.e(	TAG,
						"onCheckedChange mButton didn't have any tag data :( ");
		}
	}


	@Override
	public ExpandableListAdapter getWrappedAdapter(){

		return mWrappedAdapter;
	}


	public void setWrappedAdapter(final ExpandableListAdapter mClientAdapter){

		mWrappedAdapter = mClientAdapter;
	}


	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
								final View convertView, final ViewGroup parent){

		final View groupView = mWrappedAdapter.getGroupView(groupPosition,
															isExpanded,
															convertView,
															parent);
		if (groupView == null)
			Log.e(	TAG,
					"Users adapter returned null for getGroupView");

		Holder mGroupHolder;
		if (groupView.getTag(R.id.view_holder_key) == null){
			mGroupHolder = new Holder();

			mGroupHolder.mBox = (CheckBox) groupView
													.findViewById(android.R.id.checkbox);


			mGroupHolder.mBox.setOnCheckedChangeListener(this);
			groupView.setTag(	R.id.view_holder_key,
								mGroupHolder);
		}else
			mGroupHolder = (Holder) groupView.getTag(R.id.view_holder_key);

		mGroupHolder.tagGroupBoxData(	groupPosition,
										getGroupId(groupPosition));

		if (isChoiceOn
			&& mList.getGroupChoiceMode() != BoostExpandableList.CHECK_MODE_NONE){

			mGroupHolder.mBox.setVisibility(View.VISIBLE);


			ignoreCheckChange = true;
			mGroupHolder.mBox.setChecked(mList.isGroupChecked(groupPosition));
			ignoreCheckChange = false;

		}else
			// if choice mode isn't on or CHECK_MODE_NONE, we need to hide
			// checkable views
			mGroupHolder.mBox.setVisibility(View.INVISIBLE);

		return groupView;
	}


	@Override
	public View getChildView(final int groupPosition,
								final int childPosition,
								final boolean isLastChild,
								final View convertView,
								final ViewGroup parent){

		final View childView = mWrappedAdapter.getChildView(groupPosition,
															childPosition,
															isLastChild,
															convertView,
															parent);

		if (childView == null)
			Log.e(	TAG,
					"Users adapter returned null for getChildView");

		Holder mChildHolder;
		if (childView.getTag(R.id.view_holder_key) == null){
			mChildHolder = new Holder();

			mChildHolder.mBox = (CheckBox) childView
													.findViewById(android.R.id.checkbox);

			mChildHolder.mBox.setOnCheckedChangeListener(this);
			childView.setTag(	R.id.view_holder_key,
								mChildHolder);
		}else
			mChildHolder = (Holder) childView.getTag(R.id.view_holder_key);


		mChildHolder.tagChildBoxData(	groupPosition,
										childPosition,
										getGroupId(groupPosition),
										getChildId(	groupPosition,
													childPosition));


		if (isChoiceOn
			&& mList.getChildChoiceMode() != BoostExpandableList.CHECK_MODE_NONE){

			mChildHolder.mBox.setVisibility(View.VISIBLE);


			ignoreCheckChange = true;
			mChildHolder.mBox.setChecked(mList.isChildChecked(	groupPosition,
																childPosition));
			ignoreCheckChange = false;

		}else
			// if choice mode isn't on or CHECK_MODE_NONE, we need to hide
			// checkable views
			mChildHolder.mBox.setVisibility(View.INVISIBLE);

		return childView;
	}


	@Override
	public void registerDataSetObserver(final DataSetObserver mObserver){

		mDataSetObservable.registerObserver(mObserver);
	}


	@Override
	public void unregisterDataSetObserver(final DataSetObserver mObserver){

		mDataSetObservable.unregisterObserver(mObserver);
	}


	/*********************************************************************
	 * Delegates to client's adapter (mWrappedAdapter)
	 **********************************************************************/
	@Override
	public boolean areAllItemsEnabled(){

		return mWrappedAdapter.areAllItemsEnabled();
	}


	@Override
	public Object getChild(final int groupPosition, final int childPosition){

		return mWrappedAdapter.getChild(groupPosition,
										childPosition);
	}


	@Override
	public long getChildId(final int groupPosition, final int childPosition){

		return mWrappedAdapter.getChildId(	groupPosition,
											childPosition);
	}


	@Override
	public int getChildrenCount(final int groupPosition){

		return mWrappedAdapter.getChildrenCount(groupPosition);
	}


	@Override
	public long getCombinedChildId(final long groupId, final long childId){

		return mWrappedAdapter.getCombinedChildId(	groupId,
													childId);
	}


	@Override
	public long getCombinedGroupId(final long arg0){

		return mWrappedAdapter.getCombinedGroupId(arg0);
	}


	@Override
	public Object getGroup(final int groupPosition){

		return mWrappedAdapter.getGroup(groupPosition);
	}


	@Override
	public int getGroupCount(){

		return mWrappedAdapter.getGroupCount();
	}


	@Override
	public long getGroupId(final int groupPosition){

		return mWrappedAdapter.getGroupId(groupPosition);
	}


	@Override
	public boolean hasStableIds(){

		return mWrappedAdapter.hasStableIds();
	}


	@Override
	public boolean isChildSelectable(final int groupPosition,
										final int childPosition){

		return mWrappedAdapter.isChildSelectable(	groupPosition,
													childPosition);
	}


	@Override
	public boolean isEmpty(){

		return mWrappedAdapter.isEmpty();
	}


	@Override
	public void onGroupCollapsed(final int groupPosition){

		mWrappedAdapter.onGroupCollapsed(groupPosition);
	}


	@Override
	public void onGroupExpanded(final int groupPosition){

		mWrappedAdapter.onGroupExpanded(groupPosition);
	}


	private void registerClientDataSetObserver(final DataSetObserver mObserver){

		mWrappedAdapter.registerDataSetObserver(mObserver);

	}


	private void
		unregisterClientDataSetObserver(final DataSetObserver mObserver){

		mWrappedAdapter.unregisterDataSetObserver(mObserver);

	}
}
