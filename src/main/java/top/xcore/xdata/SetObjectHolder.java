package top.xcore.xdata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetObjectHolder<T extends XDataWrapper> {
    private Set<T> _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public SetObjectHolder(XDataWrapper source, int index, Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  Set<T> get() {
        if (_cache == null) {
            Set<XData> datas = (Set<XData>)_source.getDataSet(_index);
            if (datas == null) {
                return null;
            }
            _cache = new HashSet<>();
            for(XData data: datas) {
                if (data instanceof XDataWrapper) {
                    T instance = (T) data;
                    _cache.add(instance);
                } else {
                    T ele = newInstance(data);
                    _cache.add(ele);
                }
            }
        }
        return _cache;
    }

    protected T newInstance(XData xData) {
        return _converter.convert(xData);
    }

    public void set(Set<T> obj) {
        _cache = obj;
        _source.set(_index,obj);
    }
}
