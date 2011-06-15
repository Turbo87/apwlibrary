package crl.android.pdfwriter;

public class PDFWriter {

	private PDFDocument mDocument;
	private IndirectObject mCatalog;
	private Pages mPages;
	private Page mCurrentPage;

	public PDFWriter() {
		newDocument(800, 600);
	}

	public PDFWriter(int pageHeight, int pageWidth) {
		newDocument(pageHeight, pageWidth);
	}

	private void newDocument(int pageHeight, int pageWidth) {
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
		
	public void setPageFont(String subType, String baseFont) {
		mCurrentPage.setPageFont(subType, baseFont);
	}

	public void setPageFont(String subType, String baseFont, String encoding) {
		mCurrentPage.setPageFont(subType, baseFont, encoding);
	}
	
	public void addRawContent(String rawContent) {
		mCurrentPage.addRawContent(rawContent);
	}

	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text) {
		mCurrentPage.addText(leftPosition, topPositionFromBottom, fontSize, text, crl.android.pdfwriter.StandardFonts.DEGREES_0_ROTATION);
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
		return mDocument.toPDFString();
	}
	
}
