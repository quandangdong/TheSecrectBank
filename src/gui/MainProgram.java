/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import entity.UserAll;
import services.ServiceController;

/**
 *
 * @author DongQuan
 */
public class MainProgram {

    public static void main(String[] args) {

        ServiceController controller = new ServiceController(); //gọi Service

        int choice, choice1;
        Menu menu = new Menu();
        Menu subMenu = new Menu();
        UserAll ua = null;

        do {
            menu.mainMenu();
            choice = menu.getUserChoice(" Your choice is: ", "Number (1-2) only. Try again!");
            switch (choice) {

                case 1:
                    controller.createAccount();
                    controller.saveToFile();
                    break;

                case 2:
                    ua = controller.login();
                    if (ua == null) {
                        break;
                    } else {
                        do {
                            System.out.println("\n" + ua);
                            subMenu.subMenu();
                            subMenu.printSubMenu();
                            choice1 = subMenu.getUserChoice(" Your choice is: ", "Number (1-5) only. Try again!");
                            switch (choice1) {
                                case 1:
                                    controller.withdraw(ua);
                                    controller.saveToFile();
                                    break;
                                case 2:
                                    controller.deposit(ua);
                                    controller.saveToFile();
                                    break;
                                case 3:
                                    controller.tranfer(ua);
                                    controller.saveToFile();
                                    break;
                                case 4:
                                    if (ua.getBalance() > 0) {
                                        System.out.println("Your account have money, please withdraw all");
                                        break;
                                    } else {
                                        int check = controller.removeAccount(ua);
                                        if (check == 0) {
                                            System.out.println("Remove cancelled");
                                        } else if (check < 0) {
                                            System.out.println("Remove failed!");
                                        } else if (check == 1) {
                                            System.out.println("Remove successfully!");
                                            ua = null;
                                            controller.saveToFile();
                                        }
                                        break;
                                    }
                                case 5:
                                    controller.changePassword(ua);
                                    controller.saveToFile();
                                    break;
                                case 6:
                                    controller.showHistory(ua);
                                    break;
                            }
                        } while (choice1 > 0 && choice1 < 7 && ua != null);
                        break;
                    }

                default:
                    System.out.println("Thanks for using <3");
                    break;
            }
        } while (choice > 0 && choice < 3);
    }
}
