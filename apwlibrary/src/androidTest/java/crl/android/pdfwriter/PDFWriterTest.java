package crl.android.pdfwriter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PDFWriterTest {

    private Context context;

    @Before
    public void initTargetContext() {
        context = InstrumentationRegistry.getTargetContext();
        assertNotNull(context);
    }

    @Test
    public void test() throws Exception {
        PDFWriter mPDFWriter = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);
        mPDFWriter.setId("1234567890");

        AssetManager mngr = context.getAssets();
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
        mPDFWriter.addTextAsHex(70, 50, 12, "68656c6c6f20776f726c6420286173206865782921");
        mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER, StandardFonts.WIN_ANSI_ENCODING);
        mPDFWriter.addRawContent("0 0 0 rg\n");
        mPDFWriter.addText(30, 90, 10, "� CRL", Transformation.DEGREES_270_ROTATION);

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

        byte[] bytes = mPDFWriter.asString().getBytes(StandardCharsets.US_ASCII);

        assertArrayEquals(IOUtils.toByteArray(mngr.open("output.pdf")), bytes);
    }
}
