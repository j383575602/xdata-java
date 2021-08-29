package top.xcore.xdata;


import java.util.*;

public class Jsoner {
    public static String jsonify(XData value, XData projectMeta) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        XData classMeta = getMeta(projectMeta, value.getType());
        int count = 0;
        for (Map.Entry<Integer, Object> entry : value.fields.entrySet()) {
            int index = entry.getKey();
            int collectionFlag = index & XType.MASK_TYPE_COLLECTION;
            boolean isCollection = collectionFlag != 0;
            int realType = (index & ~XType.MASK_TYPE_COLLECTION) & XType.MASK_TYPE;
            sb.append("\"").append(mapKey(index, classMeta)).append("\"").append(":");
            if (isCollection) {
                if (collectionFlag == XType.MASK_TYPE_COLLECTION_LIST
                    || collectionFlag == XType.MASK_TYPE_COLLECTION_SET
                ) {
                    Collection<?> values = (Collection<?>) entry.getValue();
                    writeCollection(sb,values,realType,count,projectMeta,value.fields.size());
                } else if (collectionFlag == XType.MASK_TYPE_COLLECTION_STRING_MAP
                    || collectionFlag == XType.MASK_TYPE_COLLECTION_INT_MAP
                        || collectionFlag == XType.MASK_TYPE_COLLECTION_LONG_MAP
                        || collectionFlag == XType.MASK_TYPE_COLLECTION_FLOAT_MAP
                        || collectionFlag == XType.MASK_TYPE_COLLECTION_DOUBLE_MAP
                ) {
                    Map map = (Map) entry.getValue();
                    writeMap(sb,map,realType,count,projectMeta,value.fields.size());
                }
            } else {
                Object item = entry.getValue();
                if (isNumber(realType)) {
                    sb.append(item);
                } else if (isObject(realType)) {
                    XData sub = (XData) item;
                    String subString = jsonify(sub, projectMeta);
                    sb.append(subString);
                } else {
                    sb.append("\"").append(item).append("\"");
                }
                if (count < value.fields.size()-1) {
                    sb.append(",");
                }
            }
            count ++;
        }
        sb.append("}");
        return sb.toString();
    }

    private static void writeCollection(StringBuilder sb,Collection<?> values, int realType,int count,XData projectMeta,int totalCount) {
        sb.append("[");
        List<?> list = new ArrayList<>(values);
        for (int i = 0; i < list.size(); i++) {
            if (isNumber(realType)) {
                sb.append(list.get(i));
            } else if (isObject(realType)) {
                XData sub = (XData) list.get(i);
                String subString = jsonify(sub, projectMeta);
                sb.append(subString);
            } else {
                sb.append("\"").append(list.get(i)).append("\"");
            }
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        if (count < totalCount-1) {
            sb.append(",");
        }
    }

    private static void writeMap(StringBuilder sb,Map<?,?> values, int realType,int count,XData projectMeta,int totalCount) {
        sb.append("{");
        final int[] i = {0};
        values.entrySet().forEach((entry) -> {
            sb.append("\"").append(entry.getKey()).append("\":");
            if (isNumber(realType) || isBoolean(realType)) {
                sb.append(entry.getValue());
            } else if (isObject(realType)) {
                XData sub = (XData) entry.getValue();
                String subString = jsonify(sub, projectMeta);
                sb.append(subString);
            } else {
                sb.append("\"").append(entry.getValue()).append("\"");
            }
            if (i[0] < values.size() - 1) {
                sb.append(",");
            }
            i[0]++;
        });
        sb.append("}");
        if (count < totalCount-1) {
            sb.append(",");
        }
    }


    public static XData getMeta(XData projectMeta, int type) {
        List<XData> classes = (List<XData>) projectMeta.getDataList(XProjectMeta.CLASSES);
        for (XData cls : classes) {
            if (cls.getInteger(XClassMeta.TYPE) == type) {
                return cls;
            }
        }
        return null;
    }

    public static String mapKey(int index, XData classInfo) {
        List<XData> fields = (List<XData>) classInfo.getDataList(XClassMeta.FIELDS);
        for (XData field : fields) {
            if (field.getInteger(XFieldMeta.FIELD_INDEX) == index) {
                return field.getString(XFieldMeta.NAME);
            }
        }
        return String.valueOf(index);
    }

    public static boolean isNumber(int type) {
        if (type == XType.TYPE_BYTE_i_1
                || type == XType.TYPE_BYTE_i_2
                || type == XType.TYPE_BYTE_i_4
                || type == XType.TYPE_BYTE_i_8
                || type == XType.TYPE_BYTE_f_4
                || type == XType.TYPE_BYTE_f_8
        ) {
            return true;
        }
        return false;
    }

    public static boolean isBoolean(int type) {
        return type == XType.TYPE_BOOLEAN;
    }

    public static boolean isObject(int type) {
        return type >= XType.TYPE_OBJECT_START;
    }
}
