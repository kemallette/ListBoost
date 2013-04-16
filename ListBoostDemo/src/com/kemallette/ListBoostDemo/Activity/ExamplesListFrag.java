package com.kemallette.ListBoostDemo.Activity;


import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostAdapter;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Task.Tasks;

public class ExamplesListFrag	extends
								SherlockFragment implements
												OnItemClickListener{

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks{

		public void onItemSelected(int position);
	}

	private Callbacks			mCallbacks;
	private ArrayList<String>	mItems;
	private BoostListView		mList;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ExamplesListFrag(){

	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.examples_list_frag,
								container,
								true);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		init();

	}


	@Override
	public void onAttach(Activity activity){

		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks))
			throw new IllegalStateException("Activity must implement fragment's callbacks.");

		mCallbacks = (Callbacks) activity;
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
	public void
		onItemClick(AdapterView<?> list, View item, int position, long id){

		mCallbacks.onItemSelected(position);
	}

}
