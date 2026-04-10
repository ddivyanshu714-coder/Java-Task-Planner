public class Main {
    public static void main(String[] args) {
        DatabaseHelper.createTable();
        javax.swing.SwingUtilities.invokeLater(() ->
            new StudyPlannerGUI().setVisible(true));
    }
}