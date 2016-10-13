package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;
    HashMap root;
    public TrieNode() {
        children = new HashMap<>();
        root=children;
        isWord = false;
    }

    public void add(String s) {
        children=root;
        TrieNode t=null;
        for(int i=0; i<s.length();i++){
            String key=Character.toString(s.charAt(i));
            //Log.d("key", key);

            if(children.containsKey(key)){
                t=children.get(key);
            }else{
                t=new TrieNode();
                children.put(key, t);
            }

            children = t.children;
        }
        t.isWord=true;
    }

    public boolean isWord(String s) {
        children=root;
        Log.d("Test", "Testing if "+s+" is a valid word");

        for(int i=0; i<s.length(); i++) {
            String key = Character.toString(s.charAt(i));
            TrieNode t=null;
            if(children.containsKey(key)){
                Log.d("key", "children has the key");
                t=children.get(key);
                if(t.isWord){
                    Log.d("Test",s+" is a valid word");

                    return true;
                }
            }else{
                Log.d("Test", s + " is not a valid word");

                return false;
            }
            children=t.children;
        }
       return false;
    }

    public String getAnyWordStartingWith(String prefix) {
        ArrayList<String>wordList=new ArrayList<>();

        Log.d("test","getting a word starting with "+prefix);
        children=root;
        String key="";
        //check if the prefix is valid
        for(int i=0; i<prefix.length(); i++){
            key=Character.toString(prefix.charAt(i));
            TrieNode t=null;
            if(children.containsKey(key)){
                Log.d("test", "Children contains key "+key);
                t=children.get(key);
            }else{
                Log.d("test", "Children does not contains key "+key);

                return null;
            }
            children=t.children;
        }

        preOrderTraversal(this, prefix, wordList);
        Random random=new Random();
        int index=random.nextInt(wordList.size());
        String word=wordList.get(index);

        Log.d("test", "Returning long word "+word);

        return  word;

    }

    private void preOrderTraversal(TrieNode t, String prefix, ArrayList<String>wordList){

        if(t.isWord && t.children.isEmpty()) {
            wordList.add(prefix);
        }else {
            if(t.isWord){
                wordList.add(prefix);
            }
            for (String key : t.children.keySet()) {
                String temp=prefix+key;
                preOrderTraversal(t.children.get(key), temp,wordList);
            }
        }

    }
    //consider all the children of the current prefix and attempt to randomly pick one that is not a complete word
    public String getGoodWordStartingWith(String prefix) {
        Random random=new Random();
        int index;
        ArrayList<String>wordList=new ArrayList<>();
        children=root;
        for(int i=0; i<prefix.length();i++){
            TrieNode t=null;
            String key=Character.toString(prefix.charAt(i));
            if(children.containsKey(key)){
                t=children.get(key);
            }else{
                return  null;
            }
            children=t.children;
        }
        //Put all the keys that do not form a valid word into an array
        ArrayList<String>keysArray=new ArrayList<>();
        for(String s: this.children.keySet()){
            if(!this.isWord){ //add the keys to the keysArray if the trie is not a valid word
                keysArray.add(s);
            }
        }

        if (keysArray.isEmpty()){
            preOrderTraversal(this, prefix, wordList);
        }else{
            index=random.nextInt(keysArray.size());
            String key=keysArray.get(index);
            prefix=prefix+key;
            preOrderTraversal(this.children.get(key),prefix,wordList);
        }

        index=random.nextInt(wordList.size());

        String word=wordList.get(index);
        return word;
    }


}
