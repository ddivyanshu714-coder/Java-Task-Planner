import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudyPlannerGUI extends JFrame {

    private JTextField titleField, deadlineField;
    private JComboBox<String> priorityBox;
    private DefaultTableModel tableModel;
    private JTable table;

    public StudyPlannerGUI() {
        setTitle("Smart Task Planner");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new GridLayout(4, 2, 8, 8));
        top.setBorder(BorderFactory.createTitledBorder("Add New Task"));
        top.setBackground(new Color(240, 248, 255));

        top.add(new JLabel("  Title:"));
        titleField = new JTextField();
        top.add(titleField);

        top.add(new JLabel("  Deadline (e.g. 2025-06-01):"));
        deadlineField = new JTextField();
        top.add(deadlineField);

        top.add(new JLabel("  Priority:"));
        priorityBox = new JComboBox<>(new String[]{
            "1 - Critical", "2 - High", "3 - Medium", "4 - Low", "5 - Minimal"});
        top.add(priorityBox);

        JButton addBtn = new JButton("Add Task");
        addBtn.setBackground(new Color(70, 130, 180));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        top.add(new JLabel());
        top.add(addBtn);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Deadline", "Priority", "Status"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getColumnModel().getColumn(3).setMaxWidth(70);
        table.getColumnModel().getColumn(4).setMaxWidth(80);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Your Tasks (sorted by priority)"));
        add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));

        JButton doneBtn = new JButton("Mark Done");
        doneBtn.setBackground(new Color(60, 179, 113));
        doneBtn.setForeground(Color.WHITE);
        doneBtn.setFocusPainted(false);

        JButton delBtn = new JButton("Delete Task");
        delBtn.setBackground(new Color(200, 60, 60));
        delBtn.setForeground(Color.WHITE);
        delBtn.setFocusPainted(false);

        bottom.add(doneBtn);
        bottom.add(delBtn);
        add(bottom, BorderLayout.SOUTH);

        loadTasks();

        addBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String deadline = deadlineField.getText().trim();
            int priority = priorityBox.getSelectedIndex() + 1;
            if (title.isEmpty() || deadline.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in Title and Deadline!");
                return;
            }
            DatabaseHelper.insertTask(title, deadline, priority);
            titleField.setText("");
            deadlineField.setText("");
            priorityBox.setSelectedIndex(0);
            loadTasks();
            JOptionPane.showMessageDialog(this, "Task added!");
        });

        doneBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a task first!");
                return;
            }
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String currentStatus = tableModel.getValueAt(row, 4).toString();
            boolean nowDone = !currentStatus.equals("DONE");
            DatabaseHelper.markCompleted(id, nowDone);
            loadTasks();
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a task first!");
                return;
            }
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this task?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseHelper.deleteTask(id);
                loadTasks();
            }
        });
    }

    private void loadTasks() {
        tableModel.setRowCount(0);
        for (String[] row : DatabaseHelper.getAllTasks())
            tableModel.addRow(row);
    }
}