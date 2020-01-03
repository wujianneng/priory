package com.pos.priory.adapters;

import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pos.priory.R;
import com.pos.priory.beans.PayTypeBean;

import java.util.List;

public class PayTypeAdapter extends BaseQuickAdapter<PayTypeBean, BaseViewHolder> {

    public PayTypeAdapter(int layoutResId, @Nullable List<PayTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PayTypeBean item) {
        helper.setText(R.id.name_tv, item.getPayTypeName());
        final EditText edtWeight = helper.getView(R.id.edt_view);
        edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
        edtWeight.setText(item.getPayAmount() + "");
        edtWeight.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String str = s.toString();
                        item.setPayAmount(TextUtils.isEmpty(str) ? 0 : Double.parseDouble(str));
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };
                edtWeight.setTag(textWatcher);
                edtWeight.addTextChangedListener(textWatcher);
            } else {
                edtWeight.removeTextChangedListener((TextWatcher) edtWeight.getTag());
                edtWeight.setText(item.getPayAmount() + "");
            }
        });
    }
}
