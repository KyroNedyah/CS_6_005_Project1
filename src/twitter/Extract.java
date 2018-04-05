/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.List;
import java.util.Set;

import java.time.Instant;
import java.util.HashSet;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
	if(tweets.isEmpty())
	{
		return null;
	}

	Instant start = tweets.get(0).getTimestamp();
	Instant end = tweets.get(0).getTimestamp();

	for(Tweet tweet : tweets)
	{
		if(tweet.getTimestamp().compareTo(start) < 0)
		{
			start = tweet.getTimestamp();
		}
		else if(tweet.getTimestamp().compareTo(end) > 0)
		{
			end = tweet.getTimestamp();
		}
	}

	return new Timespan(start, end);
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
	Set<String> mentions = new HashSet<String>();
	for(Tweet tweet : tweets)
	{
		String text = tweet.getText();
		int index = 1;
		while((index = text.indexOf('@', index)) > 0)
		{
			if(!isValid(text.charAt(index-1)))
			{
				boolean success = false;

				for(int i = 1; i + index < text.length() && !success; i++)
				{
					if(!isValid(text.charAt(index+i)))
					{
						String mention = text.substring(index, index+i).toLowerCase();
						mentions.add(mention);
						success = true;
					}
				}

				if(!success && index+1 < text.length())
				{
					String mention = text.substring(index, text.length());
					mentions.add(mention);
				}
			}
			index++;
		}
	}

	return mentions;
    }

	/**
	* Returns true if the character is a valid character for a twitter handle.
	*
	* @param c
	*		character to check
	* @return true if character is valid twitter handle character
	*/
	private static boolean isValid(char c)
	{
		return (c >= 48 && c <= 57) || (c >= 65 && c <= 90) || (c >= 97 && c <= 122) || c == 126 || c == 95;
	}
}
