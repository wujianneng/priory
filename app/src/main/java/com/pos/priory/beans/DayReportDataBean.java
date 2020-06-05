package com.pos.priory.beans;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class DayReportDataBean implements MultiItemEntity {
    public int itemType = 0;

    @Override
    public int getItemType() {
        return itemType;
    }
}
