/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Fitria Khoirunnisa
 */

public class B_KFold_CrossValidation {
    //Inisialisasi Public
    public int ukuran_k;
    
    //Inisialisasi Private
    private String[] doc_list, kategori_file, nama_file;
    private ArrayList<String[]> nama_file_fold_uji_list, doc_fold_uji_list, kategori_fold_uji_list;
    private ArrayList<String[]> doc_fold_latih_list, kategori_fold_latih_list;
    
    //Constructor Class
    public B_KFold_CrossValidation(int ukuran_k) {
        this.ukuran_k = ukuran_k;
    }
   
    public void initData(String[] doc_list, String[] kategori_file, String[] nama_file){
        this.nama_file = nama_file;
        this.doc_list = doc_list;
        this.kategori_file = kategori_file;
    }
    
    public void setDataKFoldCrossValidation(){
        //Inisialisasi Kelompok
        int fold, count;
        //Inisialisasi File (Bagi)
        String[] nama_file_uji, data_uji, kategori_uji, data_latih, kategori_latih;
        //Inisialisasi File (Fold)
        String[] nama_file_fold, doc_fold, kategori_fold;
     
        //Inisialisasi List Kosong (Fold)
        ArrayList<String[]> nama_file_fold_temp_list = new ArrayList<>();
        ArrayList<String[]> doc_fold_temp_list = new ArrayList<>();
        ArrayList<String[]> kategori_fold_temp_list = new ArrayList<>();
        
        //Inisialisasi Index Dokumen
        ArrayList<Integer> indeks_doc_list = new ArrayList<>();
        
        //Kumpulan File diberikan Index
        for (int i = 0; i<doc_list.length; i++) {
            indeks_doc_list.add(i);
        }
        
        //Dokumen Diacak Sebanyak Dokumen
        Collections.shuffle(indeks_doc_list);
        
        fold = 0;
        for (int i = 1; i <= ukuran_k; i++) {
            //Penentuan Set Data, Pembagian Kumpulan File dengan K ... 300/10 = 30
            nama_file_fold = new String[doc_list.length/ukuran_k];
            doc_fold = new String[doc_list.length/ukuran_k];
            kategori_fold = new String[doc_list.length/ukuran_k];
            
            //Penentuan Index Dokumen, Index Dokumen Diambil dari Fold
            for (int j = 0; j < doc_fold.length; j++) {
                nama_file_fold[j] = nama_file[indeks_doc_list.get(j+fold)];
                doc_fold[j] = doc_list[indeks_doc_list.get(j+fold)];
                kategori_fold[j] = kategori_file[indeks_doc_list.get(j+fold)];
            }
            
            //Nilai Fold Didapatkan berdasarkan 300/10 = 30
            fold += (doc_list.length/ukuran_k);
            
            //Daftar Dokumen dalam setiap Foldnya
            nama_file_fold_temp_list.add(nama_file_fold);
            doc_fold_temp_list.add(doc_fold);
            kategori_fold_temp_list.add(kategori_fold);
        }
        
        //Didefinisikan Subject
        nama_file_fold_uji_list = new ArrayList<>();
        doc_fold_uji_list = new ArrayList<>();
        kategori_fold_uji_list = new ArrayList<>();
        doc_fold_latih_list = new ArrayList<>();
        kategori_fold_latih_list = new ArrayList<>();

        for (int i = 0; i < ukuran_k; i++) {
            //Penentuan Dokumen Uji, Dokumen Uji adalah 300/10 = 30
            data_uji = new String[doc_list.length/ukuran_k];
            kategori_uji = new String[doc_list.length/ukuran_k];
            nama_file_uji = new String[doc_list.length/ukuran_k];
            
            //Penentuan Dokumen Latih, Dokumen Latih adalah 300-(300/10) = 300-30 = 270
            kategori_latih = new String[doc_list.length-(doc_list.length/ukuran_k)];
            data_latih = new String[doc_list.length-(doc_list.length/ukuran_k)];

            //Penentuan Index Dokumen Uji, Berdasarkan Index Set [0][0] = index 30
            for (int j = 0; j < data_uji.length; j++) {
                nama_file_uji[j] = nama_file_fold_temp_list.get(i)[j];
                data_uji[j] = doc_fold_temp_list.get(i)[j];
                kategori_uji[j] = kategori_fold_temp_list.get(i)[j];
            }
            
            count = 0;
            for (int j = 0; j < doc_fold_temp_list.size(); j++) {
                //Index j=0 tidak sama dengan i=0, jadi [1][2]
                if(j != i){
                    //Penentuan Index Dokumen Latih, Berdasarkan Index Uji dengan Cara Dicek 
                    for (int l = 0; l < doc_fold_temp_list.get(j).length; l++) {
                        data_latih[count] = doc_fold_temp_list.get(j)[l];
                        kategori_latih[count] = kategori_fold_temp_list.get(j)[l];
                        count++;
                    }
                }
            }
            
            nama_file_fold_uji_list.add(nama_file_uji);             
            doc_fold_uji_list.add(data_uji);   
            kategori_fold_uji_list.add(kategori_uji);
            doc_fold_latih_list.add(data_latih);
            kategori_fold_latih_list.add(kategori_latih);
        }
    }
}
