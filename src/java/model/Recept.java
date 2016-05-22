/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 604772006
 */
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Chlebovics Kornél
 */
public class Recept implements Serializable {

    private String megnevezes;
    private String leiras;
    private ArrayList<Osszetevok> osszetevok;

    /**
     * Üres konstruktor
     */
    public Recept() {
        this.leiras = "";
        this.megnevezes = "";
        this.osszetevok = new ArrayList<>();
    }

    /**
     * Paraméteres konstruktor
     *
     * @param megnevezes
     * @param leiras
     * @param osszetevok
     */
    public Recept(String megnevezes, String leiras, ArrayList<Osszetevok> osszetevok) {
        this.megnevezes = megnevezes;
        this.leiras = leiras;
        this.osszetevok = osszetevok;
    }

    public Recept(String megnevezes, String leiras) {
        this.megnevezes = megnevezes;
        this.leiras = leiras;
        this.osszetevok = new ArrayList<>();
    }

    /**
     * Egy Recepthez hozzádunk egy összetevőt
     */
    public void osszetevotHozzaad(String mennyiseg_egyseg, String mennyiseg_tipus, String osszetevo_fajta) {
        this.osszetevok.add(new Osszetevok(mennyiseg_egyseg, mennyiseg_tipus, osszetevo_fajta));
    }

    public void osszetevotHozzaad(Osszetevok otevo) {
        this.osszetevok.add(otevo);
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public String getLeiras() {
        return leiras;
    }

    public ArrayList<Osszetevok> getOsszetevok() {
        return osszetevok;
    }

    public DefaultTableModel getOsszetevokTablaban() {
        String[] columnName = {"Mennyiség", "Egység", "Összetevő"};
        DefaultTableModel eredmeny = new DefaultTableModel(columnName, 0);

        for (int i = 0; i < osszetevok.size(); i++) {
            Object[] obj = {osszetevok.get(i).getMennyiseg_egyseg(), osszetevok.get(i).getMennyiseg_tipus(), osszetevok.get(i).getOsszetevo_fajta()};
            eredmeny.addRow(obj);

        }
        return eredmeny;
    }

    public void setOsszetevok(ArrayList<Osszetevok> osszetevok) {
        this.osszetevok = osszetevok;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        boolean eredmeny = true;
        final Recept other = (Recept) obj;
        if (!this.megnevezes.equals(other.megnevezes)) {
            eredmeny = false;
            //System.out.println("Megnevezés nem egyezik");
        }
        if (!this.leiras.equals(other.leiras)) {
            eredmeny = false;
            //System.out.println("Leírás nem egyezik");
        }

        if (this.osszetevok.size() != other.osszetevok.size()) {
            eredmeny = false;
            //System.out.println("Összetevők mérete nem egyezik");
        }

        for (int i = 0; i < this.osszetevok.size(); i++) {
            if (!((this.osszetevok.get(i)).equals((other.osszetevok.get(i))))) {
                eredmeny = false;
                //System.out.println("Összetevő nem egyezik: this "+this.osszetevok.get(i)+" és other: "+other.osszetevok.get(i));
            }
        }

        if (!eredmeny) {
            throw new RuntimeException("A recept szerkesztve lett. Recept törlését szerkesztés nélkül vigye véghez!");
        }
        return eredmeny;
    }

    private synchronized void writeObject(java.io.ObjectOutputStream stream) throws java.io.IOException {
        stream.defaultWriteObject();
        
        }
    

    private void readObject(java.io.ObjectInputStream stream) throws
            java.io.IOException, ClassNotFoundException {
        stream.defaultReadObject();
       
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.megnevezes);
        hash = 71 * hash + Objects.hashCode(this.leiras);
        hash = 71 * hash + Objects.hashCode(this.osszetevok);
        return hash;
    }

}
