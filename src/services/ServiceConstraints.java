/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.Scanner;

/**
 *
 * @author DongQuan
 */
public class ServiceConstraints {
    // dùng để ràng buộc

    public static Scanner sc = null;

    public static String getString(String noun) {
        String ip;
        sc = new Scanner(System.in);
        while (true) {
            System.out.print("Input" + noun);
            ip = sc.nextLine().trim();
            if (ip.isEmpty()) {
                System.out.println("Nothing is inputted, try again!");
            } else {
                return ip;
            }
        }
    }

    public static int checkAccountName(String ip) {
        int i;
        int check = 0;
        if (ip.contains("admin")) {
            System.out.println("\nAccount name not have @,#,$,*,&,admin\n");
            check = 1;
        }
        for (i = 0; i < ip.length(); i++) {
            char c = ip.charAt(i);
            if (c == '@' || c == '#' || c == '$' || c == '*' || c == '&') {
                System.out.println("\n Account name not have @,#,$,*,&,admin \n");
                check = 1;
            } else {
                check = 0;
            }

        }
        return check;
    }

    public static String getStringAccountName(String noun) {
        boolean check = true;
        String ip = "";
        String ip1;
        int i;
        sc = new Scanner(System.in);
        while (true) {
            System.out.print("Input" + noun);
            ip1 = sc.nextLine().trim();
            if (ip1.isEmpty()) {
                System.out.println("Nothing is inputted, try again!");
            }
            if (checkAccountName(ip1) == 0) {
                ip = ip1;
                return ip;
            }
        }
    }

    public static double getDouble(String noun) {
        double ip;
        sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Input" + noun);
                ip = Double.parseDouble(sc.nextLine());
                return ip;
            } catch (NumberFormatException e) {
                System.out.println("Interger only! Try again");
            }
        }
    }

    public static int checkYN(String noun) {
        sc = new Scanner(System.in);
        String input = "";
        System.out.print("Do you want to " + noun);
        input = sc.nextLine().trim();
        if (input.equals("Yes") || input.equals("yes")) {
            return 1;
        } else if (input.equals("No") || input.equals("no")) {
            return 0;
        } else {
            return -1;
        }
    }

}
