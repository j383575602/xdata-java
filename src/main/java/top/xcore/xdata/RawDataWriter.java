package top.xcore.xdata;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wlzhao on 2017/3/1.
 */

public class RawDataWriter {
    private static final int MAX = 1024*1024;
    private int infoSize;
    LinkedBuffer buffer;
    private  boolean debug = true;

    public RawDataWriter() {
        this(8);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public RawDataWriter(int bufferSize) {
        buffer = new LinkedBuffer(bufferSize);
    }


    public byte[] write(int type,Object value) {
        if (type == XType.TYPE_BYTE_i_1) {
            byte b = ((Number) value).byteValue();
            writeByte(b);
        } else if (type == XType.TYPE_BYTE_i_2) {
            short s = ((Number) value).shortValue();
            writeByte2(s);
        } else if (type == XType.TYPE_BYTE_i_4) {
            int i = ((Number) value).intValue();
            writeByte4I(i);
        } else if (type == XType.TYPE_BYTE_i_8) {
            long i = ((Number) value).longValue();
            writeByte8I(i);
        } else if (type == XType.TYPE_BYTE_f_4) {
            float i = ((Number) value).floatValue();
            writeByte4F(i);
        } else if (type == XType.TYPE_BYTE_f_8) {
            double i = ((Number) value).doubleValue();
            writeByte8F(i);
        } else if (type == XType.TYPE_BOOLEAN) {
            boolean  b = (boolean) value;
            writeBoolean(b);
        } else if (type == XType.TYPE_STRING) {
            String s = (String) value;
            writeString(s);
        } else if (type == XType.TYPE_DATE) {
            Date d = (Date) value;
            writeByte8I(d.getTime());
        }
        return commit();
    }


    public byte[] commit() {
        //System.out.println("infoSize:" + infoSize+",total:" + position+",effect percent=" +(1-(infoSize / (float)position)));
        infoSize = 0;
        long t1 = System.currentTimeMillis();
        byte[] result = buffer.toBytes();
        long t2 = System.currentTimeMillis();
        if (debug) {
            System.out.println("commit use:" + (t2-t1));
        }
        return result;
    }


    public void writeBoolean(boolean value) {
        byte b = value ? (byte)1 : (byte)0;
        writeByte(b);
    }



    public void writeByte(byte b) {
        buffer.writeByte(b);
    }

    public void writeByte2(short s) {
        buffer.writeByte((byte) ((s >> 8) & 0xFF));
        buffer.writeByte((byte) (s & 0xFF));
    }

    public void writeByte4I(int i) {
        for (int index = 3; index >= 0; index--) {
            buffer.writeByte((byte) ((i >> index * 8) & 0xFF));
        }
    }

    public void writeByte8I(long i) {
        for (int index = 7; index >= 0; index--) {
            buffer.writeByte((byte) ((i >> index * 8) & 0xFF));
        }
    }

    public void writeByte4F(float i) {
        int value = Float.floatToIntBits(i);
        writeByte4I(value);
    }

    public void writeByte8F(double i) {
        long value = Double.doubleToLongBits(i);
        writeByte8I(value);
    }

    public void writeString(String string) {
        byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
        buffer.writeBytes(bytes);
    }
}
