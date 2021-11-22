/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author DongQuan
 */
public class Menu {

    private ArrayList<String> listMenu = new ArrayList<>();

    public void addOpption(String option) {
        listMenu.add(option);
    }
    
     public void printMenu() {
        System.out.println("\n===============================");
        System.out.println("----Welcome to Quan's bank----");
        for (int i = 0; i < listMenu.size(); i++) {
            System.out.println((i + 1) + "." + listMenu.get(i));
        }
    }

    public void printSubMenu() {

        for (int i = 0; i < listMenu.size(); i++) {
            System.out.println((i + 1) + "." + listMenu.get(i));
        }
    }

    public void mainMenu() {
        Menu menu = new Menu();
        menu.addOpption(" Create new account");
        menu.addOpption(" Login");
        menu.printMenu();
        System.out.println("Other: Quit");
    }

    public void subMenu() {
        Menu subuMenu = new Menu();
        System.out.println();
        subuMenu.addOpption(" Withdrawl Money");
        subuMenu.addOpption(" Deposit Money");
        subuMenu.addOpption(" Transfer Money");
        subuMenu.addOpption(" Remove account");
        subuMenu.addOpption(" Change password");
        subuMenu.addOpption(" Show history");
        subuMenu.printSubMenu();
        System.out.println("Other: log out");
    }

    public int getUserChoice(String inputMess, String errorMess) {
        int choice = 0;
        while (true) {
            System.out.print(inputMess);
            try {
                Scanner sc = new Scanner(System.in);
                choice = Integer.parseInt(sc.nextLine());
                return choice;

            } catch (NumberFormatException e) {
                System.out.println(errorMess);
            }

        }
    }
}
