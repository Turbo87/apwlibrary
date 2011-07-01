//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

public class PDFWriter {

	private PDFDocument mDocument;
	private IndirectObject mCatalog;
	private Pages mPages;
	private Page mCurrentPage;

	public PDFWriter() {
		newDocument(PaperSize.A4_WIDTH, PaperSize.A4_HEIGHT);
	}

	public PDFWriter(int pageWidth, int pageHeight) {
		newDocument(pageWidth, pageHeight);
	}
	
	private void newDocument(int pageWidth, int pageHeight) {
		mDocument = new PDFDocument();
		mCatalog = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mCatalog);
		mPages = new Pages(mDocument, pageWidth, pageHeight);
		mDocument.includeIndirectObject(mPages.getIndirectObject());
		renderCatalog();
		newPage();
	}
	
	private void renderCatalog() {
		mCatalog.setDictionaryContent("  /Type /Catalog\n  /Pages "+mPages.getIndirectObject().getIndirectReference()+"\n");
	}
	
	public void newPage() {
		mCurrentPage = mPages.newPage();
		mDocument.includeIndirectObject(mCurrentPage.getIndirectObject());
		mPages.render();
	}
	
	public void setFont(String subType, String baseFont) {
		mCurrentPage.setFont(subType, baseFont);
	}

	public void setFont(String subType, String baseFont, String encoding) {
		mCurrentPage.setFont(subType, baseFont, encoding);
	}
	
	public void addRawContent(String rawContent) {
		mCurrentPage.addRawContent(rawContent);
	}

	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text) {
		mCurrentPage.addText(leftPosition, topPositionFromBottom, fontSize, text, StandardFonts.DEGREES_0_ROTATION);
	}
	
	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text, String transformation) {
		mCurrentPage.addText(leftPosition, topPositionFromBottom, fontSize, text, transformation);
	}
	
	public void addLine(int fromLeft, int fromBottom, int toLeft, int toBottom) {
		mCurrentPage.addLine(fromLeft, fromBottom, toLeft, toBottom);
	}
	
	public void addRectangle(int fromLeft, int fromBottom, int toLeft, int toBottom) {
		mCurrentPage.addRectangle(fromLeft, fromBottom, toLeft, toBottom);
	}
	
	public String asString() {
		mPages.render();
		return mDocument.toPDFString();
	}
	
}
