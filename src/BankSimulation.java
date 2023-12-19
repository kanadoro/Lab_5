

import java.util.ArrayList;
import java.util.List;

public class BankSimulation {

    public static class BankAccount {
        private int accountNumber;
        private String accountName;
        private double balance;

        public BankAccount(int accountNumber, String accountName, double initialDeposit) {
            this.accountNumber = accountNumber;
            this.accountName = accountName;
            this.balance = initialDeposit;
        }

        public void deposit(double amount) {
            if (amount < 0) {
                throw new NegativeAmountException("Deposit amount cannot be negative.");
            }
            balance += amount;
        }

        public void withdraw(double amount) {
            if (amount < 0) {
                throw new NegativeAmountException("Withdrawal amount cannot be negative.");
            }
            if (amount > balance) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal.");
            }
            balance -= amount;
        }

        public double getBalance() {
            return balance;
        }

        public String getAccountSummary() {
            return "Account Number: " + accountNumber + "\nAccount Name: " + accountName + "\nBalance: $" + balance;
        }

        public int getAccountNumber() {
            return accountNumber;
        }
    }

    public static class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    public static class NegativeAmountException extends RuntimeException {
        public NegativeAmountException(String message) {
            super(message);
        }
    }

    public static class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

    public static class Bank {
        private List<BankAccount> accounts;

        public Bank() {
            this.accounts = new ArrayList<>();
        }

        public void createAccount(String accountName, double initialDeposit) {
            if (initialDeposit < 0) {
                throw new NegativeAmountException("Initial deposit cannot be negative.");
            }

            int accountNumber = generateAccountNumber();
            BankAccount newAccount = new BankAccount(accountNumber, accountName, initialDeposit);
            accounts.add(newAccount);
        }

        public BankAccount findAccount(int accountNumber) {
            for (BankAccount account : accounts) {
                if (account.getAccountNumber() == accountNumber) {
                    return account;
                }
            }
            throw new AccountNotFoundException("Account not found with account number: " + accountNumber);
        }

        public void transferMoney(int fromAccountNumber, int toAccountNumber, double amount) {
            if (amount < 0) {
                throw new NegativeAmountException("Transfer amount cannot be negative.");
            }

            BankAccount fromAccount = findAccount(fromAccountNumber);
            BankAccount toAccount = findAccount(toAccountNumber);

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);
        }

        private int generateAccountNumber() {
            // Logic for generating a unique account number
            return accounts.size() + 1;
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank();

        try {
            bank.createAccount("John Doe", -500);
        } catch (NegativeAmountException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            bank.transferMoney(1, 2, 100);
        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        bank.createAccount("Alice Wonderland", 1000);

        // Create account 3 before transferring money to it
        bank.createAccount("Charlie Chaplin", 0);

        try {
            bank.transferMoney(1, 3, 1500);
        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        bank.createAccount("Bob Builder", 500);
        try {
            bank.transferMoney(2, 3, 200);
        } catch (AccountNotFoundException | NegativeAmountException | InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Account 1: \n" + bank.findAccount(1).getAccountSummary());
        System.out.println("\nAccount 2: \n" + bank.findAccount(2).getAccountSummary());
        System.out.println("\nAccount 3: \n" + bank.findAccount(3).getAccountSummary());
    }

}
