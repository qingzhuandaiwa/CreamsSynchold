package com.yinkun.creams.app;

import com.jfinal.core.JFinal;

public class AppRunner {

    public static void main(String[] argv) {
        JFinal.start("src/main/webapp", 9999, "/");
    }
    
    
}
