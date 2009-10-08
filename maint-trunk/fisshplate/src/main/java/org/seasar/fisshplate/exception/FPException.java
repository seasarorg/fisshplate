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
package org.seasar.fisshplate.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.seasar.fisshplate.util.ResourceUtil;
import org.seasar.fisshplate.wrapper.RowWrapper;


/**
 * 例外の基底クラスです。
 * @author rokugen
 *
 */
public class FPException extends Exception {

    private static final long serialVersionUID = 4301708318614085971L;

    private static final Object[] EMPTY_ARGS = new Object[0];
	
	private String messageId;
	
	private Object[] args;
    
    private String message;    
	
	/**
     * リソースバンドルのキーを受け取って例外を生成します。
	 * @param messageId リソースバンドルのキー
	 */
	public FPException(String messageId) {
		this(messageId, EMPTY_ARGS);
	}

	/**
     * リソースバンドルのキーを受け取って例外を生成します。引数にメッセージの埋め込みパラメータを指定します。
	 * @param messageId リソースバンドルのキー
	 * @param args メッセージへの埋め込みパラメータ
	 */
	public FPException(String messageId, Object[] args) {
		this(messageId, args, null, null);
	}
	
	/**
     * リソースバンドルのキーを受け取って例外を生成します。
     * 引数にエラーの発生した行を指定します。
     * エラーが発生した行を指定すると、埋め込みパラメータとして行番号を追加します。
	 * @param messageId リソースバンドルのキー
	 * @param row エラーが発生した行
	 */
	public FPException(String messageId, RowWrapper row){
		this(messageId, null, row, null);
	}
	
	/**
     * リソースバンドルのキーを受け取って、既存の例外をラップします。
     * 引数にメッセージの埋め込みパラメータと、エラーの発生した行を指定します。
     * エラーが発生した行を指定すると、埋め込みパラメータの最後に行番号を追加します。
	 * @param messageId リソースバンドルのキー
	 * @param args メッセージの埋め込みパラメータ
	 * @param row エラーが発生した行
	 */
	public FPException(String messageId, Object[] args, RowWrapper row){
		this(messageId, args, row, null);
	}
	
	/**
     * リソースバンドルのキーを受け取って、既存の例外をラップします。
     * 引数にメッセージの埋め込みパラメータと、ラップする既存の例外を指定します。
	 * @param messageId リソースバンドルのキー
	 * @param args メッセージの埋め込みパラメータ
	 * @param cause ラップする例外
	 */
	public FPException(String messageId, Object[] args, Throwable cause) {
		this(messageId,args,null,cause);		
	}


	/**
     * リソースバンドルのキーを受け取って、既存の例外をラップします。
     * 引数にメッセージの埋め込みパラメータと、ラップする既存の例外を指定します。
     * エラーが発生した行を指定すると、埋め込みパラメータの最後に行番号を追加します。
	 * @param messageId リソースバンドルのキー
	 * @param args メッセージの埋め込みパラメータ
	 * @param row エラーが発生した行
	 * @param cause ラップする例外
	 */
	public FPException(String messageId, Object[] args, RowWrapper row, Throwable cause) {
		initCause(cause);
		
		
		this.messageId = messageId;
		this.args = getParam(args, row);		
        
        ResourceBundle bundle = ResourceUtil.getAppExceptionBundle();
        String pattern = bundle.getString(messageId);
        this.message = MessageFormat.format(pattern, this.args);
	}
	
	private Object[] getParam(Object[] args, RowWrapper row){
		if(row == null || row.isNullRow()){
			return args;
		}		
		return getParamIncludingRowNum(args, row);
	}

	private Object[] getParamIncludingRowNum(Object[] args, RowWrapper row) {
		int rowNum = row.getHSSFRow().getRowNum() + 1;
		int paramLength = (args ==null)? 1:args.length + 1;
		Object[] params = new Object[paramLength];		
		for(int i=0; i < paramLength - 1; i++){			
			params[i] = args[i];
		}
		params[paramLength - 1] = row.getSheet().getSheetName() + " : " + rowNum;
		return params;
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
