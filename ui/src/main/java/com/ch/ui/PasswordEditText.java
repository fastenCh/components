package com.ch.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 密码输入
 * @author ch
 * @date 2022年7月6日14:21:18
 */
public final class PasswordEditText extends AppCompatEditText
        implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcher {

    private final boolean isAlwaysShow;
    private Drawable mCurrentDrawable;
    private final Drawable mVisibleDrawable;
    private final Drawable mInvisibleDrawable;

    private OnTouchListener mTouchListener;
    private OnFocusChangeListener mFocusChangeListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    @SuppressWarnings("all")
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);

        mVisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_visibility_off));
        mVisibleDrawable.setBounds(0, 0, mVisibleDrawable.getIntrinsicWidth(), mVisibleDrawable.getIntrinsicHeight());

        mInvisibleDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_visibility));
        mInvisibleDrawable.setBounds(0, 0, mInvisibleDrawable.getIntrinsicWidth(), mInvisibleDrawable.getIntrinsicHeight());

        boolean isShow = a.getBoolean(R.styleable.PasswordEditText_isShow, false);
        mCurrentDrawable = isShow ? mInvisibleDrawable : mVisibleDrawable;
        setTransformationMethod(isShow ? HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
        isAlwaysShow = a.getBoolean(R.styleable.PasswordEditText_isAlwaysShow, false);
        if (isAlwaysShow) refreshDrawableStatus();

        setDrawableVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        super.addTextChangedListener(this);
        a.recycle();
    }

    private void setDrawableVisible(boolean visible) {
        mCurrentDrawable.setVisible(visible, false);
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                isAlwaysShow || visible ? mCurrentDrawable : null,
                drawables[3]);
    }

    private void refreshDrawableStatus() {
        Drawable[] drawables = getCompoundDrawablesRelative();
        setCompoundDrawablesRelative(
                drawables[0],
                drawables[1],
                mCurrentDrawable,
                drawables[3]);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        mFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mTouchListener = onTouchListener;
    }

    /**
     * {@link OnFocusChangeListener}
     */

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus && getText() != null) {
            setDrawableVisible(getText().length() > 0);
        } else {
            setDrawableVisible(false);
        }
        if (mFocusChangeListener != null) {
            mFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    /**
     * {@link OnTouchListener}
     */

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int x = (int) event.getX();

        // 是否触摸了 Drawable
        boolean touchDrawable = false;
        // 获取布局方向
        int layoutDirection = getLayoutDirection();
        if (layoutDirection == LAYOUT_DIRECTION_LTR) {
            // 从左往右
            touchDrawable = x > getWidth() - mCurrentDrawable.getIntrinsicWidth() - getPaddingEnd() && x < getWidth() - getPaddingEnd();
        } else if (layoutDirection == LAYOUT_DIRECTION_RTL) {
            // 从右往左
            touchDrawable = x > getPaddingStart() && x < getPaddingStart() + mCurrentDrawable.getIntrinsicWidth();
        }

        if ( touchDrawable) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mCurrentDrawable == mVisibleDrawable) {
                    mCurrentDrawable = mInvisibleDrawable;
                    // 密码可见
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    refreshDrawableStatus();
                } else if (mCurrentDrawable == mInvisibleDrawable) {
                    mCurrentDrawable = mVisibleDrawable;
                    // 密码不可见
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                    refreshDrawableStatus();
                }
                Editable editable = getText();
                if (editable != null) {
                    setSelection(editable.toString().length());
                }
            }
            return true;
        }
        return mTouchListener != null && mTouchListener.onTouch(view, event);
    }

    /**
     * {@link TextWatcher}
     */

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isFocused()) {
            setDrawableVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
