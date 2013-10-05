import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class MainMenu extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton button = new JButton("Play");
	
	MainMenu() {
		this.add(button);
		button.addActionListener(new ButtonListener());
	}
	
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if	(e.getActionCommand().equals("Play")) {
				add(new SnakePanel());
			}
		}
		
	}
}
