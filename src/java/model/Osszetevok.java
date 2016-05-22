/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author 604772006
 */
public class Osszetevok implements Serializable{
    String mennyiseg_egyseg;
   String mennyiseg_tipus;
   String osszetevo_fajta;

    public String getMennyiseg_egyseg() {
        return mennyiseg_egyseg;
    }

    public String getMennyiseg_tipus() {
        return mennyiseg_tipus;
    }

    public String getOsszetevo_fajta() {
        return osszetevo_fajta;
    }

    public void setMennyiseg_egyseg(String mennyiseg_egyseg) {
        this.mennyiseg_egyseg = mennyiseg_egyseg;
    }

    public void setMennyiseg_tipus(String mennyiseg_tipus) {
        this.mennyiseg_tipus = mennyiseg_tipus;
    }

    public void setOsszetevo_fajta(String osszetevo_fajta) {
        this.osszetevo_fajta = osszetevo_fajta;
    }

    public Osszetevok(String mennyiseg_egyseg, String mennyiseg_tipus, String osszetevo_fajta) {
        this.mennyiseg_egyseg = mennyiseg_egyseg;
        this.mennyiseg_tipus = mennyiseg_tipus;
        this.osszetevo_fajta = osszetevo_fajta;
    }

    public Osszetevok() {
        this.mennyiseg_egyseg = "";
        this.mennyiseg_tipus = "";
        this.osszetevo_fajta = "";
    }

    @Override
    public String toString() {
        return "Osszetevok{" + "mennyiseg_egyseg=" + mennyiseg_egyseg + ", mennyiseg_tipus=" + mennyiseg_tipus + ", osszetevo_fajta=" + osszetevo_fajta + '}';
    }

    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Osszetevok other = (Osszetevok) obj;
        if (this.mennyiseg_egyseg.equals(other.mennyiseg_egyseg) && this.mennyiseg_tipus.equals(other.mennyiseg_tipus) && this.osszetevo_fajta.equals(other.osszetevo_fajta)) 
        {
            return true;
        }
        return false;
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
        hash = 59 * hash + Objects.hashCode(this.mennyiseg_egyseg);
        hash = 59 * hash + Objects.hashCode(this.mennyiseg_tipus);
        hash = 59 * hash + Objects.hashCode(this.osszetevo_fajta);
        return hash;
    }
    
}
