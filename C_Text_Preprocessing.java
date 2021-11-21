/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

//import Entity.Dokumen_Kamus;

import Entity.Dokumen_Kamus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fitria
 */
public class C_Text_Preprocessing {
   
    public String Casefolding(String kalimat){
        int test;
        String hasil = "";
        //Delimiter 20 Karakter
        char [] delimiter = {'.',',','&','/','"','?',':',';','!','[',']','{','}','(',')','_','+','=','<','>',
                             '@','#','$','%','^','&','*','`','~','-','0','1','2','3','4','5','6','7','8','9'};
        
        //Kalimat-> delimiter -> CaseFolding (Kalimat)
        for (int i=0; i<kalimat.length(); i++)
        {
            test=0;
                for (int k=0; k<delimiter.length; k++)
                {
                    //Jika Karakter disuatu Dokumen Tidakada Delimiter, maka Ditambahkan
                    if (kalimat.charAt(i) == delimiter[k])
                    {
                           test++;                
                    }
                }
                //Jika Karakter disuatu Dokumen Ada, Dilakukan Pengecekkan
                if(test<1)
                {
                    hasil = hasil + kalimat.charAt(i);
                }    
            }
        //Kalimat Delimiter =  Huruf Kecil
        String cf_kalimat = hasil.toLowerCase();
//        System.out.println("Case Folding = "+cf_kalimat);
        
        return cf_kalimat;
   }
    
    public ArrayList<String> tokenizing(String casefolding){
        ArrayList<String> tk_kata = new ArrayList<>();
        String[] temp = casefolding.split(" ");
        
        //Kata Dilakukan Berulang dengan Split Kata
        for (String kata1 : temp) {
            //Kata Tidak Boleh Kosong
            if(!(kata1.isEmpty()))
            {
                 tk_kata.add(kata1);
            }
        }
//        System.out.println("Tokenisasi = "+tk_kata);
        
        return tk_kata;
   }
    
    public ArrayList<String> filtering (ArrayList<String> tokenization) {
        ArrayList<String> filtering = new ArrayList<>();
        Dokumen_Kamus dokumen = new Dokumen_Kamus();
        
        try {
            //Daftar Stopword -> Stopword
            ArrayList<String> stopword = dokumen.get_stopword_list();
            //Tokenizing dilakukan Perulangan Untuk dicek Stopword
            for (int i = 0; i < tokenization.size(); i++) {
                if (!tokenization.get(i).equals("")) {
                    if(!stopword.contains(tokenization.get(i)))
                    filtering.add(tokenization.get(i));
                }
            }
        } catch (Exception e) {
            Logger.getLogger(C_Text_Preprocessing.class.getName()).log(Level.SEVERE, null, e);
        }
//        System.out.println("Filtering = "+filtering);
        
        return filtering;
     }
     
    public ArrayList<String> stemming (ArrayList<String> filtering) throws IOException {
        ArrayList<String> stemming = new ArrayList<>();
        D_Proses_Stemming stem = new D_Proses_Stemming();
        
        //Pengambilan Proses Stemming
        for (int i = 0; i <filtering.size(); i++) {
            stemming.add(stem.KataDasar(filtering.get(i)));
        }
//        System.out.println("Stemming = "+stemming);
        
        return stemming;
     }
       
}
