package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kemallette.ListBoostDemo.R;


public class ExamplesListActivity	extends
									SherlockFragmentActivity{


	@Override
	protected void onCreate(Bundle arg0){

		super.onCreate(arg0);
		setContentView(R.layout.examples_list_activity);

		initListFrag();
	}


	private void initListFrag(){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		mTransaction.add(	R.id.container,
							ExamplesListFrag.newInstance());
		mTransaction.commit();
	}


}
