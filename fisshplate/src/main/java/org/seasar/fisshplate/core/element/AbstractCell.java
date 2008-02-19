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
package org.seasar.fisshplate.core.element;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * セル要素の基底抽象クラスです。
 * @author rokugen
 *
 */
public abstract class AbstractCell implements TemplateElement {
	/**
	 * テンプレート側のセル
	 */
	protected CellWrapper cell;
	private boolean isMergedCell;
	private short relativeMergedColumnTo;
	private int relativeMergedRowNumTo;
	private Object cellValue;
	

	/**
	 * コンストラクタです。
	 * 
	 * @param cell
	 */
	AbstractCell(CellWrapper cell) {
		this.cell = cell;
		HSSFSheet templateSheet = cell.getRow().getSheet().getHSSFSheet();
		int rowNum = cell.getRow().getHSSFRow().getRowNum();
		
		//マージ情報をなめて、スタート地点が合致すれば保存しておく。
		for(int i=0; i < templateSheet.getNumMergedRegions();i++){
			Region reg = templateSheet.getMergedRegionAt(i);			
			setUpMergedCellInfo(cell.getHSSFCell().getCellNum(), rowNum, reg);
			if(isMergedCell){
				break;
			}
		}
		
		cellValue = cell.getObjectValue();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.element.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		HSSFCell out = context.getCurrentCell();
		copyCellStyle(context, out);
		
		mergeImpl(context);
		
		context.nextCell();		
	}
	
	/**
	 * このクラスを継承したクラスで実装される、データ埋め込み処理の実装です。
	 * @param context コンテキスト
	 * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられる例外です。
	 */
	protected abstract void mergeImpl(FPContext context) throws FPMergeException ;



	private void setUpMergedCellInfo(short cellNum, int rowNum, Region reg){
		if(reg.getColumnFrom() != cellNum || reg.getRowFrom() != rowNum){
			isMergedCell = false;
			return;
		}						
		isMergedCell = true;
		relativeMergedColumnTo = (short) (reg.getColumnTo() - reg.getColumnFrom());
		relativeMergedRowNumTo = reg.getRowTo() - reg.getRowFrom();
	}	
	

	/**
	 * テンプレート側のセルのスタイルを出力側へコピーします。フォントやデータフォーマットも反映されます。
	 * @param context コンテキスト
	 * @param outCell 出力するセル
	 */
	protected void copyCellStyle(FPContext context, HSSFCell outCell) {
		HSSFCell hssfCell = cell.getHSSFCell();

		HSSFWorkbook outWb = cell.getRow().getSheet().getWorkbook().getHSSFWorkbook();
		HSSFCellStyle outStyle = outWb.createCellStyle();
		HSSFCellStyle templateStyle = hssfCell.getCellStyle(); 
		copyProperties(outStyle, templateStyle);

		short fontIndex = cell.getHSSFCell().getCellStyle().getFontIndex();
		HSSFFont font = outWb.getFontAt(fontIndex);
		outStyle.setFont(font);
		
		outCell.setCellStyle(outStyle);
		if(isMergedCell){
			mergeCell(context);
		}
		
	}	

	private void copyProperties(Object dest, Object src) {
		try {
			BeanUtils.copyProperties(dest, src);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void mergeCell(FPContext context){		
		short columnFrom = context.getCurrentCellNum();
		int rowFrom = context.getCurrentRowNum();
		
		Region reg = new Region();
		reg.setColumnFrom(columnFrom);
		reg.setColumnTo((short) (columnFrom + relativeMergedColumnTo));
		reg.setRowFrom(rowFrom);
		reg.setRowTo(rowFrom + relativeMergedRowNumTo);
		HSSFSheet hssfSheet = cell.getRow().getSheet().getHSSFSheet();
		hssfSheet.addMergedRegion(reg);		
	}

	public Object getCellValue() {
		return cellValue;
	}

	public void setCellValue(Object cellValue) {
		this.cellValue = cellValue;
	}

}
