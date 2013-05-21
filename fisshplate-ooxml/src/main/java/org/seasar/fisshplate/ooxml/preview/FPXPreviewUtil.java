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
package org.seasar.fisshplate.ooxml.preview;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.seasar.fisshplate.exception.FPException;
import org.seasar.fisshplate.preview.FPPreviewUtil;

/**
 * テンプレートファイルと埋め込みデータファイルから出力ファイルを生成するユーティリティクラスです。(OOXML対応版)
 * @author rokugen
 */
public class FPXPreviewUtil {
	private FPXPreviewUtil(){}

    /**
     * テンプレートファイルのストリームと、データ用ファイルのストリームから出力ファイルを生成して戻します。
     *
     * @param template テンプレート用ストリーム
     * @param data データ用ストリーム
     * @return データを埋め込んだワークブック
     * @throws FPException
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static final Workbook getWorkbook(InputStream template, InputStream data) throws FPException, IOException, InvalidFormatException{
        Workbook tempWb = WorkbookFactory.create(template);
        Workbook dataWb = WorkbookFactory.create(data);
        return FPPreviewUtil.getWorkbook(tempWb, dataWb);
    }
	
}
