package top.xcore.xdata;

/**
 * Created by wlzhao on 2017/4/3.
 * 该表记录了OWNER_TYPE 表与 REF_TYPE 表中元素的关联关系，owner（ownerId）中的字段（owner_prop）来自于ref_type表中的ref_id行
 */
public class XObjectRefWrapper extends XBaseRecordWrapper {
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

    public XObjectRefWrapper(XData data) {
        super(data);
    }

    public XObjectRefWrapper() {
        super(XType.TYPE_OBJECT_REF);
    }

    protected XObjectRefWrapper(int type) {
        super(type);
    }

    public int getOWNER_ID() {
        return super.getInteger(XObjectRef.OWNER_ID);
    }

    public void setOWNER_ID(int OWNER_ID) {
        super.set(XObjectRef.OWNER_ID,OWNER_ID);
    }

    public int getOWNER_TYPE() {
        return super.getInteger(XObjectRef.OWNER_TYPE);
    }

    public void setOWNER_TYPE(int OWNER_TYPE) {
        super.set(XObjectRef.OWNER_TYPE,OWNER_TYPE);
    }

    public int getOWNER_PROP() {
        return super.getInteger(XObjectRef.OWNER_PROP);
    }

    public void setOWNER_PROP(int OWNER_PROP) {
        super.set(XObjectRef.OWNER_PROP,OWNER_PROP);
    }

    public int getREF_TYPE() {
        return super.getInteger(XObjectRef.REF_TYPE);
    }

    public void setREF_TYPE(int REF_TYPE) {
        super.set(XObjectRef.REF_TYPE,REF_TYPE);
    }

    public int getREF_ID() {
        return super.getInteger(XObjectRef.REF_ID);
    }

    public void setREF_ID(int REF_ID) {
        super.set(XObjectRef.REF_ID,REF_ID);
    }
}
