package com.mehrdad.falahati;

import java.util.ArrayList;
import java.util.List;

public record FrameState(List<Tote> slot1, List<Tote> slot2, List<Tote> slot3) {

    FrameState() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public int getSlotWeight(List<Tote> slot) {
        return slot.stream().mapToInt(Tote::weight).sum();
    }

    public int getShipCost() {
        var weight1 = getSlotWeight(slot1);
        var weight2 = getSlotWeight(slot2);
        var weight3 = getSlotWeight(slot3);

        var max = Math.max(weight1, Math.max(weight2, weight3));
        var min = Math.min(weight1, Math.min(weight2, weight3));

        return 1000 + (max - min) * (max - min);
    }

    public FrameState copy() {
        return new FrameState(new ArrayList<>(this.slot1), new ArrayList<>(this.slot2), new ArrayList<>(this.slot3));
    }
}
