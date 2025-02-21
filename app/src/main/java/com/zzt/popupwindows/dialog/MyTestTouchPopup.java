package com.zzt.popupwindows.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzt.popupwindow.R;


/**
 * @author: zeting
 * @date: 2021/8/4
 */
public class MyTestTouchPopup extends PopupWindow {
    private static final String TAG = MyTestTouchPopup.class.getSimpleName();
    private View mPopView;
    TextView tv_content;

    public MyTestTouchPopup(Context context) {
        super(context);
        init(context);
        setPopupWindow();
    }


    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(R.layout.layout_popup_windows_touch, null);
    }


    private void setPopupWindow() {
        this.setContentView(mPopView);
        this.setFocusable(true);
//        dismissIfOutsideTouch(true);
//        this.setTouchable(true);

        tv_content = mPopView.findViewById(R.id.tv_content);
        tv_content.setOnClickListener(v -> Log.d(TAG, "点击了 里面的文本"));
    }

    @Override
    public void setTouchInterceptor(View.OnTouchListener l) {
        super.setTouchInterceptor(l);
    }

    private View.OnTouchListener mOutsideTouchDismissListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d(TAG, "点击了   PopupWindow 区域");
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                Log.d(TAG, "点击了  PopupWindow  属于外部区域");
                dismiss();
                return true;
            }
            return false;
        }
    };

    public void dismissIfOutsideTouch(boolean dismissIfOutsideTouch) {
        this.setOutsideTouchable(dismissIfOutsideTouch);
        if (dismissIfOutsideTouch) {
            this.setTouchInterceptor(mOutsideTouchDismissListener);
        } else {
            this.setTouchInterceptor(null);
        }
    }

}
