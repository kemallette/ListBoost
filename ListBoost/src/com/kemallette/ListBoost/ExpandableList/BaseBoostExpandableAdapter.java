package com.kemallette.ListBoost.ExpandableList;


import android.content.Context;
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


public abstract class BaseBoostExpandableAdapter extends
												BaseExpandableListAdapter	implements
																			BoostExpandableAdapter,
																			OnCheckedChangeListener{


	private static final String	TAG	= "BaseBoostExpandableAdapter";


	private class Holder{

		CheckBox	mBox;

		Bundle		mBoxData;
	}


	private ExpandableListCheckListener	mCheckListener;

	private BoostExpandableListView		mList;
	private ExpandableListAdapter		wrapped;


	public BaseBoostExpandableAdapter(	Context context,
										BoostExpandableListView expandableListView,
										ExpandableListAdapter adapterToWrap){


		mList = expandableListView;
		wrapped = adapterToWrap;
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
					mCheckListener.onGroupCheckChange(	buttonView,
														mBoxData.getInt(GROUP_POSITION),
														mBoxData.getLong(ID),
														isChecked);


			}else

			// This distinguishes between getView checking/unchecking
			// and a real check event
			if (isChecked != mList.isChildChecked(	mBoxData.getInt(GROUP_POSITION),
													mBoxData.getInt(CHILD_POSITION)))
				mCheckListener.onChildCheckChange(	buttonView,
													mBoxData.getInt(GROUP_POSITION),
													mBoxData.getInt(CHILD_POSITION),
													mBoxData.getLong(ID),
													isChecked);
		}else
			Log.e(	TAG,
					"onCheckedChanged buttonView.getTag was null.");
	}


	@Override
	public View getChildView(int groupPosition,
								int childPosition,
								boolean isLastChild,
								View convertView,
								ViewGroup parent){


		convertView = wrapped.getChildView(	groupPosition,
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
					Log.e(	TAG,
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
						Log.e(	TAG,
								"Couldn't find android.R.id.checkbox in convertView during getChildView. "
									+
									"Make sure each child item has a checkbox with the id @android:id/checkbox.");
					mHolder.mBox.setOnCheckedChangeListener(this);
					mHolder.mBoxData = new Bundle();
				}
			}

			mHolder.mBoxData.putBoolean(IS_CHECKED,
										mList.isChildChecked(	groupPosition,
																childPosition));
			mHolder.mBox.setChecked(mHolder.mBoxData.getBoolean(IS_CHECKED));
			mHolder.mBoxData.putBoolean(IS_GROUP,
										false);
			mHolder.mBoxData.putInt(GROUP_POSITION,
									groupPosition);
			mHolder.mBoxData.putInt(CHILD_POSITION,
									childPosition);
			mHolder.mBoxData.putLong(	ID,
										getChildId(	groupPosition,
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

		convertView = wrapped.getGroupView(	groupPosition,
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
					Log.e(	TAG,
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
						Log.e(	TAG,
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
			mHolder.mBoxData.putLong(	ID,
										getGroupId(groupPosition));

			convertView.setTag(mHolder);
			mHolder.mBox.setTag(mHolder.mBoxData);

		}

		return convertView;
	}


	/**
	 * Don't use this to monitor check changes. Instead, register your listener
	 * on your {@link BoostExpandableListView}. This is used by
	 * {@link BoostExpandableListView} internally.
	 * 
	 * @param mListener
	 */
	protected void
		setExpandableCheckListener(ExpandableListCheckListener mListener){

		mCheckListener = mListener;
	}


	@Override
	public ExpandableListAdapter getWrappedAdapter(){

		return wrapped;
	}


	public boolean areAllItemsEnabled(){

		return wrapped.areAllItemsEnabled();
	}


	public Object getChild(int groupPosition, int childPosition){

		return wrapped.getChild(groupPosition,
								childPosition);
	}


	public long getChildId(int groupPosition, int childPosition){

		return wrapped.getChildId(	groupPosition,
									childPosition);
	}


	public int getChildrenCount(int groupPosition){

		return wrapped.getChildrenCount(groupPosition);
	}


	public long getCombinedChildId(long groupId, long childId){

		return wrapped.getCombinedChildId(	groupId,
											childId);
	}


	public long getCombinedGroupId(long groupId){

		return wrapped.getCombinedGroupId(groupId);
	}


	public Object getGroup(int groupPosition){

		return wrapped.getGroup(groupPosition);
	}


	public int getGroupCount(){

		return wrapped.getGroupCount();
	}


	public long getGroupId(int groupPosition){

		return wrapped.getGroupId(groupPosition);
	}


	public boolean hasStableIds(){

		return wrapped.hasStableIds();
	}


	public boolean isChildSelectable(int groupPosition, int childPosition){

		return wrapped.isChildSelectable(	groupPosition,
											childPosition);
	}


	public boolean isEmpty(){

		return wrapped.isEmpty();
	}


	public void onGroupCollapsed(int groupPosition){

		wrapped.onGroupCollapsed(groupPosition);
	}


	public void onGroupExpanded(int groupPosition){

		wrapped.onGroupExpanded(groupPosition);
	}


	public void registerDataSetObserver(DataSetObserver observer){

		wrapped.registerDataSetObserver(observer);
	}


	public void unregisterDataSetObserver(DataSetObserver observer){

		wrapped.unregisterDataSetObserver(observer);
	}


}
