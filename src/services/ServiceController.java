/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import config.Configuration;
import entity.UserAll;
import entity.UserInfo;
import impl.repo.ImplUserRepository;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author DongQuan
 */
public class ServiceController {

    private final DecimalFormat currency = new DecimalFormat("#,###,### VNĐ");
    ImplUserRepository repo = new ImplUserRepository(); // gọi repo
    public ArrayList history = new ArrayList();
    public ArrayList history1 = new ArrayList();
    
    LocalDateTime time = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
    public String formatted = time.format(format);
    
    File file = new File(Configuration.logFile);
    public double fileWeight = (file.length());

    // check valid password, paramater will be the password user inputted.
    private int validPwd(String pwd) {
        int L = pwd.length();
        if (L < 7) {
            return -1;
        }
        int upperLetter = 0, lowerLetter = 0, numberLetter = 0, specialLetter = 0;
        for (int i = 0; i < pwd.length(); i++) {
            char c = pwd.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                upperLetter++;
            } else if (c >= 'a' && c <= 'z') {
                lowerLetter++;
            } else if (c >= '0' && c <= '9') {
                numberLetter++;
            } else {
                specialLetter++;
            }
        }
        if (upperLetter > 0 && numberLetter > 0 && specialLetter > 0 && lowerLetter > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    // used to encryte password
    private String md5(String pwd) {
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(pwd.getBytes());
            BigInteger bigInteger = new BigInteger(1, digest.digest());
            result = bigInteger.toString(16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }

    public String inputAndCheckPassword(int kind) {
        String password = "";
        while (true) {
            switch (kind) {
                case 1:
                    password = ServiceConstraints.getString(" your password: ");
                    break;
                case 2:
                    password = ServiceConstraints.getString(" your confirm password: ");
                    break;
                case 3:
                    password = ServiceConstraints.getString(" your new password: ");
                    break;

                case 4:
                    password = ServiceConstraints.getString(" your old password: ");
                    break;
            }

            if (validPwd(password) > 0) {
                return md5(password);
            } else {
                System.out.println("\n  + Password must contain at least 6 characters\n"
                        + "  including uppercase letters, lower case letters\n"
                        + "  numeric characters and 1 special character\n");
            }
        }

    }

    public int checkSameId(String ip) {
        for (UserInfo obj : repo.getListUif()) {
            if (obj.getId().equals(ip)) {
                return 1;
            }
        }
        return -1;
    }

    public String getAndCheckSameId() {
        Scanner sc = null;
        int flag = 0, check = 0;
        String id = "";

        while (flag == 0) {
            String ip = "";
            sc = new Scanner(System.in);
            System.out.print("Input account's ID: ");
            ip = sc.nextLine();
            if (ip.isEmpty()) {
                System.out.println("Nothing is inputted, try again!");
            }
            check = checkSameId(ip);
//          check = checkSameIdAndUpperCase(ip);
            if (check > 0) {
                System.out.println("Id existed. Input again!");
            } else {
                id = ip;
                flag = 1;
            }
        }

        return id;
    }

//  ABOUT CREAT ACCOUNT
    public void createAccount() {
        boolean check;
        String accountName = ServiceConstraints.getStringAccountName(" your account name: ");
        String id = getAndCheckSameId();
        String password = inputAndCheckPassword(1);
        String confirmPassword;

        do {
            confirmPassword = inputAndCheckPassword(2);
            check = password.equals(confirmPassword); //check the same between password and confirm password
            if (check == true) {
                UserInfo uf = new UserInfo(id, password, accountName);
                UserAll ua = new UserAll(id, accountName, 0.0);
                repo.create(uf, ua);
                System.out.println("Your encrypted password: " + password);
                System.out.println("Create successfully!\n");
                System.out.print("Your account ");
                repo.writeDateTime(ua, " was created!");
                System.out.println(formatted +" "+ fileWeight+"B");
                show(ua);
            } else {
                System.out.println("\nYour confirm password must be same with password. Try again!\n");
                check = false;
            }
        } while (check == false);

    }

// ABOUT LOGIN
    //return userAll obj or null.
    // used to check for withdraw and deposit
    private UserAll checkLogin(String idToLogin, String passwordToLogin) {
        int i = repo.login(idToLogin, passwordToLogin);
        if (i > -1) {
            return repo.readDetail(idToLogin);
//            return repo.getListUa().get(i);
        }
        return null;
    }

    public UserAll login() {
        UserAll ua;
        String idToLogin = ServiceConstraints.getString(" your Id's account to login: ");
        String passwordToLogin = inputAndCheckPassword(1);
        ua = checkLogin(idToLogin, passwordToLogin);
        if (ua == null) {
            System.out.println("Your account not existed. Please create a new account!\n");
        } else if (ua != null) {
            repo.writeDateTime(ua, " login to account");
            System.out.println(formatted +" "+ fileWeight+"B");
        }
        return ua;
    }

    //tất cả nhận thằng ua khi đc login vào r xử lí dụa trên thằng ua đó
//ABOUT WITHDRAW    
    public void withdraw(UserAll ua) {
        int checkYN, check;
        double balance = 0.0;
        double moneyRequest = 0;
        balance = ua.getBalance();
        do {
            if (balance <= 0) {
                System.out.println("Your account did't have money to withdraw!");
                return;
            }
            moneyRequest = ServiceConstraints.getDouble(" money you want to withdraw: ");
            check = repo.checkBalance(balance, moneyRequest + 1.0);
            if (check < 0) {
                System.out.println("\n Not enough money in your account! Try again! Or input '0' VNĐ to exist!\n");
            } else if (moneyRequest == 0) {
                return;
            }
        } while (check < 0);

        do {

            checkYN = ServiceConstraints.checkYN("to withdraw(Yes/no): ");
            if (checkYN > 0) {
                balance = repo.withdraw(balance, moneyRequest);
                System.out.println("Money in your account: " + currency.format(balance));
                System.out.println(formatted +" "+ fileWeight+"B");
                ua.setBalance(balance);
                repo.update(ua);
                repo.writeDateTime(ua, " withdraw " + currency.format(ua.getBalance()));
                return;
            } else if (checkYN < 0) {
                System.out.println("(Yes/yes) or (No/no) only! Try again");
            } else {
                System.out.println("Withdraw canceled");
            }
        } while (checkYN < 0);

    }

//ABOUT DEPOSIT
    public void deposit(UserAll ua) {
        double balance = ua.getBalance();
        double depositMoney = ServiceConstraints.getDouble(" money you want to deposit: ");
        double newBalance = repo.deposit(balance, depositMoney);
        int check;
        do {
            check = ServiceConstraints.checkYN("deposit(Yes/no): ");
            if (check == 0) {
                System.out.println("Deposit canceled");
                return;
            } else if (check == 1) {
                ua.setBalance(newBalance);
                System.out.println("Your money in account: " + currency.format(ua.getBalance()));
                System.out.println(formatted +" "+ fileWeight+"B");
                repo.update(ua);
                repo.writeDateTime(ua, " deposit " + currency.format(ua.getBalance()));
//                show(ua);
            } else if (check < 0) {
                System.out.println("(Yes/yes) or (No/no) only! Try again!");
            }
        } while (check < 0);
    }

// ABOUT TRANSFER
    public void tranfer(UserAll ua) {
        int check = 0, check1 = 0, checkYN = 0;
        String recipientId = "";
        double moneyRequest = 0;

        if (repo.getListUa().size() == 1 && repo.getListUif().get(0).getId().equals(ua.getId())) {
            System.out.println("Bank is emty! You are first customer, can't transfer money!");
            return;
        } else if (ua.getBalance() == 0) {
            System.out.println("Your account didn't have money to transfer! Please deposit money");
            return;
        }

        // check ID EXISTED
        while (check1 <= 0) {
            recipientId = ServiceConstraints.getString(" recipient's ID: ");
            check1 = checkSameId(recipientId);
            if (recipientId.isEmpty()) {
                System.out.println("Nothing input. try again!");
                check1 = 0;
            } else if (recipientId.equals(ua.getId())) {
                System.out.println("You can't transfer to yourself! Try again!\n");
                check1 = 0;
            } else if (check1 < 0) {
                System.out.println("Id not existed. Try again!\n");
            }
        }

        //check MONEY IN ACCOUNT
        do {
            moneyRequest = ServiceConstraints.getDouble(" money you want to transfer: ");
            check = repo.checkBalance(ua.getBalance(), moneyRequest);
            if (check < 0) {
                System.out.println("Not enough money to tranfer! Try again!\n");
            }
        } while (check < 0);

        // check YN
        do {
            checkYN = ServiceConstraints.checkYN("to tranfer(Yes/no): ");
            if (checkYN > 0) {
                if (repo.tranfer(recipientId, moneyRequest, ua) == 1) {
                    System.out.println("Tranfer successfully!");
                    System.out.println("Money in your account: " + currency.format(ua.getBalance()));
                    System.out.println(formatted +" "+ fileWeight+"B");
                    repo.writeDateTime(ua, " tranfer " + currency.format(ua.getBalance()) + " to " + recipientId);
//                    show(ua);
                }
            } else if (checkYN < 0) {
                System.out.println("(Yes/yes) or (No/no) only! Try again!");
            } else {
                System.out.println("Tranfer canceled");
            }
        } while (checkYN < 0);
    }

    //ABOUT REMOVE ACCOUNT
    public int removeAccount(UserAll ua) {

        int flag;
        int check = 0;
        do {
            flag = ServiceConstraints.checkYN("remove account(Yes/no): ");
            if (flag == 1) {
                if (repo.delete(ua) > 0) {
                    repo.writeDateTime(ua, " removed");
                    System.out.println(formatted +" "+ fileWeight+"B");
                    check = 1;
                } else {
                    check = -1;
                }
            } else if (flag == 0) {
                check = 0;
            } else if (flag < 0) {
                System.out.println("(Yes/yes) or (No/no) only! Try again!");
            }
        } while (flag < 0);

        return check;
    }

    // ABOUT CHANGE PASSWORD
    public void changePassword(UserAll ua) {
        int check = 0;
        int i, j;
        String oldPassword = "";

        oldPassword = inputAndCheckPassword(4);
        for (i = 0; i < repo.getListUa().size(); i++) { // search UAlist to find id
            if (repo.getListUa().get(i).getId().equals(ua.getId())) {
                if (repo.getListUif().get(i).getPassword().equals(oldPassword)) { // search UIflist to find password base on above id

                    String newPassword = "";
                    do {
                        newPassword = inputAndCheckPassword(3);
                        check = repo.changePassword(ua, newPassword); //HAM CHANGEPASSWORD BEN REPO SE TRA VE -1 NEU TRUNG
                        if (check < 0) {
                            System.out.println("\n Your new password must be different from old password! Try again!\n");
                        } else if (check > 0) {
                            System.out.println("Change password successfully");
                            System.out.println(formatted +" "+ fileWeight+"B");
                            repo.writeDateTime(ua, " change new pasword!");
                            return;
                        }
                    } while (check < 0);
                } else if (!repo.getListUif().get(i).getPassword().equals(oldPassword)) {
                    System.out.println("Wrong account password! Can't change password!");
                    return;
                }
            }
        }

    }

// ABOUT SAVE TO FILE
    public void saveToFile() {
        int check = repo.saveToFile();
        if (check < 0) {
            System.out.println("Save to file failed");
        }
    }

    // show userAll detail
    private void show(UserAll ua) {
        System.out.println(ua);
    }

    public void showHistory(UserAll ua) {
        String id = ua.getId();
        double d1 = 0.0;
        history = repo.readDateTime();

        for (int i = 0; i < history.size(); i++) {

            String sentence = history.get(i).toString();
            String stringTime = sentence.split(",")[0];

            LocalDateTime time = LocalDateTime.now();
            String formatted = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss"));

            try {
                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
                d1 = ((formater.parse(formatted).getMonth()) - (formater.parse(stringTime).getMonth()));
            } catch (ParseException e) {
                System.out.println(e);
            }

            if (d1 <= 0) {
                if (sentence.contains(id)) {
                    System.out.println(sentence);
                }
            }
        }
    }

}

//    public String checkUpperCase(String ip) {
//        int i, check;
//        while(check == 0){    
//        for (i = 0; i < ip.length(); i++) {
//            char c = ip.charAt(i);
//            if (c >= 'A' && c <= 'Z') {
//                check = 1;
//            } 
//            else{
//                 sout("your id need to have an uppercase letter);
//            }
//        }
//      }
//      return ip;
//    }
//    public int checkSameIdAndUpperCase(String ip) {
//        int i;
//        for (i = 0; i < repo.getListUif().size(); i++) {
//            if (repo.getListUif().get(i).getId().equals(ip)) {
//                return -1;
//            }
//            for (int j = 0; j < ip.length(); j++) {
//                char c = ip.charAt(j);
//                if (c >= 'A' && c <= 'Z') {
//                    return 1;
//                }
//            }
//        }
//        return 0;
//    }
// check valid + encrypt password and return encrypted password to createAccount function
// String newPassword = "";
//                    do {
//                        check = 0;
//                        newPassword = ServiceConstraints.getString(" your new password: ");
//                        for (int i = 0; i < newPassword.length(); i++) {
//                            char c = newPassword.charAt(i);
//                            if (c == '#') {
//                                System.out.println("New password not contain 'admin' or '#'");
//                                check = -1;
//                            }
//                        }
//                        if (newPassword.contains("admin")) {
//                            System.out.println("New password not contain 'admin' or '#'");
//                            check = -1;
//                        }
//                    } while (check < 0);
//                    password = newPassword;
//                    break;
