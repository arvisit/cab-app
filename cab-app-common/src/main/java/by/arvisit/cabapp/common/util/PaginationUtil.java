package by.arvisit.cabapp.common.util;

public final class PaginationUtil {

    private PaginationUtil() {
    }

    public static int getLastPageNumber(long itemsCount, int pageSize) {
        long pages = itemsCount / pageSize;
        long remainder = itemsCount % pageSize;

        if (itemsCount == 0) {
            return 0;
        }
        return remainder == 0 ? (int) (pages - 1) : (int) pages;
    }
}
