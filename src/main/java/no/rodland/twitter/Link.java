package no.rodland.twitter;

import org.apache.log4j.Logger;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA. User: fmr Date: Apr 29, 2009 Time: 8:00:52 PM
 */
public class Link {
    private static final Logger log = Logger.getLogger(Link.class);
    private static final Pattern MULTIPLE_URLS = Pattern.compile("(https?://.*)*(https?://.*)");
    private static final Pattern GOOGLE_ENDING = Pattern.compile("([^&]*)&.*");
    private static final String FUNNY_HTTP = "http%3A";
    private static final String NORMAL_HTTP = "http:";

    private final String origLink;
    private final String simplified;
    private static final String SLASH = "/";

    public Link(String origLink) {
        this.origLink = origLink;
        this.simplified = simplify(origLink);
    }

    public String getLink() {
        return simplified;
    }

    public static String simplify(String origLink) {
        String retString = origLink;
        try {
            retString = URLDecoder.decode(retString, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            log.error("Unable to decode url: " + origLink, e);
        }
        retString = removeMulitpleUrls(retString);
        retString = removeParams(retString);
        retString = removeLastSlash(retString);

        return retString;
    }

    public static String removeLastSlash(String retString) {
        if (retString == null) {
            return null;
        }
        if (retString.length() == 0) {
            return "";
        }
        if (retString.lastIndexOf(SLASH) == (retString.length() - 1)) {
            return retString.substring(0, (retString.length() - 1));
        }
        return retString;
    }

    private static String fixFunnyHttp(String url) {
        if (url == null) {
            return null;
        }
        String decoded = url;
        try {
            decoded = URLDecoder.decode(url, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            log.error("Unable to decode url: " + url, e);
        }

        return decoded;
    }

    @Override
    public String toString() {
        return "Link: " + origLink + " simplified to " + simplified;
    }

    public static String removeMulitpleUrls(String url) {
        if (url == null) {
            return null;
        }
        Matcher m = MULTIPLE_URLS.matcher(url);
        if (m.matches()) {
            return m.group(2);
        }
        return url;
    }

    public static String removeParams(String url) {
        if (url == null) {
            return null;
        }
        Matcher m = GOOGLE_ENDING.matcher(url);
        if (m.matches()) {
            return m.group(1);
        }
        return url;
    }
}
