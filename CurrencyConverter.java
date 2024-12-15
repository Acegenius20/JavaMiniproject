import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import java.util.Scanner;

// Currency enum
enum Currency {
    USD("United States Dollar"),
    EUR("Euro"),
    GBP("British Pound"),
    JPY("Japanese Yen"),
    AUD("Australian Dollar"),
    CAD("Canadian Dollar"),
    CHF("Swiss Franc");

    private final String fullName;

    Currency(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}

// Currency Converter Class
public class CurrencyConverter {
    private static final Map<Currency, BigDecimal> exchangeRates = new EnumMap<>(Currency.class);

    static {
        // Exchange rates relative to USD
        exchangeRates.put(Currency.USD, BigDecimal.ONE);
        exchangeRates.put(Currency.EUR, new BigDecimal("0.92"));
        exchangeRates.put(Currency.GBP, new BigDecimal("0.79"));
        exchangeRates.put(Currency.JPY, new BigDecimal("149.50"));
        exchangeRates.put(Currency.AUD, new BigDecimal("1.52"));
        exchangeRates.put(Currency.CAD, new BigDecimal("1.35"));
        exchangeRates.put(Currency.CHF, new BigDecimal("0.88"));
    }

    // Method to convert between two currencies
    public static BigDecimal convert(BigDecimal amount, Currency from, Currency to) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be non-negative.");
        }

        BigDecimal fromRate = exchangeRates.getOrDefault(from, BigDecimal.ONE);
        BigDecimal toRate = exchangeRates.getOrDefault(to, BigDecimal.ONE);

        // Conversion: amount in USD * targetRate / baseRate
        BigDecimal amountInUSD = amount.divide(fromRate, 6, RoundingMode.HALF_UP);
        return amountInUSD.multiply(toRate).setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Print supported currencies
        System.out.println("Supported Currencies:");
        for (Currency currency : Currency.values()) {
            System.out.printf("%s - %s%n", currency.name(), currency.getFullName());
        }

        try {
            // Input: amount to convert
            System.out.print("\nEnter the amount to convert: ");
            BigDecimal amount = scanner.nextBigDecimal();

            // Input: source currency
            System.out.print("Enter source currency code (e.g., USD): ");
            Currency fromCurrency = Currency.valueOf(scanner.next().toUpperCase());

            // Input: target currency
            System.out.print("Enter target currency code (e.g., EUR): ");
            Currency toCurrency = Currency.valueOf(scanner.next().toUpperCase());

            // Perform conversion
            BigDecimal convertedAmount = convert(amount, fromCurrency, toCurrency);

            // Output result
            System.out.printf("\n%.2f %s = %.2f %s%n",
                    amount, fromCurrency.name(),
                    convertedAmount, toCurrency.name()
            );
        } catch (IllegalArgumentException e) {
            System.err.println("Error: Invalid input. Please enter valid currency codes.");
        } finally {
            scanner.close();
        }
    }
}
