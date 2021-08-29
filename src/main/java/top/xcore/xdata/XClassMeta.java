package top.xcore.xdata;

/**
 * Created by wlzhao on 2017/3/6.
 */
public interface XClassMeta {
    int TYPE_INDEX     = XType.TYPE_CLASS_META;
    int TYPE         = 1 | XType.TYPE_BYTE_i_4;
    int NAME         = 2 | XType.TYPE_STRING;
    int FIELDS       = 3 | XType.TYPE_FIELD_META | XType.MASK_TYPE_COLLECTION_LIST;

}
