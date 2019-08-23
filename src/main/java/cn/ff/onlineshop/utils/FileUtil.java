package cn.ff.onlineshop.utils;

//import com.hikvision.ga.dvisual.web.common.constants.DvisualConstants;
//import com.hikvision.ga.dvisual.web.common.constants.ErrorCode;
//import com.hikvision.ga.dvisual.web.common.tools.json.JSONObject;
//import com.hikvision.ga.logger.build.HikGaLoggerFactory;
//import com.hikvision.ga.logger.log.HikGaLogger;
import cn.ff.onlineshop.tools.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 与文件相关的工具类
 */
public class FileUtil {
	//private static final HikGaLogger logger = HikGaLoggerFactory.getLogger(FileUtil.class);

	/**
	 * 检查文件夹路径是否存在,若不存在，创建一个
	 */
	public static void checkFolder(String url) {
		File file = new File(url);
		if (!file.exists() && file.getParentFile().exists()) {
			file.mkdir();
		}
	}

	/**
	 * 删除文件或文件夹，若文件夹下有文件，则一并删除
	 */
	public static void delete(String filePath) {
		if (StringUtils.isNotBlank(filePath)) {
			delete(new File(filePath));
		}
	}

	/**
	 * 删除文件或文件夹，若文件夹下有文件，则一并删除
	 */
	public static void delete(File file) {
		if (file != null && file.exists()) {
			File[] files = null;
			if (file.isDirectory() && (files = file.listFiles()) != null && files.length != 0) {
				for (int i = 0; i < files.length; i++) {
					delete(files[i]);
				}
			}
			if (!file.delete() && file.exists()) {
				//没有权限删除该文件
				//logger.errorWithErrorCode(ErrorCode.SERVER_ERROR.getCode(), "没有权限删除该文件");
			}
		}
	}

	/**
	 * 根据文件名，获取classpath下的文件
	 */
	public static File getFileFromClasspath(String filename) throws FileNotFoundException {
		return ResourceUtils.getFile("classpath:" + filename);
	}

	/**
	 * 获取文件后缀
	 */
	public static String getFileSuffix(String fileName) {
		if (StringUtils.isBlank(fileName) || !fileName.contains(".")) {
			return null;
		}
		return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
	}

	/**
	 * 读取csv文件，并转成JSON字符串 首行默认为名称，会将名称全部转成小写
	 */
	public static String readCSVFile(File file) throws IOException {
		InputStream is = null;
		InputStreamReader fr = null;
		BufferedReader br = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<List<String>> listFile = new ArrayList<List<String>>();
		try {
			// 读取文件
			is = new FileInputStream(file);
			fr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(fr);
			String rec = null;// 一行
			String str;// 一个单元格
			// 读取一行
			while ((rec = br.readLine()) != null) {
				int index = 0;
				String p = "(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,";
				Pattern pCells = Pattern.compile(p);
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
					index = mCells.end();
				}
				cells.add(rec.substring(index));
				listFile.add(cells);
			}
			// 开始解析文件内容
			if (listFile != null && listFile.size() > 0) {
				List<String> names = listFile.get(0);
				for (int i = 1; i < listFile.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					List<String> values = listFile.get(i);
					for (int j = 0; j < values.size(); j++) {
						map.put(names.get(j).toLowerCase(), values.get(j));
					}
					result.add(map);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				is.close();
			}
			if (fr != null) {
				fr.close();
			}
			if (br != null) {
				br.close();
			}
		}
		return JSONObject.fromObject(result);
	}

	/**
	 * 获取路径下的所有文件/文件夹
	 * @param directoryPath 需要遍历的文件夹路径
	 * @param isAddDirectory 是否将子文件夹的路径也添加到list集合中
	 * @return
	 */
	public static Set<String> getAllFile(String directoryPath, boolean isAddDirectory) {
		Set<String> list = new HashSet<String>();
		File baseFile = new File(directoryPath);
		if (baseFile.isFile() || !baseFile.exists()) {
			return list;
		}
		File[] files = baseFile.listFiles();
		if(files != null){
			for (File file : files) {
				if (file.isDirectory()) {
					if(isAddDirectory){
						list.add(file.getAbsolutePath());
					}
					list.addAll(getAllFile(file.getAbsolutePath(),isAddDirectory));
				} else {
					list.add(file.getName());
				}
			}
		}
		return list;
	}
}
