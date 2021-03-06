package com.walmart.app;

import java.util.*;

public final class Utilities {

    /*
    Returns a block of continuous space as it is.
    Removes it from the rowSpaceMap and continuousSpaceMap.
     */
    public static SeatsBlock assignSeatBlocks(Map<Integer, List<Integer>> continuousSpaceMap, int numSeats, Map<Integer, List<List<Integer>>> rowSpaceMap) {
        SeatsBlock seatsBlock = null;
        int row = continuousSpaceMap.get(numSeats).remove(0);
        if (continuousSpaceMap.get(numSeats).isEmpty())
            continuousSpaceMap.remove(numSeats);
        List<List<Integer>> rowBlock = rowSpaceMap.get(row);
        for (List<Integer> block : rowBlock)
            if(block.get(1)-block.get(0)+1 == numSeats) {
                seatsBlock = new SeatsBlock(row, block.get(0), block.get(1));
                rowBlock.remove(block);
                break;
            }
        return seatsBlock;
    }

    /*
    Modifies the maximum available space block to accommodate the required numSeats.
    Updates both the continuousSpaceMap and rowSpaceMap accordingly.
     */
    public static SeatsBlock assignRemainingSeatBlock(TreeMap<Integer, List<Integer>> continuousSpaceMap, int numSeats, Map<Integer, List<List<Integer>>> rowSpaceMap) {
        SeatsBlock seatsBlock = null;
        int availableSpace = continuousSpaceMap.lastKey();
        int row = continuousSpaceMap.get(availableSpace).remove(0);
        if (continuousSpaceMap.get(availableSpace).isEmpty())
            continuousSpaceMap.remove(availableSpace);
        int newAvailableForThatBlock = availableSpace - numSeats;
        List<Integer> newRow = new ArrayList<>();
        if (continuousSpaceMap.containsKey(newAvailableForThatBlock))
            newRow = continuousSpaceMap.get(newAvailableForThatBlock);
        newRow.add(row);
        continuousSpaceMap.put(newAvailableForThatBlock, newRow);
        List<List<Integer>> rowBlock = rowSpaceMap.get(row);
        for (List<Integer> block : rowBlock)
            if(block.get(1)-block.get(0)+1 == availableSpace) {
                seatsBlock = new SeatsBlock(row, block.get(0), block.get(0)+numSeats-1);
                block.set(0,block.get(0)+numSeats);
                break;
            }
        return seatsBlock;
    }

    /*
    checks if the previous block is part of a continuous space and updates the previous blocks end to the current
    blocks end and removes the current block from the rowSpaceMap. The continuous spaces map should also be modified
    accordingly. The previous block space and current block space is removed from the continuous space map here, and
    the new continuous space is sent back to be added into the continuous space map there.
     */
    public static Integer mergeLeftRowMap(List<List<Integer>> rowSeatBlocks, int index, TreeMap<Integer, List<Integer>> continuousSpaceMap, SeatsBlock seatsBlock) {
        if (index == 0 || index >= rowSeatBlocks.size())
            return 0;
        int newContinuousSpace = 0;
        int previousBlockSpace;
        int currentBlockSpace;
        if (rowSeatBlocks.get(index).get(0) == rowSeatBlocks.get(index-1).get(1)+1) {
            newContinuousSpace = rowSeatBlocks.get(index).get(1) - rowSeatBlocks.get(index-1).get(0) + 1;
            previousBlockSpace = rowSeatBlocks.get(index-1).get(1) - rowSeatBlocks.get(index-1).get(0) + 1;
            currentBlockSpace = rowSeatBlocks.get(index).get(1) - rowSeatBlocks.get(index).get(0) + 1;
            rowSeatBlocks.get(index-1).set(1,rowSeatBlocks.get(index).get(1));
            rowSeatBlocks.remove(index);
            continuousSpaceMap.get(previousBlockSpace).remove((Integer) seatsBlock.getRow());
            continuousSpaceMap.get(currentBlockSpace).remove((Integer) seatsBlock.getRow());
            if (continuousSpaceMap.get(previousBlockSpace).isEmpty())
                continuousSpaceMap.remove(previousBlockSpace);
            if (continuousSpaceMap.containsKey(currentBlockSpace) && continuousSpaceMap.get(currentBlockSpace).isEmpty())
                continuousSpaceMap.remove(currentBlockSpace);
        }
        return newContinuousSpace;
    }
}
