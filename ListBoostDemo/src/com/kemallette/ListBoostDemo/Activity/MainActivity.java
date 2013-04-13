package com.kemallette.ListBoostDemo.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoostDemo.R;


public class MainActivity	extends
							Activity implements
									OnItemClickListener{

	private BoostListView	mList;


	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		initViews();
	}


	@Override
	public void
		onItemClick(AdapterView<?> list, View view, int position, long id){

		final Intent mIntent = new Intent();


	}


	/***********************************************************
	 * 
	 * Sets up views
	 * 
	 ************************************************************/
	private void initViews(){

		mList = (BoostListView) findViewById(R.id.list);
		mList.setOnItemClickListener(this);

	}
}
