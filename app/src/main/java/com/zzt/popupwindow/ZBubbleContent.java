package com.zzt.popupwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: zeting
 * @date: 2025/2/20
 * 在 Content 中插入气泡
 */
public class ZBubbleContent {
    private static final String TAG = ZBubbleContent.class.getSimpleName();

    private View contentView;
    private Context context;

    private Window window; // 添加的 Windows
    private View anchor; // 添加锚点视图
    private View showView; // 插入的视图


    @IntDef({ZAnchorGravity.ANCHOR_GRAVITY_UP, ZAnchorGravity.ANCHOR_GRAVITY_DOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ZAnchorGravity {
        int ANCHOR_GRAVITY_UP = 1; //1 在上面
        int ANCHOR_GRAVITY_DOWN = 0; //0 在下面
    }

    private @ZAnchorGravity int anchorGravity; // 位于锚点方位 0   在下面 1 在上面


    @IntDef({ZLayoutGravity.LAYOUT_GRAVITY_START, ZLayoutGravity.LAYOUT_GRAVITY_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ZLayoutGravity {
        int LAYOUT_GRAVITY_START = 0; //0 ，位于父视图左边
        int LAYOUT_GRAVITY_END = 1; //1，位于俯视图右边
    }

    private @ZLayoutGravity int layoutGravity; // 位于俯视图 0 左边 ,1  右边

    private int marginDimen;// 距离边距

    public ZBubbleContent(Window window) {
        this.window = window;
    }


    @NonNull
    public View getAnchor() {
        return anchor;
    }


    @NonNull
    public View getShowView() {
        return showView;
    }


    public int getAnchorGravity() {
        return anchorGravity;
    }

    /**
     * 设置位于锚点方位 0   在下面 1 在上面
     *
     * @param anchorGravity
     * @return
     */
    public ZBubbleContent setAnchorGravity(@ZAnchorGravity int anchorGravity) {
        this.anchorGravity = anchorGravity;
        return this;
    }

    public int getLayoutGravity() {
        return layoutGravity;
    }

    /**
     * 设置视图位置  0 左边 ,1  右边
     *
     * @param layoutGravity
     * @return
     */
    public ZBubbleContent setLayoutGravity(@ZLayoutGravity int layoutGravity) {
        this.layoutGravity = layoutGravity;
        return this;
    }

    public int getMarginDimen() {
        return marginDimen;
    }

    /**
     * 设置距离左右边缘间距
     *
     * @param marginDimen
     * @return
     */
    public ZBubbleContent setMarginDimen(int marginDimen) {
        this.marginDimen = marginDimen;
        return this;
    }


    /**
     * 显示气泡
     */
    public void showBubble(@NonNull View anchor, @NonNull View showView) {
        this.anchor = anchor;
        this.showView = showView;
        if (window != null && anchor != null && showView != null) {
//            anchor.post(new Runnable() {
//                @Override
//                public void run() {
            contentView = window.getDecorView().findViewById(android.R.id.content);
            if (contentView instanceof FrameLayout) {

                // 锚点距离上方
                int[] anchorLocationScr = new int[2];
                anchor.getLocationOnScreen(anchorLocationScr);

                // 锚点高度
                int anchorHeight = anchor.getHeight();

                context = contentView.getContext();
                ZBubbleFrameLayout zBubbleFrameLayout = findAddBubbleFLByContent((FrameLayout) contentView);
                FrameLayout.LayoutParams parentLP = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);

                // 设置方向
                if (layoutGravity == ZLayoutGravity.LAYOUT_GRAVITY_END) {
                    // 父视图排版右边
                    parentLP.gravity = Gravity.END;
                } else {
                    // 父视图排版左边
                    parentLP.gravity = Gravity.START;
                }

                // 指定测量规格，这里使用 UNSPECIFIED，表示父视图不限制子视图的大小
                int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                showView.measure(widthMeasureSpec, heightMeasureSpec);
                int addHeight = showView.getMeasuredHeight();

                // 设置在上方还是在下方
                if (anchorGravity == ZAnchorGravity.ANCHOR_GRAVITY_UP) {
                    // 在上方
                    // 设置间距
                    parentLP.setMargins(marginDimen, anchorLocationScr[1] - addHeight, marginDimen, 0);
                } else {
                    // 在下方
                    parentLP.setMargins(marginDimen, anchorLocationScr[1] + anchorHeight, marginDimen, 0);
                }

                /**********************测试数据**********************/
                showView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_test_v1));
                /**********************测试数据**********************/

                // 给视图添加 tag
                showView.setTag(R.id.z_bubble_tag, "bubbleTag:" + System.currentTimeMillis());

                zBubbleFrameLayout.addView(showView, parentLP);
            }
//                }
//            });
        }
    }


    /**
     * 查找内容中是否已经包含了气泡父视图
     *
     * @param contentLayout
     * @return
     */
    private ZBubbleFrameLayout findAddBubbleFLByContent(FrameLayout contentLayout) {
        ZBubbleFrameLayout bubbleFLByContent = findBubbleFLByContent(contentLayout);
        if (bubbleFLByContent == null) {

            // 当前视图没有，添加一个进入
            ZBubbleFrameLayout zBubbleFrameLayout = new ZBubbleFrameLayout(contentLayout.getContext());

            /**********************测试数据**********************/
//            zBubbleFrameLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_test_v1));
            /**********************测试数据**********************/

            contentLayout.addView(zBubbleFrameLayout, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return zBubbleFrameLayout;
        } else {
            return bubbleFLByContent;
        }
    }


    /**
     * 查找内容中是否已经包含了气泡父视图
     *
     * @param contentLayout
     * @return
     */
    private ZBubbleFrameLayout findBubbleFLByContent(FrameLayout contentLayout) {
        int childCount = contentLayout.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            View childAt = contentLayout.getChildAt(i);
            if (childAt instanceof ZBubbleFrameLayout) {
                return (ZBubbleFrameLayout) childAt;
            }
        }
        return null;
    }


    /**
     * 移除当前气泡
     */
    public void dismissBubble(View removeView) {
        if (window != null && anchor != null && removeView != null) {
            contentView = window.getDecorView().findViewById(android.R.id.content);
            if (contentView instanceof FrameLayout) {
                ZBubbleFrameLayout bubbleFLByContent = findBubbleFLByContent((FrameLayout) contentView);
                if (bubbleFLByContent != null) {
                    int bChildCount = bubbleFLByContent.getChildCount();
                    if (bChildCount > 0) {
                        for (int i = 0; i < bChildCount; i++) {
                            View childAt = bubbleFLByContent.getChildAt(i);
                            Object childTag = childAt.getTag(R.id.z_bubble_tag);
                            Object showTag = removeView.getTag(R.id.z_bubble_tag);
                            if (childTag instanceof String && showTag instanceof String) {
                                Log.d(TAG, "已添加视图 childTag:" + childTag + " > showTag:" + showTag);
                                if (childTag.equals(showTag)) {
                                    bubbleFLByContent.removeView(childAt);
                                    break;
                                }
                            }
                        }
                    }

                    // 最近检测一下气泡父布局还有没有数据，没有父布局也移除
                    int lastChildCount = bubbleFLByContent.getChildCount();
                    if (lastChildCount == 0) {
                        int contCount = ((FrameLayout) contentView).getChildCount();
                        for (int i = contCount - 1; i >= 0; i--) {
                            View childAt = ((FrameLayout) contentView).getChildAt(i);
                            if (childAt instanceof ZBubbleFrameLayout) {
                                ((FrameLayout) contentView).removeView(childAt);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断当前视图是否已经展示
     */
    public void isShowBubble() {

    }

}
