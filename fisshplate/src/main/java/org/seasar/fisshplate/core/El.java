package org.seasar.fisshplate.core;

import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * テンプレートのセルの値が評価式の場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El extends AbstractCell {
	private String expression;

	/**
	 * コンストラクタです。
	 * @param sheet テンプレート側のシート
	 * @param cell テンプレート側のセル
	 * @param rowNum 行番号
	 * @param expression 評価式
	 */
	El(HSSFSheet sheet, HSSFCell cell,  int rowNum, String expression) {
		super(sheet, cell, rowNum);
		this.expression = expression;
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		HSSFCell out = context.getCurrentCell();		
		copyCellStyle(context, out);
		
		Object value = getValue(context);		
		if(value instanceof Date){
			out.setCellValue((Date)value);			
		}else if(isNumber(value)){
			out.setCellValue(Double.valueOf(value.toString()));
		}else{
			out.setCellValue(new HSSFRichTextString(value.toString()));			
		}
		context.nextCell();
	}
	
	private boolean isNumber(Object value){
		try{
			Double.valueOf(value.toString());
			return true;
		}catch (NumberFormatException e) {
			return false;
		}
	}
	
	private Object getValue(FPContext context) throws FPMergeException{	
		//TODO NULL許可、COALESCHE機能を追加
		Map<String,Object> data = context.getData();
		
		Object value = OgnlUtil.getValue(expression, data);
		if(value == null){
			throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED,new Object[]{expression});
		}
		return value;
	}

}
