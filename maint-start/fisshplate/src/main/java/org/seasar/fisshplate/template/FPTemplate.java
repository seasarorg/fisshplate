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
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.context.PageContext;
import org.seasar.fisshplate.core.element.Root;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.FPParser;
import org.seasar.fisshplate.core.parser.RowParser;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.util.InputStreamUtil;
import org.seasar.fisshplate.wrapper.SheetWrapper;
import org.seasar.fisshplate.wrapper.WorkbookWrapper;

/**
 * FiSSH Plateで{@link HSSFWorkbook}を生成する際のエントリポイントとなるクラスです。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPTemplate {
	private FPParser parser = new FPParser();

	public FPTemplate() {

	}
	
	/**
	 * 独自でカスタマイズした{@link TemplateElement}を適用する{@link RowParser}を追加します。
	 * @param rowParser パーサ
	 */
	public void addRowParser(RowParser rowParser){
		parser.addRowParser(rowParser);
	}

	/**
	 * @param templateName
	 *            テンプレートファイル名
	 * @param data
	 *            埋め込みデータ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPParseException 解析時にエラーが発生した際に投げられます。
	 * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
	 * @throws IOException
	 *             ファイルIOでエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(String templateName, Map data) throws FPParseException, FPMergeException,IOException {
		InputStream is = InputStreamUtil.getResourceAsStream(templateName);
		HSSFWorkbook workbook = new HSSFWorkbook(new POIFSFileSystem(is));
		InputStreamUtil.close(is);
		return process(workbook, data);
	}

	/**
	 * @param is
	 *            テンプレートファイルの{@link InputStream}。
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPParseException 解析時にエラーが発生した際に投げられます。
	 * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
	 * @throws IOException
	 *             ファイルIOでエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(InputStream is, Map data) throws FPParseException,FPMergeException,IOException {
		return process(new HSSFWorkbook(new POIFSFileSystem(is)), data);		
	}

	/**
	 * テンプレート用ワークブックと埋め込み用データを受け取り、出力用{@link HSSFWorkbook}を生成して戻します。
	 * 
	 * @param hssfWorkbook
	 *            テンプレート用{@link HSSFWorkbook}。
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPParseException 解析時にエラーが発生した際に投げられます。
	 * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(HSSFWorkbook hssfWorkbook, Map data) throws FPParseException,FPMergeException {
		WorkbookWrapper workbook = new WorkbookWrapper(hssfWorkbook);
		for(int i=0; i < workbook.getSheetCount(); i++){
    		SheetWrapper sheet = workbook.getSheetAt(i);
    		if(sheet.getRowCount() < 1){
    		    continue;
    		}
    		Root root = parser.parse(sheet);
    		
    		sheet.prepareForMerge();
    		if(data ==null){
    			data = new HashMap();
    		}
    		FPContext context = new FPContext(sheet.getHSSFSheet(), data);
    		// ページコンテキスト情報の追加
    		PageContext pageContext = new PageContext();
    		data.put(FPConsts.PAGE_CONTEXT_NAME, pageContext);
    
    		root.merge(context);
		}
		
		return hssfWorkbook;
	}
		
	

}
