// Ethan S
// Nim Project

import java.util.Scanner;
import java.util.Random;

class Main {
  // MAIN METHOD
  public static void main(String[] args) throws Exception {
    // Scanner
    Scanner scan = new Scanner(System.in);

    // "Flow" variables
    String response = "";
    String responseLetter = "";
    String responseNumber = "";
    String winCondition = "no";
    boolean proceed = false;
    boolean validAns = false;
    boolean com = false;
    int turn = 0;

    // "Counter" variables - this program uses "sticks"
    int sticksA = 1;
    int sticksB = 3;
    int sticksC = 5;
    int sticksD = 7;

    // Introduction
    System.out.println("Welcome to Nim Revamped.");

    // Player setup (Solo vs. Multi)
    // Loop until it gets a good response.
    while (proceed == false){
      System.out.print("Choose: \"solo\" (vs. a computer) or \"multi\" (vs. a friend) mode? ");
      response = scan.next();

      // Response checker, || means 'or'
      if (response.equalsIgnoreCase("solo") || response.equalsIgnoreCase("multi")){
        proceed = true;
      }
      else {
        System.out.println("Invalid response, try again");
      }

      // If valid, change the boolean that activates computer player
      if (proceed == true){
        if (response.equalsIgnoreCase("solo")){
          com = true;
        }
      }
    }
    proceed = false;

    // Stick setup
    // Loop until it gets a good response.
    while (proceed == false){
      System.out.print("Choose: \"standard\" or \"random\" game? ");
      response = scan.next();

      // Response checker, || means 'or'
      if (response.equalsIgnoreCase("standard") || response.equalsIgnoreCase("random")){
        proceed = true;
      }
      else {
        System.out.println("Invalid response, try again");
      }
      
      // If valid, set up the sticks using stick_setup function
      if (proceed == true){
        sticksA = stick_setup(response, "A");
        sticksB = stick_setup(response, "B");
        sticksC = stick_setup(response, "C");
        sticksD = stick_setup(response, "D");
        System.out.println("Finished Loading! Good Luck!");
      }
    }
    proceed = false;

    // Actual game loop
    // Keep going until a "win condition" is detected, then break.
    while (proceed == false){
      display_board(sticksA, sticksB, sticksC, sticksD);
      
      // Prompts
      if (turn == 0){ // First turn
        System.out.print("This is the first turn, Player 1. You have an option. \nEnter \"pass\" to skip this turn only OR \nEnter a row and amount to take. Sample responses: \"a1\", \"b3\", \"d5\", etc\nYour move: ");
        response = scan.next();
      }
      else { // Other turns
        if (com == true && (2 == ((turn % 2) + 1))){ // AI Player
        System.out.print("[COM] Player 2's turn: ");
        response = com_move(sticksA, sticksB, sticksC, sticksD);
        System.out.println(response);
        }
        else { // Regular Player
          System.out.print("Player " + ((turn % 2) + 1) + "'s turn.\nEnter a row and amount to take: ");
          response = scan.next();
        }
      }
    
      // Use anti_cheat method (line 217) to validate response. First turn gets a special scenario.
      if (turn == 0){
        if (response.equalsIgnoreCase("pass")){
          System.out.println("Turn passed.");
          turn++;
        }
        else {
          validAns = anti_cheat(response, sticksA, sticksB, sticksC, sticksD);
        }
      }
      else {
        validAns = anti_cheat(response, sticksA, sticksB, sticksC, sticksD);
      }

      // Valid Answer means we can manipulate the strings and change the board. Otherwise do nothing.
      if (validAns == true){
        responseLetter = (response.substring(0,1)).toUpperCase();
        responseNumber = response.substring(1,2);

        if (responseLetter.equalsIgnoreCase("A"))
          sticksA -= Integer.parseInt(responseNumber);
        else if (responseLetter.equalsIgnoreCase("B"))
          sticksB -= Integer.parseInt(responseNumber);
        else if (responseLetter.equalsIgnoreCase("C"))
          sticksC -= Integer.parseInt(responseNumber);
        else if (responseLetter.equalsIgnoreCase("D"))
          sticksD -= Integer.parseInt(responseNumber);
      }

      // Check for a win condition: exactly 1 (Winner scenario) or 0 (Loser scenario) left.
      winCondition = win_check(sticksA, sticksB, sticksC, sticksD);

      // If it returns a proper win condition, change the proceed variable so the loop can end.
      if (winCondition.equals("win") || winCondition.equals("lose"))
        proceed = true;

      // Only increment the turn when a valid answer is given. Reset the variable to prepare it for the next turn.
      if (validAns == true)
        turn++;
        //System.out.println(""); // More space between "turns"
      validAns = false;
    }
    
    // Out of the loop means game over. Display the board one last time and determine the winner!
    display_board(sticksA, sticksB, sticksC, sticksD);
    if (winCondition.equals("win")){
      System.out.println("One stick remains. GAME OVER! \nPlayer " + (turn % 2 + 1) + " has to take the last stick, so the winner is... Player " + Math.abs((turn % 2) - 2) + "!");
    }
    else if (winCondition.equals("lose")){
      System.out.println("\nThe board is empty. GAME OVER! \nPlayer " + Math.abs((turn % 2) - 2) + " made a terrible mistake, so the winner is... Player " + ((turn % 2) + 1) + "!");
    }
    scan.close(); // All done.
  }

  // FUNCTION - simulate loading, only used in function below
  public static void loading(String row) throws Exception {
    for (int i=0; i<4; i++){
      if (i==0)
        System.out.print("Loading Row " + row + "... /\r");
      if (i==1)
        System.out.print("Loading Row " + row + "... |\r");
      if (i==2)
        System.out.print("Loading Row " + row + "... \\\r");
      if (i==3)
        System.out.print("Loading Row " + row + "... -\r");
      Thread.sleep(250);
    }
  }

  // FUNCTION - determines the amount of sticks in each row
  public static int stick_setup(String response, String row) throws Exception {
    Random r = new Random();
    int stickCount = 0;

    if (response.equalsIgnoreCase("standard")){
      if (row.equals("A"))
        stickCount = 1;
      if (row.equals("B"))
        stickCount = 3;
      if (row.equals("C"))
        stickCount = 5;
      if (row.equals("D"))
        stickCount = 7;
    }
    
    else if (response.equalsIgnoreCase("random")){
      loading(row);
      stickCount = 1 + r.nextInt(6);
    }
    return stickCount;
  }

  // FUNCTION - print the playing field
  public static void display_board(int A, int B, int C, int D) {
    System.out.println();
    System.out.print("A | ");
    for (int i=0; i!=A; i++){
      System.out.print("/");
    }
    System.out.print("\nB | ");
    for (int i=0; i!=B; i++){
      System.out.print("/");
    }    
    System.out.print("\nC | ");
    for (int i=0; i!=C; i++){
      System.out.print("/");
    }
    System.out.print("\nD | ");
    for (int i=0; i!=D; i++){
      System.out.print("/");
    }
    System.out.println();
  }

  // FUNCTION - ensure only valid answers make changes to the playing field
  public static boolean anti_cheat(String response, int A, int B, int C, int D) {
    // Five tests
    boolean testLength = false;
    boolean testCategory = false;
    boolean testPositive = false;
    boolean testContains = false;
    boolean testEnough = false;

    String responseLetter = "";
    String responseNumber = "";

    // TEST ONE - Is your response at least 2 characters long?
    // Pass this to do the "string picking" 
    if (response.length() == 2){
      testLength = true;
      responseLetter = (response.substring(0,1)).toUpperCase();
      responseNumber = response.substring(1,2);
      /* DEBUG System.out.println(responseLetter);
      System.out.println(responseNumber); */
    }

    // TEST TWO and THREE - Did you pick row A, B, C, or D, and is your number positive?
    // These tests ONLY run if #1 passes to prevent out of bounds errors.
    if (testLength == true){
      if (responseLetter.equals("A") || responseLetter.equals("B") || responseLetter.equals("C") || responseLetter.equals("D")){
        testCategory = true;
      }

      if (responseNumber.equals("1") || responseNumber.equals("2") || responseNumber.equals("3") || responseNumber.equals("4") || responseNumber.equals("5") || responseNumber.equals("6") || responseNumber.equals("7") || responseNumber.equals("8") || responseNumber.equals("9")){
        testPositive = true;
      }
    }
    
    // TEST FOUR and FIVE - Does your pile contain + have enough?
    // These tests ONLY run if #2 and #3 pass to prevent parsing errors.
    if (testCategory == true && testPositive == true){
      if (responseLetter.equals("A")){
        if (A != 0)
          testContains = true;
        if (Integer.parseInt(responseNumber) <= A)
          testEnough = true;
      }
      else if (responseLetter.equals("B")){
        if (B != 0)
          testContains = true;
        if (Integer.parseInt(responseNumber) <= B)
          testEnough = true;
      }
      else if (responseLetter.equals("C")){
        if (C != 0)
          testContains = true;
        if (Integer.parseInt(responseNumber) <= C)
          testEnough = true;
      }
      else if (responseLetter.equals("D")){
        if (D != 0)
          testContains = true;
        if (Integer.parseInt(responseNumber) <= D)
          testEnough = true;
      }
    }
  
    // Error messages
    if (testLength == false)
      System.out.println("ERROR: Invalid answer length. Must be exactly 2 characters. Try again.");
    else if (testCategory == false)
      System.out.println("ERROR: Invalid Row. Try again.");
    else if (testPositive == false)
      System.out.println("ERROR: Unacceptable Number. Try again.");
    else if (testContains == false)
      System.out.println("ERROR: This row is empty. Try again.");
    else if (testEnough == false)
      System.out.println("ERROR: You're taking too many. Try again.");

    /* DEBUG System.out.println(testLength);
    System.out.println(testCategory);
    System.out.println(testPositive);
    System.out.println(testContains);
    System.out.println(testEnough); */

    // Return a final verdict; is the answer valid or not?? 
    return (testLength && testCategory && testPositive && testContains && testEnough);
  }

  // FUNCTION - when there is exactly 1 or 0 sticks left, return a boolean that ends the game loop
  public static String win_check(int A, int B, int C, int D) {
    String condition = "";
    if ((A + B + C + D) == 1)
      condition = "win";
    else if ((A + B + C + D) == 0)
      condition = "lose";
    return condition;
  }

  // FUNCTION - Computer's move if the user is playing "solo", this bot plays completely randomly and can even throw the game (lose on purpose)!
  public static String com_move(int A, int B, int C, int D) {
    Random r = new Random();
    int rng = 0;
    String letter = "false";
    String amount = "false";
    String finalString = "";

    while (letter == "false"){
      rng = 1 + r.nextInt(4);
      if (rng == 1 && A >= 1)
        letter = "a";
      if (rng == 2 && B >= 1)
        letter = "b";
      if (rng == 3 && C >= 1)
        letter = "c";
      if (rng == 4 && D >= 1)
        letter = "d";
    }

    while (amount == "false"){
      if (letter.equals("a")){
        rng = 1 + r.nextInt((A));
        amount = Integer.toString(rng);
      }
      else if (letter.equals("b")){
        rng = 1 + r.nextInt((B));
        amount = Integer.toString(rng);
      }
      else if (letter.equals("c")){
        rng = 1 + r.nextInt((C));
        amount = Integer.toString(rng);
      }
      else if (letter.equals("d")){
        rng = 1 + r.nextInt((D));
        amount = Integer.toString(rng);
      }
    }

    finalString = (letter + amount);
    return finalString; 
  }
}