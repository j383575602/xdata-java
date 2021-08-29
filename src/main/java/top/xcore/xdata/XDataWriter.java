package top.xcore.xdata;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by wlzhao on 2017/3/1.
 */

public class XDataWriter {
    private int infoSize;
    LinkedBuffer buffer;
    //ListBuffer buffer;
    private  boolean debug = true;

    public XDataWriter() {
        this(4096);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public XDataWriter(int bufferSize) {
        buffer = new LinkedBuffer(bufferSize);
    }

    public byte[] writeData(XData xData) {
        long t0 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData begin");
        }
        doWriteData(xData);
        long t1 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData use:" + (t1-t0));
        }
        return commit();
    }

    /**
     * whether need write type, only the outer level object need write type, inner object do not need
     * @param xData
     */
    private void doWriteData(XData xData) {
        XData toWrite = xData;
        if (xData instanceof XDataWrapper) {
            XDataWrapper xDataWrapper = (XDataWrapper) xData;
            if (xDataWrapper._data != null) {
                toWrite = xDataWrapper._data;
            }
        }
        writeType(toWrite);
        writeFieldCount(toWrite);
        writeFieldValue(toWrite);
    }

    private void doWriteDataWithoutType(XData xData) {
        XData toWrite = xData;
        if (xData instanceof XDataWrapper) {
            XDataWrapper xDataWrapper = (XDataWrapper) xData;
            if (xDataWrapper._data != null) {
                toWrite = xDataWrapper._data;
            }
        }
        writeFieldCount(toWrite);
        writeFieldValue(toWrite);
    }

    private void writeFieldCount(XData xData) {
        writeByte((byte)xData.fields.size());
        infoSize ++;
    }

    private byte[] commit() {
        //System.out.println("infoSize:" + infoSize+",total:" + position+",effect percent=" +(1-(infoSize / (float)position)));
        infoSize = 0;
        long t1 = System.currentTimeMillis();
        byte[] result = buffer.toBytes();
        long t2 = System.currentTimeMillis();
        if (debug) {
            System.out.println("commit use:" + (t2-t1));
        }
        seek(0);
        return result;
    }

    public LinkedBuffer writeDataToBuffer(XData data) {
        long t0 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData begin");
        }
        doWriteData(data);
        long t1 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData use:" + (t1-t0));
        }
        return buffer;
    }

    public void toStream(XData xData,OutputStream os) throws IOException {
        long t0 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData begin");
        }
        doWriteData(xData);
        long t1 = System.currentTimeMillis();
        if (debug) {
            System.out.println("writeData use:" + (t1-t0));
        }
        buffer.writeToStream(os);
        seek(0);
    }

    private void writeBlob(byte[] array) {
        int len =  array.length;
        writeByte4I(len);
        infoSize += 4;
        buffer.writeBytes(array);
    }


    private void writeBoolean(boolean value) {
        byte b = value ? (byte)1 : (byte)0;
        writeByte(b);
    }

    private void writeList(List<?> values, int rawType) {
        int len = values.size();
        writeByte4I(len);
        infoSize += 4;
        final int size = values.size();
        for(int i=0;i<size;i++) {
            Object rawValue = values.get(i);
            writeSingleObject(rawValue,rawType);
        }
    }

    private void writeSet(Set<?> values, int rawType) {
        int len = values.size();
        writeByte4I(len);
        infoSize += 4;
        for(Object rawValue : values) {
            writeSingleObject(rawValue,rawType);
        }
    }


    protected void writeByte(byte b) {
        buffer.writeByte(b);
    }

    protected void writeByte2(short s) {
        buffer.writeByte((byte) ((s >> 8) & 0xFF));
        buffer.writeByte((byte) (s & 0xFF));
    }

    private void writeByte4I(int i) {
        for (int index = 3; index >= 0; index--) {
            buffer.writeByte((byte) ((i >> index * 8) & 0xFF));
        }
    }

    private void writeByte8I(long i) {
        for (int index = 7; index >= 0; index--) {
            buffer.writeByte((byte) ((i >> index * 8) & 0xFF));
        }
    }

    private void writeByte4F(float i) {
        int value = Float.floatToIntBits(i);
        writeByte4I(value);
    }

    private void writeByte8F(double i) {
        long value = Double.doubleToLongBits(i);
        writeByte8I(value);
    }

    private void writeString(String string) {
        byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
        buffer.writeBytes(bytes);
    }

    private void writeType(XData data) {
        writeByte4I(data.getType());
        infoSize += 4;
    }

    private void jump(int count) {
        buffer.jump(count);
    }

    private void writeFieldValue(XData xData) {
        Set<Map.Entry<Integer, Object>> entries = xData.fields.entrySet();
        for (Map.Entry<Integer, Object> entry : entries) {
            int index = entry.getKey();
            Object rawValue = entry.getValue();
            writeByte4I(index);
            infoSize += 4;
            /**
             * 是否是集合
             */
            int collectionFlag = (index & XType.MASK_TYPE_COLLECTION);
            /**
             * 原始类型
             */
            int rawType = index & XType.MASK_TYPE & (~XType.MASK_TYPE_COLLECTION);
            if (collectionFlag !=0) {
                writeCollection(collectionFlag,rawType,rawValue);
            } else {
                writeSingleObject(rawValue, rawType);
            }
        }
    }

    private void writeCollection(int collectionType,int rawType,Object object) {
        switch (collectionType) {
            case XType.MASK_TYPE_COLLECTION_LIST:
                List<?> list = (List<?>) object;
                writeList(list,rawType);
                break;
            case XType.MASK_TYPE_COLLECTION_SET:
                Set<?> set = (Set<?>) object;
                writeSet(set,rawType);
                break;
            case XType.MASK_TYPE_COLLECTION_INT_MAP:
                Map map = (Map) object;
                writeIntMap(rawType,map);
                break;
            case XType.MASK_TYPE_COLLECTION_STRING_MAP:
                map = (Map) object;
                writeStringMap(rawType,map);
                break;
            case XType.MASK_TYPE_COLLECTION_LONG_MAP:
                map = (Map) object;
                writeLongMap(rawType,map);
                break;
            case XType.MASK_TYPE_COLLECTION_FLOAT_MAP:
                map = (Map) object;
                writeFloatMap(rawType,map);
                break;
            case XType.MASK_TYPE_COLLECTION_DOUBLE_MAP:
                map = (Map) object;
                writeDoubleMap(rawType,map);
                break;
        }
    }

    private void writeIntMap(int rawType,Map map) {
        writeByte4I(map.size());
        Set<Map.Entry<?,?>> entries = map.entrySet();
        entries.forEach( entry -> {
            if (!(entry.getKey() instanceof Integer)) {
                throw new RuntimeException("IntMap key must be Integer");
            }
            writeByte4I((Integer) entry.getKey());
            writeSingleObject(entry.getValue(),rawType);
        });
    }

    private void writeStringMap(int rawType,Map map) {
        writeByte4I(map.size());
        Set<Map.Entry<?,?>> entries = map.entrySet();
        entries.forEach( entry -> {
            if (!(entry.getKey() instanceof String)) {
                throw new RuntimeException(" the key of StringMap must be String");
            }
            writeStringField((String)entry.getKey());
            writeSingleObject(entry.getValue(),rawType);
        });
    }

    private void writeLongMap(int rawType,Map map) {
        writeByte4I(map.size());
        Set<Map.Entry<?,?>> entries = map.entrySet();
        entries.forEach( entry -> {
            if (!(entry.getKey() instanceof Long)) {
                throw new RuntimeException(" the key of StringMap must be String");
            }
            writeByte8F(((Long) entry.getKey()).doubleValue());
            writeSingleObject(entry.getValue(),rawType);
        });
    }


    private void writeFloatMap(int rawType,Map map) {
        writeByte4I(map.size());
        Set<Map.Entry<?,?>> entries = map.entrySet();
        entries.forEach( entry -> {
            if (!(entry.getKey() instanceof Float)) {
                throw new RuntimeException(" the key of StringMap must be String");
            }
            writeByte4F(((Float) entry.getKey()));
            writeSingleObject(entry.getValue(),rawType);
        });
    }

    private void writeDoubleMap(int rawType,Map map) {
        writeByte4I(map.size());
        Set<Map.Entry<?,?>> entries = map.entrySet();
        entries.forEach( entry -> {
            if (!(entry.getKey() instanceof Double)) {
                throw new RuntimeException(" the key of StringMap must be String");
            }
            writeByte8F(((Double) entry.getKey()));
            writeSingleObject(entry.getValue(),rawType);
        });
    }


    private void writeSingleObject(Object rawValue, int rawType) {
        if (rawType == XType.TYPE_BYTE_i_1) {
            byte value = ((Number) rawValue).byteValue();
            writeByte(value);
        } else if (rawType == XType.TYPE_BYTE_i_2) {
            short value = ((Number) rawValue).shortValue();
            writeByte2(value);
        } else if (rawType == XType.TYPE_BYTE_i_4) {
            int value = ((Number) rawValue).intValue();
            writeByte4I(value);
        } else if (rawType == XType.TYPE_BYTE_i_8) {
            long value = ((Number) rawValue).longValue();
            writeByte8F(value);
        } else if (rawType == XType.TYPE_BYTE_f_4) {
            float value = ((Number) rawValue).floatValue();
            writeByte4F(value);
        } else if (rawType == XType.TYPE_BYTE_f_8) {
            double value = ((Number) rawValue).doubleValue();
            writeByte8F(value);
        } else if (rawType == XType.TYPE_BLOB) {
            byte[] bytes = (byte[]) rawValue;
            writeBlob(bytes);
        } else if (rawType == XType.TYPE_DATE) {
            Date d = (Date) rawValue;
            writeByte8F(d.getTime());
        } else if (rawType == XType.TYPE_BOOLEAN) {
            boolean b = (boolean) rawValue;
            writeBoolean(b);
        } else if (rawType == XType.TYPE_STRING) {
            String s = (String) rawValue;
            writeStringField(s);
        } else if (rawType >= XType.TYPE_OBJECT_START) {
            XData data = (XData) rawValue;
            doWriteDataWithoutType(data);
        } else {
            System.out.println("Note support type : " + rawType);
        }
    }



    private void writeStringField(String rawValue) {
        byte[] bytes = rawValue.getBytes(Charset.forName("UTF-8"));
        writeByte4I(bytes.length);
        buffer.writeBytes(bytes);
    }

    private void seek(int position) {
        buffer.seek(position);
    }
}
