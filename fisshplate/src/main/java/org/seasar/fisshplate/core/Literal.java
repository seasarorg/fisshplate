package org.seasar.fisshplate.core;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.context.FPContext;

/**
 * リテラル値を持つセル情報を保持する要素クラスです。
 * @author rokugen
 *
 */
public class Literal extends AbstractCell {	

	/**
	 * コンストラクタです。
	 * @param sheet テンプレート側のシート
	 * @param cell 保持するテンプレート側のセル
	 * @param rowNum 行番号
	 */
	Literal(HSSFSheet sheet, HSSFCell cell, int rowNum) {
		super(sheet, cell,rowNum);
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.element.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) {
		HSSFCell out = context.getCurrentCell();		
		copyCellStyle(context, out);
		
		int cellType = templateCell.getCellType();
		out.setCellType(cellType);
		switch(cellType){
		case HSSFCell.CELL_TYPE_NUMERIC:
			out.setCellValue(templateCell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_STRING:
			out.setCellValue(templateCell.getRichStringCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			out.setCellValue(templateCell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			out.setCellFormula(templateCell.getCellFormula());
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			out.setCellErrorValue(templateCell.getErrorCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		default:
		}
		context.nextCell();
	}

}
