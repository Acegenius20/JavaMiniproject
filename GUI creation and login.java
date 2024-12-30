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
