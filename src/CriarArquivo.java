/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Serac02
 */
public class CriarArquivo {

 
    
    public CriarArquivo(){
   
    }
    
    
    public void salvar(String content, String fileName){
    
    emTxt(content,fileName);
    emXml(content,fileName);
    
    
    }
    
    private void emTxt(String content, String fileName){
    try {
			File file = new File(fileName+".txt");
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
                       // j.salvou(true);
			System.out.println("Salvou "+fileName+".txt");
 
		} catch (IOException e) {
                    //j.salvou(false);
                    System.out.println("Falha para"+ fileName+".txt");
			e.printStackTrace();
		}
    }
    private void emXml(String content, String fileName){
    try {
			File file = new File(fileName+".xml");
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
                       // j.salvou(true);
			System.out.println("Salvou "+fileName+".xml");
 
		} catch (IOException e) {
                    //j.salvou(false);
                    System.out.println("Falha para"+ fileName+".xml");
			e.printStackTrace();
		}
    }
    
}
