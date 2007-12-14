package org.seasar.fisshplate.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.seasar.fisshplate.util.ResourceUtil;


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
		this(messageId, args, null);
	}

	/**
     * リソースバンドルのキーを受け取って、既存の例外をラップします。
     * 引数にメッセージの埋め込みパラメータと、ラップする既存の例外を指定します。
	 * @param messageId リソースバンドルのキー
	 * @param args メッセージの埋め込みパラメータ
	 * @param cause ラップする例外
	 */
	public FPException(String messageId, Object[] args, Throwable cause) {
		initCause(cause);
		this.messageId = messageId;
		if(args == null){
			this.args = null;
		}else{
			this.args = args.clone();
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
		return args.clone();
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
