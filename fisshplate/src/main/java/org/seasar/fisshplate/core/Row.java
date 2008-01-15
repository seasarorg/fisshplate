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
 * 
 * @author rokugen
 * 
 */
public class Row implements TemplateElement {
	protected List cellElementList = new ArrayList();

	private HSSFRow templateRow;

	private Root root;

	private static final Pattern patEl = Pattern.compile("^\\s*\\$\\{(.+)\\}");

	//#picture(${data.picture})
	private static final Pattern patPicture = Pattern.compile("^\\#picture\\(\\$\\{(.+)}\\s+cell=(.+)\\s*\\s+row=(.+)\\)");

	/**
	 * コンストラクタです。テンプレート側の行オブジェクトを受け取り、その行内のセル情報を解析して保持します。
	 * 
	 * @param templateSheet
	 *            テンプレート側のシート
	 * @param templateRow
	 *            テンプレート側の行オブジェクト
	 * @param root
	 *            自分自身が属してるルート要素クラス
	 */
	Row(HSSFSheet templateSheet, HSSFRow templateRow, Root root) {
		this.root = root;
		this.templateRow = templateRow;
		if (templateRow == null) {
			return;
		}
		int rowNum = templateRow.getRowNum();
		for (int i = 0; i <= templateRow.getLastCellNum(); i++) {
			HSSFCell templateCell = templateRow.getCell((short) i);
			TemplateElement element = getElement(templateSheet, templateCell, rowNum);
			cellElementList.add(element);
		}
	}

	private TemplateElement getElement(HSSFSheet templateSheet, HSSFCell templateCell, int rowNum) {
		if (templateCell == null) {
			return new NullElement();
		}

		if (templateCell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
			return new Literal(templateSheet, templateCell, rowNum);
		}

		// 画像の場合
		String pictureValue = templateCell.getRichStringCellValue().getString();
		Matcher pictureMat = patPicture.matcher(pictureValue);
		if (pictureMat.find()) {
			String picturePathExpression = pictureMat.group(1);
			String cellRange = pictureMat.group(2);
			String rowRange = pictureMat.group(3);
			return new Picture(templateSheet, templateCell, rowNum, picturePathExpression,cellRange,rowRange);
		}
		
		String value = templateCell.getRichStringCellValue().getString();
		Matcher mat = patEl.matcher(value);
		if (mat.find()) {
			String expression = mat.group(1);
			return new El(templateSheet, templateCell, rowNum, expression);
		} else {
			return new Literal(templateSheet, templateCell, rowNum);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		// ヘッダ・フッタ制御
		if (context.shouldHeaderOut()) {
			context.setShouldHeaderOut(false);
			root.getPageHeader().merge(context);
		}
		context.setShouldFooterOut(true);

		HSSFRow outRow = context.getcurrentRow();
		if (templateRow != null) {
			outRow.setHeight(templateRow.getHeight());
		}
		Map data = context.getData();
		data.put(FPConsts.ROW_NUMBER_NAME, new Integer(context.getCurrentRowNum() + 1));
		for (int i = 0; i < cellElementList.size(); i++) {
			TemplateElement elem = (TemplateElement) cellElementList.get(i);
			elem.merge(context);
		}
		context.nextRow();
	}

}
