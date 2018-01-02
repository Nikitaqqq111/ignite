package org.apache.ignite.testsuites;

import junit.framework.TestResult;
import junit.framework.TestSuite;
import org.apache.ignite.examples.StreamingExample;
import org.apache.ignite.testframework.configvariations.ConfigVariationsTestSuiteBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StreamingExampleTest {

    private TestSuite suite;

    @Before
    public void setUp() throws Exception {
        suite = new ConfigVariationsTestSuiteBuilder(
                "StreamingExample Test Suite",
                StreamingExample.class)
                .gridsCount(3)
                .testedNodesCount(2).withClients()
                .build();
    }

    @Test
    public void streamingExampleTest() throws Exception {
        TestResult result = new TestResult();
        suite.run(result);
        assertTrue(result.errorCount() == 0);
    }

}
