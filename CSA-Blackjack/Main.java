// Ethan S
// Blackjack Project

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    Random r = new Random();

    // Card management variables
    ArrayList<Integer> deck = new ArrayList<Integer>();
    ArrayList<Integer> p1 = new ArrayList<Integer>();
    ArrayList<Integer> p2 = new ArrayList<Integer>();
    int card = 0;

    // Game variables
    String input = "null";
    String choice = "null";
    String winner = "null";
    boolean playing = true;
    boolean dealing = true;
    int turn = 0;
    int money = 100;
    int bet = 0;

    // Introduction
    System.out.println("WELCOME TO THE JACKBLACK CASINO!");
    System.out.println("You have a chance to turn $100 into a fortune. GOOD LUCK\n");
    System.out.println("Press Enter to Continue... ");
    scan.nextLine();

    while (playing){
      // [ANNOUNCE TURN]
      turn++;
      System.out.println("Round " + turn + "!");

      // [CLEAR CARDS]
      p1.clear();
      p2.clear();

      // [SHUFFLE IF NEEDED and STATE DECK SIZE]
      if (turn == 1) {
        loading_bar("\033[3mShuffling\033[0m", 16);
        deck = shuffle();
      }
      else if (deck.size() <= 10) {
        loading_bar("\033[3mShuffling\033[0m", 16);
        deck = shuffle();
      }
      System.out.println("There are " + deck.size() + " cards in the deck.");
      //<DEBUG> System.out.println(deck);

      // [PLACE BETS]
      System.out.println("Place your bet! You have $" + money + ".");
      bet = bet_query(money); // method
      clear_screen(); 
        
      if (bet == money) // extra bonus statement
        System.out.println("ALL IN BABY");

      // [INITIAL DEAL]
      // Player Card One
      card = r.nextInt(deck.size());
      p1.add(deck.get(card));
      deck.remove(card);

      // Player Card Two
      card = r.nextInt(deck.size());
      p1.add(deck.get(card));
      deck.remove(card);

      // Dealer Card One
      card = r.nextInt(deck.size());
      p2.add(deck.get(card));
      deck.remove(card);
      // Dealer Card Two is a ???, do it later.

      // [PLAYER TURN]
      while (dealing){
        game_display(bet, p1, p2, true);

        if (get_total(p1).contains("Bust")){
          dealing = false;
          System.out.println("Player has busted! Too bad.\nPress Enter to Continue... ");
          scan.nextLine();
        }
        else if (get_total(p1).contains("BLACKJACK!")){
          dealing = false;
          System.out.println("Press Enter to Continue... ");
          scan.nextLine();
        }
        else {
          choice = card_query("Player, \"hit\", \"stand\", or \"double down\"? "); // method
          switch (choice){
            case "H":
              loading_bar("\033[3mHitting\033[0m", 20);
              card = r.nextInt(deck.size());
              p1.add(deck.get(card));
              deck.remove(card);
              break; // stop here so loop again
            case "D": // flow through for the other cases
              loading_bar("\033[3mDouble Down!\033[0m", 15);
              card = r.nextInt(deck.size());
              p1.add(deck.get(card));
              deck.remove(card);
              bet = bet * 2;
            case "S":
              dealing = false;
            default:
              dealing = false;
              break;
          }
        }
        clear_screen();
      }
      dealing = true; // ready for the next loop

      // One time game display and loading bar for mystery card
      game_display(bet, p1, p2, true);
      loading_bar("\033[3mRevealing\033[0m", 18); // special ASCII italics
      clear_screen();
      // NOTE: REPLIT SUPPORTS ITALICS, MAY HAVE COMPATIBILITY ISSUES ELSEWHERE
      
      // Generate Mystery Dealer Card
      card = r.nextInt(deck.size());
      p2.add(deck.get(card));
      deck.remove(card);

      // [DEALER TURN]
      while (dealing){
        game_display(bet, p1, p2, false);

        if (get_total(p2).contains("Bust")){
          dealing = false;
          System.out.println("Dealer has busted. \nPress Enter to Continue... ");
          scan.nextLine();
        }
        else if (get_total(p2).contains("BLACKJACK!")){
          dealing = false;
          System.out.println("Dealer got a Blackjack. \nPress Enter to Continue... ");
          scan.nextLine();
        }
        else { // Dealer AI
          choice = get_total(p2); // get the total to work with
          if (choice.contains("20") || choice.contains("19") || choice.contains("18") || choice.contains("17")){ // dealer stand when the total is 20, 19, 18, 17
            dealing = false;
          }
          // dealer hits otherwise, with the loading bar
          else {
            loading_bar("\033[3mHitting\033[0m", 20);
            card = r.nextInt(deck.size());
            p2.add(deck.get(card));
            deck.remove(card);
          }
        }
        clear_screen();
      }
      dealing = true; // ready for the next loop

      // [RESULTS]
      game_display(bet, p1, p2, false);
      winner = winner_evaluate(p1, p2);
      
      if (winner.equals("blackjack")){
        bet = (bet * 3) / 2;
        System.out.println("Player Natural Blackjack! (150% Payout) +$" + bet);
        money += bet;
      }
      else if (winner.equals("player")){
        System.out.println("Player Wins! +$" + bet);
        money += bet;
      }
      else if (winner.equals("dealer")){
        System.out.println("Dealer Wins! -$" + bet);
        money -= bet;
      }
      else {
        System.out.println("Push (Tie).");
      }
      System.out.println("Your Money: $" + money);

      if (money < 1) // stop playing if out of money
        playing = false;
      
      if (playing){ // continue or exit? (don't do this if out of money)
        System.out.println("Press Enter for the next hand, or type 'quit' to quit... ");
        input = scan.nextLine();
        if (input.equalsIgnoreCase("quit"))
          playing = false;
        clear_screen();
      }
    }

    // [CONCLUSION]
    if (money == 0)
      System.out.println("\nGAME OVER. \nYou are OUT OF MONEY...");
    else if (money < 0)
      System.out.println("\nGAME OVER. \nYou are BANKRUPT and IN DEBT... \"congrats\"");
    else
      System.out.println("You walk out of the Jackblack Casino with $" + money + ". GG");
    
    // close scanner
    scan.close();
  }

  // [BEGIN FUNCTIONS/METHODS] - there are eleven of them
  public static String card_letter(int num){
    //FUNCTION: Show the card to the user (for face cards)
    switch (num) {
      case 1:
        return "A";
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        return Integer.toString(num);
    }
  }

  public static int card_value(int num){
    //FUNCTION: Determine what to add to the total. Aces are initially high (11), all faces are 10. Ace management is in next function
    switch (num) {
      case 1:
        return 11;
      case 11:
        return 10;
      case 12:
        return 10;
      case 13:
        return 10;
      default:
        return num;
    }
  }

  public static int sum_cards(ArrayList<Integer> list){
    //FUNCTION: Take in a list of integers (a player's hand) and return the sum of all the numbers.
    //Uses: card_value()
    int total = 0;
    int aces = 0;

    for (int i=0; i<list.size(); i++){
      // Standard adding to total varaible
      total += card_value(list.get(i));
      //<DEBUG> System.out.println("*Summing... id" + list.get(i) + " is " + card_letter(list.get(i)) + " is +" + card_value(list.get(i)) + " = " + total);

      // Aces system
      if (list.get(i) == 1){
        aces++;
        //<DEBUG> System.out.println("*ACE FOUND: x" + aces);
      }
      if (total > 21 && aces > 0){
        total -= 10;
        aces -= 1;
        //<DEBUG> System.out.println("*USING ACE, remaining x" + aces);
      }
    }
    return total;
  }

  public static String get_hand(ArrayList<Integer> list){
    //FUNCTION: Return a string that's a visual representation of the person's hand (ArrayList of #s)
    //Uses: card_letter()
    String displayString = "";
    for (int i=0; i<list.size(); i++){
      displayString = displayString +  "[" + card_letter(list.get(i)) + "] ";
    }
    return displayString;
  }

  public static String get_total(ArrayList<Integer> list){
    //FUNCTION: Return a string of the value of the cards, with two special cases - 21 = " <star emoji> BLACKJACK!" and over 21 = " <fire emoji> Bust"
    //Uses: sum_cards()
    int total = sum_cards(list);
    if (total == 21)
      return ("⭐ BLACKJACK! ⭐");
    else if (total > 21)
      return ("🔥 Bust 🔥");
    else
      return (Integer.toString(total));
  }

  public static void clear_screen(){  
    //FUNCTION: Literally just clear the screen. THANK YOU: https://stackoverflow.com/questions/2979383/how-to-clear-the-console
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
  }

  public static int bet_query(int money){
    //FUNCTION: The betting procedure. Take in the player's current money for reference. Ask for a valid bet, either ask again or return the bet depending on response validity.
    Scanner scan = new Scanner(System.in);
    int bet = 0;

    if (money == -1)
      scan.close(); // tricks compiler
    
    while (true){
      try {
        bet = scan.nextInt();
      }
      catch (Exception ex) {
        System.out.println("bad bet. try again");
        scan.nextLine();
        continue;
      }

      if (bet > money){
        System.out.println("don't bet more than you have fool. try again");
      }
      else if (bet <= 0){
        System.out.println("don't try to be sly with your zero or a negative number. try again");
      }
      else {
        return bet;
      }
    }
  }

  public static String card_query(String question){
    //FUNCTION: The dealing procedure. Ask the user what they want to do on their turn, and return a character that determine what happens in the main method
    Scanner scan = new Scanner(System.in);
    String input = "null";

    if (question.equals("CLOSE"))
      scan.close(); // tricks compiler

    while (true) {
      System.out.println(question);
      input = scan.nextLine();

      if (input.equalsIgnoreCase("hit")){
        return "H";
      }
      else if (input.equalsIgnoreCase("stand")){
        return "S";
      }
      else if (input.equalsIgnoreCase("double down")){
        return "D";
      }
      else
        System.out.println("Invalid input, try again");
    }
  }

  public static void game_display(int bet, ArrayList<Integer> p1, ArrayList<Integer> p2, boolean mystery){
    //FUNCTION: 6 (+1) print statements that displayes the full blackjack game/table/dealing
    System.out.println("$" + bet + " is at stake...");
    System.out.println("♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦\n");
    System.out.println("Player Hand: " + get_hand(p1));  
    System.out.println("Total: " + get_total(p1) + "\n");
    System.out.print("Dealer Hand: " + get_hand(p2));  
    if (mystery == true){
      System.out.println("[???]");
    }
    else {
      System.out.println();
    }
    System.out.println("Total: " + get_total(p2) + "\n");
    System.out.println("♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦ ♠ ♥ ♣ ♦");
  }

  public static String winner_evaluate(ArrayList<Integer> p1, ArrayList<Integer> p2){
    //FUNCTION: Pass the lists, sum up the 2 totals, evaluate the winner, and return a corresponding string. More efficent than previous version (sum_cards in each if statement)
    int total1 = sum_cards(p1);
    int total2 = sum_cards(p2);

    if ((total1 == total2) && (total1 <= 21 && total2 <= 21)){ // test: p == d = push cards are under 21. DO THIS FIRST!
      return "push";
    }
    else if ((total1 == 21) && (p1.size() == 2)){ // test: p == 21 on first deal = INSTANT 150% WIN
      return "blackjack";
    }
    else if (total1 > 21){ // test: player bust = dealer win
      return "dealer";
    }
    else if (total2 > 21){ // test: dealer bust = player win
      return "player";
    }
    else if (total1 > total2){ // test: p > d = player win
      return "player";
    }
    else if (total1 < total2){ // test: d > p = dealer win
      return "dealer";
    }
    else {
      return "push"; // something went horribly wrong, null/void the round
    }
  }
  
  public static void loading_bar(String message, int icons){
    //FUNCTION: Create (and finish) a loading bar of filled/unfilled diamonds that symbolize the dealer's turn.
    //Thanks Mr. Jorgens for the original "modulus animation" code concept.
    for (int i=0; i<=icons; i++){
      String string1 = "";
      String string2 = "";
      int j = 0;
      int g = 0;

      for (j=0; j<=i; j++){
        string1 = string1 + "◆";
      }

      for (g=0; g<=(icons-i); g++){
        string2 = string2 + "◇";
      }

      System.out.print(message + " [" + string1 + string2 + "]\r");

      // safe wait command
      try {
        Thread.sleep(50); // Wait command
      }
      catch (Exception ex) {
        System.out.print("");
      }
    }
  }

  public static ArrayList<Integer> shuffle(){
    //FUNCTION: Create a new deck of cards by adding 1,1,1,1,2,2,2,2 ... 13,13,13,13 to an ArrayList via for loop. Return that list to be used in main method.
    ArrayList<Integer> newdeck = new ArrayList<Integer>();
    for (int i=4; i<56; i++){
      newdeck.add(i/4); // integer truncation for the win
    }
    return newdeck;
  }
}