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
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.exception.FPParseException;

/**
 * テンプレート側のシートを解析し、要素クラスの構造を組み立てて保持します。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPParser {

	private List<TemplateElement> elementList = new ArrayList<TemplateElement>();
	private Stack<AbstractBlock> blockStack = new Stack<AbstractBlock>();

	private static final Pattern patIterator = Pattern.compile("^\\s*#foreach\\s+(\\S+)\\s*:\\s*(\\S+)(\\s+index=(\\S+))*\\s*$");
	private static final Pattern patEnd = Pattern.compile("^\\s*#end\\s*$");
	private static final Pattern patIf = Pattern.compile("^\\s*#if\\s*\\(\\s*(.+)\\s*\\)");
	private static final Pattern patElseIf = Pattern.compile("^\\s*#else\\s+if\\s*\\(\\s*(.+)\\s*\\)");
	private static final Pattern patElse = Pattern.compile("^\\s*#else\\s*$");
	private static final Pattern patComment = Pattern.compile("^\\s*#\\.*");
	private static final Pattern patPageBreak = Pattern.compile("#pageBreak");

	// Header情報
	private Stack<AbstractBlock> headerStack = new Stack<AbstractBlock>();
	private static final Pattern patPageHeaderStart = Pattern.compile("#pageHeaderStart");
	private static final Pattern patPageHeaderEnd = Pattern.compile("#pageHeaderEnd");
	private boolean isProcessHeader = false;
	private boolean isUseHeader = false;
	private List<TemplateElement> headerList = new ArrayList<TemplateElement>();

	// Footer情報
	private Stack<AbstractBlock> footerStack = new Stack<AbstractBlock>();
	private static final Pattern patPageFooterStart = Pattern.compile("#pageFooterStart");
	private static final Pattern patPageFooterEnd = Pattern.compile("#pageFooterEnd");
	private boolean isProcessFooter = false;
	private boolean isUseFooter = false;
	private List<TemplateElement> footerList = new ArrayList<TemplateElement>();

	/**
	 * ルートの要素リストを戻します。
	 * 
	 * @return 要素リスト
	 */
	public List<TemplateElement> getRoot() {
		return elementList;
	}

	/**
	 * コンストラクタです。引数で渡されたテンプレートのシートを元に解析します。
	 * 
	 * @param sheet
	 *            テンプレートのシート
	 * @throws FPParseException
	 *             テンプレートの解析時に構文上のエラーが判明した際に投げられます。
	 */
	public FPParser(HSSFSheet sheet) throws FPParseException {
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			parseRow(sheet, sheet.getRow(i));
		}
		// スタックにまだブロックが残ってたら#end不足
		if (blockStack.size() > 0) {
			throw new FPParseException(FPConsts.MESSAGE_ID_END_ELEMENT);
		}
		finalCheck();
	}

	private void parseRow(HSSFSheet sheet, HSSFRow row) throws FPParseException {
		if (isControlRow(row)) {
			return;
		}
		Row rowElem = new Row(sheet, row);
		// Header内にいる場合はHeaderブロック内の子要素とする。
		if (isProcessHeader) {
			AbstractBlock block = headerStack.lastElement();
			block.addChild(rowElem);
			return;
		}
		// Footer内にいる場合はHeaderブロック内の子要素とする。
		if (isProcessFooter) {
			AbstractBlock block = footerStack.lastElement();
			block.addChild(rowElem);
			return;
		}
		// ブロック内に居る場合は、そのブロック内の子要素とする。そうでない場合はルートに行を追加する。
		if (blockStack.size() > 0) {
			AbstractBlock block = blockStack.lastElement();
			block.addChild(rowElem);
		} else {
			elementList.add(rowElem);
		}
	}

	/**
	 * 制御行かどうかを判定しつつ、要素オブジェクト構築。うーん、どうもしっくりこない。
	 * 
	 * @param row
	 *            テンプレート行
	 * @return 制御行なら<code>true</code>。出力行なら<code>false</code>。
	 * @throws FPParseException
	 */
	private boolean isControlRow(HSSFRow row) throws FPParseException {

		if (row == null) {
			return false;
		}

		HSSFCell cell = row.getCell((short) 0);
		if (cell == null || (cell.getCellType() != HSSFCell.CELL_TYPE_STRING)) {
			return false;
		}
		String value = cell.getRichStringCellValue().getString();

		boolean isControlRow = true;

		Matcher mat;
		if ((mat = patIterator.matcher(value)).find()) {
			iteratorBlock(mat);
		} else if ((mat = patEnd.matcher(value)).find()) {
			end();
		} else if ((mat = patIf.matcher(value)).find()) {
			ifBlock(mat);
		} else if ((mat = patElseIf.matcher(value)).find()) {
			elseIfBlock(mat);
		} else if ((mat = patElse.matcher(value)).find()) {
			elseBlock();
		} else if ((mat = patPageBreak.matcher(value)).find()) {
			breakBlock();
		} else if ((mat = patPageHeaderStart.matcher(value)).find()) {
			pageHeaderBlock();
		} else if ((mat = patPageHeaderEnd.matcher(value)).find()) {
			pageHeaderEnd();
		} else if ((mat = patPageFooterStart.matcher(value)).find()) {
			pageFooterBlock();
		} else if ((mat = patPageFooterEnd.matcher(value)).find()) {
			pageFooterEnd();
		} else if ((mat = patComment.matcher(value)).find()) {
			// コメント行はパス
		} else {
			isControlRow = false;
		}

		return isControlRow;
	}

	private void finalCheck() {
		// Headerを設定している場合
		if (isUseHeader) {
			TemplateElement headerElem = headerList.get(0);
			elementList.add(0, headerElem);
		}

		// Footerを設定している場合
		if (isUseFooter) {
			TemplateElement footerElem = footerList.get(0);
			elementList.add(footerElem);
		}
	}

	private void pageFooterEnd() {
		AbstractBlock block = footerStack.pop();
		footerList.add(block);
		isProcessFooter = false;
	}

	private void pageFooterBlock() {
		AbstractBlock block = new PageHeaderBlock();
		footerStack.push(block);
		isProcessFooter = true;
		isUseFooter = true;
	}

	private void pageHeaderEnd() {
		AbstractBlock block = headerStack.pop();
		headerList.add(block);
		isProcessHeader = false;
	}

	private void pageHeaderBlock() {
		AbstractBlock block = new PageHeaderBlock();
		headerStack.push(block);
		isProcessHeader = true;
		isUseHeader = true;
	}

	private void breakBlock() {
		AbstractBlock block = new PageBreakBlock();
		if (blockStack.size() > 0) {
			AbstractBlock parentBlock = blockStack.lastElement();
			parentBlock.addChild(block);
			return;
		}
		elementList.add(block);
	}

	private void iteratorBlock(Matcher mat) {
		String varName = mat.group(1);
		String iteratorName = mat.group(2);
		String indexName = mat.group(4);
		AbstractBlock block = new IteratorBlock(varName, iteratorName, indexName);
		pushBlockToStack(block);
	}

	private void ifBlock(Matcher mat) {
		String condition = mat.group(1);
		AbstractBlock block = new IfBlock(condition);
		pushBlockToStack(block);
	}

	private void elseIfBlock(Matcher mat) throws FPParseException {
		AbstractBlock parent = getParentIfBlock();
		String condition = mat.group(1);
		AbstractBlock block = new ElseIfBlock(condition);
		((IfBlock) parent).setNextBlock(block);
		blockStack.push(block);
	}

	private void elseBlock() throws FPParseException {
		AbstractBlock parent = getParentIfBlock();
		AbstractBlock block = new ElseBlock();
		((IfBlock) parent).setNextBlock(block);
		blockStack.push(block);
	}

	private AbstractBlock getParentIfBlock() throws FPParseException {
		if (blockStack.size() < 1) {
			throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF);
		}

		AbstractBlock parent = blockStack.lastElement();

		if (!(parent instanceof IfBlock)) {
			throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF);
		}
		return parent;

	}

	private void end() throws FPParseException {
		if (blockStack.size() < 1) {
			throw new FPParseException(FPConsts.MESSAGE_ID_END_ELEMENT);
		}
		AbstractBlock block = blockStack.pop();
		// elseとelse ifの場合、ifが出るまでpop継続する
		Class clazz = block.getClass();
		if ((clazz == ElseBlock.class) || clazz == ElseIfBlock.class) {
			while (block.getClass() != IfBlock.class) {
				block = blockStack.pop();
			}
		}
		// ブロックのネストがルートまで戻ったらルートの要素リストに追加する。
		if (blockStack.size() < 1) {
			elementList.add(block);
		}
	}

	private void pushBlockToStack(AbstractBlock block) {
		if (blockStack.size() > 0) {
			AbstractBlock parentBlock = blockStack.lastElement();
			parentBlock.addChild(block);
		}
		blockStack.push(block);
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
	 * ヘッダーの内容を返却します
	 * 
	 * @return headerList
	 */
	public List<TemplateElement> getHeaderList() {
		return headerList;
	}

	/**
	 * フッターの有無を返却します
	 * 
	 * @return フッターの有無
	 */
	public boolean isUseFooter() {
		return isUseFooter;
	}

	/**
	 * フッターの内容を返却します
	 * 
	 * @return footerList
	 */
	public List<TemplateElement> getFooterList() {
		return footerList;
	}

}
