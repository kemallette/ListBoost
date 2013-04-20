package com.kemallette.ListBoost.List;


import java.util.BitSet;

import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.WrapperListAdapter;

import com.kemallette.ListBoost.R;
import com.kemallette.ListBoost.Util.ExpandCollapseAnimation;

public abstract class BaseBoostAdapter	extends
										BaseAdapter	implements
													WrapperListAdapter{

	private static final String					TAG					= "BaseBoostAdapter";


	protected boolean							isSlidingEnabled;
	protected boolean							isSwipeRevealEnabled;
	protected boolean							isDragSortEnabled;
	protected boolean							isMultipleChoiceEnabled;

	/**
	 * The position of the last expanded list item. If -1 there is no list item
	 * expanded. Otherwise it points to the position of the last expanded list
	 * item
	 */
	private int									lastOpenPosition	= -1;

	protected int								slideToggleId		= R.id.slide_toggle_button;
	protected int								slidingViewId		= R.id.sliding_view_id;
	protected int[]								slidingViewButtonIds;
	protected OnSlidingMenuItemClickListener	mActionListener;


	/**
	 * Reference to the last expanded list item. Since lists are recycled this
	 * might be null if though there is an expanded list item
	 */
	private View								lastOpen			= null;

	/**
	 * A list of positions of all list items that are expanded. Normally only
	 * one is expanded. But a mode to expand multiple will be added soon.
	 * 
	 * If an item on position x is open, its bit is set
	 */
	private BitSet								openItems			= new BitSet();

	/**
	 * We remember, for each collapsable view its height. So we dont need to
	 * recalculate. The height is calculated just before the view is drawn.
	 */
	private final SparseIntArray				viewHeights			= new SparseIntArray(10);

	private ViewGroup							parent;

	protected final BaseAdapter					wrapped;


	public BaseBoostAdapter(BaseAdapter wrapped){

		super();

		this.wrapped = wrapped;
	}


	@Override
	public View
		getView(int position, View convertView, ViewGroup viewGroup){

		this.parent = viewGroup;

		convertView = wrapped.getView(	position,
										convertView,
										viewGroup);

		if (isSlidingEnabled
			&& convertView != null)
			setupSlidingMenu(	convertView,
								position);

		return convertView;
	}


	public void
		enableSlidingMenu(OnSlidingMenuItemClickListener mActionListener,
							int slideToggleId,
							int slidingLayoutId,
							int... slidingViewButtonIds){

		isSlidingEnabled = true;

		this.mActionListener = mActionListener;
		this.slideToggleId = slideToggleId;
		this.slidingViewId = slidingLayoutId;
		this.slidingViewButtonIds = slidingViewButtonIds;


	}


	public void
		enableSlidingMenu(OnSlidingMenuItemClickListener mActionListener,
							int... slidingViewButtonIds){

		enableSlidingMenu(	mActionListener,
							R.id.slide_toggle_button,
							R.id.sliding_view_id,
							slidingViewButtonIds);
	}


	public void disableSlidingMenu(){

		isSlidingEnabled = false;
	}


	public void enableSwipeToReveal(){

	}


	public void disableSwipeToReveal(){

	}


	public void enableMultipleChoice(){

	}


	public void disableMultipleChoice(){

	}


	public void enableDragSort(){

	}


	public void disableDragSort(){

	}


	/**
	 * This method is used to get the Button view that should expand or collapse
	 * the Expandable View. <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expand_toggle_button)
	 * </pre>
	 * 
	 * A listener will be attached to the button which will either expand or
	 * collapse the expandable view
	 * 
	 * @see #getExpandableView(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a button
	 */
	public View getExpandToggleButton(View parent){

		return parent.findViewById(slideToggleId);
	}


	/**
	 * This method is used to get the view that will be hidden initially and
	 * expands or collapse when the ExpandToggleButton is pressed @see
	 * getExpandToggleButton <br/>
	 * Normally it will be implemented as:
	 * 
	 * <pre>
	 * return parent.findViewById(R.id.expandable)
	 * </pre>
	 * 
	 * @see #getExpandToggleButton(View)
	 * @param parent
	 *            the list view item
	 * @ensure return!=null
	 * @return a child of parent which is a view (or often ViewGroup) that can
	 *         be collapsed and expanded
	 */
	public View getExpandableView(View parent){

		return parent.findViewById(slidingViewId);
	}


	/**
	 * Gets the duration of the collapse animation in ms. Default is 330ms.
	 * Override this method to change the default.
	 * 
	 * @return the duration of the anim in ms
	 */
	protected int getAnimationDuration(){

		return 330;
	}


	protected void setupSlidingMenu(final View convertView, final int position){

		View more = getExpandToggleButton(convertView);
		View itemToolbar = getExpandableView(convertView);
		itemToolbar.measure(parent.getWidth(),
							parent.getHeight());

		setupSlidingMenu(	more,
							itemToolbar,
							position);

		// add the action listeners
		if (slidingViewButtonIds != null
			&& convertView != null)
			for (int id : slidingViewButtonIds){
				View buttonView = convertView.findViewById(id);
				if (buttonView != null)
					buttonView.findViewById(id)
								.setOnClickListener(new OnClickListener(){

									@Override
									public void
										onClick(View view){

										if (mActionListener != null)
											mActionListener.onSlideItemClick(	convertView,
																				view,
																				position);
									}
								});
			}
	}


	private void setupSlidingMenu(final View button,
									final View target,
									final int position){

		if (target == lastOpen
			&& position != lastOpenPosition)
			// lastOpen is recycled, so its reference is false
			lastOpen = null;
		if (position == lastOpenPosition)
			// re reference to the last view
			// so when can animate it when collapsed
			lastOpen = target;
		int height = viewHeights.get(	position,
										-1);
		if (height == -1){
			viewHeights.put(position,
							target.getMeasuredHeight());
			updateExpandable(	target,
								position);
		}else
			updateExpandable(	target,
								position);

		button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view){

				view.setAnimation(null);
				// check wether we need to expand or collapse
				int type =
							target.getVisibility() == View.VISIBLE
																	? ExpandCollapseAnimation.COLLAPSE
																	: ExpandCollapseAnimation.EXPAND;

				// remember the state
				if (type == ExpandCollapseAnimation.EXPAND)
					openItems.set(	position,
									true);
				else
					openItems.set(	position,
									false);
				// check if we need to collapse a different view
				if (type == ExpandCollapseAnimation.EXPAND){
					if (lastOpenPosition != -1
						&& lastOpenPosition != position){
						if (lastOpen != null)
							animateView(lastOpen,
										ExpandCollapseAnimation.COLLAPSE);
						openItems.set(	lastOpenPosition,
										false);
					}
					lastOpen = target;
					lastOpenPosition = position;
				}else if (lastOpenPosition == position)
					lastOpenPosition = -1;

				animateView(target,
							type);
			}
		});
	}


	private void updateExpandable(View target, int position){

		final LinearLayout.LayoutParams params =
													(LinearLayout.LayoutParams) target.getLayoutParams();
		if (openItems.get(position)){
			target.setVisibility(View.VISIBLE);
			params.bottomMargin = 0;
		}else{
			target.setVisibility(View.GONE);
			params.bottomMargin = 0 - viewHeights.get(position);
		}
	}


	/**
	 * Performs either COLLAPSE or EXPAND animation on the target view
	 * 
	 * @param target
	 *            the view to animate
	 * @param type
	 *            the animation type, either ExpandCollapseAnimation.COLLAPSE or
	 *            ExpandCollapseAnimation.EXPAND
	 */
	private void animateView(final View target, final int type){


		Animation anim = new ExpandCollapseAnimation(
														target,
														type
							);
		anim.setDuration(getAnimationDuration());
		anim.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation){

			}


			@Override
			public void onAnimationRepeat(Animation animation){

			}


			@Override
			public void onAnimationEnd(Animation animation){

				if (type == ExpandCollapseAnimation.EXPAND)
					if (parent instanceof ListView){
						ListView listView = (ListView) parent;
						int movement = target.getBottom();
						Rect r = new Rect();
						boolean visible = target.getGlobalVisibleRect(r);
						Rect r2 = new Rect();
						listView.getGlobalVisibleRect(r2);
						if (!visible)
							listView.smoothScrollBy(movement,
													1000);
						else if (r2.bottom == r.bottom)
							listView.smoothScrollBy(movement,
													1000);
					}

			}
		});
		target.startAnimation(anim);

	}


	/**
	 * Closes the current open item. If it is current visible it will be closed
	 * with an animation.
	 * 
	 * @return true if an item was closed, false otherwise
	 */
	public boolean collapseLastOpen(){

		if (lastOpenPosition != -1){
			// if visible animate it out
			if (lastOpen != null)
				animateView(lastOpen,
							ExpandCollapseAnimation.COLLAPSE);
			openItems.set(	lastOpenPosition,
							false);
			lastOpenPosition = -1;
			return true;
		}
		return false;
	}


	public Parcelable onSaveInstanceState(Parcelable parcelable){

		SavedState ss = new SavedState(parcelable);
		ss.lastOpenPosition = this.lastOpenPosition;
		ss.openItems = this.openItems;
		return ss;
	}


	public void onRestoreInstanceState(SavedState state){

		this.lastOpenPosition = state.lastOpenPosition;
		this.openItems = state.openItems;
	}


	/**
	 * Utility methods to read and write a bitset from and to a Parcel
	 */
	private static BitSet readBitSet(Parcel src){

		int cardinality = src.readInt();

		BitSet set = new BitSet();
		for (int i = 0; i < cardinality; i++)
			set.set(src.readInt());

		return set;
	}


	private static void writeBitSet(Parcel dest, BitSet set){

		int nextSetBit = -1;

		if (dest != null
			&& set != null)
			dest.writeInt(set.cardinality());

		while ((nextSetBit = set.nextSetBit(nextSetBit + 1)) != -1)
			dest.writeInt(nextSetBit);
	}

	/**
	 * The actual state class
	 */
	static class SavedState	extends
							View.BaseSavedState{

		public BitSet	openItems			= null;
		public int		lastOpenPosition	= -1;


		SavedState(Parcelable superState){

			super(superState);
		}


		private SavedState(Parcel in){

			super(in);
			in.writeInt(lastOpenPosition);
			writeBitSet(in,
						openItems);
		}


		@Override
		public void writeToParcel(Parcel out, int flags){

			super.writeToParcel(out,
								flags);
			lastOpenPosition = out.readInt();
			openItems = readBitSet(out);
		}

		// required field that makes Parcelables from a Parcel
		public static final Parcelable.Creator<SavedState>	CREATOR	=
																		new Parcelable.Creator<SavedState>(){

																			@Override
																			public SavedState
																				createFromParcel(Parcel in){

																				return new SavedState(in);
																			}


																			@Override
																			public SavedState[]
																				newArray(int size){

																				return new SavedState[size];
																			}
																		};
	}


	/***********************************************************
	 * 
	 * Wrapped adapter delegates
	 * 
	 ************************************************************/
	@Override
	public ListAdapter getWrappedAdapter(){

		return wrapped;
	}


	@Override
	public boolean areAllItemsEnabled(){

		return wrapped.areAllItemsEnabled();
	}


	@Override
	public boolean isEnabled(int i){

		return wrapped.isEnabled(i);
	}


	@Override
	public void registerDataSetObserver(DataSetObserver dataSetObserver){

		wrapped.registerDataSetObserver(dataSetObserver);
	}


	@Override
	public void unregisterDataSetObserver(DataSetObserver dataSetObserver){

		wrapped.unregisterDataSetObserver(dataSetObserver);
	}


	@Override
	public int getCount(){

		return wrapped.getCount();
	}


	@Override
	public Object getItem(int i){

		return wrapped.getItem(i);
	}


	@Override
	public long getItemId(int i){

		return wrapped.getItemId(i);
	}


	@Override
	public boolean hasStableIds(){

		return wrapped.hasStableIds();
	}


	@Override
	public int getItemViewType(int i){

		return wrapped.getItemViewType(i);
	}


	@Override
	public int getViewTypeCount(){

		return wrapped.getViewTypeCount();
	}


	@Override
	public boolean isEmpty(){

		return wrapped.isEmpty();
	}


	@Override
	public void notifyDataSetChanged(){

		wrapped.notifyDataSetChanged();
	}


	@Override
	public void notifyDataSetInvalidated(){

		wrapped.notifyDataSetInvalidated();
	}
}
