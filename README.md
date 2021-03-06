
# Homepage:
 
 XData is a simple serialize framework based on binary data, it is swift, small, safe and stable. Enable you to transfer data among java, javascript, ios and flutter.
 You can edit your contract classes and learn more on the page: [http://www.xcore.top](http://www.xcore.top), and all api calls of this website are using XData as the sample.
 So, if you are using ,or considering using JSON ,XML, or ProtocolBuffer, We recommend you have a try on XData as an alternative.

# xdata-java
This is the xdata repo for java platform. If you want to download all platforms,there they are:

-  xdata-all: a repo managed all platforms by google repo tool.

    git@github.com:j383575602/xdata-all.git

-  xdata-java : java platform

    git@github.com:j383575602/xdata-java.git

-  xdata-objective-c : objective-c platform

    git@github.com:j383575602/xdata-objective-c.git

- xdata-javascript : javascript platform

    git@github.com:j383575602/xdata-javascript.git

-  xdata-dart : dart platform

    git@github.com:j383575602/xdata-dart.git


# Demo
    Assume there are two classes defined with XData protocol.  User, Car

    // init some models
    XCarWrapper car1 = new XCarWrapper();
    car1.setBrand("Audi");

    XCarWrapper car2 = new XCarWrapper();
    car2.setBrand("Porsche");

    List<XCarWrapper> cars = new ArrayList<>();
    cars.add(car1);
    carr.add(car2);


    XUserWrapper user = new XUserWrapper();
    user.setName("John Smith");
    user.setAge(35);
    user.setCars(cars);


    //start to serialize
    XDataWriter writer = new XDataWriter();
    byte[] bytes = writer.writeData(user);


    //start to deserialize
    XDataParser parser = new XDataParser();

    XData data = parser.parse(bytes);

    XUserWrapper user2 = new XUserWrapper(data);
    
    //start to check 
    assert(user2.getName().equals("John Smith"));
    assert(user2.getAge() == 35);
    assert(user2.getCars().size() == 2);
    assert(user2.getCars().get(0).getBrand().equals("Audi"));
    assert(user2.getCars().get(1).getBrand().equals("Porsche"));


# Supprted Types


 |order|datatype |  single |List      | Set   | StringMap|IntMap | LongMap | FloatMap |DoubleMap |
  |-----|-----|---------| ---------|--------|----------|-------|---------|----------|----------|
  |1|**int8**|int|List\<Byte>| Set\<Byte> |Map<String,Byte>|Map<Integer,Byte>| Map<Long,Byte>|Map<Float,Byte>|Map<Double,Byte>|
  |2|**int16**|int|List\<Short>| Set\<Short> |Map<String,Short>|Map<Integer,Short>| Map<Long,Short>|Map<Float,Short>|Map<Double,Short>|
  |3|**int32**|int|List\<Integer>|Set\<Integer>|Map<String,Integer>|Map<Integer,Integer>| Map<Long,Integer>|Map<Float,Integer>|Map<Double,Integer>|
  |4|**int64**|int|List\<Long>|Set\<Long>|Map<String,Long>|Map<Integer,Long>| Map<Long,Long>|Map<Float,Long>|Map<Double,Long>|
  |5|**float32**|float|List\<Float>|Set\<Float>|Map<String,Float>|Map<Integer,Float>| Map<Long,Float>|Map<Float,Float>|Map<Double,Float>|
  |6|**float64**|double|List\<Double>|Set\<Double>|Map<String,Double>|Map<Integer,Double>| Map<Long,Double>|Map<Float,Double>|Map<Double,Double>|
  |7|**boolean**|boolean|List\<Boolean>|Set\<Boolean>|Map<String,Boolean>|Map<Integer,Boolean>| Map<Long,Boolean>|Map<Float,Boolean>|Map<Double,Boolean>
  |8|**String**|String|List\<String>|Set\<String>|Map<String,String>|Map<Integer,String>| Map<Long,String>|Map<Float,String>|Map<Double,String>|
  |9|**Date**|Date|List\<Date>|Set\<Date>|Map<String,Date>|Map<Integer,Date>| Map<Long,Date>|Map<Float,Date>|Map<Double,Date>|
  |10|**byte[]**|byte[]|List\<byte[]>|Set\<byte[]>|Map<String,byte[]>|Map<Integer,byte[]>| Map<Long,byte[]>|Map<Float,byte[]>|Map<Double,byte[]>|
  |11|**XData**|XData|List\<XData>|Set\<XData>|Map<String,XData>|Map<Integer,XData>| Map<Long,XData>|Map<Float,XData>|Map<Double,XData>|
  
