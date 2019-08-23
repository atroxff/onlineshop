package cn.ff.onlineshop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util");
			nsaex.printStackTrace();
		}
	}

	/**
	 * 生成文件的md5校验值
	 */
	public static String getMD5(File file) throws IOException {
		if (file == null || !file.exists())
			return null;
		InputStream ins = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = ins.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		ins.close();
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5(InputStream ins) throws IOException {
		byte[] buffer = new byte[1024];
		int numRead = 0;
		while ((numRead = ins.read(buffer)) > 0) {
			messagedigest.update(buffer, 0, numRead);
		}
		return bufferToHex(messagedigest.digest());
	}
	
	/**
     * MD5加密字符串
     */
    public static String getMD5(String str) {
    	messagedigest.update(str.getBytes());
        return bufferToHex(messagedigest.digest());
    }

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		// 取字节中高 4 位的数字转换, >>>
		char c0 = hexDigits[(bt & 0xf0) >>> 4];
		// 取字节中低 4 位的数字转换
		char c1 = hexDigits[bt & 0x0f];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

}
