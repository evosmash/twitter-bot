package no.rodland.twitter;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by IntelliJ IDEA. User: fmr Date: May 6, 2009 Time: 10:51:26 AM
 */
public class Deleter {
    private static final Logger log = Logger.getLogger(Deleter.class);
    private static Twitter twitter;

    private static String twitterUser;
    private static String twitterPassword;
    private static List<Long> statusIds = new ArrayList<Long>();

    public static void main(String[] args) throws TwitterException {
        init(args);
        delete(statusIds);
    }

    private static void delete(List<Long> ids) throws TwitterException {
        Twitter tw = getTwitter();
        log.info("deleting status: " + ids);
        for (Long id : ids) {
            log.info(tw.destroyStatus(id));
        }
    }

    private static Twitter getTwitter() {
        if (twitter == null) {
            twitter = TwitterAPI.getTwitter(twitterUser, twitterPassword);
        }
        return twitter;
    }

    private static void init(String[] args) {
        if (args.length >= 3) {
            twitterUser = args[0];
            twitterPassword = args[1];
            for (int i = 2; i < args.length; i++) {
                statusIds.add(Long.valueOf(args[i]));
            }
        }
        else {
            usage();
            System.exit(2);
        }
    }

    private static void usage() {
        System.out.println("Twitter Deleter");
        System.out.println("usage 1: java no.rodland.twitter.Deleter <twitteruser> <twitterpassword> statusId statusId ...");
    }
}