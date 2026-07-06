import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;

public class SubjectTable {

    private static JTable table;
    private static DefaultTableModel model;
    private static boolean modified = false;

    
    private static final String SAVE_FILE = "Subject.dat";

    private static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            int rows = model.getRowCount();
            int cols = model.getColumnCount();
            String[][] data = new String[rows][cols];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    Object v = model.getValueAt(r, c);
                    data[r][c] = v == null ? "" : v.toString();
                }
            }
            out.writeObject(data);
            modified = false;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Save failed: " + ex.getMessage());
        }
    }

    private static void loadData() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            String[][] data = (String[][]) in.readObject();
            for (int r = 0; r < Math.min(data.length, model.getRowCount()); r++) {
                for (int c = 0; c < Math.min(data[r].length, model.getColumnCount()); c++) {
                    model.setValueAt(data[r][c], r, c);
                }
            }
            modified = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame frame = new JFrame("📚 SCHOOL MANAGE($HUBHAM)");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(1400, 750);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().setBackground(new Color(18, 18, 18));

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(18, 18, 18));

            String[] columns = new String[25];
            columns[0] = "SUBJECT";

            for (int i = 1; i < 25; i++) {
                columns[i] = String.valueOf(i);
            }

            Object[][] data = {
                    {"    Geography"},
                    {"    History & Civics"},
                    {"    Computer"},
                    {"    Math"},
                    {"    Hindi Literature"},
                    {"    Hindi Grammar"},
                    {"    T.C"},
                    {"    J.C"},
                    {"    English Grammar"},
                    {"    Biology"},
                    {"    Chemistry"},
                    {"    Physics"}
            };

            model = new DefaultTableModel(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            loadData();

            table = new JTable(model);

            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    int row = table.rowAtPoint(e.getPoint());
                    int col = table.columnAtPoint(e.getPoint());

                    if (row >= 0 && col > 0) {

                        Object value = model.getValueAt(row, col);

                        if ("X".equals(value)) {
                            model.setValueAt("", row, col);
                        } else {
                            model.setValueAt("X", row, col);
                        }

                        modified = true;
                        table.repaint();
                    }
                }
            });

            table.setRowSelectionAllowed(false);
            table.setCellSelectionEnabled(false);
            table.setFocusable(false);

            table.setRowHeight(45);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setGridColor(new Color(55, 55, 55));
            table.setBackground(new Color(28, 28, 28));
            table.setForeground(Color.WHITE);

            table.setSelectionBackground(new Color(0, 180, 120));
            table.setSelectionForeground(Color.WHITE);

            table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

            JTableHeader header = table.getTableHeader();

            header.setFont(new Font("Segoe UI", Font.BOLD, 16));
            header.setBackground(new Color(35, 35, 35));
            header.setForeground(new Color(0, 255, 170));
            header.setPreferredSize(new Dimension(100, 50));

            header.setResizingAllowed(false);
            header.setReorderingAllowed(false);

            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column) {

                    Component c = super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {

                        if (row % 2 == 0) {
                            c.setBackground(new Color(28, 28, 28));
                        } else {
                            c.setBackground(new Color(38, 38, 38));
                        }

                        if (column > 0) {
                            c.setForeground(Color.WHITE);
                        } else {
                            c.setForeground(Color.WHITE);
                        }
                    }

                    setHorizontalAlignment(column == 0 ? LEFT : CENTER);

                    if (column == 0) {

                        c.setFont(new Font("Segoe UI", Font.BOLD, 15));
                        c.setForeground(new Color(0, 255, 170));

                    } else {

                        JLabel lbl = (JLabel) c;

                        lbl.setIcon(null);

                        String text = value == null ? "" : value.toString();

                        lbl.setText(text);

                        if ("X".equals(text)) {
                            lbl.setForeground(new Color(255, 80, 80));
                        } else {
                            lbl.setForeground(Color.WHITE);
                        }

                        lbl.setHorizontalAlignment(SwingConstants.CENTER);
                        lbl.setFont(new Font("Arial", Font.BOLD, 24));
                    }

                    return c;
                }
            };

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }

            for (int i = 0; i < table.getColumnCount(); i++) {

                if (i == 0) {
                    table.getColumnModel().getColumn(i).setPreferredWidth(220);
                } else {
                    table.getColumnModel().getColumn(i).setPreferredWidth(55);
                }
            }

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBackground(new Color(18, 18, 18));
            scrollPane.getViewport().setBackground(new Color(18, 18, 18));
            scrollPane.setBorder(null);

            JPanel footer = new JPanel();
            footer.setBackground(new Color(25, 25, 25));

            JLabel footerText = new JLabel("Made by SHUBHAM");
            footerText.setForeground(new Color(0, 255, 170));
            footerText.setFont(new Font("Segoe UI", Font.BOLD, 14));

            footer.add(footerText);

            mainPanel.add(scrollPane, BorderLayout.CENTER);

            frame.add(mainPanel, BorderLayout.CENTER);
            frame.add(footer, BorderLayout.SOUTH);

            
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    if (!modified) {
                        System.exit(0);
                    }

                    int result = JOptionPane.showConfirmDialog(
                            frame,
                            "Do you want to save changes?",
                            "Subject Tracker",
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        saveData();
                        System.exit(0);
                    } else if (result == JOptionPane.NO_OPTION) {
                        System.exit(0);
                    }
                }
            });


            frame.setVisible(true);
        });
    }
}
