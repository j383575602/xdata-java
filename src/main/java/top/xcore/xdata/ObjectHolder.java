package top.xcore.xdata;

public class ObjectHolder<T extends XDataWrapper> {
    private Object _cache;
    private XDataWrapper _source;
    private int _index;
    private Converter<T> _converter;
    public ObjectHolder(XDataWrapper source,int index ,Converter<T> converter) {
        this._source = source;
        this._index = index;
        this._converter = converter;
    }

    public  T get() {
        if (_cache == null) {
            XData xData  = _source.getData(_index);
            if (xData == null) {
                return null;
            }
            if (xData instanceof XDataWrapper) {
                _cache = xData;
            } else {
                _cache = newInstance(xData);
            }
        }
        return (T)_cache;
    }

    public void set(Object obj) {
        _cache = obj;
        _source.set(_index,obj);
    }

    protected  T newInstance(XData xData) {
        return _converter.convert(xData);
    }
}
