/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.util.ArrayList;

/**
 *
 * @author Fitria Khoirunnisa
 */

public class F_Term_Frequency {
    ArrayList<String> term_doc, term_all_doc, n_gram_term_doc, n_gram_term_all_doc;    
    ArrayList<ArrayList> term_kategori_doc, n_gram_term_kategori_doc;
    
    double[][] tf_kategori;
    double[] tf_query;
    
    public F_Term_Frequency() {
        
    }
    
/*------------------------------------INIT TERM DOC------------------------------------*/    
    public void initTermDoc(ArrayList<String> term_doc){
        this.term_doc = term_doc;
    }
    
    public void initTermAllDoc(ArrayList<String> term_all_doc){
        this.term_all_doc = term_all_doc;
    }
    
    public void initTermKategori(ArrayList<ArrayList> term_kategori_doc){
        this.term_kategori_doc = term_kategori_doc;
    }
    
/*------------------------------------INIT NGRAM DOC------------------------------------*/
    public void initNGaramTermDoc(ArrayList<String> n_gram_term_doc){
        this.n_gram_term_doc = n_gram_term_doc;
    }
    
    public void initNGaramTermAllDoc(ArrayList<String> n_gram_term_all_doc){
        this.n_gram_term_all_doc = n_gram_term_all_doc;
    }
    
    public void initNGaramTermKategori(ArrayList<ArrayList> n_gram_term_kategori_doc){
        this.n_gram_term_kategori_doc = n_gram_term_kategori_doc;
    }
        
/*------------------------------------MATRIKS TABEL TERM FREQUENCY------------------------------------*/   
    public void tfTermKategori(){
        int frek;
        ArrayList<String> term_list;
        
        tf_kategori = new double[term_all_doc.size()][5];

        for(int i=0; i<tf_kategori.length; i++){
            for(int j=0; j<tf_kategori[i].length; j++){
                term_list = term_kategori_doc.get(j);

                frek = 0;

                for(int k=0; k<term_list.size(); k++){
                    if(term_all_doc.get(i).equals(term_list.get(k))){
                        frek++;
                    }
                }
                
                tf_kategori[i][j] = frek;
            }
        }
    }
    
    public void tfTermQuery(){
        int frek;
        
        tf_query = new double[term_all_doc.size()];

        for(int i=0; i<term_all_doc.size(); i++){
            frek = 0;
                    
            for(int k=0; k<term_doc.size(); k++){
                if(term_all_doc.get(i).equals(term_doc.get(k))){
                    frek++;
                }
            }
            
            tf_query[i] = frek;
        }
    }  
    
/*------------------------------------MATRIKS TABEL NGRAM FREQUENCY------------------------------------*/
    public void tfNgramKategori(){
        ArrayList<String> n_gram_list;
        int frek;
        
        tf_kategori = new double[n_gram_term_all_doc.size()][5];
        
        for(int i=0; i<tf_kategori.length; i++){
            for(int j=0; j<tf_kategori[i].length; j++){
                n_gram_list = n_gram_term_kategori_doc.get(j);

                frek = 0;
                
                for(int k=0; k<n_gram_list.size(); k++){
                    if(n_gram_term_all_doc.get(i).equals(n_gram_list.get(k))){
                        frek++;
                    }
                }

                tf_kategori[i][j] = frek;
            }
        }
    }
    
    public void tfNgramQuery(){
        int frek;
        
        tf_query = new double[n_gram_term_all_doc.size()];
        
        for(int i=0; i<n_gram_term_all_doc.size(); i++){
            frek = 0;
                    
            for(int k=0; k<n_gram_term_doc.size(); k++){
                if(n_gram_term_all_doc.get(i).equals(n_gram_term_doc.get(k))){
                    frek++;
                }
            }

            tf_query[i] = frek;
        }
    }
    
    public double[][] getTFKategori(){
        return tf_kategori;
    }
    
    public double[] getTFQuery(){
        return tf_query;
    }
 
}
