package com.ctrip.xcore.text.v1.xcore.text.v1.wraper;

import top.xcore.xdata.XData;
import top.xcore.xdata.XDataWrapper;
import com.ctrip.xcore.text.v1.ModelWithAllKindFields;
import com.ctrip.xcore.text.v1.SimpleModel;

/**
 * Created by wlzhao on 2017/9/19.
 */
public class ModelWithAllKindFieldsWrapper extends XDataWrapper {
    public ModelWithAllKindFieldsWrapper(XData data) {
        super(data);
    }

    public ModelWithAllKindFieldsWrapper() {
        super(ModelWithAllKindFields.TYPE_INDEX);
    }

    public void setModel_field(SimpleModelWrapper b) {
        set(ModelWithAllKindFields.model_field,b);
    }

    private SimpleModelWrapper model_field;
    public SimpleModelWrapper getModelField() {
        if (model_field != null) {
            return model_field;
        }
        XData xData = getData(ModelWithAllKindFields.model_field);
        if (xData instanceof  SimpleModelWrapper) {
            model_field = (SimpleModelWrapper) xData;
            return model_field;
        }
        model_field = new SimpleModelWrapper(xData);
        return model_field;
    }

    public void setShort_field(short b) {
        set(SimpleModel.short_field,b);
    }
}
