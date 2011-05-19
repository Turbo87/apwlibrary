package crl.android.pdfwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import crl.android.pdfwriter.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class PDFWriterDemo extends Activity {
	
	TextView mText;
	
	private String generateHelloWorldPDF() {
		PDFWriter mPDFWriter = new PDFWriter();
		mPDFWriter.setPageHeight(400);
		mPDFWriter.setPageWidth(320);
        mPDFWriter.setPageFont(
        	crl.android.pdfwriter.StandardFonts.SUBTYPE,
        	crl.android.pdfwriter.StandardFonts.TIMES_ROMAN,
        	crl.android.pdfwriter.StandardFonts.WIN_ANSI_ENCODING
        );
        mPDFWriter.addRawContent("1 0 0 rg\n");
        mPDFWriter.addText(70, 50, 12, "hello world");
        mPDFWriter.addRawContent("0 0 0 rg\n");
        mPDFWriter.addText(30, 90, 10, "© CRL", crl.android.pdfwriter.StandardFonts.DEGREES_270_ROTATION);
        mPDFWriter.addLine(150, 150, 150, 200);
        mPDFWriter.addRawContent("[] 0 d\n");
        mPDFWriter.addRawContent("1 w\n");
        mPDFWriter.addRawContent("0 0 1 RG\n");
        mPDFWriter.addRawContent("0 1 0 rg\n");
        mPDFWriter.addRectangle(40, 50, 20, 50);
        String s = mPDFWriter.asString();
        return s;
	}
	
	private void outputToScreen(int viewID, String pdfContent) {
        mText = (TextView) this.findViewById(viewID);
        mText.setText(pdfContent);
	}
	
	private void outputToFile(String fileName, String pdfContent, String encoding) {
        File newFile = new File(Environment.getExternalStorageDirectory()+"/"+fileName);
        try {
            newFile.createNewFile();
            try {
            	FileOutputStream pdfFile = new FileOutputStream(newFile);
            	pdfFile.write(pdfContent.getBytes(encoding));
                pdfFile.close();
            } catch(FileNotFoundException e) {
            	//
            }
        } catch(IOException e) {
        	//
        }
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String pdfcontent = generateHelloWorldPDF();
        outputToScreen(R.id.text, pdfcontent);
        outputToFile("helloworld.pdf",pdfcontent,"ISO-8859-1");
    }
}