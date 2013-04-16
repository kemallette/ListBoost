package com.kemallette.ListBoost.List;


import android.content.Context;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kemallette.ListBoost.List.BoostListView.OnActionClickListener;


public class BoostCursorAdapter extends BaseBoostCursorAdapter{

	private static final String	TAG	= "BoostCursorAdapter";

	protected int	            toggleButtonId;
	protected int	            expandableViewId;

	int[]	                    expandableViewButtonIds;

	OnActionClickListener	    mActionListener;


	public BoostCursorAdapter(CursorAdapter toBeWrapped,
	                          Context context,
	                          OnActionClickListener mActionListener,
	                          int toggleButtonId,
	                          int expandableViewId,
	                          boolean autoRequery,
	                          int... expandableViewButtonIds){

		super(toBeWrapped,
		      context,
		      toBeWrapped.getCursor(),
		      autoRequery);

		this.mActionListener = mActionListener;
		this.toggleButtonId = toggleButtonId;
		this.expandableViewId = expandableViewId;
		this.expandableViewButtonIds = expandableViewButtonIds;
	}


	public BoostCursorAdapter(CursorAdapter toBeWrapped,
	                          Context context,
	                          OnActionClickListener mActionListener,
	                          int toggleButtonId,
	                          int expandableViewId,
	                          int flags,
	                          int... expandableViewButtonIds){

		super(toBeWrapped,
		      context,
		      toBeWrapped.getCursor(),
		      flags);

		this.mActionListener = mActionListener;
		this.toggleButtonId = toggleButtonId;
		this.expandableViewId = expandableViewId;
		this.expandableViewButtonIds = expandableViewButtonIds;
	}


	@Override
	public View getView(final int position,
	                    View convertView,
	                    ViewGroup viewGroup){

		final View mView = super.getView(position,
		                                 convertView,
		                                 viewGroup);
		// add the action listeners
		if (expandableViewButtonIds != null
		    && mView != null){
			for (int id : expandableViewButtonIds){
				View buttonView = mView.findViewById(id);
				if (buttonView != null){
					buttonView.findViewById(id)
					          .setOnClickListener(new OnClickListener(){

						          @Override
						          public void
						              onClick(View view){

							          if (mActionListener != null){
								          mActionListener.onClick(mView,
								                                  view,
								                                  position);
							          }
						          }
					          });
				}
			}
		}
		return mView;
	}


	@Override
	public View getExpandToggleButton(View parent){

		return parent.findViewById(toggleButtonId);
	}


	@Override
	public View getExpandableView(View parent){

		return parent.findViewById(expandableViewId);
	}

}
