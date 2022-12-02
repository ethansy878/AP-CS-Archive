// Word Bank stores the solutions and valids lists and can perform operations on them.

import java.util.ArrayList;

class WordBank {
  private ArrayList<String> solutions = new ArrayList<String>();
  private ArrayList<String> valids = new ArrayList<String>();
  private boolean validListExists = false;
  private String wrdle = "";

  /* Overloaded constructor (x2) 
   * If the websiter was successful, passes ArrayLists 
   * If not, passes a string to indicate using backup list */
  public WordBank(ArrayList<String> list1, ArrayList<String> list2) {
    for (String each : list1){
      solutions.add(each);
    }
    for (String each : list2){
      valids.add(each);
    }
    validListExists = true;
    //System.out.println(solutions + "SOLUTIONS");
    //System.out.println(valids + "VALIDS"); //<DEBUG>
  }
  public WordBank(String backup) {
    backup();
  }

  // Backup list
  private void backup() {
    solutions.add("audio");
    solutions.add("feast");
    solutions.add("ghoul");
    solutions.add("whole");
    solutions.add("house");
    solutions.add("plant");
    solutions.add("sheet");
    solutions.add("yours");
    solutions.add("trick");
    solutions.add("kicks");
    solutions.add("zebra");
    solutions.add("axles");
    solutions.add("hunch");
    solutions.add("mouse");
    solutions.add("tarps");
    solutions.add("flick");
    solutions.add("great");
    solutions.add("under");
    solutions.add("novel");
    solutions.add("rusty");
    solutions.add("lousy");
    validListExists = false;
  }

  // Get methods
  public ArrayList<String> getSolutions() {
    return solutions;
  }

  public String getWrdle() {
    return wrdle;
  }

  // Set method
  public void select(int rng) {
    wrdle = solutions.get(rng);
  }

  // Evaluate - create the hint string by comparing chars
  public String evaluate(String guess) {
    StringBuilder hint = new StringBuilder();
    for (int i=0; i<guess.length(); i++){
      hint.append("x");
    }
    
    StringBuilder answr = new StringBuilder();
    answr.append(wrdle);
    /* A StringBuilder is necessary for "replace-elimination"
     * that prevents incorrect/duplicate hints;
     * greens become '.', yellows become ',' in the builder.
     * - "wrdle" is the class var holding the answer. 
     * - "answr" is the method var holding a StringBuilder. */
      
    char guessChar;
    char answrChar;

    // Greens first - chars are matching at same spot
    for (int i = 0; i < guess.length(); i++) {
      guessChar = guess.charAt(i);
      answrChar = answr.charAt(i);

      if (guessChar == answrChar) {
        hint.setCharAt(i, 'g');
        answr.setCharAt(i, '.');
      }
    }

    // Yellows next - chars in wrdle, not same spot
    // Black last - char not in wrdle
    for (int i = 0; i < guess.length(); i++) {
      guessChar = guess.charAt(i);
      answrChar = answr.charAt(i);

      //System.out.println(answr + " "); //<DEBUG>
      //System.out.println(guess + " "); //<DEBUG>
      //System.out.print(guessChar + " "); //<DEBUG>

      if (answrChar == '.'){
        // Do nothing
        //System.out.println("SKIPPED (GREEN)"); //<DEBUG>
      }
      else if (answr.toString().contains(Character.toString(guessChar))) {
        hint.setCharAt(i, 'y');
        answr.setCharAt(answr.toString().indexOf(guessChar), ',');
        //System.out.println("YELLOWED"); //<DEBUG>
      }
      else {
        hint.setCharAt(i, 'b');
        //System.out.println("BLACKED"); //<DEBUG>
      }
    }
    //System.out.println(hint); //<DEBUG>
    return hint.toString();
  }

  // Anti Cheat - make sure a guess is valid
  public boolean antiCheat(String guess) {

    guess = guess.toLowerCase();
      
    // Must be a five letter word
    if (!(guess.length() == 5)) {
      Iconer.antiCheatMsg(guess, "length");
      return true;
    }

    // Version A: Check against list
    if (validListExists){
      if (!(valids.contains(guess) ||
      solutions.contains(guess))){
        Iconer.antiCheatMsg(guess, "invalid");
        return true;
      }
    }

    // Version B: No 4 letter combinations
    else {
      for (int i = 0; i < guess.length(); i++) {
        char checkedChar = guess.charAt(i);
        // System.out.println("CHAR " + checkedChar); //<DEBUG>
        int matches = 0;
  
        for (int c = guess.indexOf(checkedChar) + 1; c < guess.length(); c++) {
          if (checkedChar == guess.charAt(c)) {
            matches++;
            //System.out.println("MATCH! " + matches); //<DEBUG>
          }
          //System.out.println("LOOPED"); //<DEBUG>
        }
        //System.out.println("MATCH COUNT " + matches); //<DEBUG>
        if (matches >= 3) {
          Iconer.antiCheatMsg(guess, "invalid");
          return true;
        }
      }
    } 
    return false;
  }
}