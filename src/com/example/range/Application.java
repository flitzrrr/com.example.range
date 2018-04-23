package com.example.range;

import org.eclipse.compare.rangedifferencer.IRangeComparator;
import org.eclipse.compare.rangedifferencer.RangeDifference;
import org.eclipse.compare.rangedifferencer.RangeDifferencer;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class Application implements IApplication {

	private static class LineComparator implements IRangeComparator {

		private String[] fLines;

		public LineComparator(String[] lines) {
			fLines = lines;
		}

		@Override
		public int getRangeCount() {
			return fLines.length;
		}

		@Override
		public boolean rangesEqual(int thisIndex, IRangeComparator other,
				int otherIndex) {
			String s1 = fLines[thisIndex];
			String s2 = ((LineComparator) other).fLines[otherIndex];
			return s1.equals(s2);
		}

		@Override
		public boolean skipRangeComparison(int length, int maxLength, IRangeComparator other) {
			return false;
		}
	};
	
	@Override
	public Object start(IApplicationContext context) throws Exception {

		String[] myJcl = new String[]{ "Zeile1", "Zeile2", "Zeile3", "Zeile4", "Zeile5", "Zeile6", "Zeile7"};
		String[] smartJcl = new String[]{ "sm1", "Zeile1", "Zeile2", "smart2", "Zeile3", "Zeile4", "sm3","sm3", "Zeile5", "Zeile6", "Zeile7", "SM4"};
		
		LineComparator left = new LineComparator(myJcl);
		LineComparator right = new LineComparator(smartJcl);

		RangeDifference[] diffs = RangeDifferencer.findRanges(left, right);	
		for (RangeDifference diff : diffs) {
			System.out.println(diff);
		}
		System.out.println();
		for (int i = 0; i < myJcl.length; i++) {
			System.out.println("" + i + " -> " + newline(i, diffs) + ": " + smartJcl[newline(i, diffs)]);
		}
		return IApplication.EXIT_OK;
	}

	private int newline(int i, RangeDifference[] diffs) {
		int diffInd = -1;
		for (int j = 0; j < diffs.length && diffInd == -1; j++) {
			if (diffs[j].leftStart() <= i && i < diffs[j].leftStart() + diffs[j].leftLength()) {
				diffInd = j;
			}
		} 
		int posInDiff = i - diffs[diffInd].leftStart(); 
		return diffs[diffInd].rightStart() + posInDiff;
	}

	@Override
	public void stop() {
	}
}
