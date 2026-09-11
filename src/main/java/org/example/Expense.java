package org.example;

 public class Expense {
    private double expense;
    private String wallet;
Expense(String wallet, double expense){
    this.expense =expense;
    this.wallet = wallet;
}
     public double getExpense() {
         return expense;
     }
     public String getWallet(){return wallet;}
}


