//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package crl.android.pdfwriter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Body extends List {

    private int mByteOffsetStart;
    private int mObjectNumberStart;
    private int mGeneratedObjectsCount;
    private ArrayList<IndirectObject> mObjectsList;

    public Body() {
        super();
        clear();
    }

    public int getObjectNumberStart() {
        return mObjectNumberStart;
    }

    public void setObjectNumberStart(int Value) {
        mObjectNumberStart = Value;
    }

    public int getByteOffsetStart() {
        return mByteOffsetStart;
    }

    public void setByteOffsetStart(int Value) {
        mByteOffsetStart = Value;
    }

    public int getObjectsCount() {
        return mObjectsList.size();
    }

    private int getNextAvailableObjectNumber() {
        return ++mGeneratedObjectsCount + mObjectNumberStart;
    }

    public IndirectObject getNewIndirectObject() {
        return getNewIndirectObject(getNextAvailableObjectNumber(), 0, true);
    }

    public IndirectObject getNewIndirectObject(int Number, int Generation, boolean InUse) {
        IndirectObject iobj = new IndirectObject();
        iobj.setNumberID(Number);
        iobj.setGeneration(Generation);
        iobj.setInUse(InUse);
        return iobj;
    }

    public IndirectObject getObjectByNumberID(int Number) {
        for (IndirectObject iobj : mObjectsList)
            if (iobj.getNumberID() == Number)
                return iobj;

        return null;
    }

    public void includeIndirectObject(IndirectObject iobj) {
        mObjectsList.add(iobj);
    }

    private String render() {
        int offset = mByteOffsetStart;
        for (int x = 1; x <= mObjectsList.size(); x++) {
            IndirectObject iobj = getObjectByNumberID(x);
            String s = "";
            if (iobj != null)
                s = iobj.toPDFString() + "\n";
            mList.add(s);
            iobj.setByteOffset(offset);
            offset += s.length();
        }
        return renderList();
    }

    @Override
    public String toPDFString() {
        return render();
    }

    public int writeTo(OutputStream stream) throws IOException {
        int offset = mByteOffsetStart;
        for (int x = 1; x <= mObjectsList.size(); x++) {
            IndirectObject iobj = getObjectByNumberID(x);
            if (iobj != null) {
                iobj.setByteOffset(offset);
                offset += iobj.writeTo(stream) + 1;
                stream.write('\n');
            }
        }
        return offset - mByteOffsetStart;
    }

    @Override
    public void clear() {
        super.clear();
        mByteOffsetStart = 0;
        mObjectNumberStart = 0;
        mGeneratedObjectsCount = 0;
        mObjectsList = new ArrayList<IndirectObject>();
    }
}
