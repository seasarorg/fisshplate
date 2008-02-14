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

import org.seasar.fisshplate.consts.FPConsts;
import org.seasar.fisshplate.core.element.AbstractBlock;
import org.seasar.fisshplate.core.element.ElseIfBlock;
import org.seasar.fisshplate.core.element.IfBlock;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;
import org.seasar.fisshplate.wrapper.RowWrapper;

/**
 * else if を解析するクラスです。
 * @author rokugen
 */
public class ElseIfBlockParser implements StatementParser{
	private static final Pattern patElseIf = Pattern.compile("^\\s*#else\\s+if\\s*(.+)");
	/* (non-Javadoc)
	 * @see org.seasar.fisshplate.core.parser.StatementParser#process(org.seasar.fisshplate.wrapper.CellWrapper, org.seasar.fisshplate.core.parser.FPParser)
	 */
	public boolean process(CellWrapper cell, FPParser parser)	throws FPParseException {
		String value = cell.getStringValue();
		Matcher mat = patElseIf.matcher(value);
		if(!mat.find()){
			return false;
		}
		RowWrapper row = cell.getRow();
		AbstractBlock parent = getParentIfBlock(row, parser);
		String condition = mat.group(1);
		AbstractBlock block = new ElseIfBlock(condition);
		((IfBlock) parent).setNextBlock(block);
		parser.pushBlockToStack(block);
		return true;
	}
	//TODO elseにも同一処理あり。なんとかするべし。
	private AbstractBlock getParentIfBlock(RowWrapper row,FPParser parser) throws FPParseException {
		if (parser.isBlockStackBlank()) {
			throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF,
					new Object[]{new Integer(row.getHSSFRow().getRowNum() + 1)});
		}

		AbstractBlock parent = parser.getLastElementFromStack();

		if (!(parent instanceof IfBlock)) {
			throw new FPParseException(FPConsts.MESSAGE_ID_LACK_IF,
					new Object[]{new Integer(row.getHSSFRow().getRowNum() + 1)});
		}
		return parent;

	}

}
