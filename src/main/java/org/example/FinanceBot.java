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
    boolean waitingforAmount=false;
    String choose;
    boolean waitingForSavings=false;
    boolean waitingForSavingsAmount=false;
    boolean isWaitingForIncome = false;
    boolean waitingforHistory = false;
    int chose;
    double sum=0;

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
        if (update.hasCallbackQuery()) {

            String callbeckData = update.getCallbackQuery().getData();
            long chatid = update.getCallbackQuery().getMessage().getChatId();
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

                waitingforAmount = true;
            } else if (callbeckData.equals("expense_saving")) {
                choose = "\uD83D\uDCB3 Savings";
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

                waitingforAmount = true;
            } else if (callbeckData.equals("add_saving")) {
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
            } else if (callbeckData.equals("expense_history")) {
                choose = "\u23F3\uFE0F expense_history";

                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                StringBuilder text = new StringBuilder("Expense history:\n"+"sum:  "+financeService.getHistoryTotalExpenses()+"\n\n");
                for (Expense expense: financeService.expenses)
                    text.append(expense.getWallet())
                            .append(": -")
                            .append(expense.getExpense())
                            .append("\n");
                message.setText(text.toString());

                for (Expense expense: financeService.savingsExpenses)
                    text.append(expense.getWallet())
                            .append(": -")
                            .append(expense.getExpense())
                            .append("\n");
                message.setText(text.toString());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            } else if (callbeckData.equals("income_history")) {
                choose = "⏳ income_history";
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                StringBuilder text = new StringBuilder("Incomes history:\n"+"sum: "+financeService.getIncomeHistory()+"\n\n");
                for (Income income: financeService.incomes)
                    text.append("+")
                            .append(income.getIncome())
                            .append("\n");
                message.setText(text.toString());
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }


            }
            else if (callbeckData.equals("income")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
                message.setText("""
                        Enter income amount: 
                        """);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                isWaitingForIncome=true;
            }
            return;

        }
        long chatId = 0;
        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            chatId = update.getMessage().getChatId();

            System.out.println(messageText);

            if (messageText.equals("/start")) {

                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                message.setText("Welcome to Finance Tracker!");


                ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
                KeyboardRow row1 = new KeyboardRow();
                row1.add(new KeyboardButton("\uD83D\uDCB0 Balance"));
                row1.add(new KeyboardButton("\uD83D\uDCB8 Expense"));
                KeyboardRow row2 = new KeyboardRow();
                row2.add(new KeyboardButton("\uD83C\uDFE6 Savings"));
                row2.add(new KeyboardButton("\uD83D\uDCC3 history"));

                keyboard.setKeyboard(List.of(row1, row2));
                message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else if (messageText.equals("/balance") || messageText.equals("\uD83D\uDCB0 Balance")) {
                SendMessage message = new SendMessage();

                message.setChatId(String.valueOf(chatId));
                message.setText(
                        "Your balance: " + financeService.getBalance()
                );

                InlineKeyboardButton incomeButton = new InlineKeyboardButton();
                incomeButton.setText("\uD83D\uDCB5 Income");
                incomeButton.setCallbackData("income");
                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                keyboard.setKeyboard(List.of(
                        List.of(incomeButton)));
                message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (isWaitingForIncome) {
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
                } else {
                    financeService.addIncome(amount);
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Income: +" + amount);
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    isWaitingForIncome = false;

                }
            } else if (messageText.equals("/expense") || messageText.equals("\uD83D\uDCB8 Expense")) {

                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("""
                        Choose a wallet:
                        """);
                InlineKeyboardButton balanceButton = new InlineKeyboardButton();
                balanceButton.setText("\uD83D\uDCB6 Balance");
                balanceButton.setCallbackData("expense_balance");
                InlineKeyboardButton savingsButton = new InlineKeyboardButton();
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

            }


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
            } else if (messageText.equals("/savings") || messageText.equals("\uD83C\uDFE6 Savings")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText(
                        "Your Saving balance: " + financeService.getSavingsBalance()

                );
                InlineKeyboardButton addSaving = new InlineKeyboardButton();
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

            else if (waitingForSavingsAmount) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                double amount = Double.parseDouble(messageText);
                if (amount < 0) {
                    message.setText("Saving cannot be negative");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (financeService.getBalance() < amount) {
                    message.setText("""
                            Insufficient balance""");
                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
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
            } else if (messageText.equals("/history") || messageText.equals("\uD83D\uDCC3 history")) {
                SendMessage message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("""
                        Choose a history income or expense:
                        """);
                InlineKeyboardButton expenseButton = new InlineKeyboardButton();
                expenseButton.setText("\u23F3\uFE0F expense history");
                expenseButton.setCallbackData("expense_history");
                InlineKeyboardButton incomeButton = new InlineKeyboardButton();
                incomeButton.setText("⏳ income_history");
                incomeButton.setCallbackData("income_history");
                InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
                keyboard.setKeyboard(List.of(
                        List.of(incomeButton),
                        List.of(expenseButton)));
                message.setReplyMarkup(keyboard);
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                waitingforHistory = true;
            }


        } else
            return;

    }

    }


