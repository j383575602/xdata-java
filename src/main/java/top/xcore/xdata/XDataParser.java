package top.xcore.xdata;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by wlzhao on 2017/2/21.
 */
public class XDataParser {
    byte[] data;
    int position = 0;
    private boolean debug = true;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public XDataParser() {
    }

    public XData parse(byte[] data) {
        this.data = data;
        long t0 = System.currentTimeMillis();
        XData x = readData();
        long t1 = System.currentTimeMillis();
        if (debug) {
            System.out.println("parse use:" + (t1 - t0));
        }
        return x;
    }

    private XData readData() {
        final int type = readInt();//parseInt(typeBytes);
        XData result = new XData(type);
        final int fieldCount = readByte();
        for(int i=0;i<fieldCount;i++) {
            int index = readInt();
            int collectionFlags = index & XType.MASK_TYPE_COLLECTION;
            boolean isCollection =collectionFlags > 0;
            int rawType = index & XType.MASK_TYPE & (~XType.MASK_TYPE_COLLECTION);
            if (isCollection) {
                result.set(index,readCollection(rawType,collectionFlags));
            } else {
                result.set(index,readSingleField(rawType));
            }
        }
        return result;
    }

    private XData readDataWithType(int type) {
        //final int type = readInt();//parseInt(typeBytes);
        XData result = new XData(type);
        final int fieldCount = readByte();
        for(int i=0;i<fieldCount;i++) {
            int index = readInt();
            int collectionFlags = index & XType.MASK_TYPE_COLLECTION;
            boolean isCollection =collectionFlags > 0;
            int rawType = index & XType.MASK_TYPE & (~XType.MASK_TYPE_COLLECTION);
            if (isCollection) {
                result.set(index,readCollection(rawType,collectionFlags));
            } else {
                result.set(index,readSingleField(rawType));
            }
        }
        return result;
    }


    private Object readSingleField(int rawType) {
        if (rawType == XType.TYPE_BYTE_i_1) {
            return readByte();
        } else if (rawType == XType.TYPE_BYTE_i_2) {
            return readShort();
        } else if (rawType == XType.TYPE_BYTE_i_4) {
            return readInt();
        } else if (rawType == XType.TYPE_BYTE_i_8) {
            long l = readLong();//TODO :check
            double d = Double.longBitsToDouble(l);
            return  (long)d;
        } else if (rawType == XType.TYPE_BYTE_f_4) {
            return readFloat();
        } else if (rawType == XType.TYPE_BYTE_f_8) {
            return readDouble();
        } else if (rawType == XType.TYPE_STRING) {
            int len = readInt();
            return readString(len);
        } else if (rawType == XType.TYPE_BOOLEAN) {
            byte value = readByte();
            return  value == 1;
        } else if (rawType == XType.TYPE_BLOB) {
            int len = readInt();
            return readBytes(len);
        } else if (rawType == XType.TYPE_DATE) {
            long l = readLong();
            double d = Double.longBitsToDouble(l);
            return new Date((long) d);
        } else if(rawType >= XType.TYPE_OBJECT_START) {
            XData xData2 = readDataWithType(rawType);
            return xData2;
        }
        return null;
    }

    private Object readCollection(int rawType,int collectionFlags) {
        switch (collectionFlags) {
            case XType.MASK_TYPE_COLLECTION_SET:
            case XType.MASK_TYPE_COLLECTION_LIST:
                return readList(rawType,collectionFlags);
            case XType.MASK_TYPE_COLLECTION_INT_MAP:
                return readIntMap(rawType);
            case XType.MASK_TYPE_COLLECTION_STRING_MAP:
                return readStringMap(rawType);
            case XType.MASK_TYPE_COLLECTION_LONG_MAP:
                return readLongMap(rawType);
            case XType.MASK_TYPE_COLLECTION_DOUBLE_MAP:
                return readDoubleMap(rawType);
            case XType.MASK_TYPE_COLLECTION_FLOAT_MAP:
                return readFloatMap(rawType);
            default:
                throw new RuntimeException("collection type is not right");
        }
    }

    private <T> HashMap<Integer,T> readIntMap(int rawType) {
        int count = readInt();
        HashMap<Integer,T> map = new HashMap<>();
        for(int i=0;i<count;i++) {
            int key = readInt();
            Object value = readSingleField(rawType);
            map.put(key,(T)value);
        }
        return map;
    }

    private <T> HashMap<String,T> readStringMap(int rawType) {
        int count = readInt();
        HashMap<String,T> map = new HashMap<>();
        for(int i=0;i<count;i++) {
            String key = (String) readSingleField(XType.TYPE_STRING);
            Object value = readSingleField(rawType);
            map.put(key,(T)value);
        }
        return map;
    }

    private <T> HashMap<Long,T> readLongMap(int rawType) {
        int count = readInt();
        HashMap<Long,T> map = new HashMap<>();
        for(int i=0;i<count;i++) {
            Long key = ((Double)readDouble()).longValue();
            Object value = readSingleField(rawType);
            map.put(key,(T)value);
        }
        return map;
    }

    private <T> HashMap<Float,T> readFloatMap(int rawType) {
        int count = readInt();
        HashMap<Float,T> map = new HashMap<>();
        for(int i=0;i<count;i++) {
            Float key = readFloat();
            Object value = readSingleField(rawType);
            map.put(key,(T)value);
        }
        return map;
    }

    private <T> HashMap<Double,T> readDoubleMap(int rawType) {
        int count = readInt();
        HashMap<Double,T> map = new HashMap<>();
        for(int i=0;i<count;i++) {
            Double key = readDouble();
            Object value = readSingleField(rawType);
            map.put(key,(T)value);
        }
        return map;
    }

    private static  <T> Collection<T> newCollection(int count,int type) {
        if (type == XType.MASK_TYPE_COLLECTION_LIST) {
            return new ArrayList<>(count);
        } else if (type == XType.MASK_TYPE_COLLECTION_SET) {
            return new HashSet<>(count);
        }
        throw new RuntimeException("Only set and list is support");
    }

    public Collection<?> readList(int rawType,int collectionType) {
        int count = readInt();
        if (rawType == XType.TYPE_BYTE_i_1) {
            Collection<Byte> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                byte b = readByte();
                result.add(b);
            }
            return result;
        } else if (rawType == XType.TYPE_BYTE_i_2) {
            Collection<Short> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                short b = readShort();
                result.add(b);
            }
            return result;
        } else if (rawType == XType.TYPE_BYTE_i_4) {
            Collection<Integer> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                int b = readInt();
                result.add(b);
            }
            return result;
        } else if (rawType == XType.TYPE_BYTE_i_8) {
            Collection<Long> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                long l = readLong();
                double d = Double.longBitsToDouble(l);//TODO:check
                result.add((long) d);
            }
            return result;
        } else if (rawType == XType.TYPE_BYTE_f_4) {
            Collection<Float> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                float l = readFloat();
                result.add(l);
            }
            return result;
        } else if (rawType == XType.TYPE_BYTE_f_8) {
            Collection<Double> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                long l = readLong();
                double d = Double.longBitsToDouble(l);
                result.add(d);
            }
            return result;
        } else if (rawType == XType.TYPE_BOOLEAN) {
            Collection<Boolean> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                byte value = readByte();
                result.add(value == 1);
            }
            return result;
        } else if (rawType == XType.TYPE_BLOB) {
            Collection<byte[]> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                int len = readInt();
                byte[] bytes = readBytes(len);
                result.add(bytes);
            }
            return result;
        } else if (rawType == XType.TYPE_DATE) {
            Collection<Date> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                long l = readLong();
                double d = Double.longBitsToDouble(l);
                Date date =  new Date((long) d);
                result.add(date);
            }
            return result;
        } else if (rawType == XType.TYPE_STRING) {
            Collection<String> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                int len = readInt();
                String s = readString(len);
                result.add(s);
            }
            return result;
        } else if (rawType >= XType.TYPE_OBJECT_START) {
            Collection<XData> result = newCollection(count,collectionType);
            for (int j = 0; j < count; j++) {
                XData xData = readDataWithType(rawType);
                result.add(xData);
            }
            return result;
        }
        return null;
    }



    public List<XData> readDataList(int type) {
        short count = readShort();
        ArrayList<XData> result = new ArrayList<>(count);
        for (int j = 0; j < count; j++) {
            XData xData = readDataWithType(type);
            result.add(xData);
        }
        return result;
    }

    private byte[] readBytes(int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, position, result, 0, length);
        position += length;
        return result;
    }

    private short readShort() {
        return (short) (((readByte() & 0xFF) << 8) | (readByte() & 0xFF));
    }

    private long readLong() {
        return ((((long) readByte() & 0xFF) << 56) |
                (((long) readByte() & 0xFF) << 48) |
                (((long) readByte() & 0xFF) << 40) |
                (((long) readByte() & 0xFF) << 32) |
                (((long) readByte() & 0xFF) << 24) |
                (((long) readByte() & 0xFF) << 16) |
                (((long) readByte() & 0xFF) << 8) |
                (((long) readByte() & 0xFF)));
    }

    private byte readByte() {
        byte result = data[position];
        position++;
        return result;
    }

    private float readFloat() {
        int result = readInt();
        float fr = Float.intBitsToFloat(result);
        return fr;
    }

    private double readDouble() {
        long l = readLong();
        return Double.longBitsToDouble(l);
    }

    private String readString(int len) {
        String result = "";
        try {
            result = new String(data, position, len,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        position += len;
        return result;
    }

    private int readInt() {
        return (((readByte() & 0xFF) << 24) | ((readByte() & 0xFF) << 16) | ((readByte() & 0xFF) << 8) | (readByte() & 0xFF));
    }
}
