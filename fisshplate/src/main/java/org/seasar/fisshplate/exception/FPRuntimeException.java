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

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.seasar.fisshplate.util.ResourceUtil;


/**
 * 実行時例外の基底クラスです。
 * @author rokugen
 *
 */
public class FPRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -2162742877459981377L;

    private static final Object[] EMPTY_ARGS = new Object[0];

    private String messageId;

    private Object[] args;

    private String message;

    /**
     * リソースバンドルのキーを受け取って例外を生成します。
     * @param messageId リソースバンドルのキー
     */
    public FPRuntimeException(String messageId) {
        this(messageId, EMPTY_ARGS);
    }

    /**
     * リソースバンドルのキーを受け取って例外を生成します。引数にメッセージの埋め込みパラメータを指定します。
     * @param messageId リソースバンドルのキー
     * @param args メッセージへの埋め込みパラメータ
     */
    public FPRuntimeException(String messageId, Object[] args) {
        this(messageId, args, null);
    }

    /**
     * リソースバンドルのキーを受け取って、既存の例外をラップします。
     * 引数にメッセージの埋め込みパラメータと、ラップする既存の例外を指定します。
     * @param messageId リソースバンドルのキー
     * @param args メッセージの埋め込みパラメータ
     * @param cause ラップする例外
     */
    public FPRuntimeException(String messageId, Object[] args, Throwable cause) {
        initCause(cause);
        this.messageId = messageId;
        if(args == null){
            this.args = null;
        }else{
            this.args = (Object[]) args.clone();
        }

        ResourceBundle bundle = ResourceUtil.getAppExceptionBundle();
        String pattern = bundle.getString(messageId);
        this.message = MessageFormat.format(pattern, args);
    }

    /**
     * 埋め込みパラメータを戻します。
     * @return 埋め込みパラメータ
     */
    public Object[] getArgs() {
        if(args == null){
            return null;
        }
        return (Object[]) args.clone();
    }

    /**
     * リソースバンドルのキーを戻します。
     * @return リソースバンドルのキー
     */
    public String getMessageId() {
        return messageId;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return message;
    }
}
