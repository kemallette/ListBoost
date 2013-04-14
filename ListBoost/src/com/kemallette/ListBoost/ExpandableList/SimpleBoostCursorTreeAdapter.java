package com.kemallette.ListBoost.ExpandableList;


import android.content.Context;
import android.database.Cursor;
import android.view.View;


public class SimpleBoostCursorTreeAdapter	extends
											BaseBoostCursorTreeAdapter{

	public SimpleBoostCursorTreeAdapter(Context context,
										BoostExpandableListView expandableListView,
										Cursor cursor,
										int collapsedGroupLayout,
										int expandedGroupLayout,
										int childLayout,
										int lastChildLayout){

		super(	context,
				expandableListView,
				cursor,
				collapsedGroupLayout,
				expandedGroupLayout,
				childLayout,
				lastChildLayout);
		// TODO Auto-generated constructor stub
	}


	public SimpleBoostCursorTreeAdapter(Context context,
										BoostExpandableListView expandableListView,
										Cursor cursor,
										int collapsedGroupLayout,
										int expandedGroupLayout,
										int childLayout){

		super(	context,
				expandableListView,
				cursor,
				collapsedGroupLayout,
				expandedGroupLayout,
				childLayout);
		// TODO Auto-generated constructor stub
	}


	public SimpleBoostCursorTreeAdapter(Context context,
										BoostExpandableListView expandableListView,
										Cursor cursor,
										int groupLayout,
										int childLayout){

		super(	context,
				expandableListView,
				cursor,
				groupLayout,
				childLayout);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Cursor getChildrenCursor(Cursor groupCursor){

		return null;
	}


	@Override
	protected void bindGroupView(View view,
									Context context,
									Cursor cursor,
									boolean isExpanded){


	}


	@Override
	protected void bindChildView(View view,
									Context context,
									Cursor cursor,
									boolean isLastChild){

	}
}
