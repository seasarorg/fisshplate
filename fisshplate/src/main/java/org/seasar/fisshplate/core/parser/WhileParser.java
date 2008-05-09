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
package org.seasar.fisshplate.core.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.WhileBlock;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * whileを解析するクラスです。
 * @author rokugen
 */
public class WhileParser implements StatementParser {
	private static final Pattern pat = Pattern.compile("^\\s*#while\\s+(.+)");

	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
	 */
	public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
		String value = cell.getStringValue();
		Matcher mat = pat.matcher(value);
		if(!mat.find()){
			return false;
		}
		String condition = mat.group(1);
		RowWrapper row = cell.getRow();
		AbstractBlock block = new WhileBlock(row, condition);		
		parser.addBlockToParentIfExists(block);
		parser.pushBlockToStack(block);
		return true;
	}

}