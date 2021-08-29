package com.ctrip.xcore.text.v1.xcore.text.v1.wraper;

import top.xcore.xdata.XData;
import top.xcore.xdata.XDataWrapper;
import com.ctrip.xcore.text.v1.SimpleModel;

/**
 * Created by wlzhao on 2017/9/19.
 */
public class SimpleModelWrapper extends XDataWrapper {
    public SimpleModelWrapper(XData data) {
        super(data);
    }

    public SimpleModelWrapper() {
        super(SimpleModel.TYPE_INDEX);
    }

    public void setByte_field(byte b) {
        set(SimpleModel.byte_field,b);
    }

    public byte getByte_field() {
        return getByte(SimpleModel.byte_field);
    }

    public void setShort_field(short b) {
        set(SimpleModel.short_field,b);
    }

    public short getShort_field() {
        return getShort(SimpleModel.short_field);
    }
}
