import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;

public class javaDatabase {

    private static final int NUMBEROFNOTES = 8 ; // correspond au nombre de note dans une octave
    private static final String BDD = "sae";
    private static final String url = "jdbc:mysql://localhost:3306/" + BDD;
    private static final String user = "admin";
    private static final String passwd = "admin";
    LinkedList<Integer> data ;
    Connection conn = null ;
    public javaDatabase(LinkedList<Integer> data){
        try{
            this.conn = DriverManager.getConnection(url, user, passwd);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.data = new LinkedList<>(data);
    }
    public LinkedList<Integer> filterData(){
        LinkedList<Integer> filteredData = new LinkedList<Integer>();
        for(int i = 1 ; i< this.data.size()-1 ; i++){
            filteredData.add(this.data.get(i));
        }
        return filteredData ;
    }

    public int[] findMaxiAndMini(){
        int maxi = this.filterData().get(0);
        int mini = this.filterData().get(0);
        for(int i = 0 ; i < this.filterData().size() ; i++){
            if(this.filterData().get(i) < mini)mini = this.filterData().get(i); // afin de trouver le mini de la liste des valeurs filtrés ;
            if(this.filterData().get(i) > maxi ) maxi = this.filterData().get(i); // afin de trouver le maxi de la liste des valeurs filtrés ;
        }
        return new int[]{mini, maxi};
    }
    public HashMap<String, Float> scaleData(){

        int mini = findMaxiAndMini()[0]; // valeur mini de l'enregistrement ;
        int maxi = findMaxiAndMini()[1];  // valeur maxi de l'enregistrement ;

        HashMap<String, Float> dict = new HashMap<>(); // dictionnaire qui va contenir les données avec une clef associé ;

        LinkedList<Float> notes = new LinkedList<>() ; // liste stockant les différents échelons des notes ;

        LinkedList<String> nomNotes = new LinkedList<>() ; // liste qui va permettre de pouvoir donnée le nom aux clefs du dictionnaire ;

        float level = (maxi-mini)/8; // on divise la différence par 8 car il y a 8 notes possible pour 1 octave ;

        for(int i = 0 ; i < NUMBEROFNOTES ; i++){ // jusqu'à 8 car 8 notes dans une octave ;
            if(i ==0 )notes.add(mini+level); // correspond a la premiere note, son mini capté + échelon calculé précedement ;
            else notes.add(notes.get(i-1)+level); // note précedente + échelon ;
        }
        for(int i = 0 ; i < NUMBEROFNOTES ; i++){
            String s = "niveau "+String.valueOf(i+1);
            nomNotes.add(s);
        }
        for(int i = 0 ; i < NUMBEROFNOTES ; i++ )dict.put(nomNotes.get(i),notes.get(i));
        return dict;
    }


    public String transformDataIntoString(){
        String convertData = "";

        HashMap<String, Float> dict = scaleData();

        for(int i = 0 ; i < this.filterData().size() ; i++){
            if( dict.get("niveau 1") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 2") ) convertData += "A";
            if (dict.get("niveau 2") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 3")) convertData += "B";
            if( dict.get("niveau 3") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 4") )convertData+= "C";
            if( dict.get("niveau 4") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 5") )convertData+= "D";
            if( dict.get("niveau 5") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 6") )convertData+= "E";
            if( dict.get("niveau 6") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 7") )convertData+= "F";
            if( dict.get("niveau 7") <= this.filterData().get(i) && this.filterData().get(i) <= dict.get("niveau 8") )convertData+= "G";
            if(this.filterData().get(i) >= dict.get("niveau 8") )convertData+="H";
        }
        return convertData;
    }
    public void checkValue(){
        // L'essaie de connexion à votre base de donées
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, passwd);

            System.out.println(this.data);
            System.out.println("Connecté");
                /*
                Statement req = conn.createStatement();

                ResultSet rs = req.executeQuery("SELECT * FROM partitions");
                while (rs.next()){
                    System.out.println(rs.getInt(1));
                    System.out.println("les partitions : " + rs.getString(2));
                }*/
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Erreur");
            System.exit(0);
        };
    }

    public void insertData(String dataChar){
        try{
            Connection conn = DriverManager.getConnection(url, user, passwd);
            Statement req = conn.createStatement() ;
            String sqlReq = "INSERT INTO PARTITIONS(notes,duree) VALUES(?,?)";
            PreparedStatement ps = conn.prepareStatement(sqlReq);
            ps.setString(1,dataChar);
            ps.setInt(2, (int) (this.filterData().size()* 0.5));
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}