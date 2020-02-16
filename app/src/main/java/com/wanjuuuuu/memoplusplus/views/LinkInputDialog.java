package com.wanjuuuuu.memoplusplus.views;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;

import com.wanjuuuuu.memoplusplus.R;

public class LinkInputDialog {

    private Context mContext;
    private ClearEditText mEditText;
    private AlertDialog mDialog;

    public LinkInputDialog(Context context) {
        mContext = context;
        mDialog = createDialog();
    }

    public void show() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    // need to call after show dialog
    public void setPositiveListener(View.OnClickListener listener) {
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }
        Button button = mDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (button == null) {
            return;
        }
        button.setOnClickListener(listener);
    }

    public String getLinkUrl() {
        Editable text = mEditText.getText();
        if (text == null) {
            return null;
        }
        return text.toString();
    }

    private AlertDialog createDialog() {
        FrameLayout frameLayout = new FrameLayout(mContext);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

        int left = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_edit_text_marginLeft);
        int top = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_edit_text_marginTop);
        int right = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_edit_text_marginRight);
        int bottom = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_edit_text_marginBottom);
        layoutParams.setMargins(left, top, right, bottom);

        mEditText = new ClearEditText(mContext);
        mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        mEditText.setLayoutParams(layoutParams);
        frameLayout.addView(mEditText);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setView(frameLayout);
        dialogBuilder.setTitle(mContext.getResources().getString(R.string.dialog_link_description));
        dialogBuilder.setPositiveButton(mContext.getResources().getString(R.string.dialog_positive_button), null);
        dialogBuilder.setNegativeButton(mContext.getResources().getString(R.string.dialog_negative_button), null);

        return dialogBuilder.create();
    }
}
