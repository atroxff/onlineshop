package cn.ff.onlineshop.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

public class EncryptUtil {

	/** base64 编码 */
	public static String base64Encode(String str) {
		return Base64.encodeBase64String(str.getBytes());
	}

	/** base64 解码 */
	public static String base64Decode(String base64Str) throws Exception {
		if (StringUtils.isNotBlank(base64Str)) {
			byte[] bt = Base64.decodeBase64(base64Str);
			String str = new String(bt, "UTF-8");
			return str;
		}
		return null;
	}

	 public static void main(String[] args) {
	 try {
	 System.out.println(base64Encode("1"));
	 System.out.println(base64Decode("MQ=="));
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 }

}
