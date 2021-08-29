package top.xcore.xdata.test;

import top.xcore.xdata.XData;
import top.xcore.xdata.XDataParser;
import top.xcore.xdata.XDataWriter;
import com.ctrip.xcore.text.v1.ModelWithAllKindFields;
import com.ctrip.xcore.text.v1.SimpleModel;
import com.ctrip.xcore.text.v1.xcore.text.v1.wraper.SimpleModelWrapper;
//import org.junit.Assert;
//import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wlzhao on 2017/7/18.
 */
public class TestCase {
    /*
    @Test
    public void testDataKind() {
        XData data = newModelWithAllKind(10);
        XDataWriter writer = new XDataWriter();
        byte[] bytes = writer.writeData(data);
        XDataParser parser = new XDataParser();
        XData out = parser.parse(bytes);
        Assert.assertNotNull(out);
    }

    @Test
    public void testAccessField() {
        XData data = newSimpleModel();
        byte byteValue = data.getByte(SimpleModel.byte_field);
        Assert.assertTrue( byteValue == 8);
        short shortValue = data.getShort(SimpleModel.short_field);
        Assert.assertTrue( shortValue == 102);
        int intValue = data.getInteger(SimpleModel.int_field);
        Assert.assertTrue( intValue == 102934);
        long longValue = data.getLong(SimpleModel.long_field);
        Assert.assertTrue( longValue == 34134324325L);
        float floatValue = data.getFloat(SimpleModel.float_field);
        Assert.assertTrue( floatValue == 32414.5f);
        double doubleValue = data.getDouble(SimpleModel.double_field);
        Assert.assertTrue( doubleValue == 32414.09);
        String stringValue = data.getString(SimpleModel.string_field);
        Assert.assertTrue( "This is a String".equals(stringValue));
        XData all = new XData(ModelWithAllKindFields.TYPE_INDEX);
        all.set(ModelWithAllKindFields.model_field,data);
        List<XData> list = new ArrayList<>();
        list.add(data);
        all.set(ModelWithAllKindFields.model_list_field,list);
        XData m = all.getData(ModelWithAllKindFields.model_field);
        Assert.assertTrue(m == data);
        List<XData> list2 = (List<XData>) all.getDataList(ModelWithAllKindFields.model_list_field);
        Assert.assertTrue(list.equals(list2));
    }

    @Test
    public void testValueOf() {
        XData data = newSimpleModel();
        XData copy = XData.valueOf(SimpleModel.TYPE_INDEX,data);
        Assert.assertTrue(copy != null);
    }

    @Test
    public void testToString() {
        XData data = newSimpleModel();
        String string = data.toString();
        Assert.assertNotNull(string);
    }

    @Test
    public void testAccessFieldError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        byte byteValue = data.getByte(SimpleModel.byte_field);
        Assert.assertTrue( byteValue == 0);
        short shortValue = data.getShort(SimpleModel.short_field);
        Assert.assertTrue( shortValue == 0);
        int intValue = data.getInteger(SimpleModel.int_field);
        Assert.assertTrue( intValue == 0);
        long longValue = data.getLong(SimpleModel.long_field);
        Assert.assertTrue( longValue == 0);
        float floatValue = data.getFloat(SimpleModel.float_field);
        Assert.assertTrue( floatValue == 0);
        double doubleValue = data.getDouble(SimpleModel.double_field);
        Assert.assertTrue( doubleValue == 0);
        String stringValue = data.getString(SimpleModel.string_field);
        Assert.assertTrue( stringValue == null);
    }

    @Test
    public void testWriteToStream() {
        XData data = newModelWithAllKind(10);
        XDataWriter writer = new XDataWriter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            writer.toStream(data,baos);
            XDataParser parser = new XDataParser();
            XData out = parser.parse(baos.toByteArray());
            Assert.assertNotNull(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBigData() {
        XData data = newModelWithAllKind(1000);
        XDataWriter writer = new XDataWriter();
        byte[] bytes = writer.writeData(data);
        XDataParser parser = new XDataParser();
        XData out = parser.parse(bytes);
        Assert.assertNotNull(out);
    }

    @Test
    public void testByteKindError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.byte_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testRemoveByteKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.byte_field,8);
        boolean contains = containsField(data,SimpleModel.byte_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.byte_field,0);
        contains = containsField(data,SimpleModel.byte_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testShortKindError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.short_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testRemoveIntKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.int_field,12333424);
        boolean contains = containsField(data,SimpleModel.int_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.int_field,0);
        contains = containsField(data,SimpleModel.int_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testShortIntError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.int_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testRemoveLongKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.long_field,123334242342L);
        boolean contains = containsField(data,SimpleModel.long_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.long_field,0);
        contains = containsField(data,SimpleModel.long_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testFloatError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.float_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testRemoveFloatKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.float_field,123.0f);
        boolean contains = containsField(data,SimpleModel.float_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.float_field,0);
        contains = containsField(data,SimpleModel.float_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testDoubleError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.double_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testDoubleFloatKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.double_field,123924234234.00);
        boolean contains = containsField(data,SimpleModel.double_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.double_field,0);
        contains = containsField(data,SimpleModel.double_field);
        Assert.assertFalse(contains);
    }


    @Test
    public void testStringError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.string_field,100);
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testStringKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.string_field,"I am string");
        boolean contains = containsField(data,SimpleModel.string_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.string_field,"");
        contains = containsField(data,SimpleModel.string_field);
        Assert.assertFalse(contains);
        data.set(SimpleModel.string_field,"I am string");
        contains = containsField(data,SimpleModel.string_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.string_field,null);
        contains = containsField(data,SimpleModel.string_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testDataError() {
        XData data = new XData(ModelWithAllKindFields.TYPE_INDEX);
        boolean failed;
        try {
            data.set(ModelWithAllKindFields.model_field,"It is error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testData() {
        XData data = new XData(ModelWithAllKindFields.TYPE_INDEX);
        XData model = newSimpleModel();
        data.set(ModelWithAllKindFields.model_field,model);
        boolean contains = containsField(data,ModelWithAllKindFields.model_field);
        Assert.assertTrue(contains);
        data.set(ModelWithAllKindFields.model_field,null);
        contains = containsField(data,ModelWithAllKindFields.model_field);
        Assert.assertFalse(contains);
    }

    @Test
    public void testDataListError() {
        XData data = new XData(ModelWithAllKindFields.TYPE_INDEX);
        boolean failed;
        try {
            data.set(ModelWithAllKindFields.model_list_field,"It is error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testDataList() {
        XData data = new XData(ModelWithAllKindFields.TYPE_INDEX);
        List<XData> list = new ArrayList<>();
        XData model = newSimpleModel();
        list.add(model);
        data.set(ModelWithAllKindFields.model_list_field,list);
        boolean contains = containsField(data,ModelWithAllKindFields.model_list_field);
        Assert.assertTrue(contains);
        data.set(ModelWithAllKindFields.model_list_field,null);
        contains = containsField(data,ModelWithAllKindFields.model_list_field);
        Assert.assertFalse(contains);

        data.set(ModelWithAllKindFields.model_list_field,list);
        contains = containsField(data,ModelWithAllKindFields.model_list_field);
        Assert.assertTrue(contains);
        list.clear();
        data.set(ModelWithAllKindFields.model_list_field,list);
        contains = containsField(data,ModelWithAllKindFields.model_list_field);
        Assert.assertFalse(contains);
    }



    @Test
    public void testShortLongError() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        boolean failed;
        try {
            data.set(SimpleModel.long_field,"It should be error");
            failed = false;
        } catch (Exception e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }

    @Test
    public void testRemoveShortKind() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.short_field,123);
        boolean contains = containsField(data,SimpleModel.short_field);
        Assert.assertTrue(contains);
        data.set(SimpleModel.short_field,0);
        contains = containsField(data,SimpleModel.short_field);
        Assert.assertFalse(contains);
    }


    private boolean containsField(XData data , int fieldIndex) {
        try {
            Field map = XData.class.getDeclaredField("fields");
            map.setAccessible(true);
            Map fields = (Map) map.get(data);
            boolean contains = fields.keySet().contains(fieldIndex);
            return contains;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private XData newModelWithAllKind(int size) {
        XData data = new XData(ModelWithAllKindFields.TYPE_INDEX);
        XData model = newSimpleModel();
        data.set(ModelWithAllKindFields.model_field,model);
        List<XData> datas = new ArrayList<>();
        for(int i=0;i<size;i++) {
            XData ele = newSimpleModel();
            datas.add(ele);
        }
        data.set(ModelWithAllKindFields.model_list_field,datas);
        return data;

    }
    private XData newSimpleModel() {
        XData data = new XData(SimpleModel.TYPE_INDEX);
        data.set(SimpleModel.byte_field,8);
        data.set(SimpleModel.short_field,102);
        data.set(SimpleModel.int_field,102934);
        data.set(SimpleModel.long_field,34134324325L);
        data.set(SimpleModel.float_field,32414.5f);
        data.set(SimpleModel.double_field,32414.09);
        data.set(SimpleModel.string_field,"This is a String");
        return data;
    }

    private SimpleModelWrapper newSimpleModelWrapper() {
        SimpleModelWrapper wrapper = new SimpleModelWrapper();
        wrapper.setByte_field((byte) 5);
        return wrapper;
    }

    @Test
    public void testWrapper() {
        SimpleModelWrapper simpleModel = newSimpleModelWrapper();
        Assert.assertTrue(simpleModel.getByte_field() == 5);

    }

    @Test
    public void testWrapper2() {
        XData data = newSimpleModel();
        SimpleModelWrapper simpleModel = new SimpleModelWrapper(data);
        Assert.assertTrue(simpleModel.getByte_field() == 8);

    }

    @Test
    public void printDouble() {
        //Long d = 109l;
        //long l = Double.doubleToLongBits(d);
        long l = 109l;
        for (int index = 7; index >= 0; index--) {
            System.out.println("b[" +(7-index)+"]=" + (byte) ((l >> index * 8) & 0xFF));
        }
    }

     */
}
