import java.time.LocalDate;

public class CheckoutRequest {
    private String toolCode;
    private int daysToRent;
    private int discount;
    private LocalDate checkoutDate;

    public CheckoutRequest(String toolCode, int daysToRent, int discount, LocalDate checkoutDate) {
        this.toolCode = toolCode;
        this.daysToRent = daysToRent;
        this.discount = discount;
        this.checkoutDate = checkoutDate;
    }

    public String getToolCode() {
        return toolCode;
    }

    public int getDaysToRent() {
        return daysToRent;
    }

    public int getDiscount() {
        return discount;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

}
