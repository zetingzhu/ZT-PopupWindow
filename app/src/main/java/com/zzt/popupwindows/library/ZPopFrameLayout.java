
package com.zzt.popupwindows.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;

import java.lang.ref.WeakReference;

/**
 * @author: zeting
 * @date: 2021/6/21
 * PopupWindow 普通弹框内容定义布局
 */
public class ZPopFrameLayout extends FrameLayout {
    private static final String TAG = ZPopFrameLayout.class.getSimpleName();

    // 画笔
    private Paint mClipPaint;
    // 圆角半径
    private int mRadius;
    // 边框的矩形
    private RectF mBorderRect;
    // 边框颜色
    private int mBorderColor = 0;
    // 边框宽度
    private int mBorderWidth = 1;
    // 弱引用view
    private WeakReference<View> mOwner;

    public ZPopFrameLayout(Context context) {
        super(context);
        initView(context, null, 0, this);
    }

    public ZPopFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, this);
    }

    public ZPopFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        dispatchRoundBorderDraw(canvas);
    }

    public void initView(Context context, AttributeSet attrs, int defAttr, View owner) {
        mOwner = new WeakReference<>(owner);

        mClipPaint = new Paint();
        mClipPaint.setAntiAlias(true);
        mBorderRect = new RectF();

        int radius = 0;
        setRadiusAndShadow(radius);
    }

    public void setRadius(int radius) {
        if (mRadius != radius) {
            setRadiusAndShadow(radius);
        }
    }


    public int getRadius() {
        return mRadius;
    }


    public void setRadiusAndShadow(int radius) {
        final View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        mRadius = radius;
        if (useFeature()) {
            owner.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    int w = view.getWidth(), h = view.getHeight();
                    if (w == 0 || h == 0) {
                        return;
                    }
                    float radius = getRealRadius();
                    int min = Math.min(w, h);
                    if (radius * 2 > min) {
                        radius = min / 2F;
                    }
                    int top = 0, bottom = Math.max(top + 1, h);
                    if (radius <= 0) {
                        outline.setRect(0, top, w, bottom);
                    } else {
                        outline.setRoundRect(0, top, w, bottom, radius);
                    }
                }
            });
            owner.setClipToOutline(mRadius > 0);
        }
        owner.invalidate();
    }


    public void setBorderColor(@ColorInt int borderColor) {
        mBorderColor = borderColor;
    }


    public void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }

    private int getRealRadius() {
        View owner = mOwner.get();
        if (owner == null) {
            return mRadius;
        }
        return mRadius;
    }

    public void dispatchRoundBorderDraw(Canvas canvas) {
        View owner = mOwner.get();
        if (owner == null) {
            return;
        }
        int radius = getRealRadius();
        boolean needDrawBorder = mBorderWidth > 0 && mBorderColor != 0;
        if (!needDrawBorder) {
            return;
        }

        int width = canvas.getWidth(), height = canvas.getHeight();
        canvas.save();
        canvas.translate(owner.getScrollX(), owner.getScrollY());

        // react
        float halfBorderWith = mBorderWidth / 2f;
        mBorderRect.set(halfBorderWith, halfBorderWith,
                width - halfBorderWith, height - halfBorderWith);

        if (needDrawBorder) {
            mClipPaint.setColor(mBorderColor);
            mClipPaint.setStrokeWidth(mBorderWidth);
            mClipPaint.setStyle(Paint.Style.STROKE);
            if (radius <= 0) {
                canvas.drawRect(mBorderRect, mClipPaint);
            } else {
                canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint);
            }
        }
        canvas.restore();
    }

    public static boolean useFeature() {
        return Build.VERSION.SDK_INT >= 21;
    }


}
