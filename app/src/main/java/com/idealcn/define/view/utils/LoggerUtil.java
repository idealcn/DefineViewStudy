package com.idealcn.define.view.utils;

import android.util.Log;

import java.util.logging.Logger;

public class LoggerUtil {

    private Logger logger = Logger.getLogger("define");


    private LoggerUtil(){}
    private static class INSTANCE {
        static final LoggerUtil logger = new LoggerUtil();
    }


    public static LoggerUtil getInstance(){
        return INSTANCE.logger;
    }


    public  void info(String info){
        logger.info(info);
    }
}
