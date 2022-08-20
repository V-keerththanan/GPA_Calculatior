
import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBOperations {
    static DBConnection mycon = new DBConnection();
    public boolean isRegister(){
            String sql="SELECT * FROM userDetails";

        try {
            Statement statement=mycon.getMyConnection().createStatement();
            ResultSet result=statement.executeQuery(sql);
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUser(User user) {

        try {
            String sql = "INSERT INTO userDetails VALUES (?,?)";
            PreparedStatement statement = mycon.getMyConnection().prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setInt(2, user.getDuration());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Congratulations! Registration was completed.");
            }

        } catch (SQLException e) {
            System.out.println("Connection problem Check it!");

        }

    }

    public User getUser(){
        User user=new User();
        ResultSet result = null;
        try {
            result= mycon.getMyConnection().createStatement().executeQuery("SELECT * FROM userDetails");
            while (result.next()) {
                user.setUsername(result.getString(1));
                user.setDuration(result.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public void addResult(Course course){
        try {
            String sql = "INSERT INTO courseDetails(coursecode,coursecredit,coursegrade,courseyear) VALUES (?,?,?,?)";
            PreparedStatement statement = mycon.getMyConnection().prepareStatement(sql);
            statement.setString(1, course.getCourseCode());
            statement.setInt(2, course.getCourseCredit());
            statement.setString(3, course.getCourseGrade());
            statement.setInt(4, course.getYear());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Record was added!");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }


    public void delete(int id){
        try{
            String sql="DELETE FROM courseDetails WHERE id=?";

            PreparedStatement statement=mycon.getMyConnection().prepareStatement(sql);
            statement.setInt(1,id);
            int rowsUpdate = statement.executeUpdate();
            if(rowsUpdate>0){
                System.out.println("A record was deleted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearData(){
        String sql="DELETE FROM courseDetails";
        try {
            Statement statement=mycon.getMyConnection().createStatement();
            int numRow=statement.executeUpdate(sql);
            if(numRow>0){
                System.out.println("Done!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void exportData(){
        BufferedWriter bw;
        String makeStr;
        ResultSet result;


        try{
            FileWriter fileWriter=new FileWriter("text.txt");
            bw=new BufferedWriter(fileWriter);
            String content="CourseCode CourseCredit Coursegrade CourseYear";
            bw.write(content);
            result= mycon.getMyConnection().createStatement().executeQuery("SELECT * FROM courseDetails");

            while (result.next()) {
                String cod=result.getString(2);
                int cred=result.getInt(3);
                String grade=result.getString(4);
                int year=result.getInt(5);
                makeStr=cod+"\t"+cred+"\t"+grade+"\t"+year;
                bw.write(makeStr);

            }

        } catch (Exception e) {
            System.err.format("IOException: %s%n", e);
        }





    }



}

class GPA extends DBOperations {
    double[] maintainMulGpa;
    int[] maintainSumCre;
    int[] maintainSumSub;

    double tcredit=0;
    int tsumsub=0;
    double tmulgpa=0;

    public void CalculationGpa() {
        Map<String, Double> GradePoint = new HashMap<>();
        GradePoint.put("A+",4.0);
        GradePoint.put("A",4.0);
        GradePoint.put("A-",3.7);
        GradePoint.put("B+",3.3);
        GradePoint.put("B",3.0);
        GradePoint.put("B-",2.7);
        GradePoint.put("C+",2.3);
        GradePoint.put("C",2.0);
        GradePoint.put("C-",1.7);
        GradePoint.put("D+",1.3);
        GradePoint.put("D",1.0);
        GradePoint.put("E",0.0);


        int duration = getUser().getDuration();
        this.maintainMulGpa = new double[duration];
        this.maintainSumCre = new int[duration];
        this.maintainSumSub=new int[duration];
        ResultSet result = null;
        try {
            result = mycon.getMyConnection().createStatement().executeQuery("SELECT * FROM courseDetails");
            while (result.next()) {
                String tempGrade = result.getString(3);
                int tempCredit = result.getInt(2);
                Double tempGp = GradePoint.get(tempGrade) * tempCredit;
                System.out.println(maintainMulGpa[0]);
                this.maintainMulGpa[result.getInt(4) - 1] += tempGp;
                this.maintainSumCre[result.getInt(4) - 1] += tempCredit;
                this.maintainSumSub[result.getInt(4) - 1] += 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public double getTotalGpa(){

        for(int i=0;i<getUser().getDuration();i++){
            this.tmulgpa+=maintainMulGpa[i];
            this.tcredit+=maintainSumCre[i];
            this.tsumsub+=maintainSumSub[i];
        }
        return tmulgpa/tcredit;

    }
    public void getYearstatistic(int year){
        double yearGPA=maintainMulGpa[year-1]/maintainSumCre[year-1];
        System.out.println("----Year "+year+"-----");
        try {
            String sql = "SELECT * FROM courseDetails where courseyear=(?)";

            PreparedStatement statement = mycon.getMyConnection().prepareStatement(sql);
            statement.setInt(1,year);
            ResultSet result = statement.executeQuery();
            int count = 0;
            while (result.next()) {
                String Code = result.getString(1);
                String Grade = result.getString(3);
                String Credits = result.getString(2);
                String output = "#%d. Course code- %s \n Grade - %s\nCredits- %s\n\n";
                System.out.println((String.format(output, ++count, Code, Grade, Credits)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("No of Subjects: "+maintainSumSub[year-1]);
        System.out.println("Total credits: "+maintainSumCre[year-1]);
        System.out.println("GPA for year"+year+":"+yearGPA);

    }
}