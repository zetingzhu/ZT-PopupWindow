package com.zzt.popupwindows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Movie;
import android.graphics.Rect;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zzt.popupwindow.R;
import com.zzt.popupwindow.ZBubbleContent;
import com.zzt.popupwindows.dialog.MyTestDialog;
import com.zzt.popupwindows.dialog.MyTestPopupWindows;
import com.zzt.popupwindows.dialog.MyTestTouchPopup;
import com.zzt.popupwindows.dialog.ShowBottomLayoutUtil;
import com.zzt.popupwindows.library.ZFullScreenPopup;
import com.zzt.popupwindows.library.ZGuidePopup;
import com.zzt.popupwindows.library.ZDirection;
import com.zzt.popupwindows.library.ZNormalPopup;
import com.zzt.popupwindows.library.ZShapeType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.TimeZone;


public class PopupSampleActivity extends AppCompatActivity {
    private static final String TAG = PopupSampleActivity.class.getSimpleName();
    TextView tv_text1, tv_text2, tv_text3, tv_text4, tv_text5,
            tv_text6, tv_text7, tv_text8, tv_text9, tv_text10, tv_text11,
            tv_text12;
    ZBubbleContent zBubbleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_sample);

        initTest();

        initView();

        getGifTime();
    }
//

    private void initTest() {
        Log.d(TAG, "时区获取 1：" + TimeZone.getDefault().getRawOffset());
        Log.d(TAG, "时区获取 11：" + TimeZone.getDefault().getRawOffset() / (1000 * 60 * 60));
        Log.d(TAG, "时区获取 2：" + TimeZone.getDefault().getDisplayName());
        Log.d(TAG, "时区获取 3：" + TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
        Log.d(TAG, "时区获取 4：" + TimeZone.getDefault().getDisplayName(true, TimeZone.SHORT));
        Log.d(TAG, "时区获取 5：" + TimeZone.getDefault().getDisplayName(false, TimeZone.LONG));
        Log.d(TAG, "时区获取 6：" + TimeZone.getDefault().getID());
        Log.d(TAG, "时区获取 7：" + TimeZone.getDefault().getAvailableIDs().toString());
        Log.d(TAG, "时区获取 8：" + TimeZone.getDefault().getDSTSavings());
    }

    private void setTextViewGradientDown(TextView textView) {
        int[] colors = {Color.RED, Color.TRANSPARENT};
        float[] position = {0.7f, 1.0f};
        LinearGradient mLinearGradient = new LinearGradient(
                0, 0,
                0, textView.getPaint().getTextSize(),
                colors, position, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }

    private void setTextViewGradientUP(TextView textView) {
        int[] colors = {Color.TRANSPARENT, Color.RED};
        float[] position = {0.3f, 1f};
        LinearGradient mLinearGradient = new LinearGradient(
                0, 0,
                0, textView.getPaint().getTextSize(),
                colors, position, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(mLinearGradient);
        textView.invalidate();
    }

    int padding = 50;
    ZNormalPopup zNormalPopup;

    public void initView() {
        zNormalPopup = new ZNormalPopup(
                this,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        tv_text1 = findViewById(R.id.tv_text1);
        tv_text2 = findViewById(R.id.tv_text2);
        tv_text3 = findViewById(R.id.tv_text3);
        tv_text4 = findViewById(R.id.tv_text4);
        tv_text5 = findViewById(R.id.tv_text5);
        tv_text6 = findViewById(R.id.tv_text6);
        tv_text7 = findViewById(R.id.tv_text7);
        tv_text8 = findViewById(R.id.tv_text8);
        tv_text9 = findViewById(R.id.tv_text9);
        tv_text10 = findViewById(R.id.tv_text10);
        tv_text11 = findViewById(R.id.tv_text11);
        tv_text12 = findViewById(R.id.tv_text12);

        tv_text12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(v.getContext());
                textView.setTextColor(ContextCompat.getColor(v.getContext(), R.color.color_77FF89));
                textView.setText("AAAAAAAAAAAAAAAAAA");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zBubbleContent.dismissBubble(v);
                    }
                });
                if (zBubbleContent == null) {
                    zBubbleContent = new ZBubbleContent(getWindow());
                }
                zBubbleContent.setLayoutGravity(ZBubbleContent.ZLayoutGravity.LAYOUT_GRAVITY_END)
                        .setAnchorGravity(ZBubbleContent.ZAnchorGravity.ANCHOR_GRAVITY_DOWN)
                        .setMarginDimen(v.getContext().getResources().getDimensionPixelSize(R.dimen.dimen_16dp))
                        .showBubble(tv_text12, textView);
            }
        });

        tv_text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(PopupSampleActivity.this);

                textView.setPadding(padding, padding, padding, padding);
                textView.setText("外部可以触摸，不消失当前的");
                textView.setTextColor(
                        getResources().getColor(R.color.salmon)
                );
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "this text click ");
                    }
                });

                zNormalPopup.preferredDirection(ZDirection.DIRECTION_BOTTOM)
                        .view(textView)
                        .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                        .bgColorGradient(new int[]{ContextCompat.getColor(v.getContext(), R.color.color_FFD333), ContextCompat.getColor(v.getContext(), R.color.color_FF8702)})
                        .borderColor(getResources().getColor(R.color.magenta))
                        .borderWidth(0)
                        .radius(10)
                        .arrow(true)
                        .arrowSize(dp2px(v.getContext(), 30), dp2px(v.getContext(), 30))
                        .dimAmount(0.6f);
//                zNormalPopup.dismissIfOutsideTouch(false);
                zNormalPopup.setTouchable(false);
                zNormalPopup.show(v);
            }
        });

        tv_text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
                View decorView = getWindow().getDecorView();
                if (contentView instanceof FrameLayout) {
                    ShowBottomLayoutUtil showBottomPopup = new ShowBottomLayoutUtil(PopupSampleActivity.this, (FrameLayout) contentView);
                    ZFullScreenPopup fullScreenPopup = new ZFullScreenPopup(v.getContext());
                    View inflate = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_popup_windows_touch, null);
                    View viewById = inflate.findViewById(R.id.button3);
                    View tv_content = inflate.findViewById(R.id.tv_content);
                    tv_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "点击了这个里面的内容");
                        }
                    });
                    viewById.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fullScreenPopup.dismiss();
                        }
                    });
                    showBottomPopup.addView(inflate);
                    showBottomPopup.showView();
                }
            }
        });

        tv_text9.setOnClickListener(v -> {

            ZFullScreenPopup fullScreenPopup = new ZFullScreenPopup(v.getContext());
            View inflate = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_popup_windows_touch, null);
            View viewById = inflate.findViewById(R.id.button3);
            View tv_content = inflate.findViewById(R.id.tv_content);
            tv_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "点击了这个里面的内容");
                }
            });
            viewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fullScreenPopup.dismiss();
                }
            });
            fullScreenPopup.addView(inflate);
            fullScreenPopup.setTouchable(false);
//            fullScreenPopup.setFocusable(false);
            fullScreenPopup.dismissIfOutsideTouch(false);
            fullScreenPopup.show(v);
        });

        tv_text8.setOnClickListener(v -> {
//            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.guide_test_v1, null, false);
            ZGuidePopup guidePopup = new ZGuidePopup(
                    v.getContext(),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ).preferredDirection(ZDirection.DIRECTION_BOTTOM)
                    .offsetYIfBottom(dp2px(v.getContext(), 10))
                    .setLightRadius(dp2px(getBaseContext(), 10))
                    .setLightShape(ZShapeType.RECTANGULAR)
                    .setViewPadding(20, 20, 20, 20)
                    .setNightBackgroundColor(getResources().getColor(R.color.color_77FF89))

                    .view(R.layout.guide_test_v1);
            guidePopup.dismissIfOutsideTouch(true);
            guidePopup.addGuideDismissListener(new ZGuidePopup.GuideDismissListener() {
                @Override
                public boolean touchDismiss() {
                    Log.d(TAG, "这里监听点击了取消");
                    return false;
                }
            });
            guidePopup.addAttachedToWindowListener(new ZGuidePopup.AttachedToWindowListener() {
                @Override
                public void onAttachedToWindow(ZGuidePopup.ShowInfo showInfo) {

                }
            });
            guidePopup.show(v);
        });

        tv_text7.setOnClickListener(v -> {
            MyTestTouchPopup touchPopup = new MyTestTouchPopup(PopupSampleActivity.this);
            touchPopup.showAtLocation(v, Gravity.CENTER, 0, 0);
        });

        tv_text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "点击了 点击了外面的第六个按钮");

                MyTestDialog myTextDialog = new MyTestDialog(PopupSampleActivity.this);
                myTextDialog.setButtonClick1(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyTestPopupWindows popupWindows = new MyTestPopupWindows(PopupSampleActivity.this);
                        popupWindows.showAtLocation(myTextDialog.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                    }
                });
                myTextDialog.setButtonClick2(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                myTextDialog.setCancelable(false);
                myTextDialog.show();
            }
        });


        setTextViewGradientDown(tv_text4);
        setTextViewGradientUP(tv_text5);


        tv_text1.setBackgroundColor(getResources().getColor(R.color.color_77FF89));
        tv_text4.setBackgroundColor(getResources().getColor(R.color.color_77FF89));
        tv_text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRect(v);
                TextView textView = new TextView(PopupSampleActivity.this);
                textView.setPadding(padding, padding, padding, padding);
                textView.setBackgroundColor(getResources().getColor(R.color.color_FFD333));
                textView.setText("通过 dimAmount() 设置背景遮罩");
                textView.setTextColor(
                        getResources().getColor(R.color.salmon)
                );
                new ZGuidePopup(
                        v.getContext(),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ).preferredDirection(ZDirection.DIRECTION_BOTTOM)
//                        .offsetYIfBottom(dp2px(v.getContext(), 10))
                        .view(textView)
                        .show(v);
            }
        });
        tv_text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(PopupSampleActivity.this);
                textView.setPadding(padding, padding, padding, padding);
                textView.setBackgroundColor(getResources().getColor(R.color.color_FFD333));
                textView.setText("通过 dimAmount() 设置背景遮罩  这个显示在上面");
                textView.setTextColor(
                        getResources().getColor(R.color.salmon)
                );
                new ZGuidePopup(
                        v.getContext(),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ).preferredDirection(ZDirection.DIRECTION_TOP)
//                        .offsetYIfTop(dp2px(v.getContext(), 10))
                        .view(textView)
                        .show(v);
            }
        });
        tv_text1.setOnClickListener(v -> {
            Log.e(TAG, "click button text 1");
            getRect(v);
            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZDirection.DIRECTION_BOTTOM)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .bgColorGradient(new int[]{ContextCompat.getColor(v.getContext(), R.color.color_FFD333), ContextCompat.getColor(v.getContext(), R.color.color_FF8702)})
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(0)
                    .radius(10)
                    .arrow(true)
                    .arrowSize(dp2px(v.getContext(), 30), dp2px(v.getContext(), 30))
                    .dimAmount(0.6f)
                    .show(v);
        });
        tv_text2.setOnClickListener(v -> {
            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZDirection.DIRECTION_TOP)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .bgColorGradient(new int[]{ContextCompat.getColor(v.getContext(), R.color.color_FFD333), ContextCompat.getColor(v.getContext(), R.color.color_FF8702)})
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(0)
                    .radius(dp2px(v.getContext(), 10))
                    .arrow(true)
                    .arrowSize(dp2px(v.getContext(), 20), dp2px(v.getContext(), 20))
                    .offsetYIfBottom(dp2px(v.getContext(), 0))
                    .dimAmount(0.6f)
                    .show(v);
            zNormalPopup.dismissIfOutsideTouch(true);
            //endregion
        });
        tv_text3.setOnClickListener(v -> {

            //region 这个可以的
            TextView textView = new TextView(PopupSampleActivity.this);


            textView.setPadding(padding, padding, padding, padding);
            textView.setText("通过 dimAmount() 设置背景遮罩");
            textView.setTextColor(
                    getResources().getColor(R.color.salmon)
            );

            zNormalPopup.preferredDirection(ZDirection.DIRECTION_CENTER_IN_SCREEN)
                    .view(textView)
                    .bgColor(getResources().getColor(R.color.qmui_config_color_white))
                    .borderColor(getResources().getColor(R.color.magenta))
                    .borderWidth(5)
                    .radius(10)
                    .dimAmount(0.6f)
                    .onDismiss(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {

                        }
                    })
                    .show(v);
            //endregion
        });
    }

    public int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }


    private void getRect(View anchor) {
        int[] anchorRootWindow = new int[2];
        int[] anchorWindow = new int[2];
        anchor.getRootView().getLocationInWindow(anchorRootWindow);
        anchor.getLocationInWindow(anchorWindow);

        Log.d(TAG, "Window RootWindow:" + Arrays.toString(anchorRootWindow));
        Log.d(TAG, "Window anchorWindow:" + Arrays.toString(anchorWindow));

        int[] anchorRootLocation = new int[2];
        int[] anchorLocation = new int[2];
        anchor.getRootView().getLocationOnScreen(anchorRootLocation);
        anchor.getLocationOnScreen(anchorLocation);

        Log.i(TAG, "Location RootLocation :" + Arrays.toString(anchorRootLocation));
        Log.i(TAG, "Location anchorLocation:" + Arrays.toString(anchorLocation));

        Rect visibleWindowFrame = new Rect(); // 当前绑定view的窗口区域
        anchor.getWindowVisibleDisplayFrame(visibleWindowFrame);
        Log.w(TAG, "WindowDisplay:" + visibleWindowFrame.toString());
    }


    public void getGifTime() {
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(R.raw.app_ic_logo);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            Movie movie = Movie.decodeByteArray(bytes, 0, bytes.length);
            int time = movie.duration();
            Log.d(TAG, "获取动画时间：" + time);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.app_ic_logo);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        byte[] bytes = baos.toByteArray();
//        Movie movie = Movie.decodeByteArray(bytes, 0, bytes.length);
//        int time = movie.duration();
//        Log.d(TAG, "获取动画时间 2：" + time);


        InputStream is = getResources().openRawResource(R.raw.app_ic_logo);
        Movie movie3 = Movie.decodeStream(is);
        int time = movie3.duration();
        Log.d(TAG, "获取动画时间 3：" + time);

    }
}