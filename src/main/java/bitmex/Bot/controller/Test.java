package bitmex.Bot.controller;


import bitmex.Bot.view.ConsoleHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOError;
import java.util.Arrays;



public class Test {

    public static void main(String[] args) {


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

