package gui;

import model.Box;
import model.BoxListener;
import model.Placement;
import model.Puzzle;
import solvers.DLX;
import solvers.SolverListener;
import textio.PuzzleReader;
import view.PuzzleView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Robert Leenders
 */
public class MainFrame extends JFrame {
    /*
     * List of GUI items needed to draw the frame
     */
    private PuzzleView puzzleView;
    private JTextArea textAreaLog;
    private JCheckBoxMenuItem showPlacementCheckBoxMenuItem;
    private JCheckBoxMenuItem logCheckBoxMenuItem;
    private JCheckBoxMenuItem stopAfterFirstSolutionCheckBoxMenuItem;
    private JScrollPane scrollTextAreaLog;
    private JMenu solveMenu;
    private JMenuItem loadPuzzleMenuItem;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenu editMenu;

    /**
     * Constructs a new MainFrame
     */
    public MainFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
    }

    /**
     * Initializes the form. All the textual representations for the objects are acquired from the properties file.
     */
    private void initComponents() {
        ResourceBundle bundle = ResourceBundle.getBundle("gui.properties_en");
        JMenuBar mainMenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu();
        loadPuzzleMenuItem = new JMenuItem();
        JMenuItem exitMenuItem = new JMenuItem();

        solveMenu = new JMenu();
        JMenuItem automaticallySolveMenuItem = new JMenuItem();
        stopAfterFirstSolutionCheckBoxMenuItem = new JCheckBoxMenuItem();
        showPlacementCheckBoxMenuItem = new JCheckBoxMenuItem();
        logCheckBoxMenuItem = new JCheckBoxMenuItem();

        editMenu = new JMenu();
        editMenu.setEnabled(false);
        undoMenuItem = new JMenuItem();
        redoMenuItem = new JMenuItem();

        textAreaLog = new JTextArea(1, 20);
        scrollTextAreaLog = new JScrollPane(textAreaLog);
        puzzleView = new PuzzleView(this);


        textAreaLog.setEditable(false);
        scrollTextAreaLog.setVisible(false);


        setTitle(bundle.getString("MainFrame.this.title"));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();

        {
            {
                fileMenu.setText(bundle.getString("MainFrame.fileMenu.text"));

                loadPuzzleMenuItem.setText(bundle.getString("MainFrame.loadPuzzleMenuItem.text"));
                loadPuzzleMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        loadPuzzleMenuItemClicked();
                    }
                });
                fileMenu.add(loadPuzzleMenuItem);

                exitMenuItem.setText(bundle.getString("MainFrame.exitMenuItem.text"));
                exitMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                fileMenu.add(exitMenuItem);
            }
            mainMenuBar.add(fileMenu);

            {
                editMenu.setText(bundle.getString("MainFrame.editMenu.text"));

                redoMenuItem.setText(bundle.getString("MainFrame.redoMenuItem.text"));
                redoMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        redoMenuItemClicked();
                    }
                });
                editMenu.add(redoMenuItem);

                undoMenuItem.setText(bundle.getString("MainFrame.undoMenuItem.text"));
                undoMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        undoMenuItemClicked();
                    }
                });
                editMenu.add(undoMenuItem);
            }
            mainMenuBar.add(editMenu);

            {
                solveMenu.setText(bundle.getString("MainFrame.solveMenu.text"));

                automaticallySolveMenuItem.setText(bundle.getString("MainFrame.automaticallySolveMenuItem.text"));
                automaticallySolveMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        automaticallySolveMenuItemClicked();
                    }
                });
                solveMenu.add(automaticallySolveMenuItem);

                stopAfterFirstSolutionCheckBoxMenuItem.setText(bundle.getString("MainFrame.stopAfterFirstSolutionCheckBoxMenuItem.text"));
                solveMenu.add(stopAfterFirstSolutionCheckBoxMenuItem);

                showPlacementCheckBoxMenuItem.setText(bundle.getString("MainFrame.showPlacementCheckBoxMenuItem.text"));
                solveMenu.add(showPlacementCheckBoxMenuItem);

                logCheckBoxMenuItem.setText(bundle.getString("MainFrame.logCheckBoxMenuItem.text"));
                solveMenu.add(logCheckBoxMenuItem);
            }
            solveMenu.setEnabled(false);
            mainMenuBar.add(solveMenu);
        }
        setJMenuBar(mainMenuBar);

        contentPane.add(puzzleView, BorderLayout.LINE_START);
        contentPane.add(scrollTextAreaLog, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setMinimumSize(new Dimension(200, 100));
    }

    /**
     * Loads the puzzle. If an incorrect file is specified a FileNotFound exception is thrown.
     */
    private void loadPuzzleMenuItemClicked() {
        try {
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                puzzleView.setPuzzle(PuzzleReader.read(file));
                solveMenu.setEnabled(true);
                scrollTextAreaLog.setVisible(false);
                pack();
                setLocationRelativeTo(getOwner());
            }
        } catch (FileNotFoundException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("gui.properties", Locale.getDefault());
            JOptionPane.showMessageDialog(null, bundle.getString("MainFrame.fileNotFound.text"), bundle.getString("MainFrame.fileNotFound.header"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action handler for when the menu item redo is clicked. It calls redo on the puzzle, if there is a puzzle loaded.
     */
    private void redoMenuItemClicked() {
        final Puzzle puzzle = puzzleView.getPuzzle();
        if (puzzle != null) {
            puzzle.redo();
        }
        updateMenu();
    }

    /**
     * Action handler for when the menu item undo is clicked. It calls undo on the puzzle, if there is a puzzle loaded.
     */
    private void undoMenuItemClicked() {
        final Puzzle puzzle = puzzleView.getPuzzle();
        if (puzzle != null) {
            puzzle.undo();
        }
        updateMenu();
    }

    /**
     * Enables/disables some menu options based on variables in the puzzle.
     */
    public void updateMenu() {
        final Puzzle puzzle = puzzleView.getPuzzle();
        if (puzzle != null) {
            redoMenuItem.setEnabled(puzzle.redoPossible());
            undoMenuItem.setEnabled(puzzle.undoPossible());
            editMenu.setEnabled(true);
        } else {
            editMenu.setEnabled(false);
        }
    }

    /**
     * Spawns a thread(solver) which solves the puzzle.
     */
    private void automaticallySolveMenuItemClicked() {
        loadPuzzleMenuItem.setEnabled(false);
        solveMenu.setEnabled(false);
        puzzleView.setEnabled(false);

        final Puzzle oldPuzzle = puzzleView.getPuzzle();
        final DLX dlx = new DLX(new Puzzle(oldPuzzle.getName(), new Box(oldPuzzle.getBox().getRowCount(), oldPuzzle.getBox().getColumnCount(), oldPuzzle.getBox().getBlockedPositions()), oldPuzzle.getBagOfPieces()));
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                dlx.findAll();
                loadPuzzleMenuItem.setEnabled(true);
                solveMenu.setEnabled(true);
                puzzleView.setEnabled(true);
                puzzleView.setPaintPlacements(true);
            }
        });

        puzzleView.setPaintPlacements(showPlacementCheckBoxMenuItem.getState());
        final Puzzle newPuzzle = new Puzzle(oldPuzzle.getName(), new Box(oldPuzzle.getBox().getRowCount(), oldPuzzle.getBox().getColumnCount(), oldPuzzle.getBox().getBlockedPositions()), oldPuzzle.getBagOfPieces());
        puzzleView.setPuzzle(newPuzzle);
        dlx.getPuzzle().addBoxListener(new BoxListener() {
            public void placementRemoved(final Placement placement) {
                SwingUtilities.invokeLater(
                                          new Runnable() {
                                              public void run() {
                                                  newPuzzle.removePlacement(placement);
                                              }
                                          });
            }

            public void placementAdded(final Placement placement) {
                SwingUtilities.invokeLater(
                                          new Runnable() {
                                              public void run() {
                                                  newPuzzle.addPlacement(placement);
                                              }
                                          });
            }
        });


        if (logCheckBoxMenuItem.getState()) {
            textAreaLog.setText("");
            scrollTextAreaLog.setVisible(logCheckBoxMenuItem.getState());
            pack();
            dlx.addListener(new SolverListener() {
                public void solutionFound(final int solutionNumber, final Puzzle puzzle) {
                    textAreaLog.append("Solution #" + solutionNumber);
                    textAreaLog.append("\n" + puzzle.getBox() + "\n\n");
                }
            });
        }

        if (stopAfterFirstSolutionCheckBoxMenuItem.getState()) {
            dlx.addListener(new SolverListener() {
                public void solutionFound(int solutionNumber, Puzzle puzzle) {
                    if (solutionNumber == 1)
                        dlx.stop();
                }
            });
        }

        thread.start();
    }

    /**
     * Main function of this program.
     *
     * @param args
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
