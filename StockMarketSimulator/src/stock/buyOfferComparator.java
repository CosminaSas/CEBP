package stock;

import java.util.Comparator;

import stock.dtos.Offer;

public class buyOfferComparator implements Comparator<Offer> {
    public int compare(Offer b1, Offer b2) {
        if (b1.getPrice() > b2.getPrice())
            return 1;
        else if (b1.getPrice() < b2.getPrice())
            return -1;
        return 0;
    }
}
