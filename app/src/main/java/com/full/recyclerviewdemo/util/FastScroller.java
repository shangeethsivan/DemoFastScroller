package com.full.recyclerviewdemo.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.full.recyclerviewdemo.R;

/**
 * Created by Shangeeth Sivan on 25/05/17.
 */

public class FastScroller extends LinearLayout {

    private static final String TAG = "FastScroller";
    private View mHandleIV;
    private View mBubbleTV;

    private int mHeight;
    private RecyclerView mRecyclerView;
    private final ScrollListener mScrollListener = new ScrollListener();

    private static final String SCALE_X = "scaleX";
    private static final String SCALE_Y = "scaleY";
    private static final String ALPHA = "alpha";
    private static final int HANDLE_ANIMATION_DURATION = 100;

    private static final int HANDLE_HIDE_DELAY = 300;
    private AnimatorSet mCurrentAnimatorSet;

    private BubbleHandler mBubbleHandler = new BubbleHandler();
    private boolean mIsHandleMovedManually = false;

    public FastScroller(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context pContext) {
        setOrientation(HORIZONTAL);
        setClipChildren(false);
        LayoutInflater lLayoutInflater = LayoutInflater.from(pContext);
        lLayoutInflater.inflate(R.layout.scroll_element, this);

        mHandleIV = findViewById(R.id.handle_iv);
        mBubbleTV = findViewById(R.id.bubble_tv);
    }

    /**
     * Shows Bubble while clicking the data
     */
    public void showBubble() {

        AnimatorSet lAnimatorSet = new AnimatorSet();
        mBubbleTV.setPivotX(mBubbleTV.getWidth());
        mBubbleTV.setPivotY(mBubbleTV.getHeight());
        mBubbleTV.setVisibility(VISIBLE);

        Animator lGrowerX = ObjectAnimator.ofFloat(mBubbleTV, SCALE_X, 0f, 1f).setDuration(HANDLE_ANIMATION_DURATION);
        Animator lGrowerY = ObjectAnimator.ofFloat(mBubbleTV, SCALE_Y, 0f, 1f).setDuration(HANDLE_ANIMATION_DURATION);
        Animator lAlpha = ObjectAnimator.ofFloat(mBubbleTV, ALPHA, 0f, 1f).setDuration(HANDLE_ANIMATION_DURATION);
        lAnimatorSet.playTogether(lGrowerX, lGrowerY, lAlpha);
        lAnimatorSet.start();

    }

    /**
     * Hides the Bubble while the scroller is left
     */
    public void hideBubble() {

        mCurrentAnimatorSet = new AnimatorSet();
        mBubbleTV.setPivotX(mBubbleTV.getWidth());
        mBubbleTV.setPivotY(mBubbleTV.getHeight());

        Animator lShrinkX = ObjectAnimator.ofFloat(mBubbleTV, SCALE_X, 1f, 0f).setDuration(HANDLE_ANIMATION_DURATION);
        Animator lShrinkY = ObjectAnimator.ofFloat(mBubbleTV, SCALE_Y, 1f, 0f).setDuration(HANDLE_ANIMATION_DURATION);
        Animator lAlpha = ObjectAnimator.ofFloat(mBubbleTV, ALPHA, 1f, 0f).setDuration(HANDLE_ANIMATION_DURATION);
        mCurrentAnimatorSet.playTogether(lShrinkX, lShrinkY, lAlpha);
        mCurrentAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBubbleTV.setVisibility(INVISIBLE);
                mCurrentAnimatorSet = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mBubbleTV.setVisibility(INVISIBLE);
                mCurrentAnimatorSet = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mCurrentAnimatorSet.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        Log.d(TAG, "onSizeChanged: " + mHeight);
    }


    public void setPosition(float y) {
        float position = y / mHeight;
        int handleHeight = mHandleIV.getHeight();
        Log.d(TAG, "setPosition: " + " y:" + y + "   position:" + position + "     handleHeight:" + handleHeight);

        mHandleIV.setY(getValueInRange(0, mHeight - handleHeight, (int) ((mHeight - handleHeight) * position)));

        Log.d(TAG, "setPosition: " + getValueInRange(0, mHeight - handleHeight, (int) ((mHeight - handleHeight) * position)));

        int lBubbleHeight = mBubbleTV.getHeight();
        mBubbleTV.setY(getValueInRange(0, mHeight - lBubbleHeight, (int) ((mHeight - lBubbleHeight) * position)));
        Log.d(TAG, "setPosition: " + getValueInRange(0, mHeight - lBubbleHeight, (int) ((mHeight - lBubbleHeight) * position)));

        setTextForBubble(mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0)));
    }

    private int getValueInRange(int min, int max, int value) {
        int maxOfMinAndValue = Math.max(min, value);
        return Math.min(maxOfMinAndValue, max);
    }

    public void setRecyclerView(RecyclerView pRecyclerView) {
        mRecyclerView = pRecyclerView;
        pRecyclerView.addOnScrollListener(mScrollListener);
    }
//
//    private class ScrollListener extends RecyclerView.OnScrollChangeListener{
//
//        @Override
//        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//        }
//    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            Log.d(TAG, "onScrolled: " + dx + " " + dy);
            View lFirstVisibleView = recyclerView.getChildAt(0);
            int lFirstViewPosition = recyclerView.getChildLayoutPosition(lFirstVisibleView);
            int lVisibleRange = recyclerView.getChildCount();
            int lLastVisiblePosition = lFirstViewPosition + lVisibleRange;
            int lItemCount = recyclerView.getAdapter().getItemCount();
            int position;
            position = 0;
//            else if (lFirstViewPosition>= lItemCount/2)
//                position = lLastVisiblePosition;
////            else if (lLastVisiblePosition >= lItemCount - 1)
////                position = lItemCount - 1;
//            else
            position = lFirstViewPosition;

            float lProportion = (float) position / (float) (lItemCount - lVisibleRange);
            Log.d(TAG, "onScrolled: " + lProportion * mHeight + " " + mHeight + " " + position + " " + lItemCount);

            if(!mIsHandleMovedManually)
                setPosition(mHeight * lProportion);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            mIsHandleMovedManually = true;
            setPosition(event.getY());
            Log.d(TAG, "onTouchEvent: " + event.getY());
            if (mCurrentAnimatorSet != null)
                mCurrentAnimatorSet.cancel();

            getHandler().removeCallbacks(mBubbleHandler);
            if (mBubbleTV.getVisibility() == INVISIBLE)
                showBubble();

            setRecyclerViewPosition(event.getY());
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            mIsHandleMovedManually = false;
            getHandler().postDelayed(mBubbleHandler, HANDLE_HIDE_DELAY);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void setTextForBubble(int pPosition) {
        ((TextView) mBubbleTV).setText((String) ((SectionIndexer) mRecyclerView.getAdapter()).getSections()[pPosition]);
    }


    private class BubbleHandler implements Runnable {

        @Override
        public void run() {
            hideBubble();
        }

    }

    private void setRecyclerViewPosition(float y) {

        if (mRecyclerView != null) {
            int lItemCount = mRecyclerView.getAdapter().getItemCount();
            float lProportion;
            if (mHandleIV.getY() == 0)
                lProportion = 0f;
            else if (mHandleIV.getY() + mHandleIV.getHeight() >= mHeight - 1)
                lProportion = 1f;
            else
                lProportion = y / (float) mHeight;

            int lTargetPos = getValueInRange(0, lItemCount - 1, (int) (lProportion * (float) lItemCount));

            Log.d(TAG, "setRecyclerViewPosition: " + lTargetPos);
            mRecyclerView.scrollToPosition(lTargetPos);
        }

    }
}
