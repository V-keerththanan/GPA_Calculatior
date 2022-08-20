import java.util.Scanner;

public class Main {
    public static Scanner sc = new Scanner(System.in);
    static DBOperations objDBO = new DBOperations();

    public static void main(String args[]) {

        if (!objDBO.isRegister()) {
            registration();
        }
        boolean isRunning = true;
            while (isRunning) {
                showOptions();
                System.out.print("\nEnter your choice: ");
                int option = sc.nextInt();
                switch (option) {
                    case 1:
                        viewResult();

                        break;
                    case 2:
                        addResult();
                        break;
                    case 3:
                        export();
                        break;
                    case 4:
                        clearResults();
                        break;
                    case 5:
                        isRunning = false;
                        System.out.println("\nHave a nice day!");
                        break;
                    default:
                        System.out.println("Invalid option");
                }
            }



    }


    public static void registration() {
        User user = new User();
        System.out.println("--GPA Calculator--");
        System.out.print("Enter your name: ");
        user.setUsername(sc.nextLine());
        System.out.print("Course duration(in years): ");
        user.setDuration(sc.nextInt());
        objDBO.addUser(user);

    }

    public static void showOptions() {
        System.out.println("\n---GPACalculator---");
        System.out.println("\nHello "   + objDBO.getUser().getUsername()+" welcome back!");
        System.out.println("\n--Menu--");
        System.out.println("1. View results\n" +
                "2. Add results\n" +
                "3. Export data\n" +
                "4. Clear data\n" +
                "5. Exit");
    }

    public static void addResult(){
        Course course=new Course();
        System.out.println("----Add Result----");
        System.out.println("Year between 1 to "+objDBO.getUser().getDuration());
        course.setYear(sc.nextInt());
        System.out.println("Course code: ");
        course.setCourseCode(sc.next());
        System.out.println("Grade: ");
        course.setCourseGrade(sc.next());
        System.out.println("Credits: ");
        course.setCourseCredit(sc.nextInt());

        System.out.println("\nConfirm (y/n): ");
        String confirm=sc.next();
        System.out.println(confirm);
        if(confirm.equals("y")){
            objDBO.addResult(course);
        }else{
            System.out.println("Record was not added!");
        }

    }

    public static void viewResult(){

        GPA objGpa=new GPA();
        objGpa.CalculationGpa();
        System.out.println("---View Result---");
        System.out.println("Current GPA: "+objGpa.getTotalGpa());

        System.out.println("No. of subjects: "+objGpa.tsumsub);
        System.out.println("Total credits: "+objGpa.tcredit);

        System.out.println("To view records:");
        System.out.println("1. Year 1\n" +
                "2. Year 2\n" +
                "3. Year 3\n" +
                "4. Year 4\n\n" +
                "0. Back\n\n");
        System.out.println("Enter your choice: ");
        int option=sc.nextInt();
        if(option==0){
            showOptions();
        }else {
            objGpa.getYearstatistic(option);
            System.out.println("\n\nTo delete records press d");
            System.out.println("To go back press 0");
            System.out.println("Enter your choice: ");
            String option2= sc.next();
            if(option2.equals("d")){
                System.out.println("\nEnter record id to delete record: ");
                int id=sc.nextInt();
                System.out.println("\nConfirm (y/n): ");
                String confirm=sc.next();
                System.out.println(confirm);
                if(confirm.equals("y")){
                    objDBO.delete(id);
                }else{
                    System.out.println("Record was not deleted!");
                }
            }else if(option2.equals("0")){
                viewResult();
            }
        }


    }
    public static void clearResults(){
        System.out.println("---Clear Data---");
        System.out.println("\nClear all data (y/n): ");
        String confirm=sc.next();
        if(confirm.equals('y')){
            objDBO.clearData();
        }else{
            System.out.println("Did not clear data!");
        }
    }
    public static void export(){
        System.out.println("---Export Data---");
        System.out.println("Exporting your data...");
        objDBO.exportData();
        System.out.println("Done");
    }



}