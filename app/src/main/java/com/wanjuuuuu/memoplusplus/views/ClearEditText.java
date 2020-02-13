package com.wanjuuuuu.memoplusplus.views;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.widget.AppCompatEditText;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wanjuuuuu.memoplusplus.R;

public class ClearEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener {

    private Drawable mClearButton;

    public ClearEditText(Context context) {
        super(context);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mClearButton = getResources().getDrawable(R.drawable.ic_cancel, null);
        mClearButton.setBounds(0, 0, mClearButton.getIntrinsicWidth(),
                mClearButton.getIntrinsicHeight());
        setClearBtnVisible(false);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isFocused()) {
                    setClearBtnVisible(charSequence.length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        super.setOnFocusChangeListener(this);
        super.setOnTouchListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            Editable text = getText();
            setClearBtnVisible(text != null && text.length() > 0);
        } else {
            setClearBtnVisible(false);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        if (mClearButton.isVisible() && x > getWidth() - getPaddingRight() - mClearButton.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setText("");
                setHint(getHint());
            }
            return true;
        }
        return false;
    }

    private void setClearBtnVisible(boolean needVisible) {
        mClearButton.setVisible(needVisible, false);
        setCompoundDrawables(null, null, needVisible ? mClearButton : null, null);
    }
}