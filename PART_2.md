## Part 2: The Parallel Frame Loader

"The servos are cooling down\!" says the Chief Elf, wiping sweat from his brow. "But look at the EPVs\! They are still
wobbling."

You turn your attention to the **Tri-Loader Arm**. This system takes the totes and arranges them into an **EPV Frame**.
A **Frame** contains exactly **3 Slots** (Left, Middle, Right) sitting side-by-side.

You must process the tote stream again. This time, instead of just cutting the stream, you are deciding which **Slot**
in the current Frame gets the next tote.

**The Mechanics**

1. **State:** You maintain 3 active stacks simultaneously (Slot 1, Slot 2, Slot 3). Initially, all are empty.
2. **Placement:** Take the *next* tote from the stream. Place it on top of the stack in Slot 1, 2, or 3.
    * *Constraint:* The placement must be valid (Temp & Weight rules from Part 1 apply to the specific slot you choose).
    * *Capacity:* A single slot can hold max **4** totes.
3. **Shipping (The Reset):**
   At any point between tote placements, you can decide to **Ship the Frame**.
    * This clears **ALL** 3 slots (even if some are empty).
    * You incur the costs (see below).
    * You start fresh with 3 empty slots for the remaining stream.
    * *Forced Shipping:* If the next tote fits in **none** of the current slots (due to rules or capacity), you **MUST**
      ship the current Frame immediately and place the tote in the new, empty Frame.

**The Cost Function**
We need to balance **Logistics** (number of frames used) against **Stability** (how balanced the frame is).

Every time a Frame is shipped, calculate the cost:

1. **Base Logistics Cost:** **1000** (Deploying an EPV is expensive).
2. **Imbalance Penalty:** Calculate the difference between the heaviest slot and the lightest slot in that frame.
   $$Penalty = (Weight_{HeaviestSlot} - Weight_{LightestSlot})^2$$
   *(Note: Empty slots count as Weight 0).*

**The Objective**
Process the entire stream. You must decide where to place every tote and when to ship the Frames to achieve the lowest
possible score.

**Example Walkthrough**
Stream: `A20 C10 C10`

* **Step 1:** Tote `A20`. Frame is `[0, 0, 0]`.
    * Place in Slot 1. State: `[A20, 0, 0]`.
* **Step 2:** Tote `C10`.
    * Cannot put on Slot 1 (`C10` on `A20` is invalid).
    * Place in Slot 2. State: `[A20, C10, 0]`.
* **Step 3:** Tote `C10`.
    * Can go on Slot 2 (`A` on `A`, $10 \le 10$). Or Slot 3.
    * **Strategy A:** Put on Slot 2. State: `[A20, C10+C10, 0]`.
        * Ship now. Weights: $\{20, 20, 0\}$.
        * Cost: $1000 + (20 - 0)^2 = 1400$.
    * **Strategy B:** Put on Slot 3. State: `[A20, C10, C10]`.
        * Ship now. Weights: $\{20, 10, 10\}$.
        * Cost: $1000 + (20 - 10)^2 = 1100$.

*Strategy B is optimal.*

**What is the Minimum Total Cost (Logistics + Imbalance) to clear the entire stream?**

Your answer: _______________