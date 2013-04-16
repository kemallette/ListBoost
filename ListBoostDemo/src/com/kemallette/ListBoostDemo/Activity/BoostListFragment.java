package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoostDemo.R;


public class BoostListFragment	extends
								SherlockFragment{

	private BoostListView	mList;


	public BoostListFragment(){

		super();
	}


	public static BoostListFragment newInstance(Bundle args){

		BoostListFragment mFrag = new BoostListFragment();
		mFrag.setArguments(args);

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.boost_list_frag,
								container,
								true);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		init();
	}


	private void init(){

		Bundle args = getArguments();

		if (args != null
			&& !args.isEmpty()) {
			
		}

			mList = (BoostListView) getView().findViewById(R.id.list);
	}

}
