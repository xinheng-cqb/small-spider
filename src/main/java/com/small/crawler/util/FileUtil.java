package com.small.crawler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author caiqibin
 * @introduce:
 * @date 2017/5/25.
 */
public class FileUtil {

	public static void mkdirs(String absolutPath) {
		File file = new File(absolutPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	public static void delFile(String absolutPath) {
		File file = new File(absolutPath);
		if (file.exists()) {
			file.delete();
		}
	}

	public static List<String> readFile(String absolutePath, String charset) {
		File file = new File(absolutePath);
		List<String> infoList = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			String line;
			while ((line = br.readLine()) != null) {
				infoList.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}

	public static List<String> readFile(String absolutePath) {
		File file = new File(absolutePath);
		List<String> infoList = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				infoList.add(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}

	public static void writeFile(List<String> infoList, String absolutePath) {
		File file = new File(absolutePath);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for (String info : infoList) {
				bw.write(info);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
