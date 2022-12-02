// Websiter handles fetching specific information from websites.

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

/* CREDIT: TutorialsPoint - https://www.tutorialspoint.com/
 * how-to-read-the-contents-of-a-webpage-into-a-string-
 * in-java */

/* Note: StringBuffer changed to StringBuilder (program is not 
 * asynchronous) */

public class Websiter {
  private String siteURL = "";
  private String siteContents = "";

  // Constructor
  public Websiter(String site){
    siteURL = site;
  }

  /* Fetch - get characters of a website.
   * Use the URL class and StringBuilders to get contents 
   * in String format, then set object contents to this */
  public void fetch() throws IOException {
    //instantiating the URL class
    URL url = new URL(siteURL);
    //retrieving the contents of the specified page
    Scanner sc = new Scanner(url.openStream());
    //instantiating the StringBuilder class to hold the result
    StringBuilder sb = new StringBuilder();
    while(sc.hasNext()) {
       sb.append(sc.next());
       //System.out.println(sc.next()); //<DEBUG>
    }
    //retrieving the String from the String Builder object
    String result = sb.toString();
   // System.out.println(result); //<DEBUG>
    
    /* //removing the HTML tags (NOT NECESSARY HERE)
    result = result.replaceAll("<[^>]*>", ""); */
  
    siteContents = result;
   }
  
  /* Trim - reduce to the characters we want.
   * Takes in current contents, performs an operation, 
   * and sets object contents to a new String */
  public void trim(String instruction) {
    /* Key variable "codes"
     * V1: LAUNCH
     * J - find javascript file
     * M - 'Ma', start of answer list
     * O - 'Oa', middle of answer/valid lists
     * R - 'Ra', end of valid list
     * V2: UPDATE 5/20, CURRENTLY IN USE
     * J - find javascript file
     * M - 'ko', start of answer list
     * O - 'wo', middle of answer/valid lists
     * R - '_o', end of valid list
     * V3: UPDATE 9/23, WTF NEW YORK TIMES
     * J - find javascript file
     * M - 'ft', start of answer list
     * O - 'bt', middle of answer/valid lists
     * R - 'var', end of valid list
     * [IMPT NOTE!] This code relies on the javascript having
     * these key phrases. Without them, it won't work. 
     * Naturally, the program will break every now and then. */
    String keyStringJ = "react-bundle.c60d012dfdc21f4504811e6fde33862ad9c7d05d.js\"></script><scriptdefer=\"\"type=\"text/javascript\"src=\"";
    String keyStringM = "Y()))}},it=[\"";
    String keyStringO = "\"zymic\",\"";
    String keyStringR = "\"];var";

    /* FIND_JS - build the javascript page's web link
     * one character at a time; this could probably
     * be made more efficient... */
    if (instruction.equals("FIND_JS")){
      //System.out.println(siteContents); //<DEBUG>
      int start = siteContents.indexOf(keyStringJ) + keyStringJ.length();
      String linkJS = "";
      for (int i = start; true; i++){
        if (siteContents.charAt(i) == '\"'){
          break;
        }
        linkJS = linkJS + Character.toString(siteContents.charAt(i));
        //System.out.println(linkJS); //<DEBUG>
        
      }
      siteContents = linkJS;
      //System.out.println(linkJS); //<DEBUG>
      //siteContents = "https://www.nytimes.com/games/wordle/main.4d41d2be.js"; //<DEBUG>
    }

    /* FIND_LIST_ANS - substring the start and end
     * of the answer list in the page */
    else if (instruction.equals("FIND_LIST_ANS")){
      int startA = siteContents.indexOf(keyStringM) + keyStringM.length();
      int endA = siteContents.indexOf(keyStringO);

      String answerString = siteContents.substring(startA, endA);
      //System.out.println(answerString); //<DEBUG>
      siteContents = answerString;      
    }

    /* FIND_LIST_VALID - substring the start and end
     * of the valids list in the page */
    else if (instruction.equals("FIND_LIST_VAL")){
      int startV = siteContents.indexOf(keyStringO) + keyStringO.length();
      int endV = siteContents.indexOf(keyStringR);

      String validString = siteContents.substring(startV, endV);
      //System.out.println(validString); //<DEBUG>
      siteContents = validString;
    }
  }

  public ArrayList<String> split(){
    return new ArrayList<String>(Arrays.asList(
                                 siteContents.split("\",\"")));
  }

  // Get methods
  public String getURL(){
    return siteURL;
  } 
  public String getContents(){
    return siteContents;
  }
}