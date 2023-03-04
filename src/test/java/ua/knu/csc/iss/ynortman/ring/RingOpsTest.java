package ua.knu.csc.iss.ynortman.ring;

import org.junit.jupiter.api.Test;
import ua.knu.csc.iss.ynortman.ring.model.RingInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RingOpsTest {
    @Test
    public void givenCoefs_whenCalculateGenG_thenReturnRingUnityRowTest() {
        int k = 6;
        RingInteger[] expectedRow = {
                RingInteger.valueOf(1, k),
                RingInteger.valueOf(3, k),
                RingInteger.valueOf(4, k),
                RingInteger.valueOf(2, k),
                RingInteger.valueOf(5, k),
                RingInteger.valueOf(0, k)
        };
        RingInteger[] actualRow = RingOps.genG(5, 4, 6);
        assertArrayEquals(expectedRow, actualRow);
    }

    @Test
    public void givenUnityRow_whenCalculateAdditionTable_thenReturnAdditionTable() {
        int[][] expectedTable = {
                {0,1,2,3,4,5},
                {1,3,4,2,5,0},
                {2,4,0,5,1,3},
                {3,2,5,4,0,1},
                {4,5,1,0,3,2},
                {5,0,3,1,2,4}
        };
        int[] unityRow = {1, 3, 4, 2, 5, 0};
        int[][] actualTable = RingOps.additionTable(6, unityRow);
        assertArrayEquals(expectedTable, actualTable);
    }

    @Test
    public void givenAddtionTable_whenCalculateMultTable_thenReturnMultTable() {
        int[][] additionTable = {
                {0,1,2,3,4,5},
                {1,3,4,2,5,0},
                {2,4,0,5,1,3},
                {3,2,5,4,0,1},
                {4,5,1,0,3,2},
                {5,0,3,1,2,4}
        };
        int[][] expectedTable = {
                {0,0,0,0,0,0},
                {0,1,2,3,4,5},
                {0,2,2,0,0,2},
                {0,3,0,4,3,4},
                {0,4,0,3,4,3},
                {0,5,2,4,3,1}
        };
        int[][] actualTable = RingOps.multiplicationTable(6, additionTable);
        assertArrayEquals(expectedTable, actualTable);
    }
}
