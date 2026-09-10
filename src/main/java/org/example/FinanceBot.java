package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.List;


public class FinanceBot extends TelegramLongPollingBot {

    FinanceService financeService = new FinanceService();
    boolean waitingForWallet=false;
    boolean waitingforAmount=false;
    String choose;
    boolean waitingForSavings=false;
    boolean waitingForSavingsAmount=false;
    boolean isWaitingForIncome = false;
    int chose;

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
if (update.hasCallbackQuery()){
    String callbeckData = update.getCallbackQuery().getData();
    if (callbeckData.equals("expense_balance")) {
        choose = "\uD83D\uDCB6 Balance";
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
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
    if (callbeckData.equals("expense_saving")){
        choose="\uD83D\uDCB3 Savings";
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
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
    if (callbeckData.equals("add_saving")) {
        choose = "Addsavings";
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
        message.setText("""
                           Enter amount:
                           """);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        waitingForSavingsAmount = true;
        waitingForSavings = false;
    }


}
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText);

            if (messageText.equals("/start")) {

                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                message.setText("Welcome to Finance Tracker!");


                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
                KeyboardRow row1 = new KeyboardRow();
                row1.add(new KeyboardButton("\uD83D\uDCB0 Balance"));
                row1.add(new KeyboardButton("\uD83D\uDCB5 Income"));
                KeyboardRow row2 = new KeyboardRow();
                row2.add(new KeyboardButton("\uD83D\uDCB8 Expense"));
                row2.add(new KeyboardButton("\uD83C\uDFE6 Savings"));
                keyboard.setKeyboard(List.of(row1,row2));
                message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (messageText.equals("/balance")|| messageText.equals("\uD83D\uDCB0 Balance")) {
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
            } else if (messageText.startsWith("/income")|| messageText.equals("\uD83D\uDCB5 Income")) {
                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                String[] parts = messageText.split(" ");

                message.setText("""
                            Enter income amount
                            """);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                isWaitingForIncome =true;
            }
            else if(isWaitingForIncome){
                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                double amount = Double.parseDouble(messageText);
                if (amount < 0) {

                    message.setChatId(String.valueOf(chatId));
                    message.setText("""
                                Income cannot be negative
                                """);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }  else {
                    financeService.addIncome( amount);
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Income: +" + amount);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    isWaitingForIncome = false;

            }
        }
            else if (messageText.equals("/expense")||messageText.equals("\uD83D\uDCB8 Expense")) {

                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("""
                                Choose a wallet:
                                """);
                InlineKeyboardButton balanceButton =new InlineKeyboardButton();
                balanceButton.setText("\uD83D\uDCB6 Balance");
                balanceButton.setCallbackData("expense_balance");
                InlineKeyboardButton savingsButton =new InlineKeyboardButton();
                savingsButton.setText("\uD83D\uDCB3 Savings");
                savingsButton.setCallbackData("expense_saving");
               InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                        keyboard.setKeyboard(List.of(
                                List.of(balanceButton),
                                List.of(savingsButton)));
                        message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                waitingForWallet =true;
                }
//            else if (waitingForWallet) {
//                SendMessage message = new SendMessage();
//                 choose = messageText;
//                if (choose.equals("\uD83D\uDCB6 Balance")) {
//
//                    message.setChatId(String.valueOf(chatId));
//                    message.setText("""
//                            you choosed a balance.
//                            Enter amount expense:
//                            """);
//                    try {
//                        execute(message);
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                    waitingForWallet = false;
//                    waitingforAmount = true;
//                }
//                else if (choose.equals("\uD83D\uDCB3 Savings")) {
//
//                    message.setChatId(String.valueOf(chatId));
//                    message.setText("""
//                            you choosed a Savings.
//                            Enter amount expense:
//                            """);
//                    try {
//                        execute(message);
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                    waitingForWallet = false;
//                    waitingforAmount = true;
//                }
//            }
                    else if (waitingforAmount) {
                if (choose.equals("\uD83D\uDCB6 Balance")) {
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
                } else if (choose.equals("\uD83D\uDCB3 Savings")) {
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

                    else if(messageText.equals("/savings")|| messageText.equals("\uD83C\uDFE6 Savings")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText(
                        "Your Saving balance: " + financeService.getSavingsBalance()

                );
                InlineKeyboardButton addSaving =new InlineKeyboardButton();
                addSaving.setText("Add Savings");
                addSaving.setCallbackData("add_saving");
                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                keyboard.setKeyboard(List.of(
                        List.of(addSaving)));
                message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


                waitingForSavings = true;

            }
//                else if ( waitingForSavings) {
//
//                SendMessage message = new SendMessage();
//                message.setChatId(String.valueOf(chatId));
//                chose = Integer.parseInt(messageText);
//                if (chose == 1) {
//                    message.setText(
//                            "Enter amount: "
//
//                    );
//                    try {
//                        execute(message);
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                    waitingForSavingsAmount = true;
//                    waitingForSavings = false;
//                } else {
//                    message.setText(
//                            "invalid input"
//
//                    );
//                    try {
//                        execute(message);
//                    } catch (TelegramApiException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
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


