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
package org.seasar.fisshplate.context;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * 解析やデータ埋め込み時に参照される、グローバル値を保持するクラスです。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPContext {	
	private HSSFSheet outSheet;
	private int currentRowNum;
	private short currentCellNum;
	private Map data;	
	private boolean shouldHeaderOut;
	private boolean shouldFooterOut;
	private boolean skipMerge = false;
	private HSSFPatriarch patriarch;

	/**
	 * コンストラクタです。
	 * @param out
	 *            出力するシート
	 * @param data
	 *            埋め込むデータ
	 */
	public FPContext(HSSFSheet out, Map data) {		 
		this.outSheet = out;
		this.data = data;
		init();
	}

	/**
	 * 現在の出力対象位置を初期化します。
	 */
	public void init() {
		currentCellNum = 0;
		currentRowNum = 0;
	}

	/**
	 * 出力対象行を次の行に進めます。
	 */
	public void nextRow() {
		this.currentCellNum = 0;
		this.currentRowNum++;
	}

	/**
	 * 出力対象セルを次のセルに進めます。
	 */
	public void nextCell() {
		this.currentCellNum++;
	}

	/**
	 * 埋め込むデータを戻します。
	 * 
	 * @return 埋め込みデータ
	 */
	public Map getData() {
		return data;
	}

	/**
	 * 現在の出力対象行を戻します。
	 * まだ無ければ生成します。
	 * 
	 * @return 出力対象行
	 */
	public HSSFRow getCurrentRow() {
		HSSFRow row = outSheet.getRow(currentRowNum);
		if (row == null) {
			row = outSheet.createRow(currentRowNum);
		}				
		return row;
	}
	
	/**
	 * 現在の出力対象行を新たに生成します。
	 * @return
	 */
	public HSSFRow createCurrentRow(){
		return outSheet.createRow(currentRowNum);
	}

	/**
	 * 現在の出力対象セルを戻します。
	 * 
	 * @return 出力対象セル
	 */
	public HSSFCell getCurrentCell() {
		HSSFRow row = getCurrentRow();

		HSSFCell cell = row.getCell(currentCellNum);
		if (cell == null) {
			cell = row.createCell(currentCellNum);
		}
		return cell;
	}

	/**
	 * 現在の行の位置を戻します。
	 * 
	 * @return 行の位置（0スタート）
	 */
	public int getCurrentRowNum() {
		return currentRowNum;
	}

	/**
	 * 現在のセルの位置を戻します。
	 * 
	 * @return セルの位置（0スタート）
	 */
	public short getCurrentCellNum() {
		return currentCellNum;
	}

	/**
	 * 出力するシートを戻します。
	 * 
	 * @return シート
	 */
	public HSSFSheet getOutSheet() {
		return outSheet;
	}
	
	
	public boolean shouldHeaderOut(){
		return shouldHeaderOut;
	}
	
	public void setShouldHeaderOut(boolean should){
		this.shouldHeaderOut = should;
	}
	
	public boolean shouldFooterOut(){
		return shouldFooterOut;
	}
	
	public void setShouldFooterOut(boolean should){
		this.shouldFooterOut = should;
	}

	public boolean isSkipMerge() {
		return skipMerge;
	}

	public void setSkipMerge(boolean skipMerge) {
		this.skipMerge = skipMerge;
	}
	
	public HSSFPatriarch getPartriarch(){
		if(patriarch == null){
			patriarch = outSheet.createDrawingPatriarch();
		}
		return patriarch;
	}


}
