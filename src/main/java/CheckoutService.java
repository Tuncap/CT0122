public class CheckoutService {

    public static void process(CheckoutRequest request){
        validate(request);
        RentalAgreement rentalAgreement = new RentalAgreement(request.getToolCode(), request.getDaysToRent(),
                request.getDiscount(), request.getCheckoutDate());
        rentalAgreement.print();
    }

    private static void validate(CheckoutRequest request){
        if(request.getDaysToRent() < 1){
            throw new IllegalArgumentException(String.format("%s day(s) to rent is invalid.  Days to rent equipment must be greater than 0.",
                    request.getDaysToRent()));
        }
        if(request.getDiscount() < 0 || request.getDiscount() > 100 ){
            throw new IllegalArgumentException(String.format("%s%%  is out of range.  Discount must within 0 and 100.", request.getDiscount()));
        }
    }

}
