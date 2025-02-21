
package com.zzt.popupwindows.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * @author: zeting
 * @date: 2021/6/21
 * PopupWindow 对View做蒙版指引
 */
public class ZGuidePopup extends ZBasePopup {
    private static final String TAG = ZGuidePopup.class.getSimpleName();


    private int mEdgeProtectionTop;
    private int mEdgeProtectionLeft;
    private int mEdgeProtectionRight;
    private int mEdgeProtectionBottom;
    private int mOffsetX = 0;
    private int mOffsetYIfTop = 0;
    private int mOffsetYIfBottom = 0;
    private @ZDirection
    int mPreferredDirection = ZDirection.DIRECTION_BOTTOM;
    protected final int mInitWidth;
    protected final int mInitHeight;
    private View mContentView;

    /**
     * 指引背景色  黑暗
     */
    int nightBackgroundColor = Color.parseColor("#cc141A22");
    /**
     * 高亮半径
     */
    int lightRadius = 30;

    /**
     * 形状
     */
    private @ZShapeType
    int myShape = ZShapeType.RECTANGULAR;

    /**
     * 高亮区域，四边保护间距
     */
    private int leftPadding = 0;
    private int topPadding = 0;
    private int rightPadding = 0;
    private int bottomPadding = 0;
    /**
     * 引导绑定view监听
     */
    AttachedToWindowListener attachedToWindowListener;
    /**
     * 引导消失监听
     */
    GuideDismissListener guideDismissListener;

    public ZGuidePopup(Context context, int width, int height) {
        super(context);
        mInitWidth = width;
        mInitHeight = height;
    }

    public ZGuidePopup edgeProtection(int distance) {
        mEdgeProtectionLeft = distance;
        mEdgeProtectionRight = distance;
        mEdgeProtectionTop = distance;
        mEdgeProtectionBottom = distance;
        return this;
    }

    public ZGuidePopup edgeProtection(int left, int top, int right, int bottom) {
        mEdgeProtectionLeft = left;
        mEdgeProtectionTop = top;
        mEdgeProtectionRight = right;
        mEdgeProtectionBottom = bottom;
        return this;
    }

    public ZGuidePopup offsetX(int offsetX) {
        mOffsetX = offsetX;
        return this;
    }

    public ZGuidePopup offsetYIfTop(int y) {
        mOffsetYIfTop = y;
        return this;
    }

    public ZGuidePopup offsetYIfBottom(int y) {
        mOffsetYIfBottom = y;
        return this;
    }

    public ZGuidePopup setNightBackgroundColor(int nightBackgroundColor) {
        this.nightBackgroundColor = nightBackgroundColor;
        return this;
    }

    public ZGuidePopup setLightRadius(int lightRadius) {
        this.lightRadius = lightRadius;
        return this;
    }

    public ZGuidePopup setLightShape(int myShape) {
        this.myShape = myShape;
        return this;
    }

    public ZGuidePopup setViewPadding(int left, int top, int right, int bottom) {
        this.leftPadding = left;
        this.topPadding = top;
        this.rightPadding = right;
        this.bottomPadding = bottom;
        return this;
    }

    public ZGuidePopup preferredDirection(@ZDirection int preferredDirection) {
        if (preferredDirection == ZDirection.DIRECTION_CENTER_IN_SCREEN) {
            mPreferredDirection = ZDirection.DIRECTION_BOTTOM;
        } else {
            mPreferredDirection = preferredDirection;
        }
        return this;
    }

    public ZGuidePopup view(View contentView) {
        mContentView = contentView;
        return this;
    }

    public ZGuidePopup view(@LayoutRes int contentViewResId) {
        return view(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    public void addAttachedToWindowListener(AttachedToWindowListener attachedToWindowListener) {
        this.attachedToWindowListener = attachedToWindowListener;
    }

    public void addGuideDismissListener(GuideDismissListener guideDismissListener) {
        this.guideDismissListener = guideDismissListener;
    }


    public class ShowInfo {
        private int[] anchorRootLocation = new int[2];// 当前绑定view的跟视图在整个屏幕上的坐标位置
        private int[] anchorLocation = new int[2];// 当前绑定view在整个屏幕上的坐标位置
        Rect visibleWindowFrame = new Rect(); // 当前绑定view的窗口区域
        int width;
        int height;

        int x;  // 气泡左上角在屏幕中X轴
        int y;// 气泡左上角在屏幕中Y轴
        View anchor;
        int anchorCenter; // 锚点视图的X轴中心点，
        int direction = mPreferredDirection;
        int contentWidthMeasureSpec;
        int contentHeightMeasureSpec;

        ShowInfo(View anchor) {
            this.anchor = anchor;
            // for muti window
            anchor.getRootView().getLocationOnScreen(anchorRootLocation);
            anchor.getLocationOnScreen(anchorLocation);
            anchorCenter = anchorLocation[0] + anchor.getWidth() / 2;
            anchor.getWindowVisibleDisplayFrame(visibleWindowFrame);
        }

        /**
         * 锚点比例
         */
        float anchorProportion() {
            return (anchorCenter - x) / (float) width;
        }

        int windowWidth() {
            return width;
        }

        int windowHeight() {
            return height;
        }

        int getVisibleWidth() {
            return visibleWindowFrame.width();
        }

        int getVisibleHeight() {
            return visibleWindowFrame.height();
        }

        int getWindowX() {
            return x - visibleWindowFrame.left;
        }

        int getWindowY() {
            return y - visibleWindowFrame.top;
        }
    }

    @Override
    public void show(@NonNull View anchor) {
        if (mContentView == null) {
            throw new RuntimeException("you should call view() to set your content view");
        }
        ShowInfo showInfo = new ShowInfo(anchor);
        calculateWindowSize(showInfo);
        calculateXY(showInfo);
        decorateContentView(showInfo, anchor);
        mWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        showAtLocation(anchor, 0, 0);
    }

    private void decorateContentView(ShowInfo showInfo, @NonNull View anchor) {
        ContentView contentView = ContentView.wrap(mContentView, mInitWidth, mInitHeight);
        Log.d(TAG, "contentView:" + contentView.getWidth() + " - " + contentView.getHeight());

        DecorRootView decorRootView = new DecorRootView(mContext, showInfo);

        decorRootView.setTargetView(anchor);
        decorRootView.setContentView(contentView);
        decorRootView.setViewProperty(showInfo.anchorLocation[0],
                showInfo.anchorLocation[1] - showInfo.visibleWindowFrame.top,
                showInfo.anchorLocation[0] + anchor.getWidth(),
                showInfo.anchorLocation[1] + anchor.getHeight() - showInfo.visibleWindowFrame.top);
        decorRootView.setRadius(lightRadius);
        decorRootView.setMyShape(myShape);
        decorRootView.setViewPadding(leftPadding, topPadding, rightPadding, bottomPadding);
        decorRootView.setBackgroundColor(nightBackgroundColor);
        decorRootView.addGuideDismissListener(guideDismissListener);
        decorRootView.addAttachedToWindowListener(attachedToWindowListener);

        mWindow.setContentView(decorRootView);
    }

    private void calculateXY(ShowInfo showInfo) {
        if (showInfo.anchorCenter < showInfo.visibleWindowFrame.left + showInfo.getVisibleWidth() / 2) {
            // anchor point on the left
            showInfo.x = Math.max(mEdgeProtectionLeft + showInfo.visibleWindowFrame.left, showInfo.anchorCenter - showInfo.width / 2 + mOffsetX);
        } else {
            // anchor point on the left
            showInfo.x = Math.min(
                    showInfo.visibleWindowFrame.right - mEdgeProtectionRight - showInfo.width,
                    showInfo.anchorCenter - showInfo.width / 2 + mOffsetX);
        }
        int nextDirection = ZDirection.DIRECTION_BOTTOM;
        if (mPreferredDirection == ZDirection.DIRECTION_BOTTOM) {
            nextDirection = ZDirection.DIRECTION_TOP;
        } else if (mPreferredDirection == ZDirection.DIRECTION_TOP) {
            nextDirection = ZDirection.DIRECTION_BOTTOM;
        }
        handleDirection(showInfo, mPreferredDirection, nextDirection);
    }

    private void handleDirection(ShowInfo showInfo, int currentDirection, int nextDirection) {
        if (currentDirection == ZDirection.DIRECTION_TOP) {
            showInfo.y = showInfo.anchorLocation[1] - showInfo.height - mOffsetYIfTop;
        } else if (currentDirection == ZDirection.DIRECTION_BOTTOM) {
            showInfo.y = showInfo.anchorLocation[1] + showInfo.anchor.getHeight() + mOffsetYIfBottom;
        }
    }

    protected int proxyWidth(int width) {
        return width;
    }

    protected int proxyHeight(int height) {
        return height;
    }

    private void calculateWindowSize(ShowInfo showInfo) {
        boolean needMeasureForWidth = false, needMeasureForHeight = false;
        if (mInitWidth > 0) {
            showInfo.width = proxyWidth(mInitWidth);
            showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    showInfo.width, View.MeasureSpec.EXACTLY);
        } else {
            int maxWidth = showInfo.getVisibleWidth() - mEdgeProtectionLeft - mEdgeProtectionRight;
            if (mInitWidth == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.width = proxyWidth(maxWidth);
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        showInfo.width, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForWidth = true;
                showInfo.contentWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        proxyWidth(maxWidth), View.MeasureSpec.AT_MOST);
            }
        }
        if (mInitHeight > 0) {
            showInfo.height = proxyHeight(mInitHeight);
            showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                    showInfo.height, View.MeasureSpec.EXACTLY);
        } else {
            int maxHeight = showInfo.getVisibleHeight() - mEdgeProtectionTop - mEdgeProtectionBottom;
            if (mInitHeight == ViewGroup.LayoutParams.MATCH_PARENT) {
                showInfo.height = proxyHeight(maxHeight);
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        showInfo.height, View.MeasureSpec.EXACTLY);
            } else {
                needMeasureForHeight = true;
                showInfo.contentHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                        proxyHeight(maxHeight), View.MeasureSpec.AT_MOST);
            }
        }

        if (needMeasureForWidth || needMeasureForHeight) {
            mContentView.measure(
                    showInfo.contentWidthMeasureSpec, showInfo.contentHeightMeasureSpec);
            if (needMeasureForWidth) {
                showInfo.width = proxyWidth(mContentView.getMeasuredWidth());
            }
            if (needMeasureForHeight) {
                showInfo.height = proxyHeight(mContentView.getMeasuredHeight());
            }
        }
    }

    static class ContentView extends ZPopFrameLayout {
        private ContentView(Context context) {
            super(context);
        }

        static ContentView wrap(View businessView, int width, int height) {
            ContentView contentView = new ContentView(businessView.getContext());
            if (businessView.getParent() != null) {
                ((ViewGroup) businessView.getParent()).removeView(businessView);
            }
            contentView.addView(businessView, new LayoutParams(width, height));
            return contentView;
        }
    }

    class DecorRootView extends ZGuideFrameLayout {
        private ShowInfo mShowInfo;
        private View mContentView;
        // 引导绑定view监听
        AttachedToWindowListener attachedToWindowListener;
        // 引导消失监听
        GuideDismissListener guideDismissListener;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
//            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            if (action == MotionEvent.ACTION_UP) {
                if (guideDismissListener != null) {
                    if (isTouchInBlack(event)) {
                        Log.d(TAG, "点击了空白区域，说明这个可以关掉 action:" + action);
                        boolean enableDismiss = guideDismissListener.touchDismiss();
                        if (!enableDismiss) {
                            dismiss();
                        }
                    }
                } else {
                    dismiss();
                }
            }
            return true;
        }

        private boolean isTouchInBlack(MotionEvent event) {
            View childView = findChildViewUnder(event.getX(), event.getY());
            return childView == null;
        }

        private View findChildViewUnder(float x, float y) {
            final int count = getChildCount();
            for (int i = count - 1; i >= 0; i--) {
                final View child = getChildAt(i);
                final float translationX = child.getTranslationX();
                final float translationY = child.getTranslationY();
                if (x >= child.getLeft() + translationX
                        && x <= child.getRight() + translationX
                        && y >= child.getTop() + translationY
                        && y <= child.getBottom() + translationY) {
                    return child;
                }
            }
            return null;
        }

        private int mPendingWidth;
        private int mPendingHeight;
        private Runnable mUpdateWindowAction = new Runnable() {
            @Override
            public void run() {
                mShowInfo.width = mPendingWidth;
                mShowInfo.height = mPendingHeight;
                calculateXY(mShowInfo);
                mWindow.update(mShowInfo.getWindowX(), mShowInfo.getWindowY(), mShowInfo.windowWidth(), mShowInfo.windowHeight());
            }
        };

        private DecorRootView(Context context, ShowInfo showInfo) {
            super(context);
            mShowInfo = showInfo;
        }


        public void addAttachedToWindowListener(AttachedToWindowListener attachedToWindowListener) {
            this.attachedToWindowListener = attachedToWindowListener;
        }

        public void addGuideDismissListener(GuideDismissListener guideDismissListener) {
            this.guideDismissListener = guideDismissListener;
        }

        public void setContentView(View contentView) {
            if (mContentView != null) {
                removeView(mContentView);
            }
            if (contentView.getParent() != null) {
                ((ViewGroup) contentView.getParent()).removeView(contentView);
            }
            mContentView = contentView;
            addView(contentView);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            removeCallbacks(mUpdateWindowAction);
            if (mContentView != null) {
                mContentView.measure(mShowInfo.contentWidthMeasureSpec, mShowInfo.contentHeightMeasureSpec);
                int measuredWidth = mContentView.getMeasuredWidth();
                int measuredHeight = mContentView.getMeasuredHeight();
                if (mShowInfo.width != measuredWidth || mShowInfo.height != measuredHeight) {
                    mPendingWidth = measuredWidth;
                    mPendingHeight = measuredHeight;
                    post(mUpdateWindowAction);
                }
            }
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            if (mContentView != null) {
                mContentView.layout(mShowInfo.getWindowX(), mShowInfo.getWindowY(), mShowInfo.getWindowX() + mShowInfo.width, mShowInfo.getWindowY() + mShowInfo.height);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            removeCallbacks(mUpdateWindowAction);
            if (attachedToWindowListener != null) {
                attachedToWindowListener.onAttachedToWindow(mShowInfo);
            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
        }

    }

    /**
     * 引导消失监听
     */
    public interface GuideDismissListener {
        boolean touchDismiss();
    }


    /**
     * 引导绑定view监听
     */
    public interface AttachedToWindowListener {
        void onAttachedToWindow(ShowInfo showInfo);
    }

}
