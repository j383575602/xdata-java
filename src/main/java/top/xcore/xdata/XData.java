package top.xcore.xdata;


import java.util.*;

/**
 * Created by wlzhao on 2017/2/21.
 */
public class XData {
    private int type;
    HashMap<Integer,Object> fields = new HashMap<Integer,Object>();
    public void set(int index,Object value) {
        int basicType = index & XType.MASK_TYPE & ~XType.MASK_TYPE_COLLECTION;
        int collectionType = index & XType.MASK_TYPE_COLLECTION;
        boolean isCollection = collectionType != 0;
        if (isCollection) {
            if(value == null) {
                fields.remove(index);
            } else {
                setCollectionValue(index,collectionType,value);
            }
        } else if (basicType == XType.TYPE_BOOLEAN) {
            if (value == null) {
                fields.remove(index);
                return;
            }
            boolean v = (boolean) value;
            if (v == false) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_i_1) {
           if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for byte: for " + type+"." + index);
            }
            byte bvalue = (byte)((Number)value).intValue();
            if (bvalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_i_2) {
            if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for short: for " + type+"." + index);
            }
            short svalue = ((Number)value).shortValue();
            if (svalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_i_4) {
            if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for integer: for " + type+"." + index);
            }
            int ivalue = ((Number)value).intValue();
            if (ivalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_i_8) {
            if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for long: for " + type+"." + index);
            }
            long lvalue = ((Number)value).longValue();
            if (lvalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_f_4) {
            if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for float: for " + type+"." + index);
            }
            float fvalue = ((Number)value).floatValue();
            if (fvalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_BYTE_f_8) {
            if (!(value instanceof Number)) {
                throw new RuntimeException("wrong value:" + value +" for double: for " + type+"." + index);
            }
            double dvalue = ((Number)value).doubleValue();
            if (dvalue == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_STRING) {
            if (value == null || "".equals(value)) {
                fields.remove(index);
                return;
            }
            if (!(value instanceof String)) {
                throw new RuntimeException("wrong value:" + value +" for String: for " + type+"." + index);
            }
            fields.put(index, value);
        } else if (basicType == XType.TYPE_BLOB) {
            if (value == null) {
                fields.remove(index);
                return;
            }
            if (!(value instanceof byte[])) {
                throw new RuntimeException("wrong value:" + value + " for byte[] ,for " + type + "." + index);
            }
            byte[] b = (byte[]) value;
            if (b.length == 0) {
                fields.remove(index);
            } else {
                fields.put(index, value);
            }
        } else if (basicType == XType.TYPE_DATE) {
            if (value == null) {
                fields.remove(index);
            } else {
                if (value instanceof Date) {
                    fields.put(index, value);
                } else {
                    throw new RuntimeException("wrong value:" + value +" for Date ,for " + type+"." + index);
                }
            }
        } else if ((basicType >= XType.TYPE_OBJECT_START)) {
            if (value == null) {
                fields.remove(index);
            } else {
                if (value instanceof XData) {
                    fields.put(index, value);
                } else {
                    throw new RuntimeException("wrong value:" + value +" for XData ,for " + type+"." + index);
                }
            }
        }
    }

    private void setCollectionValue(int index,int collectionType,Object value) {
        switch (collectionType) {
            case XType.MASK_TYPE_COLLECTION_LIST:
            case XType.MASK_TYPE_COLLECTION_SET:
                if (value instanceof Collection<?>) {
                    Collection<?> list = (Collection<?>) value;
                    if (list.size() == 0) {
                        fields.remove(index);
                    } else {
                        fields.put(index, value);
                    }
                } else {
                    throw new RuntimeException("wrong value:" + value + " for list: for " + type + "." + index);
                }
                break;
            case XType.MASK_TYPE_COLLECTION_INT_MAP:
            case XType.MASK_TYPE_COLLECTION_LONG_MAP:
            case XType.MASK_TYPE_COLLECTION_STRING_MAP:
            case XType.MASK_TYPE_COLLECTION_FLOAT_MAP:
            case XType.MASK_TYPE_COLLECTION_DOUBLE_MAP:
                if (value instanceof Map<?,?>) {
                    Map map = (Map<?, ?>) value;
                    if (map.size() == 0) {
                        fields.remove(index);
                    } else {
                        fields.put(index, value);
                    }
                } else {
                    throw new RuntimeException("wrong value:" + value + " for list: for " + type + "." + index);
                }
        }
    }


    public XData(int type) {
        this.type = type;
    }

    public byte getByte(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : ((Number) object).byteValue();
    }

    public short getShort(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : ((Number) object).shortValue();
    }

    public int getInteger(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : (int) object;
    }

    public long getLong(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : (long) object;
    }

    public float getFloat(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : (float) object;
    }

    public double getDouble(int index) {
        Object object = fields.get(index);
        return object == null ? 0 : (double) object;
    }

    public boolean getBoolean(int index) {
        Object object = fields.get(index);
        return object == null ? false : (boolean) object;
    }

    public byte[] getBlob(int index) {
        Object object = fields.get(index);
        return (byte[]) object;
    }

    public Date getDate(int index) {
        Object obj = fields.get(index);
        return (Date) obj;
    }

    public String getString(int index) {
        return (String) fields.get(index);
    }

    public XData getData(int index) {
        return (XData) fields.get(index);
    }

    public List<?> getDataList(int index) {
        return (List<?>) fields.get(index);
    }

    public Set<?> getDataSet(int index) {
        return (Set<?>) fields.get(index);
    }

    public Map<?,?> getDataMap(int index) {return (Map)fields.get(index);}

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        String result = type +"";
        try {
            result = TypeManager.getInstance().getClassName(type);
        } catch (Exception e) {

        }
        return result;
    }

    public static XData valueOf(int type, XData xData) {
        XData xData1 = new XData(type);
        xData1.fields = xData.fields;
        return xData1;
    }
}
