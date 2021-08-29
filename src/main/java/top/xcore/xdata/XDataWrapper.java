package top.xcore.xdata;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wlzhao on 2017/9/19.
 */
public class XDataWrapper extends XData {
    XData _data;
    public XDataWrapper(XData data) {
        super(data.getType());
        setData(data);
        initFieldValueHolder();
    }

    protected void setData(XData _data) {
        if (_data instanceof XDataWrapper) {
            throw new RuntimeException("wrap a wrapper is not allowed. Must a XData object,not it's decedents");
        }
        this._data = _data;
    }

    protected XDataWrapper(int type) {
        super(type);
        initFieldValueHolder();
    }

    public <T extends XDataWrapper> T wraper(XData xData) {
        return null;
    }

    protected void initFieldValueHolder() {

    }

    @Override
    public void set(int index, Object value) {
        if (_data == null) {
            super.set(index,value);
        } else {
            _data.set(index,value);
        }
    }

    @Override
    public byte getByte(int index) {
        return _data == null ? super.getByte(index) : _data.getByte(index);
    }

    @Override
    public short getShort(int index) {
        return _data == null ? super.getShort(index) : _data.getShort(index);
    }

    @Override
    public int getInteger(int index) {
        return _data == null ? super.getInteger(index) :_data.getInteger(index);
    }

    @Override
    public long getLong(int index) {
        return _data == null ? super.getLong(index) : _data.getLong(index);
    }

    @Override
    public float getFloat(int index) {
        return _data == null ? super.getFloat(index) : _data.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return _data == null ? super.getDouble(index) : _data.getDouble(index);
    }

    @Override
    public String getString(int index) {
        return _data == null ? super.getString(index ) : _data.getString(index);
    }

    @Override
    public XData getData(int index) {
        return _data == null ? super.getData(index) : _data.getData(index);
    }

    @Override
    public boolean getBoolean(int index) {
        return _data == null ? super.getBoolean(index ) : _data.getBoolean(index);
    }

    @Override
    public byte[] getBlob(int index) {
        return _data == null ? super.getBlob(index ) : _data.getBlob(index);
    }


    @Override
    public Date getDate(int index) {
        return _data == null ? super.getDate(index) : _data.getDate(index);
    }

    @Override
    public List<?> getDataList(int index) {
        return _data == null ? super.getDataList(index ) : _data.getDataList(index);
    }

    @Override
    public Set<?> getDataSet(int index) {
        return _data == null ? super.getDataSet(index ) : _data.getDataSet(index);
    }

    @Override
    public Map<?, ?> getDataMap(int index) {
        return _data == null ? super.getDataMap(index ) : _data.getDataMap(index);
    }

    @Override
    public int getType() {
        return _data == null ? super.getType() : _data.getType();
    }
}
