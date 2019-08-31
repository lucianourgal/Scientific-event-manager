/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sepin;

import ClassesAuxiliares.BancoDeDados;
import ClassesAuxiliares.Comunicador;
import ClassesAuxiliares.GeradorPDF;
import janelas.Inicial;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luciano Urgal
 */
public class SEPIN {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
           
        try {
            BancoDeDados.comando().criarConexao();
            Inicial i = new Inicial();   
        } catch (SQLException ex) {
            Logger.getLogger(SEPIN.class.getName()).log(Level.SEVERE, null, ex);
            Inicial i = new Inicial();   
            i.conexaoFechada();          
        }
        
    }
    
}
