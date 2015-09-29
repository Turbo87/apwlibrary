//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;

public class IndirectIdentifier extends Base {

    private int mNumber;
    private int mGeneration;

    public IndirectIdentifier() {
        clear();
    }

    public void setNumber(int Number) {
        this.mNumber = Number;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setGeneration(int Generation) {
        this.mGeneration = Generation;
    }

    public int getGeneration() {
        return mGeneration;
    }

    @Override
    public void clear() {
        mNumber = 0;
        mGeneration = 0;
    }

    @Override
    public String toPDFString() {
        return Integer.toString(mNumber) + " " + Integer.toString(mGeneration);
    }

    public int writeTo(OutputStream stream) throws IOException {
        byte[] bytes = toPDFString().getBytes(StandardCharsets.US_ASCII);
        stream.write(bytes);
        return bytes.length;
    }
}
