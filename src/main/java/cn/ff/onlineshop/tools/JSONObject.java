package cn.ff.onlineshop.tools;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONObject extends LinkedHashMap<String, Object> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JSONObject() {
    }

    public JSONObject(Map<String, Object> m) {
        if(m != null)
            this.putAll(m);
    }

    public static JSONObject fromObject(String json){
        JSONObject jsonObject = new JSONObject();
        try {
            if(json == null || json.trim().length() < 1)
                return jsonObject;

            ObjectMapper mapper = new ObjectMapper();
            jsonObject = mapper.readValue(json, JSONObject.class);
            return jsonObject==null?new JSONObject():jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
    /**
     * 将JSONObject对象转换成class
     * @param jsonObject
     * @param beanClass
     * @return null 转换失败，其他转换成功
     */
    public static <T> T toBean(JSONObject jsonObject,Class<T> beanClass){
        if (jsonObject==null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonObject.toString(), beanClass);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将对象转换成jsonString
     * @param object
     * @return null:转换失败或object==null，其他:转换成功
     */
    public static String fromObject(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            str= mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    /**
     * 获取String类型的value
     * @param key
     * @return
     */
    public String optString(String key) {
        Object v = get(key);
        if(v == null) return null;

        if(v instanceof String) {
            String value = (String) v;
            if("null".equals(value))
                return null;
            return value;
        } else {
            return String.valueOf(v);
        }
    }

    public Integer optInt(String key) {
        return optInt(key, null);
    }

    public Integer optInt(String key, Integer defVal) {
        Object value = get(key);
        if(value == null) return defVal;

        if(value instanceof Number) {
            Number num = (Number) value;
            return num.intValue();
        } else if(value instanceof String) {
            String string = optString(key);
            if(string==null || string.trim().length() < 1)
                return defVal;
            Double d = Double.parseDouble(string);
            return d.intValue();
        }
        return (Integer) value;
    }

    public Boolean optBoolean(String key) {
        Object value = get(key);
        if(value == null)
            return null;
        return (Boolean) value;
    }

    @Override
    public Object put(String key, Object value){
        if(key == null) return null;
        return super.put(key, value);
    }

    public JSONObject optJSONObject(String key) {
        @SuppressWarnings("unchecked")
        Map<String, Object> m = (Map<String, Object>) get(key);
        if(m == null) return null;
        return new JSONObject(m);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JSONArray optJSONArray(String key) {
        if(containsKey(key)) {
            List list = (List) get(key);
            JSONArray jsonArray = new JSONArray(list.size());
            jsonArray.addAll(list);
            return jsonArray;
        }
        return new JSONArray();
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 判断字符串是否是符合json格式
     * @param str
     * @return
     */
    public static boolean isJSONObject(String str){
        if(null==str||"".equals(str.trim())||"null".equals(str.trim())){
            return false;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.readValue(str, JSONObject.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
