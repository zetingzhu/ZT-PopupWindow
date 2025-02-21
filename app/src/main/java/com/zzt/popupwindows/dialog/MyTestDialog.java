package com.zzt.popupwindows.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zzt.popupwindow.R;


/**
 * @author: zeting
 * @date: 2021/7/28
 */
public class MyTestDialog extends Dialog {
    Button button, button2;
    View.OnClickListener buttonClick1;
    View.OnClickListener buttonClick2;

    public void setButtonClick1(View.OnClickListener buttonClick1) {
        this.buttonClick1 = buttonClick1;
    }

    public void setButtonClick2(View.OnClickListener buttonClick2) {
        this.buttonClick2 = buttonClick2;
    }

    public MyTestDialog(@NonNull Context context) {
        super(context);
    }

    public MyTestDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyTestDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_view_test);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button.setOnClickListener(buttonClick1);
        button2.setOnClickListener(buttonClick2);
    }
}
