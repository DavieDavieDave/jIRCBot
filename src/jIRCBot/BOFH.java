package jIRCBot;

import java.util.Random;

public class BOFH {

	public static String askTheBOFH() {
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
