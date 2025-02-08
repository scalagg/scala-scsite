package gg.scala.site;

import com.google.gson.Gson;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.time.ZoneId;

public class SiteConstant {

    public static final boolean API_DEBUG = true;
    public static final String API_HOST = "http://100.82.132.12:8081/";
    public static final String API_KEY = "Mmvb500e9cZShvMgxnI44XGjyzIXOX5a";

    public static final boolean MAINTENANCE_MODE = false;

    public static final String ANNOUNCEMENTS_FORUM = "announcements";
    public static final ZoneId TIME_ZONE = ZoneId.of("America/New_York");

    public static final Gson GSON = new Gson();

    public static final Parser MARKDOWN_PARSER = Parser.builder().build();
    public static final HtmlRenderer MARKDOWN_RENDERER = HtmlRenderer.builder()
            .softbreak("<br/>")
            .build();

    public static final PolicyFactory POLICY_FACTORY = new HtmlPolicyBuilder()
            .allowElements(
                    "p",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5",
                    "h6",
                    "pre",
                    "span",
                    "strong",
                    "em",
                    "s",
                    "img",
                    "blockquote",
                    "code",
                    "ul",
                    "li",
                    "ol",
                    "br",
                    "a"
            )
            .allowAttributes("target").onElements("a")
            .allowStandardUrlProtocols()
            .allowStyling()
            .toFactory()
            .and(Sanitizers.IMAGES)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.TABLES);

}
