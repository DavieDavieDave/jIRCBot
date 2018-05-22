package jIRCBot;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ZFSCalc {

	private static ZFSCalc instance = null;
	
	public Double tbToTibRatio;
	public Double metadataOverheadRatio;
	public Double blocksOverheadRatio;
	public Double maxRecommendedUsage;
	
	private ZFSCalc() {
	
		tbToTibRatio = 0.9095;
		metadataOverheadRatio = 0.016; // 1/64 rule
		blocksOverheadRatio = 0.0;
		maxRecommendedUsage = 0.8; // 80% rule
	
	}
	
	public static ZFSCalc getInstance() {
		
		if (instance == null)
			instance = new ZFSCalc();
		
		return instance;
		
	}
	
	public String RIADZCalculator(int raidz, int drives, int size) {
		
		ZFSCalc zfscalc = ZFSCalc.getInstance();
		
		int minDrives = 0;
		int parityDrives = 0;
		int dataDrives = 0;
		
		Double dataSpace = 0.00;
		Double paritySpace = 0.00;
		Double totalSpace = 0.00;
		Double driveSize = 0.00;
		
		String answer = new String();
		String invalidConfig = "Sorry, but that is not a valid configuration.";
		String invalidRaidZ = "Sorry, but that is not a valid RAIDZ level.";
		
		if ((raidz<0) || (drives<0) || (size<0)) {
			return null;
		}
		
		if ((raidz<1) || (raidz>3)) {
			return invalidRaidZ;
		}
		
		switch (raidz) {
		case 1:
			minDrives = 3;
			parityDrives = 1;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			if (drives < minDrives)
				return invalidConfig;
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			break;
		case 2:
			minDrives = 4;
			parityDrives = 2;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			if (drives < minDrives)
				return invalidConfig;
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			break;
		case 3:
			minDrives = 5;
			parityDrives = 3;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			if (drives < minDrives)
				return invalidConfig;
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			break;
		default:
			break;
		}
		
		answer = String.format("RAID-Z%s [Drives: Data %s, Parity %s] Data: %sTB, Parity: %sTB, Total Size: %sTB", raidz, dataDrives, parityDrives, dataSpace, paritySpace, totalSpace);
		
		if (totalSpace > 0) {
			return answer;
		} else {
			return null;
		}
		
	}
	
	public double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
}
