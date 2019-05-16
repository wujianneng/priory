package com.pos.priory.coustomViews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisableReclyerView extends RecyclerView {

    public DisableReclyerView(@NonNull Context context) {
        super(context);
    }

    public DisableReclyerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
