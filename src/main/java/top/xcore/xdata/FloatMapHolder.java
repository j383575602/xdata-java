package top.xcore.xdata;

import java.util.HashMap;
import java.util.Map;

public class FloatMapHolder<T extends XDataWrapper> {
    private Map<Float,T> _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public FloatMapHolder(XDataWrapper source, int index, Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  Map<Float,T> get() {
        if (_cache == null) {
            Map<Float,XData> datas = (Map<Float,XData>)_source.getDataMap(_index);
            if (datas == null) {
                return null;
            }
            _cache = new HashMap<>();
            for(Map.Entry<Float,XData> entry : datas.entrySet()) {
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

    public void set(Map<Float,T> obj) {
        _cache = obj;
        _source.set(_index,obj);
    }
}
