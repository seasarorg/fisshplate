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

package org.seasar.fisshplate.consts;

/**
 * 定数を保持するインタフェースです。
 *
 * @author rokugen
 *
 */
public interface FPConsts {
    /**
     * イテレータ要素内で参照する現在行のインデックスの変数名です。「index」になります。
     */
    final static String DEFAULT_ITERATOR_INDEX_NAME = "index";

    /**
     * 出力されるシート内の行番号を表す変数名です。「rownum」になります。
     */
    final static String ROW_NUMBER_NAME = "rownum";

    /**
     * 出力されるシート内のページ番号を表す変数名です。「pagenum」になります。
     */
    final static String PAGE_NUMBER_NAME = "pagenum";

    /**
     * 出力されるページのコンテキスト名です「pageになります」。
     */
    final static String PAGE_CONTEXT_NAME = "page";

    /**
     * 埋め込みデータのNULL制御演算子
     */
    static final String NULL_VALUE_OPERATOR = "!";

    static final String MESSAGE_ID_END_ELEMENT = "EFP00001";

    static final String MESSAGE_ID_LACK_IF = "EFP00002";

    static final String MESSAGE_ID_NOT_ITERATABLE = "EFP00003";

    static final String MESSAGE_ID_BIND_VAR_UNDEFINED = "EFP00004";

    static final String MESSAGE_HEADER_INVALID = "EFP00005";

    static final String MESSAGE_FOOTER_INVALID = "EFP00006";

    static final String MESSAGE_PICTURE_TYPE_INVALID = "EFP00007";

    static final String MESSAGE_ID_NOT_ITERATOR_INVALID_MAX = "EFP00008";

    static final String MESSAGE_ID_PREVIEW_LACCK_OF_PARENT = "EFP00009";

    static final String MESSAGE_ID_VARNAME_ALREADY_EXISTS = "EFP00010";

    static final String MESSAGE_ID_VAR_DECLARATION_INVALID = "EFP00011";

    static final String MESSAGE_ID_WHILE_INVALID_CONDITION = "EFP00012";

    static final String MESSAGE_ID_LINK_MERGE_ERROR = "EFP00013";

    static final String MESSAGE_ID_PICTURE_MERGE_ERROR = "EFP00014";

    static final String MESSAGE_ID_HORIZONTAL_ITERATOR_INVALID_WIDTH = "EFP00015";

    static final String REGEX_BIND_VAR_START = "\\$\\{";

    static final String REGEX_BIND_VAR_END = "\\}";

    static final String REGEX_BIND_VAR = REGEX_BIND_VAR_START +  "[^" + REGEX_BIND_VAR_START + REGEX_BIND_VAR_END + "]" + "+" + REGEX_BIND_VAR_END;

    static final String PREVIEW_EMPTY_LIST_SIGN = "empty list";

    static final String REGEX_LINK="^\\s*\\#link-(\\S+)\\s+link\\s*=\\s*(.+)\\s+text\\s*=\\s*(.+)$";



}
