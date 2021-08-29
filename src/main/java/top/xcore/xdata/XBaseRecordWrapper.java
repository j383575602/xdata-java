package top.xcore.xdata;

public class XBaseRecordWrapper extends XDataWrapper{
    public XBaseRecordWrapper(XData data) {
        super(data);
    }

    public XBaseRecordWrapper() {
        super(XBaseRecord.TYPE_INDEX);
    }

    protected XBaseRecordWrapper(int type) {
        super(type);
    }

    public int getID() {
        return super.getInteger(XBaseRecord._ID);
    }

    public void setID(int id) {
        super.set(XBaseRecord._ID,id);
    }

    public byte getSTATUS() {
        return super.getByte(XBaseRecord.STATUS);
    }

    public void setSTATUS(byte status) {
        super.set(XBaseRecord.STATUS,status);
    }

    public short getADD_VERSION() {
        return super.getShort(XBaseRecord.ADD_VERSION);
    }

    public void setADD_VERSION(short add_version) {
        super.set(XBaseRecord.ADD_VERSION,add_version);
    }

    public short getVERSION() {
        return super.getShort(XBaseRecord.VERSION);
    }

    public void setVERSION(short version) {
        super.set(XBaseRecord.VERSION, version);
    }
}
