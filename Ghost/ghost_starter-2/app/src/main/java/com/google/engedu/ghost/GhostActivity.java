package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.Random;
import android.view.KeyEvent;


public class GhostActivity extends Activity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    SimpleDictionary simpleDictionary;
    FastDictionary fastDictionary;
    String wordFragment="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        try {
            InputStream is=getAssets().open("words.txt");
            fastDictionary =new FastDictionary(is);
            //simpleDictionary=new SimpleDictionary(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView txtInput=(TextView)findViewById(R.id.ghostText);
        Button btnChallenge=(Button)findViewById(R.id.btnChallenge);
        // Do computer turn stuff then make it the user's turn again
        if (wordFragment.length() >= 4 && fastDictionary.isWord(wordFragment)) {
            txtInput.setText(wordFragment);
            Log.d("test", "Computer Wins");
            label.setText(wordFragment + " is a valid word. You Lose");
            btnChallenge.setEnabled(false);
            return;
        } else{
            Log.d("Test", "getting a word starting with "+wordFragment);
            String longerWord = fastDictionary.getGoodWordStartingWith(wordFragment);
            if (longerWord!=null) {
                Log.d("Test","longerWord word beginning with "+wordFragment+" is: "+longerWord);

                String letter = longerWord.substring(wordFragment.length(), wordFragment.length() + 1);
                wordFragment = wordFragment + letter;
                txtInput.setText(wordFragment);
                Log.d("Test", "New word fragment: " + wordFragment);
                 userTurn = true;
                label.setText(USER_TURN);

            } else {
                Log.d("Test", "No word can be formed with "+wordFragment);

                txtInput.setText(wordFragment);
                label.setText("Computer Wins!!!");
                Log.d("key", "Computer Wins!");
                label.setText("No word can be formed with "+wordFragment+". You Lose!");
                txtInput.setEnabled(false);
                btnChallenge.setEnabled(false);
            }

        }



    }

    /**
     * Do stuff when user presses a key
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        TextView txtResult=(TextView)findViewById(R.id.gameStatus);

        char c = (char) event.getUnicodeChar();
        if(Character.isLetter(c)) {
            wordFragment=wordFragment+Character.toString(c);
            Log.d("Test","word fragment: "+wordFragment);
            //if(fastDictionary.isWord(wordFragment)){
              //  txtResult.setText(wordFragment+" is a valid word.");
                //Log.d("Test","valid word");
            //}
            computerTurn();
            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }
    }
    public void challengeHandler(View view){
        TextView txtStatus=(TextView)findViewById(R.id.gameStatus);
        TextView txtInput=(TextView)findViewById(R.id.ghostText);
        Button btnChallenge=(Button)findViewById(R.id.btnChallenge);
        Log.d("tag", wordFragment);
        if(!wordFragment.equals("")) {
            if (wordFragment.length() >= 4 && fastDictionary.isWord(wordFragment)) {
                txtStatus.setText("Congratulations, You Win! " + wordFragment + " is a real word");
                return;
            }
            String longWord = fastDictionary.getGoodWordStartingWith(wordFragment);
            if (longWord != null) {
                txtStatus.setText("You Lose! A possible word is " + longWord);
            } else {
                txtStatus.setText("Congratulations! You Win");
            }
            txtInput.setEnabled(false);
            btnChallenge.setEnabled(false);

        }
    }
    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        Button btnChallenge=(Button)findViewById(R.id.btnChallenge);
        wordFragment="";
        btnChallenge.setEnabled(true);
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        text.setEnabled(true);
        TextView label = (TextView) findViewById(R.id.gameStatus);
        //if (userTurn) {
            label.setText(USER_TURN);
        //} else {
        //    label.setText(COMPUTER_TURN);
        //    computerTurn();
        //}
        return true;
    }
}
