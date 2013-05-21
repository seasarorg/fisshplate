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

import java.util.Stack;

import org.apache.poi.ss.usermodel.Cell;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;

/**
 * テンプレートのセルの値にバインド変数が含まれる場合の要素クラスです。OGNLで評価します。
 * 評価は即時にはされず、Resumeクラスによって評価されます。
 * @author rokugen
 */
public class Suspend implements TemplateElement {
    private El el;
    private Stack<Cell> targetCellStack = new Stack<Cell>();

    public Suspend(El el) {
        this.el = el;
    }


    public void merge(FPContext context) throws FPMergeException {
        context.addSuspendedSet(this);
        Cell out =  context.getCurrentCell();
        targetCellStack.push(out);
        el.targetElement.copyCellStyle(context,out);
        context.nextCell();
    }

    public void resume(FPContext context) throws FPMergeException{
        Object value = el.getBoundValue(context);
        el.targetElement.setCellValue(value);
        while(!targetCellStack.empty()){
            Cell out = targetCellStack.pop();
            el.targetElement.mergeImpl(context, out);
        }
    }

    public El getEl(){
        return el;
    }

}
