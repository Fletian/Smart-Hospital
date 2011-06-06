package com.markupartist.android.widget;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.markupartist.android.widget.PullToRefreshListView.IReachedFinalItemInList;
import com.markupartist.android.widget.pulltorefresh.R;

public class PullToRefreshListView extends ListView implements OnScrollListener {

	private static final int TAP_TO_REFRESH = 1;
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;

	private static final String TAG = "PullToRefreshListView";
	private OnRefreshListener mOnRefreshListener;
	private OnScrollListener pullDownListener;
	private LayoutInflater mInflater;

	private LinearLayout mRefreshView;
	private TextView mRefreshViewText;
	private ImageView mRefreshViewImage;
	private ProgressBar mRefreshViewProgress;
	private TextView mRefreshViewLastUpdated;

	private int mCurrentScrollState;
	private int mRefreshState;

	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;

	private int mRefreshViewHeight;
	private int mRefreshOriginalTopPadding;
	private IReachedFinalItemInList finalItemInListListener;
	public View footerView;

	private boolean useFooter;
	private int pagingSize;
	
	private static final int DEFAULT_PAGING_SIZE=5;

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		mRefreshView = (LinearLayout) mInflater.inflate(
				R.layout.pull_to_refresh_header, null);

		mRefreshViewText =
			(TextView) mRefreshView.findViewById(R.id.pull_to_refresh_text);
		mRefreshViewImage =
			(ImageView) mRefreshView.findViewById(R.id.pull_to_refresh_image);
		mRefreshViewProgress =
			(ProgressBar) mRefreshView.findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewLastUpdated =
			(TextView) mRefreshView.findViewById(R.id.pull_to_refresh_updated_at);

		mRefreshViewImage.setMinimumHeight(50);
		mRefreshView.setOnClickListener(new OnClickRefreshListener());
		mRefreshOriginalTopPadding = mRefreshView.getPaddingTop();

		mRefreshState = TAP_TO_REFRESH;

		addHeaderView(mRefreshView);

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		footerView = layoutInflater.inflate(R.layout.list_view_footer, null);
		addFooterView(footerView);

		setOnScrollListener(this);

		useFooter = true;
		pagingSize = DEFAULT_PAGING_SIZE;

		measureView(mRefreshView);
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();
	}

	@Override
	protected void onAttachedToWindow() {
		setSelection(1);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

		setSelection(1);
	}

	/**
	 * Register a callback to be invoked when this list should be refreshed.
	 * 
	 * @param onRefreshListener The callback to run.
	 */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		mOnRefreshListener = onRefreshListener;
	}

	/**
	 * Set a text to represent when the list was last updated. 
	 * @param lastUpdated Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mRefreshViewLastUpdated.setVisibility(View.VISIBLE);
			mRefreshViewLastUpdated.setText(lastUpdated);
		} else {
			mRefreshViewLastUpdated.setVisibility(View.GONE);
		}
	}

	/**
	 * Smoothly scroll by distance pixels over duration milliseconds.
	 * 
	 * <p>Using reflection internally to call smoothScrollBy for API Level 8
	 * otherwise scrollBy is called.
	 * 
	 * @param distance Distance to scroll in pixels.
	 * @param duration Duration of the scroll animation in milliseconds.
	 */
	private void scrollListBy(int distance, int duration) {
		try {
			Method method = ListView.class.getMethod("smoothScrollBy",
					Integer.TYPE, Integer.TYPE);
			method.invoke(this, distance + 1, duration);
		} catch (NoSuchMethodException e) {
			// If smoothScrollBy is not available (< 2.2)
			setSelection(1);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			System.err.println("unexpected " + e);
		} catch (InvocationTargetException e) {
			System.err.println("unexpected " + e);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			if (getFirstVisiblePosition() == 0 && mRefreshState != REFRESHING) {
				if (mRefreshView.getBottom() > mRefreshViewHeight
						|| mRefreshView.getTop() >= 0
						&& mRefreshState == RELEASE_TO_REFRESH) {
					// Initiate the refresh
					mRefreshState = REFRESHING;
					prepareForRefresh();
					onRefresh();
				} else if (mRefreshView.getBottom() < mRefreshViewHeight) {
					// Abort refresh and scroll down below the refresh view
					resetHeader();
					setSelection(1);
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			applyHeaderPadding(event);
			break;
		}
		return super.onTouchEvent(event);
	}

	private void applyHeaderPadding(MotionEvent ev) {
		final int historySize = ev.getHistorySize();

		// Workaround for getPointerCount() which is unavailable in 1.5
		// (it's always 1 in 1.5)
		int pointerCount = 1;
		try {
			Method method = MotionEvent.class.getMethod("getPointerCount");
			pointerCount = (Integer)method.invoke(ev);
		} catch (NoSuchMethodException e) {
			pointerCount = 1;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (IllegalAccessException e) {
			System.err.println("unexpected " + e);
		} catch (InvocationTargetException e) {
			System.err.println("unexpected " + e);
		}

		for (int h = 0; h < historySize; h++) {
			for (int p = 0; p < pointerCount; p++) {
				if (mRefreshState == RELEASE_TO_REFRESH) {
					int topPadding = 0;
					try {
						// For Android > 2.0
						Method method = MotionEvent.class.getMethod(
								"getHistoricalY", Integer.TYPE, Integer.TYPE);
						topPadding = (int) (((Float) method.invoke(ev, p, h) / 2)
								- mRefreshViewHeight);
					} catch (NoSuchMethodException e) {
						// For Android < 2.0
						topPadding = (int) (ev.getHistoricalY(h) / 2)
						- mRefreshViewHeight;
					} catch (IllegalArgumentException e) {
						throw e;
					} catch (IllegalAccessException e) {
						System.err.println("unexpected " + e);
					} catch (InvocationTargetException e) {
						System.err.println("unexpected " + e);
					}
					mRefreshView.setPadding(
							mRefreshView.getPaddingLeft(),
							topPadding,
							mRefreshView.getPaddingRight(),
							mRefreshView.getPaddingBottom());
				}
			}
		}
	}

	/**
	 * Sets the header padding back to original size.
	 */
	private void resetHeaderPadding() {
		mRefreshView.setPadding(
				mRefreshView.getPaddingLeft(),
				mRefreshOriginalTopPadding,
				mRefreshView.getPaddingRight(),
				mRefreshView.getPaddingBottom());
	}

	/**
	 * Resets the header to the original state.
	 */
	private void resetHeader() {
		if (mRefreshState != TAP_TO_REFRESH) {
			mRefreshState = TAP_TO_REFRESH;

			resetHeaderPadding();

			// Set refresh view text to the pull label
			mRefreshViewText.setText(R.string.pull_to_refresh_tap_label);
			// Replace refresh drawable with arrow drawable
			mRefreshViewImage.setImageResource(R.drawable.ic_pulltorefresh_arrow);
			// Clear the full rotation animation
			mRefreshViewImage.clearAnimation();
			// Hide progress bar and arrow.
			mRefreshViewImage.setVisibility(View.GONE);
			mRefreshViewProgress.setVisibility(View.GONE);
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
				0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// When the refresh view is completely visible, change the text to say
		// "Release to refresh..." and flip the arrow drawable.
		/*footerView.setVisibility(View.GONE);
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
				&& mRefreshState != REFRESHING) {
			if (firstVisibleItem == 0) {
				mRefreshViewImage.setVisibility(View.VISIBLE);
				if ((mRefreshView.getBottom() > mRefreshViewHeight
						|| mRefreshView.getTop() >= 0)
						&& mRefreshState != RELEASE_TO_REFRESH) {
					mRefreshState = RELEASE_TO_REFRESH;
					mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mFlipAnimation);
				} else if (mRefreshView.getBottom() < mRefreshViewHeight
						&& mRefreshState != PULL_TO_REFRESH) {
					mRefreshState = PULL_TO_REFRESH;
					mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
					mRefreshViewImage.clearAnimation();
					mRefreshViewImage.startAnimation(mReverseFlipAnimation);
				}
			} else {
				mRefreshViewImage.setVisibility(View.GONE);
				resetHeader();
			}
		} else if (mCurrentScrollState == SCROLL_STATE_FLING
				&& firstVisibleItem == 0
				&& mRefreshState != REFRESHING) {
			setSelection(1);
		}

		if (useFooter){        
			boolean loadMore = (firstVisibleItem + visibleItemCount >= totalItemCount)
			&&(firstVisibleItem!=0&&visibleItemCount!=0&&totalItemCount!=0) && (totalItemCount > pagingSize);        
			if(loadMore) {
				footerView.setVisibility(View.VISIBLE);
				//finalItemInListListener.reachedFinalItem();
				
				
			} else {
				footerView.setVisibility(View.GONE);
			}
		}*/
		if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
                && mRefreshState != REFRESHING) {
            if (firstVisibleItem == 0) {
                mRefreshViewImage.setVisibility(View.VISIBLE);
                if ((mRefreshView.getBottom() > mRefreshViewHeight + 20
                        || mRefreshView.getTop() >= 0)
                        && mRefreshState != RELEASE_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_release_label);
                    mRefreshViewImage.clearAnimation();
                    mRefreshViewImage.startAnimation(mFlipAnimation);
                    mRefreshState = RELEASE_TO_REFRESH;
                } else if (mRefreshView.getBottom() < mRefreshViewHeight + 20
                        && mRefreshState != PULL_TO_REFRESH) {
                    mRefreshViewText.setText(R.string.pull_to_refresh_pull_label);
                    if (mRefreshState != TAP_TO_REFRESH) {
                        mRefreshViewImage.clearAnimation();
                        mRefreshViewImage.startAnimation(mReverseFlipAnimation);
                    }
                    mRefreshState = PULL_TO_REFRESH;
                }
            } else {
                mRefreshViewImage.setVisibility(View.GONE);
                resetHeader();
            }
        } else if (mCurrentScrollState == SCROLL_STATE_FLING
        		&& firstVisibleItem == 0
            	&& mRefreshState != REFRESHING) {
            setSelection(1);
        }

	if (pullDownListener != null) 
	    pullDownListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
    }
	

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mCurrentScrollState = scrollState;
	}

	public void prepareForRefresh() {
		resetHeaderPadding();

		mRefreshViewImage.setVisibility(View.GONE);
		// We need this hack, otherwise it will keep the previous drawable.
		mRefreshViewImage.setImageDrawable(null);
		mRefreshViewProgress.setVisibility(View.VISIBLE);

		// Set refresh view text to the refreshing label
		mRefreshViewText.setText(R.string.pull_to_refresh_refreshing_label);

		mRefreshState = REFRESHING;
	}

	public void onRefresh() {
		Log.d(TAG, "onRefresh");

		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh();
		}
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * @param lastUpdated Last updated at.
	 */
	public void onRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onRefreshComplete();
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 */
	public void onRefreshComplete() {        
		Log.d(TAG, "onRefreshComplete");

		resetHeader();

		// If refresh view is visible when loading completes, scroll down to
		// the next item.
		if (mRefreshView.getBottom() > 0) {
			invalidateViews();
			setSelection(1);
		}
	}

	/**
	 * Invoked when the refresh view is clicked on. This is mainly used when
	 * there's only a few items in the list and it's not possible to drag the
	 * list.
	 */
	private class OnClickRefreshListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mRefreshState != REFRESHING) {
				prepareForRefresh();
				onRefresh();
			}
		}

	}

	/**
	 * Interface definition for a callback to be invoked when list should be
	 * refreshed.
	 */
	public interface OnRefreshListener {
		/**
		 * Called when the list should be refreshed.
		 * <p>
		 * A call to {@link PullToRefreshListView #onRefreshComplete()} is
		 * expected to indicate that the refresh has completed.
		 */
		public void onRefresh();
	}


	public interface IReachedFinalItemInList{
		public void reachedFinalItem();
	}

	public void setFinalItemListener(IReachedFinalItemInList listener){
		finalItemInListListener = listener;
	}

	public void hideFooter(){
		useFooter = false;
		footerView.setVisibility(View.GONE);
	}
	
	public void setPagingSize(int pagingSize){
		this.pagingSize = pagingSize;
	}

}