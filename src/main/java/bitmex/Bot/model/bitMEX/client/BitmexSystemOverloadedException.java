/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.entity.BitmexOrder;

/**
 *
 * @author RobTerpilowski
 */
public class BitmexSystemOverloadedException extends RuntimeException {

    protected BitmexOrder submittedOrder;
    
    public BitmexSystemOverloadedException(BitmexOrder submittedOrder ) {
        super("The system is currently overloaded. Please try again later");
        //super("В данный момент система перегружена. Пожалуйста, попробуйте позже");
        this.submittedOrder = submittedOrder;
    }

    public BitmexOrder getSubmittedOrder() {
        return submittedOrder;
    }
    
    
    
    
    
    
}
