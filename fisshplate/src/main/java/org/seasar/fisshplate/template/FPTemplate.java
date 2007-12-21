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
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.context.PageContext;
import org.seasar.fisshplate.core.FPParser;
import org.seasar.fisshplate.core.TemplateElement;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;

/**
 * FiSSH Plateで{@link HSSFWorkbook}を生成する際のエントリポイントとなるクラスです。
 * 
 * @author rokugen
 * @author a-conv
 * 
 */
public class FPTemplate {
	private HSSFWorkbook templateWb;

	private FPParser parser;

	/**
	 * テンプレートファイルを{@link InputStream}として受け取って、解析します。
	 * 
	 * @param stream
	 *            テンプレートファイルの{@link InputStream}。
	 * @throws IOException
	 *             ファイルIOでエラーが発生した際に投げられます。
	 * @throws FPParseException
	 *             テンプレートの解析時にエラーが発生した際に投げられます。
	 */
	public FPTemplate(InputStream stream) throws IOException, FPParseException {
		this(new HSSFWorkbook(stream));
	}

	/**
	 * テンプレートファイルを{@link HSSFWorkbook}として受け取って、解析します。
	 * 
	 * @param wb
	 *            テンプレート用{@link HSSFWorkbook}。
	 * @throws FPParseException
	 *             テンプレート解析時にエラーが発生した際に投げられます。
	 */
	public FPTemplate(HSSFWorkbook wb) throws FPParseException {
		templateWb = wb;
		HSSFSheet templateSheet = templateWb.getSheetAt(0);
		parser = new FPParser(templateSheet);
	}

	/**
	 * 埋め込み用データを受け取り、出力用{@link HSSFWorkbook}を生成して戻します。
	 * 
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPMergeException
	 *             データ埋め込み時にエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(Map data) throws FPMergeException {

		HSSFWorkbook out = new HSSFWorkbook();
		FPContext context = new FPContext(templateWb, out, data);

		// Header情報
		if (parser.isUseHeader()) {
			context.setUseHeader(parser.isUseHeader());
			context.setHeaderList(parser.getHeaderList());
		}

		// Footer情報
		if (parser.isUseFooter()) {
			context.setUseFooter(parser.isUseFooter());
			context.setFooterList(parser.getFooterList());
		}

		// ページコンテキスト情報の追加
		PageContext pageContext = new PageContext();
		data.put(FPConsts.PAGE_CONTEXT_NAME, pageContext);

		List elementList = parser.getRoot();
		for (int i=0; i<elementList.size(); i++){
			TemplateElement elem = (TemplateElement) elementList.get(i);		
			elem.merge(context);
		}
		return out;
	}

}
