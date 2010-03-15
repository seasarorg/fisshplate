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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * @author rokugen
 */
public class Var implements TemplateElement {
    private String[] expressions;
    private RowWrapper row;
    private Pattern patDeclr = Pattern.compile("([^=\\s]+)\\s*(=\\s*[^=\\s]+)?");

    public Var(String expression, RowWrapper row){
        this.expressions = expression.split("\\s*,\\s*");
        this.row = row;
    }

    public void merge(FPContext context) throws FPMergeException {
        Map<String, Object> data = context.getData();
        for(int i=0; i < expressions.length; i++){
            String expression = expressions[i].trim();
            evalExpression(data, expression);
        }

    }

    private void evalExpression(Map<String, Object> data, String expression) throws FPMergeException{
        Matcher mat = patDeclr.matcher(expression);
        if(! mat.find()){
            throwMergeException(FPConsts.MESSAGE_ID_VAR_DECLARATION_INVALID, expression, row);
        }

        String varName = mat.group(1);
        assignVariable(data, varName);

        if(mat.group(2) != null){
            initializeVariable(data, expression);
        }
    }

    private void assignVariable(Map<String, Object> data, String varName) throws FPMergeException{
        if(data.containsKey(varName)){
            throwMergeException(FPConsts.MESSAGE_ID_VARNAME_ALREADY_EXISTS, varName, row);
        }
        data.put(varName, "");
    }

    private void initializeVariable(Map<String, Object> data, String expression) throws FPMergeException{
        try{
            OgnlUtil.getValue(expression, data);
        }catch (RuntimeException e) {
            throwMergeException(FPConsts.MESSAGE_ID_VAR_DECLARATION_INVALID, expression, row);
        }

    }

    private void throwMergeException(String messageId,String var, RowWrapper row) throws FPMergeException{
        throw new FPMergeException(messageId,	new Object[]{var},row);
    }

}
