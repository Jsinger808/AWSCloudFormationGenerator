package cloudFormationBuilder;

import java.util.*;


public class CloudFormationBuilder {

	
	public static void main(String[] args) {
	
		
		System.out.println("Please type in your file name: ");
		Scanner in = new Scanner(System.in);
		Scanner textscan = new Scanner(System.in);
		
		//Sets download folder
		System.getProperty("user.home");
		String userHomeFolder = System.getProperty("user.home") + "\\Downloads\\";
		
		//Creates a unique file name
		String fileName = textscan.nextLine();
		CreateFile file = new CreateFile(fileName, userHomeFolder);
		while (!file.newFileCheck()) {
		fileName = textscan.nextLine();
		}
	
		
		//Selects a valid Region
		boolean regionCheck = false;
		String regionSelection = null;
		while (regionCheck == false) {
		System.out.println("What AWS US Region do you intend on creating your resources in? Please type us-east-1, us-east-2, us-west-1, or us-west-2 (Leave blank for default us-east-1)");
		regionSelection = textscan.nextLine();
			if (regionSelection.isEmpty()){
				System.out.println("Default us-east-1 Region");
				regionSelection = "us-east-1";
			}
			if ((regionSelection.equals("us-east-2")) || (regionSelection.equals("us-east-1")) || (regionSelection.equals("us-west-1")) || 
					(regionSelection.equals("us-west-2"))) {
				regionCheck = true;
			}
			else {
				System.out.println("Not a valid Region. Please re-enter.");;
			}
		}
		
		//Selects a valid Availability Zone
		boolean availabilityZoneCheck = false;
		String availabilityZoneSelection = null;
		switch (regionSelection) {
		
		case "us-east-1":
			while (availabilityZoneCheck == false) {
			System.out.println("What is your availability zone? Please type us-east-1a, us-east-1b, us-east-1c, us-east-1d, us-east-1e, or us-east-1f? (Leave blank for default us-east-1a)");
			availabilityZoneSelection = textscan.nextLine();
				if (availabilityZoneSelection.isEmpty()){
				System.out.println("Default us-east-1a AZ");
				availabilityZoneSelection = "us-east-1a";
				}
				switch (availabilityZoneSelection) {
				case "us-east-1a":
					availabilityZoneSelection = "0";
					availabilityZoneCheck = true;
					break;
				case "us-east-1b":
					availabilityZoneSelection = "1";
					availabilityZoneCheck = true;
					break;
				case "us-east-1c":
					availabilityZoneSelection = "2";
					availabilityZoneCheck = true;
					break;
				case "us-east-1d":
					availabilityZoneSelection = "3";
					availabilityZoneCheck = true;
					break;
				case "us-east-1e":
					availabilityZoneSelection = "4";
					availabilityZoneCheck = true;
					break;
				case "us-east-1f":
					availabilityZoneSelection = "5";
					availabilityZoneCheck = true;
					break;
				default:
					System.out.println("Not a valid AZ. Please re-enter.");
					}
				}
			break;
	
		case "us-east-2":
			while (availabilityZoneCheck == false) {
				System.out.println("What is your availability zone? Please type us-east-2a, us-east-2b, or us-east-2c? (Leave blank for default us-east-2a)");
				availabilityZoneSelection = textscan.nextLine();
				if (availabilityZoneSelection.isEmpty()){
					System.out.println("Default us-east-2a AZ");
					availabilityZoneSelection = "us-east-2a";
				}
				switch (availabilityZoneSelection) {
				case "us-east-2a":
					availabilityZoneSelection = "0";
					availabilityZoneCheck = true;
					break;
				case "us-east-2b":
					availabilityZoneSelection = "1";
					availabilityZoneCheck = true;
					break;
				case "us-east-2c":
					availabilityZoneSelection = "2";
					availabilityZoneCheck = true;
					break;
				default:
					System.out.println("Not a valid AZ. Please re-enter.");
					}
				}
			break;
		case "us-west-1":
			while (availabilityZoneCheck == false) {
				System.out.println("What is your availability zone? Please type us-west-1a or us-west-1c? (Leave blank for default us-west-1a)"); //us-west-1b is not available to the public
				availabilityZoneSelection = textscan.nextLine();
				if (availabilityZoneSelection.isEmpty()){
				System.out.println("Default us-west-1a AZ");
				availabilityZoneSelection = "us-west-1a";
				}
				switch (availabilityZoneSelection) {
				case "us-west-1a":
					availabilityZoneSelection = "0";
					availabilityZoneCheck = true;
					break;
				case "us-west-1c":
					availabilityZoneSelection = "2";
					availabilityZoneCheck = true;
					break;
				default:
					System.out.println("Not a valid AZ. Please re-enter.");
				}
			}
			break;
		case "us-west-2":
			while (availabilityZoneCheck == false) {
				System.out.println("What is your availability zone? Please type us-west-2a, us-west-2b, us-west-2c, or us-west-2d? (Leave blank for default us-west-2a)");
				availabilityZoneSelection = textscan.nextLine();
					if (availabilityZoneSelection.isEmpty()){
					System.out.println("Default us-west-2a AZ");
					availabilityZoneSelection = "us-west-2a";
					}
					switch (availabilityZoneSelection) {
					case "us-west-2a":
						availabilityZoneSelection = "0";
						availabilityZoneCheck = true;
						break;
					case "us-west-2b":
						availabilityZoneSelection = "1";
						availabilityZoneCheck = true;
						break;
					case "us-west-2c":
						availabilityZoneSelection = "2";
						availabilityZoneCheck = true;
						break;
					case "us-west-2d":
						availabilityZoneSelection = "3";
						availabilityZoneCheck = true;
						break;
					default:
						System.out.println("Not a valid AZ. Please re-enter.");
					}

				}
			break;
		}
		
		
		//Selects a VPC Name
		System.out.println("What would you like to name your VPC?");
		String nameVPC = textscan.nextLine();
		if (nameVPC.isEmpty()){
			System.out.println("You have left your VPC's name blank");
		}
		
		//Selects a valid EC2 type
		boolean instanceEC2Check = false;
		String instanceTypeEC2 = null;
		while (instanceEC2Check == false) {
			System.out.println("What EC2 instance size would you like? Please type micro (FreeTier eligible), small, medium, large, or extra-large.");
			instanceTypeEC2 = textscan.nextLine();
				if (instanceTypeEC2.isEmpty()){
					System.out.println("Default micro tier.");
					instanceTypeEC2 = "micro";
				}
				if ((instanceTypeEC2.equals("micro")) || (instanceTypeEC2.equals("small")) || (instanceTypeEC2.equals("medium")) || (instanceTypeEC2.equals("large")) || (instanceTypeEC2.equals("extra-large"))) {
					instanceEC2Check = true;
					switch (instanceTypeEC2) {
					case "micro":
						instanceTypeEC2 = "\"t2.micro\"";
						break;
					case "small":
						instanceTypeEC2 = "\"t3.small\"";
						break;
					case "medium":
						instanceTypeEC2 = "\"t3.medium\"";
						break;
					case "large":
						instanceTypeEC2 = "\"t3.large\"";
						break;
					case "extra-large":
						instanceTypeEC2 = "\"t3.xlarge\"";
						break;
					}
				}
				else {
					System.out.println("Not a valid EC2 instance size. Please re-enter.");;
				}
			}
		
		//Selects an EC2 Name
		System.out.println("What would you like to name your EC2?");
		String nameEC2 = textscan.nextLine();
		if (nameEC2.isEmpty()){
			System.out.println("You have left your EC2's name blank");
		}
		
		//Writes all user-input to file
		WriteToExistingFile writtenFile = new WriteToExistingFile(fileName, nameVPC, availabilityZoneSelection, instanceTypeEC2, nameEC2, userHomeFolder, regionSelection);
		
		
		in.close();
		textscan.close();
	}
}


