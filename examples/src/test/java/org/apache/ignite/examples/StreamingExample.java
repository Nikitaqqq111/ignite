package org.apache.ignite.examples;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.stream.StreamTransformer;
import org.apache.ignite.testframework.junits.IgniteConfigVariationsAbstractTest;
import sun.net.util.URLUtil;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StreamingExample extends IgniteConfigVariationsAbstractTest {

    public void testStreamingExample() throws Exception {

        for (Ignite ignite : Ignition.allGrids()) {
            if (ignite.configuration().isClientMode()) {
                Path path = Paths.get("src/test/resources/classesforclassloader").toAbsolutePath();
                URL url = new URL("file:///".concat(path.toString()).concat("/"));
                URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
                @SuppressWarnings("unchecked")
                CacheEntryProcessor<String, Long, Object> cacheEntryProcessor = (CacheEntryProcessor<String, Long, Object>) urlClassLoader
                        .loadClass("org.apache.ignite.examples.StreamingExampleCacheEntryProcessor").newInstance();
                IgniteCache<String, Long> stmCache = ignite.getOrCreateCache("mycache");
                try (IgniteDataStreamer<String, Long> igniteDataStreamer = ignite.dataStreamer(stmCache.getName())) {
                    igniteDataStreamer.allowOverwrite(true);
                    igniteDataStreamer.receiver(StreamTransformer.from(cacheEntryProcessor));
                    igniteDataStreamer.addData("word", 1L);
                }
            }
        }
    }

}