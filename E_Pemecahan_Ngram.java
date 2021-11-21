/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;

/**
 *
 * @author Fitria
 */

public class E_Pemecahan_Ngram {
    public ArrayList<String> Hitung_Bigram(ArrayList<String> stemming){
        ArrayList<String> daftar_ngram = new ArrayList<>();
        String[] hasil;
        String list;
        int kata;
        
        //N-Gram dengan pembagian 2 Karakter, aku -> ak,ku
        for(int i=0; i<stemming.size(); i++){
            list = stemming.get(i);
            kata = list.length();
            
            hasil = new String[kata-1];
            //hitung pemecahan ngram
            if(list.length() == 2){
                hasil[0] = list;
            }
            else if(list.length() > 2){
                for(int j=0; j<hasil.length; j++){
                    hasil[j] = list.substring(j, j+2);
                }
            }

            //ditambahkan k daftar list
            for(int k=0; k<hasil.length; k++){
                daftar_ngram.add(hasil[k]);
            }
        }
        
//        //N-Gram dengan Pembagian 1 Karakter Awal-Akhir dan 2 Karakter, aku -> _a,ak,ku,u_
//        String list_baru;
//        for(int i=0; i<stemming.size(); i++){
//            list = stemming.get(i);
//            list_baru = "_"+list+"_";
//            kata = list_baru.length();
//            
//            hasil = new String[kata-1];
//            //hitug pemecahan ngram
//            if(list_baru.length() > 2){
//                for(int j=0; j<hasil.length; j++){
//                    hasil[j] = list_baru.substring(j, j+2);
//                }
//            }
//            
//            //ditambahkan k daftar list
//            for(int k=0; k<hasil.length; k++){
//                daftar_ngram.add(hasil[k]);
//            }
//        }
        
//        //Cetak N-Gram
//        System.out.println("N-Gram : "+daftar_ngram);
        
        return daftar_ngram;
    }
}
