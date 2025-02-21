package com.zzt.popupwindows.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.zzt.popupwindow.R;
import com.zzt.popupwindows.util.ScreenUtil;

/**
 * @author: zeting
 * @date: 2021/7/29
 */
public class MyTestPopupWindows extends PopupWindow {

    Context mContext;
    private LayoutInflater mInflater;
    private View mContentView;

    public MyTestPopupWindows(Context context) {
        super(context);
        this.mContext = mContext;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.layout_popup_windows, null);
        //设置View
        setContentView(mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new ColorDrawable());
        /**
         * 设置可以获取集点
         */
        setFocusable(true);
        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);
        /**
         *设置可以触摸
         */
        setTouchable(true);
        //sdk > 21 解决 标题栏没有办法遮罩的问题
        this.setClippingEnabled(false);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ScreenUtil.getScreenHeight(context));//屏幕的高
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80003000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        /**
         * 设置点击外部可以消失
         */
        setTouchInterceptor((v, event) -> {
            /**
             * 判断是不是点击了外部
             */
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                return true;
            }
            //不是点击外部
            return false;
        });
        /**
         * 初始化View与监听器
         */
        initView();
        initListener();
    }


    private void initView() {

    }

    private void initListener() {

    }


}
