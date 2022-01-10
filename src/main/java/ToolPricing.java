import java.math.BigDecimal;

public class ToolPricing {
    private BigDecimal dailyCharge;
    private boolean chargeWeekday;
    private boolean chargeWeekend;
    private boolean chargeHoliday;

    public ToolPricing(BigDecimal dailyCharge, boolean chargeWeekday, boolean chargeWeekend, boolean chargeHoliday) {
        this.dailyCharge = dailyCharge;
        this.chargeWeekday = chargeWeekday;
        this.chargeWeekend = chargeWeekend;
        this.chargeHoliday = chargeHoliday;
    }

    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }

    public boolean isChargeWeekday() {
        return chargeWeekday;
    }

    public boolean isChargeWeekend() {
        return chargeWeekend;
    }

    public boolean isChargeHoliday() {
        return chargeHoliday;
    }
}
