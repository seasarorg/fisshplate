package org.seasar.fisshplate.core.parser.container;

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.core.parser.RowParser;

/**
 * S2Fisshplateで使用する独自の行パーサを保持するクラスです。<p>
 * <b>s2fisshplate-parsers.dicon</b>にコンポーネント登録し、initMethodで独自パーサを登録します。<p>
 * @author rokugen
 *
 */
public class AddOnRowParserContainer {
    private List addOnRowParsers = new ArrayList();

    /**
     * 独自の行パーサを登録します。
     * @param parser 独自の行パーサ
     */
    public void addRowParser(RowParser parser){
        addOnRowParsers.add(parser);
    }

    /**
     * 指定されたインデックスに該当する、登録された行パーサを戻します。
     * @param i パーサのインデックス
     * @return インデックスに該当するパーサ
     */
    public RowParser get(int i){
        return (RowParser) addOnRowParsers.get(i);
    }

    /**
     * 登録されたパーサの総数を戻します。
     * @return パーサの総数
     */
    public int size(){
        return addOnRowParsers.size();
    }
}
