package com.zzt.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author: zeting
 * @date: 2025/2/20
 * 添加气泡基类，可以用来特殊逻辑
 */
public class ZBubbleFrameLayout extends FrameLayout {
    public ZBubbleFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ZBubbleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZBubbleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
