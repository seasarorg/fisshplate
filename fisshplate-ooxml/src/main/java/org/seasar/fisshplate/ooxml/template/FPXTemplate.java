/**
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.fisshplate.ooxml.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.RowParser;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.template.FPTemplate;
import org.seasar.fisshplate.util.InputStreamUtil;




/**
 * FiSSH Plateで{@link Workbook}を生成する際のエントリポイントとなるクラスです(OOXML対応版)。
 *
 * @author rokugen
 *
 */public class FPXTemplate {
	
	private FPTemplate template = new FPTemplate();

    public FPXTemplate() {

    }
    
    /**
     * 独自でカスタマイズした{@link TemplateElement}を適用する{@link RowParser}を追加します。
     * @param rowParser パーサ
     */
    public void addRowParser(RowParser rowParser){
        template.addRowParser(rowParser);
    }
    
    /**
     * @param templateName
     *            テンプレートファイル名
     * @param data
     *            埋め込みデータ
     * @return 出力するデータ埋め込み済みの{@link Workbook}
     * @throws FPParseException 解析時にエラーが発生した際に投げられます。
     * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
     * @throws IOException
     *             ファイルIOでエラーが発生した際に投げられます。
     * @throws InvalidFormatException ooxml形式の場合に不正なフォーマットの際に投げられます。
     */
    public Workbook process(String templateName, Map<String, Object> data) throws FPParseException, FPMergeException,IOException, InvalidFormatException {
        InputStream is = InputStreamUtil.getResourceAsStream(templateName);
        Workbook workbook = WorkbookFactory.create(is);
        InputStreamUtil.close(is);
        return template.process(workbook, data);
    }

    /**
     * @param is
     *            テンプレートファイルの{@link InputStream}。
     * @param data
     *            埋め込み用データ
     * @return 出力するデータ埋め込み済みの{@link Workbook}
     * @throws FPParseException 解析時にエラーが発生した際に投げられます。
     * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
     * @throws IOException
     *             ファイルIOでエラーが発生した際に投げられます。
     * @throws InvalidFormatException ooxml形式の場合に不正なフォーマットの際に投げられます。
     */
    public Workbook process(InputStream is, Map<String, Object> data) throws FPParseException,FPMergeException,IOException, InvalidFormatException {
        return template.process(WorkbookFactory.create(is), data);
    }

}
