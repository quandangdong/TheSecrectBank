/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.repo;

import entity.UserAll;
import entity.UserInfo;
import java.util.ArrayList;

/**
 *
 * @author DongQuan
 */
public interface IUserRepository {

    public int create(UserInfo info, UserAll all);

    public UserAll readDetail(String id);

    public int update(UserAll all);

    public int delete(UserAll all);

    public int login(String id, String password);

    public int checkBalance(double balance, double moneyRequest);
    
    public double withdraw(double balance, double depositMoney);
    
    public double deposit(double balance, double depositMoney);

    public int tranfer(String id, double moneyRequest, UserAll ua);
    
    public int changePassword(UserAll ua, String newPassword);

    public int saveToFile();
 
    public void writeDateTime(UserAll ua,String kind);
    
    public ArrayList<String> readDateTime();
}
