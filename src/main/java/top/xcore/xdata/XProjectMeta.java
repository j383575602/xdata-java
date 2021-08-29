package top.xcore.xdata;

/**
 * Created by wlzhao on 2017/3/6.
 */
public interface XProjectMeta {
    int TYPE_INDEX     = XType.TYPE_PROJECT_META;
    int NAME           = 1 | XType.TYPE_STRING;
    int CLASSES        = 2 | XType.TYPE_CLASS_META | XType.MASK_TYPE_COLLECTION_LIST;

}
