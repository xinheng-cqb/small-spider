package com.small.crawler.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.common.collect.Lists;

/**
 * @author xinheng-cqb
 * @date 2018年3月9日
 * @introduce: 处理excel的工具类，Excel2003和Excel2007版本都可以
 * @pom依赖为： <dependency> <groupId>org.apache.poi</groupId> <artifactId>poi-ooxml</artifactId>
 *          <version>3.17</version> </dependency>
 */
public class ExcelUtil {

	/**
	 * @introduce: 合并目录下的所有以suffixSign结尾的文件为一个大的xlsx结尾的大文件 其中标题行只获取一次
	 * @param inputPath 要合并的文件夹路径
	 * @param outFileName 新输出的文件名称(不用带后缀)，在inputPath目录下存放
	 * @param suffixSign 要进行合并的文件的结尾标识
	 * @return void
	 */
	public final static void jointExcel(String inputPath, String outFileName, String suffixSign) {
		List<String> fileList = getSubFileAbsolutePathArray(inputPath, suffixSign);
		if (fileList.size() == 0) {
			System.out.println(MessageFormat.format("=========目录：{0}下没有以 {1}结尾的文件，请核查=========", inputPath, suffixSign));
			return;
		}
		fileList.sort(new Comparator<String>() {
			@Override
			public int compare(String x, String y) {
				String[] x_array = x.split("_");
				String[] y_array = y.split("_");
				int length = x_array.length < y_array.length ? x_array.length : y_array.length;
				for (int i = 0; i < length; i++) {
					int result = compareValue(x_array[i], y_array[i]);
					if (result == 0) {
						continue;
					} else {
						return result;
					}
				}
				return 0;
			}
		});
		List<String> contentList = Lists.newArrayList();
		for (int i = 0; i < fileList.size(); i++) {
			List<String> tempList = readExcel(fileList.get(i));
			if (i > 0) {
				tempList.remove(0);
			}
			contentList.addAll(tempList);
		}
		if (!inputPath.endsWith("\\")) {
			inputPath += "\\";
		}
		exportExcel(MessageFormat.format("{0}{1}.xlsx", inputPath, outFileName), contentList);
	}

	private static int compareValue(String a, String b) {
		if (a.contains("\\")) {
			a = a.substring(a.lastIndexOf("\\"));
			b = b.substring(b.lastIndexOf("\\"));
		}
		a = a.split(".xls")[0];
		b = b.split(".xls")[0];
		String a_num = CrawlerUtil.matchNumber(a, "");
		if (a_num == null) {
			if (a.equals(b)) {
				return 0;
			} else {
				return a.compareTo(b) < 0 ? -1 : 1;
			}
		} else {
			String b_num = CrawlerUtil.matchNumber(b, "");
			if (b_num == null) {
				return 1;
			}
			if (Integer.parseInt(a_num) == Integer.parseInt(b_num)) {
				return 0;
			} else {
				return Integer.parseInt(a_num) < Integer.parseInt(b_num) ? -1 : 1;
			}
		}
	}

	/**
	 * @introduce:获取文件夹下以suffixSign结尾的所有文件
	 * @param folderPath
	 * @param suffixSign 结尾字符串类似.xls or .xlsx
	 * @return List<String>
	 */
	public static List<String> getSubFileAbsolutePathArray(String folderPath, String suffixSign) {
		File[] files = new File(folderPath).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(suffixSign)) {
					return true;
				}
				return false;
			}
		});
		List<String> filePathList = Lists.newArrayList();
		for (File file : files) {
			filePathList.add(file.getAbsolutePath());
		}
		return filePathList;
	}

	public final static String exportExcel(String fileName, List<String> contentList) {
		return exportExcel(fileName, contentList, false);
	}

	/**
	 * @introduce: 将集合内容导出到本地文件(集合中的数据按照列展示)
	 * @param fileName 导出文件路径+名字（请注意以.xls or xlsx结尾）
	 * @param listContent EXCEL文件正文数据集合 （每一列内容用;分隔 样例：1;test1;test11;中文）
	 * @param 是否进行行列转换
	 * @return String 反馈信息
	 */
	public final static String exportExcel(String fileName, List<String> contentList, boolean shiftdim) {
		String result = "提示：Excel文件导出成功！生成的文件位置是：" + fileName;
		try {
			// 声明一个工作薄
			Workbook workBook = null;
			if (fileName.endsWith("xlsx")) {
				workBook = new XSSFWorkbook();
			} else if (fileName.endsWith("xls")) {
				workBook = new HSSFWorkbook();
			}
			// 定义样式
			CellStyle contentStyle = workBook.createCellStyle();
			contentStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
			contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
			// 生成一个表格
			Sheet sheet = workBook.createSheet();
			workBook.setSheetName(0, "data");

			if (shiftdim) {
				// 插入需导出的数据
				for (int j = 0; j < contentList.get(0).split(";").length; j++) {
					Row row = sheet.createRow(j);
					for (int i = 0; i < contentList.size(); i++) {
						String[] tempList = contentList.get(i).split(";");
						row.createCell(i).setCellValue(tempList[j]);
					}
				}
			} else {
				// 插入需导出的数据
				for (int i = 0; i < contentList.size(); i++) {
					String[] tempList = contentList.get(i).split(";");
					Row row = sheet.createRow(i);
					for (int j = 0; j < tempList.length; j++) {
						row.createCell(j).setCellValue(tempList[j]);
					}
				}
			}

			File file = new File(fileName);
			FileOutputStream fos = new FileOutputStream(file);
			workBook.write(fos);
			fos.flush();
			fos.close();
			workBook.close();
		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	/**
	 * @introduce:读取excel文件中的内容到list中，每一行用;符号拼接成一个字符串
	 * @param filePath
	 * @return List<String>
	 */
	public final static List<String> readExcel(String filePath) {
		Workbook workBook = getWorkBook(filePath);
		List<String> contentList = Lists.newArrayList();
		if (workBook == null) {
			return contentList;
		}
		for (int sheetNum = 0; sheetNum < workBook.getNumberOfSheets(); sheetNum++) {
			// 获得当前sheet工作表
			Sheet sheet = workBook.getSheetAt(sheetNum);
			if (sheet == null) {
				continue;
			}
			// 获得当前sheet的开始行
			int firstRowNum = sheet.getFirstRowNum();
			// 获得当前sheet的结束行
			int lastRowNum = sheet.getLastRowNum();
			// 循环除了第一行的所有行
			for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
				// 获得当前行
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				// 获得当前行的开始列
				int firstCellNum = row.getFirstCellNum();
				// 获得当前行的列数
				int lastCellNum = row.getPhysicalNumberOfCells();
				StringBuilder sb = new StringBuilder();
				// 循环当前行
				for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
					Cell cell = row.getCell(cellNum);
					sb.append(getCellValue(cell)).append(";");
				}
				contentList.add(sb.toString());
			}
		}
		try {
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contentList;
	}

	/**
	 * @introduce: 读取单元格内容为字符串
	 * @param cell
	 * @return String
	 */
	private static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 把数字当成String来读，避免出现1读成1.0的情况
		CellType cellType = cell.getCellTypeEnum();
		if (CellType.NUMERIC == cellType) {
			cell.setCellType(CellType.STRING);
		}
		cellType = cell.getCellTypeEnum();
		if (CellType.STRING == cellType) {
			cellValue = String.valueOf(cell.getStringCellValue());
		} else if (CellType.BOOLEAN == cellType) {
			cellValue = String.valueOf(cell.getBooleanCellValue());
		} else if (CellType.FORMULA == cellType) {
			cellValue = String.valueOf(cell.getCellFormula());
		} else if (CellType.BLANK == cellType) {
			cellValue = "";
		} else if (CellType.ERROR == cellType) {
			cellValue = "非法字符";
		} else {
			cellValue = "未知类型";
		}
		return cellValue;
	}

	/**
	 * @introduce: 根据文件后缀读取对应版本的excel
	 * @param filePath
	 * @return Workbook
	 */
	private static Workbook getWorkBook(String filePath) {
		// 获得文件名
		// 创建Workbook工作薄对象，表示整个excel
		Workbook workbook = null;
		try {
			// 获取excel文件的io流
			InputStream is = new FileInputStream(new File(filePath));
			// 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
			if (filePath.endsWith("xls")) {
				// 2003
				workbook = new HSSFWorkbook(is);
			} else if (filePath.endsWith("xlsx")) {
				// 2007
				workbook = new XSSFWorkbook(is);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * @introduce:合并多个Excel为一个
	 * @param outputFileName 输出文件（最好带路径）
	 * @param inputFileNameArray 需要合并的多个文件数组
	 */
	public static void mergeExcel(String outputFileName, String... inputFileNameArray) {
		Workbook workBook = null;
		if (inputFileNameArray[0].endsWith("xlsx")) {
			workBook = new XSSFWorkbook();
		} else if (inputFileNameArray[0].endsWith("xls")) {
			workBook = new HSSFWorkbook();
		}
		// 定义样式
		CellStyle contentStyle = workBook.createCellStyle();
		contentStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
		contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中

		int index = 0;
		// 生成一个表格
		for (String inputFileName : inputFileNameArray) {
			Workbook workBookInput = getWorkBook(inputFileName);
			for (int sheetNum = 0; sheetNum < workBookInput.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
				Sheet sheetInput = workBookInput.getSheetAt(sheetNum);
				String sheetName = sheetInput.getSheetName();
				if (workBookInput.getNumberOfSheets() == 1) {
					sheetName = inputFileName.substring(inputFileName.lastIndexOf("\\") + 1);
				}
				Sheet sheet = workBook.createSheet();
				workBook.setSheetName(index, sheetName);
				copy(sheetInput, sheet);
				index++;
			}
		}
		try {
			File file = new File(outputFileName);
			FileOutputStream fos = new FileOutputStream(file);
			workBook.write(fos);
			fos.flush();
			fos.close();
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void copy(Sheet formSheet, Sheet toSheet) {
		// 获得当前sheet的开始行
		int firstRowNum = formSheet.getFirstRowNum();
		// 获得当前sheet的结束行
		int lastRowNum = formSheet.getLastRowNum();
		// 循环除了第一行的所有行
		for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
			// 获得当前行
			Row row = formSheet.getRow(rowNum);
			Row toRow = toSheet.createRow(rowNum);
			if (row == null) {
				continue;
			}
			// 获得当前行的开始列
			int firstCellNum = row.getFirstCellNum();
			// 获得当前行的列数
			int lastCellNum = row.getPhysicalNumberOfCells();
			// 循环当前行
			for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
				Cell cell = row.getCell(cellNum);
				toRow.createCell(cellNum).setCellValue(getCellValue(cell));
			}
		}
	}

	/**
	 * @introduce: 的获取文件夹下以".xls" 结尾的所有文件 ，
	 * @param folderPath
	 * @return List<String>
	 */
	public static String[] getSubFileAbsolutePathArray(String folderPath) {
		List<String> fileList = getSubFileAbsolutePathArray(folderPath, ".xls");
		return fileList.toArray(new String[fileList.size()]);
	}
}
