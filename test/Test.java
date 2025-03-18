/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import Modelo.RepresentacionIEEE;
/**
 *
 * @author StevenB
 */
public class Test {
    public static void main(String[] args) {
        RepresentacionIEEE rep = new RepresentacionIEEE();
        
        String resultado = rep.decToIEEE64(0.75);
        
        System.out.println(rep.IEEEToDec64(resultado));
        System.out.println(resultado);
    }
}
