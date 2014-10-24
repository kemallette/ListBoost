package com.kemallette.ListBoostDemo.Adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Model.WorldCity;
import com.kemallette.ListBoostDemo.Util.SeedDataUtil;


public class ExampleAdapter	extends
							BaseExpandableListAdapter{

	private static class ExampleHolder{

		TextView	mTitle;
	}

	private ExampleHolder						mHolder;

	private LayoutInflater						inflater;


	private HashMap<Character, List<WorldCity>>	mData;
	private Context								ctx;


	public ExampleAdapter(Context ctx){

		this.ctx = ctx;

		mData = SeedDataUtil.getAphabetGroupedCities(	ctx,
														15);
	}


	@Override
	public int getGroupCount(){

		return mData.keySet().size();
	}


	@Override
	public int getChildrenCount(int groupPosition){

		return mData.get((char) ('a'
							+ groupPosition))
					.size();
	}


	@Override
	public Object getGroup(int groupPosition){

		return (char) ('a'
		+ groupPosition);
	}


	@Override
	public Object getChild(int groupPosition, int childPosition){

		return mData.get((char) ('a'
							+ groupPosition))
					.get(childPosition);
	}


	@Override
	public long getGroupId(int groupPosition){

		return (Character) getGroup(groupPosition);
	}


	@Override
	public long getChildId(int groupPosition, int childPosition){

		return mData.get((char) ('a'
							+ groupPosition))
					.get(childPosition)
					.getId();
	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
								View convertView, ViewGroup parent){

		if (convertView == null){

			if (inflater == null)
				inflater = LayoutInflater.from(ctx);

			convertView = inflater.inflate(	R.layout.expandable_list_group_item,
											null);
		}

		if (convertView.getTag() == null){
			mHolder = new ExampleHolder();

			mHolder.mTitle = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(mHolder);
		}else
			mHolder = (ExampleHolder) convertView.getTag();

		mHolder.mTitle.setText(getGroup(groupPosition).toString()
														.toUpperCase());

		return convertView;
	}


	@Override
	public View getChildView(final int groupPosition,
								final int childPosition,
								boolean isLastChild,
								View convertView,
								ViewGroup parent){

		if (convertView == null){

			if (inflater == null)
				inflater = LayoutInflater.from(ctx);

			convertView = inflater.inflate(	R.layout.expandable_list_child_item,
											null);
		}

		if (convertView.getTag() == null){
			mHolder = new ExampleHolder();

			mHolder.mTitle = (TextView) convertView.findViewById(R.id.title);

			convertView.setTag(mHolder);
		}else
			mHolder = (ExampleHolder) convertView.getTag();

		mHolder.mTitle.setText(getChild(groupPosition,
										childPosition).toString());
		return convertView;
	}


	@Override
	public boolean hasStableIds(){

		return true;
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition){

		return true;
	}

}
