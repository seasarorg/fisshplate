package org.seasar.fisshplate.exception;

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

}
