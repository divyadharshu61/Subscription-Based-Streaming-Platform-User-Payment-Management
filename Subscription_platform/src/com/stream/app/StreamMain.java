package com.stream.app;
import java.util.Date;
import java.util.Scanner;
import com.stream.service.SubscriptionService;
public class StreamMain {
	private static SubscriptionService subscriptionService;
    public static void main(String[] args) {

        subscriptionService = new SubscriptionService();
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Streaming Platform Console ---");

        try {
            boolean r = subscriptionService.activateSubscription(
                    "USR1001",
                    "MONTHLY_STD",
                    new Date(),
                    new Date(System.currentTimeMillis() + 30L * 24 * 3600 * 1000),
                    "CARD"
            );
            System.out.println(r ? "ACTIVATED" : "FAILED");
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            boolean r = subscriptionService.activateSubscription(
                    "USR1002",
                    "MONTHLY_STD",
                    new Date(),
                    new Date(System.currentTimeMillis() + 30L * 24 * 3600 * 1000),
                    "UPI"
            );
            System.out.println(r ? "ACTIVATED" : "FAILED");
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            boolean r = subscriptionService.processPayment(
                    920005,
                    199.00,
                    "CARD"
            );
            System.out.println(r ? "PAID" : "FAILED");
        } catch (Exception e) {
            System.out.println(e);
        }

        sc.close();
    }
}

	
	
	 