package cloudFormationBuilder;

import java.io.*;

public class CreateFile {
	
		public static boolean check(String fileName, String userHomeFolder) {
				try {
			      File myObj = new File(userHomeFolder + fileName + ".yaml");
			      if (myObj.createNewFile()) {
			        System.out.println(myObj.getName() + " created at " + userHomeFolder);
			        return true;
			      } 
			      else {
			        System.out.println("File already exists. Please enter a new file name.");
			        return false;
			      }
			    } catch (IOException e) {
			      System.out.println("An error occurred. Please enter a new file name.");
			      e.printStackTrace();
			      return false;
			    }
			}
		}