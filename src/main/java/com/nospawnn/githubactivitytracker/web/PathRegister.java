package com.nospawnn.githubactivitytracker.web;

import com.renomad.minum.state.Context;
import com.renomad.minum.utils.FileUtils;
import com.renomad.minum.web.WebFramework;
import com.renomad.minum.web.RequestLine.Method;

public class PathRegister {
    public static final String TEMPLATE_DIR = "src/main/resources/templates";

    private final Context context;
    private final WebFramework webFramework;

    public PathRegister(Context context) {
        this.context = context;
        this.webFramework = context.getFullSystem().getWebFramework();

    }

    public void registerDomains() {
        Page.fileUtils = new FileUtils(context.getLogger(), context.getConstants());
        Page p = new Page();

        webFramework.registerPath(Method.GET, "", p::initialHtml);
        webFramework.registerPath(Method.GET, "events", p::eventsForUser);
        webFramework.registerPath(Method.GET, "eventsingle", p::singleEventView);
    }

}
