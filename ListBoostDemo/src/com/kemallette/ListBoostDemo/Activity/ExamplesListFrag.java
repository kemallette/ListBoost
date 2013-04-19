package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoostDemo.R;

public class ExamplesListFrag	extends
								SherlockFragment implements
												OnItemClickListener{

	private BoostListView	mList;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ExamplesListFrag(){

	}


	public static ExamplesListFrag newInstance(){

		ExamplesListFrag mFrag = new ExamplesListFrag();

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.examples_list_frag,
								container,
								false);
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
			&& !args.isEmpty()){

		}

		mList = (BoostListView) getView().findViewById(R.id.list);

		setAdapter();
	}


	private void setAdapter(){

		String[] mData = getResources().getStringArray(R.array.list_examples);

		ArrayAdapter<String> mBaseAdapter = new ArrayAdapter<String>(	getActivity(),
																		R.layout.simple_list_item,
																		android.R.id.text1,
																		mData);


		mList.setAdapter(mBaseAdapter);
		mList.setOnItemClickListener(this);
	}


	@Override
	public void
		onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3){

		ActivityUtil.launchBoostDemoActivity(	getActivity(),
												position);
	}

}
