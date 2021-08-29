package top.xcore.xdata;

public interface Converter<T extends XDataWrapper> {
    public T convert(XData xData);
}
