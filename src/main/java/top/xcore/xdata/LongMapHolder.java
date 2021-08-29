package top.xcore.xdata;

import java.util.HashMap;
import java.util.Map;

public class LongMapHolder<T extends XDataWrapper> {
    private Map<Long,T> _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public LongMapHolder(XDataWrapper source, int index, Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  Map<Long,T> get() {
        if (_cache == null) {
            Map<Long,XData> datas = (Map<Long,XData>)_source.getDataMap(_index);
            if (datas == null) {
                return null;
            }
            _cache = new HashMap<>();
            for(Map.Entry<Long,XData> entry : datas.entrySet()) {
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

    public void set(Map<Long,T> obj) {
        _cache = obj;
        _source.set(_index,obj);
    }
}
