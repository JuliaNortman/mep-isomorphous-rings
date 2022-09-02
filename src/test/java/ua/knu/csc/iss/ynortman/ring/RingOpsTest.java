package ua.knu.csc.iss.ynortman.ring;

import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.RingOps;

import static org.junit.jupiter.api.Assertions.*;

public class RingOpsTest {
    @Test
    public void givenCoefs_whenCalculateGenG_thenReturnRingUnityRowTest() {
        int[] expectedRow = {1, 3, 4, 2, 5, 0};
        int[] actualRow = RingOps.genG(5, 4, 6);
        assertArrayEquals(expectedRow, actualRow);
    }
}
