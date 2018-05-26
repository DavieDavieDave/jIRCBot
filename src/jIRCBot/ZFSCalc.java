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
	
	public String RIADZCalculator(String level, int drives, int size) {
		
		ZFSCalc zfscalc = ZFSCalc.getInstance();
		
		int minDrives = 0;
		int parityDrives = 0;
		int dataDrives = 0;
		
		Double dataSpace = 0.00;
		Double paritySpace = 0.00;
		Double totalSpace = 0.00;
		Double driveSize = 0.00;
		Double recommendedUsage = 0.00;
		
		String answer = new String();
		String raidLevel = new String();
		String invalidConfig = "Sorry, but that is not a valid configuration.";
		String invalidLevel = "Sorry, but that is not a valid ZFS level.";
		String invalidMirror = "Number of drives must be an even number.";
		
		if ((level == null) || (drives<0) || (size<0))
			return invalidConfig;
				
		String[] levels = {"raidz1","raidz2","raidz3","mirror","stripe"};
		
		int i;
		for (i=0; i <levels.length; i++)
			if(level.toLowerCase().contains(levels[i])) break;
		
		switch (i) {
		case 0:
			minDrives = 3;
			parityDrives = 1;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			raidLevel = "RAID-Z1";
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			recommendedUsage = this.round(dataSpace * zfscalc.maxRecommendedUsage, 2);
			break;
		case 1:
			minDrives = 4;
			parityDrives = 2;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			raidLevel = "RAID-Z2";
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			recommendedUsage = this.round(dataSpace * zfscalc.maxRecommendedUsage, 2);
			break;
		case 2:
			minDrives = 5;
			parityDrives = 3;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			raidLevel = "RAID-Z3";
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			recommendedUsage = this.round(dataSpace * zfscalc.maxRecommendedUsage, 2);
			break;
		case 3:
			minDrives = 2;
			parityDrives = drives / 2;
			dataDrives = drives / 2;
			driveSize = size * zfscalc.tbToTibRatio;
			raidLevel = "Mirror";
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			recommendedUsage = this.round(dataSpace * zfscalc.maxRecommendedUsage, 2);
			if(drives%2 != 0)
				return invalidMirror;
			break;
		case 4:
			minDrives = 1;
			parityDrives = 0;
			dataDrives = drives - parityDrives;
			driveSize = size * zfscalc.tbToTibRatio;
			raidLevel = "Stripe";
			dataSpace = this.round(driveSize * dataDrives, 2);
			paritySpace = this.round(driveSize * parityDrives, 2);
			totalSpace = this.round(driveSize * drives, 2);
			recommendedUsage = this.round(dataSpace * zfscalc.maxRecommendedUsage, 2);
			break;
		default:
			return invalidLevel;
		}
		
		if (drives < minDrives)
			return invalidConfig;
		
		answer = String.format("%s [Drives: %s (%s+%s parity)] Total: %sTB, Parity: %sTB, Usable: %sTB (Max recommended usage: %sTB)", 
				raidLevel, drives, dataDrives, parityDrives, totalSpace, paritySpace, dataSpace, recommendedUsage);
		
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
