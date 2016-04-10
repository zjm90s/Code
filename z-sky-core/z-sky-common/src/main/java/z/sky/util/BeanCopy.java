package z.sky.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copy By Reflect
 * @author jianming.zhou
 * @since 2016-01-04
 *
 */
public class BeanCopy {

    /**
     * sourceBean赋值给targetBean
     * @param targetBean
     * @param sourceBean
     */
    public static void copy(Object targetBean, Object sourceBean){
        Map<String, Object> map = new HashMap<String, Object>();
        setValueToMap(map,sourceBean);

        setValues(targetBean,map);
    }

    /**
     * 将获取到的源bean值封装到一个map中
     * @param sourceMap
     * @param sourceBean
     */
    @SuppressWarnings("unchecked")
    public static void setValueToMap(Map<String, Object> sourceMap, Object sourceBean){
    	if (null == sourceMap || null == sourceBean) {
    		return;
    	}
        Class<?> clazz = sourceBean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            try {
                Method method = clazz.getMethod(getGetMethodName(field));
                Object value = method.invoke(sourceBean);
                if (null == value) {
                	continue;
                }

                Class<?> fieldType = field.getType();
                String fieldTypeName = fieldType.getSimpleName();
                String fieldName = field.getName();

                if(isBasicType(fieldTypeName)){  //java基础数据类型
                    sourceMap.put(fieldName, value);
                }else if(fieldType.isEnum()){  //Enum类型
                    sourceMap.put(fieldName, sourceSelfDefEnum((Enum<?>) value));
                }else if("List".equals(fieldTypeName)){  //List类型
                	//获取List<T>中T的类型
                	ParameterizedType  pt = (ParameterizedType) method.getGenericParameterTypes()[0];
                	Class<?> keyType = (Class<?>) pt.getActualTypeArguments()[0];
                	
                    List<Object> valueList = (List<Object>) value;
                    List<Object> list = new ArrayList<Object>();
                    if (isBasicType(keyType.getSimpleName())) {
                    	for (Object obj : valueList) {
                    		list.add(obj);
                    	}
                    } else {
                    	for (Object obj : valueList) {
                    		//把List<T>中T类型数据用Map封装
                    		Map<String, Object> map = new HashMap<String, Object>();
                    		setValueToMap(map,obj);
                    		list.add(map);
                    	}
                    }
                    sourceMap.put(fieldName, list);
                }else if("Map".equals(fieldTypeName)){  //Map类型
                    Map<Object,Object> valueMap = (Map<Object, Object>) value;
                    Set<Object> keys = valueMap.keySet();
                    Iterator<Object> it = keys.iterator();

                    while(it.hasNext()){
                    	Object key = it.next();
                    	Object val = valueMap.get(key);
                    	if (isBasicType(val.getClass().getSimpleName())) {
                    		valueMap.put(key, val);
                    	} else {
                    		//把Map<K,V>中V类型数据用Map封装
                    		Map<String, Object> map = new HashMap<String, Object>();
                    		setValueToMap(map,valueMap.get(key));
                    		valueMap.put(key, map);
                    	}
                    }
                    sourceMap.put(fieldName, valueMap);
                }else{  //自定义类
                    Map<String, Object> map = new HashMap<String, Object>();
                    setValueToMap(map,value);
                    sourceMap.put(fieldName, map);
                }

            } catch (Exception e) {
            	if (!(e instanceof NoSuchMethodException)) {
            		e.printStackTrace();
            	}
            }
        }

    }

    /**
     * 将获取的map封装数据写入目标bean中
     * @param targetBean
     * @param sourceMap
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void setValues(Object targetBean, Map<String, Object> sourceMap){
    	if (null == targetBean || null == sourceMap) {
    		return;
    	}
        Class<?> clazz = targetBean.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for(Field field : fields){
            try {
                Class<?> fieldType = field.getType();
                String fieldTypeName = fieldType.getSimpleName();
                String fieldName = field.getName();

                if(isBasicType(fieldTypeName)){  //java基础数据类型
                	Object value = basicType(fieldTypeName,sourceMap.get(fieldName));
                    Method method = clazz.getMethod(getSetMethodName(field), fieldType);
                    method.invoke(targetBean, value);
                }else if(fieldType.isEnum()){      //Enum类型
                    Method method = clazz.getMethod(getSetMethodName(field), fieldType);
                    method.invoke(targetBean, targetSelfDefEnum((Class<Enum>) fieldType, (String) sourceMap.get(fieldName)));
                }else if("List".equals(fieldTypeName)){  //List类型
                    List<Object> sourceList = (List<Object>) sourceMap.get(fieldName);

                    if (null != sourceList) {
                    	//获取List<T>中T的类型
                    	Method method = clazz.getMethod(getSetMethodName(field), fieldType);
                    	ParameterizedType  pt = (ParameterizedType) method.getGenericParameterTypes()[0];
                    	Class<?> keyType = (Class<?>) pt.getActualTypeArguments()[0];
                    	
                    	List<Object> targetList = new ArrayList<Object>();
                    	if (isBasicType(keyType.getSimpleName())) {
                    		for (Object object : sourceList) {
                    			targetList.add(object);
                    		}
                    	} else {
                      		for (Object object : sourceList) {
                    			Object obj = keyType.newInstance();
                    			setValues(obj, (Map<String, Object>)object);
                    			targetList.add(obj);
                    		}
                    	}
                    	method.invoke(targetBean, targetList);
                    }
                }else if("Map".equals(fieldTypeName)){  //Map类型
                    Map<Object, Object> valueMap = (Map<Object, Object>) sourceMap.get(fieldName);
                    
                    if (null != valueMap) {
                    	//获取Map<K,V>中K,V的类型
                    	Method method = clazz.getMethod(getSetMethodName(field), fieldType);
                    	ParameterizedType  pt = (ParameterizedType) method.getGenericParameterTypes()[0];
                    	Class<?> valueType = (Class<?>) pt.getActualTypeArguments()[1];
                    	
                    	Set<Object> keys = valueMap.keySet();
                    	Iterator<Object> it = keys.iterator();
                    	Map<Object, Object> targetMap = new HashMap<Object, Object>();
                    	while(it.hasNext()){
                    		Object key = it.next();
                    		Object val = valueMap.get(key);
                    		if (isBasicType(val.getClass().getSimpleName())) {
                    			targetMap.put(key, val);
                    		} else {
                    			Object obj = valueType.newInstance();
                    			setValues(obj, (Map<String, Object>) valueMap.get(key));
                    			targetMap.put(key, obj);
                    		}
                    	}
                    	method.invoke(targetBean, targetMap);
                    }
                }else{    //自定义类
                    Object obj = fieldType.newInstance();
                    Method method = clazz.getMethod(getSetMethodName(field), fieldType);
                    method.invoke(targetBean, obj);
                    setValues(obj,(Map<String, Object>) sourceMap.get(fieldName));
                }

            } catch (Exception e) {
            	if (!(e instanceof NoSuchMethodException)) {
            		e.printStackTrace();
            	}
            }
        }
    }

    /**
     * 获取get方法名
     * @param field
     * @return
     */
    private static String getGetMethodName(Field field){
        if("boolean".equals(field.getType().getSimpleName())){
            return "is" + firstCharUpperCase(field.getName());
        }else{
            return "get" + firstCharUpperCase(field.getName());
        }
    }

    /**
     * 获取set方法名
     * @param field
     * @return
     */
    private static String getSetMethodName(Field field){
        return "set" + firstCharUpperCase(field.getName());
    }

    /**
     * 首字母大写
     * @param str
     * @return
     */
    private static String firstCharUpperCase(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    /**
     * 判断是否是java基础数据类型
     * @param obj
     * @return
     */
    private static boolean isBasicType(String obj){
        switch(obj){
            case "String" : return true;
            case "Boolean" : return true;
            case "boolean" : return true;
            case "Integer" : return true;
            case "int" : return true;
            case "Long" : return true;
            case "long" : return true;
            case "Short" : return true;
            case "short" : return true;
            case "Double" : return true;
            case "double" : return true;
            case "Float" : return true;
            case "float" : return true;
            case "Byte" : return true;
            case "byte" : return true;
            case "Character" : return true;
            case "char" : return true;
            case "Date" : return true;
            case "Timestamp" : return true;
            case "DateTime" : return true;
            default : return false;
        }
    }

    /**
     * 如果是java基础数据类型，则返回相应类型值
     * <p>有时map中的属性类型会丢失，所以此处部分类型作了强制转换
     * @param type
     * @param obj
     * @return
     */
    private static Object basicType(String type, Object obj){
    	if (null == obj) {
    		return null;
    	}
        switch(type){
            case "String" : return obj;
            case "Boolean" : return obj;
            case "boolean" : return obj;
            case "Integer" : return Integer.valueOf(obj + "");
            case "int" : return Integer.parseInt(obj + "");
            case "Long" : return Long.valueOf(obj + "");
            case "long" : return Long.parseLong(obj + "");
            case "Short" : return Short.valueOf(obj + "");
            case "short" : return Short.parseShort(obj + "");
            case "Double" : return Double.valueOf(obj + "");
            case "double" : return Double.parseDouble(obj + "");
            case "Float" : return Float.valueOf(obj + "");
            case "float" : return Float.parseFloat(obj + "");
            case "Byte" : return Byte.valueOf(obj + "");
            case "byte" : return Byte.parseByte(obj + "");
            case "Character" : return obj;
            case "char" : return obj;
            case "Date" : return obj;
            case "Timestamp" : return obj;
            case "DateTime" : return obj;
            default : return null;
        }
    }

    /**
     * 源枚举类取值
     * @param sourceEnum
     * @return
     */
    private static String sourceSelfDefEnum(Enum<?> sourceEnum){
        return ((Enum<?>)sourceEnum).name();
    }

    /**
     * 目标枚举类赋值
     * @param targetEnumType
     * @param sourceEnumName
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Enum<?> targetSelfDefEnum(Class<Enum> targetEnumType, String sourceEnumName){
        return Enum.valueOf(targetEnumType, sourceEnumName);
    }
}
