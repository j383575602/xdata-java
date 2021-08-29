package top.xcore.xdata;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by wlzhao on 2017/2/22.
 */
public class ClassUtils {
    public static Set<String> getClassName(String packageName, boolean isRecursion) {
        Set<String> classNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (packageName == null) {
            return null;
        }
        String packagePath = packageName.replace(".", "/");

        URL url = loader.getResource(packagePath);
        System.out.println(packagePath + "=>" + url);
        if (url != null) {
            String protocol = url.getProtocol();
            System.out.println(protocol + ":" + protocol);
            if ("file".equals(protocol)) {
                classNames = getClassNameFromDir(url.getPath(), packageName, isRecursion);
            } else if ("jar".equals(protocol)) {
                classNames = getClassNameFromJar(url.getPath(), packageName, isRecursion);
            }
        }
        return classNames;
    }
                /**
                 * 从项目文件获取某包下所有类
                 * @param filePath 文件路径
                 * @param isRecursion 是否遍历子包
                 * @return 类的完整名称
                 */
    private static Set<String> getClassNameFromDir(String filePath, String packageName, boolean isRecursion) {
        System.out.println("filePath:" + filePath);
        Set<String> className = new HashSet<String>();
        File file = new File(filePath);
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                if (isRecursion) {
                    className.addAll(getClassNameFromDir(childFile.getPath(), packageName+"."+childFile.getName(), isRecursion));
                }
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    className.add(packageName+ "." + fileName.replace(".class", ""));
                }
            }
        }
        return className;
    }

    private static Set<String> getClassNameFromJar(String filePath, String packageName, boolean isRecursion) {
        System.out.println("filePath:" + filePath);
        Set<String> className = new HashSet<String>();
        String jarPath = filePath.substring(filePath.indexOf("/"),filePath.indexOf(".jar") + 4);
        System.out.println("jarPath:" + jarPath);
        JarFile file = null;
        try {
            file = new JarFile(jarPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> files = file.entries();
        while(files.hasMoreElements()) {
            JarEntry entry = files.nextElement();
           // System.out.println("entry:" + entry.getName());
            if (entry.isDirectory()) {
                continue;
            }
            String name = entry.getName();
            if (name.endsWith(".class") && !name.contains("$")) {
                String fileName =  name.replace(".class", "");
                fileName = fileName.replace("/",".");
                System.out.println("fileName:" + fileName);
                className.add(fileName);
            }
        }
        return className;
    }

    private static Class getTargetClass(int type) throws ClassNotFoundException, IllegalAccessException {
        Set<String> allClasses = null;
        if (type == XType.TYPE_OBJECT_REF) {
            allClasses = new HashSet<>();
            allClasses.add("com.ctrip.serialize.xdata.core.ObjectRef");
        } else {
            allClasses = getClassName("com.ctrip.serialize.xdata.schema", false);
        }
        for (String name : allClasses) {
            Class<?> c = Class.forName(name);
            Field[] fields = c.getFields();
            Field typeIndex = null;
            ArrayList<Field> columns = new ArrayList<>();
            for (Field field : fields) {
                int value = (int) field.get(null);
                if (value == type) {
                    typeIndex = field;
                    return c;
                }
            }
        }
        return null;
    }

    protected static List<ColumnInfo> getColumns(int type) {
        Set<String> allClasses = null;
        if (type == XType.TYPE_OBJECT_REF) {
            allClasses = new HashSet<>();
            allClasses.add("com.ctrip.serialize.xdata.core.ObjectRef");
        } else {
            allClasses =getClassName("com.ctrip.serialize.xdata.schema", false);
        }
        for(String name : allClasses) {
            try {
                Class<?> c = Class.forName(name);
                Field[] fields = c.getFields();
                Field typeIndex = null;
                ArrayList<Field> columns = new ArrayList<>();
                b : for(Field field : fields) {
                    int value = (int) field.get(null);
                    if (value == type) {
                        typeIndex = field;
                    } else {
                        columns.add(field);
                    }
                }
                if (typeIndex != null) {
                    List<ColumnInfo> infos = new ArrayList<>(columns.size());
                    for(Field field : columns) {
                        int value = (int) field.get(null);
                        int fieldType = value & XType.MASK_TYPE;
                        int index = value & XType.MASK_INDEX;
                        ColumnInfo info = new ColumnInfo();
                        info.name = "F"+ value;
                        info.isPrimayKey = (index == 1);
                        String typeString = "";
                        if (fieldType == XType.TYPE_BYTE_i_1) {
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
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
