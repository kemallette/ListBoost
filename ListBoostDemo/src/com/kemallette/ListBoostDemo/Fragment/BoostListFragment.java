package com.kemallette.ListBoostDemo.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostAdapter;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoost.List.OnSlidingMenuItemClickListener;
import com.kemallette.ListBoostDemo.R;


public class BoostListFragment	extends
								SherlockFragment implements
												OnSlidingMenuItemClickListener{

	private final int[]		mSlidingViewButtonIds	= new int[] {	R.id.b1,
						R.id.b2,
						R.id.b3,
						R.id.b4					};

	private BoostListView	mList;
	private BoostAdapter	mAdapter;


	public BoostListFragment(){

		super();
	}


	public static BoostListFragment newInstance(Bundle mFeatures){

		BoostListFragment mFrag = new BoostListFragment();

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.boost_list_frag,
								container,
								false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		init();

	}


	@Override
	public void onSlideItemClick(View itemView, View clickedView, int position){

		Toast.makeText(	getActivity(),
						((Button) clickedView).getText(),
						Toast.LENGTH_SHORT)
				.show();
	}


	private void init(){

		mList = (BoostListView) getView().findViewById(R.id.list);


		// mAdapter = new BoostDemoAdapter(mAdapterToBeWrapped);

		mList.setAdapter(mAdapter);

		if (getArguments() != null
			&& !getArguments().isEmpty()){

			// int listFeature =
			// getArguments().getInt(ActivityUtil.LIST_FEATURE);


		}

	}

	private class BoostDemoAdapter	extends
									BoostAdapter{

		private class Holder{

			CheckBox	mBox;
			Button		mSlideToggle;
			TextView	mText;
		}

		private Holder	mHolder;


		public BoostDemoAdapter(BaseAdapter wrapped){

			super(wrapped);
		}


		@Override
		public View
			getView(int position, View convertView, ViewGroup viewGroup){

			convertView = super.getView(position,
										convertView,
										viewGroup);

			if (convertView != null){

				mHolder = (Holder) convertView.getTag();
				if (mHolder == null){

					mHolder = new Holder();

					mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
					mHolder.mSlideToggle = (Button) convertView.findViewById(R.id.slide_toggle_button);
					mHolder.mText = (TextView) convertView.findViewById(android.R.id.text1);
				}

				mHolder.mText.setText(getItem(position).toString());

			}
			return convertView;
		}
	}

}
