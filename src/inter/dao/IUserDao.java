/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inter.dao;

import entity.UserAll;
import entity.UserInfo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public interface IUserDao {

    public ArrayList<UserInfo> readInfo();

    public ArrayList<UserAll> readAll();

    public int writeInfo(ArrayList<UserInfo> listInfo) throws IOException, FileNotFoundException;

    public int writeAll(ArrayList<UserAll> listAll) throws IOException, FileNotFoundException;
    
    public int writeHistory(ArrayList<String> historyList) throws IOException, FileNotFoundException;
    
    public ArrayList<String> readHistory() throws IOException, FileNotFoundException;
}
