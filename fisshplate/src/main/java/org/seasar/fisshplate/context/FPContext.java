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

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.TemplateElement;

/**
 * 解析やデータ埋め込み時に参照される、グローバル値を保持するクラスです。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPContext {
	private HSSFWorkbook template;
	private HSSFWorkbook outWorkBook;
	private HSSFSheet outSheet;
	private int currentRowNum;
	private short currentCellNum;
	private Map<String, Object> data;

	private boolean isUseHeader;
	private List<TemplateElement> headerList = null;

	/**
	 * コンストラクタです。
	 * 
	 * @param template
	 *            テンプレートとなるワークブック
	 * @param out
	 *            出力するワークブック
	 * @param data
	 *            埋め込むデータ
	 */
	public FPContext(HSSFWorkbook template, HSSFWorkbook out, Map<String, Object> data) {
		this.template = template;
		this.outWorkBook = out;
		this.outSheet = outWorkBook.createSheet();
		outWorkBook.setSheetName(0, template.getSheetName(0));
		HSSFSheet templateSheet = template.getSheetAt(0);
		outSheet.setDefaultColumnWidth(templateSheet.getDefaultColumnWidth());
		outSheet.setDefaultRowHeight(templateSheet.getDefaultRowHeight());

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
	public Map<String, Object> getData() {
		return data;
	}

	/**
	 * 現在の出力対象行を戻します。
	 * 
	 * @return 出力対象行
	 */
	public HSSFRow getcurrentRow() {
		HSSFRow row = outSheet.getRow(currentRowNum);
		if (row == null) {
			row = outSheet.createRow(currentRowNum);
		}
		return row;
	}

	/**
	 * 現在の出力対象セルを戻します。
	 * 
	 * @return 出力対象セル
	 */
	public HSSFCell getCurrentCell() {
		HSSFRow row = getcurrentRow();

		HSSFCell cell = row.getCell(currentCellNum);
		if (cell == null) {
			cell = row.createCell(currentCellNum);
		}
		HSSFSheet templateSheet = template.getSheetAt(0);
		short width = templateSheet.getColumnWidth(currentCellNum);
		short defaultWidth = templateSheet.getDefaultColumnWidth();
		// テンプレート上で列幅がデフォルトのまんまだと、HSSFSheet#getDefaultColumnWidth()の値が
		// 戻って来ちゃうので、苦肉の策でデフォルトと同じだったら256倍してます。
		// TODO フォント設定前なので若干狂う。シートのデフォルトのフォントが取れればいいんだけど・・・
		if (width == defaultWidth) {
			width *= 256;
		}
		outSheet.setColumnWidth(currentCellNum, width);
		return cell;
	}

	/**
	 * 出力するワークブックを戻します。
	 * 
	 * @return 出力するワークブック
	 */
	public HSSFWorkbook getOutWorkBook() {
		return outWorkBook;
	}

	/**
	 * テンプレートとなるワークブックを戻します。
	 * 
	 * @return テンプレートワークブック
	 */
	public HSSFWorkbook getTemplate() {
		return template;
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

	/**
	 * ヘッダーの有無を返却します
	 * 
	 * @return ヘッダーの有無
	 */
	public boolean isUseHeader() {
		return isUseHeader;
	}

	/**
	 * ヘッダーの有無を設定します
	 * 
	 * @param isUseHeader
	 *            ヘッダーの有無
	 */
	public void setUseHeader(boolean isUseHeader) {
		this.isUseHeader = isUseHeader;
	}

	public void setHeaderList(List<TemplateElement> headerList) {
		this.headerList = headerList;
	}
	
	public List<TemplateElement> getHeaderList() {
		return this.headerList;
	}
}
