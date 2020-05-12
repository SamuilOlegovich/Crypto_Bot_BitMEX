package bitmex.Bot.controller;


import java.util.ArrayList;

public class Test_2 {



    public static void main(String[] args) {

        System.out.println(System.getProperty("os.name"));
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("peta");
        arrayList.add("vasa");
        arrayList.add("tola");

        for (String s : arrayList) {
            System.out.println(s);
        }

        System.out.println();

        arrayList.add(0, "kosta");

        for (String s : arrayList) {
            System.out.println(s);
        }

        System.out.println();

        arrayList.set(0, "Ura");

        for (String s : arrayList) {
            System.out.println(s);
        }

//        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();
//        arrayLists.add(arrayList);
//
//        arrayList.clear();
//
//        System.out.println();
//
//        for (ArrayList<String> s : arrayLists) {
//
//                System.out.println(s.size());
//            for (String a : s) {
//                System.out.println(a);
//            }
//        }

        Test_2 test_2 = new Test_2();

        test_2.q.add(arrayList);
        arrayList.clear();
        test_2.q.out();
        test_2.q.out();

    }

    public Q q;

    public Test_2() {
        this.q = new Q();
    }

    class Q {
        ArrayList<ArrayList<String>> arrayLists2;

        public Q() {
            this.arrayLists2 = new ArrayList<>();
        }

        public void add(ArrayList<String> stringArrayList) {
            ArrayList<String> strings = new ArrayList<>(stringArrayList);
            //strings.addAll(stringArrayList);
            arrayLists2.add(strings);
        }

        public void out() {
            System.out.println();
            System.out.println();

            for (ArrayList<String> s : arrayLists2) {
                System.out.println(s.size());
                for (String a : s) {
                    System.out.println(a);
                }
            }
        }
    }
}




