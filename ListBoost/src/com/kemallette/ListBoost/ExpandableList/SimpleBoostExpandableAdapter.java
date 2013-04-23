package com.kemallette.ListBoost.ExpandableList;


import java.util.List;
import java.util.Map;

import android.content.Context;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;


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

		this(	context,
				expandableListView,
				groupData,
				collapsedGroupLayout,
				groupFrom,
				groupTo,
				childData,
				childLayout,
				childFrom,
				childTo);

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

		this(	context,
				expandableListView,
				groupData,
				collapsedGroupLayout,
				groupFrom,
				groupTo,
				childData,
				childLayout,
				childFrom,
				childTo);


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
				new SimpleExpandableListAdapter(context,
												groupData,
												groupLayout,
												groupFrom,
												groupTo,
												childData,
												childLayout,
												childFrom,
												childTo));
	}


}
