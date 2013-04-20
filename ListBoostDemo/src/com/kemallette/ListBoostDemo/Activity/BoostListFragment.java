package com.kemallette.ListBoostDemo.Activity;


import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostAdapter;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ActivityUtil.Callbacks;


public class BoostListFragment	extends
								SherlockFragment implements
												Callbacks{

	private ArrayList<String>	mItems;
	private BoostListView		mList;
	private BoostAdapter		mAdapter;


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

		Collections.addAll(	mItems,
							getActivity().getResources()
											.getStringArray(R.array.list_examples));

		mList = (BoostListView) getView().findViewById(R.id.list);

		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
																	getActivity(),
																	android.R.layout.simple_list_item_activated_1,
																	android.R.id.text1,
																	mItems);

		BoostAdapter mBoostAdapter = new BoostAdapter(	mAdapter,
														null,
														null);
		mList.setAdapter(mBoostAdapter);
	}


	@Override
	public void enableSwipe(){

		if (mAdapter != null){

			// mAdapter.enableSwipe();
		}

	}


	@Override
	public void disableSwipe(){


	}


	@Override
	public void enableSliding(){

		if (mAdapter != null){
			// mAdapter.enableSlidingMenu();
		}
	}


	@Override
	public void disableSliding(){


	}


	@Override
	public void enableMultiSelect(){


	}


	@Override
	public void disableMultiSelect(){


	}


	@Override
	public void enableDragSort(){


	}


	@Override
	public void disableDragSort(){


	}


}
