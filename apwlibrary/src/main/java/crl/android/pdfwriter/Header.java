//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;

public class Header extends Base {

    private String mVersion;
    private String mRenderedHeader;

    public Header() {
        clear();
    }

    public void setVersion(int Major, int Minor) {
        mVersion = Integer.toString(Major) + "." + Integer.toString(Minor);
        render();
    }

    public int getPDFStringSize() {
        return mRenderedHeader.length();
    }

    private void render() {
        mRenderedHeader = "%PDF-" + mVersion + "\n%\u00a9\u00bb\u00aa\u00b5\n";
    }

    @Override
    public String toPDFString() {
        return mRenderedHeader;
    }

    @Override
    public void clear() {
        setVersion(1, 4);
    }

	public int writeTo(OutputStream stream) throws IOException {
		byte[] bytes = mRenderedHeader.getBytes(StandardCharsets.US_ASCII);
		stream.write(bytes);
		return bytes.length;
	}
}
