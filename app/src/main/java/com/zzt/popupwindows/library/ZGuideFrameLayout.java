
package com.zzt.popupwindows.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

/**
 * @author: zeting
 * @date: 2021/6/21
 * 指引的黑色背景绘制
 */
public class ZGuideFrameLayout extends FrameLayout {
    private static final String TAG = ZGuideFrameLayout.class.getSimpleName();

    /**
     * 弱引用view
     */
    private WeakReference<View> mTargetOwner;
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
     * 背景画笔
     */
    private Paint bgPaint;
    /**
     * 绘图层叠模式
     */
    private PorterDuffXfermode porterDuffXfermode;
    /**
     * 形状
     */
    private @ZShapeType
    int myShape = ZShapeType.RECTANGULAR;
    /**
     * 高亮区域.四条边
     */
    private int leftValue = 0;
    private int topValue = 0;
    private int rightValue = 0;
    private int bottomValue = 0;

    /**
     * 高亮区域，四边保护间距
     */
    private int leftPadding = 0;
    private int topPadding = 0;
    private int rightPadding = 0;
    private int bottomPadding = 0;
    /**
     * 显示圆角，圆，椭圆：增加的半径，方：圆角半径
     */
    private int radius = 0;

    public ZGuideFrameLayout(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ZGuideFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ZGuideFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public void initView(Context context, AttributeSet attrs, int defAttr) {
        // 透明画笔
        mCirclePaint = new Paint();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);// 或者CLEAR
        mCirclePaint.setXfermode(porterDuffXfermode);
        mCirclePaint.setAntiAlias(true);

        // 背景画笔
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#b2172346"));
    }

    public void setTargetView(View targetView) {
        mTargetOwner = new WeakReference<>(targetView);
    }

    public void setViewProperty(int left, int top, int right, int bottom) {
        this.leftValue = left;
        this.topValue = top;
        this.rightValue = right;
        this.bottomValue = bottom;
    }

    public void setViewPadding(int left, int top, int right, int bottom) {
        this.leftPadding = left;
        this.topPadding = top;
        this.rightPadding = right;
        this.bottomPadding = bottom;
    }

    /**
     * 设置圆半径和圆角半径
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setMyShape(@ZShapeType int myShape) {
        this.myShape = myShape;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.v(TAG, "指引的黑色背景绘制 dispatchDraw");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "指引的黑色背景绘制 onDraw");
        View view = mTargetOwner.get();
        if (view == null) {
            return;
        }
        drawBackground(canvas);
    }

    private void drawBackground(Canvas canvas) {
        Log.v(TAG, "指引的黑色背景绘制 drawBackground");
        Drawable background = getBackground();
        if (background instanceof ColorDrawable) {
            Log.d(TAG, "指引的黑色背景绘制 ColorDrawable");
            ColorDrawable colorDrawable = (ColorDrawable) background;
            int color = colorDrawable.getColor();
            bgPaint.setColor(color);

            bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            temp = new Canvas(bitmap);
            // 绘制屏幕背景
            temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), bgPaint);

            RectF oval = new RectF();
            if (myShape != 0) {
                switch (myShape) {
                    case ZShapeType.CIRCULAR://圆形
                        temp.drawCircle(leftValue + (rightValue - leftValue) * 0.5F,
                                topValue + (bottomValue - topValue) * 0.5F,
                                Math.max((rightValue - leftValue) * 0.5F,
                                        (bottomValue - topValue) * 0.5F) + radius, mCirclePaint);
                        break;
                    case ZShapeType.ELLIPSE://椭圆
                        oval.left = leftValue - leftPadding;
                        oval.top = topValue - topPadding;
                        oval.right = rightValue + rightPadding;
                        oval.bottom = bottomValue + bottomPadding;
                        temp.drawOval(oval, mCirclePaint);
                        break;
                    case ZShapeType.RECTANGULAR://圆角矩形
                        oval.left = leftValue - leftPadding;
                        oval.top = topValue - topPadding;
                        oval.right = rightValue + rightPadding;
                        oval.bottom = bottomValue + bottomPadding;
                        temp.drawRoundRect(oval, radius, radius, mCirclePaint);
                        break;
                }
            } else {
                oval.left = leftValue;
                oval.top = topValue;
                oval.right = rightValue;
                oval.bottom = bottomValue;
                temp.drawRoundRect(oval, radius, radius, mCirclePaint);
            }
            // 绘制到屏幕
            canvas.drawBitmap(bitmap, 0, 0, bgPaint);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            setBackground(bitmapDrawable);
        }
    }
}
