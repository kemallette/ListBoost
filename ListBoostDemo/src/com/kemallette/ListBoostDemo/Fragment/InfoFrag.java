package com.kemallette.ListBoostDemo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kemallette.ListBoostDemo.R;


public class InfoFrag extends Fragment{

	private static final String	TAG	= "InfoFrag";

	private TextView	        startingInstructions, featureOptionsInstructions;


	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public InfoFrag(){

	}


	public static InfoFrag newInstance(){

		return new InfoFrag();
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState){

		final View fragView = inflater.inflate(R.layout.info_frag, container, false);

		startingInstructions = (TextView) fragView.findViewById(R.id.starting_instructions);
		featureOptionsInstructions = (TextView) fragView.findViewById(R.id.toggling_feature_options_instructions);

		return fragView;
	}


	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState){

		super.onViewCreated(view, savedInstanceState);

		populateInstructions();
	}


	private void populateInstructions(){

		startingInstructions.setText(Html.fromHtml(getResources().getString(R.string.starting_instructions)));
		featureOptionsInstructions.setText(Html.fromHtml(getResources().getString(R.string.toggling_feature_options_instructions)));
	}


}
