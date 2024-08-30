package com.nospawnn.githubactivitytracker.web;

import com.renomad.minum.web.FullSystem;

public class Web {
    public static void run() {
        var fs = FullSystem.initialize();

        new PathRegister(fs.getContext()).registerDomains();

        fs.block();
    }
}
