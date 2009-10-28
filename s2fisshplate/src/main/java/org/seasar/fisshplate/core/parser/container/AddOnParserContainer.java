package org.seasar.fisshplate.core.parser.container;

import java.util.ArrayList;
import java.util.List;

import org.seasar.fisshplate.core.parser.RowParser;

/**
 * S2Fisshplateで使用する独自パーサを保持するクラスです。
 * @author rokugen
 *
 */
public class AddOnParserContainer {
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
    public RowParser getRowParser(int i){
        return (RowParser) addOnRowParsers.get(i);
    }

    /**
     * 登録された行パーサの総数を戻します。
     * @return 行パーサの総数
     */
    public int rowParserCount(){
        return addOnRowParsers.size();
    }
}
