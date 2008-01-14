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

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * @author a-conv
 */
public class Picture extends AbstractCell {
	private ElExpression expression;

	/**
	 * コンストラクタです。
	 * 
	 * @param sheet
	 *            テンプレート側のシート
	 * @param cell
	 *            テンプレート側のセル
	 * @param rowNum
	 *            行番号
	 * @param expression
	 *            評価式
	 */
	Picture(HSSFSheet sheet, HSSFCell cell, int rowNum, String expression) {
		super(sheet, cell, rowNum);
		this.expression = new ElExpression(expression);
		System.out.println(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	public void merge(FPContext context) throws FPMergeException {
		HSSFCell out = context.getCurrentCell();
		copyCellStyle(context, out);
		Object value = getValue(context);
		String picturepath = (String) value;
		writePicture(picturepath, context);
		context.nextCell();
	}

	private FileInputStream createFileInputStream(String filePath) throws FPMergeException {
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new FPMergeException("対象ファイルが見つかりません");
		}
	}

	private void close(FileInputStream fis) throws FPMergeException {
		try {
			fis.close();
		} catch (IOException e) {
			throw new FPMergeException("対象ファイルを閉じる際にエラーが発生しました。");
		}
	}

	private BufferedImage read(FileInputStream fis) throws FPMergeException {
		try {
			return ImageIO.read(fis);
		} catch (IOException e) {
			throw new FPMergeException("対象ファイルを開く際にエラーが発生しました。");
		}
	}

	private void write(BufferedImage img, String suffix, ByteArrayOutputStream baos) throws FPMergeException {
		try {
			ImageIO.write(img, suffix, baos);
		} catch (IOException e) {
			throw new FPMergeException("対象ファイルを開く際にエラーが発生しました。");
		}
	}

	private void close(ByteArrayOutputStream baos) throws FPMergeException {
		try {
			baos.close();
		} catch (IOException e) {
			throw new FPMergeException("対象ファイルを閉じる際にエラーが発生しました。");
		}
	}

	private void writePicture(String picturepath, FPContext context) throws FPMergeException {
		FileInputStream imgFis = createFileInputStream(picturepath);
		BufferedImage img = read(imgFis);
		int imgWidth = 0;
		int imgHeight = 0;
		imgWidth = img.getWidth();
		imgHeight = img.getHeight();
		close(imgFis);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		write(img, "jpg", baos);

		HSSFWorkbook workbook = context.getOutWorkBook();
		HSSFSheet worksheet = context.getOutSheet();

		HSSFPatriarch patriarch = worksheet.createDrawingPatriarch();

		HSSFClientAnchor anchor = new HSSFClientAnchor();
		anchor.setDx1(0);
		anchor.setDx2(imgWidth);
		anchor.setDy1(0);
		anchor.setDy2(imgHeight);
		anchor.setCol1(context.getCurrentCellNum());
		anchor.setCol2(context.getCurrentCellNum());
		anchor.setRow1(context.getCurrentRowNum());
		anchor.setRow2(context.getCurrentRowNum());
		byte[] pictureData = baos.toByteArray();
		int pictureIndex = workbook.addPicture(pictureData, HSSFWorkbook.PICTURE_TYPE_JPEG);
		patriarch.createPicture(anchor, pictureIndex);

		close(baos);
	}

	private Object getValue(FPContext context) throws FPMergeException {

		Map data = context.getData();

		Object value = OgnlUtil.getValue(expression.getExpression(), data);

		if (value == null) {
			if (expression.isNullAllowed()) {
				return expression.getNullValue();
			} else {
				throw new FPMergeException(FPConsts.MESSAGE_ID_EL_EXPRESSION_UNDEFINED, new Object[] { expression.getExpression() });
			}
		}
		return value;
	}

}
