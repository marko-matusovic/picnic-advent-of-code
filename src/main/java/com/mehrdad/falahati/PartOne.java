package com.mehrdad.falahati;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class PartOne {

    private static int[] cost = new int[5];

    static {
        cost[1] = 50;
        cost[2] = 25;
        cost[3] = 10;
        cost[4] = 0;
    }

    public static void main(String[] args) {
        String input = "F45 C20 F60 C45 A20 A15 A07";

        minimumHandlingCost(input.lines());

        try (Stream<String> lines = Files.lines(Path.of("INPUT"))) {

            minimumHandlingCost(lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void minimumHandlingCost(Stream<String> lines) {
        List<Tote> totes = lines
                .map(input -> input.trim().split(" "))
                .flatMap(parts -> Arrays.stream(parts).map(Tote::new))
                .toList();

        int[] dp = new int[totes.size() + 1];
        dp[0] = 0;

        for (var i = 1; i <= totes.size(); i++) {
            dp[i] = Integer.MAX_VALUE;
        }

        findMinimumCost(dp, totes);
        int answer = dp[totes.size()];

        System.out.println("Minimum Handling Cost: " + answer);
    }

    private static void findMinimumCost(int[] dp, List<Tote> tots) {
        for (var i = 1; i <= tots.size(); i++) {
            for (var stackSize = 1; stackSize <= Math.min(4, i); stackSize++) {
                var stackStrat = i - stackSize;

                if (isValidStack(tots, stackStrat, i)) {
                    var newCost = dp[stackStrat] + cost[stackSize];
                    dp[i] = Math.min(dp[i], newCost);
                }
            }
        }
    }

    private static boolean isValidStack(List<Tote> totes, int strat, int end) {
        for (var i = strat; i < end - 1; i++) {
            var bottom = totes.get(i);
            var top = totes.get(i + 1);

            if (bottom.getTemperature() > top.getTemperature())
                return false;

            if (bottom.weight() < top.weight())
                return false;
        }
        return true;
    }
}
