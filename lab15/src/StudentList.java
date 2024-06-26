import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.*;

public class StudentList extends JFrame {
    private static HashMap<String, Student> studentMap = new HashMap<>();
    static JPopupMenu myPopup;

    public static void main(String[] args) {
        StudentList window = new StudentList("Student List");
        window.setVisible(true);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setMinimumSize(window.getSize());
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }


    public StudentList(String s) {
        super(s);
        final DefaultListModel<String> studentListModel = new DefaultListModel<>();
        final JList<String> studentList = new JList<>();
        JScrollPane myScroll = new JScrollPane(studentList);
        studentList.setModel(studentListModel);
        myPopup = new JPopupMenu();

        JMenuItem myItem1 = new JMenuItem("Добавить студента");
        myItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Введите имя студента:");
                if (studentMap.containsKey(name)) {
                    JOptionPane.showMessageDialog(null, "Студент с таким именем уже существует.");
                    return;
                }
                int age;
                try {
                    age = Integer.parseInt(JOptionPane.showInputDialog("Введите возраст студента:"));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Возраст должен быть только числом.");
                    return;
                }
                String address = JOptionPane.showInputDialog("Введите адрес студента:");
                Student student = new Student(name, age, address);
                studentListModel.addElement(name);
                studentMap.put(name, student);
                saveToFile();
            }
        });


        myPopup.add(myItem1);

        JButton deleteButton = new JButton("Удалить студента");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = studentList.getSelectedValue();
                if (name != null) {
                    studentListModel.removeElement(name);
                    studentMap.remove(name);
                    saveToFile();
                }
            }
        });
        add(deleteButton, BorderLayout.SOUTH);
        JButton clearButton = new JButton("Очистить список");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                studentListModel.clear();
                studentMap.clear();
                saveToFile();
            }
        });
        add(clearButton, BorderLayout.NORTH);

        studentList.setComponentPopupMenu(myPopup);
        studentList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                studentList.setSelectedIndex(studentList.locationToIndex(e.getPoint()));
            }
        });

        studentList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String name = studentList.getSelectedValue();
                Student student = studentMap.get(name);
                if (student != null) {
                    JOptionPane.showMessageDialog(null, "Имя: " + student.getName() + "\nВозраст: " + student.getAge() + "\nАдрес: " + student.getAddress());
                }
            }
        });

        add(myScroll, BorderLayout.CENTER);
    }

    public void saveToFile() {
        try {
            FileWriter writer = new FileWriter("s.txt");
            for (Map.Entry<String, Student> entry : studentMap.entrySet()) {
                Student student = entry.getValue();
                writer.write("Имя: " + student.getName() + ", Возраст: " + student.getAge() + ", Адрес: " + student.getAddress() + "\n");
            }
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

class Student implements Serializable {
    private String name;
    private int age;
    private String address;

    public Student(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }
}
