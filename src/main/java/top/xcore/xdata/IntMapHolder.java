package top.xcore.xdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntMapHolder<T extends XDataWrapper> {
    private Map<Integer,T> _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public IntMapHolder(XDataWrapper source, int index, Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  Map<Integer,T> get() {
        if (_cache == null) {
            Map<Integer,XData> datas = (Map<Integer,XData>)_source.getDataMap(_index);
            if (datas == null) {
                return null;
            }
            _cache = new HashMap<>();
            for(Map.Entry<Integer,XData> entry : datas.entrySet()) {
                if (entry.getValue() instanceof XDataWrapper) {
                    T instance = (T) entry.getValue();
                    _cache.put(entry.getKey(),instance);
                } else {
                    T ele = newInstance(entry.getValue());
                    _cache.put(entry.getKey(),ele);
                }
            }
        }
        return _cache;
    }

    protected T newInstance(XData xData) {
        return _converter.convert(xData);
    }

    public void set(Map<Integer,T> obj) {
        _cache = obj;
        _source.set(_index,obj);
    }
}
