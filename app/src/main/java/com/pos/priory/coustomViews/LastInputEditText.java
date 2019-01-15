package com.pos.priory.coustomViews;

/**
 * Created by Lenovo on 2019/1/15.
 */

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

/**
 * 只能在最后输入数据的输入框
 * <pre>
 *     author  : Fantasy
 *     version : 1.0, 2018-08-11
 *     since   : 1.0, 2018-08-11
 * </pre>
 */
public class LastInputEditText extends AppCompatEditText {

    public LastInputEditText(Context context) {
        super(context);
    }

    public LastInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LastInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (selStart == selEnd) { // 防止不能多选
            setSelection(getText().length()); // 保证光标始终在最后面
        }
    }

}