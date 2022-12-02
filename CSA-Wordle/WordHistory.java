// Word History stores current guesses and hints.

import java.util.ArrayList;

class WordHistory {
  private ArrayList<String> guesses = new ArrayList<String>();
  private ArrayList<String> hints = new ArrayList<String>();

  // Get methods
  public ArrayList<String> getGuesses(){
    return guesses;
  }
  public ArrayList<String> getHints(){
    return hints;
  }

  // Set methods
  public void addGuess(String g){
    guesses.add(g);
  }
  public void addHint(String h){
    hints.add(h);
  }
}