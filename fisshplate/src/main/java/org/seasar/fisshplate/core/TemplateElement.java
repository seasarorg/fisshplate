package org.seasar.fisshplate.core;


import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * テンプレートの各要素を反映するインタフェースです。
 * @author rokugen
 *
 */
public interface TemplateElement {
	/**
	 * コンテキストに格納されたデータをテンプレートに埋め込みます。
	 * @param context コンテキスト
	 * @throws FPMergeException データ埋め込み時にエラーが発生した際に投げられます。
	 */
	void merge(FPContext context) throws FPMergeException;
}
