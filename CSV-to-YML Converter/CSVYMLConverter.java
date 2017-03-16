/*
The Able.Parser 
Written By: CN
Version: 1.0
Date: 02/07/2017
Status: 50% Complete

Problem:  The alignment and formatting of YML is challanging to me and I don't like it. Our YML files need to be formatted 
perfectly to prevent unneccessary non-value add work. Also to keep our engineers happy.

Goal: This prorgram will take input from Excel outputted CSV files, read it into arrays, then spit out a perfectly
generated Able Health testdata YML file.

Motto: "If the task sucks, find a better way to do it!"

Copywrite: All work created in this program are the sole property of Able Health, it made not be bought, stolen, or transferred
without the written consent of Able Health.

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JFileChooser;
import java.io.File;       

 
public class CSVYMLConverter {

   TreeMap tm;
   TreeMap testPatientsKeyDataMap;
   TreeMap testPatientsAttributesDataMap;
   
   TreeMap patientsAbleKeyOutputFormat;
   TreeMap patientsAbleAttributeOutputFormat;
   TreeMap patientsAbleClinicalModelOutputFormat;
         
   String[] inArray = new String[5];
   String[] patientKeyDataMap = new String[4];
   
   StringBuffer sb = new StringBuffer();
   
   String tempString = new String();
  
   String newline = System.getProperty("line.separator");  
   String fileToParse = "";
   String fileToCreate = "";
 
            
   BufferedReader fileReader = null;
   BufferedWriter fileWriter = null;
   BufferedReader fileReaderAttributes = null;
   BufferedReader fileWriterAttributes = null;
   
   PrintWriter out = null;
   
   int count = 0;
   
   final String DELIMITER = ",";
   String inputMeasureString;

   int countPatientClinicalActivitiesData = 0;
   int countPatientMedications = 0;
   int countPatientLabs = 0;
   int countPatientDiagnosis = 0;
     
      
   public static void main(String[] args)
   {
      CSVYMLConverter able = new CSVYMLConverter();
      able.startProg();
          
   }
   
   public void startProg() {
   
      tm = new TreeMap<Integer,String[]>();
      testPatientsKeyDataMap   = new TreeMap<Integer,String[]>();  
      testPatientsAttributesDataMap = new TreeMap<Integer,String[]>();
   
      patientsAbleKeyOutputFormat = new TreeMap<String,String[]>();
      patientsAbleAttributeOutputFormat = new TreeMap<String,String[]>();
      patientsAbleClinicalModelOutputFormat = new TreeMap<String,String[]>();
                     
      System.out.println("Enter the measure ID: ");
      Scanner scanner = new Scanner(System.in);
      inputMeasureString = scanner.nextLine();
      
      inputMeasureString = inputMeasureString + ".yml";
      
      fileToCreate = inputMeasureString;
      
      try
      {
      
         String line = null;
         fileReader = new BufferedReader(new FileReader(fileToParse));
         fileWriter = new BufferedWriter(new FileWriter(fileToCreate,true));
         
         out = new PrintWriter(fileToCreate);
            
         out.println("measure_id: " + inputMeasureString);
         out.println("test_set_keys: ");
         out.println("   organization_key: ");
         
         
         int j = 0;
            
         while ((line = fileReader.readLine()) != null) {
           
            String[] tokens = line.split(DELIMITER,-1);
               
            //System.out.println("Count of patient clinical activities = " + countPatientClinicalActivitiesData);
            //System.out.println("Count of patient medications = " + countPatientMedications);
            //System.out.println("Count of patient labs = " + countPatientLabs);
             
          
            /*
           
           A new approach: This reads an input file in seperated format and stores the data in dynamically 
           generated arrays.  it then writes the data out from those arrays to a file - > done.
           
           A New Approach approach:
           
           while (read each line in use fileReader) {
           then calculate the number of models/activities using that data to size the hashmaps
           
           line test case 1 read:  key, attributes,clinical model, 
                                   1,2,3,4,5,6,7,8,9,10,11,12,13,14,15
            
           line test case 2 read:  key, attributes,clinical model, 
                                   1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 
           
           generate header data, then iterate through three loops:
           
           1st: for (from 1-9 generate key data for each patient from each entry in the map
           -> write this to an output file (need to figure out how to have a "running string" which I can
           append to throughout this program)
           
           2nd: ask for input on the provider/organization model
           -> append the provider data to the output string
           
           3rd: for (iterate through the reminder of the hashmaps)
           -> append this data to hashmaps.
         
         */                                      
            
            for (int i = 0; i < 5; i++) {
            
               String tempToken = tokens[i];
               String writeString = this.generatePatientKey(tempToken,i,out);
               //System.out.print(writeString);
               out.print(writeString);
             
            }
            
            this.generateProviderKeyData(out);
            
            this.setLineFormat(tokens);
            
           
           
           
           
         }
         
         
      }
      catch (Exception e) {
         System.out.println("Except this :" + e);
      }
         
      out.close();
   
   }
   
      
   private void setLineFormat(String[] tempTokens) throws Exception {
   
      String tempString;
      String clinicalActivity = "c";
   
   
      for (int i = 0; i < tempTokens.length; i++) {
      
         tempString = tempTokens[i];
      
         if (Pattern.matches(tempString,clinicalActivity)) 
         {
            countPatientClinicalActivitiesData++;
            System.out.println("Match Clinical Activity: " + countPatientClinicalActivitiesData);
         }
         if (Pattern.matches(tempString,"l"))
         {
            countPatientLabs++; 
            System.out.println("Match Lab: " + countPatientLabs);
         }
         if (Pattern.matches(tempString,"m")) 
         {
            countPatientMedications++;
            System.out.println("Match Meds: " + countPatientMedications);
         }
         if (Pattern.matches(tempString,"c")) 
         {
            countPatientDiagnosis++;
            System.out.println("Match Meds: " + countPatientDiagnosis); 
         }
         
      
      }
      
   }
   
   

   private String generatePatientKey(String tempString, int tempNum, PrintWriter p) {
   
      
      String tempToken = tempString;
      String holdString = new String();        
          
      switch (tempNum) {
         case 0: tempString = new String("   - " + "name: " + tempToken + newline);
            p.print(tempString);
            break;
         case 1: tempString = new String("     " + "denominator: " + tempToken + newline);
            p.print(tempString);
            break;
         case 2: tempString = new String("     " + "denominator_exclusion: " + tempToken + newline);
            p.print(tempString);
            break;
         case 3: tempString = new String("     " + "denominator_exception: " + tempToken + newline);
            p.print(tempString);
            break;
         case 4: tempString = new String("     " + "numerator: " + tempToken + newline); 
            p.print(tempString);
         default:
            break;
         
      }
         
      return holdString;            
   }
         
     
   private String generatePatientAttributes(String tempString, int tempNum) {
    
      String tempToken = tempString;
      String holdString = new String();  
    
    
      switch (tempNum) {
      
         case 5: tempString = new String("   - " + "name: " + tempToken + newline);
            holdString = tempString;
            break;
         case 6: tempString = new String("     " + "attributes: " + tempToken + newline);
            holdString = tempString;
            break;
         case 7: tempString = new String("     " + "   date_of_birth: " + tempToken + newline);
            holdString = tempString;
            break;
         case 8: tempString = new String("     " + "   given_name: " + tempToken + newline);
            holdString = tempString;
            break;
         case 9: tempString = new String("     " + "   family_name: " + tempToken + newline);   
            holdString = tempString;
            break;
         case 10: tempString = new String("     " + "   sex: " + tempToken + newline);
            holdString = tempString;
            break;
         default:
            break;
      }          
               
        
      return holdString; 
   
   }
 

   private void generateProviderKeyData(PrintWriter p) {
    
      p.println("providers_key: ");
      p.println("- given_name: Rachel");
      p.println("  family_name: Katz");
      p.println("  credential: DO");
      p.println("  primary_specialty: Family Medicine");
      p.println("  pqrs_provider = true");
      
     
      
   }
   
}
   
   

