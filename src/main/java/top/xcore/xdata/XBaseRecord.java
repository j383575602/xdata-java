package top.xcore.xdata;

/**
 * Created by wlzhao on 2017/3/6.
 */
public interface XBaseRecord {
    int TYPE_INDEX     = XType.TYPE_BASS_RECORD;
    int _ID            = 1 | XType.TYPE_BYTE_i_4;
    int STATUS         = 2 | XType.TYPE_BYTE_i_1;
    int ADD_VERSION    = 3 | XType.TYPE_BYTE_i_2;
    int VERSION        = 4 | XType.TYPE_BYTE_i_2;
}
