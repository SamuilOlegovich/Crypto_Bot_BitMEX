/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitmex.Bot.model.bitMEX.client;

import bitmex.Bot.model.bitMEX.entity.BitmexAmendOrder;
import bitmex.Bot.model.bitMEX.entity.BitmexChartData;
import bitmex.Bot.model.bitMEX.entity.BitmexInstrument;
import bitmex.Bot.model.bitMEX.entity.BitmexOrder;
import bitmex.Bot.model.bitMEX.entity.newClass.Ticker;
import bitmex.Bot.model.bitMEX.enums.ChartDataBinSize;
//import com.sumzerotrading.data.Ticker;
import java.util.List;

/**
 *
 * @author RobTerpilowski
 */
public interface IBitmexRestClient {
    List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize, String endTime, boolean getInprogressBar);
    List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize, String endTime);
    List<BitmexChartData> getChartData(Ticker ticker, int count, ChartDataBinSize binSize);
//    List<BitmexChartData> getChartData(Ticker ticker, int count, BitmexRestClient.ChartDataBinSize binSize);
    BitmexOrder amendOrder(BitmexAmendOrder order);
    BitmexInstrument getInstrument(Ticker ticker);
    BitmexOrder[] cancelOrder(BitmexOrder order);
    BitmexOrder submitOrder(BitmexOrder order);
}
