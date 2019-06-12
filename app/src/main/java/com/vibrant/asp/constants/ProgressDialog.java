package com.vibrant.asp.constants;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vibrant.asp.R;


public class ProgressDialog extends Dialog {


    public ProgressDialog(Context context) {
        super(context);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    public static ProgressDialog show(Context context) {
        if (context == null) {
            return null;
        }
        ProgressDialog dialog = new ProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_bar);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog show(Context context, String msg) {
        if (context == null) {
            return null;
        }
        ProgressDialog dialog = new ProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("");
        View view = (LayoutInflater.from(context)).inflate(R.layout.progress_bar, null);
        TextView tv_progress_bar_text = (TextView) view.findViewById(R.id.tv_progress_bar_text);
        tv_progress_bar_text.setText(getMessage(msg));
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }


    public interface OnInitValues {
        void init(ProgressBar progressBar);
    }

    public static String getMessage(String msg) {
        if (msg == null)
            return "Please wait...";
        return msg;
    }
}
