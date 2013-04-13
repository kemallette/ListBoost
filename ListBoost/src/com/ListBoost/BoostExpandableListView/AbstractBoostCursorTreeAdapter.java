package com.ListBoost.BoostExpandableListView;


import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ResourceCursorTreeAdapter;


public abstract class AbstractBoostCursorTreeAdapter extends ResourceCursorTreeAdapter implements BoostExpandable, OnCheckedChangeListener{

	private static final String	TAG	= "AbstractBoostCursorTreeAdapter",
	                                ID = "id",
	                                IS_CHECKED = "isChecked",
	                                IS_GROUP = "isGroup",
	                                GROUP_POSITION = "groupPosition",
	                                CHILD_POSITION = "childPosition";

	private class Holder{

		CheckBox	mBox;

		Bundle		mBoxData;
	}


	private ArrayList<ExpandableListCheckListener>	mCheckListeners;

	private BoostExpandableListView	               mList;


	public AbstractBoostCursorTreeAdapter(Context context,
	                                      BoostExpandableListView expandableListView,
	                                      Cursor cursor,
	                                      int collapsedGroupLayout,
	                                      int expandedGroupLayout,
	                                      int childLayout,
	                                      int lastChildLayout){


		super(context,
		      cursor,
		      collapsedGroupLayout,
		      expandedGroupLayout,
		      childLayout,
		      lastChildLayout);

		mList = expandableListView;
	}


	public AbstractBoostCursorTreeAdapter(Context context,
	                                      BoostExpandableListView expandableListView,
	                                      Cursor cursor,
	                                      int collapsedGroupLayout,
	                                      int expandedGroupLayout,
	                                      int childLayout){

		super(context,
		      cursor,
		      collapsedGroupLayout,
		      expandedGroupLayout,
		      childLayout);
		mList = expandableListView;
	}


	public AbstractBoostCursorTreeAdapter(Context context,
	                                      BoostExpandableListView expandableListView,
	                                      Cursor cursor,
	                                      int groupLayout,
	                                      int childLayout){

		super(context,
		      cursor,
		      groupLayout,
		      childLayout);
		mList = expandableListView;
	}


    @Override
    public void registerDataSetObserver(DataSetObserver observer){

	    super.registerDataSetObserver(observer);
	    
	   
    }


    @Override
    public void unregisterDataSetObserver(DataSetObserver observer){
	    super.unregisterDataSetObserver(observer);
    }


	@Override
	public void onCheckedChanged(CompoundButton buttonView,
	                             boolean isChecked){

		if (buttonView.getTag() != null){

			Bundle mBoxData = (Bundle) buttonView.getTag();
			mBoxData.putBoolean(IS_CHECKED,
			                    isChecked);


			if (mBoxData.getBoolean(IS_GROUP)){

				// This distinguishes between getView checking/unchecking
				// and a real check event
				if (isChecked != mList.isGroupChecked(mBoxData.getInt(GROUP_POSITION)))
					for (ExpandableListCheckListener mListener : mCheckListeners){
						mListener.onGroupCheckChange(buttonView,
						                             mBoxData.getInt(GROUP_POSITION),
						                             mBoxData.getLong(ID),
						                             isChecked);
					}


			}else{

				// This distinguishes between getView checking/unchecking
				// and a real check event
				if (isChecked != mList.isChildChecked(mBoxData.getInt(GROUP_POSITION),
				                                      mBoxData.getInt(CHILD_POSITION)))
					for (ExpandableListCheckListener mListener : mCheckListeners){
						mListener.onChildCheckChange(buttonView,
						                             mBoxData.getInt(GROUP_POSITION),
						                             mBoxData.getInt(CHILD_POSITION),
						                             mBoxData.getLong(ID),
						                             isChecked);
					}
			}
		}else
			Log.e(TAG,
			      "onCheckedChanged buttonView.getTag was null.");
	}


	@Override
	public View getChildView(int groupPosition,
	                         int childPosition,
	                         boolean isLastChild,
	                         View convertView,
	                         ViewGroup parent){


		convertView = super.getChildView(groupPosition,
		                                 childPosition,
		                                 isLastChild,
		                                 convertView,
		                                 parent);


		if (convertView != null){

			Holder mHolder;

			if (convertView.getTag() == null){

				mHolder = new Holder();
				mHolder.mBoxData = new Bundle();
				mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);

				if (mHolder.mBox == null)
					Log.e(TAG,
					      "Couldn't find android.R.id.checkbox in convertView during getChildView. "
					          +
					          "Make sure each child item has a checkbox with the id @android:id/checkbox.");
				mHolder.mBox.setOnCheckedChangeListener(this);

			}else{
				mHolder = (Holder) convertView.getTag();

				if (mHolder.mBox != null)
					mHolder.mBoxData = (Bundle) mHolder.mBox.getTag();
				else{
					mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
					if (mHolder.mBox == null)
						Log.e(TAG,
						      "Couldn't find android.R.id.checkbox in convertView during getChildView. "
						          +
						          "Make sure each child item has a checkbox with the id @android:id/checkbox.");
					mHolder.mBox.setOnCheckedChangeListener(this);
					mHolder.mBoxData = new Bundle();
				}
			}

			mHolder.mBoxData.putBoolean(IS_CHECKED,
			                            mList.isChildChecked(groupPosition,
			                                                 childPosition));
			mHolder.mBox.setChecked(mHolder.mBoxData.getBoolean(IS_CHECKED));
			mHolder.mBoxData.putBoolean(IS_GROUP,
			                            false);
			mHolder.mBoxData.putInt(GROUP_POSITION,
			                        groupPosition);
			mHolder.mBoxData.putInt(CHILD_POSITION,
			                        childPosition);
			mHolder.mBoxData.putLong(ID,
			                         getChildId(groupPosition,
			                                    childPosition));

			convertView.setTag(mHolder);
			mHolder.mBox.setTag(mHolder.mBoxData);

		}

		return convertView;
	}


	@Override
	public View getGroupView(int groupPosition,
	                         boolean isExpanded,
	                         View convertView,
	                         ViewGroup parent){

		convertView = super.getGroupView(groupPosition,
		                                 isExpanded,
		                                 convertView,
		                                 parent);

		if (convertView != null){

			Holder mHolder;

			if (convertView.getTag() == null){

				mHolder = new Holder();
				mHolder.mBoxData = new Bundle();
				mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);

				if (mHolder.mBox == null)
					Log.e(TAG,
					      "Couldn't find android.R.id.checkbox in convertView during getGroupView. "
					          +
					          "Make sure each group item has a checkbox with the id @android:id/checkbox.");
				mHolder.mBox.setOnCheckedChangeListener(this);

			}else{
				mHolder = (Holder) convertView.getTag();

				if (mHolder.mBox != null)
					mHolder.mBoxData = (Bundle) mHolder.mBox.getTag();
				else{
					mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
					if (mHolder.mBox == null)
						Log.e(TAG,
						      "Couldn't find android.R.id.checkbox in convertView during getGroupView. "
						          +
						          "Make sure each group item has a checkbox with the id @android:id/checkbox.");
					mHolder.mBox.setOnCheckedChangeListener(this);
					mHolder.mBoxData = new Bundle();
				}
			}

			mHolder.mBoxData.putBoolean(IS_CHECKED,
			                            mList.isGroupChecked(groupPosition));

			mHolder.mBox.setChecked(mHolder.mBoxData.getBoolean(IS_CHECKED));

			mHolder.mBoxData.putBoolean(IS_GROUP,
			                            true);
			mHolder.mBoxData.putInt(GROUP_POSITION,
			                        groupPosition);
			mHolder.mBoxData.putLong(ID,
			                         getGroupId(groupPosition));

			convertView.setTag(mHolder);
			mHolder.mBox.setTag(mHolder.mBoxData);

		}

		return convertView;
	}


	public boolean
	    registerExpandableCheckListener(ExpandableListCheckListener listener){

		if (mCheckListeners == null)
			mCheckListeners = new ArrayList<ExpandableListCheckListener>();

		return mCheckListeners.add(listener);
	}


	public boolean
	    unregisterExpandableCheckListener(ExpandableListCheckListener listener){

		if (mCheckListeners == null)
			return false;
		else
			return mCheckListeners.remove(listener);
	}
}
