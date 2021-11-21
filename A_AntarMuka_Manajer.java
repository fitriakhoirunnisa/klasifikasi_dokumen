/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 *
 * @author User
 */

public class A_AntarMuka_Manajer {
    C_Text_Preprocessing praproses;
    B_KFold_CrossValidation kfold;
    E_Pemecahan_Ngram pemecahanngram;
    F_Term_Frequency weightterm, weightngram;
    G_NBC_Klasifikasi nbcprobabilitas_term, nbcprobabilitas_ngram;
    
    //muat data
    int ukuran_k, fold;
    String[] nama_file, teks_file, kategori_file ; 
    ArrayList<String[]> nama_file_uji, data_uji, kategori_uji, data_latih, kategori_latih;
        
    //praproses direct, grup, kategori grup
    double keseluruhan;
    double[] nKategori;
    ArrayList<ArrayList> token_stemming_grup_kategori;
    ArrayList<String> token_stemming_query, token_stemming_grup;
    ArrayList<String> token_stemming_kesehatan, token_stemming_politik, token_stemming_ekonomi, token_stemming_teknologi, token_stemming_olahraga;

    //term frequency, ngram frequency
    double[][] tf_kategori;
    double[] jum_kategori_term, jum_kategori_ngram, prior_term, prior_ngram;
    String[] hasil_klasifikasi_term, hasil_klasifikasi_ngram;
    ArrayList<String> nGramList, nGramQueryList;
    ArrayList<ArrayList> nGramListKategori;
    
    public void initparameter(int k, int fold){
        this.ukuran_k = k;
        this.fold = fold;
    }
    
    /*------------------------------------LOAD FILE ALL DOCUMENT (MUAT DATA)------------------------------------*/
    public void muat_data(String path){
        File folder = new File(path);
        File[] list_file = folder.listFiles();
        
        kfold = new B_KFold_CrossValidation(10); 
    
        try {
            nama_file = new String[list_file.length];
            teks_file = new String[list_file.length];
            kategori_file= new String[list_file.length];
            
            for(int i=0;i<list_file.length;i++)
            {
                FileInputStream fstream= new FileInputStream(list_file[i]);
                DataInputStream dataIn = new DataInputStream(fstream);
                
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(dataIn))) {
                    String nilai;
                    while((nilai = reader.readLine())!= null){
                        teks_file[i] = teks_file[i] + nilai +"\n";
                    }
                    reader.close();
                }
                nama_file[i] = list_file[i].getName().split("_")[0];
                kategori_file[i] = list_file[i].getName().split("_")[1].replace(".txt", "");
            }
        } catch (IOException ex) {
          System.out.println("Pengambilan File gagal"+ex.getMessage());
        }
        
        kfold.initData(nama_file, teks_file, kategori_file);
        kfold.setDataKFoldCrossValidation();
        
        nama_file_uji = kfold.getKFoldNamaFileUji();
        data_uji = kfold.getKFoldUji();
        kategori_uji = kfold.getKFoldKategoriUji();
        data_latih = kfold.getKFoldLatih();
        kategori_latih = kfold.getKFoldKategoriLatih();  
    }
    
    /*------------------------------------PRAPROSES SATU DOKUMEN (DATA TESTING (QUERY))------------------------------------*/
    public void Praproses_kata_direct(String teks) throws IOException{
        boolean isAda;
        String teks_cs;
        ArrayList<String> token_teks, token_filter, token_stem;
        
        praproses = new C_Text_Preprocessing();
        token_stemming_query = new ArrayList<>();
        
        teks_cs = praproses.Casefolding(teks);
        token_teks = praproses.tokenizing(teks_cs);
        token_filter = praproses.filtering(token_teks);
        token_stem = praproses.stemming(token_filter);
        
        for(int i=0; i<token_stem.size(); i++){
            if(token_stemming_query.isEmpty()){
                token_stemming_query.add(token_stem.get(i));
            }
            else{
                isAda = false;
                for(int k=0; k<token_stemming_query.size(); k++){
                    if((token_stem.get(i)).equals(token_stemming_query.get(k))){
                        isAda = true;
                    }
                }

                if(isAda == false){
                    token_stemming_query.add(token_stem.get(i));
                }
            }
        }
//        for (int i = 0; i < token_stemming_query.size(); i++) {
//            System.out.println("Token Query : "+token_stemming_query.get(i));
//        }
    }
    
    /*------------------------------------PRAPROSES ALL DOCUMENT (DOCUMENT TRAINING)------------------------------------*/    
    public void Praproses_kata_grup(String[] dataLatih) throws IOException{
        boolean isAda;
        String teks_file_cs;
        ArrayList<String> token_teks, token_filter, token_stemming;
        
//        for (int i = 0; i < data_latih.get(fold).length; i++) {
//            System.out.println("Dokumen Data Training ke-"+(i+1)+" : "+data_latih.get(fold)[i]);
//            System.out.println("Dokumen Kategori Training ke-"+(i+1)+" : "+kategori_latih.get(fold)[i]);
//        }

        praproses = new C_Text_Preprocessing();
        token_stemming_grup = new ArrayList<>();
        
        for(int i=0; i<dataLatih.length; i++){
            teks_file_cs = praproses.Casefolding(dataLatih[i]);
            token_teks = praproses.tokenizing(teks_file_cs);
            token_filter =  praproses.filtering(token_teks);
            token_stemming = praproses.stemming(token_filter);
            
            if(i==0){
                for(int j=0;j<token_stemming.size();j++){
                    token_stemming_grup.add(token_stemming.get(j));
                }
            }
            else{
                for(int j=0;j<token_stemming.size();j++){
                    isAda = false;
                    for(int k=0; k<token_stemming_grup.size(); k++){
                        if((token_stemming.get(j)).equals(token_stemming_grup.get(k))){
                            isAda = true;
                        }
                    }
                    
                    if(isAda == false){
                        token_stemming_grup.add(token_stemming.get(j));
                    }
                } 
            }   
        }  
//        for (int i = 0; i < token_stemming_grup.size(); i++) {
//            System.out.println("Token Training : "+token_stemming_grup.get(i));
//        }
    }  
    
    
    /*------------------------------------PRAPROSES DOCUMENT CATEGORY------------------------------------*/
    public void Praproses_kata_kategori(String[] dataLatih, String[] kategorilatih) throws IOException{
        boolean isAda;
        String[] teks_file_kategori_cs;
        ArrayList<String> token_teks, token_filter, token_stemming;
        
        praproses = new C_Text_Preprocessing();
        teks_file_kategori_cs = new String[dataLatih.length];
        
        token_stemming_kesehatan = new ArrayList<>();
        token_stemming_ekonomi = new ArrayList<>();
        token_stemming_politik = new ArrayList<>();
        token_stemming_teknologi = new ArrayList<>();
        token_stemming_olahraga = new ArrayList<>();
        
        token_stemming_grup_kategori = new ArrayList<>();
        
        keseluruhan = kategorilatih.length;
        
        nKategori = new double[5];
        for (int i = 0; i < nKategori.length; i++) {
            nKategori[i] = 0;
        }

//        System.out.println("Jumlah Data Latih : "+kategorilatih.length);
        
        for(int i=0;i<dataLatih.length;i++){
            if(kategorilatih[i].split("_")[0].contains("Kesehatan")){
                nKategori[0]++;
                teks_file_kategori_cs[i] = praproses.Casefolding(dataLatih[i]);
                token_teks = praproses.tokenizing(teks_file_kategori_cs[i]);
                token_filter =  praproses.filtering(token_teks);
                token_stemming = praproses.stemming(token_filter);
                
                for(int j=0;j<token_stemming.size();j++){
                    if(token_stemming_kesehatan.isEmpty()){
                        token_stemming_kesehatan.add(token_stemming.get(j));
                    }
                    else{
                        isAda = false;
                        for(int k=0; k<token_stemming_kesehatan.size(); k++){
                            if((token_stemming.get(j)).equals(token_stemming_kesehatan.get(k))){
                                isAda = true;
                            }
                        }

                        if(isAda == false){
                            token_stemming_kesehatan.add(token_stemming.get(j));
                        }
                    }
                }  
//                for (int j = 0; j < token_stemming_kesehatan.size(); j++) {
//                    System.out.println("token stemming kesehatan : "+token_stemming_kesehatan.get(j));
//                }
            }
            else if(kategorilatih[i].split("_")[0].contains("Ekonomi")){
                nKategori[1]++;
                teks_file_kategori_cs[i] = praproses.Casefolding(dataLatih[i]);
                token_teks = praproses.tokenizing(teks_file_kategori_cs[i]);
                token_filter =  praproses.filtering(token_teks);
                token_stemming = praproses.stemming(token_filter);
                
                for(int j=0;j<token_stemming.size();j++){
                    if(token_stemming_ekonomi.isEmpty()){
                        token_stemming_ekonomi.add(token_stemming.get(j));
                    }
                    else{
                        isAda = false;
                        for(int k=0; k<token_stemming_ekonomi.size(); k++){
                            if((token_stemming.get(j)).equals(token_stemming_ekonomi.get(k))){
                                isAda = true;
                            }
                        }

                        if(isAda == false){
                            token_stemming_ekonomi.add(token_stemming.get(j));
                        }
                    }
                }
//                for (int j = 0; j < token_stemming_ekonomi.size(); j++) {
//                    System.out.println("token stemming ekonomi : "+token_stemming_ekonomi.get(j));
//                }
            }
            else if(kategorilatih[i].split("_")[0].contains("Politik")){
                nKategori[2]++;
                teks_file_kategori_cs[i] = praproses.Casefolding(dataLatih[i]);
                token_teks = praproses.tokenizing(teks_file_kategori_cs[i]);
                token_filter =  praproses.filtering(token_teks);
                token_stemming = praproses.stemming(token_filter);
                
                for(int j=0;j<token_stemming.size();j++){
                    if(token_stemming_politik.isEmpty()){
                        token_stemming_politik.add(token_stemming.get(j));
                    }
                    else{
                        isAda = false;
                        for(int k=0; k<token_stemming_politik.size(); k++){
                            if((token_stemming.get(j)).equals(token_stemming_politik.get(k))){
                                isAda = true;
                            }
                        }

                        if(isAda == false){
                            token_stemming_politik.add(token_stemming.get(j));
                        }
                    }
                }
//                for (int j = 0; j < token_stemming_politik.size(); j++) {
//                    System.out.println("token stemming politik: "+token_stemming_politik.get(j));
//                }
            }
            else if(kategorilatih[i].split("_")[0].contains("Teknologi")){
                nKategori[3]++;
                teks_file_kategori_cs[i] = praproses.Casefolding(dataLatih[i]);
                token_teks = praproses.tokenizing(teks_file_kategori_cs[i]);
                token_filter =  praproses.filtering(token_teks);
                token_stemming = praproses.stemming(token_filter);
                
                for(int j=0;j<token_stemming.size();j++){
                    if(token_stemming_teknologi.isEmpty()){
                        token_stemming_teknologi.add(token_stemming.get(j));
                    }
                    else{
                        isAda = false;
                        for(int k=0; k<token_stemming_teknologi.size(); k++){
                            if((token_stemming.get(j)).equals(token_stemming_teknologi.get(k))){
                                isAda = true;
                            }
                        }

                        if(isAda == false){
                            token_stemming_teknologi.add(token_stemming.get(j));
                        }
                    }
                }
//                for (int j = 0; j < token_stemming_teknologi.size(); j++) {
//                    System.out.println("token stemming teknologi: "+token_stemming_teknologi.get(j));
//                }
            }
            else{
                nKategori[4]++;
                teks_file_kategori_cs[i] = praproses.Casefolding(dataLatih[i]);
                token_teks = praproses.tokenizing(teks_file_kategori_cs[i]);
                token_filter =  praproses.filtering(token_teks);
                token_stemming = praproses.stemming(token_filter);

                for(int j=0;j<token_stemming.size();j++){
                    if(token_stemming_olahraga.isEmpty()){
                        token_stemming_olahraga.add(token_stemming.get(j));
                    }
                    else{
                        isAda = false;
                        for(int k=0; k<token_stemming_olahraga.size(); k++){
                            if((token_stemming.get(j)).equals(token_stemming_olahraga.get(k))){
                                isAda = true;
                            }
                        }

                        if(isAda == false){
                            token_stemming_olahraga.add(token_stemming.get(j));
                        }
                    }
                }
//                for (int j = 0; j < token_stemming_olahraga.size(); j++) {
//                    System.out.println("token stemming olahraga: "+token_stemming_olahraga.get(j));
//                }
            }
        }
        
        token_stemming_grup_kategori.add(token_stemming_kesehatan);
        token_stemming_grup_kategori.add(token_stemming_ekonomi);
        token_stemming_grup_kategori.add(token_stemming_politik);
        token_stemming_grup_kategori.add(token_stemming_teknologi);
        token_stemming_grup_kategori.add(token_stemming_olahraga);
    }
    
    /*------------------------------------PRAPROSES PEMECAHAN KATA KE NGRAM (QUERY)------------------------------------*/
    public ArrayList<String> pemecahan_kata_to_ngram(ArrayList<String> token_list){
        boolean ada;
        ArrayList<String> n_gram_list_temp, n_gram_list;
        pemecahanngram = new E_Pemecahan_Ngram();
  
        n_gram_list_temp = pemecahanngram.Hitung_Bigram(token_list);

        n_gram_list = new ArrayList<>();

        for(int m=0; m<n_gram_list_temp.size(); m++){
            ada = false;
            
            if(n_gram_list.isEmpty()){
                n_gram_list.add(n_gram_list_temp.get(m));
            }
            else{
                for(int i=0; i<n_gram_list.size(); i++){
                    if(n_gram_list.get(i).equals(n_gram_list_temp.get(m))){
                        ada = true;
                    }
                }
                
                if(ada==false){
                    n_gram_list.add(n_gram_list_temp.get(m));
                }
            }
        } 
//        System.out.println("ngram: "+n_gram_list);
            
        return n_gram_list;
    }
    
//    /*------------------------------------PRAPROSES PEMECAHAN KATA KE NGRAM (QUERY)------------------------------------*/
 public ArrayList<String> pemecahan_kategori_to_ngram(ArrayList<String> token_list){
        boolean ada;
        ArrayList<String> n_gram_list_temp, n_gram_list_kategori;
        pemecahanngram = new E_Pemecahan_Ngram();
  
        n_gram_list_temp = pemecahanngram.Hitung_Bigram(token_list);

        n_gram_list_kategori = new ArrayList<>();

        for(int m=0; m<n_gram_list_temp.size(); m++){
            ada = false;
            
            if(n_gram_list_kategori.isEmpty()){
                n_gram_list_kategori.add(n_gram_list_temp.get(m));
            }
        } 
//        System.out.println("ngram: "+n_gram_list_kategori);
            
        return n_gram_list_kategori;
    }
    
    
    /*------------------------------------TRAINING TERM------------------------------------*/
    public void pelatihan_term() throws IOException{
        
        //Test Dokumen Data Latih & Kategori
//        for (int i = 0; i < data_latih.get(fold).length; i++) {
//            System.out.println("Data Training ke-"+(i+1)+ "Kategori-"+kategori_latih.get(fold)[i]+ "\t:\t" +data_latih.get(fold)[i]);
//        }
        
        Praproses_kata_grup(data_latih.get(fold));
        Praproses_kata_kategori(data_latih.get(fold), kategori_latih.get(fold));
        
        weightterm = new F_Term_Frequency();
        weightterm.initTermAllDoc(token_stemming_grup);
        weightterm.initTermKategori(token_stemming_grup_kategori);
        
        weightterm.tfTermKategori();
        tf_kategori = weightterm.getTFKategori();
        
        nbcprobabilitas_term = new G_NBC_Klasifikasi();
        nbcprobabilitas_term.initTfKategori(tf_kategori);
        jum_kategori_term = nbcprobabilitas_term.hitung_jum_kategori();
        prior_term = nbcprobabilitas_term.PriorProbability(nKategori, keseluruhan);
//        for (int j = 0; j < prior_term.length; j++) {
//                System.out.println("prior \t: "+prior_term[j]);
//        }
    }
    
    /*------------------------------------TESTING TERM------------------------------------*/
    public String[] klasifikasi_term() throws IOException{
        double[] tf_query, likehood, posterior;
        double max, idx_max;

        //Test Dokumen Data Uji & Kategori
//        for (int i = 0; i < data_uji.get(fold).length; i++) {
//            System.out.println("Data Testing ke-"+(i+1)+ "Kategori-"+kategori_uji.get(fold)[i]+ "\t:\t" +data_uji.get(fold)[i]);
//        }
        
        pelatihan_term();
        
        hasil_klasifikasi_term = new String[data_uji.get(fold).length];
        
        for (int i = 0; i < data_uji.get(fold).length; i++) {
            Praproses_kata_direct(data_uji.get(fold)[i]);
            
            weightterm.initTermDoc(token_stemming_query);
            
            weightterm.tfTermQuery();
            tf_query = weightterm.getTFQuery();
            
            nbcprobabilitas_term.initTfQuery(tf_query);
            likehood = nbcprobabilitas_term.likelihoodProbability(jum_kategori_term);
//            for (int j = 0; j < likehood.length; j++) {
//                System.out.println("likehood\t: "+likehood[j]);
//            }
            posterior = nbcprobabilitas_term.posteriorProbability(prior_term, likehood);
//            for (int j = 0; j < posterior.length; j++) {
//                System.out.println("posterior\t: "+posterior[j]);
//            }
            
            idx_max = 0;
            max = posterior[0];
            for (int j = 0; j < posterior.length; j++) {
                if(max < posterior[j]){
                    max = posterior[j];
                    idx_max = j;
                }
            }

            if(idx_max == 0){
                hasil_klasifikasi_term[i] = "Kesehatan";
            }
            else if(idx_max == 1){
                hasil_klasifikasi_term[i] = "Ekonomi";
            }
            else if(idx_max == 2){
                hasil_klasifikasi_term[i] = "Politik";
            }
            else if(idx_max == 3){
                hasil_klasifikasi_term[i] = "Teknologi";
            }
            else{
                hasil_klasifikasi_term[i] = "Olahraga";
            }
            
        }
//        for (int i = 0; i < hasil_klasifikasi_term.length; i++) {
//            System.out.println("Hasil Klasifikasi Term : "+hasil_klasifikasi_term);
//        }
        
        return hasil_klasifikasi_term;
    }
    
    /*------------------------------------TRAINING NGRAM------------------------------------*/
    public void pelatihan_ngram() throws IOException{

        nGramListKategori = new ArrayList<>();
        
        //Test Dokumen Data Latih & Kategori
//        for (int i = 0; i < data_latih.get(fold).length; i++) {
//            System.out.println("Data Training ke-"+(i+1)+ "Kategori-"+kategori_latih.get(fold)[i]+ "\t:\t" +data_latih.get(fold)[i]);
//        }
        
        Praproses_kata_grup(data_latih.get(fold));
        Praproses_kata_kategori(data_latih.get(fold), kategori_latih.get(fold));
        
        nGramList = pemecahan_kata_to_ngram(token_stemming_grup);
//        for (int j = 0; j < nGramList.size(); j++) {
//            System.out.println("ngram grup: "+nGramList.get(j));
//        }
        
        nGramListKategori.add(pemecahanngram.Hitung_Bigram(token_stemming_grup_kategori.get(0)));
        nGramListKategori.add(pemecahanngram.Hitung_Bigram(token_stemming_grup_kategori.get(1)));
        nGramListKategori.add(pemecahanngram.Hitung_Bigram(token_stemming_grup_kategori.get(2)));
        nGramListKategori.add(pemecahanngram.Hitung_Bigram(token_stemming_grup_kategori.get(3)));
        nGramListKategori.add(pemecahanngram.Hitung_Bigram(token_stemming_grup_kategori.get(4)));
        
//        for (int j = 0; j < nGramListKategori.size(); j++) {
//            System.out.println("ngram kategori: "+nGramListKategori.get(j));
//        }
        
        weightngram = new F_Term_Frequency();
        weightngram.initNGaramTermAllDoc(nGramList);
        weightngram.initNGaramTermKategori(nGramListKategori);
        
        weightngram.tfNgramKategori();
        tf_kategori = weightngram.getTFKategori();
//        for (int j = 0; j < tf_kategori.length; j++) {
//                System.out.println("tf\t: "+tf_kategori[j]);
//        }
        
        nbcprobabilitas_ngram = new G_NBC_Klasifikasi();
        nbcprobabilitas_ngram.initTfKategori(tf_kategori);
        jum_kategori_ngram = nbcprobabilitas_ngram.hitung_jum_kategori();
        prior_ngram = nbcprobabilitas_ngram.PriorProbability(nKategori, keseluruhan);
//        for (int j = 0; j < prior_ngram.length; j++) {
//                System.out.println("prior\t: "+prior_ngram[j]);
//        }
        
    }
    
    /*------------------------------------TESTING NGRAM------------------------------------*/
    public String[] klasifikasi_ngram() throws IOException{
        double[] tf_query_ngram, likehood, posterior;
        double max, idx_max;
        
        //Test Dokumen Data Uji & Kategori
//        for (int i = 0; i < data_uji.get(fold).length; i++) {
//            System.out.println("Data Testing ke-"+(i+1)+ "Kategori-"+kategori_uji.get(fold)[i]+ "\t:\t" +data_uji.get(fold)[i]);
//        }
        
        pelatihan_ngram();
        
        hasil_klasifikasi_ngram = new String[data_uji.get(fold).length];
        
        for (int i = 0; i < data_uji.get(fold).length; i++) {
            Praproses_kata_direct(data_uji.get(fold)[i]);
            
            nGramQueryList = pemecahan_kata_to_ngram(token_stemming_query);
//            for (int j = 0; j < nGramQueryList.size(); j++) {
//                System.out.println("ngram query: "+nGramQueryList.get(j));
//            }
            
            weightngram.initNGaramTermDoc(nGramQueryList);
            
            weightngram.tfNgramQuery();
            tf_query_ngram = weightngram.getTFQuery();
            
            nbcprobabilitas_ngram.initTfQuery(tf_query_ngram);
            likehood = nbcprobabilitas_ngram.likelihoodProbability(jum_kategori_ngram);
//            for (int j = 0; j < likehood.length; j++) {
//                System.out.println("likelihood\t: "+likehood[j]);
//            }
            posterior = nbcprobabilitas_ngram.posteriorProbability(prior_ngram, likehood);
//            for (int j = 0; j < posterior.length; j++) {
//                System.out.println("posterior: "+posterior[j]);
//            }

            idx_max = 0;
            max = posterior[0];
            for (int j = 0; j < posterior.length; j++) {
                if(max < posterior[j]){
                    max = posterior[j];
                    idx_max = j; 
                }
            }
           
            if(idx_max == 0){
                hasil_klasifikasi_ngram[i] = "Kesehatan";
            }
            else if(idx_max == 1){
                hasil_klasifikasi_ngram[i] = "Ekonomi";
            }
            else if(idx_max == 2){
                hasil_klasifikasi_ngram[i] = "Politik";
            }
            else if(idx_max == 3){
                hasil_klasifikasi_ngram[i] = "Teknologi";
            }
            else{
                hasil_klasifikasi_ngram[i] = "Olahraga";
            }
        }
        
//        for (int i = 0; i < hasil_klasifikasi_ngram.length; i++) {
//            System.out.println("Hasil Klasifikasi Term : "+hasil_klasifikasi_ngram);
//        }
        
        return hasil_klasifikasi_ngram;
    }
    
    /*------------------------------------GET FILE------------------------------------*/
    public String[] getTeksFiles() {
        return teks_file;
    }
    
    public ArrayList<String[]> getKategoriUji(){
        return kategori_uji;
    }
    
    public ArrayList<String[]> getNamaFileUji(){
        return nama_file_uji;
    }
    
}