package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class FinanceBot extends TelegramLongPollingBot {

        FinanceService financeService = new FinanceService();

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

            }
            else if (messageText.equals("/balance")) {

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
            }
            else if (messageText.startsWith("/income")){
                String[] parts= messageText.split(" ");
                if (parts.length==2){

                    try{
                        SendMessage message=new SendMessage();
                        double amount = Double.parseDouble(parts[1]);
                        financeService.addIncome(amount);
                        message.setChatId(String.valueOf(chatId));
                        if (amount<=0){
                            message.setText("Income must be greater than zero");
                            execute(message);
                        }
                        else {
                            financeService.addIncome(amount);
                            message.setText("Income: +" + amount);
                            execute(message);
                        }
                    }catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    catch (NumberFormatException e){
                        SendMessage message = new SendMessage();
                        message.setChatId(String.valueOf(chatId));
                        message.setText("Amount must be a number");

                        try {
                            execute(message);
                        } catch (TelegramApiException ex) {
                            ex.printStackTrace();
                        }
                    }
                    }
                }
                else {
                   try {
                       SendMessage message = new SendMessage();
                       message.setChatId(String.valueOf(chatId));
                       message.setText("Please enter an amount. Example: /income 5000");
                       execute(message);
                   }catch (TelegramApiException e) {
                       e.printStackTrace();
                   }
                }

            }
        }
    }
