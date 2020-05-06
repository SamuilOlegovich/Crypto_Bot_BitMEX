package bitmex.Bot.model.enums;

public enum BidAsk {
    DELTA_MIN_VOL_ASK_HL,
    DELTA_MIN_VOL_BID_HL,
    OPEN_POS_BID_MINUS,         // ОИ продавцов минус
    OPEN_POS_ASK_MINUS,         // ОИ покупателей минус
    DELTA_ZS_MIN_MINUS,         // зеркальная минимум Дельта минус
    DELTA_ZS_MIN_PLUS,          // зеркальная минимум Дельта плюс
    OPEN_POS_ASK_PLUS,          // ОИ покупателей плюс
    OPEN_POS_BID_PLUS,          // ОИ продавцов плюс
    OPEN_POS_MINUS_HL,          // макс открытый интерес - отрицательная -
    OPEN_POS_PLUS_HL,           // макс открытый интерес - положительный +
    OI_ZS_MIN_MINUS,            // зеркальный  минимум ОИ минус
    OI_ZS_MIN_PLUS,             // зеркальный минимум  ОИ плюс
    DELTA_ZS_MINUS,             // зеркальная Дельта минус
    OPEN_POS_MINUS,             // открытый интерес - отрицательная -
    DELTA_ZS_PLUS,              // зеркальная Дельта плюс
    OPEN_POS_PLUS,              // открытый интерес - положительный +
    DELTA_ASK_HL,               // макс дельта положительная +
    DELTA_BID_HL,               // макс дельта отрицательная -
    OI_ZS_MINUS,                // зеркальный ОИ минус
    OI_ZS_PLUS,                 // зеркальный ОИ плюс
    DELTA_ASK,                  // дельта положительная +
    DELTA_BID,                  // дельта отрицательная -
    VOLUME,
    ASK,
    BID;
}




