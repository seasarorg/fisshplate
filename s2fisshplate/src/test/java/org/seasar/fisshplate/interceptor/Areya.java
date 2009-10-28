package org.seasar.fisshplate.interceptor;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.element.TemplateElement;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

public class Areya implements TemplateElement {
    private CellWrapper originalCell;

    public Areya(CellWrapper cell) {
        originalCell = cell;
    }

    public void merge(FPContext context) throws FPMergeException {
        HSSFCell currentCell = context.getCurrentCell();
        currentCell.setCellStyle(originalCell.getHSSFCell().getCellStyle());
        currentCell.setCellValue(new HSSFRichTextString("独自タグテストです"));
        context.nextRow();
    }

}
