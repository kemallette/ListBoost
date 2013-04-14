package com.kemallette.ListBoost.ExpandableList;


import java.util.List;
import java.util.Map;

import android.content.Context;


public class SimpleBoostExpandableAdapter	extends
											BaseBoostExpandableAdapter{

	public SimpleBoostExpandableAdapter(Context context,
										BoostExpandableListView expandableListView,
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

		super(	context,
				expandableListView,
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


	public SimpleBoostExpandableAdapter(Context context,
										BoostExpandableListView expandableListView,
										List<? extends Map<String, ?>> groupData,
										int expandedGroupLayout,
										int collapsedGroupLayout,
										String[] groupFrom,
										int[] groupTo,
										List<? extends List<? extends Map<String, ?>>> childData,
										int childLayout,
										String[] childFrom,
										int[] childTo){

		super(	context,
				expandableListView,
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


	public SimpleBoostExpandableAdapter(Context context,
										BoostExpandableListView expandableListView,
										List<? extends Map<String, ?>> groupData,
										int groupLayout,
										String[] groupFrom,
										int[] groupTo,
										List<? extends List<? extends Map<String, ?>>> childData,
										int childLayout,
										String[] childFrom,
										int[] childTo){

		super(	context,
				expandableListView,
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


}
