package com.zzt.popupwindows.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

/**
 * @author: zeting
 * @date: 2021/9/3
 * 设置底部展示布局
 */
public class ShowBottomLayoutUtil {
    private ArrayList<ViewInfo> mViews = new ArrayList<>();
    protected Context mContext;
    protected FrameLayout rootView;

    public ShowBottomLayoutUtil(Context context, FrameLayout rootView) {
        this.mContext = context;
        this.rootView = rootView;
    }

    public ShowBottomLayoutUtil addView(@LayoutRes int contentViewResId) {
        return addView(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    public ShowBottomLayoutUtil addView(View view) {
        return addView(view, defaultContentLp());
    }

    public ShowBottomLayoutUtil addView(View view, FrameLayout.LayoutParams lp) {
        mViews.add(new ViewInfo(view, lp));
        return this;
    }

    private FrameLayout.LayoutParams defaultContentLp() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public void showView() {
        if (rootView == null) {
            return;
        }
        if (mViews.isEmpty()) {
            return;
        }
        ArrayList<ViewInfo> views = new ArrayList<>(mViews);
        for (int i = 0; i < views.size(); i++) {
            ViewInfo info = mViews.get(i);
            View view = info.view;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            rootView.addView(view, info.lp);
        }
    }


    public void hideView(FrameLayout rootView) {
        if (rootView != null) {
            int childCount = rootView.getChildCount();
            for (int i = 1; i < childCount; i++) {
                View childAt = rootView.getChildAt(i);
                if (childAt != null) {
                    rootView.removeView(childAt);
                }
            }
        }
    }

    class ViewInfo {
        private View view;
        private FrameLayout.LayoutParams lp;

        public ViewInfo(View view, FrameLayout.LayoutParams lp) {
            this.view = view;
            this.lp = lp;
        }
    }

}