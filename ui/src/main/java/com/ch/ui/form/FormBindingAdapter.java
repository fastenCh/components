package com.ch.ui.form;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class FormBindingAdapter {
    @BindingAdapter("android:text")
    public static void setText(FormText view, CharSequence text) {
        final CharSequence oldText = view.getText();
        if ((text == null && oldText.length() == 0) || (text != null && text.equals(oldText))) {
            return;
        }
        if (!haveContentsChanged(text, oldText)) {
            return;
        }
        view.setText(text);
    }

    @BindingAdapter("android:text")
    public static void setText(FormFiled view, CharSequence text) {
        final CharSequence oldText = view.getText();
        if ((text == null && oldText.length() == 0) || (text != null && text.equals(oldText))) {
            return;
        }
        if (!haveContentsChanged(text, oldText)) {
            return;
        }
        view.setText(text);
    }

    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static String getTextString(FormText view) {
        return view.getText().toString();
    }

    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static String getTextString(FormFiled view) {
        return view.getText().toString();
    }

    @BindingAdapter(value = "android:textAttrChanged")
    public static void setTextWatcher(FormFiled view, final InverseBindingListener textAttrChanged) {
        view.getMEtText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textAttrChanged != null) {
                    textAttrChanged.onChange();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @BindingAdapter(value = "android:textAttrChanged")
    public static void setTextWatcher(FormText view, final InverseBindingListener textAttrChanged) {
        view.getMTvText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textAttrChanged != null) {
                    textAttrChanged.onChange();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private static boolean haveContentsChanged(CharSequence str1, CharSequence str2) {
        if ((str1 == null) != (str2 == null)) {
            return true;
        } else if (str1 == null) {
            return false;
        }
        final int length = str1.length();
        if (length != str2.length()) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                return true;
            }
        }
        return false;
    }
}
