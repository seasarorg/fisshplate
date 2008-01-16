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
package org.seasar.fisshplate.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.context.PageContext;
import org.seasar.fisshplate.core.FPParser;
import org.seasar.fisshplate.core.Root;
import org.seasar.fisshplate.exception.FPException;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * FiSSH Plateで{@link HSSFWorkbook}を生成する際のエントリポイントとなるクラスです。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPTemplate {

	public FPTemplate(){
		
	}
	
	/**
	 * @param is
	 *            テンプレートファイルの{@link InputStream}。
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPException 解析時、データ埋め込み時にエラーが発生した際に投げられます。
	 * @throws IOException
	 *             ファイルIOでエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(InputStream is, Map data) throws FPException,IOException {
		return process(new HSSFWorkbook(is), data);		
	}

	/**
	 * テンプレート用ワークブックと埋め込み用データを受け取り、出力用{@link HSSFWorkbook}を生成して戻します。
	 * @param hssfWorkbook 
	 *            テンプレート用{@link HSSFWorkbook}。
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPException 解析時、データ埋め込み時にエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(HSSFWorkbook hssfWorkbook, Map data) throws FPException {
		WorkbookWrapper workbook = new WorkbookWrapper(hssfWorkbook);
		FPParser parser = new FPParser(workbook.getSheetAt(0));		
		FPContext context = new FPContext(hssfWorkbook.getSheetAt(0), data);
		// ページコンテキスト情報の追加
		PageContext pageContext = new PageContext();
		data.put(FPConsts.PAGE_CONTEXT_NAME, pageContext);

		Root root = parser.getRoot();
		root.merge(context);
		
		return hssfWorkbook;
	}

}
