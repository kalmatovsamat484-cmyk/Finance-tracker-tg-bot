package org.example;

import java.util.ArrayList;

public class FinanceService {

    ArrayList<Income> incomes= new ArrayList<>();
    ArrayList<Expense> expenses= new ArrayList<>();
    ArrayList<Savings> savings= new ArrayList<>();
    ArrayList<Expense> savingsExpenses = new ArrayList<>();

    public void addIncome (double amount){

        if (amount< 0){
            System.out.println("Income cannot be negative");
        }
        else
            incomes.add(new Income(amount));
    }
    public void addExpenses (String wallet, double amount){

        if (wallet.equals("\uD83D\uDCB6 Balance")){
            if (amount < 0){
                System.out.println("Expense cannot be negative");
            }
            else if(amount == 0){
                return;
            }
            else if(getBalance() < amount){
                System.out.println("Insufficient balance");
            }
            else
                expenses.add(new Expense(amount));
        }
        else if (wallet.equals("\uD83D\uDCB3 Savings")) {

                if (amount < 0) {
                    System.out.println("Expense cannot be negative");
                } else if (getSavingsBalance() < amount) {
                    System.out.println("Insufficient balance");
                } else
                    savingsExpenses.add(new Expense(amount));


        }

        }

    public double getTotalIncome(){
       double sumIncomes=0;
for (Income i: incomes){
sumIncomes += i.getIncome();
}
return sumIncomes;
    }
    public double getTotalExpenses(){
        double sumExpenses=0;
        for (Expense i: expenses){
            sumExpenses += i.getExpense();
        }
        return sumExpenses;
    }
    public double getBalance(){
        Budget budget = new Budget(getTotalIncome(), getTotalExpenses(), getTotalSavings());
        return budget.getBalance();
    }
    public void addSavings(double amount){

        if (amount < 0){
            System.out.println("Savings cannot be negative");
        }
        else if(getBalance() < amount){
            System.out.println("Insufficient balance");
        }
        else
            savings.add(new Savings(amount));
    }
    public double getTotalSavings(){
        double sumSavings = 0;
        for (Savings i: savings){
            sumSavings+= i.getSavings();
        }
        return sumSavings;
    }
    public double getTotalSavingsExpenses(){
        double sum = 0;
        for (Expense i: savingsExpenses){
            sum+= i.getExpense();
        }
        return sum;
    }

    public double getSavingsBalance(){
       return getTotalSavings() - getTotalSavingsExpenses();
    }

}

