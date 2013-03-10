//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import crl.android.pdfwriter.R;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

public class PDFWriterDemo extends Activity {
	
	TextView mText;
	
	private String generateHelloWorldPDF() {
		PDFWriter mPDFWriter = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);

		// note that to make this images snippet work
		// you have to uncompress the assets.zip file
		// included into your project assets folder
        AssetManager mngr = getAssets();
		try {
			Bitmap xoiPNG = BitmapFactory.decodeStream(mngr.open("CRL-borders.png"));
			Bitmap xoiJPG = BitmapFactory.decodeStream(mngr.open("CRL-star.jpg"));
			Bitmap xoiBMP1 = BitmapFactory.decodeStream(mngr.open("CRL-1bit.bmp"));
			Bitmap xoiBMP8 = BitmapFactory.decodeStream(mngr.open("CRL-8bits.bmp"));
			Bitmap xoiBMP24 = BitmapFactory.decodeStream(mngr.open("CRL-24bits.bmp"));
	        mPDFWriter.addImage(400, 600, xoiPNG, Transformation.DEGREES_315_ROTATION);
	        mPDFWriter.addImage(300, 500, xoiJPG);
	        mPDFWriter.addImage(200, 400, 135, 75, xoiBMP24);
	        mPDFWriter.addImage(150, 300, 130, 70, xoiBMP8);
	        mPDFWriter.addImageKeepRatio(100, 200, 50, 25, xoiBMP8);
	        mPDFWriter.addImageKeepRatio(50, 100, 30, 25, xoiBMP1, Transformation.DEGREES_270_ROTATION);
	        mPDFWriter.addImageKeepRatio(25, 50, 30, 25, xoiBMP1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN);
        mPDFWriter.addRawContent("1 0 0 rg\n");
        mPDFWriter.addText(70, 50, 12, "hello world");
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER, StandardFonts.WIN_ANSI_ENCODING);
        mPDFWriter.addRawContent("0 0 0 rg\n");
        mPDFWriter.addText(30, 90, 10, "© CRL", Transformation.DEGREES_270_ROTATION);
        
        mPDFWriter.newPage();
        mPDFWriter.addRawContent("[] 0 d\n");
        mPDFWriter.addRawContent("1 w\n");
        mPDFWriter.addRawContent("0 0 1 RG\n");
        mPDFWriter.addRawContent("0 1 0 rg\n");
        mPDFWriter.addRectangle(40, 50, 280, 50);
        mPDFWriter.addText(85, 75, 18, "Code Research Laboratories");
        
        mPDFWriter.newPage();
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER_BOLD);
        mPDFWriter.addText(150, 150, 14, "http://coderesearchlabs.com");
        mPDFWriter.addLine(150, 140, 270, 140);
        
        int pageCount = mPDFWriter.getPageCount();
        for (int i = 0; i < pageCount; i++) {
        	mPDFWriter.setCurrentPage(i);
        	mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / " + Integer.toString(pageCount));
        }
        
        String s = mPDFWriter.asString();
        return s;
	}
	
	private void outputToScreen(int viewID, String pdfContent) {
        mText = (TextView) this.findViewById(viewID);
        mText.setText(pdfContent);
	}
	
	private void outputToFile(String fileName, String pdfContent, String encoding) {
        File newFile = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
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