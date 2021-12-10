package stock;

import java.util.Comparator;

import stock.dtos.Offer;

public class SellOfferComparator implements Comparator<Offer> {
    public int compare(Offer s1, Offer s2) {
        if (s1.getPrice() > s2.getPrice())
            return 1;
        else if (s1.getPrice() < s2.getPrice())
            return -1;
        return 0;
    }
}
