package org.seasar.fisshplate.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * 行要素クラスです。行の中にあるセルの情報を保持します。
 * @author rokugen
 *
 */
public class Row implements TemplateElement {
	private List<TemplateElement> cellElementList = new ArrayList<TemplateElement>();	
	private HSSFRow templateRow;
	
	private static final Pattern patEl = Pattern.compile("^\\s*\\$\\{(.+)\\}");
	
	/**
	 * コンストラクタです。テンプレート側の行オブジェクトを受け取り、その行内のセル情報を解析して保持します。
	 * @param templateRow テンプレート側の行オブジェクト
	 */
	Row(HSSFSheet templateSheet, HSSFRow templateRow){		
		this.templateRow = templateRow;
		if(templateRow == null){
			return;
		}		
		int rowNum = templateRow.getRowNum();
		//TODO リファクタリングしましょうよ
		for(int i=0; i <= templateRow.getLastCellNum();i++){
			HSSFCell templateCell = templateRow.getCell((short) i);
			if(templateCell == null){
				continue;
			}
			TemplateElement elem = new NullElement();
			if(templateCell.getCellType() == HSSFCell.CELL_TYPE_STRING){			
				String value = templateCell.getRichStringCellValue().getString();
				Matcher mat;			
				if((mat = patEl.matcher(value)).find()){
					String expression = mat.group(1);
					elem = new El(templateSheet,templateCell, rowNum, expression);
				}else{
					elem = new Literal(templateSheet,templateCell,rowNum);
				}
			}else{
				elem = new Literal(templateSheet,templateCell,rowNum);
			}
			cellElementList.add(elem);
		}
	}
	
	public void merge(FPContext context) throws FPMergeException {
		HSSFRow outRow = context.getcurrentRow();
		if(templateRow != null){
			outRow.setHeight(templateRow.getHeight());
		}
		Map<String, Object> data = context.getData();
		data.put(FPConsts.ROW_NUMBER_NAME, context.getCurrentRowNum() + 1);
		for(TemplateElement elem: cellElementList){
			elem.merge(context);
		}
		context.nextRow();
	}

}
