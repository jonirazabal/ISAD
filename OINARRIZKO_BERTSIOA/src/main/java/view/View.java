package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.drinkless.tdlib.Client;
import org.drinkless.tdlib.TdApi;
import org.drinkless.tdlib.TdApi.DownloadFile;

import client.TdClient;
import jtable.MyTableModel;
import model.Model;
import system.Control.ViewControl;
import system.ViewEvent;

public class View implements ObserverTrait, ActionListener {

	private static final Logger LOGGER = Logger.getLogger(View.class.getName());

	private final ViewControl mControl;
	private final Model mModel;

	private final JFrame jframe;
	private JTable jtable;
	private final JScrollPane scrollPaneDer;
	private final JScrollPane scrollPane;
	private JPanel tablePanela = new JPanel();
	private JPanel behekoPanela = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static final ConcurrentMap<Integer, JLabel> pictures = new ConcurrentHashMap<Integer, JLabel>();
	
	private StyleContext context = new StyleContext();
	private StyledDocument document = new DefaultStyledDocument(context);
	
	private TextPaneDer textPaneDer = new TextPaneDer(document);;
	private Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);
	

	private MyTableModel tableModel = new MyTableModel();
	
	public void gehitu(String texto, int lastMessage, String id) {
		tableModel.insertElement(texto,lastMessage, id);
		tableModel.fireTableDataChanged();
	}
	public void gehituDer(String title) {
		try {
			System.out.println("Mezua: "+title);
			document.insertString(0, title + "\n", null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			System.out.println("Ops");
		}
		
	}
	
	private View(ViewControl control, Model model) {
		mControl = control;
		mModel = model;
		jframe = new JFrame("Proba");
		jframe.setAlwaysOnTop(true);
		jframe.setLayout(new BoxLayout(jframe.getContentPane(), BoxLayout.Y_AXIS));
		jtable = new JTable(tableModel);
		
		jtable.getSelectionModel().addListSelectionListener((ListSelectionListener) new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            // do some actions here, for example
	            // print first column value from selected row
	        	
	            try {
					document.remove(0, document.getLength());
					//pictures.clear();
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	mControl.lortuTxatarenAzkenMezuak(jtable.getValueAt(jtable.getSelectedRow(),2).toString(), 10);
	        }
	    });
		jtable.setAutoCreateRowSorter(true);	
		
		scrollPane= new JScrollPane(jtable);
		scrollPane.setPreferredSize(new Dimension(350, 500));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		scrollPaneDer = new JScrollPane(textPaneDer);
		scrollPaneDer.setPreferredSize(new Dimension(400, 500));
		scrollPaneDer.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneDer.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JButton ok = new JButton("Ok");
		ok.setActionCommand("ok");
		ok.addActionListener((ActionListener) this);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPane, scrollPaneDer);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(350);
		
		final JButton cancel = new JButton("Cancel");
		
		behekoPanela.add(ok);
		behekoPanela.add(cancel);
		
		JPanel goiPanel = new JPanel();
		goiPanel.add(splitPane);
		jframe.add(goiPanel);
		jframe.add(behekoPanela);

		jframe.setSize(750, 600);
		jframe.setVisible(true);

		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public void init() {
		/**
		 * Setup view on startup after model was initialized.
		 * 
		 */

	}

	public static Optional<View> create(ViewControl control, Model model) {
		View view;
		try {
			view = invokeAndWait(new Callable<View>() {
				@Override
				public View call() throws Exception {
					return new View(control, model);
				}
			});
		} catch (ExecutionException | InterruptedException ex) {
			LOGGER.log(Level.WARNING, "can't start view", ex);
			return Optional.empty();
		}
		control.addObserver(view);
		return Optional.of(view);
	}

	/* static */

	private static <T> T invokeAndWait(Callable<T> callable) throws InterruptedException, ExecutionException {
		FutureTask<T> task = new FutureTask<>(callable);
		SwingUtilities.invokeLater(task);
		// blocking
		return task.get();
	}

	@Override
	public void updateOnEDT(Observable o, Object arg) {
		// manage notifier events
		// statusChange, newMessage, connectionFailure...

		if (arg instanceof ViewEvent.StatusChange) {
			//
		} else if (arg instanceof ViewEvent.NewMessage) {
			String mezua = ((ViewEvent.NewMessage) arg).mezua;
			gehituDer(mezua);
			//
		} else if (arg instanceof ViewEvent.NewPhoto) {
			String path = ((ViewEvent.NewPhoto) arg).path;
			int id = ((ViewEvent.NewPhoto) arg).id;
			gehituArgazkia(id, path);
			//
		}
		else if (arg instanceof ViewEvent.NewChat) {
			long idLong = ((ViewEvent.NewChat) arg).id;
			String id = String.valueOf(idLong);
			String title = ((ViewEvent.NewChat) arg).title;
			int lastMessage = ((ViewEvent.NewChat) arg).ordua;
			if(!tableModel.containsId(id)) {gehitu(title,lastMessage, id);}
			
			
			
		} else {
			LOGGER.warning("unexpected argument: " + arg);
		}
	}

	public void gehituArgazkia(int id, String path) {
		// TODO Auto-generated method stub
		JLabel label;
		path = path.split("/tdlib")[1];
		System.out.println(path);
		Icon icon = new ImageIcon(getClass().getResource(path));
		if(pictures.get(id)==null) {
			System.out.println("Ez dago pictures-en");
			label = new JLabel(icon);
			pictures.put(id, label);
		}
		else {
			System.out.println("pictures-en dago");
			label = pictures.get(id);
		}
		StyleConstants.setComponent(labelStyle, label);
		try {
			document.insertString(0, "Ignored\n", labelStyle);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch (e.getActionCommand()) {
			case "ok":
				System.out.println("OK Button");
				mControl.getChatList();
		}
		
	}
	private static class TextPaneDer extends JTextPane {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TextPaneDer(StyledDocument document) {
            super(document);
            setOpaque(false);

            // this is needed if using Nimbus L&F - see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6687960
        }

        @Override
        protected void paintComponent(Graphics g) {
            // set background green - but can draw image here too
        	ImageIcon img = new ImageIcon(getClass().getResource("/telegram.jpg"
        			+ ""));
        	g.drawImage(img.getImage(),0, 0, this);
//607123000
            // uncomment the following to draw an image
            // Image img = ...;
            // g.drawImage(img, 0, 0, this);


            super.paintComponent(g);
        }
    }
}
