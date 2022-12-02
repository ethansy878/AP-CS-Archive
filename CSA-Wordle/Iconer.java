// Iconer is responsible for printing game text/visuals. Class is fully static.

import java.util.ArrayList;

class Iconer {
  private static String black = "⬛";
  private static String yellow = "🟨";
  private static String green = "🟩";
  private static String red = "🟥";
  private static String blue = "🟦";

  // Helper - make blue line
  private static void blueLine(int amount){
    for (int i=0; i<=amount; i++){
      System.out.print(blue);
    }
    System.out.println();
  }
  
  // Helper - safe wait
  private static void wait(int time){
    try {
      Thread.sleep(time); // Actual java wait command
    }
    catch (Exception ex) {
      System.out.print("");
    }    
  }

  // Helper - shorthand character upper case
  public static char upCase(char c){
    return Character.toUpperCase(c);
  }

  // Intro message
  public static void intro(){
    System.out.println("\t H\t E\t L\t L\t O");
    System.out.println("\t" + black + "\t" + yellow + "\t" + green + "\t" + yellow + "\t" + black + "\n");
    System.out.println("\t W\t R\t D\t L\t E");
    System.out.println("\t" + black + "\t" + yellow + "\t" + green + "\t" + yellow + "\t" + black + "\n");
    System.out.println("\t P\t L\t A\t Y\t R");
    System.out.println("\t" + black + "\t" + yellow + "\t" + green + "\t" + yellow + "\t" + black + "\n");
    blueLine(12);
  }
  
  // Outro message
  public static void outro(String result, String wrdle){
    if (result.equals("win")){
      System.out.println("\t" + green + "\t" + green + "\t" + green + "\t" + green + "\t" + green);
      System.out.println(green + "\t W\t I\t N\t N\t R\t" + green);
      System.out.println("\t" + green + "\t" + green + "\t" + green + "\t" + green + "\t" + green + "\n");
    }
    else if (result.equals("lose")){
      System.out.println("\t" + red + "\t" + red + "\t" + red + "\t" + red + "\t" + red);
      
      // Printing wrdle if you fail
      System.out.print(red + "\t");
      for (int i=0; i<wrdle.length(); i++){
        System.out.print(" " + upCase(wrdle.charAt(i)) + "\t");
      }
      System.out.println(red);
      
      System.out.println("\t" + red + "\t" + red + "\t" + red + "\t" + red + "\t" + red + "\n");
    }
    blueLine(12);
  }

  // Game field - all the previous guesses
  public static void fieldIcons(ArrayList<String> guesses,
                                ArrayList<String> hints,
                                String key){
    /* Three looping variables:
     * t = turn -- stopping pt. is guess list size
     * l = letter
     * h = hint
     * hidOffset = to enable "hidden" mode
     * Note: I did not make reusable methods here because of
     * the need for a "t" turn variable. */

    // Hidden mode offset manager
    int hidOffset = 0;
    if (key.equals("hidden")){
      if (guesses.size() == 0){
        hidOffset = 0;
      }
      else {
        hidOffset = guesses.size() - 1; 
      }
    }
    
    for (int t=(0 + hidOffset); t<guesses.size(); t++){ 
      // NORMAL PROCESS
      if (key.equals("normal") || key.equals("hidden")){
        // Print the letters
        for (int l=0; l<guesses.get(t).length(); l++){
          System.out.print("\t " + guesses.get(t).charAt(l));
        }
        System.out.println(); // 1 line break after all letters
        
        // Print the clues in emoji form
        for (int h=0; h<hints.get(t).length(); h++){
          char hint = hints.get(t).charAt(h);
          if (hint == 'b'){
            System.out.print("\t" + black);
          }
          else if (hint == 'y'){
            System.out.print("\t" + yellow);
          }
          else if (hint == 'g'){
            System.out.print("\t" + green);
          }
        }
      System.out.println("\n"); // 2 line breaks after all emoji
      }

      // CONDENSED PROCESS
      else if (key.equals("condensed")){
        for (int h=0; h<hints.get(t).length(); h++){
          char hint = hints.get(t).charAt(h);
          System.out.print("\t");
          if (hint == 'b'){
            System.out.print(black);
          }
          else if (hint == 'y'){
            System.out.print(yellow);
          }
          else if (hint == 'g'){
            System.out.print(green);
          }
        }
        System.out.println();
      }
    }
    blueLine(12);
  }
  
  // Feedback from the current guess, with suspenseful waiting
  public static void revealIcons(ArrayList<String> guesses,
                                 ArrayList<String> hints){

    int sizeI = guesses.size() - 1; // "Size index"
    
    // Print the letters
    for (int l=0; l<guesses.get(sizeI).length(); l++){
      System.out.print("\t " + guesses.get(sizeI).charAt(l));
    }
    System.out.println();

    // Print the clues in emoji form
    for (int h=0; h<hints.get(sizeI).length(); h++){
      char hint = hints.get(sizeI).charAt(h);
      if (hint == 'b'){
        System.out.print("\t" + black);
      }
      else if (hint == 'y'){
        System.out.print("\t" + yellow);
      }
      else if (hint == 'g'){
        System.out.print("\t" + green);
      }
    wait(500);
    }
  }

  // Print what turn the user is on, ask for their next guess
  public static void nextPrompt(int turn, int turnCap, String diff){
    System.out.print("Guess ");
    if (diff.equals("hidden")){
      System.out.print("?");
    }
    else {
      System.out.print(turn);
    }
    System.out.print("/" + turnCap + ": ");
  }

  public static void clear(){
    /* Credit: https://stackoverflow.com/questions/2979383/
     * how-to-clear-the-console */
    System.out.print("\033[H\033[2J");  
    //System.out.flush();  
  }

  public static void antiCheatMsg(String word, String key){
    if (key.equals("length")){
      System.out.println(red + " ALERT: The word " + word + " is not 5 letters " + red);
    }
    else if (key.equals("invalid")){
      System.out.println(red + " ALERT: The word " + word + " is invalid " + red);      
    }
  }

  /* UNUSED METHOD
  public static void loadBar(String message, int icons){
     * Credit: Thanks to Mr. Jorgens for the original concept
     * of a "modulus animation". 
     * Three loops, three variables: i, j, k. *
    for (int i=0; i<=icons; i++){
      String string1 = "";
      String string2 = "";
      int j = 0;
      int k = 0;

      for (j=0; j<=i; j++){
        string1 = string1 + green;
      }

      for (k=0; k<(icons-i); k++){
        string2 = string2 + black;
      }

      System.out.print(message + yellow + string1 + string2 + yellow + "\r");

      wait(100);
    }
  }
  */
}