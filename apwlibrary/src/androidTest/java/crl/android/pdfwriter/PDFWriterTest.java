package crl.android.pdfwriter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@TargetApi(Build.VERSION_CODES.KITKAT)
public class PDFWriterTest {

    private PDFWriter pdfWriter;
    private byte[] expected;

    @SuppressWarnings("deprecation")
    @Before
    public void initTargetContext() throws IOException {
        Context context = InstrumentationRegistry.getTargetContext();
        assertNotNull(context);

        AssetManager assetManager = context.getAssets();
        expected = IOUtils.toByteArray(assetManager.open("output-22-" + Build.CPU_ABI + ".pdf"));

        // reset image counter to make sure the files are identical
        XObjectImage.mImageCount = 0;

        pdfWriter = new PDFWriter(PaperSize.FOLIO_WIDTH, PaperSize.FOLIO_HEIGHT);
        pdfWriter.setId("1234567890");

        Bitmap xoiPNG = BitmapFactory.decodeStream(assetManager.open("CRL-borders.png"));
        Bitmap xoiJPG = BitmapFactory.decodeStream(assetManager.open("CRL-star.jpg"));
        Bitmap xoiBMP1 = BitmapFactory.decodeStream(assetManager.open("CRL-1bit.bmp"));
        Bitmap xoiBMP8 = BitmapFactory.decodeStream(assetManager.open("CRL-8bits.bmp"));
        Bitmap xoiBMP24 = BitmapFactory.decodeStream(assetManager.open("CRL-24bits.bmp"));
        pdfWriter.addImage(400, 600, xoiPNG, Transformation.DEGREES_315_ROTATION);
        pdfWriter.addImage(300, 500, xoiJPG);
        pdfWriter.addImage(200, 400, 135, 75, xoiBMP24);
        pdfWriter.addImage(150, 300, 130, 70, xoiBMP8);
        pdfWriter.addImageKeepRatio(100, 200, 50, 25, xoiBMP8);
        pdfWriter.addImageKeepRatio(50, 100, 30, 25, xoiBMP1, Transformation.DEGREES_270_ROTATION);
        pdfWriter.addImageKeepRatio(25, 50, 30, 25, xoiBMP1);

        pdfWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN);
        pdfWriter.addRawContent("1 0 0 rg\n");
        pdfWriter.addTextAsHex(70, 50, 12, "68656c6c6f20776f726c6420286173206865782921");
        pdfWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER, StandardFonts.WIN_ANSI_ENCODING);
        pdfWriter.addRawContent("0 0 0 rg\n");
        pdfWriter.addText(30, 90, 10, "� CRL", Transformation.DEGREES_270_ROTATION);

        pdfWriter.newPage();
        pdfWriter.addRawContent("[] 0 d\n");
        pdfWriter.addRawContent("1 w\n");
        pdfWriter.addRawContent("0 0 1 RG\n");
        pdfWriter.addRawContent("0 1 0 rg\n");
        pdfWriter.addRectangle(40, 50, 280, 50);
        pdfWriter.addText(85, 75, 18, "Code Research Laboratories");

        pdfWriter.newPage();
        pdfWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.COURIER_BOLD);
        pdfWriter.addText(150, 150, 14, "http://coderesearchlabs.com");
        pdfWriter.addLine(150, 140, 270, 140);

        int pageCount = pdfWriter.getPageCount();
        for (int i = 0; i < pageCount; i++) {
            pdfWriter.setCurrentPage(i);
            pdfWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / " + Integer.toString(pageCount));
        }
    }

    @Test
    public void testAsString() throws Exception {
        byte[] bytes = pdfWriter.asString().getBytes(StandardCharsets.US_ASCII);
        assertArrayEquals(expected, bytes);
    }

    @Test
    public void testWriteTo() throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        pdfWriter.writeTo(stream);
        assertArrayEquals(expected, stream.toByteArray());
    }
}
