/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.fisshplate.core;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * リテラル値を持つセル情報を保持する要素クラスです。
 * @author rokugen
 *
 */
public class Literal extends AbstractCell {	

	/**
	 * コンストラクタです。
	 * @param cell 保持するテンプレート側のセル
	 */
	Literal(CellWrapper cell) {
		super(cell);
	}

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.element.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) {
		HSSFCell out = context.getCurrentCell();
		copyCellStyle(context, out);
		
		HSSFCell templateCell = cell.getHSSFCell();
		
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
