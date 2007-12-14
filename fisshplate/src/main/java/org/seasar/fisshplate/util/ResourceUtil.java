package org.seasar.fisshplate.util;

import java.util.ResourceBundle;



/**
 * アプリケーションで利用するリソースバンドルを管理するユーティリティクラスです。以下のリソースバンドルを管理します。
 * <dl>
 *  <dt>appexception.properties
 *  <dd>アプリケーション固有の例外に使うメッセージ
 * </dl>
 * @author kei
 *
 */
public class ResourceUtil {
	public static final String MESSAGE_RESOURCE_NAME = "fisshplateMessages";
	
	/**
     * アプリケーション固有の例外用リソースバンドルを戻します。
	 * @return リソースバンドル
	 */
	public static final ResourceBundle getAppExceptionBundle(){
		return ResourceBundle.getBundle(MESSAGE_RESOURCE_NAME);
	}

}
