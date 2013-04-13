package com.ListBoost.BoostExpandableListView;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleExpandableListAdapter;


public abstract class AbstractBoostExpandableAdapter extends SimpleExpandableListAdapter implements BoostExpandable{

	private ArrayList<ExpandableListCheckListener>	mCheckListeners;


	public AbstractBoostExpandableAdapter(Context context,
	                                      List<? extends Map<String, ?>> groupData,
	                                      int expandedGroupLayout,
	                                      int collapsedGroupLayout,
	                                      String[] groupFrom,
	                                      int[] groupTo,
	                                      List<? extends List<? extends Map<String, ?>>> childData,
	                                      int childLayout,
	                                      int lastChildLayout,
	                                      String[] childFrom,
	                                      int[] childTo){

		super(context,
		      groupData,
		      expandedGroupLayout,
		      collapsedGroupLayout,
		      groupFrom,
		      groupTo,
		      childData,
		      childLayout,
		      lastChildLayout,
		      childFrom,
		      childTo);
		// TODO Auto-generated constructor stub
	}


	public AbstractBoostExpandableAdapter(Context context,
	                                      List<? extends Map<String, ?>> groupData,
	                                      int expandedGroupLayout,
	                                      int collapsedGroupLayout,
	                                      String[] groupFrom,
	                                      int[] groupTo,
	                                      List<? extends List<? extends Map<String, ?>>> childData,
	                                      int childLayout,
	                                      String[] childFrom,
	                                      int[] childTo){

		super(context,
		      groupData,
		      expandedGroupLayout,
		      collapsedGroupLayout,
		      groupFrom,
		      groupTo,
		      childData,
		      childLayout,
		      childFrom,
		      childTo);
		// TODO Auto-generated constructor stub
	}


	public AbstractBoostExpandableAdapter(Context context,
	                                      List<? extends Map<String, ?>> groupData,
	                                      int groupLayout,
	                                      String[] groupFrom,
	                                      int[] groupTo,
	                                      List<? extends List<? extends Map<String, ?>>> childData,
	                                      int childLayout,
	                                      String[] childFrom,
	                                      int[] childTo){

		super(context,
		      groupData,
		      groupLayout,
		      groupFrom,
		      groupTo,
		      childData,
		      childLayout,
		      childFrom,
		      childTo);
		// TODO Auto-generated constructor stub
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.SimpleExpandableListAdapter#getChildView(int,
	 * int, boolean, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getChildView(int groupPosition,
	                         int childPosition,
	                         boolean isLastChild,
	                         View convertView,
	                         ViewGroup parent){

		return super.getChildView(groupPosition,
		                          childPosition,
		                          isLastChild,
		                          convertView,
		                          parent);
	}


	@Override
	public View getGroupView(int groupPosition,
	                         boolean isExpanded,
	                         View convertView,
	                         ViewGroup parent){

		return super.getGroupView(groupPosition,
		                          isExpanded,
		                          convertView,
		                          parent);
	}

}
