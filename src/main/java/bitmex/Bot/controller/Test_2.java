package bitmex.Bot.controller;


import bitmex.Bot.model.Gasket;
import bitmex.Bot.view.ConsoleHelper;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static bitmex.Bot.model.enums.TimeFrame.*;

public class Test_2 {

    public static void main(String[] args) {
        System.out.println(M5.name());
        System.out.println(M5.ordinal());
        System.out.println(M30.ordinal() > M30.ordinal());






//        int value = 1000;
//
//
//        List<Integer> list =  new ArrayList<>();
//        // добовляем в массив все значения нужного таймфрейма за нужное время
//        // можно их конечно и не добавлять, а считать по мере прихождения, как пример привел так, так как не знаю как
//        // приходят данные
//        list.add(value);
//        // сортируем если массив сам по себе не сортирует
//
//        // далее узнаем длину массива и делим ее на два
//        // потом узнаем средние числа этих двух половинок
//        int size = list.size();
//
//        int averageFinish = 0;
//        int averageStop = 0;
//        int count = 0;
//
//        for (int i = 0; i < size/2; i++) {
//            averageFinish = averageFinish + list.get(i);
//            count++;
//        }
//        averageFinish = averageFinish / count;
//        count = 0;
//
//        for (int i = size/2; i < size; i++) {
//            averageStop = averageStop + list.get(i);
//            count++;
//        }
//        averageStop = averageStop / count;
//        count = 0;
//
//        //  в итоге получаем актуальное среднее
//        int actualValues = 0;
//
//        for (Integer integer : list) {
//            if (integer > averageFinish && integer < averageStop) {
//                actualValues = actualValues + integer;
//                count++;
//            }
//        }
//        actualValues = actualValues / count; // актуальное значение готово
//        count = 0;
//        list.clear();
//
//        // можно еще варианты придумать, например
//        // там где можно определить пучности значений и узнать актуальное среднее значение пучносте

    }
}


