package com.mehrdad.falahati;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PartTwo {

    public static void main(String[] args) {
        String input = "A20 C10 C10";
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

        FrameState initialState = new FrameState();

        Map<String, Integer> memo = new HashMap<>();

        int result = findMinCost(totes, 0, initialState, memo);

        System.out.println("Part 2 - Minimum Total Cost: " + result);
    }

    private static int findMinCost(List<Tote> totes, int position, FrameState state, Map<String, Integer> memo) {
        if (position == totes.size()) {
            return state.getShipCost();
        }

        String key = createKey(position, state);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        Tote currentTote = totes.get(position);
        int minCost = Integer.MAX_VALUE;

        boolean canPlace = false;

        if (canAddToSlot(state.slot1(), currentTote)) {
            canPlace = true;
            FrameState newState = state.copy();
            newState.slot1().add(currentTote);

            int cost1 = findMinCost(totes, position + 1, newState, memo);
            minCost = Math.min(minCost, cost1);

            int cost2 = newState.getShipCost() + findMinCost(totes, position + 1, new FrameState(), memo);
            minCost = Math.min(minCost, cost2);
        }

        if (canAddToSlot(state.slot2(), currentTote)) {
            canPlace = true;
            FrameState newState = state.copy();
            newState.slot2().add(currentTote);

            int cost1 = findMinCost(totes, position + 1, newState, memo);
            minCost = Math.min(minCost, cost1);

            int cost2 = newState.getShipCost() + findMinCost(totes, position + 1, new FrameState(), memo);
            minCost = Math.min(minCost, cost2);
        }

        if (canAddToSlot(state.slot3(), currentTote)) {
            canPlace = true;
            FrameState newState = state.copy();
            newState.slot3().add(currentTote);

            int cost1 = findMinCost(totes, position + 1, newState, memo);
            minCost = Math.min(minCost, cost1);

            int cost2 = newState.getShipCost() + findMinCost(totes, position + 1, new FrameState(), memo);
            minCost = Math.min(minCost, cost2);
        }

        if (!canPlace) {
            int shipCost = state.getShipCost();
            minCost = shipCost + findMinCost(totes, position, new FrameState(), memo);
        }

        memo.put(key, minCost);
        return minCost;
    }

    private static String createKey(int position, FrameState state) {
        return position + "|" +
                serializeSlot(state.slot1()) + "|" +
                serializeSlot(state.slot2()) + "|" +
                serializeSlot(state.slot3());
    }

    private static String serializeSlot(List<Tote> slot) {
        if (slot.isEmpty()) return "[]";
        return slot.stream()
                .map(t -> "" + t.type() + t.weight())
                .collect(Collectors.joining(",", "[", "]"));
    }

    private static boolean canAddToSlot(List<Tote> slot, Tote newTote) {
        if (slot.size() >= 4) return false;

        if (slot.isEmpty()) return true;

        Tote top = slot.get(slot.size() - 1);

        if (top.getTemperature() > newTote.getTemperature()) return false;

        return top.weight() >= newTote.weight();
    }
}
