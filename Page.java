//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.util.ArrayList;

public class Page {

	private PDFDocument mDocument;
	private IndirectObject mIndirectObject;
	private ArrayList<IndirectObject> mPageFonts;
	private IndirectObject mPageContents;
	
	public Page(PDFDocument document) {
		mDocument = document;
		mIndirectObject = mDocument.newIndirectObject();
		mPageFonts = new ArrayList<IndirectObject>();
		setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN, StandardFonts.WIN_ANSI_ENCODING);
		mPageContents = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(mPageContents);
	}
	
	public IndirectObject getIndirectObject() {
		return mIndirectObject;
	}
	
	private String getFontReferences() {
		String result = "";
		int x = 0;
		for (IndirectObject lFont: mPageFonts)
			result += "      /F"+Integer.toString(++x)+" "+lFont.getIndirectReference()+"\n";
		return result;
	}

	public void render(String pagesIndirectReference) {
		mIndirectObject.setDictionaryContent("  /Type /Page\n  /Parent "+pagesIndirectReference+
			"\n  /Resources <<\n    /Font <<\n"+getFontReferences()+"    >>\n  >>\n  /Contents "+mPageContents.getIndirectReference()+"\n");
	}
	
	public void setFont(String subType, String baseFont) {
		IndirectObject lFont = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(lFont);
		lFont.setDictionaryContent("  /Type /Font\n  /Subtype /"+subType+"\n  /BaseFont /"+baseFont+"\n");
		mPageFonts.add(lFont);
	}

	public void setFont(String subType, String baseFont, String encoding) {
		IndirectObject lFont = mDocument.newIndirectObject();
		mDocument.includeIndirectObject(lFont);
		lFont.setDictionaryContent("  /Type /Font\n  /Subtype /"+subType+"\n  /BaseFont /"+baseFont+"\n  /Encoding /"+encoding+"\n");
		mPageFonts.add(lFont);
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
		addText(leftPosition, topPositionFromBottom, fontSize, text, StandardFonts.DEGREES_0_ROTATION);
	}
	
	public void addText(int leftPosition, int topPositionFromBottom, int fontSize, String text, String transformation) {
		addContent(
			"BT\n"+
			transformation+" "+Integer.toString(leftPosition)+" "+Integer.toString(topPositionFromBottom)+" Tm\n" +
			"/F"+Integer.toString(mPageFonts.size())+" "+Integer.toString(fontSize)+" Tf\n"+
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
