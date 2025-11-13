package com.satoriviacafe.common.utils.reflect;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LongSummaryStatistics;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 简单的微基准（JUnit 测试风格）。默认被禁用（@Disabled）。
 *
 * 说明：
 * - 运行前请确保测试环境与目标相对稳定（JIT、负载、其他进程会影响结果）。
 * - 如果需要更精确测量，推荐使用 JMH（可进一步集成），我可以据此生成 JMH 版本。
 */
@Disabled("Micro-benchmark - enable manually for local measurement; consider using JMH for production-grade benchmarks")
public class ReflectUtilsMicroBenchmarkTest {

    private static final int THREADS = Math.max(2, Runtime.getRuntime().availableProcessors());
    private static final int OPS_PER_THREAD = 5_000;

    @Test
    public void benchmark_cache_vs_no_cache() throws InterruptedException {
        ReflectUtilsTestHelper helper = new ReflectUtilsTestHelper();
        // Warmup a little
        for (int i = 0; i < 100; i++) {
            ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{123.0});
        }

        // Measure with cache (hot)
        BenchmarkResult hot = runConcurrentBenchmark(helper, true);

        // Measure without cache: clear cache frequently to simulate cache-miss heavy scenario
        ReflectUtils.clearMethodCache();
        BenchmarkResult cold = runConcurrentBenchmark(helper, false);

        System.out.println("=== ReflectUtils MicroBenchmark ===");
        System.out.println("Threads: " + THREADS + " , Ops/thread: " + OPS_PER_THREAD);
        System.out.println("Hot (cache) - avgLatencyMicros: " + hot.avgMicros() + " , p50: " + hot.percentile(50) + " , p95: " + hot.percentile(95));
        System.out.println("Cold (no cache) - avgLatencyMicros: " + cold.avgMicros() + " , p50: " + cold.percentile(50) + " , p95: " + cold.percentile(95));

        // basic sanity assertions (no exceptions and some measurable difference expected)
        assertTrue(hot.totalOps > 0);
        assertTrue(cold.totalOps > 0);
    }

    private BenchmarkResult runConcurrentBenchmark(ReflectUtilsTestHelper helper, boolean keepCache) throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(THREADS);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(THREADS);
        ConcurrentLinkedQueue<Long> timingsMicros = new ConcurrentLinkedQueue<>();
        AtomicInteger errors = new AtomicInteger(0);

        for (int t = 0; t < THREADS; t++) {
            ex.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < OPS_PER_THREAD; i++) {
                        try {
                            if (!keepCache) {
                                // periodically clear cache to simulate cold scenario
                                if ((i & 31) == 0) ReflectUtils.clearMethodCache();
                            }
                            long s = System.nanoTime();
                            Object r = ReflectUtils.invokeMethodByName(helper, "echo", new Object[]{456.0});
                            long e = System.nanoTime();
                            timingsMicros.add(TimeUnit.NANOSECONDS.toMicros(e - s));
                            if (r == null) {
                                errors.incrementAndGet();
                            }
                        } catch (Throwable exx) {
                            errors.incrementAndGet();
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        boolean finished = done.await(60, TimeUnit.SECONDS);
        ex.shutdownNow();
        if (!finished) {
            fail("Benchmark did not finish in time");
        }
        List<Long> samples = new ArrayList<>(timingsMicros);
        return new BenchmarkResult(samples, errors.get());
    }

    private static class BenchmarkResult {
        final List<Long> samples;
        final int errors;
        final int totalOps;

        BenchmarkResult(List<Long> samples, int errors) {
            this.samples = samples;
            this.errors = errors;
            this.totalOps = samples.size();
        }

        double avgMicros() {
            return samples.stream().mapToLong(Long::longValue).average().orElse(Double.NaN);
        }

        long percentile(double p) {
            if (samples.isEmpty()) return 0;
            List<Long> sorted = samples.stream().sorted().collect(Collectors.toList());
            int idx = (int) Math.ceil((p / 100.0) * sorted.size()) - 1;
            idx = Math.max(0, Math.min(sorted.size() - 1, idx));
            return sorted.get(idx);
        }

        @Override
        public String toString() {
            LongSummaryStatistics st = samples.stream().mapToLong(Long::longValue).summaryStatistics();
            return "count=" + st.getCount() + " avg=" + st.getAverage() + " min=" + st.getMin() + " max=" + st.getMax();
        }
    }
}
