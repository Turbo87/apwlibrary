package crl.android.pdfwriter;

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
		return Integer.toString(mNumber)+" "+Integer.toString(mGeneration);
	}

}
