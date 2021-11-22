/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author DongQuan
 */
public class UserAll implements Serializable{
    private String id;
    private String name;
    private double balance = 0.0;
    private final DecimalFormat currency = new DecimalFormat("#,###,### VNĐ");
    
    public UserAll() {
    }

    public UserAll(String id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Id: " + id + "  name: " + name + "  balance: " + currency.format(balance);
    }
}
