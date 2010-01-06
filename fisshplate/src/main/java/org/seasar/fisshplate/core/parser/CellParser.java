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

import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * セル単位のタグから各要素を解析するインタフェースです。
 * @author rokugen
 */
public interface CellParser {
    /**
     * セルの内容を解析し、このパーサに合致した場合はそれに紐つく{@link TemplateElement}を実装したクラスを戻します。
     * 合致しない場合はnullを戻します。
     * @param cell 解析対象セル
     * @param value 解析対象セルの値
     * @return 紐ついた要素クラス
     */
    public AbstractCell getElement(CellWrapper cell, String value);

}
