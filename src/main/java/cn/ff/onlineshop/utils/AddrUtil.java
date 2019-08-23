package cn.ff.onlineshop.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 地址相关 支持windows及linux系统
 */
public class AddrUtil {

	/**
	 * 是否是windows系统
	 * 
	 * @return true：是，false:否
	 */
	public static boolean isWindowOS() {
		boolean isWindowOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowOS = true;
		}
		return isWindowOS;
	}

	/**
	 * 获取InetAddress 支持windows及linux系统
	 * 
	 * @return
	 */
	public static InetAddress getInetAddress() {
		InetAddress inetAddress = null;
		try {
			// 如果是windows操作系统
			if (isWindowOS()) {
				inetAddress = InetAddress.getLocalHost();
			} else {
				boolean bFindIP = false;
				// 定义一个内容都是NetworkInterface的枚举对象
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				// 如果枚举对象里面还有内容(NetworkInterface)
				while (null != netInterfaces && netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					// 获取下一个内容(NetworkInterface)
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有IP
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						inetAddress = (InetAddress) ips.nextElement();
						if (inetAddress.isSiteLocalAddress() // 属于本地地址
								&& !inetAddress.isLoopbackAddress() // 不是回环地址
								&& inetAddress.getHostAddress().indexOf(":") == -1) { // 地址里面没有:号
							bFindIP = true; // 找到了地址
							break; // 退出循环
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inetAddress;
	}

	/**
	 * 获取本地IP地址
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		if (getInetAddress() == null) {
			return "127.0.0.1";
		} else {
			return getInetAddress().getHostAddress();
		}
	}

	/**
	 * 获取真实IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}