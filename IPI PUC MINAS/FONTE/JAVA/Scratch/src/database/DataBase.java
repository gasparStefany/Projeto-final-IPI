/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import people.Pessoa;
import resultado.Resultado;

/**
 *
 * @author Vinicius Francisco da Silva
 */
public class DataBase{
    private short primary_key;
    private Connection connect;
    private static final Scanner scanner = new Scanner(System.in);
    File file;
    private RandomAccessFile raf;
    
    public DataBase() throws FileNotFoundException, IOException{
        raf = new RandomAccessFile(new File("dbcod.txt"),"rw");
        if(raf.length() > 0){
            raf.seek(raf.length()-4);
            this.primary_key = (short)(raf.readInt()+1);
        }else if(raf.length() == 0){
            this.primary_key = 0;
        }// End if        
        this.connect = null;
    }// End DataBase()
    
    public void connection() throws ClassNotFoundException{
        System.out.println("\n######################################");
        System.out.println("\nTELA DE CONECÇÃO AO ORACLE DATABASE");
        System.out.println("\nDigite seu Usuário");
        String user = scanner.nextLine();
        //System.out.println("Digite a senha");
        //String key = scanner.nextLine();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",user,"V!n!=programmer++");
        }catch(SQLException e){
            System.out.println("Erro: Conecção não realizada! Verifique se o login e a senha estão corretos!\n" + e.getMessage());
        }// End catch     
        System.out.println("\n######################################");
    }// End connection()
     
    /**
     * 
     * @param pessoa
     * @param resultado 
     */
    public void insert(Pessoa pessoa,Resultado resultado) throws IOException{
        String sql_1 = "INSERT INTO USUARIO(ID_,NOME_USUARIO,EMAIL_USUARIO) VALUES(?,?,?)";
        String sql = "INSERT INTO RESULTADO(ID_RESPOSTA,ID_USUARIO,VALOR1_SOMA1,VALOR2_SOMA1,RESULTADO_SOMA1,"
                  + "RESPOSTA_SOMA1,VALOR1_SOMA2,VALOR2_SOMA2,RESULTADO_SOMA2,RESPOSTA_SOMA2,VALOR1_SOMA3,VALOR2_SOMA3,"
                  + "RESULTADO_SOMA3,RESPOSTA_SOMA3) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{      
            PreparedStatement stmt = this.connect.prepareStatement(sql);
            PreparedStatement st = this.connect.prepareStatement(sql_1);
           
            st.setString(1,Integer.toString(this.primary_key));
            st.setString(2,pessoa.getNome());
            st.setString(3,pessoa.getEmail());         
            st.execute();
            st.close();  
                    
            stmt.setString(1,Integer.toString(this.primary_key));
            stmt.setString(2,Integer.toString(this.primary_key));
            stmt.setString(3,Integer.toString(resultado.getValor1_soma1()));
            stmt.setString(4,Integer.toString(resultado.getValor2_soma1()));
            stmt.setString(5,Integer.toString(resultado.getResultado_soma1()));
            stmt.setString(6,Integer.toString(resultado.getResposta_soma1()));
            stmt.setString(7,Integer.toString(resultado.getValor1_soma2()));
            stmt.setString(8,Integer.toString(resultado.getValor2_soma2()));
            stmt.setString(9,Integer.toString(resultado.getResultado_soma2()));
            stmt.setString(10,Integer.toString(resultado.getResposta_soma2()));
            stmt.setString(11,Integer.toString(resultado.getValor1_soma3()));
            stmt.setString(12,Integer.toString(resultado.getValor2_soma3()));
            stmt.setString(13,Integer.toString(resultado.getResultado_soma3()));
            stmt.setString(14,Integer.toString(resultado.getResposta_soma3()));
            stmt.execute();
            stmt.close();      
            raf.writeInt(this.primary_key);
            this.primary_key++;
        }catch(SQLException u){      
            throw new RuntimeException(u);      
        }// End catch      
    }// End insert()
}// End DataBase
