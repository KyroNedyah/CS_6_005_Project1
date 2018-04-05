/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2017-02-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "whoisthis", "sometweet", d3);
    private static final Tweet tweet4 = new Tweet(4, "somedude", "yet another tweet", d1);
    private static final Tweet tweet1Mention = new Tweet(5, "alpha", "hello @bob", d1);
    private static final Tweet tweet2Mention = new Tweet(6, "beta", "hello @bob and @meg", d1);
    private static final Tweet tweetFake = new Tweet(7, "gamma", "@me my email hi@google.com oh and @", d1);
    private static final Tweet tweetHybrid = new Tweet(8, "alpha", "hi @bob is your email bob@bob.bob?", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

	@Test
	public void testGetTimespanThree()
	{
		Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3));
		
		assertEquals("expected start", d1, timespan.getStart());
		assertEquals("expected finish", d3, timespan.getEnd());
	}

	@Test
	public void testEqualTimespan()
	{
		Timespan timespan1 = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
		Timespan timespan2 = Extract.getTimespan(Arrays.asList(tweet1, tweet4));

		assertEquals("expected start (1-3)", d1, timespan1.getStart());
		assertEquals("expected finish (1-3)", d3, timespan1.getEnd());
		assertEquals("expected start (1-1)", d1, timespan2.getStart());
		assertEquals("expected finish (1-1)", d1, timespan2.getEnd());
	}

	@Test
	public void testGetMentionedUsersOneMention()
	{
		Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(tweet1Mention));

		String[] expected = {"@bob"};
		assertEquals("expected number of mentions", expected.length, mentions.size());
		for(String mention : expected)
		{
			assertTrue("expected mention: " + mention, mentions.contains(mention));
		}
	}

	@Test
	public void testGetMentionedUsersManyMentioned()
	{
		Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(tweet2Mention));

		String[] expected = {"@bob", "@meg"};
		assertEquals("expected number of mentions", expected.length, mentions.size());
		for(String mention : expected)
		{
			assertTrue("expected mention: " + mention, mentions.contains(mention));
		}
	}

	@Test
	public void testGetMentionedUserDuplicates()
	{
		Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(tweet1Mention, tweet2Mention));

		String[] expected = {"@bob", "@meg"};
		assertEquals("expected number of mentions", expected.length, mentions.size());
		for(String mention : expected)
		{
			assertTrue("expected mention: " + mention, mentions.contains(mention));
		}
	}

	@Test
	public void testGetMentionedUsersFakeMention()
	{
		Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(tweetFake));
		assertTrue("expected empty", mentions.isEmpty());
	}

	@Test
	public void testGetMentionedUsersHybridMention()
	{
		Set<String> mentions = Extract.getMentionedUsers(Arrays.asList(tweetHybrid));

		String[] expected = {"@bob"};
		assertEquals("expected number of mentions", expected.length, mentions.size());
		for(String mention : expected)
		{
			assertTrue("expected mention: " + mention, mentions.contains(mention));
		}
	}
}
