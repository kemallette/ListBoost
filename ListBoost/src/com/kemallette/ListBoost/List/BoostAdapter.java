package com.kemallette.ListBoost.List;


import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kemallette.ListBoost.R;
import com.kemallette.ListBoost.List.BoostListView.OnActionClickListener;

/**
 * ListAdapter that adds sliding functionality to a list. Uses
 * R.id.expandalbe_toggle_button and R.id.expandable id's if no ids are given in
 * the contructor.
 */
public class BoostAdapter	extends
							AbstractBoostAdapter{

	private static final String	TAG					= "BoostAdapter";

	public static int			toggleButtonId		= R.id.expandable_toggle_button;
	public static int			expandableViewId	= R.id.expandable;

	int[]						expandableViewButtonIds;

	OnActionClickListener		mActionListener;


	public BoostAdapter(BaseAdapter wrapped,
						int toggleButtonId,
						int expandableViewId,
						OnActionClickListener mActionListener,
						int... expandableViewButtonIds){

		super(wrapped);

		BoostAdapter.toggleButtonId = toggleButtonId;
		BoostAdapter.toggleButtonId = expandableViewId;
		this.mActionListener = mActionListener;
		this.expandableViewButtonIds = expandableViewButtonIds;
	}


	public BoostAdapter(BaseAdapter wrapped,
						OnActionClickListener mActionListener,
						int... expandableViewButtonIds){

		this(	wrapped,
				R.id.expandable_toggle_button,
				R.id.expandable,
				mActionListener,
				expandableViewButtonIds);
	}


	@Override
	public View
		getView(final int position, View convertView, ViewGroup viewGroup){

		convertView = super.getView(position,
									convertView,
									viewGroup);

		// add the action listeners
		if (expandableViewButtonIds != null
			&& convertView != null){

			final View mListItemView = convertView;

			for (int id : expandableViewButtonIds){

				View buttonView = convertView.findViewById(id);

				if (buttonView != null)
					buttonView.findViewById(id)
								.setOnClickListener(new OnClickListener(){

									@Override
									public void onClick(View view){

										if (mActionListener != null)
											mActionListener.onClick(mListItemView,
																	view,
																	position);
									}
								});

			}

		}
		return convertView;
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
