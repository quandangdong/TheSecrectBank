/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl.repo;

import entity.UserAll;
import entity.UserInfo;
import impl.dao.ImplUserDao;
import inter.repo.IUserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author DongQuan
 */
public class ImplUserRepository implements IUserRepository {

    ImplUserDao dao = new ImplUserDao(); // gọi dao

    private ArrayList<UserInfo> listUif = new ArrayList<>();// only contain id and password to confirm.
    private ArrayList<UserAll> listUa = new ArrayList<>(); // contact with and have a affect on amount of money of an account
    private ArrayList<String> historyList = new ArrayList<>();

    // constructor
    public ImplUserRepository() {
        listUif = dao.readInfo();
        listUa = dao.readAll();
        try {
            historyList = dao.readHistory();
        } catch (IOException ex) {
            System.out.println("ERROR HISTORY!");
        }
    }

    // getter && setter
    public ArrayList<UserInfo> getListUif() {
        return listUif;
    }

    public void setListUif(ArrayList<UserInfo> listUif) {
        this.listUif = listUif;
    }

    public ArrayList<UserAll> getListUa() {
        return listUa;
    }

    public void setListUa(ArrayList<UserAll> listUa) {
        this.listUa = listUa;
    }

    @Override
    public int create(UserInfo info, UserAll all) {
        try {
            listUif.add(info);
        } catch (Exception e) {
            return 0;
        }
        try {
            listUa.add(all);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    //read will return toString of userAll
    @Override
    public UserAll readDetail(String id) {
        for (UserAll obj : listUa) {
            if (obj.getId().equals(id)) {
                return obj;
            }
        }
        return null;
    }

    //update amount of money, service will take an balance obj of userAll + amount of money and then set to the list 
    @Override
    public int update(UserAll usA) {
        int i;
        try {
            for (i = 0; i < listUa.size(); i++) {
                if (listUa.get(i).getId().equals(usA.getId())) {
                    listUa.set(i, usA);
                }
                return 1;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public int delete(UserAll ua) {
        int i, j;
        try {
            for (i = 0; i < listUif.size(); i++) {
                if (listUif.get(i).getId().equals(ua.getId()) && listUif.get(i).getName().equals(ua.getName())) {
                    listUif.remove(i);
                }
            }

            for (j = 0; j < listUa.size(); j++) {
                if (listUa.get(j).getId().equals(ua.getId()) && listUa.get(j).getName().equals(ua.getName())) {
                    listUa.remove(j);
                    return 1;
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    @Override
    public int login(String id, String password) {
        int i;
        for (i = 0; i < listUif.size(); i++) {
            if (listUif.get(i).getId().equals(id) && listUif.get(i).getPassword().equals(password)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int checkBalance(double balance, double moneyRequest) {
        int check;
        if (balance < moneyRequest) {
            check = -1;
        } else if (balance == moneyRequest) {
            check = 1;
        } else {
            check = 1;
        }
        return check;
    }

    @Override
    public double withdraw(double balance, double withdrawMoney) {
        balance -= withdrawMoney;
        return balance;
    }

    @Override
    public double deposit(double balance, double depositMoney) {
        balance = balance + depositMoney;
        return balance;
    }

    @Override
    public int tranfer(String id, double moneyRequest, UserAll ua) {
        int check = 0;
        int i;
        for (i = 0; i < listUa.size(); i++) {
            if (listUa.get(i).getId().equals(id)) {
                double balance = listUa.get(i).getBalance();
                double recipientBalance = balance + moneyRequest;
                listUa.get(i).setBalance(recipientBalance);

                double senderBalance = ua.getBalance() - moneyRequest;
                ua.setBalance(senderBalance);
                update(ua);
                check = 1;
            }
        }
        return check;
    }

    @Override
    public int changePassword(UserAll ua, String newPassword) {
        int i = 0, check = 0;
        for (i = 0; i < listUif.size(); i++) {
            if (ua.getId().equals(listUif.get(i).getId())) {
                if (listUif.get(i).getPassword().equals(newPassword)) { // KIEM TRA MAT KHAU CO TRUNG KO
                    check = -1;
                } else {
                    listUif.get(i).setPassword(newPassword);
                    check = 1;
                }
            }
        }
        return check;
    }

    @Override
    public int saveToFile() {
        int check;
        try {
            check = dao.writeInfo(listUif);
            if (check < 0) {
                System.out.println("wrong");
            }
        } catch (Exception e) {
            return -1;
        }
        int check1;
        try {
            check1 = dao.writeAll(listUa);
            if (check1 < 0) {
                System.out.println("wrong");
            }
        } catch (Exception e) {
            return -1;
        }
        return 1;
    }

    @Override
    public void writeDateTime(UserAll ua, String kind) {
        String sentence;
        String formatted;
        
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm:ss");
        formatted = time.format(format);
        
       
        sentence = formatted + "," + ua.getId()+"->" + kind;
        
        historyList.add(sentence);
        try {
            dao.writeHistory(historyList);
        } catch (IOException e) {
            System.out.println("Save history fail!!");
        }
    }

    @Override
    public ArrayList<String> readDateTime() {
        return historyList;
    }

}
