/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scratch;

import database.DataBase;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.Socket;
import java.io.*;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.ConnectException;
import people.Pessoa;
import java.util.Scanner;
import resultado.Resultado;
/**
 * 
 * @author Vinicius Francisco da Silva
 */
public class Scratch{
   private static DataBase db;
    /**
     * 
     * @throws InterruptedException 
     * @throws java.io.IOException 
     * @throws java.net.ConnectException 
     */
    public static void chegadaDados() throws InterruptedException, IOException, ClassNotFoundException{
    	String ip = "127.0.0.1";
        int port = 42001;
        
        Socket socket = null;
        FileOutputStream fileoutputstream = null;
        DataInputStream in = null;     
        carregando();
            
        try{  
            socket = new Socket(ip, port); 
            db = new DataBase();
            db.connection();
            System.out.println("Conecção estabelecida com sucesso!\n");
            getSom();
        }catch(ConnectException e){
            System.out.println("ERRO: Não foi possível estabelecer conecção!");
            System.exit(1);
        }// End catch
         System.out.println("#################################################################################################################"); 
        //System.out.println("Pressione [q] para parar de ler dados\n");
        Scanner input = new Scanner(System.in);
        //char chr = ' ';
        
        Pessoa pessoa = new Pessoa();
        Resultado resultado = new Resultado();
        
        try{
            in = new DataInputStream(socket.getInputStream());
            while(true){
                if(in.available() > 0){
                    String string = ler(in);
                    if(getData(string,pessoa,resultado)){
                        db.insert(pessoa,resultado);
                        pessoa = new Pessoa();
                        resultado = new Resultado();
                    }// End if
                }// End if
            }// End while
        }catch(Exception e){
            e.printStackTrace();
        }// End catch
   }// End ler()

    /**
     * 
     * @param str
     * @return 
     */
    private static boolean getData(String str,Pessoa pessoa,Resultado resultado){
        boolean resp = false;
        if(str.contains("sensor-update "+String.valueOf((char)0x22)+"nome"+String.valueOf((char)0x22))){
            str = str.replace("sensor-update "+String.valueOf((char)0x22)+"nome"+String.valueOf((char)0x22),"");
            str = str.replace(String.valueOf((char)0x22),"");
            pessoa.setNome(str.trim());
        }else if(str.contains("sensor-update "+String.valueOf((char)0x22)+"email"+String.valueOf((char)0x22))){
            str = str.replace("sensor-update "+String.valueOf((char)0x22)+"email"+String.valueOf((char)0x22),"");
            str = str.replace(String.valueOf((char)0x22),"");
            pessoa.setEmail(str.trim());
        }else if(str.contains("sensor-update "+String.valueOf((char)0x22)+"primeiroNumero"+String.valueOf((char)0x22)) && 
            str.contains("resultado") && str.contains("segundoNumero")){
            str = str.replace("sensor-update "+String.valueOf((char)0x22)+"primeiroNumero"+String.valueOf((char)0x22),"");    
            str = str.replace(String.valueOf((char)0x22)+"resultado"+String.valueOf((char)0x22),"");
            str = str.replace(String.valueOf((char)0x22)+"segundoNumero"+String.valueOf((char)0x22),"");
            str = str.substring(1,str.length());
           if(str.contains("velocidade") && str.contains("tempo") && str.contains("total")){
               
           }// End if
            String[] string = str.split("  ");
            
            string[0] = string[0].trim();
            string[1] = string[1].trim();
            string[2] = string[2].trim();
            
            int[] integer = new int[3];
            if(isNumber(string[0]) && isNumber(string[1]) && isNumber(string[2])){
                try{    
                    integer[0] = Integer.parseInt(string[0]);
                    integer[1] = Integer.parseInt(string[1]);
                    integer[2] = Integer.parseInt(string[2]);
                }catch(Exception e){
                    e.printStackTrace();
                }// End catch
            }else{
                string[0] = getNumber(string[0]) != null ? getNumber(string[0]) : "0";
                string[1] = getNumber(string[1]) != null ? getNumber(string[1]) : "0";
                string[2] = getNumber(string[2]) != null ? getNumber(string[2]) : "0";
                
                try{    
                    integer[0] = Integer.parseInt(string[0]);
                    integer[1] = Integer.parseInt(string[1]);
                    integer[2] = Integer.parseInt(string[2]);
                }catch(Exception e){
                    e.printStackTrace();
                }// End catch
            }// End else
            
            if(resultado.getValor1_soma1() == -1 && resultado.getValor2_soma1() == -1 && resultado.getResposta_soma1() == -1 && resultado.getResultado_soma1() == -1){
               resultado.setValor1_soma1(integer[0]);
               resultado.setValor2_soma1(integer[2]);
               resultado.setResposta_soma1(integer[1]);
               resultado.setResultado_soma1(23);
            }else if(resultado.getValor1_soma2() == -1 && resultado.getValor2_soma2() == -1 && resultado.getResposta_soma2() == -1 && resultado.getResultado_soma2() == -1){
                resultado.setValor1_soma2(integer[0]);
                resultado.setValor2_soma2(integer[2]);
                resultado.setResposta_soma2(integer[1]);
                resultado.setResultado_soma2(23);
            }else{
                resultado.setValor1_soma3(integer[0]);
                resultado.setValor2_soma3(integer[2]);
                resultado.setResposta_soma3(integer[1]);
                resultado.setResultado_soma3(23);
            }// End else
        }// End else if
        
        if(!pessoa.isObjectEmpty() && !resultado.isObjectEmpty()){
            resp = true;
        }// End if
        return resp;
    }// End getData()
    
    public static boolean isNumber(String str){
        boolean resp = false;
        for(int i = 0; i < str.length(); i++){
            if(Character.isDigit(str.charAt(i))){
                resp = true;
            }else{
                return false;
            }// End else 
        }// End if
        return resp;
    }// End isNumber()
    
    public static String getNumber(String str){
        String string = "";
        boolean flag_fitstposition = false;
        boolean flag_character = false;
        for(int i = 0; i < str.length(); i++){
            if(Character.isDigit(str.charAt(i))){
                string += str.charAt(i);
            }else{
                i = str.length();
            }// End else
        }// End for    
        return isNumber(string.trim()) ? string.trim() : null;
    }// End getNumber()
    
    /**
     * 
     * @throws InterruptedException 
     */
    private static void carregando() throws InterruptedException{
	int i = 0;
        System.out.print("Loading " + (char)0x5B);
        String loading = "";
        
        while(loading.length() < 10){
            loading += (char)0x23;
            Thread.sleep(1000);
            System.out.print(loading);
            i++;
        }// while
        System.out.println((char)0x3E + "" + (char)0x5D + " - 100%");
    }// End carregando()

    /**
     * 
     */
    private static void getSom(){
	AudioClip clip = null;
        try{
            clip = Applet.newAudioClip(new URL("http://www.soundjay.com/button/beep-02.wav"));
        }catch(MalformedURLException e){
            e.printStackTrace();
        }// End catch
        clip.play();
    }// End getSom()

    /**
     * 
     * @param in
     * @return
     * @throws IOException 
     */
   private static String ler(DataInputStream in) throws IOException{
        in.readShort();
        return in.readUTF();
   }// End readMessage()
   
   /**
     *
     * @param args
     * @throws java.lang.Exception
     */
  
   public static void main(String[] args) throws Exception{
        System.out.println("\n#################################################################################################################\n");
        System.out.println("PONTIFÍCIA UNIVERSIDADE CATOLICA DE MINAS GERAIS - PUC MINAS");
        System.out.println("INSTITUTO DE CIENCIAS EXATAS INFORMATICA - ICEI");
        System.out.println("DEPARTAMENTO DE CIENCIA DA COMPUTACAO - DCC\n");
        System.out.println("=================================================================================================================\n");
        System.out.println("Software desenvolvido para a Disciplina de Introducao a pesquisa e informastica - IPI");
        System.out.println("Desenvolvido por: Vinicius Francisco da Silva, Stefany Gaspar\n");
        System.out.println("=================================================================================================================\n");
        System.out.println("BASE PARA COMUNICAÇÃO ENTRE SOFTWARES JAVA E SCRATCH");    
        chegadaDados();
   }// End main()
}// End class Scratch
