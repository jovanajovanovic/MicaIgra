package com.mica.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CustomTableCellRenderer extends DefaultTableCellRenderer {
	private String odabranaAkcijaStr;
	private String odabranoSelektovanoPoljeStr;
	
	
	public CustomTableCellRenderer(String odabranoSelektovanoPoljeStr, String odabranaAkcijaStr) {
		this.odabranaAkcijaStr = odabranaAkcijaStr;
		this.odabranoSelektovanoPoljeStr = odabranoSelektovanoPoljeStr;
	}



	@Override
	 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	 {	
	     final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	     String akcijaStr = (String) table.getValueAt(row, 1);
	     String selektovanoPoljeStr = (String) table.getValueAt(row, 0);
	     if(selektovanoPoljeStr.equals(odabranoSelektovanoPoljeStr) && akcijaStr.equals(odabranaAkcijaStr)) c.setBackground(new Color(255, 51, 51)); // svetlo crvena boja
	     else c.setBackground(Color.WHITE);
	     
	     return c;
	 }
}
