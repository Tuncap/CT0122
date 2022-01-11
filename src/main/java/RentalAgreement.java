import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class RentalAgreement {

    private final Tool tool;
    private final int daysRented;
    private final int discount;
    private final LocalDate startDate;
    private final LocalDate returnDate;
    private int chargeDays;
    private BigDecimal dailyCharge;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public RentalAgreement(String toolCode, int daysRented, int discount, LocalDate startDate) {
        this.tool = new ToolRepo().getToolByCode(toolCode);
        this.daysRented = daysRented;
        this.discount = discount;
        this.startDate = startDate;
        this.returnDate = startDate.plusDays(daysRented);
        populateChargeFields(daysRented, startDate);
    }

    private void populateChargeFields(int daysToRent, LocalDate startDate) {
        ToolPricingRepo toolPricingRepo = new ToolPricingRepo();
        ToolPricing toolPricing = toolPricingRepo.getPricingByToolType(tool.getToolType());
        this.dailyCharge = toolPricing.getDailyCharge();
        this.chargeDays = getChargeDays(toolPricing, startDate, startDate.plusDays(daysToRent));
        this.preDiscountCharge = toolPricing.getDailyCharge().multiply(BigDecimal.valueOf(chargeDays))
                .setScale(2,  RoundingMode.HALF_UP);
        this.discountAmount = getDiscountAmount();
        this.finalCharge = preDiscountCharge.subtract(discountAmount);
    }

    private BigDecimal getDiscountAmount(){
        BigDecimal discountDecimal = BigDecimal.valueOf(discount).divide(BigDecimal.valueOf(100));
        return preDiscountCharge.multiply(discountDecimal).setScale(2, RoundingMode.HALF_UP);
    }

    private int getChargeDays(ToolPricing toolPricing, LocalDate startDate, LocalDate endDate){
        Set<LocalDate> holidays = !toolPricing.isChargeHoliday() ?
                RentalDateUtil.getHolidaysWithinRange(startDate.getYear(), endDate.getYear())
                : new HashSet<>();
        Set<LocalDate> dates = startDate.plusDays(1).datesUntil(endDate.plusDays(1)).collect(Collectors.toSet());
        List<LocalDate> datesToRemove = new ArrayList<>();

        for (LocalDate date : dates) {
            if (!toolPricing.isChargeHoliday() && holidays.contains(date)) {
                datesToRemove.add(date);
            }

            if (!toolPricing.isChargeWeekday() && isDayWeekday(date.getDayOfWeek())) {
                datesToRemove.add(date);
            }

            if (!toolPricing.isChargeWeekend() && isDayWeekend(date.getDayOfWeek())) {
                datesToRemove.add(date);
            }
        }
        datesToRemove.forEach(dates::remove);
        return dates.size();
    }

    private boolean isDayWeekend(DayOfWeek dayOfWeek){
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isDayWeekday(DayOfWeek dayOfWeek) {
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    public void print() {
        System.out.format("Rental Agreement\n" +
                        "Tool code: %s\n" +
                        "Tool type: %s\n" +
                        "Tool brand: %s\n" +
                        "Rental days: %s\n" +
                        "Check out date: %s\n" +
                        "Due date: %s\n" +
                        "Daily rental charge: %s\n" +
                        "Charge days: %s\n" +
                        "Pre-discount charge: %s\n" +
                        "Discount percent: %s%%\n" +
                        "Discount amount: %s\n" +
                        "Final charge: %s\n",
                this.tool.getCode(),
                this.tool.getToolType(),
                this.tool.getBrand(),
                this.daysRented,
                RentalDateUtil.getTwoDigitFormat(this.startDate),
                RentalDateUtil.getTwoDigitFormat(this.returnDate),
                formatCurrency(this.dailyCharge),
                this.chargeDays,
                formatCurrency(this.preDiscountCharge),
                this.discount,
                formatCurrency(this.discountAmount),
                formatCurrency(this.finalCharge)
        );
    }

    public String formatCurrency(BigDecimal amount){
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }
}
