package top.xcore.xdata;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by wlzhao on 2017/2/22.
 */
public class TypeManager {
    public String typePackage;
    private static TypeManager sInstance;
    HashMap<Integer,Class<?>> clazz = new HashMap<>();
    private Set<String> pathes = new HashSet<>();
    private HashMap<Integer,List<ColumnInfo>> allClassInfo = new HashMap<>();

    public static TypeManager getInstance() {
        if (sInstance == null) {
            sInstance = new TypeManager();
        }
        return sInstance;
    }

    public void registerClass(HashMap<Integer,Class<?>> cls) {
        clazz.putAll(cls);
    }

    public void setTypePackage(String typePackage) {
        this.typePackage = typePackage;
    }

    public void addPath(String path) {
        pathes.add(path);
        loadModelByPath(path);
    }

    public void addClass(Class<?> cls) {
        loadClass(cls);
    }

    public List<ColumnInfo> getColumnInfoOf(int type) {
        return allClassInfo.get(type);
    }

    private void loadModelByPath(String path) {
        System.out.println("loadModelByPath:" + path);
        Set<String> allClassed = ClassUtils.getClassName(path,true);
        System.out.println("class count:" + allClassed.size());
        for(String name : allClassed) {
            try {
                Class<?> kls = Class.forName(name);
                if (kls.getSimpleName().equals("CType")) {
                    continue;
                }
                loadClass(kls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadClass(Class<?> kls) {
        System.out.println("cls:" + kls.getName());
        try {
            Field indexField = kls.getField("TYPE_INDEX");
            if (indexField != null) {
                int index = indexField.getInt(null);
                clazz.put(index, kls);
                List<ColumnInfo> infos = createColumnInfo(kls);
                allClassInfo.put(index,infos);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void loadModels() {
        for(String path : pathes) {
            loadModelByPath(path);
        }
    }

    private List<ColumnInfo> createColumnInfo(Class<?> cls) {
        Field[] fields = cls.getFields();
        Field typeIndex = null;
        ArrayList<Field> columns = new ArrayList<>();
        for(Field field : fields) {
            if ("TYPE_INDEX".equals(field.getName())) {
                typeIndex = field;
            } else {
                columns.add(field);
            }
        }
        if (typeIndex != null) {
            List<ColumnInfo> infos = new ArrayList<>(columns.size());
            for(Field field : columns) {
                int value = 0;
                try {
                    value = (int) field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                int fieldType = value & XType.MASK_TYPE;

                int index = value & XType.MASK_INDEX;
                ColumnInfo info = new ColumnInfo();
                info.name = "F"+ value;
                info.isPrimayKey = (index == 1);
                String typeString = "";
                //if is list<primitive> user string to join
                if (((value & XType.MASK_TYPE_COLLECTION) == XType.MASK_TYPE_COLLECTION_LIST) && fieldType< XType.TYPE_OBJECT_START) {
                    typeString = "varchar(256)";
                } else if (fieldType == XType.TYPE_BYTE_i_1) {
                    typeString = "tinyint";
                } else if (fieldType == XType.TYPE_BYTE_i_2) {
                    typeString = "smallint";
                } else if (fieldType == XType.TYPE_BYTE_i_4) {
                    typeString = "int(11)";
                } else if (fieldType == XType.TYPE_BYTE_i_8) {
                    typeString = "bigint";
                } else if (fieldType == XType.TYPE_BYTE_f_4) {
                    typeString = "float";
                } else if (fieldType == XType.TYPE_BYTE_f_8) {
                    typeString = "double";
                } else if (fieldType == XType.TYPE_STRING) {
                    typeString = "varchar(256)";
                } else if (fieldType == XType.TYPE_DATE) {
                    typeString = "date";
                } else if (fieldType == XType.TYPE_BLOB) {
                    typeString = "blob";
                } else  if (fieldType == XType.TYPE_BOOLEAN) {
                    typeString = "smallint";
                } else {
                    continue;// ignore
                }
                info.type = typeString;
                if (info.isPrimayKey) {
                    infos.add(0,info);
                } else {
                    infos.add(info);
                }
            }
            return infos;
        }
        return null;
    }

    private Class<?> getClass(int type) {
        if (clazz.isEmpty()) {
            Set<String> names = ClassUtils.getClassName(typePackage, false);
            if (names == null) {
                return null;
            }
            for (String name : names) {
                try {
                    Class<?> kls = Class.forName(name);
                    Field indexField = kls.getField("TYPE_INDEX");
                    if (indexField != null) {
                        int index = indexField.getInt(null);
                        clazz.put(index, kls);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("error:" + name);
                    //e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    System.err.println("error:" + name);
                    //e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.err.println("error:" + name);
                    //e.printStackTrace();
                }
            }
        }
        return clazz.get(type);
    }

    public String getClassName(int type) {
        Class<?> cls = getClass(type);
        if (cls == null) {
            return "unknown:" + type;

        }
        return cls.getName();
    }

    public String getFieldName(int typeIndex,int fieldIndex) {
        Class<?> cls = getClass(typeIndex);
        if (cls == null) {
            return typeIndex + "." + fieldIndex;
        }
        String clsName = cls.getName();
        Field[] fieldList = cls.getFields();
        for(Field field : fieldList) {
            try {
                int value = (int) field.get(null);
                if (value == fieldIndex) {
                    return clsName +"." +field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return  typeIndex + "." + fieldIndex;
    }
}
