package jIRCBot;

import java.util.Random;

public class Toys {

	/*
	 * Simple 8-ball
	 */
	public static String EightBall() {

		String[] eightBall = {
				"It is certain",
				"It is decidedly so",
				"Without a doubt",
				"Yes definitely",
				"You may rely on it",
				"As I see it, yes",
				"Most likely",
				"Outlook good",
				"Yes",
				"Signs point to yes",
				"Reply hazy try again",
				"Ask again later",
				"Better not tell you now",
				"Cannot predict now",
				"Concentrate and ask again",
				"Don't count on it",
				"My reply is no",
				"My sources say no",
				"Outlook not so good",
				"Very doubtful"
		};
		
		Random rand = new Random();
		
		int randomNumber = rand.nextInt(eightBall.length);
		
		return eightBall[randomNumber];
		
	}
	
	/*
	 * Coin flip
	 */
	public static String FlipCoin() {

		String[] coin = {
				"heads",
				"tails"
		};
		
		Random rand = new Random();
		
		int randomNumber = rand.nextInt(coin.length);
		
		String answer = "It's " + coin[randomNumber];
		
		return answer;
		
	}
	
	/*
	 * Generate a BOFH excuse
	 */
	public static String BOFH() {
		String[] bofhFirst = {
				"Temporary",
				"Intermittant",
				"Partial",
				"Redundant",
				"Total",
				"Multiplexed",
				"Inherent",
				"Duplicated",
				"Dual-Homed",
				"Synchronous",
				"Bidirectional",
				"Serial",
				"Asynchronous",
				"Multiple",
				"Replicated",
				"Non-Replicated",
				"Unregistered",
				"Non-Specific",
				"Generic",
				"Migrated",
				"Localised",
				"Resignalled",
				"Dereferenced",
				"Nullified",
				"Aborted",
				"Serious",
				"Minor",
				"Major",
				"Extraneous",
				"Illegal",
				"Insufficient",
				"Viral",
				"Unsupported",
				"Outmoded",
				"Legacy",
				"Permanent",
				"Invalid",
				"Deprecated",
				"Virtual",
				"Unreportable",
				"Undetermined",
				"Undiagnosable",
				"Unfiltered",
				"Static",
				"Dynamic",
				"Delayed",
				"Immediate",
				"Nonfatal",
				"Fatal",
				"Non-Valid",
				"Unvalidated",
				"Non-Static",
				"Unreplicatable",
				"Non-Serious"
		};
		
		String[] bofhSecond = {
				"Array",
				"Systems",
				"Hardware",
				"Software",
				"Firmware",
				"Backplane",
				"Logic-Subsystem",
				"Integrity",
				"Subsystem",
				"Memory",
				"Comms",
				"Integrity",
				"Checksum",
				"Protocol",
				"Parity",
				"Bus",
				"Timing",
				"Synchronisation",
				"Topology",
				"Transmission",
				"Reception",
				"Stack",
				"Framing",
				"Code",
				"Programming",
				"Peripheral",
				"Environmental",
				"Loading",
				"Operation",
				"Parameter",
				"Syntax",
				"Initialisation",
				"Execution",
				"Resource",
				"Encryption",
				"Decryption",
				"File",
				"Precondition",
				"Authentication",
				"Paging",
				"Swapfile",
				"Service",
				"Gateway",
				"Request",
				"Proxy",
				"Media",
				"Registry",
				"Configuration",
				"Metadata",
				"Streaming",
				"Retrieval",
				"Installation",
				"Library",
				"Handler"
		};
		
		String bofhThird[] = {
				"Interruption",
				"Destabilisation",
				"Destruction",
				"Desynchronisation",
				"Failure",
				"Dereferencing",
				"Overflow",
				"Underflow",
				"NMI",
				"Interrupt",
				"Corruption",
				"Anomoly",
				"Seizure",
				"Override",
				"Reclock",
				"Rejection",
				"Invalidation",
				"Halt",
				"Exhaustion",
				"Infection",
				"Incompatibility",
				"Timeout",
				"Expiry",
				"Unavailability",
				"Bug",
				"Condition",
				"Crash",
				"Dump",
				"Crashdump",
				"Stackdump",
				"Problem",
				"Lockout"

		};
		
		String bofhFourth[] = {
				"Error",
				"Problem",
				"Warning",
				"Signal",
				"Flag"
		};
			
		Random rand = new Random();
		
		int intBofhFirst = rand.nextInt(bofhFirst.length);
		int intBofhSecond = rand.nextInt(bofhSecond.length);
		int intBofhThird = rand.nextInt(bofhThird.length);
		int intBofhFourth = rand.nextInt(bofhFourth.length);
		
		String bofhAnswer = bofhFirst[intBofhFirst] + " " + bofhSecond[intBofhSecond] + " " + bofhThird[intBofhThird] + " " + bofhFourth[intBofhFourth];
		
		return bofhAnswer;
		
	}
	
}
