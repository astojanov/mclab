import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.regex.*;
import java.util.Scanner;
import aspectMatlab.Main;

/* NewAspectMatlabGUI.java requires no other files. */
public class NewAspectMatlabGUI extends JPanel
                      implements ListSelectionListener {
    private JList aspectList;
	private JList aspectWovenList;
	private JList matlabList;
	private JList matlabWovenList;
	private JTextArea preview;
    private DefaultListModel aspectListModel;
	private DefaultListModel aspectWovenListModel;
	private DefaultListModel matlabListModel;
	private DefaultListModel matlabWovenListModel;

    private static final String addString = "Add Aspect";
    private static final String removeString = "Remove Aspect";
	private static final String addMatlabString = "Add Matlab File";
    private static final String removeMatlabString = "Remove Matlab File";
	private static final String viewString = "View";
	private static final int visibleRows = 8;
    private JButton addButton;
	private JButton removeButton;
	private JButton addMatlabButton;
	private JButton removeMatlabButton;
	private JButton viewAspect;
	private JButton viewMatlab;
	private JButton weaveButton;
	private JButton quitButton;
	private String directory;
	private static JFrame frame;

    public NewAspectMatlabGUI(String directory){
        super(new BorderLayout());
		this.directory = directory;
		preview = new JTextArea(visibleRows*2, 50);
		JScrollPane previewScrollPane = new JScrollPane(preview);
		
		aspectListModel = new DefaultListModel();
		matlabListModel = new DefaultListModel();
		
		//Build list of all files in current folder
		File dir = new File(directory);
		for(File file : dir.listFiles()){
			if(file.getName().matches("(.*).m")){
				try{
					Scanner scan = new Scanner(file);
					String line = scan.nextLine();
					while(line.matches("%(.*)")){
						line = scan.nextLine();
					}
					if(line.matches("aspect(.*)")){
						System.out.println(file.getName() + " is Aspect");
						aspectListModel.addElement(file.getName());
					}else{
						System.out.println(file.getName() + " is Matlab");
						matlabListModel.addElement(file.getName());
					}
				}
				catch(java.io.FileNotFoundException e){}
			}
		}
        //Create list of aspects and put it in scroll pane
        aspectList = new JList(aspectListModel);
        aspectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aspectList.setSelectedIndex(0);
        aspectList.addListSelectionListener(this);
        aspectList.setVisibleRowCount(visibleRows);
        JScrollPane aspectListScrollPane = new JScrollPane(aspectList);
		
		//Create list of aspects to be woven and put it in scroll pane
		aspectWovenListModel = new DefaultListModel();
		aspectWovenList = new JList(aspectWovenListModel);
        aspectWovenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        aspectWovenList.setSelectedIndex(0);
        aspectWovenList.addListSelectionListener(this);
        aspectWovenList.setVisibleRowCount(visibleRows);
        JScrollPane aspectWovenListScrollPane = new JScrollPane(aspectWovenList);
		
		//Create list of matlab files and put it in scroll pane

        matlabList = new JList(matlabListModel);
        matlabList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matlabList.setSelectedIndex(0);
        matlabList.addListSelectionListener(this);
        matlabList.setVisibleRowCount(visibleRows);
        JScrollPane matlabListScrollPane = new JScrollPane(matlabList);
		
		
		//Create list of matlabs to be woven and put it in scroll pane
		matlabWovenListModel = new DefaultListModel();
		matlabWovenList = new JList(matlabWovenListModel);
        matlabWovenList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        matlabWovenList.setSelectedIndex(0);
        matlabWovenList.addListSelectionListener(this);
        matlabWovenList.setVisibleRowCount(visibleRows);
        JScrollPane matlabWovenListScrollPane = new JScrollPane(matlabWovenList);
		
		
		
		//Set up buttons
        addButton = new JButton(addString);
        addButton.setActionCommand(addString);
        addButton.addActionListener(new AddListener());
		
		viewAspect = new JButton(viewString);
		viewAspect.setActionCommand("viewAspect");
		viewAspect.addActionListener(new AuxiliaryListener());
		
		removeButton = new JButton(removeString);
		removeButton.setActionCommand(removeString);
		removeButton.addActionListener(new RemoveListener());
		removeButton.setEnabled(false);
		
		viewMatlab = new JButton(viewString);
		viewMatlab.setActionCommand("viewMatlab");
		viewMatlab.addActionListener(new AuxiliaryListener());
		
		addMatlabButton = new JButton(addMatlabString);
        addMatlabButton.setActionCommand(addMatlabString);
        addMatlabButton.addActionListener(new AddMatlabListener());
		
		removeMatlabButton = new JButton(removeMatlabString);
		removeMatlabButton.setActionCommand(removeMatlabString);
		removeMatlabButton.addActionListener(new RemoveMatlabListener());
		removeMatlabButton.setEnabled(false);
		
		weaveButton = new JButton("Weave");
        weaveButton.setActionCommand("weave");
		weaveButton.addActionListener(new AuxiliaryListener());
        		
		quitButton = new JButton("Exit");
        quitButton.setActionCommand("exit");  
		quitButton.addActionListener(new AuxiliaryListener());
		
		
		addButton.setMaximumSize(removeMatlabButton.getMaximumSize());
		removeButton.setMaximumSize(removeMatlabButton.getMaximumSize());
		addMatlabButton.setMaximumSize(removeMatlabButton.getMaximumSize());
		
		JPanel aspectButtonPane = new JPanel();
		aspectButtonPane.setLayout(new BoxLayout(aspectButtonPane, BoxLayout.X_AXIS));
		aspectButtonPane.add(viewAspect);
		aspectButtonPane.add(addButton);
		
		JPanel matlabButtonPane = new JPanel();
		matlabButtonPane.setLayout(new BoxLayout(matlabButtonPane, BoxLayout.X_AXIS));
		matlabButtonPane.add(viewMatlab);
		matlabButtonPane.add(addMatlabButton);
		
		
        
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel subPanel = new JPanel();
		subPanel.add(weaveButton,BorderLayout.WEST);
		subPanel.add(quitButton,BorderLayout.EAST);
		
		//Create the left side panel, containing the add aspect/matlab buttons.
		JPanel leftPane = new JPanel();
		leftPane.add(Box.createRigidArea(new Dimension(0, 5)));
		leftPane.setLayout(new BoxLayout(leftPane, BoxLayout.Y_AXIS));
		leftPane.add(aspectListScrollPane);
		leftPane.add(Box.createRigidArea(new Dimension(0, 5)));
		addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPane.add(aspectButtonPane);
		leftPane.add(Box.createRigidArea(new Dimension(0, 10)));
		leftPane.add(matlabListScrollPane);
		leftPane.add(Box.createRigidArea(new Dimension(0, 5)));
		addMatlabButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftPane.add(matlabButtonPane);
		leftPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(leftPane, BorderLayout.WEST);
		
		
		//Create the right side panel, containing the add aspect/matlab buttons.
		JPanel rightPane = new JPanel();
		rightPane.setPreferredSize(leftPane.getPreferredSize());
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		rightPane.add(aspectWovenListScrollPane);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPane.add(removeButton);
		rightPane.add(Box.createRigidArea(new Dimension(0, 10)));
		rightPane.add(matlabWovenListScrollPane);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		removeMatlabButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rightPane.add(removeMatlabButton);
		rightPane.add(Box.createRigidArea(new Dimension(0, 5)));
		mainPanel.add(rightPane, BorderLayout.EAST);
		
		mainPanel.add(previewScrollPane,BorderLayout.CENTER);
		add(mainPanel,BorderLayout.NORTH);
		add(subPanel,BorderLayout.SOUTH);
		

    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = aspectList.getSelectedIndex();
			String addName = aspectListModel.getElementAt(index).toString();
            aspectListModel.remove(index);
			
			aspectWovenListModel.addElement(addName);

            int size = aspectListModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                addButton.setEnabled(false);

            } else { //Select an index.
                if (index == aspectListModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                aspectList.setSelectedIndex(index);
                aspectList.ensureIndexIsVisible(index);
            }
        }
    }
	
	class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = aspectWovenList.getSelectedIndex();
			String removeName = aspectWovenListModel.getElementAt(index).toString();
            aspectWovenListModel.remove(index);
			
			aspectListModel.addElement(removeName);

            int size = aspectWovenListModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeButton.setEnabled(false);

            } else { //Select an index.
                if (index == aspectWovenListModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                aspectWovenList.setSelectedIndex(index);
                aspectWovenList.ensureIndexIsVisible(index);
            }
        }
    }
	
	class AddMatlabListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = matlabList.getSelectedIndex();
			String addName = matlabListModel.getElementAt(index).toString();
            matlabListModel.remove(index);
			
			matlabWovenListModel.addElement(addName);

            int size = matlabListModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                addMatlabButton.setEnabled(false);

            } else { //Select an index.
                if (index == matlabListModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                matlabList.setSelectedIndex(index);
                matlabList.ensureIndexIsVisible(index);
            }
        }
    }
	
	class RemoveMatlabListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = matlabWovenList.getSelectedIndex();
			String removeName = matlabWovenListModel.getElementAt(index).toString();
            matlabWovenListModel.remove(index);
			
			matlabListModel.addElement(removeName);

            int size = matlabWovenListModel.getSize();

            if (size == 0) { //Nobody's left, disable firing.
                removeMatlabButton.setEnabled(false);

            } else { //Select an index.
                if (index == matlabWovenListModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                matlabWovenList.setSelectedIndex(index);
                matlabWovenList.ensureIndexIsVisible(index);
            }
        }
    }
	
	//Listener for loading/showing previews when view button is pressed.
	class AuxiliaryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
			String filename = "";
			if(e.getActionCommand().equals("viewAspect")){
				filename = aspectListModel.getElementAt(aspectList.getSelectedIndex()).toString();

			}
			if(e.getActionCommand().equals("viewMatlab")){
				filename = matlabListModel.getElementAt(matlabList.getSelectedIndex()).toString();
			}
			preview.setText("");
			try{
				Scanner scan = new Scanner(new File(directory + "/" + filename));
				while(scan.hasNext()){
					preview.append(scan.nextLine()+"\n");
					}
			}catch(java.io.FileNotFoundException a){}
			
			if(e.getActionCommand().equals("weave")){
				
				String[] args = new String[matlabWovenListModel.getSize() + aspectWovenListModel.getSize()];
				int i = 0;
				for(int j=0;j<matlabWovenListModel.getSize();j++){
					args[i]=matlabWovenListModel.getElementAt(j).toString();
					i++;
				}
				for(int j=0;j<aspectWovenListModel.getSize();j++){
					args[i]=aspectWovenListModel.getElementAt(j).toString();
					i++;
				}
				
				preview.setText("Aspects have been woven.");
				aspectMatlab.Main.main(args);
				
				
				System.out.println("weave button selected");
			}
			if(e.getActionCommand().equals("exit")){
				preview.setText("exit");
				frame.dispose();
			}
			
        }
    }
	
	
	

    //Update buttons upon selection of list items.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
			
            if (aspectList.getSelectedIndex() == -1) {
            //No selection, disable button.
                addButton.setEnabled(false);

            } else {
            //Selection, enable button.
                addButton.setEnabled(true);
            }
			
			if (aspectWovenList.getSelectedIndex() == -1) {
            //No selection, disable button.
                removeButton.setEnabled(false);

            } else {
            //Selection, enable button.
                removeButton.setEnabled(true);
            }
			
			if (matlabList.getSelectedIndex() == -1) {
            //No selection, disable button.
                addMatlabButton.setEnabled(false);

            } else {
            //Selection, enable button.
                addMatlabButton.setEnabled(true);
            }
			
			if (matlabWovenList.getSelectedIndex() == -1) {
            //No selection, disable button.
                removeMatlabButton.setEnabled(false);

            } else {
            //Selection, enable button.
                removeMatlabButton.setEnabled(true);
            }
			
			
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(String directory) {
        //Create and set up the window.
        frame = new JFrame("NewAspectMatlabGUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new NewAspectMatlabGUI(directory);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

	public static void buildGUI(final String directory){
	    //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(directory);
            }
        });
	
	}
	
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI("/home/2008/abodza/Desktop/GUIinvisualbasic");
            }
        });
    }
}