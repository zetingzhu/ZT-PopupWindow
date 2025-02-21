package com.zzt.popupwindows.library;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: zeting
 * @date: 2021/8/17
 * 显示内容的方向
 */
@IntDef({ZDirection.DIRECTION_TOP, ZDirection.DIRECTION_BOTTOM, ZDirection.DIRECTION_CENTER_IN_SCREEN})
@Retention(RetentionPolicy.SOURCE)
public @interface ZDirection {
    int DIRECTION_TOP = 0; // 显示在上面
    int DIRECTION_BOTTOM = 1;// 显示在下面
    int DIRECTION_CENTER_IN_SCREEN = 2;// 现在在屏幕中间
}