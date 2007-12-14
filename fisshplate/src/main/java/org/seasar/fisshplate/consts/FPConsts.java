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
	final static String ITERATOR_INDEX_NAME = "index";

	/**
	 * 出力されるシート内の行番号を表す変数名です。「rownum」になります。
	 */
	final static String ROW_NUMBER_NAME = "rownum";

	static final String MESSAGE_ID_END_ELEMENT = "EFP00001";

	static final String MESSAGE_ID_LACK_IF = "EFP00002";

	static final String MESSAGE_ID_NOT_ITERATABLE = "EFP00003";

	static final String MESSAGE_ID_EL_EXPRESSION_UNDEFINED = "EFP00004";

}
