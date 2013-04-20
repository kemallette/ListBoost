package com.kemallette.ListBoost.List;


import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class BoostListView	extends
							ListView{

	private static final String	TAG	= "BoostListView";
	private BoostAdapter		adapter;


	public BoostListView(Context context){

		super(context);
	}


	public BoostListView(	Context context,
							AttributeSet attrs){

		super(	context,
				attrs);
	}


	public BoostListView(	Context context,
							AttributeSet attrs,
							int defStyle){

		super(	context,
				attrs,
				defStyle);
	}


	public void setAdapter(BoostAdapter adapter){

		this.adapter = adapter;

		super.setAdapter(adapter);
	}


	/**
	 * Collapses the currently open view.
	 * 
	 * @return true if a view was collapsed, false if there was no open view.
	 */
	public boolean collapse(){

		if (adapter != null)
			return adapter.collapseLastOpen();
		return false;
	}


	/**
	 * Registers a OnItemClickListener for this listview which will expand the
	 * item by default. Any other OnItemClickListener will be overriden.
	 * 
	 * To undo call setOnItemClickListener(null)
	 * 
	 * Important: This method call setOnItemClickListener, so the value will be
	 * reset
	 */
	public void enableExpandOnItemClick(){

		this.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView,
									View view,
									int i,
									long l){

				BoostAdapter adapter =
										(BoostAdapter) getAdapter();
				adapter.getExpandToggleButton(view)
						.performClick();
			}
		});
	}


	@Override
	public Parcelable onSaveInstanceState(){

		if (adapter != null)
			return adapter.onSaveInstanceState(super.onSaveInstanceState());
		else
			return super.onSaveInstanceState();
	}


	@Override
	public void onRestoreInstanceState(Parcelable state){

		if (!(state instanceof BaseBoostAdapter.SavedState)){
			super.onRestoreInstanceState(state);
			return;
		}

		BaseBoostAdapter.SavedState ss =
											(BaseBoostAdapter.SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		if (adapter != null)
			adapter.onRestoreInstanceState(ss);
	}
}
