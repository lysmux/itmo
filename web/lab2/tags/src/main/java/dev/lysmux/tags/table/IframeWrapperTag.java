package dev.lysmux.tags.table;

import dev.lysmux.tags.Utils;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Setter;

import java.io.IOException;

public class IframeWrapperTag extends SimpleTagSupport {
    @Setter
    private String cssClass;

    @Setter
    private String id;

    @Setter
    private String tableUrl;

    private static final String IFRAME_SCRIPT_LOADED_KEY = "iframeScriptLoaded";

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();

        Boolean isScriptLoaded = (Boolean) getJspContext().getAttribute(IFRAME_SCRIPT_LOADED_KEY, PageContext.PAGE_SCOPE);
        if (isScriptLoaded == null) {
            Utils.loadJS("iframe.js", out);
            getJspContext().setAttribute(IFRAME_SCRIPT_LOADED_KEY, true, PageContext.PAGE_SCOPE);
        }

        out.print("<iframe onload=\"setupIframeResize(this)\"");
        out.print(" src=\"%s\"".formatted(tableUrl));
        out.print(" style=\"display:block;border:none;width:100%\"");
        if (cssClass != null) out.print(" class='%s'".formatted(cssClass));
        if (id != null) out.print(" id='%s'".formatted(id));
        out.println("></iframe>");
    }
}
