
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SaltedHash {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         
        Scanner sc = new Scanner(System.in);
        //to get the password file
        System.out.println("Please enter the Password file name.");
        String file = sc.next();
    
        BufferedReader reader = null;
        PrintWriter writer= null;
        //trys to make a file for the output.  If there is an error, it will tell you.
        try {
            writer = new PrintWriter("hashed.txt", "UTF-8");
        } 
        catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("Error: could not create file to store hashes in");
        }
        
        //trys to open the password file
        try {
            reader = new BufferedReader(new FileReader(file));
            } 
        catch (FileNotFoundException ex) {
            System.out.println("File not found.  Make sure the file in in the same directory as program.");
            System.exit(1);
            }
        
        String line, password, saltstr, ascii = "";
        char cur;
        
        //reads all of the passwords from the given file
        try{
            while ((line = reader.readLine()) != null)
            {
                password = line;
                ascii = "";
                long left = 0, right = 0, hash = 0;
                
                //goes through all possible combinations of salt and turns it into a string make formating easier
               for(int salt=0; salt<1000;salt++){ 
                   if (salt<10){
                       saltstr = "00"+ salt;
                   }else if(salt<100){
                       saltstr = "0" + salt;
                   }else saltstr = ""+salt;
                 
                   //stores the ascii value as a string to make the procces simpler. converted to long later on.
                   ascii = "";
                   ascii += saltstr;
                   
                   for (int i = 0;i<password.length();i++){
                     cur = password.charAt(i);
                     ascii += (int)cur;  
                    }
                   //splits the ascii value into 2 sides and parses to get it to a long for easy algorithm proccesing.
                   String leftstr, rightstr;
                   leftstr = ascii.substring(0, 8);
                   rightstr = ascii.substring(8);
                   left = Long.parseLong(leftstr);
                   right = Long.parseLong(rightstr);
                  //runs through the hashing algorithm
                   hash = (( 243 * left) + right) % 85767489;
                   //prints the correct info to hashed.txt
                   writer.println(password + " " + saltstr+ " " + hash); 
               }
            }//end of password file reached
        reader.close();
        writer.close();
        }
  //if there is an error while reading file
  catch (Exception e)
  {
    System.err.format("Exception occurred trying to read '%s'.", file);
  }
  // after passwords have been hashed and stored
  String target = "";
  while (!target.contains("exit")){
  //gets the target hash value
  System.out.println("Please enter a hash value or part of a hash value to see possible passwords , or type exit to close the program.");
        target = sc.next();
        //trys to open hashed.txt for read
        try {
            reader = new BufferedReader(new FileReader("hashed.txt"));
            } 
        catch (FileNotFoundException ex) {
            System.out.println("File not found.  Make sure the file in in the same directory as program.");
            System.exit(1);
            }
        int flag = 0;
        //goes through all enties on hashed.txt to see if the target is there
        try {
            while ((line = reader.readLine()) != null)
            {
                if (line.contains(target)){
                    System.out.println("Possible password found. The order is password, salt, hash value.  "+ line);
                    flag = 1;
                }
                
            }
        } catch (IOException ex) {
            System.out.println("Error. Could not open the program generated file hashed.txt.");
        }
        //if no password was found
        if (flag != 1)
            System.out.println("The hash value was not in the given dictionary.");
  }
    }
    
}

