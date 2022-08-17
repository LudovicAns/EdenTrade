package fr.edencraft.edentrade.trade;

public class TradeBuildException extends Exception {

    public TradeBuildException(String cause, TradeBuilder tradeBuilder) {
        super("TradeBuildException: \n" + cause);
    }

}
