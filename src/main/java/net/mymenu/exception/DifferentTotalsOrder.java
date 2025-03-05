package net.mymenu.exception;

public class DifferentTotalsOrder extends MyMenuException {
    public DifferentTotalsOrder() {
        super("Sum of items is different from the total");
    }
}
