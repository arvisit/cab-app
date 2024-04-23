package by.arvisit.cabapp.paymentservice.util;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static int getLastPageNumber(long itemsCount, int pageSize) {
        long pages = itemsCount / pageSize;
        long remainder = itemsCount % pageSize;
        return remainder == 0 ? (int) (pages - 1) : (int) pages;
    }
}
