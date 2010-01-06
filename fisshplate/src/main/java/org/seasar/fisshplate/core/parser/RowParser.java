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
 *
 */

package org.seasar.fisshplate.core.parser;

import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * 行単位のタグから各要素を解析するインタフェースです。
 * @author rokugen
 */
public interface RowParser {
    /**
     * セルの内容がこのパーサーと合致するか否かを戻します。
     * 合致する場合、{@link TemplateElement}を生成し、呼び出し元の{@link FPParser}へ処理を委譲します。
     * @param cell セル
     * @param parser 呼び出し元FPParser
     * @return 合致するか否か
     * @throws FPParseException 解析時にエラーが発生した際に投げられます。
     */
    boolean process(CellWrapper cell, FPParser parser) throws FPParseException ;
}
