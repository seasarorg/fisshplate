package org.seasar.fisshplate.template;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.FPParser;
import org.seasar.fisshplate.core.TemplateElement;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;

/**
 * FiSSH Plateで{@link HSSFWorkbook}を生成する際のエントリポイントとなるクラスです。
 * 
 * @author rokugen
 * 
 */
public class FPTemplate {
	private HSSFWorkbook templateWb;

	private FPParser parser;

	/**
	 * テンプレートファイルを{@link InputStream}として受け取って、解析します。
	 * 
	 * @param stream
	 *            テンプレートファイルの{@link InputStream}。
	 * @throws IOException
	 *             ファイルIOでエラーが発生した際に投げられます。
	 * @throws FPParseException
	 *             テンプレートの解析時にエラーが発生した際に投げられます。
	 */
	public FPTemplate(InputStream stream) throws IOException, FPParseException {
		this(new HSSFWorkbook(stream));
	}

	/**
	 * テンプレートファイルを{@link HSSFWorkbook}として受け取って、解析します。
	 * 
	 * @param wb
	 *            テンプレート用{@link HSSFWorkbook}。
	 * @throws FPParseException
	 *             テンプレート解析時にエラーが発生した際に投げられます。
	 */
	public FPTemplate(HSSFWorkbook wb) throws FPParseException {
		templateWb = wb;
		HSSFSheet templateSheet = templateWb.getSheetAt(0);
		parser = new FPParser(templateSheet);
	}

	/**
	 * 埋め込み用データを受け取り、出力用{@link HSSFWorkbook}を生成して戻します。
	 * 
	 * @param data
	 *            埋め込み用データ
	 * @return 出力するデータ埋め込み済みの{@link HSSFWorkbook}
	 * @throws FPMergeException
	 *             データ埋め込み時にエラーが発生した際に投げられます。
	 */
	public HSSFWorkbook process(Map<String, Object> data)
			throws FPMergeException {

		HSSFWorkbook out = new HSSFWorkbook();
		FPContext context = new FPContext(templateWb, out, data);
		List<TemplateElement> elementList = parser.getRoot();

		for (TemplateElement elem : elementList) {
			elem.merge(context);
		}
		return out;

	}

}
