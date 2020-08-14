package bitmex.Bot.controller;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import bitmex.Bot.view.ConsoleHelper;

import java.io.IOError;
import java.util.Arrays;



public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        Cat cat = new Cat();
        cat.setAge(17);
        System.out.println(test.toJson(cat));

        Cat cat2 = new CatXvost();
        cat2.setAge(20);
        cat2.setXvost(21);

        System.out.println(test.toJson(cat));
        System.out.println(test.toJson(cat2));


    }

    protected String toJson(Object object) {
        ObjectMapper mapper = getObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
//            throw new SumZeroException(ex);
            ConsoleHelper.writeERROR(Arrays.toString(ex.getStackTrace()));
            throw new IOError(ex);
        }
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}

class Cat {
    private int age;
    @JsonIgnore
    private int xvost;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

//    @JsonIgnore
    public int getXvost() {
        return -1;
    }

//    @JsonIgnore
    public void setXvost(int xvost) {
    }
}

@JsonAutoDetect
class CatXvost extends Cat {
    private int xvost;

    @Override
    public int getXvost() {
        return xvost;
    }

    @Override
    public void setXvost(int xvost) {
        this.xvost = xvost;
    }
}
