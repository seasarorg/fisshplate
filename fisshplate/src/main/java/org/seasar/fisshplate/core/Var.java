/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 */
package org.seasar.fisshplate.core;

import java.util.Map;

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * @author rokugen
 */
public class Var implements TemplateElement {
	private String[] vars;
	private RowWrapper row;
	
	Var(String varName, RowWrapper row){
		this.vars = varName.split("\\s*,\\s*");
		this.row = row;
	}

	public void merge(FPContext context) throws FPMergeException {
		Map data = context.getData();		
		for(int i=0; i < vars.length; i++){
			String var = vars[i].trim();			
			if(data.containsKey(var)){
				throw new FPMergeException(FPConsts.MESSAGE_ID_VARNAME_ALREADY_EXISTS,
						new Object[]{var,new Integer(row.getHSSFRow().getRowNum() + 1)});
			}
			data.put(var, "");
		}

	}

}
