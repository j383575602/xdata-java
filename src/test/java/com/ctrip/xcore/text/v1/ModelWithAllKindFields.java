package com.ctrip.xcore.text.v1;

import top.xcore.xdata.XType;

/**
 * Created by wlzhao on 2017/9/16.
 */
public interface ModelWithAllKindFields {

    int TYPE_INDEX = ((XType.TYPE_PROJECT_START >> 13) + 2) << 13;

    int model_field = 1 | SimpleModel.TYPE_INDEX;
    /**
     * com.ctrip.xcore.text.v1.SimpleModel List类型的字段
     */
    int model_list_field = 2 | SimpleModel.TYPE_INDEX | XType.MASK_TYPE_COLLECTION_LIST;

}
