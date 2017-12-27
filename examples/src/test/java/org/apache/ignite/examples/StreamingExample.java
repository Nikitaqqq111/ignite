package org.apache.ignite.examples;

import org.apache.ignite.*;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.stream.StreamTransformer;
import org.apache.ignite.testframework.junits.IgniteConfigVariationsAbstractTest;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;

public class StreamingExample extends IgniteConfigVariationsAbstractTest {

    public static class StreamingExampleCacheEntryProcessor implements CacheEntryProcessor<String, Long, Object> {
        @Override
        public Object process(MutableEntry<String, Long> e, Object... arg) throws EntryProcessorException {
            Long val = e.getValue();
            e.setValue(val == null ? 1L : val + 1);
            return null;
        }
    }

    public void testStreamingExample() throws Exception {
        for (Ignite ignite : Ignition.allGrids()) {
            if (ignite.configuration().isClientMode()) {
                IgniteCache<String, Long> stmCache = ignite.getOrCreateCache("mycache");
                try (IgniteDataStreamer<String, Long> stmr = ignite.dataStreamer(stmCache.getName())) {
                    stmr.allowOverwrite(true);
                    stmr.receiver(StreamTransformer.from(new StreamingExampleCacheEntryProcessor()));
                    stmr.addData("word", 1L);
                    System.out.println("Finished");
                }
            }
        }
    }

}