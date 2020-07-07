package IIUser;

import bitmex.Bot.model.strategies.IIUser.CompareAndMakeDecisionUser;

import java.util.ArrayList;




public class CompareAndMakeDecisionUserTest {
    public static void main(String[] args) {
        ArrayList<String> pattern = new ArrayList<>();
        ArrayList<String> market = new ArrayList<>();

        pattern.add("BUY===1===SELL===0===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===OPEN_POS_BID_PLUS_SMALL===ID===1");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9781.0===value===69770===type===OPEN_POS_BID_PLUS_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9780.5===value===-1088603===type===ASK_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9778.0===value===69770===type===VOLUME_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:38:00===price===9777.5===value===-1088603===type===DELTA_ASK_SMALL===avg===null===dir===null===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===M5===preview===null===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===BID_SMALL===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("BIAS===SELL===4.5===2020-06-11 11:40:00");
        pattern.add("period===null===preview===null===time===2020-06-11 11:37:00===price===9776.0===value===514702===type===OPEN_POS_PLUS===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("period===null===preview===null===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===OPEN_POS_ASK_PLUS===avg===null===dir===null===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===280876===type===ASK===avg===null===dir===-1===open===9777.5===close===9776.0===high===9778.0===low===9776.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===-1633222===type===VOLUME===avg===null===dir===-1===open===9776.0===close===9774.0===high===9776.5===low===9774.0");
        pattern.add("period===null===preview===null===time===NULL===price===NULL===value===592200===type===BID===avg===null===dir===-1===open===9774.5===close===9774.5===high===9774.5===low===9774.0");
        pattern.add("period===null===preview===null===time===2020-06-11 11:38:00===price===9769.5===value===-1088603===type===DELTA_BID===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===null===preview===null===time===2020-06-11 11:38:00===price===9768.5===value===69770===type===OPEN_POS_BID_MINUS===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        pattern.add("period===NULL===preview===null===time===2020-06-11 11:37:00===price===9767.0===value===514702===type===DELTA_ZS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");






        market.add("BUY===1===SELL===0===AVERAGE===3.28===MAX===5.0===SIZE===220===BLOCK===1===TYPE===OPEN_POS_BID_PLUS_SMALL===ID===1");
        market.add("period===M5===preview===1===time===2020-06-11 11:38:00===price===9781.0===value===69770===type===OPEN_POS_BID_PLUS_SMALL===avg===null===dir===1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");

        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9780.5===value===-1088603===type===ASK_SMALL===avg===null===dir===0===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9778.0===value===69770===type===VOLUME_SMALL===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===1===time===2020-06-11 11:38:00===price===9777.5===value===-1088603===type===DELTA_ASK_SMALL===avg===null===dir===1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M5===preview===1===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===BID_SMALL===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");


        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");



        market.add("BIAS===SELL===4.5===2020-06-11 11:40:00");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M8===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");


        market.add("period===M15===preview===0===time===2020-06-11 11:37:00===price===9775.0===value===514702===type===OPEN_POS_ASK_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        market.add("period===M15===preview===1===time===2020-06-11 11:39:00===price===9774.0===value===514702===type===OPEN_POS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        market.add("period===M0===preview===1===time===2020-06-11 11:38:00===price===9774.0===value===592200===type===BID===avg===null===dir===-1===open===9774.5===close===9774.5===high===9774.5===low===9774.0");
        market.add("period===M15===preview===0===time===2020-06-11 11:38:00===price===9774.0===value===-1633222===type===VOLUME===avg===null===dir===-1===open===9776.0===close===9774.0===high===9776.5===low===9774.0");
        market.add("period===M5===preview===0===time===2020-06-11 11:38:00===price===9774.0===value===280876===type===ASK===avg===null===dir===-1===open===9777.5===close===9776.0===high===9778.0===low===9776.0");

        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M90===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");


        market.add("period===M5===preview===0===time===2020-06-11 11:37:00===price===9769.5===value===-1088603===type===DELTA_BID===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");
        market.add("period===M15===preview===1===time===2020-06-11 11:37:00===price===9769.0===value===514702===type===DELTA_ZS_PLUS===avg===null===dir===1===open===9769.5===close===9781.5===high===9782.0===low===9769.0");
        market.add("period===M5===preview===1===time===2020-06-11 11:36:00===price===9768.5===value===69770===type===OPEN_POS_BID_MINUS===avg===null===dir===-1===open===9782.0===close===9779.0===high===9782.0===low===9778.5");


        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.5===value===377855===type===OPEN_POS_PLUS_HL===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-585140===type===DELTA_BID_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===-194061===type===OPEN_POS_MINUS_HL===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:08:00===price===9254.0===value===276839===type===OI_ZS_MIN_MINUS===avg===0===dir===-1===open===9254.5===close===9254.0===high===9254.5===low===9254.0");
        market.add("period===M5===preview===0===time===2020-06-26 05:05:00===price===9254.0===value===-585140===type===DELTA_ZS_MIN_PLUS===avg===0===dir===1===open===9253.5===close===9254.0===high===9254.5===low===9253.5");



        new CompareAndMakeDecisionUser(market, pattern);
    }
}
