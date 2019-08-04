package com.github.rosklyar.wordnet.impl;

import com.github.rosklyar.wordnet.utils.Digraph;
import org.javatuples.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.Arrays.stream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 1)
@Measurement(iterations = 1)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"}, warmups = 2)
public class CompareBFSWordNetTest {

    private static final Pair<Digraph, Map<String, List<Integer>>> digraph = Helper.buildDigraph("synsets.txt", "hypernyms.txt");
    private static final WordNet wordNetSimple = new DigraphWordNet(digraph.getValue1(), new SimpleBFSShortestCommonAncestor(digraph.getValue0()));
    private static final WordNet wordNetUpgraded = new DigraphWordNet(digraph.getValue1(), new UpgradedBFSShortestCommonAncestor(digraph.getValue0()));
    private static final Outcast outcastSimple = new WordNetOutcast(wordNetSimple);
    private static final Outcast outcastUpgraded = new WordNetOutcast(wordNetUpgraded);
    private static final List<String> nouns = Helper.nouns("synsets.txt");
    private List<String[]> testCases;

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup
    public void setup() {
        testCases = generateTestSet(10, 5);
    }

    @Benchmark
    public void outcastSimple() {
        for (String[] testCase : testCases) {
            outcastSimple.outcast(testCase);
        }
    }

    @Benchmark
    public void outcastUpgraded() {
        for (String[] testCase : testCases) {
            outcastUpgraded.outcast(testCase);
        }
    }

    @Test
    public void correctness() {
        List<String[]> testCases = generateTestSet(10000, 5);
        for (String[] testCase : testCases) {
            Assertions.assertEquals(outcastUpgraded.outcast(testCase), outcastSimple.outcast(testCase));
        }
    }

    private static List<String[]> generateTestSet(int numberOfTests, int numberOfWords) {
        List<String[]> testSet = new ArrayList<>(numberOfTests);
        IntStream.range(0, numberOfTests).forEach(i -> testSet.add(i, getNouns(numberOfWords)));
        return testSet;
    }

    private static String[] getNouns(int numberOfWords) {
        return stream(generate(numberOfWords, nouns.size()))
                .mapToObj(nouns::get)
                .toArray(String[]::new);
    }

    private static int[] generate(int size, int bound) {
        Random rd = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rd.nextInt(bound);
        }
        return arr;
    }
}