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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ognl.NoSuchPropertyException;
import ognl.OgnlException;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.BindVariable;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.OgnlUtil;

/**
 * テンプレートのセルの値にバインド変数が含まれる場合の要素クラスです。OGNLで評価します。
 * @author rokugen
 *
 */
public class El implements TemplateElement{
    private Map<String, Object> expressionMap = new HashMap<String, Object>();
    protected AbstractCell targetElement;
    private String originalCellValue;

    /**
     * コンストラクタです。
     * @param target バインド変数を評価する対象となるセル要素
     */
    public El(AbstractCell target) {
        this.targetElement = target;
        originalCellValue = (String) target.getCellValue();
        Pattern patEl = Pattern.compile(FPConsts.REGEX_BIND_VAR);
        Matcher mat = patEl.matcher(originalCellValue);
        while(mat.find()){
            expressionMap.put(mat.group(), null);
        }
    }

    /* (non-Javadoc)
     * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
     */
    public void merge(FPContext context) throws FPMergeException {
        Object value = getBoundValue(context);
        targetElement.setCellValue(value);
        targetElement.merge(context);
    }

    protected Object getBoundValue(FPContext context) throws FPMergeException{
        Object value = null;
        if(context.isSkipMerge()){
            value = "";
        }else{
            putValueToMap(context);
            value = buildValue();
        }
        return value;
    }

    private void putValueToMap(FPContext context) throws FPMergeException{
        Map<String, Object> data = context.getData();
        Set<String> key = expressionMap.keySet();
        for(Iterator<String> itr = key.iterator(); itr.hasNext();){
            String expString = (String) itr.next();
            BindVariable bindVar = new BindVariable(expString);
            Object value = getValue(data,bindVar);
            value = convertLineFeed(value);
            expressionMap.put(expString, value);
        }
    }

    protected Object convertLineFeed(Object value){
        if (value instanceof String){
            value = ((String)value).replaceAll("(\r\n|\r)", "\n");

        }
        return value;
    }

    private Object buildValue(){

        if(onlySingleBindVarIn(originalCellValue)){
            Set<String> keySet = expressionMap.keySet();
            return expressionMap.get(keySet.iterator().next());
        }else{
            return replaceAllBindVariable(originalCellValue);
        }
    }

    private String replaceAllBindVariable(String cellValue){
        Set<String> keySet = expressionMap.keySet();
        for(Iterator<String> itr = keySet.iterator(); itr.hasNext();){
            String key = (String) itr.next();
            cellValue = cellValue.replaceAll(Pattern.quote(key), expressionMap.get(key).toString());
        }
        return cellValue;
    }

    private boolean onlySingleBindVarIn(String value){
        if(expressionMap.size() != 1){
            return false;
        }

        String exp = expressionMap.keySet().iterator().next().toString();
        value = value.replaceAll(Pattern.quote(exp), "");

        return (value.trim().length() == 0);
    }



    private Object getValue(Map<String, Object> data, BindVariable bindVar) throws FPMergeException{
        Object value = null;
        try{
            value = OgnlUtil.getValue(bindVar.getName(), data);
        }catch(RuntimeException e){
            handleGetValueRuntimeException(e);
        }

        return (value == null)?getNullValue(bindVar):value;
    }

    private void handleGetValueRuntimeException(RuntimeException e){
        if( (e.getCause() instanceof NoSuchPropertyException)){
            return;
        }

        if( e.getCause() instanceof OgnlException){
            return;
        }

        throw e;
    }

    private Object getNullValue(BindVariable bindVar) throws FPMergeException{
        if(bindVar.isNullAllowed()){
            return bindVar.getNullValue();
        }else{
            throw new FPMergeException(FPConsts.MESSAGE_ID_BIND_VAR_UNDEFINED,
                    new Object[]{bindVar.getName()},targetElement.cell.getRow());
        }
    }

    public String getOriginalCellValue() {
        return originalCellValue;
    }




}

