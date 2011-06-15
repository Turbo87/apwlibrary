package crl.android.pdfwriter;

public class Page {

	private PDFDocument mDocument;
	private IndirectObject mIndirectObject;
	private IndirectObject mPageFont;
	private IndirectObject mPageContents;
	
	public Page(PDFDocument document) {
		mDocument = document;
		mIndirectObject = mDocument.newIndirectObject();
		mPageFont = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPageFont);
		setPageFont(
				crl.android.pdfwriter.StandardFonts.SUBTYPE,
				crl.android.pdfwriter.StandardFonts.TIMES_ROMAN,
				crl.android.pdfwriter.StandardFonts.WIN_ANSI_ENCODING
		);
		mPageContents = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPageContents);
	}
	
	public IndirectObject getIndirectObject() {
		return mIndirectObject;
	}

	public void render(String pagesIndirectReference) {
		mIndirectObject.setDictionaryContent("  /Type /Page\n  /Parent "+pagesIndirectReference+
			"\n  /Resources <<\n    /Font <<\n      /F1 "+mPageFont.getIndirectReference()+"\n    >>\n  >>\n  /Contents "+mPageContents.getIndirectReference()+"\n");
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
	
	public void addRectangle(int fromLeft, int fromBottom, int toLeft, int toBottom) {
		addContent(
			Integer.toString(fromLeft)+" "+Integer.toString(fromBottom)+" "+
			Integer.toString(toLeft)+" "+Integer.toString(toBottom)+" re\nS\n"
		);
	}
}
