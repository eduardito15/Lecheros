/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dominio;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Edu
 */
@Entity
public class Banco extends ObjetoPersistente {
    
    @Column
    private String name;
    
    @Column
    private String branchOffice;
    
    @Column
    private String address;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the branchOffice
     */
    public String getBranchOffice() {
        return branchOffice;
    }

    /**
     * @param branchOffice the branchOffice to set
     */
    public void setBranchOffice(String branchOffice) {
        this.branchOffice = branchOffice;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getBranchOffice() + " " + this.getAddress(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
