package Controller;

//import Entity.Dokumen_Kamus;

import Entity.Dokumen_Kamus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fitria Khoirunnisa
 */

public class D_Proses_Stemming {

    public String KataDasar(String kata) throws IOException {
        //Inisialisasi
        String akarkata = null;
        String kata_asli = kata;
        
        //Get Entity Kamus Kata Dasar
        Dokumen_Kamus dokumen = new Dokumen_Kamus();
        ArrayList<String> kata_dasar = dokumen.get_kata_dasar();
        
        //Pengecekan JIKA Dokumen(Kata)
        boolean syarat = false;
        for (int i = 0; i < kata_dasar.size(); i++) {

            if (kata_dasar.get(i).toLowerCase().trim().equals(kata.toLowerCase())) {
                syarat = true;
            }
        }
        if (syarat == true) {
            akarkata = kata;
        } else {
            kata = hapusInfleksionalSuffiks(kata);
//            System.out.println(kata);
            kata = hapusDerivationSuffiks(kata);
//            System.out.println(kata);
        }

        syarat = false;
        for (int i = 0; i < kata_dasar.size(); i++) {

            if (kata_dasar.get(i).toLowerCase().trim().equals(kata.toLowerCase())) {
                syarat = true;
            }
        }

        if (syarat == false) {
            akarkata = kata_asli;
        } else {
            akarkata = kata;
        }

        return akarkata;
    }

    public boolean kata_dasar(String kata) throws IOException {
        boolean syarat = false;
        
        Dokumen_Kamus dokumen = new Dokumen_Kamus();
        ArrayList<String> kata_dasar = dokumen.get_kata_dasar();
        
        for (int i = 0; i < kata_dasar.size(); i++) {

            if (kata_dasar.get(i).toLowerCase().trim().equals(kata.toLowerCase())) {
                syarat = true;
            }
        }
        
        return syarat;
    }

    public String hapusInfleksionalSuffiks(String kata) {
        if (kata.endsWith("lah") || kata.endsWith("kah") || kata.endsWith("nya") || kata.endsWith("tah") || kata.endsWith("pun")) {
            kata = kata.substring(0, kata.length() - 3);
        } else if (kata.endsWith("ku") || kata.endsWith("mu")) {
            kata = kata.substring(0, kata.length() - 2);
        }
      
        return kata;
    }

    public String hapusDerivationSuffiks(String kata) throws IOException {
        String bersikan;
        String akarkata;
        
        boolean isHapusSuffix = false;
        if (kata.endsWith("i")) {
            bersikan = kata.substring(0, kata.length() - 1);
            isHapusSuffix = true;
        } else if (kata.endsWith("kan")) {
            bersikan = kata.substring(0, kata.length() - 3);
            isHapusSuffix = true;
        } else if (kata.endsWith("an")) {
            bersikan = kata.substring(0, kata.length() - 2);
            isHapusSuffix = true;
        } else {
            bersikan = kata;
        }

        //3a. Jika Kata Ditemukan dalam Kamus maka Algoritma Berhenti 
        Dokumen_Kamus dokumen = new Dokumen_Kamus();
        ArrayList<String> kata_dasar = dokumen.get_kata_dasar();

        if (kata_dasar.contains(bersikan)) {
            akarkata = bersikan;
        } else {
            
            //lanjut ke langkah 4.
            //jika ada suffiks yang dihapus pergi ke langkah 4a.
            akarkata = bersikan;
            if (isHapusSuffix == true) {
                akarkata = derivationPrefiksA(akarkata);
            } else {
                //jika tidak ada pergi langkah 4b.
                akarkata = derivationPrefiksB(akarkata);
            }
        }
        return akarkata;
    }

    private String derivationPrefiksA(String akarKata) {
        //periksa kombinasi awalan dan akhiran yang tidak diijinkan
        boolean tipe1 = (akarKata.startsWith("be") && akarKata.endsWith("i"));
        boolean tipe2 = akarKata.startsWith("di") && akarKata.endsWith("an");
        boolean tipe3 = akarKata.startsWith("ke") && (akarKata.endsWith("i") || akarKata.endsWith("kan"));
        boolean tipe4 = akarKata.startsWith("me") && akarKata.endsWith("an");
        boolean tipe5 = akarKata.startsWith("se") && (akarKata.endsWith("i") || akarKata.endsWith("kan"));
        
        //jika kata tidak ditemukan maka menuju ke langkah 4b
        if (((tipe1) || (tipe2) || (tipe3) || (tipe4) || (tipe5)) == false) {
            try {
                akarKata = derivationPrefiksB(akarKata);
            } catch (IOException ex) {
                Logger.getLogger(D_Proses_Stemming.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return akarKata;
    }

    private String derivationPrefiksB(String akarKata) throws IOException {
        //tipe awalan ke-1 : -di,-ke,-se
        if ((akarKata.startsWith("di") || akarKata.startsWith("ke") || akarKata.startsWith("se")) && akarKata.length() > 2) {
            akarKata = akarKata.substring(2, akarKata.length());
//          System.out.println(akarKata);
        } 

        //tipe awalan ke-2 : -te,-me,-be,-pe
        else if ((akarKata.startsWith("te") || akarKata.startsWith("me") || akarKata.startsWith("be") || akarKata.startsWith("pe"))) {
            
            //jika tipe prefiks bukan none digunakan rabel 2.
            if (akarKata.startsWith("be")) {
                if (akarKata.startsWith("ber") && vowel(akarKata.charAt(3)) == true) {
                    akarKata = akarKata.substring(3, akarKata.length());
                    String kata_asli = akarKata;
                    if (!kata_dasar(akarKata)) {
                        akarKata = kata_asli.substring(2, kata_asli.length());
                    }

                } else if (akarKata.startsWith("belajar")) {
                    akarKata = "ajar";

                } else if (akarKata.length() > 5) {
                    if (akarKata.startsWith("be")
                            && (akarKata.charAt(2) != 'r' || akarKata.charAt(2) != 'l' || akarKata.charAt(5) != 'r' || akarKata.charAt(5) != 'l')
                            && consonan(akarKata.charAt(2)) == true
                            && akarKata.charAt(3) == 'e'
                            && akarKata.charAt(4) == 'r'
                            && consonan(akarKata.charAt(5)) == true) {
                        
                        akarKata = akarKata.substring(2, akarKata.length());
                       
                    } else if (akarKata.startsWith("ber")
                            && consonan(akarKata.charAt(3)) == true
                            && any_letter(akarKata.charAt(4)) == true ) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                       
                    } else if (akarKata.length() > 7) {
                        if (akarKata.startsWith("be") && akarKata.charAt(3) != 'r' && consonan(akarKata.charAt(3)) == true
                                && any_letter(akarKata.charAt(4)) == true
                                && akarKata.charAt(5) == 'e'
                                && akarKata.charAt(6) == 'r'
                                && vowel(akarKata.charAt(7)) == true) {
                            
                            akarKata = akarKata.substring(7, akarKata.length());
                        }
                    }
                }

            } else if (akarKata.startsWith("te")) {
                if (akarKata.startsWith("ter")
                        && vowel(akarKata.charAt(3)) == true) {
                    String kata_asli = akarKata;
                    akarKata = akarKata.substring(3, akarKata.length());
                    if (!kata_dasar(akarKata)) {
                        akarKata = kata_asli.substring(2, kata_asli.length());
                    }
                    
                } else if (akarKata.length() > 6) {
                    if (akarKata.startsWith("ter")
                            && (akarKata.charAt(3) != 'r' && consonan(akarKata.charAt(3)) == true)
                            && akarKata.charAt(4) == 'e'
                            && akarKata.charAt(5) == 'r'
                            && vowel(akarKata.charAt(6)) == true) {
                        
                        akarKata = akarKata.substring(6, akarKata.length());

                    }else    if (akarKata.startsWith("ter")
                            && consonan(akarKata.charAt(3)) == true
                            && any_letter(akarKata.charAt(4)) == true ) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                       
                    }  else if (akarKata.length() > 7) {
                        if (akarKata.startsWith("te")
                                && (akarKata.charAt(3) != 'r' || akarKata.charAt(6) != 'r')
                                && consonan(akarKata.charAt(3)) == true
                                && akarKata.charAt(4) == 'e'
                                && akarKata.charAt(5) == 'r'
                                && consonan(akarKata.charAt(6)) == true) {
                            
                            akarKata = akarKata.substring(7, akarKata.length());
                        }
                    }
                }
                
            } else if (akarKata.startsWith("me")) {
                if (akarKata.startsWith("me") && (akarKata.charAt(2) == 'l' || akarKata.charAt(2) == 'r' || akarKata.charAt(2) == 'w' || akarKata.charAt(2) == 'y') && vowel(akarKata.charAt(3)) == true) {
                    akarKata = akarKata.substring(2, akarKata.length());

                } else if (akarKata.length() > 3) {
                    if (akarKata.startsWith("mem") && (akarKata.charAt(3) == 'b' || akarKata.charAt(3) == 'f' || akarKata.charAt(3) == 'v')) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                        
                    } else if (akarKata.startsWith("mem") && ((akarKata.charAt(3) == 'r'  && vowel(akarKata.charAt(4)) == true) || vowel(akarKata.charAt(3)) == true)) {
                        String kata_asli = akarKata;
                       
                        akarKata = akarKata.substring(3, akarKata.length());
                        
                        if (!kata_dasar(akarKata)) {
                            akarKata = 'p' + kata_asli.substring(3, kata_asli.length());
                        }

                    } else if (akarKata.startsWith("men") && (akarKata.charAt(3) == 'c' || akarKata.charAt(3) == 'd' || akarKata.charAt(3) == 'j' || akarKata.charAt(3) == 'z')) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                        
                    } else if (akarKata.startsWith("men") && vowel(akarKata.charAt(3)) == true) {
                        String kata_asli = akarKata;
                        
                        akarKata = akarKata.substring(2, akarKata.length());
                        
                        if (!kata_dasar(akarKata)) {
                            akarKata = 't' + kata_asli.substring(3, kata_asli.length());
                        }

                    } else if (akarKata.length() > 4) {
                     
                        if (akarKata.startsWith("meng") && (akarKata.charAt(4) == 'g' || akarKata.charAt(4) == 'h' || akarKata.charAt(4) == 'q')) {
                            
                            akarKata = akarKata.substring(4, akarKata.length());
                            
                        } else if (akarKata.startsWith("meng") && vowel(akarKata.charAt(4))) {
                            String kata_asli = akarKata;
                            
                            akarKata = akarKata.substring(4, akarKata.length());
                            
                            if (!kata_dasar(akarKata)) {
                                akarKata = 'k' + kata_asli.substring(4, kata_asli.length());
                            }
                        } else if (akarKata.startsWith("meny") && vowel(akarKata.charAt(4))) {

                            akarKata = 's' + akarKata.substring(4, akarKata.length());

                        } else if (akarKata.length() > 5) {
                            if (akarKata.startsWith("memp") && vowel(akarKata.charAt(4)) && akarKata.charAt(4) != 'e') {
                                
                                akarKata = akarKata.substring(3, akarKata.length());
                                
                            } else if (akarKata.length() > 6) {
                                if (akarKata.startsWith("mempe") && (akarKata.charAt(5) == 'r' || akarKata.charAt(5) == 'l')) {
                                   
                                    akarKata = akarKata.substring(3, akarKata.length());
                                }
                            }
                        }
                    }
                }
            }
            if (akarKata.startsWith("pe")) {
                if (akarKata.length() > 3) {
                    if (akarKata.startsWith("pe") && (akarKata.charAt(2) == 'w' || akarKata.charAt(2) == 'y') && vowel(akarKata.charAt(3))) {
                        
                        akarKata = akarKata.substring(2, akarKata.length());
                        
                    } else if (akarKata.startsWith("per") && vowel(akarKata.charAt(3))) {
                        String kata_asli = akarKata;
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                        
                        if (!kata_dasar(akarKata)) {
                            akarKata = kata_asli.substring(2, kata_asli.length());
                        }
                        
                    } else if (akarKata.startsWith("pem") && (akarKata.charAt(3) == 'b' ||akarKata.charAt(3) == 'f' || akarKata.charAt(3) == 'v')) {

                        akarKata = akarKata.substring(3, akarKata.length());

                    } else if (akarKata.startsWith("pem") && ((akarKata.charAt(3) == 'r' && vowel(akarKata.charAt(4))) || vowel(akarKata.charAt(3)))) {
                        String kata_asli = akarKata;
                        
                        akarKata = akarKata.substring(2, akarKata.length());
                        
                        if (!kata_dasar(akarKata)) {
                            akarKata = 'p' + kata_asli.substring(2, kata_asli.length());
                        }
                    } else if (akarKata.startsWith("pen") && (akarKata.charAt(3) == 'c' || akarKata.charAt(3) == 'd' || akarKata.charAt(3) == 'j' || akarKata.charAt(3) == 'z')) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());

                    } else if (akarKata.startsWith("pen") && vowel(akarKata.charAt(3))) {
                        String kata_asli = akarKata;
                        
                        akarKata = akarKata.substring(2, akarKata.length());
                        
                        if (!kata_dasar(akarKata)) {
                            
                            akarKata = 't' + kata_asli.substring(3, kata_asli.length());
                        }
                        
                    } else if (akarKata.startsWith("pel") && vowel(akarKata.charAt(3)) && akarKata != "pelajar") {

                        akarKata = akarKata.substring(2, akarKata.length());

                    } else if (akarKata.length() > 4) {
                        if (akarKata.startsWith("peng") && (akarKata.charAt(4) == 'g' || akarKata.charAt(4) == 'h' || akarKata.charAt(4) == 'q')) {

                            akarKata = akarKata.substring(4, akarKata.length());

                        }else if (akarKata.startsWith("per") && consonan(akarKata.charAt(3)) == true && any_letter(akarKata.charAt(4)) == true ) {
                        
                        akarKata = akarKata.substring(3, akarKata.length());
                       
                    } else if (akarKata.startsWith("peng") && vowel(akarKata.charAt(4))) {
                            String kata_asli = akarKata;
                            
                            akarKata = akarKata.substring(4, akarKata.length());
                            
                            if (!kata_dasar(akarKata)) {
                                
                                akarKata = 'k' + kata_asli.substring(4, kata_asli.length());
                                
                            }
                            
                        } else if (akarKata.startsWith("peny") && vowel(akarKata.charAt(4))) {

                            akarKata = 's' + akarKata.substring(4, akarKata.length());

                        } else if (akarKata.length() > 5) {
                            if (akarKata.startsWith("pe")
                                    && consonan(akarKata.charAt(2))
                                    && (akarKata.charAt(2) == 'r' || akarKata.charAt(2) == 'w' || akarKata.charAt(2) == 'y' || akarKata.charAt(2) == 'l' || akarKata.charAt(2) == 'm' || akarKata.charAt(2) == 'n')
                                    && akarKata.charAt(3) == 'e'
                                    && akarKata.charAt(4) == 'r'
                                    && vowel(akarKata.charAt(5))) {

                                akarKata = 's' + akarKata.substring(4, akarKata.length());

                            } else if (akarKata.length() > 6) {
                                if (akarKata.startsWith("per")
                                        && consonan(akarKata.charAt(3))
                                        && any_letter(akarKata.charAt(4))
                                        && (akarKata.charAt(3) != 'r')
                                        && akarKata.charAt(5) == 'e'
                                        && akarKata.charAt(6) == 'r'
                                        && vowel(akarKata.charAt(7))) {

                                    akarKata = 's' + akarKata.substring(7, akarKata.length());
                                }
                            }
                        }
                    }
                }
            }
        }
        return akarKata;
    }

    private boolean vowel(char huruf) {
        if (huruf == 'a' || huruf == 'i' || huruf == 'u' || huruf == 'e' || huruf == 'o') {
            return true;
        }
        return false;
    }

    private boolean consonan(char huruf) {
        if (huruf == 'b' || huruf == 'c' || huruf == 'd' || huruf == 'f' || huruf == 'g'
                || huruf == 'h' || huruf == 'j' || huruf == 'k' || huruf == 'l' || huruf == 'm'
                || huruf == 'n' || huruf == 'p' || huruf == 'q' || huruf == 'r' || huruf == 's'
                || huruf == 't' || huruf == 'v' || huruf == 'w' || huruf == 'x' || huruf == 'y' || huruf == 'z') {
            return true;
        }
        return false;
    }

    private boolean any_letter(char huruf) {
        if (huruf == 'a' || huruf == 'i' || huruf == 'u' || huruf == 'e' || huruf == 'o'
                || huruf == 'b' || huruf == 'c' || huruf == 'd' || huruf == 'f' || huruf == 'g'
                || huruf == 'h' || huruf == 'j' || huruf == 'k' || huruf == 'l' || huruf == 'm'
                || huruf == 'n' || huruf == 'p' || huruf == 'q' || huruf == 'r' || huruf == 's'
                || huruf == 't' || huruf == 'v' || huruf == 'w' || huruf == 'x' || huruf == 'y' || huruf == 'z') {
            return true;
        }
        return false;
    }
}
