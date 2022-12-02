// Ethan S
// Quarter 3 Project - Wordle ("Wrdle")

/* Play Wordle in Java through Wrdle! 
 * Features word fetching from the NYT website
 * and a special hard mode called Memory mode. */

// Imports for Main
import java.util.Scanner;
import java.util.Random;

class Main {
  public static void main(String[] args) {
    // Utilities
    Scanner scan = new Scanner(System.in);
    Random rng = new Random();

    // Game variables
    boolean antiCheat = true;    
    String playing = "play";
    String difficulty = "normal";
    String input = "";
    String hint = "";
    int turn = 0;
    int turnCap = 6;
    boolean replay = true;
    
    // Important Objects
    /* Iconer (Static) */
    Websiter webMain;
    Websiter webJSA;
    Websiter webJSV;
    WordBank wordBank;
    WordHistory wordHistory = new WordHistory();

    // Introductory Message
    Iconer.intro();
    System.out.println("Welcome to Wrdle! \n");

    try {
      System.out.println("Attempting to access official Wordle list...");

      // Make a websiter for the main Wordle (Game) page
      webMain = new Websiter("https://www.nytimes.com/games/wordle/index.html");
      webMain.fetch(); // Fetch contents
      webMain.trim("FIND_JS"); // Trim to just javascript link
      System.out.println("File found: " + 
                        webMain.getContents()); // Status message

      // Make a websiter for the javascript page, answers
      webJSA = new Websiter(webMain.getContents());
      webJSA.fetch(); // Fetch contents
      webJSA.trim("FIND_LIST_ANS"); // Trim to just answer string
      //System.out.println(webJSA.getContents()); //<DEBUG>

      // Make a websiter for the javascript page, valids
      webJSV = new Websiter(webMain.getContents());
      webJSV.fetch(); // Fetch contents
      webJSV.trim("FIND_LIST_VAL"); // Trim to just valids string
      //System.out.println(webJSV.getContents()); //<DEBUG>

      // Construct word bank, using split to make an ArrayList
      wordBank = new WordBank(webJSA.split(), webJSV.split());
      System.out.println("Successfully fetched " +  
                         "word lists.\n"); // Status message
      
      //throw new Exception(); //<DEBUG>
    }
    catch (Exception ex) {
        System.out.println(ex);
      System.out.println("Failed to access official Wordle list. Using backup list...\n");
      wordBank = new WordBank("backup");
    }

    while (replay){
      System.out.println("Selecting word...");
      wordBank.select(rng.nextInt(wordBank.getSolutions().size()));
      //wordBank.select(0); //<DEBUG>
      //System.out.println(wordBank.getWrdle()); //<DEBUG>
      System.out.println("Word selected.");
      
      System.out.print("\nHow many attempts do you want? ");
      System.out.print("(Default 6) ");
      turnCap = 6;
      try {
        turnCap = Integer.parseInt(scan.nextLine());
        if (turnCap < 1){
          System.out.println("Note: You have infinite attempts.");
        }
      }
      catch (Exception ex) {
        System.out.println("Using default.");
      }
      
      System.out.print("\nGame is Ready! Press Enter to Start, ");
      System.out.print("or type \"hidden\" to play hard mode: ");
      if (scan.nextLine().equals("hidden")){
        difficulty = "hidden";
      }
      
      Iconer.clear();
  
      // Game loop
      while (playing.equals("play")){
        turn++;
  
        // Show field
        Iconer.fieldIcons(wordHistory.getGuesses(),
                          wordHistory.getHints(),
                          difficulty);
  
        // Get valid guess from user
        while (antiCheat){
          Iconer.nextPrompt(turn, turnCap, difficulty);
          input = scan.next();
          antiCheat = wordBank.antiCheat(input);
        }
        antiCheat = true;
  
        // Add guesses and evaluated hint to wordHistory
        wordHistory.addGuess(input.toUpperCase());
        hint = wordBank.evaluate(input);
        wordHistory.addHint(hint);
  
        // Loop (game) ending checks
        if (hint.equals("ggggg")){
          playing = "win";
        }
        else if (turn == turnCap){
          playing = "lose";
        }
  
        // Show this guess's hints
        Iconer.revealIcons(wordHistory.getGuesses(),
                           wordHistory.getHints());
        //scan.next(); //<DEBUG>
        Iconer.clear();
      }
  
      // End screens
      Iconer.outro(playing, wordBank.getWrdle());
      Iconer.fieldIcons(wordHistory.getGuesses(),
                        wordHistory.getHints(),
                       "normal");
      Iconer.fieldIcons(wordHistory.getGuesses(),
                        wordHistory.getHints(),
                       "condensed");
        
      System.out.print("\nAgain? Enter \"Y\" or \"N\": ");
      if (!scan.next().equalsIgnoreCase("Y")){
        replay = false;
        scan.nextLine();
      }
      Iconer.clear();
    }
    
    // Finish
    scan.close(); 
    System.out.println("Thanks for playing!");
  }
}
//https://www.nytimes.com/games/wordle/index.html
//https://www.nytimes.com/games/wordle/main.4d41d2be.js
//https://www.nytimes.com/games-assets/v2/wordle.c60d012dfdc21f4504811e6fde33862ad9c7d05d.js