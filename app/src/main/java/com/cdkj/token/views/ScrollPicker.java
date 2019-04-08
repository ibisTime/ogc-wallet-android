package com.cdkj.token.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动选择
 * Created by Administrator on 2016/6/28.
 */
public class ScrollPicker extends View {

    public interface ScrollPickerData {
        String getShowString();

        String getSelectType();
    }

    private int mMinTextSize = 24; // 最小的字体
    private int mMaxTextSize = 32; // 最大的字体
    // 字体渐变颜色
    private int mStartColor = Color.BLACK; // 中间选中ｉｔｅｍ的颜色
    private int mEndColor = Color.GRAY; // 上下两边的颜色
    private int mVisibleItemCount = 5; // 可见的item数量

    private boolean mIsInertiaScroll = true; // 快速滑动时是否惯性滚动一段距离，默认开启
    private boolean mIsCirculation = true; // 是否循环滚动，默认开启

    private Paint mPaint; //
    private int mMeasureWidth;
    private int mMeasureHeight;
    private int mSelected; // 当前选中的item下标，实际保持不变一直为中间位置的那个元素
    private List<ScrollPickerData> mData;
    private List<ScrollPickerData> mInitData;//初始化数据
    private int mItemHeight = 0; //每个条目的高度=mMeasureHeight／mVisibleItemCount
    private int mCenterY; //中间item的起始坐标y
    private float mLastMoveY; // 触摸的坐标y

    private float mMoveLength = 0; // item移动长度，负数表示向上移动，正数表示向下移动

    private GestureDetectorCompat mGestureDetector;
    private OnSelectedListener mListener;

    private Scroller mScroller;
    private boolean mIsFling; // 是否正在惯性滑动
    private boolean mIsMovingCenter; // 是否正在滑向中间
    // 可以把scroller看做模拟的触屏滑动操作，mLastScrollY为上次触屏滑动的坐标
    private int mLastScrollY = 0; // Scroller的坐标y


    private boolean scrollDestion = false;//false 向上滑动，true向下滑动

    public ScrollPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollPicker(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mGestureDetector = new GestureDetectorCompat(getContext(),
                new FlingOnGestureListener());
        mScroller = new Scroller(getContext());
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs,
                    R.styleable.ScrollPicker);
            mMinTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.ScrollPicker_min_text_size,
                    mMinTextSize);
            mMaxTextSize = typedArray.getDimensionPixelSize(
                    R.styleable.ScrollPicker_max_text_size,
                    mMaxTextSize);
            mStartColor = typedArray.getColor(
                    R.styleable.ScrollPicker_start_color, mStartColor);
            mEndColor = typedArray.getColor(
                    R.styleable.ScrollPicker_end_color, mEndColor);
            mVisibleItemCount = typedArray.getInt(
                    R.styleable.ScrollPicker_visible_item_count,
                    mVisibleItemCount);
            mIsCirculation = typedArray.getBoolean(R.styleable.ScrollPicker_is_circulation, true);
            typedArray.recycle();
        }
    }


    /**
     * 是否循环
     *
     * @param issCirculation
     */
    public void setIsCirculationt(boolean issCirculation) {
        this.mIsCirculation = issCirculation;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawItem(canvas, mSelected);
        int length = mVisibleItemCount / 2 + 1;
        for (int i = 1; i <= length && i <= mData.size() / 2; i++) {
            drawItem(canvas, mSelected - i);
            drawItem(canvas, mSelected + i);
        }
    }

    private void drawItem(Canvas canvas, int position) {
        int relative = position - mSelected;
        mPaint.setColor(mStartColor);

        if (position < 0 || position >= mData.size()) {
            return;
        }

        String text = mData.get(position).getShowString();
        float x = 0;
        if (relative == -1) { // 上一个
            if (mMoveLength < 0) { // 向上滑动
                mPaint.setTextSize(mMinTextSize);
            } else { // 向下滑动
                mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                        * mMoveLength / mItemHeight);
            }
        } else if (relative == 0) { // 中间item,当前选中
            mPaint.setColor(mEndColor);
            mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                    * (mItemHeight - Math.abs(mMoveLength)) / mItemHeight);
        } else if (relative == 1) { // 下一个
            if (mMoveLength > 0) { // 向下滑动
                mPaint.setTextSize(mMinTextSize);
            } else { // 向上滑动
                mPaint.setTextSize(mMinTextSize + (mMaxTextSize - mMinTextSize)
                        * -mMoveLength / mItemHeight);
            }
        } else { // 其他
            mPaint.setTextSize(mMinTextSize);
        }
        x = (mMeasureWidth - mPaint.measureText(text)) / 2;

        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        // 绘制文字时，文字的baseline是对齐ｙ坐标的，下面换算使其垂直居中。fmi.top值是相对baseline的，为负值
        float y = mCenterY + relative * mItemHeight + mItemHeight / 2
                - fmi.descent + (fmi.bottom - fmi.top) / 2;
//        computeColor(relative);
        canvas.drawText(text, x, y + mMoveLength, mPaint);
    }

    /**
     * 计算字体颜色，渐变
     *
     * @param relative 　相对中间item的位置
     */
    private void computeColor(int relative) {
        int color = mStartColor; // 　其他默认为ｍEndColor
        if (relative == -1 || relative == 1) { // 上一个或下一个
            // 处理上一个item且向上滑动　或者　处理下一个item且向下滑动　，颜色为mEndColor
            if ((relative == -1 && mMoveLength < 0)
                    || (relative == 1 && mMoveLength > 0)) {
                color = mStartColor;
            } else { // 计算渐变的颜色
                float rate = (mItemHeight - Math.abs(mMoveLength))
                        / mItemHeight;
                color = computeGradientColor(mStartColor, mEndColor, rate);
            }
        } else if (relative == 0) { // 中间item
            float rate = Math.abs(mMoveLength) / mItemHeight;
            color = computeGradientColor(mStartColor, mEndColor, rate);
        }

        mPaint.setColor(color);
    }

    private int computeGradientColor(int mStartColor, int mEndColor, float rate) {
        int dColor = mEndColor - mStartColor;
        return (int) (mStartColor + dColor * rate);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();
        mItemHeight = mMeasureHeight / mVisibleItemCount;
        mCenterY = mVisibleItemCount / 2 * mItemHeight;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 点击时取消所有滚动效果
                cancelScroll();
                mLastMoveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getY() - mLastMoveY) < 0.1f) {
                    return true;
                }
                mMoveLength += event.getY() - mLastMoveY;
                mLastMoveY = event.getY();
                checkCirculation();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mLastMoveY = event.getY();
                moveToCenter();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) { // 正在滚动
            // 可以把scroller看做模拟的触屏滑动操作，mLastScrollY为上次滑动的坐标
            mMoveLength = mMoveLength + mScroller.getCurrY() - mLastScrollY;
            mLastScrollY = mScroller.getCurrY();
            checkCirculation();
            invalidate();
        } else { // 滚动完毕
            if (mIsFling) {
                mIsFling = false;
                moveToCenter();
            } else if (mIsMovingCenter) {
                mIsMovingCenter = false;
                notifySelected();
            }
        }
    }

    public void cancelScroll() {
        mIsFling = mIsMovingCenter = false;
        mScroller.abortAnimation();
    }


    // 循环滚动
    private void checkCirculation() {

        if (mMoveLength >= mItemHeight) { //向下滑动,最后一个元素放在头部
            mData.add(0, mData.remove(mData.size() - 1));
            mMoveLength = 0;
        } else if (mMoveLength <= -mItemHeight) { // 向上滑动，第一个元素放在尾部
            mData.add(mData.remove(0));
            mMoveLength = 0;
        }
    }

    // 移动到中间位置
    private void moveToCenter() {
        if (!mScroller.isFinished() || mIsFling) {
            return;
        }
        cancelScroll();

        // 向下滑动
        if (mMoveLength > 0) {
            if (mMoveLength < mItemHeight / 2) {
                scroll(mMoveLength, 0);
            } else {
                scroll(mMoveLength, mItemHeight);
            }
        } else {
            if (-mMoveLength < mItemHeight / 2) {
                scroll(mMoveLength, 0);
            } else {
                scroll(mMoveLength, -mItemHeight);
            }
        }

    }

    // 平滑滚动
    private void scroll(float from, int to) {

        mLastScrollY = (int) from;
        mIsMovingCenter = true;
        mScroller.startScroll(0, (int) from, 0, 0);
        mScroller.setFinalY(to);
        invalidate();
    }

    // 惯性滑动，
    private void fling(float from, float vY) {
        mLastScrollY = (int) from;
        mIsFling = true;
        // 最多可以惯性滑动10个item
        mScroller.fling(0, (int) from, 0, (int) vY, 0, 0, -10
                * mItemHeight, 10 * mItemHeight);
        invalidate();
    }

    private void notifySelected() {
        if (mListener != null) {
            // 告诉监听器选择完毕
            post(new Runnable() {
                @Override
                public void run() {
                    mListener.onSelected(mData, mSelected);
                }
            });
        }
    }

    /**
     * 快速滑动时，惯性滑动一段距离
     *
     * @author huangziwei
     */
    private class FlingOnGestureListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               final float velocityY) {
            if (mIsInertiaScroll) {
                cancelScroll();
                fling(mMoveLength, velocityY);
            }
            return true;
        }
    }

    public List<ScrollPickerData> getData() {
        return mData;
    }

    public void setData(List<ScrollPickerData> data) {
        mData.clear();
        this.mData.addAll(data);
        mInitData = mData;
        mSelected = data.size() / 2;
        invalidate();
    }

    /**
     * @param startColor 正中间的颜色
     * @param endColor   上下两边的颜色
     */
    public void setColor(int startColor, int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        invalidate();
    }

    public void setMinTestSize(int size) {
        mMinTextSize = size;
        invalidate();
    }

    public void setMaxTestSize(int size) {
        mMaxTextSize = size;
        invalidate();
    }

    public ScrollPickerData getSelectedItem() {

        if (mSelected < -1 || mSelected >= mData.size()) {
            return null;
        }

        return mData.get(mSelected);
    }

    public int getSelectedPosition() {
        return mSelected;
    }

    public void setSelectedPosition(int position) {
        if (position < 0 || position > mInitData.size() - 1
                || position == mSelected) {
            return;
        }

        LogUtil.I("size : " + mInitData.size() + " mSelected : " + mSelected + " position : " + position);
        int count = Math.abs(mSelected - position);
        List<ScrollPickerData> list = new ArrayList<>();
        if (position < mSelected) {
            list.addAll(mInitData.subList(mInitData.size() - count, mInitData.size()));
            list.addAll(mInitData.subList(0, mInitData.size() - count));
        } else {
            list.addAll(mInitData.subList(count, mInitData.size()));
            list.addAll(mInitData.subList(0, count));
        }
        mData = list;
        invalidate();
        if (mListener != null) {
            mListener.onSelected(mData, mSelected);
        }
    }

    public boolean isScrollDestion() {
        return scrollDestion;
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        mListener = listener;
    }

    public OnSelectedListener getListener() {
        return mListener;
    }

    public boolean isInertiaScroll() {
        return mIsInertiaScroll;
    }

    public void setInertiaScroll(boolean inertiaScroll) {
        this.mIsInertiaScroll = inertiaScroll;
    }

    /**
     * @author huangziwei
     */
    public interface OnSelectedListener {
        void onSelected(List<ScrollPickerData> data, int position);
    }
}
