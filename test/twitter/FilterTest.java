/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-03-17T11:00:00Z");
	private static final Instant d4 = Instant.parse("2016-04-17T11:00:00Z");
	private static final Instant d5 = Instant.parse("2017-04-17T11:00:00Z");


    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    private static final Tweet tweet3 = new Tweet(3, "bob", " hi it's me bob", d3);
    private static final Tweet tweet4 = new Tweet(4, "bob", " hi @alyssa", d4);
    private static final Tweet tweet5 = new Tweet(5, "bob", " hi @bbitdiddle", d5);

    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */

	@Test
	public void testWrittenByMultiple()
	{
		List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), "bob");

		assertEquals("expected 3 tweets", 3, writtenBy.size());
		assertTrue("expected list to contain all tweets", writtenBy.containsAll(Arrays.asList(tweet3, tweet4, tweet5)));
		assertEquals("expected same order", 0, writtenBy.indexOf(tweet3));
		assertEquals("expected same order", 1, writtenBy.indexOf(tweet4));
		assertEquals("expected same order", 2, writtenBy.indexOf(tweet5));
	}

	@Test
	public void testWrittenByNone()
	{
		List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "bob");

		assertEquals("expected empty list", 0, writtenBy.size());
	}

	@Test
	public void testInTimespanNoResultsBefore()
	{
		Instant testStart = Instant.parse("2015-02-17T09:00:00Z");
		Instant testEnd = Instant.parse("2015-02-18T09:00:00Z");

		List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));

		assertTrue("expected empty list", inTimespan.isEmpty());
	}

	@Test
	public void testInTimespanNoResultsAfter()
	{
		Instant testStart = Instant.parse("2019-02-17T09:00:00Z");
		Instant testEnd = Instant.parse("2019-02-18T09:00:00Z");

		List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4), new Timespan(testStart, testEnd));

		assertTrue("expected empty list", inTimespan.isEmpty());
	}

	@Test
	public void testContainsNoResults()
	{
		List<Tweet> contains = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), Arrays.asList("how about no"));

		assertTrue("expected empty list", contains.isEmpty());
	}

	@Test
	public void testContainsSomeResults()
	{
		List<Tweet> contains = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), Arrays.asList("hi"));

		assertEquals("expected 3 tweets", 3, contains.size());
		assertTrue("expected list to contain all tweets", contains.containsAll(Arrays.asList(tweet3, tweet4, tweet5)));
		assertEquals("expected same order", 0, contains.indexOf(tweet3));
		assertEquals("expected same order", 1, contains.indexOf(tweet4));
		assertEquals("expected same order", 2, contains.indexOf(tweet5));
	}

	@Test
	public void testContainsManyQuery()
	{
		List<Tweet> contains = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), Arrays.asList("talk", "rivset"));

		assertEquals("expected 2 tweets", 2, contains.size());
		assertTrue("expected list to contain all tweets", contains.containsAll(Arrays.asList(tweet1, tweet2)));
		assertEquals("expected same order", 0, contains.indexOf(tweet1));
		assertEquals("expected same order", 1, contains.indexOf(tweet2));
	}
}
