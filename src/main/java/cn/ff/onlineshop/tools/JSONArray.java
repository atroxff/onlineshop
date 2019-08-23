package cn.ff.onlineshop.tools;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JSONArray extends ArrayList<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JSONArray() {
		
	}
	
	public JSONArray(int initialCapacity) {
		super(initialCapacity);
	}

	public JSONArray(List<Object> list) {
		if(list != null)
			this.addAll(list);
	}
	/**
	 * 将字符串转换为JSONArray
	 * @param json 
	 * @return 
	 */
	public static JSONArray fromObject(String json){
		JSONArray jsonArray = new JSONArray();
		try {
			if(json == null || json.trim().length() < 1)
				return jsonArray;
			ObjectMapper mapper = new ObjectMapper();
			jsonArray = mapper.readValue(json, JSONArray.class);
			return jsonArray==null?new JSONArray():jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
			return jsonArray;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static JSONArray fromObject(List list){
		return new JSONArray(list);
	}

	public String getString(int i) {
		return (String) get(i);
	}
	
	public JSONObject getJSONObject(int i) {
		@SuppressWarnings("unchecked")
		Map<String, Object> m = (Map<String, Object>) get(i);
		return new JSONObject(m);
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
     * 判断字符串是否是符合json数组
     * @param str
     * @return
     */
    public static boolean isJSONArray(String str){
    	if(null==str||"".equals(str.trim())||"null".equals(str.trim())){
    		return false;
    	}
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		mapper.readValue(str, JSONArray.class);
			return true;
		} catch (Exception e) {
			return false;
		}
    }
//	public static void main(String[] args) throws IOException {
//		String fragment = null;
//		ClassPathResource cpr = new ClassPathResource("common_jndi.json", DatabaseController.class);
//		System.out.println(fragment = FileUtils.readFileToString(cpr.getFile(), "utf-8"));
//		JSONArray jsonArray = JSONArray.fromObject(fragment);
//	}
}
