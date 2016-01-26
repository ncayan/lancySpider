package tool;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
/*import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;*/

import java.security.MessageDigest;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;

import play.db.ebean.Transactional;

public class comfunction {

	public static String getCurTime() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date()).toString();
	}

	public static String getCurFileName() {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return df.format(new Date()).toString();
	}

	public static String getDateStr(Date dt) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dt).toString();
	}

	public static String getDateTimeStr(Date dt) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(dt).toString();
	}

	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };

		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 删除文件
	public static boolean deleteFile(String deleteFiles) {

		if (deleteFiles != null && !deleteFiles.equals("")
				&& !deleteFiles.equals(",")) {
			deleteFiles = deleteFiles.substring(1);
			for (String file : deleteFiles.split(",")) {
				try {
					file = file.replace("/", File.separator);
					FileUtils.forceDelete(new File(Constans.UPLOAD_PATH_PRODUCT
							+ File.separator + file));
				} catch (Exception e) {
					return false;
				}
			}

		}
		return true;
	}

	public static boolean deleteFileDB(String dbFileName) {

		if (dbFileName != null && !dbFileName.equals("")
				&& !dbFileName.equals(",")) {

			for (String file : dbFileName.split(",")) {
				try {
					file = file.replace("/", File.separator);
					FileUtils.forceDelete(new File(Constans.UPLOAD_PATH_PRODUCT
							+ File.separator + file));
				} catch (Exception e) {
					return false;
				}
			}

		}
		return true;
	}

	public static boolean isSuperManager(String username, String password) {
		boolean isSuperManager = false;
		if (username.equals("admin") && MD5(password).equals(MD5("admin")))
			isSuperManager = true;
		return isSuperManager;
	}

	/*public static String uploadFile(String location, File file, String prefix) {
		String rtn = "";
		String hostName = "localhost";
		String username = "user";
		String password = "user";
		FTPClient ftp = null;

		InputStream in = null;
		try {
			ftp = new FTPClient();
			ftp.connect(hostName);
			ftp.login(username, password);

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.makeDirectory(location);
			ftp.changeWorkingDirectory("/" + location);

			int reply = ftp.getReplyCode();

			if (FTPReply.isPositiveCompletion(reply)) {

				in = new FileInputStream(file);
				String filename = getCurFileName() + "." + prefix;
				ftp.storeFile(filename, in);
				rtn = "/" + location + "/" + filename;
			}

			ftp.logout();
			ftp.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rtn;
	}*/

	public static String hashMapToJson(Map map) {
		String string = "";
		for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
			string = string + "{";
			Entry e = (Entry) it.next();
			string += "'id':'" + e.getKey() + "',";
			string += "'name':'" + e.getValue() + "'";
			string += "},";
		}
		if (string.lastIndexOf(",") != -1) {
			string = string.substring(0, string.lastIndexOf(","));
		}
		return string;
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isDate(String str) {
		boolean isDate = false;
		try {
			DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			formater.parse(str);
			isDate = true;
		} catch (Exception e) {

		}

		return isDate;
	}

	/*public static boolean WriteExcel(String fileToBeWrite, String sheetName,
			List<String> lstFloor, List<String> lstTitle) throws Exception {

		// 创建对工作表的引用。
		boolean isWrite = false;
		File file = new File(fileToBeWrite);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fOut = null;
		try {
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);

			Sheet sheet = workbook.createSheet(sheetName);
			Row rowhead = sheet.createRow(0);
			for (int k = 0; k < lstTitle.size(); k++) {
				Cell cellhd = rowhead.createCell(k);
				cellhd.setCellType(Cell.CELL_TYPE_STRING);
				cellhd.setCellValue(lstTitle.get(k));
			}

			for (int i = 0; i < lstFloor.size(); i++) {
				String[] vstr = lstFloor.get(i).split("\\|");
				Row rowbody = sheet.createRow(i + 1);
				int columnLength = vstr.length;
				for (int j = 0; j < lstTitle.size(); j++) {
					Cell cellbd = rowbody.createCell(j);
					cellbd.setCellType(Cell.CELL_TYPE_STRING);

					if (j >= columnLength) {
						cellbd.setCellValue("");
					} else
						cellbd.setCellValue(vstr[j]);
				}

			}

			fOut = new FileOutputStream(fileToBeWrite);

			workbook.write(fOut);
			isWrite = true;
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (fOut != null)
				fOut.close();
			fOut = null;
		}
		return isWrite;
	}*/
	public static boolean WriteExcel_x(String fileToBeWrite, String sheetName,
			List<String> lstFloor, List<String> lstTitle) throws Exception {

		// 创建对工作表的引用。
		boolean isWrite = false;
		File file = new File(fileToBeWrite);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream fOut = null;
		try {
			SXSSFWorkbook workbook = new SXSSFWorkbook(100);

			Sheet sheet = workbook.createSheet(sheetName);
			Row rowhead = sheet.createRow(0);
			for (int k = 0; k < lstTitle.size(); k++) {
				Cell cellhd = rowhead.createCell(k);
				cellhd.setCellType(Cell.CELL_TYPE_STRING);
				cellhd.setCellValue(lstTitle.get(k));
			}

			for (int i = 0; i < lstFloor.size(); i++) {
				String[] vstr = lstFloor.get(i).split("\\$");
				Row rowbody = sheet.createRow(i + 1);
				int columnLength = vstr.length;
				for (int j = 0; j < lstTitle.size(); j++) {
					Cell cellbd = rowbody.createCell(j);
					cellbd.setCellType(Cell.CELL_TYPE_STRING);

					if (j >= columnLength) {
						cellbd.setCellValue("");
					} else
						cellbd.setCellValue(vstr[j]);
				}

			}

			fOut = new FileOutputStream(fileToBeWrite);

			workbook.write(fOut);
			isWrite = true;
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (fOut != null)
				fOut.close();
			fOut = null;
		}
		return isWrite;
	}

	/*public static String ReadExcel(File fileToBeRead, String sheetName,
			String areaid) throws Exception {

		// 创建对工作表的引用。
		String errMessage = "";
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
					fileToBeRead));
			XSSFSheet sheet = null;
			try {
				sheet = workbook.getSheet(sheetName);// 提供服务清单
			} catch (Exception e) {
				System.out.println("异常：[" + fileToBeRead.getName() + "--没有--"
						+ sheetName + "sheet]");
				throw new Exception("【异常：读取表[" + fileToBeRead.getName()
						+ "]时异常!】");
			}
			int firstRowNum = sheet.getFirstRowNum(); // 得到第一行
			int lastRowNum = sheet.getLastRowNum(); // 得到最后一行
			Object object = null, object5 = null, object6 = null, object7 = null, object8 = null;
			for (int i = firstRowNum + 1; i <= lastRowNum; i++) {
				XSSFRow row = sheet.getRow(i);
				int firstCellNum = row.getFirstCellNum();// 明确
				int lastCellNum = row.getLastCellNum();// 明确

				XSSFCell cell = row.getCell(0);
				object = getExcel(cell);
				if (object == null) {
					errMessage = "首列数据不能为空，请填写";
					return errMessage;
				} else {
					if (!isNumeric(object.toString())) {
						errMessage = "首列数据不正确";
						return errMessage;
					} else {
						XSSFCell cell7 = row.getCell(6);
						object7 = getExcel(cell7);
						XSSFCell cell8 = row.getCell(7);
						object8 = getExcel(cell8);
						if (object7 == null || !isDate(object7.toString())) {
							errMessage = "入住日期不能为空且必须为2011-11-11这样的日期格式(文本格式)";
							return errMessage;
						}
						if (object8 == null || !isDate(object8.toString())) {
							errMessage = "搬出日期不能为空且必须为2011-11-11这样的日期格式(文本格式)";
							return errMessage;
						}
					}

				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("【异常：读取表[" + fileToBeRead.getName() + "]时异常!】");
		}
		return errMessage;
	}

	public static Object getExcel(XSSFCell cell) {
		Object object = null;
		try {
			if (cell != null) {
				int k = cell.getCellType();

				if (k == 0) {
					// 是否为日期
					if (HSSFDateUtil.isCellInternalDateFormatted(cell)) {
						// 用于转化为日期格式
						Date d = cell.getDateCellValue();
						DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
						String date = formater.format(d);
						object = cell.getDateCellValue();
					} else {
						object = cell.getNumericCellValue();

					}

				} else if (k == 1) {
					object = cell.getStringCellValue();

					if (object != null) {

						object = object.toString().trim().replace(" ", "")
								.replace("\n", "");
					}
				} else {
					object = null;
				}
			}
		} catch (Exception e) {
			System.out.println("第" + cell.getRowIndex() + "行"
					+ cell.getColumnIndex() + "列 Exception");
			return null;
		}
		cell = null;

		return object;
	}*/

	public static String FormetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * * 字符串转换到时间格式 * @param dateStr 需要转换的字符串 * @param formatStr 需要格式的目标字符串 举例
	 * yyyy-MM-dd * @return Date 返回转换后的时间 * @throws ParseException 转换异常
	 */
	public static Date StringToDate(String dateStr, String formatStr) {
		DateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
