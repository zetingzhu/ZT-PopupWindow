package com.zzt.popupwindows.library;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: zeting
 * @date: 2021/8/16
 * 设置高亮形状
 */
@IntDef({ZShapeType.CIRCULAR, ZShapeType.ELLIPSE, ZShapeType.RECTANGULAR})
@Retention(RetentionPolicy.SOURCE)
public @interface ZShapeType {
    int CIRCULAR = 1;//圆形
    int ELLIPSE = 2;//椭圆
    int RECTANGULAR = 3;//带圆角的矩形
}
