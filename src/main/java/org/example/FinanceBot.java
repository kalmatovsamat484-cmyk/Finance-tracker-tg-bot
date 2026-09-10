package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class FinanceBot extends TelegramLongPollingBot {

    FinanceService financeService = new FinanceService();
    boolean waitingForWallet=false;
    boolean waitingforAmount=false;
    int choose;
    boolean waitingForSavings=false;
    boolean waitingForSavingsAmount=false;

    @Override
    public String getBotUsername() {
        return "demo12_finance_bot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText);

            if (messageText.equals("/start")) {

                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                message.setText("Welcome to Finance Tracker!");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (messageText.equals("/balance")) {
                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                message.setText(
                        "Your balance: " + financeService.getBalance()
                );

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (messageText.startsWith("/income")) {
                String[] parts = messageText.split(" ");
                if (parts.length == 2) {

                    try {
                        SendMessage message = new SendMessage();
                        double amount = Double.parseDouble(parts[1]);
                        message.setChatId(String.valueOf(chatId));
                        if (amount <= 0) {
                            message.setText("Income must be greater than zero");
                            execute(message);
                        } else {
                            financeService.addIncome(amount);
                            message.setText("Income: +" + amount);
                            execute(message);
                        }
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Amount must be a number");

                        try {
                            execute(message);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }

                    }
                } else {
                    try {
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Please enter an amount. Example: /income 5000");
                        execute(message);
                    } catch (TelegramApiException e) {

                    }
                }
            }
            else if (messageText.equals("/expense")) {

                        SendMessage message = new SendMessage();

                        message.setChatId(String.valueOf(chatId));
                        message.setText("""
                                Choose a wallet:
                                1. Balance
                                2. Savings
                                """);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                waitingForWallet =true;
                }
            else if (waitingForWallet) {
                SendMessage message = new SendMessage();
                 choose = Integer.parseInt(messageText);
                if (choose == 1) {

                    message.setChatId(String.valueOf(chatId));
                    message.setText("""
                            you choosed a balance.
                            Enter amount expense:
                            """);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    waitingForWallet = false;
                    waitingforAmount = true;
                }
                else if (choose == 2) {

                    message.setChatId(String.valueOf(chatId));
                    message.setText("""
                            you choosed a Savings.
                            Enter amount expense:
                            """);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    waitingForWallet = false;
                    waitingforAmount = true;
                }
            }
                    else if (waitingforAmount) {
                if (choose == 1) {
                    SendMessage message = new SendMessage();
                    double amount = Double.parseDouble(messageText);
                    if (amount < 0) {

                        message.setChatId(String.valueOf(chatId));
                        message.setText("""
                                Expense cannot be negative
                                """);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (financeService.getBalance() < amount) {
                        message.setChatId(String.valueOf(chatId));
                        message.setText("""
                                Insufficient balance""");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        financeService.addExpenses(choose, amount);
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Expense: -" + amount);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        waitingforAmount = false;
                    }
                } else if (choose == 2) {
                    SendMessage message = new SendMessage();
                    double amount = Double.parseDouble(messageText);
                    if (amount < 0) {

                        message.setChatId(String.valueOf(chatId));
                        message.setText("""
                                Expense cannot be negative
                                """);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else if (financeService.getSavingsBalance() < amount) {
                        message.setChatId(String.valueOf(chatId));
                        message.setText("""
                                Insufficient balance""");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        financeService.addExpenses(choose, amount);
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Expense: -" + amount);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        waitingforAmount = false;
                    }
                }
                    }

                    else if(messageText.equals("/savings")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText(
                        "Your Saving balance: " + financeService.getSavingsBalance()

                );
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                message.setText(
                        "Press 1 to add money to savings"
                );
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                waitingForSavings = true;

            }
                else if ( waitingForSavings) {

                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                choose = Integer.parseInt(messageText);
                if (choose == 1) {
                    message.setText(
                            "Enter amount: "

                    );
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    waitingForSavingsAmount = true;
                    waitingForSavings = false;
                } else {
                    message.setText(
                            "invalid input"

                    );
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
                    else if (waitingForSavingsAmount){
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                    double amount = Double.parseDouble(messageText);
                    if (amount<0){
                        message.setText("Saving cannot be negative");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (financeService.getBalance()<amount){
                        message.setText("""
                                Insufficient balance""");
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        financeService.addSavings(amount);
                        message.setText(
                                "Savings +" + amount

                        );
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                    }
                waitingForSavingsAmount = false;
                }
                else
                    return;
            }

        }
    }


