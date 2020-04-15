package bitmex.Bot.model.enums;

public enum BidAsk {
    DELTA_MIN_VOL_ASK_HL,
    DELTA_MIN_VOL_BID_HL,
    OPEN_POS_MINUS_HL,          // макс открытый интерес - отрицательная -
    OPEN_POS_PLUS_HL,           // макс открытый интерес - положительный +
    OPEN_POS_MINUS,             // открытый интерес - отрицательная -
    OPEN_POS_PLUS,              // открытый интерес - положительный +
    DELTA_ASK_HL,               // макс дельта положительная +
    DELTA_BID_HL,               // макс дельта отрицательная -
    DELTA_ASK,                  // дельта положительная +
    DELTA_BID,                  // дельта отрицательная -
    VOLUME,
    ASK,
    BID;
}
