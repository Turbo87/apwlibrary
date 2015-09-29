//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;

public class EnclosedContent extends Base {

    private final String mBegin;
    private final String mEnd;
    protected StringBuilder mContent;

    public EnclosedContent(String begin, String end) {
        mBegin = begin;
        mEnd = end;
        clear();
    }

    public boolean hasContent() {
        return mContent.length() > 0;
    }

    public void setContent(String Value) {
        clear();
        mContent.append(Value);
    }

    public String getContent() {
        return mContent.toString();
    }

    public void addContent(String Value) {
        mContent.append(Value);
    }

    public void addNewLine() {
        mContent.append("\n");
    }

    public void addSpace() {
        mContent.append(" ");
    }

    @Override
    public void clear() {
        mContent = new StringBuilder();
    }

    @Override
    public String toPDFString() {
        return mBegin + mContent.toString() + mEnd;
    }

    public int writeTo(OutputStream stream) throws IOException {
        byte[] bytes = toPDFString().getBytes(StandardCharsets.US_ASCII);
        stream.write(bytes);
        return bytes.length;
    }
}
