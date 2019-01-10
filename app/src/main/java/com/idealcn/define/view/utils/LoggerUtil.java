package com.idealcn.define.view.utils;

import android.util.Log;

import java.util.logging.Logger;

public class LoggerUtil {

    private Logger logger = Logger.getLogger("define");


    private LoggerUtil(){}
    public static class INSTANCE {
        public static final LoggerUtil logger = new LoggerUtil();
    }



    public  void info(String info){
        logger.info(info);
    }
}
