/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzt.popupwindows.library;

import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class ZFullScreenPopup extends ZBasePopup<ZFullScreenPopup> {
    private int mAnimStyle = NOT_SET;
    private ArrayList<ViewInfo> mViews = new ArrayList<>();

    public ZFullScreenPopup(Context context) {
        super(context);
        mWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public ZFullScreenPopup animStyle(int animStyle) {
        mAnimStyle = animStyle;
        return this;
    }

    public ZFullScreenPopup addView(@LayoutRes int contentViewResId) {
        return addView(LayoutInflater.from(mContext).inflate(contentViewResId, null));
    }

    public ZFullScreenPopup addView(View view) {
        return addView(view, defaultContentLp());
    }

    public ZFullScreenPopup addView(View view, FrameLayout.LayoutParams lp) {
        mViews.add(new ViewInfo(view, lp));
        return this;
    }

    private FrameLayout.LayoutParams defaultContentLp() {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        return lp;
    }

    public boolean isShowing() {
        return mWindow.isShowing();
    }

    public void show(View parent) {
        if (isShowing()) {
            return;
        }
        if (mViews.isEmpty()) {
            throw new RuntimeException("you should call addView() to add content view");
        }
        ArrayList<ViewInfo> views = new ArrayList<>(mViews);
        FrameLayout rootView = new FrameLayout(mContext);
        for (int i = 0; i < views.size(); i++) {
            ViewInfo info = mViews.get(i);
            View view = info.view;
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }

            rootView.addView(view, info.lp);
        }
        mWindow.setContentView(rootView);
        if (mAnimStyle != NOT_SET) {
            mWindow.setAnimationStyle(mAnimStyle);
        }

        showAtLocation(parent, 0, 0);
    }

    @Override
    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {
        lp.flags |= FLAG_LAYOUT_IN_SCREEN | FLAG_LAYOUT_INSET_DECOR;
        super.modifyWindowLayoutParams(lp);
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
