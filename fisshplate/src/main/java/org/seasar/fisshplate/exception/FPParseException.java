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

package org.seasar.fisshplate.exception;

import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * テンプレート解析時に投げられる例外クラスです。
 * @author rokugen
 *
 */
public class FPParseException extends FPException {
    private static final long serialVersionUID = 1L;

    /**
     * メッセージIDを元にリソースバンドルからメッセージを取得します。
     * @param messageId メッセージID
     */
    public FPParseException(String messageId){
        super(messageId);
    }

    /**
     * メッセージIDを元にリソースバンドルからメッセージを取得します。
     * @param messageId メッセージID
     * @param args 埋め込みパラメータ
     */
    public FPParseException(String messageId, Object[]args){
        super(messageId,args);
    }

    /**
     * メッセージIDを元にリソースバンドルからメッセージを取得します。
     * エラーが発生した行の行番号が、埋め込みパラメータの最後に追加されます。
     * @param messageId メッセージID
     * @param args 埋め込みパラメータ
     * @param row エラーが発生した行
     */
    public FPParseException(String messageId, Object[]args,RowWrapper row){
        super(messageId,args,row);
    }

    /**
     * リソースバンドルのキーを受け取って例外を生成します。
     * 引数にエラーの発生した行を指定します。
     * エラーが発生した行を指定すると、埋め込みパラメータとして行番号を追加します。
     * @param messageId リソースバンドルのキー
     * @param row エラーが発生した行
     */
    public FPParseException(String messageId,RowWrapper row) {
        super(messageId,row);
    }
}
