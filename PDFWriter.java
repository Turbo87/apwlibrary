package crl.android.pdfwriter;

public class PDFWriter {

	private PDFDocument mDocument;
	private IndirectObject mCatalog;
	private IndirectObject mPages;
	private IndirectObject mPage;
	private IndirectObject mPageFont;
	private IndirectObject mPageContents;
	private int mPageHeight = 800;
	private int mPageWidth = 600;

	public PDFWriter() {
		mDocument = new PDFDocument();
		mCatalog = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mCatalog);
		mPages = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPages);
		mPage = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPage);
		mPageFont = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPageFont);
		mPageContents = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPageContents);
		renderCatalog();
		renderPagesObject();
		renderPage();
	}
	
	private void renderCatalog() {
		mCatalog.setDictionaryContent("  /Type /Catalog\n  /Pages "+mPages.getIndirectReference()+"\n");
	}
	
	private void renderPagesObject() {
		Array lMediaBox = new Array();
		String content[] = {"0", "0", Integer.toString(mPageHeight), Integer.toString(mPageWidth)};
		lMediaBox.addItemsFromStringArray(content);
		Array lKids = new Array();
		lKids.addItem(mPage.getIndirectReference());
		mPages.setDictionaryContent("  /Type /Pages\n  /MediaBox "+lMediaBox.toPDFString()+"\n  /Count 1\n  /Kids "+lKids.toPDFString()+"\n");
	}
	
	private void renderPage() {
		mPage.setDictionaryContent("  /Type /Page\n  /Parent "+mPages.getIndirectReference()+
				"\n  /Resources <<\n    /Font <<\n      /F1 "+mPageFont.getIndirectReference()+"\n    >>\n  >>\n  /Contents "+mPageContents.getIndirectReference()+"\n");
	}

	public void setPageHeight(int Value) {
		mPageHeight = Value;
		renderPagesObject();
	}

	public void setPageWidth(int Value) {
		mPageWidth = Value;
		renderPagesObject();
	}
	
	public void setPageFont(String subType, String baseFont) {
		mPageFont.setDictionaryContent("  /Type /Font\n  /Subtype /"+subType+"\n  /BaseFont /"+baseFont+"\n");
	}

	public void setPageFont(String subType, String baseFont, String encoding) {
		mPageFont.setDictionaryContent("  /Type /Font\n  /Subtype /"+subType+"\n  /BaseFont /"+baseFont+"\n  /Encoding /"+encoding+"\n");
	}
	
	private void addContent(String content) {
		mPageContents.addStreamContent(content);
		String streamContent = mPageContents.getStreamContent();
		mPageContents.setDictionaryContent("  /Length "+Integer.toString(streamContent.length())+"\n");
		mPageContents.setStreamContent(streamContent);
	}
	
	public void addRawContent(String rawContent) {
		addContent(rawContent);		
	}

	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text) {
		addText(leftPosition, topPositionFromBottom, fontSize, text, crl.android.pdfwriter.StandardFonts.DEGREES_0_ROTATION);
	}
	
	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text, String transformation) {
		addContent(
			"BT\n"+
			transformation+" "+Integer.toString(leftPosition)+" "+Integer.toString(topPositionFromBottom)+" Tm\n" +
			"/F1 "+Integer.toString(fontSize)+" Tf\n"+
			"("+text+") Tj\n"+
			"ET\n"
		);
	}
	
	public void addLine(int fromLeft, int fromBottom, int toLeft, int toBottom) {
		addContent(
			Integer.toString(fromLeft)+" "+Integer.toString(fromBottom)+" m\n"+
			Integer.toString(toLeft)+" "+Integer.toString(toBottom)+" l\nS\n"
		);
	}
	
	public String asString() {
		return mDocument.toPDFString();
	}
	
}
