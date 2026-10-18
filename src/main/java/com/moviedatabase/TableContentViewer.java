package com.moviedatabase;
//imports mostly for GUI
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

// creates a GUI 
public final class TableContentViewer extends JFrame {
    private final JTextField hostField = new JTextField("192.168.");
    private final JTextField portField = new JTextField("5432");
    private final JTextField databaseField = new JTextField("mydb");
    private final JTextField usernameField = new JTextField("admin");
    private final JPasswordField passwordField = new JPasswordField();
    private final JTextField tableField = new JTextField("movies");
    private final JButton showButton = new JButton("Show content");

    private final JTable resultTable = new JTable(new DefaultTableModel());

    // constructor for the GUI
    private TableContentViewer() {
        super("Movie Database Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 6));
        addField(fields, "Host", hostField);
        addField(fields, "Port", portField);
        addField(fields, "Database", databaseField);
        addField(fields, "Username", usernameField);
        addField(fields, "Password", passwordField);
        addField(fields, "Table (or schema.table)", tableField);
        fields.add(new JLabel());
        fields.add(showButton);

        add(fields, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        showButton.addActionListener(event -> loadTableFromDatabase());
        setSize(900, 600);
        setLocationRelativeTo(null);
    }
    // adds a field for the gui 
    private static void addField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
    }
    // loads table from the database
    private void loadTableFromDatabase() {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String database = databaseField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String tableName = tableField.getText().trim();

        showButton.setEnabled(false);

        // creates a database connection
        new SwingWorker<DefaultTableModel, Void>() {
            @Override
            protected DefaultTableModel doInBackground() throws Exception {
                DatabaseConnection connection = new DatabaseConnection(host, port, database, username, password);
                return connection.readTable(tableName);
            }

            @Override
            protected void done() {
                try {
                    resultTable.setModel(get());
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException exception) {
                    Throwable cause = exception.getCause();
                    JOptionPane.showMessageDialog(
                            TableContentViewer.this,
                            cause.getMessage() == null ? cause.toString() : cause.getMessage(),
                            "Database error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    showButton.setEnabled(true);
                }
            }
        }.execute();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new TableContentViewer().setVisible(true));
    }
}
