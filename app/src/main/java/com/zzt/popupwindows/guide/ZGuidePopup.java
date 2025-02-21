package com.zzt.popupwindows.guide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.zzt.popupwindows.library.ZBasePopup;

import java.util.ArrayList;

/**
 * @author: zeting
 * @date: 2021/7/2
 */
public class ZGuidePopup extends ZBasePopup<ZGuidePopup> {
    /**
     * 绘制前景bitmap
     */
    private Bitmap bitmap;
    /**
     * 背景色和透明度，格式 #aarrggbb
     */
    private int backgroundColor;
    /**
     * Canvas,绘制bitmap
     */
    private Canvas temp;
    /**
     * 透明圆形画笔
     */
    private Paint mCirclePaint;
    /**
     * 绘图层叠模式
     */
    private PorterDuffXfermode porterDuffXfermode;
    /**
     * 形状
     */
    private MyShape myShape;

    /**
     * targetView圆心
     */
    private int[] center;
    /**
     * targetView 的外切圆半径
     */
    private int radius;

    private ArrayList<ViewInfo> mViews = new ArrayList<>();

    class ViewInfo {
        private View view;
        private ConstraintLayout.LayoutParams lp;

        public ViewInfo(View view, ConstraintLayout.LayoutParams lp) {
            this.view = view;
            this.lp = lp;
        }
    }

    /**
     * 定义目标控件的形状，共3种。圆形，椭圆，带圆角的矩形（可以设置圆角大小），不设置则默认是圆形
     */
    public enum MyShape {
        CIRCULAR, ELLIPSE, RECTANGULAR
    }


    public ZGuidePopup(Context context) {
        super(context);
    }

    public boolean isShowing() {
        return mWindow.isShowing();
    }

    @Override
    public void show(@NonNull View anchor) {
        if (isShowing()) {
            return;
        }
        if (mViews.isEmpty()) {
            throw new RuntimeException("you should call addView() to add content view");
        }
    }


    /**
     * 绘制背景色
     *
     * @param view
     */
    public void drawBackground(View view) {
        // 先绘制bitmap，再将bitmap绘制到屏幕
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        temp = new Canvas(bitmap);

        // 背景画笔
        Paint bgPaint = new Paint();
        if (backgroundColor != 0)
            bgPaint.setColor(backgroundColor);
        else
            bgPaint.setColor(Color.parseColor("#cc222222"));

        // 绘制屏幕背景
        temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), bgPaint);

        // targetView 的透明圆形画笔
        if (mCirclePaint == null)
            mCirclePaint = new Paint();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);// 或者CLEAR
        mCirclePaint.setXfermode(porterDuffXfermode);
        mCirclePaint.setAntiAlias(true);

        if (myShape != null) {
            RectF oval = new RectF();
            switch (myShape) {
                case CIRCULAR://圆形
                    temp.drawCircle(center[0], center[1], radius, mCirclePaint);//绘制圆形
                    break;
                case ELLIPSE://椭圆
                    //RectF对象
                    oval.left = center[0] - 150 - radius * 2;                     //左边
                    oval.top = center[1] - 50 - radius;                                   //上边
                    oval.right = center[0] + 150 + radius * 2;                             //右边
                    oval.bottom = center[1] + 50 + radius;                                //下边
                    temp.drawOval(oval, mCirclePaint);                   //绘制椭圆
                    Log.i("TZLTEST", "drawBackground: ");
                    break;
                case RECTANGULAR://圆角矩形
                    //RectF对象
                    oval.left = center[0] - 150;                              //左边
                    oval.top = center[1] - 50;                                   //上边
                    oval.right = center[0] + 150;                             //右边
                    oval.bottom = center[1] + 50;                                //下边
                    temp.drawRoundRect(oval, radius, radius, mCirclePaint);                   //绘制圆角矩形
                    break;
            }
        } else {
            temp.drawCircle(center[0], center[1], radius, mCirclePaint);//绘制圆形
        }

        // 绘制到屏幕
        Drawable drawable = new BitmapDrawable(view.getResources(), bitmap);
        view.setBackground(drawable);
        bitmap.recycle();
    }

}
