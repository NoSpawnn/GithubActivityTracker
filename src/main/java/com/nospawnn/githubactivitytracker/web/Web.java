package com.nospawnn.githubactivitytracker.web;

import com.renomad.minum.web.FullSystem;

public class Web {
    public static void run() {
        var fs = FullSystem.initialize();
        var context = fs.getContext();
        var constants = context.getConstants();
        
        new PathRegister(context).registerDomains();

        System.out.printf("\nServer running, access at Access at http://%s:%d or https://%s:%d\n",
                constants.hostName,
                constants.serverPort,
                constants.hostName,
                constants.secureServerPort);

        fs.block();
    }
}
