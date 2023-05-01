import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class NotepadGUI extends JFrame implements ActionListener {
    private JTextArea textArea;
    private File currentFile;

    public NotepadGUI() {
        super("Notepad");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);

        // create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // create file menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(this);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke('N', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(newMenuItem);
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(this);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(openMenuItem);
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(this);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        fileMenu.add(saveMenuItem);
        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(this);
        fileMenu.add(saveAsMenuItem);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(this);
        fileMenu.add(exitMenuItem);

        // create edit menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        JMenuItem cutMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutMenuItem.setText("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke('X', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        editMenu.add(cutMenuItem);
        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyMenuItem.setText("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke('C', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        editMenu.add(copyMenuItem);
        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke('V', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        editMenu.add(pasteMenuItem);
        JMenuItem selectAllMenuItem = new JMenuItem("Select All");
        selectAllMenuItem.addActionListener(this);
        selectAllMenuItem.setAccelerator(KeyStroke.getKeyStroke('A', java.awt.event.InputEvent.CTRL_DOWN_MASK));
        editMenu.add(selectAllMenuItem);

        // create format menu
        JMenu formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);
//        JMenuItem fontMenuItem = new JMenuItem("Font");
//        fontMenuItem.addActionListener(this);
//        formatMenu.add(fontMenuItem);
        JMenuItem fontSizeMenuItem = new JMenuItem("Font Size");
        fontSizeMenuItem.addActionListener(this);
        formatMenu.add(fontSizeMenuItem);
        JMenuItem fontColorMenuItem = new JMenuItem("Font Color");
        fontColorMenuItem.addActionListener(this);
        formatMenu.add(fontColorMenuItem);

        // create text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        new NotepadGUI();
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "New":
                newFile();
                break;
            case "Open":
                openFile();
                break;
            case "Save":
                saveFile();
                break;
            case "Save As":
                saveFileAs();
                break;
            case "Exit":
                exit();
                break;
            case "Select All":
                selectAll();
                break;
            case "Font":
                changeFont();
                break;
            case "Font Size":
                changeFontSize();
                break;
            case "Font Color":
                changeFontColor();
                break;
        }
    }

    private void newFile() {
        textArea.setText("");
        currentFile = null;
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileReader reader = new FileReader(selectedFile);
                 BufferedReader br = new BufferedReader(reader)) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                textArea.setText(sb.toString());
                currentFile = selectedFile;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveFileAs();
        } else {
            try (FileWriter writer = new FileWriter(currentFile)) {
                writer.write(textArea.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(selectedFile)) {
                writer.write(textArea.getText());
                currentFile = selectedFile;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exit() {
        System.exit(0);
    }

    private void selectAll() {
        textArea.selectAll();
    }

    private void changeFont() {
        Font currentFont = textArea.getFont();
//        Font newFont = JFontChooser.showDialog(this, "Choose Font", currentFont);
//        if (newFont != null) {
//            textArea.setFont(newFont);
//        }
    }

    private void changeFontSize() {
        String currentFontSizeStr = Integer.toString(textArea.getFont().getSize());
        String newFontSizeStr = JOptionPane.showInputDialog(this, "Enter font size:", currentFontSizeStr);
        int newFontSize = Integer.parseInt(newFontSizeStr);
        Font currentFont = textArea.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), newFontSize);
        textArea.setFont(newFont);
    }

    private void changeFontColor() {
        Color currentColor = textArea.getForeground();
        Color newColor = JColorChooser.showDialog(this, "Choose Font Color", currentColor);
        if (newColor != null) {
            textArea.setForeground(newColor);
        }
    }
}
