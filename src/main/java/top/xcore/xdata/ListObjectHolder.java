package top.xcore.xdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ListObjectHolder<T extends XDataWrapper> {
    private List<T> _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public ListObjectHolder(XDataWrapper source, int index,Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  List<T> get() {
        if (_cache == null) {
            List<XData> datas = (List<XData>)_source.getDataList(_index);
            if (datas == null) {
                return null;
            }
            _cache = new ArrayList<>();
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

    public void set(List<T> obj) {
        _cache = obj;
        _source.set(_index,obj);
    }
}
