

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterWithLogin {
    // Map to hold exchange rates loaded from a file
    private static final Map<String, Double> exchangeRates = new HashMap<>();
    private static final String RATES_FILE = "exchange_rates.txt";

    private JFrame loginFrame, converterFrame;
    private JTextField userField, amountField;
    private JPasswordField passField;
    private JTextArea resultArea;

    // Hardcoded login credentials
    private final String validUsername = "user";
    private final String validPassword = "password";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loadExchangeRates();
            new CurrencyConverterWithLogin().createLoginGUI();
        });
    }

    // Load exchange rates from a file
    private static void loadExchangeRates() {
        File file = new File(RATES_FILE);
        if (!file.exists()) {
            // Initialize file with default rates if not present
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("USD_TO_INR=83.0\n");
                writer.write("INR_TO_USD=0.012\n");
                writer.write("USD_TO_EUR=0.92\n");
                writer.write("EUR_TO_USD=1.08\n");
                writer.write("USD_TO_GBP=0.78\n");
                writer.write("GBP_TO_USD=1.28\n");
                writer.write("INR_TO_EUR=0.011\n");
                writer.write("EUR_TO_INR=91.0\n");
                writer.write("INR_TO_GBP=0.0094\n");
                writer.write("GBP_TO_INR=106.0\n");
                writer.write("EUR_TO_GBP=0.85\n");
                writer.write("GBP_TO_EUR=1.18\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Read rates from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    exchangeRates.put(parts[0], Double.parseDouble(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Login GUI
    private void createLoginGUI() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        userField = new JTextField();
        passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginFrame.add(userLabel);
        loginFrame.add(userField);
        loginFrame.add(passLabel);
        loginFrame.add(passField);
        loginFrame.add(new JLabel()); // Empty space
        loginFrame.add(loginButton);

        loginButton.addActionListener(e -> validateLogin());

        loginFrame.setVisible(true);
    }

    // Validate login credentials
    private void validateLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals(validUsername) && password.equals(validPassword)) {
            JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
            loginFrame.dispose();
            createConverterGUI();
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Invalid Credentials. Try Again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Currency Converter GUI
    private void createConverterGUI() {
        converterFrame = new JFrame("Currency Converter");
        converterFrame.setSize(400, 300);
        converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        converterFrame.setLayout(new GridLayout(5, 2));

        JLabel amountLabel = new JLabel("Enter Amount:");
        JLabel fromLabel = new JLabel("From Currency:");
        JLabel toLabel = new JLabel("To Currency:");
        amountField = new JTextField();
        JComboBox<String> fromCurrency = new JComboBox<>(new String[]{"USD", "INR", "EUR", "GBP"});
        JComboBox<String> toCurrency = new JComboBox<>(new String[]{"USD", "INR", "EUR", "GBP"});
        JButton convertButton = new JButton("Convert");
        resultArea = new JTextArea();

        converterFrame.add(amountLabel);
        converterFrame.add(amountField);
        converterFrame.add(fromLabel);
        converterFrame.add(fromCurrency);
        converterFrame.add(toLabel);
        converterFrame.add(toCurrency);
        converterFrame.add(new JLabel()); // Empty space
        converterFrame.add(convertButton);
        converterFrame.add(new JLabel("Result:"));
        converterFrame.add(resultArea);

        convertButton.addActionListener(e -> performConversion(fromCurrency, toCurrency));

        converterFrame.setVisible(true);
    }

    // Perform currency conversion
    private void performConversion(JComboBox<String> fromCurrency, JComboBox<String> toCurrency) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            String from = fromCurrency.getSelectedItem().toString();
            String to = toCurrency.getSelectedItem().toString();

            if (from.equals(to)) {
                resultArea.setText("No conversion needed.");
                return;
            }

            String key = from + "_TO_" + to;
            if (exchangeRates.containsKey(key)) {
                double rate = exchangeRates.get(key);
                double convertedAmount = amount * rate;
                resultArea.setText(String.format("%.2f %s = %.2f %s", amount, from, convertedAmount, to));
                saveConversionData(amount, from, convertedAmount, to);
            } else {
                resultArea.setText("Conversion rate not available.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(converterFrame, "Invalid Amount Entered!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Save conversion data to a file
    private void saveConversionData(double amount, String from, double convertedAmount, String to) {
        try (FileWriter writer = new FileWriter("conversion_data.txt", true)) {
            writer.write(String.format("%.2f %s -> %.2f %s%n", amount, from, convertedAmount, to));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(converterFrame, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

