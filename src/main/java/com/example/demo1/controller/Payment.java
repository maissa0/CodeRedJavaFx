package com.example.demo1.controller;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public class Payment {
    public static void main(String[] args) {
// Set your secret key here
        Stripe.apiKey = "sk_test_51PCFbFJ4hcfRdihgnZEf8CV6hvWt1ch2Sw6uCmK8xO5WxXH93knfgyO3w8mdoPND1HvynGoELJYnJVE7W4RzYWaV004OvqA1nT";

        try {
// Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
// Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
