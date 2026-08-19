import java.util.Scanner;

public class ElectricBillEstimator {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String customerName;
        double previousReading;
        double currentReading;
        int householdSize;
        int billingMonth;
        double ratePerKwh;

        double kwhUsed;
        double effectiveRate;
        double totalBill;
        double kwhPerPerson;
        String usageLevel;

        System.out.println("========================================");
        System.out.println("   MONTHLY ELECTRIC BILL ESTIMATOR");
        System.out.println("========================================");
        System.out.println();

        System.out.print("1. Enter the customer name: ");
        customerName = input.nextLine();

        System.out.print("2. Enter the PREVIOUS meter reading (kWh): ");
        previousReading = input.nextDouble();

        System.out.print("3. Enter the CURRENT meter reading (kWh): ");
        currentReading = input.nextDouble();

        System.out.print("4. Enter the number of people in the household: ");
        householdSize = input.nextInt();

        System.out.print("5. Enter the billing month (1 = January to 12 = December): ");
        billingMonth = input.nextInt();

        System.out.print("6. Enter the base rate per kWh (in pesos): ");
        ratePerKwh = input.nextDouble();

        System.out.println();

        if (currentReading < previousReading || householdSize <= 0 || ratePerKwh <= 0) {

            System.out.println("INVALID INPUT. Please check the following:");
            System.out.println(" - The current reading must be equal to or higher than the previous reading.");
            System.out.println(" - The household size must be at least 1 person.");
            System.out.println(" - The rate per kWh must be greater than zero.");
            System.out.println("Please run the program again with correct values.");

        } else {

            kwhUsed = currentReading - previousReading;

            if (kwhUsed <= 100) {
                usageLevel = "LIFELINE (very low consumption)";
                effectiveRate = ratePerKwh * 0.80;

            } else if (kwhUsed <= 300) {
                usageLevel = "NORMAL";
                effectiveRate = ratePerKwh;

            } else if (kwhUsed <= 500) {
                usageLevel = "HIGH";
                effectiveRate = ratePerKwh * 1.15;

            } else {
                usageLevel = "VERY HIGH";
                effectiveRate = ratePerKwh * 1.30;
            }

            totalBill = kwhUsed * effectiveRate;
            kwhPerPerson = kwhUsed / householdSize;

            System.out.println("----------------------------------------");
            System.out.println("           BILLING SUMMARY");
            System.out.println("----------------------------------------");
            System.out.println("Customer          : " + customerName);
            System.out.println("Household size    : " + householdSize + " person(s)");
            System.out.printf("Previous reading  : %.2f kWh%n", previousReading);
            System.out.printf("Current reading   : %.2f kWh%n", currentReading);
            System.out.printf("Electricity used  : %.2f kWh%n", kwhUsed);
            System.out.println("Usage level       : " + usageLevel);
            System.out.printf("Rate applied      : PHP %.2f per kWh%n", effectiveRate);
            System.out.printf("TOTAL BILL        : PHP %.2f%n", totalBill);
            System.out.printf("Usage per person  : %.2f kWh%n", kwhPerPerson);
            System.out.println("----------------------------------------");
            System.out.println();

            System.out.println("ASSESSMENT:");

            if (kwhUsed > 300 && kwhPerPerson > 120) {

                if (billingMonth >= 3 && billingMonth <= 5) {
                    System.out.println("- Your consumption is high, but this is the hot season.");
                    System.out.println("- Cooling appliances are the most likely cause.");
                    System.out.println("- Set the air conditioner to 25 degrees and use a timer at night.");

                } else if (householdSize >= 5 || kwhUsed > 700) {
                    System.out.println("- Your consumption is high for a large or heavily loaded household.");
                    System.out.println("- Check for appliances left plugged in and old, inefficient units.");
                    System.out.println("- Consider staggering the use of the washing machine and electric iron.");

                } else {
                    System.out.println("- WARNING: this consumption is unusually high for your household size.");
                    System.out.println("- Please inspect your wiring for a possible leak or faulty meter.");
                    System.out.println("- Compare this bill with the same month last year.");
                }

            } else if (kwhUsed <= 100 && householdSize >= 4) {
                System.out.println("- Excellent. A household of " + householdSize
                        + " staying under 100 kWh is very efficient.");
                System.out.println("- You qualify for the lifeline discounted rate.");

            } else {
                System.out.println("- Your consumption is within the expected range for your household.");
                System.out.println("- Keep monitoring your meter every month to catch sudden increases.");
            }

            System.out.println();
            System.out.println("Thank you for using the Monthly Electric Bill Estimator.");
        }

        input.close();
    }
}
