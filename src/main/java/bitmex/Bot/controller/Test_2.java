package bitmex.Bot.controller;



import java.util.*;

public class Test_2 {



    public static void main(String[] args) {
        List<Integer> treeSet = new ArrayList<>();
        treeSet.add(9);
        treeSet.add(5);
        treeSet.add(6);
        treeSet.add(5);
        System.out.println(treeSet);
        Collections.reverse(treeSet);
        Set<Integer> set = new TreeSet<>(treeSet);
        treeSet.clear();
        treeSet.addAll(set);
        Collections.reverse(treeSet);
        System.out.println(treeSet);

    }


}




