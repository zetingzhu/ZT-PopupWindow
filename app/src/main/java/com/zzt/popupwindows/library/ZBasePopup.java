package com.zzt.popupwindows.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.lang.ref.WeakReference;

/**
 * @author: zeting
 * @date: 2021/6/21
 * PopupWindow 弹框基类
 */
public abstract class ZBasePopup<T extends ZBasePopup> {
    public static final float DIM_AMOUNT_NOT_EXIST = -1f;
    public static final int NOT_SET = -1;

    protected final PopupWindow mWindow;
    protected WindowManager mWindowManager;
    protected Context mContext;
    protected WeakReference<View> mAttachedViewRf;
    private float mDimAmount = DIM_AMOUNT_NOT_EXIST;
    private PopupWindow.OnDismissListener mDismissListener;
    private boolean mDismissIfOutsideTouch = true;

    private View.OnAttachStateChangeListener mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {

        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            dismiss();
        }
    };
    private View.OnTouchListener mOutsideTouchDismissListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//                mWindow.dismiss();
                return true;
            }
            return false;
        }
    };


    public ZBasePopup(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindow = new PopupWindow(context);
        initWindow();
    }

    private void initWindow() {
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mWindow.setFocusable(true);
        mWindow.setTouchable(true);
        mWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ZBasePopup.this.onDismiss();
                if (mDismissListener != null) {
                    mDismissListener.onDismiss();
                }
            }
        });
        dismissIfOutsideTouch(mDismissIfOutsideTouch);
    }

    /**
     * 设置popupWindows 昏暗背景， 0.0F - 1.0F   0.0F 透明 1.0F不透明
     *
     * @param dimAmount
     * @return
     */
    public T dimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    /**
     * this.setFocusable(true);  设置为false ,点击都直接透传到下面view上 ，点击外部区域 popupWindows 消失，内部区域 popupWindows 不消失
     * dismissIfOutsideTouch(true);
     * this.setTouchable(false); 设置为 popupWindows 不消失，点击透传
     */

    /**
     * 设置外部可以触摸，并且消失窗口
     */
    public T dismissIfOutsideTouch(boolean dismissIfOutsideTouch) {
        mDismissIfOutsideTouch = dismissIfOutsideTouch;
        mWindow.setOutsideTouchable(dismissIfOutsideTouch);
        if (dismissIfOutsideTouch) {
            mWindow.setTouchInterceptor(mOutsideTouchDismissListener);
        } else {
            mWindow.setTouchInterceptor(null);
        }
        return (T) this;
    }

    /**
     * 是否可以拦截点击事件
     */
    public T setTouchable(boolean touchable) {
        mWindow.setTouchable(touchable);
        return (T) this;
    }

    public T setFocusable(boolean touchable) {
        mWindow.setFocusable(touchable);
        return (T) this;
    }


    public T onDismiss(PopupWindow.OnDismissListener listener) {
        mDismissListener = listener;
        return (T) this;
    }

    private void removeOldAttachStateChangeListener() {
        if (mAttachedViewRf != null) {
            View oldAttachedView = mAttachedViewRf.get();
            if (oldAttachedView != null) {
                oldAttachedView.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
            }
        }
    }

    public View getDecorView() {
        View decorView = null;
        try {
            if (mWindow.getBackground() == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mWindow.getContentView().getParent();
                } else {
                    decorView = mWindow.getContentView();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView = (View) mWindow.getContentView().getParent().getParent();
                } else {
                    decorView = (View) mWindow.getContentView().getParent();
                }
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }

        return decorView;
    }

    protected void showAtLocation(@NonNull View parent, int x, int y) {
        if (!ViewCompat.isAttachedToWindow(parent)) {
            return;
        }
        removeOldAttachStateChangeListener();
        parent.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
        mAttachedViewRf = new WeakReference<>(parent);
        mWindow.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
        if (mDimAmount != DIM_AMOUNT_NOT_EXIST) {
            updateDimAmount(mDimAmount);
        }
    }

    private void updateDimAmount(float dimAmount) {
        View decorView = getDecorView();
        if (decorView != null) {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) decorView.getLayoutParams();
            p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            p.dimAmount = dimAmount;
            modifyWindowLayoutParams(p);
            mWindowManager.updateViewLayout(decorView, p);
        }
    }

    protected void modifyWindowLayoutParams(WindowManager.LayoutParams lp) {

    }

    protected void onDismiss() {

    }

    /**
     * 关闭对话框
     */
    public final void dismiss() {
        removeOldAttachStateChangeListener();
        mAttachedViewRf = null;
        mWindow.dismiss();
    }

    /**
     * 关闭对话框
     *
     * @param delayMillis
     */
    public final void dismissDelayed(long delayMillis) {
        if (mAttachedViewRf != null && mAttachedViewRf.get() != null) {
            mAttachedViewRf.get().postDelayed(() -> {
                removeOldAttachStateChangeListener();
                mAttachedViewRf = null;
                mWindow.dismiss();
            }, delayMillis);
        }
    }

    public abstract void show(@NonNull View anchor);
}
