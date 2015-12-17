package utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XR on 2015/12/17.
 */
public class JavaBeanUtil {
    public static  <T> Map<String, Object> obj2Map(T obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            PropertyDescriptor pd = null;
            try {
                pd = new PropertyDescriptor(field.getName(), obj.getClass());
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            Method getMethod = pd.getReadMethod();
            Object o = null;
            try {
                o = getMethod.invoke(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            if (o != null )
            map.put(field.getName(), o);
        }
        if (0 == map.get("id"))map.remove("id");
        return map;
    }
}
