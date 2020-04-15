/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.listener.IExecutionListener;
import bitmex.Bot.model.bitMEX.listener.IPositionListener;
import bitmex.Bot.model.bitMEX.listener.IOrderListener;
import bitmex.Bot.model.bitMEX.listener.IQuoteListener;
import bitmex.Bot.model.bitMEX.listener.ITradeListener;
import bitmex.Bot.model.bitMEX.listener.IPongListener;

/**
 *
 * @author RobTerpilowski
 */
public interface IMessageProcessor {

    void addExecutionListener(IExecutionListener listener);
    void addPositionListener(IPositionListener listener);
    void addQuoteListener(IQuoteListener listener);
    void addOrderListener(IOrderListener listener);
    void addTradeListener(ITradeListener listener);
    void addPongListener(IPongListener listener);
    void processMessage(String message);
    void startProcessor();
    void stopProcessor();
    int getQueueSize();
}
