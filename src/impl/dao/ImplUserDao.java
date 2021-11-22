/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl.dao;

import config.Configuration;
import entity.UserAll;
import entity.UserInfo;
import inter.dao.IUserDao;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class ImplUserDao implements IUserDao {

    private final String fileUser = Configuration.userFile;
    private final String fileBank = Configuration.bankFile;
    private final String fileLog = Configuration.logFile;

    @Override
    public ArrayList<UserInfo> readInfo() {
        ArrayList<UserInfo> listInfo = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fileUser);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                UserInfo info;
                while (fis.available() > 0) {
                    info = (UserInfo) ois.readObject();
                    listInfo.add(info);
                }
            } catch (Exception e) {
            } finally {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }

        } catch (IOException e) {

        }
        return listInfo;
    }

    @Override
    public ArrayList<UserAll> readAll() {
        ArrayList<UserAll> listAll = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fileBank);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                UserAll all;
                while (fis.available() > 0) {
                    all = (UserAll) ois.readObject();
                    listAll.add(all);
                }
            } catch (Exception e) {
            } finally {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }

        } catch (IOException e) {

        }
        return listAll;
    }

    @Override
    public int writeInfo(ArrayList<UserInfo> listInfo) throws IOException, FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(fileUser);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            for (UserInfo info : listInfo) {
                oos.writeObject(info);
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return 1;
    }

    @Override
    public int writeAll(ArrayList<UserAll> listAll) throws IOException, FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(fileBank);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            for (UserAll all : listAll) {
                oos.writeObject(all);
            }
        } catch (IOException e) {
            System.out.println(e);
            return -1;
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return 1;

    }

    @Override
    public int writeHistory(ArrayList<String> historyList) throws IOException, FileNotFoundException {
        FileOutputStream fos = new FileOutputStream(fileLog);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        try {
            for (String list : historyList) {
                oos.writeObject(list);
            }
        } catch (IOException e) {
            System.out.println(e);
            return -1;
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return 1;
    }

    @Override
    public ArrayList<String> readHistory() throws FileNotFoundException, IOException {
        ArrayList<String> list = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fileLog);
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                String sentence;
                while (fis.available() > 0) {
                    sentence = (String) ois.readObject();
//                    if (sentence.contains(ua.getId())){ 
                        list.add(sentence);
//                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            }

        } catch (IOException e) {

        }
        return list;
    }

}
