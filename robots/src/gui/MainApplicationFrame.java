package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final Logger logger;
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame(Logger logger) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        this.logger = logger;
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                doExit();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(logger);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription("Выход из программы");
        {
            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");

            JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_E);
            exitItem.addActionListener((event) -> doExit());
            exitMenu.add(exitItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
        return menuBar;
    }

    private void doExit() {
        var result = JOptionPane.showConfirmDialog(this, "Точно выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
