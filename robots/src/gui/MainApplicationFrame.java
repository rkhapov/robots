package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.lang.reflect.InvocationTargetException;
import javax.swing.*;

import javax.swing.filechooser.FileNameExtensionFilter;
import log.Logger;
import robots.RobotClassLoader;
import robots.RobotFactory;
import robots.RobotRunner;

/**
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

  private final Logger logger;
  private final GameWindow gameWindow;
  private final JDesktopPane desktopPane = new JDesktopPane();
  private final RobotClassLoader robotClassLoader;
  private final RobotFactory robotFactory;


  public MainApplicationFrame(Logger logger) {
    robotClassLoader = new RobotClassLoader();
    robotFactory = new RobotFactory();

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

    gameWindow = new GameWindow();
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

    var programMenu = createJMenu("Программа", KeyEvent.VK_B, "Управление программой");
    var addRobotLogic = createJMenuItem("Загрузить логику", KeyEvent.VK_L, (event) -> chooseFile());
    programMenu.add(addRobotLogic);

    menuBar.add(programMenu);
    menuBar.add(lookAndFeelMenu);
    menuBar.add(testMenu);
    menuBar.add(exitMenu);

    return menuBar;
  }

  private void chooseFile(){
    var chooser = new JFileChooser();
    var filter = new FileNameExtensionFilter("Java compiled class (.class)", "class");
    chooser.setFileFilter(filter);
    var result = chooser.showDialog(null, "Загрузить логику бота");
    if (result == JFileChooser.APPROVE_OPTION){
      var file = chooser.getSelectedFile();
      var path = file.getPath();

      logicLoader(path);
    }
  }

  private void logicLoader(String filePath){
    try {
      var robotClass = robotClassLoader.loadClass(filePath);
      var robotInstance = robotFactory.createRobot(robotClass, 50, 50, 0);

      gameWindow.setRobot(new RobotRunner(robotInstance));
    }
    catch(ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
      JOptionPane.showMessageDialog(
          this,
          String.format("Не удалось загрузить программу робота: %s", e.toString()),
          "Ошибка загрузки робота",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private JMenuItem createJMenuItem(String text, int mnemonic, ActionListener action) {
    var jMenuItem = new JMenuItem(text, mnemonic);
    jMenuItem.addActionListener(action);
    return jMenuItem;
  }

  private JMenu createJMenu(String menuName, int mnemonic, String accessibleDescription) {
    var jmenu = new JMenu(menuName);
    jmenu.setMnemonic(mnemonic);
    jmenu.getAccessibleContext().setAccessibleDescription(
        accessibleDescription);
    return jmenu;
  }

  private void doExit() {
    var result = JOptionPane
        .showConfirmDialog(this, "Точно выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
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
