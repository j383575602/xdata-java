package com.ctrip.xcore.text.v1;

import top.xcore.xdata.XType;

/**
 * Created by wlzhao on 2017/9/16.
 */
public interface SimpleModel {
    int TYPE_INDEX = ((XType.TYPE_PROJECT_START >> 13) + 1) << 13;
    /**
     * byte型的字段
     */
    int byte_field = 1 | XType.TYPE_BYTE_i_1;
    /**
     * short型的字段
     */
    int short_field = 2 | XType.TYPE_BYTE_i_2;
    /**
     * int型的字段
     */
    int int_field = 4 | XType.TYPE_BYTE_i_4;
    /**
     * long型的字段
     */
    int long_field = 5 | XType.TYPE_BYTE_i_8;
    /**
     * float型的字段
     */
    int float_field = 6 | XType.TYPE_BYTE_f_4;
    /**
     * double型的字段
     */
    int double_field = 7 | XType.TYPE_BYTE_f_8;
    /**
     * String型的字段
     */
    int string_field = 8 | XType.TYPE_STRING;
}
