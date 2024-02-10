import java.sql.*;
import java.util.Scanner;

public class MyConnection {
    private String userName = "root";
    private String password = "";
    private String dbName = "demo";
    private String host = "localhost";
    private int port = 3306;
    private Connection con = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public void preparedCalisanlariGetir(int id){
        String query = "Select * from calisanlar where id > ? and ad like ?";
        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,id); //soru isareti yerine gecer
            preparedStatement.setString(2,"M%");

            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String email = rs.getString("email");

                System.out.println("Ad : " + ad + " Soyad : " + soyad + " Email : " + email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void CalisanSil(){
        try {
            statement = con.createStatement();
            String query = "Delete from calisanlar where id='7'";
            int value = statement.executeUpdate(query);
            System.out.println(value + " kadar veri etkilendi");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void CalisanGuncelle(){
        try {
            statement = con.createStatement();
            String query = "Update calisanlar Set email = 'st@gmail.com' where id=1";
            statement.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void CalisanEkle(){
        try {
            statement = con.createStatement();
            String ad = "Burak";
            String soyad = "Sahil";
            String email = "bs@gmail.com";
            String query = "Insert INTO calisanlar (ad,soyad,email) VALUES('" + ad + "','" + soyad + "','" + email + "')";

            statement.executeLargeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void CalisanlariGetir(){
        String query = "Select * from calisanlar";
        try {
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()){
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String email = rs.getString("email");

                System.out.println("Id :" + id + " Ad: " + ad + " Soyad: " + soyad + " Email: " + email);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public MyConnection() {
        //"jdbc:mysql://localhost:80/demo"
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        }

        try {
            con = DriverManager.getConnection(url, userName, password);
            System.out.println("Connection successful");
        } catch (SQLException e) {
            System.out.println("Connection failed");
        }
    }
    //querylerin calismasini kontrol etmek icin
    // toplu calistirabilmek icin
    public void commitandrollback(){
        Scanner scanner = new Scanner(System.in);
        try {
            con.setAutoCommit(false); //kontrol bizde
            String query1 = "Delete from calisanlar where id=3";
            String query2 = "Update calisanlar Set email = 'as@gmail.com' where id=2";

            System.out.println("Guncellemeden once");
            CalisanlariGetir();

            Statement statement1 = con.createStatement();
            statement1.executeUpdate(query1);
            statement1.executeUpdate(query2);

            System.out.println("Islemleriniz kaydedilsin mi?(yes/no)");
            String answer = scanner.nextLine();
            if (answer.equals("yes")){
                con.commit();
                CalisanlariGetir();
                System.out.println("Veritabani guncellendi");
            } else {
                con.rollback();
                System.out.println("Veritabani guncellemesi iptal edildi");
                CalisanlariGetir();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        MyConnection connection = new MyConnection();
        /*
        System.out.println("*************************");
        connection.CalisanlariGetir();
        System.out.println("*************************");
        connection.CalisanEkle();
        connection.CalisanlariGetir();
        connection.CalisanGuncelle();
        connection.CalisanlariGetir();
        connection.CalisanSil();
        connection.CalisanlariGetir();
        connection.preparedCalisanlariGetir(3);
         */

        connection.commitandrollback();
    }
}
