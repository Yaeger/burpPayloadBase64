package netspi.payloadEncoder.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import burp.ITab;

public class base64Tab extends JTabbedPane implements ITab, ActionListener {

    public JPanel mainPanel, menuPanel;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTabCaption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getUiComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
