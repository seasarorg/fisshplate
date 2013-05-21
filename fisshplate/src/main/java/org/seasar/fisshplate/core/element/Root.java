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

package org.seasar.fisshplate.core.element;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * <p>全ての要素のルートとなる要素クラスです。</p>
 * <p>内部で、ヘッダ要素、ボディ要素のリスト、フッタ要素を保持します。</p>
 * @author rokugen
 */
public class Root implements TemplateElement {
    private TemplateElement pageHeaderBlock = new NullElement();
    private List<TemplateElement> bodyElementList = new ArrayList<TemplateElement>();
    private TemplateElement pageFooterBlock = new NullElement();

    public void merge(FPContext context) throws FPMergeException {
        context.setShouldHeaderOut(false);
        pageHeaderBlock.merge(context);
        mergeBodyElement(context);
        if(context.shouldFooterOut()){
            pageFooterBlock.merge(context);
        }
        removeUnwantedRows(context);
    }

    private void mergeBodyElement(FPContext context) throws FPMergeException{
        for(int i=0; i < bodyElementList.size(); i++){
            TemplateElement elem = (TemplateElement) bodyElementList.get(i);
            elem.merge(context);
        }
    }

    private void removeUnwantedRows(FPContext context){
        //ゴミ清掃
        Sheet outSheet = context.getOutSheet();
        int currentRowNum = context.getCurrentRowNum();
        int lastRowNum = outSheet.getLastRowNum();

        for(int i = currentRowNum; i <= lastRowNum; i++){
            outSheet.removeRow(outSheet.getRow(i));
        }
    }

    /**
     * ページヘッダの要素を戻します。
     * @return ページヘッダの要素
     */
    public TemplateElement getPageHeader(){
        return pageHeaderBlock;
    }

    /**
     * ページヘッダの要素を設定します。
     * @param pageHeader ページヘッダの要素
     */
    public void setPageHeader(TemplateElement pageHeader){
        this.pageHeaderBlock = pageHeader;
    }

    public TemplateElement getPageFooter(){
        return pageFooterBlock;
    }

    public void setPageFooter(TemplateElement pageFooter){
        this.pageFooterBlock = pageFooter;
    }

    /**
     * ボディの要素を追加します。
     * @param element ボディの要素
     */
    public void addBody(TemplateElement element){
        bodyElementList.add(element);
    }

    /**
     * ボディ要素のリストを戻します。
     * @return ボディ要素のリスト
     */
    public List<TemplateElement> getBodyElementList(){
        return bodyElementList;
    }

}
