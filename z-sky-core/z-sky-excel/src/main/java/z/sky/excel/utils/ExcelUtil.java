package z.sky.excel.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import z.sky.excel.annotation.ECell;
import z.sky.excel.annotation.ESheet;

/**
 * Excel导出工具
 * <p>有特殊属性，可另行完善
 * @see ESheet, ECell [配套注解]
 * @author jianming.zhou
 *
 */
public class ExcelUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	/**
	 * 导出Excel
	 * @param <T>
	 * @param list
	 */
	public static <T> Workbook getWorkbook(List<T> list) {
		ZipSecureFile.setMinInflateRatio(0.001);
		Workbook wb = new SXSSFWorkbook();
		if (list == null || list.isEmpty()) {
			wb.createSheet("Sheet");
			return wb;
		}
		try {
			makeSheet(wb, list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return wb;
	}
	
	/**
	 * 组装Sheet
	 * @param wb
	 * @param list
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static <T> void makeSheet(Workbook wb, List<T> list) throws IllegalArgumentException, IllegalAccessException {
		Object titleRow = list.get(0);
		Sheet sheet = createSheet(wb, titleRow);
		CellStyle titleStyle = createTitleStyle(wb);
		makeSheetTitle(wb, sheet, titleRow, titleStyle);
		makeSheetData(wb, sheet, list);
	}
	
	/**
	 * 创建Sheet
	 * @param wb
	 * @param titleRow
	 * @return
	 */
	private static Sheet createSheet(Workbook wb, Object titleRow) {
		Class<?> baseClazz = titleRow.getClass();
		ESheet eSheet = baseClazz.getAnnotation(ESheet.class);
		Sheet sheet = eSheet != null ? wb.createSheet(eSheet.value()) : wb.createSheet();
		return sheet;
	}
	
	/**
	 * 设置标题公共样式
	 * @param wb
	 * @return
	 */
	private static CellStyle createTitleStyle(Workbook wb) {
		CellStyle titleStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		titleStyle.setFont(font);
//		titleStyle.setAlignment(CellStyle.ALIGN_CENTER); 
		titleStyle.setBorderTop(CellStyle.BORDER_DOUBLE);
		titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
		return titleStyle;
	}
	
	/**
	 * 组装首行标题
	 * @param sheet
	 * @param titleRow
	 */
	private static void makeSheetTitle(Workbook wb, Sheet sheet, Object titleRow, CellStyle titleStyle) {
		Class<?> baseClazz = titleRow.getClass();
		Row headRow = sheet.createRow(0);
        Field[] headFields = baseClazz.getDeclaredFields();
        int cHeadIndex = 0;
        for(Field field : headFields){
        	ECell eCell = field.getAnnotation(ECell.class);
        	if (eCell == null) {
        		continue;
        	}
        	
        	Cell cell = headRow.createCell(cHeadIndex);
        	cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        	cell.setCellStyle(titleStyle);
    		if (!"".equals(eCell.value())) {
    			cell.setCellValue(eCell.value());
    		} else {
    			cell.setCellValue(field.getName());
    		}
    		sheet.setColumnWidth(cHeadIndex, eCell.width() * 256);
    		cHeadIndex++;
        }
	}
	
	/**
	 * 组装行数据
	 * @param sheet
	 * @param list
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static <T> void makeSheetData(Workbook wb, Sheet sheet, List<T> list) throws IllegalArgumentException, IllegalAccessException {
		Map<String, CellStyle> cellStyleMap = getCellStyleMap(wb);
		for (int rIndex = 0; rIndex < list.size(); rIndex++) {
			Row row = sheet.createRow(1 + rIndex);
			Object rowData = list.get(rIndex);
	        Class<?> clazz = rowData.getClass();
	        Field[] fields = clazz.getDeclaredFields();
	        int cIndex = 0;
	        for(Field field : fields){
	        	ECell eCell = field.getAnnotation(ECell.class);
	        	if (eCell == null) {
	        		continue;
	        	}
	        	field.setAccessible(true);
	        	Object value = field.get(rowData);
	        	if (value != null) {
	        		Cell cell = row.createCell(cIndex);
	        		setCellValue(cell, value, field.getType(), eCell, cellStyleMap);
	        	}
				cIndex++;
	        }
		}

	}
	
	/**
	 * 预设单元格样式
	 * 
	 * <p> NUMBER : 数字正常显示，非科学计数法
	 * 
	 * @param wb
	 * @return
	 */
	private static Map<String, CellStyle> getCellStyleMap(Workbook wb) {
		Map<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
		// NUMBER
		CellStyle cellStyle4Num = wb.createCellStyle();
		cellStyle4Num.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		cellStyleMap.put("NUMBER", cellStyle4Num);
		return cellStyleMap;
	}
	
	/**
	 * 设置单元格值
	 * @param cell
	 * @param value
	 * @param fieldType
	 */
	private static void setCellValue(Cell cell, Object value, Class<?> fieldType, ECell eCell, Map<String, CellStyle> cellStyleMap) {
		String fieldTypeName = fieldType.getSimpleName();
		switch(fieldTypeName) {
			case "String" : cell.setCellValue((String)value); break;
			case "int" :
			case "Integer" : cell.setCellValue((Integer)value); break;
			case "long" :
			case "Long" : 
				cell.setCellStyle(cellStyleMap.get("NUMBER"));
				cell.setCellValue((Long) value);
				break;
			case "double" :
			case "Double" : cell.setCellValue((Double)value); break;
			case "float" :
			case "Float" : cell.setCellValue((Float)value); break;
			case "short" :
			case "Short" : cell.setCellValue((Short)value); break;
			case "boolean" :
			case "Boolean" : cell.setCellValue((Boolean)value); break;
			case "byte" :
			case "Byte" : cell.setCellValue((Byte)value); break;
			case "Date" :
			case "Timestamp" :
			case "DateTime" : cell.setCellValue(new DateTime(value).toString(eCell.timeFormat())); break;
		}

	}
	
}
