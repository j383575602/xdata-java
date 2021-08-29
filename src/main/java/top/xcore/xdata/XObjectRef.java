package top.xcore.xdata;

/**
 * Created by wlzhao on 2017/4/3.
 * 该表记录了OWNER_TYPE 表与 REF_TYPE 表中元素的关联关系，owner（ownerId）中的字段（owner_prop）来自于ref_type表中的ref_id行
 */
public interface XObjectRef extends XBaseRecord {
    int TYPE_INDEX = XType.TYPE_OBJECT_REF;
    /**
     * 引用关系的所有者
     */
    int OWNER_ID   = 5 | XType.TYPE_BYTE_i_4;
    /**
     * 所有者的类型，在查询有个所有者持有其他类的引用时的查询条件
     */
    int OWNER_TYPE = 6 | XType.TYPE_BYTE_i_4;
    /**
     * 所有者的哪一个属性上的引用
     */
    int OWNER_PROP = 7 | XType.TYPE_BYTE_i_4;
    /**
     * 被应用对象的类型
     */
    int REF_TYPE   = 8 | XType.TYPE_BYTE_i_4;
    /**
     * 被引用对象的ID
     */
    int REF_ID     = 9 | XType.TYPE_BYTE_i_4;
}
