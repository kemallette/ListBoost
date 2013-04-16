package com.kemallette.ListBoostDemo.Activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ExamplesListFrag.Callbacks;


public class ExamplesExpandableListFrag	extends
										SherlockFragment{

	private static final String	TAG	= "ExamplesExpandableListFrag";


	public ExamplesExpandableListFrag(){

		super();
	}


	public static ExamplesExpandableListFrag newInstance(){

		ExamplesExpandableListFrag mFrag = new ExamplesExpandableListFrag();

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.examples_expandable_list_frag,
								container,
								true);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

	}


	@Override
	public void onAttach(Activity activity){

		super.onAttach(activity);

	}
}
