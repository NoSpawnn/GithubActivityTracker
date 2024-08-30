package com.nospawnn.githubactivitytracker.web;

import com.renomad.minum.state.Context;
import com.renomad.minum.utils.FileUtils;
import com.renomad.minum.web.WebFramework;
import com.renomad.minum.web.RequestLine.Method;

public class PathRegister {
    private final Context context;
    private final WebFramework webFramework;

    public PathRegister(Context context) {
        this.context = context;
        this.webFramework = context.getFullSystem().getWebFramework();

    }

    public void registerDomains() {
        Page p = new Page(new FileUtils(context.getLogger(), context.getConstants()));

        webFramework.registerPath(Method.GET, "", p::initialHtml);
        webFramework.registerPath(Method.GET, "events", p::eventsForUser);
    }

}
