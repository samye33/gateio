package com.server;

import java.util.Date;

public class MyConstant {
    public static String COOKIEBROWSER;
    public static String COOKIETRADEBID;
    public static String TRADEPWD;
    public static String COINTYPE;
    public static Double SALERATE;
    public static String USERAGENT;
    public static String NICKNAME;
    public static String UID;
    public static String PVER;
    public static String CAPTCHA;
    public static String AWSALB;
    public final static String BIDSALEBASEURL = "Hm_lvt_0a1ead8031fdf1a7228954da1b158d36=";
    public static String[] global_price_watch_markets = {"BTC_USDT", "BCH_USDT", "ETH_USDT", "ETC_USDT", "QTUM_USDT", "LTC_USDT", "DASH_USDT", "ZEC_USDT", "BTM_USDT", "EOS_USDT", "REQ_USDT", "SNT_USDT", "OMG_USDT", "PAY_USDT", "CVC_USDT", "ZRX_USDT", "TNT_USDT", "XMR_USDT", "XRP_USDT", "DOGE_USDT", "BAT_USDT", "PST_USDT", "BTG_USDT", "DPY_USDT", "LRC_USDT", "STORJ_USDT", "RDN_USDT", "STX_USDT", "KNC_USDT", "LINK_USDT", "CDT_USDT", "AE_USDT", "RCN_USDT", "TRX_USDT", "KICK_USDT", "VEN_USDT", "MCO_USDT", "FUN_USDT", "DATA_USDT", "ZSC_USDT", "MDA_USDT", "XTZ_USDT", "GNT_USDT", "GEM_USDT", "RFR_USDT", "DADI_USDT", "ABT_USDT", "OST_USDT", "XLM_USDT", "MOBI_USDT", "OCN_USDT", "ZPT_USDT", "COFI_USDT", "JNT_USDT", "BLZ_USDT", "GXS_USDT", "MTN_USDT", "RUFF_USDT", "TNC_USDT", "ZIL_USDT", "TIO_USDT", "BTO_USDT", "THETA_USDT", "DDD_USDT", "MKR_USDT", "DAI_USDT", "SMT_USDT", "MDT_USDT", "MANA_USDT", "LUN_USDT", "SALT_USDT", "FUEL_USDT", "ELF_USDT", "DRGN_USDT", "GTC_USDT", "QLC_USDT", "DBC_USDT", "BNTY_USDT", "LEND_USDT", "ICX_USDT", "BTF_USDT", "ADA_USDT", "LSK_USDT", "WAVES_USDT", "BIFI_USDT", "MDS_USDT", "DGD_USDT", "QASH_USDT", "POWR_USDT", "FIL_USDT", "BCD_USDT", "SBTC_USDT", "GOD_USDT", "BCX_USDT", "HSR_USDT", "QSP_USDT", "INK_USDT", "MED_USDT", "BOT_USDT", "QBT_USDT", "TSL_USDT", "GNX_USDT", "NEO_USDT", "GAS_USDT", "IOTA_USDT", "NAS_USDT", "BCDN_USDT", "SNET_USDT", "BTS_USDT", "XMC_USDT", "CS_USDT", "MAN_USDT", "LYM_USDT", "ONT_USDT", "BFT_USDT", "IHT_USDT", "SENC_USDT", "SWH_USDT", "LRN_USDT", "DOCK_USDT", "MITH_USDT", "SKM_USDT", "XVG_USDT", "NANO_USDT"};
    public static String GLOBAL_PRICE_MARKETS;
    public static String MONITOR_GLOBAL_PRICE_MARKETS="";

    public static String InitialCookieBySale() {
        String cookie = "AWSALB=" + AWSALB + "; lasturl=%2F; market_title=usdt;" + BIDSALEBASEURL + (new Date()).getTime() + ";" +
                BIDSALEBASEURL + ((new Date()).getTime() + 1005) + ";captcha=" + CAPTCHA + "; uid=" + UID + "; nickname=" + NICKNAME + "; pver=" + PVER + ";";
        System.out.println(cookie);
        return cookie;
    }

    public static String global_price_watch_markets() {
        String aaa = "";
        for (int i = 0; i < MyConstant.global_price_watch_markets.length; i++) {
            if (i == MyConstant.global_price_watch_markets.length - 1) {
                aaa += "\"" + MyConstant.global_price_watch_markets[i] + "\"";
            } else {
                aaa += "\"" + MyConstant.global_price_watch_markets[i] + "\"" + ",";
            }

        }
        GLOBAL_PRICE_MARKETS = aaa;
        return aaa;
    }

}
