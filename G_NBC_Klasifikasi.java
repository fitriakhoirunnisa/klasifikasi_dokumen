/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Fitria Khoirunnisa
 */

public class G_NBC_Klasifikasi { 
    double[][] tf_kategori;
    double[] tf_query;
    
    public G_NBC_Klasifikasi(){
        
    }
    
    public void initTfKategori(double[][] tf_kategori){
        this.tf_kategori = tf_kategori;
    }
    
    public void initTfQuery(double[] tf_query){
        this.tf_query = tf_query;
    }

/*------------------------------------HITUNG JUMLAH TERM FREQUENCY PER KATEGORI------------------------------------*/    
    public double[] hitung_jum_kategori(){ 
        double[] jum_kategori;
        
        jum_kategori = new double[tf_kategori[0].length]; 
        
        for(int i=0; i<tf_kategori[0].length; i++){
            jum_kategori[i] = 0;
        
            for(int j=0; j<tf_kategori.length; j++){
                jum_kategori[i] = jum_kategori[i] + tf_kategori[j][i];
            }
        }
        
        return jum_kategori;
    }
    
/*------------------------------------HITUNG PRIORI PER KATEGORI------------------------------------*/    
    public double[] PriorProbability(double[] nKategori, double keseluruhan){
        double[] prior = new double[nKategori.length];
        
        for(int i=0;i<nKategori.length;i++){
            prior[i] = nKategori[i]/keseluruhan;
        }
        
        return prior;
    }
    
/*------------------------------------HITUNG LIKELIHOOD PER KATA/KARAKTER------------------------------------*/     
    public double[] likelihoodProbability(double[] jum_kategori){
        double[] likelihood = new double[jum_kategori.length];
        
        //Query ada pada Semua Dokumen, Likelihood = frek kata + 1 / banyak tf + jumlah kategori 
        for(int i=0; i<likelihood.length; i++){
            likelihood[i] = 1;
            for (int j = 0; j < tf_kategori.length; j++) {
                if(tf_query[j] != 0){
                    likelihood[i] = likelihood[i] * ((tf_kategori[j][i] + 1)/(tf_kategori.length+jum_kategori[i]));
                }
            }
        } 
        
        return likelihood;
    }
 
/*------------------------------------HITUNG POSTERIOR PER KATEGORI------------------------------------*/     
    public double[] posteriorProbability(double[] prior, double[] likehood){
        double[] posterior = new double[5];
        
        //Posterior hasil = Prior * Likelihood
        for (int i = 0; i < posterior.length; i++) {
            posterior[i] = prior[i] * likehood[i];
        }
        
        return posterior;
    }
}
