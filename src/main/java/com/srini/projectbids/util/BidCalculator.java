package com.srini.projectbids.util;

import com.srini.projectbids.core.Bid;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;

public class BidCalculator {
    public static Bid findLeastBid(Collection<Bid> bidders){
        if (bidders == null || bidders.isEmpty()) {
            return null;
        }
        List<Bid> tempList = new ArrayList<>(bidders);
        Collections.sort(tempList);
        return tempList.get(0);
    }
}
