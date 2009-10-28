package org.seasar.fisshplate.interceptor;

import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.core.parser.FPParser;
import org.seasar.fisshplate.core.parser.RowParser;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.wrapper.CellWrapper;

public class TestRowParser implements RowParser{

    public boolean process(CellWrapper cell, FPParser parser)  throws FPParseException {
        String value = cell.getStringValue();
        if (!"あれやこれや".equals(value)) {
            return false;
        }
        TemplateElement elem = new Areya(cell);
        parser.addTemplateElement(elem);
        return true;

    }

}
