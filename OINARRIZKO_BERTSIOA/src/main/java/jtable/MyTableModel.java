package jtable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
public class MyTableModel extends AbstractTableModel {

	private Vector<Lag> data = new Vector<Lag>();
	private Vector<String> columnNames = new Vector<String>();
	public MyTableModel() {
		kargatu();
	}
	
	public void kargatu() {
		hasieratuZutabeIzenak();
	}
	
	public void insertElement(String txat,int lastMessage, String id) {
		Date date = new Date(lastMessage*1000L);
		//System.out.println(date.toString());
		ZonedDateTime d = ZonedDateTime.ofInstant(date.toInstant(),
                ZoneId.systemDefault());
		String dateFrom = calendar(d);
		data.add(new Lag(txat,dateFrom, id));
	}
	public void tableClear() {
		data=new Vector<Lag>();
	}
	public void deleteElement(int i) {
		if(i<=-1 || i>=data.size()) {
		}
		else {data.remove(i);};
	}
	public boolean containsId(String id) {
		for(int i=0;i<=data.size()-1;i++) {
			if(data.get(i).getBalioa(1).equals(id)) {
				return true;
			}
		}
		return false;
	}
	
	public void hasieratuZutabeIzenak() {
		columnNames.add("Txat");
		columnNames.add("Last Message");
		columnNames.add("ID");
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return data.get(rowIndex).a.get(columnIndex);
		}
	public String getColumnName(int i) {
		return columnNames.get(i);
	}
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    if (data.isEmpty()) {
	        return Object.class;
	    }
	    return getValueAt(0, columnIndex).getClass();
	}
	
	public boolean isCellEditable(int row, int col) { 
	    return true; 
	}
	public void setValueAt(Object value, int row, int col) {
		  data.get(row).insertElementAt(value, col); // save edits some where
		  fireTableCellUpdated(row, col); // informe any object about changes
		}
	
	public static class DateRenderer extends MyTableModel {
	    DateFormat formatter;
	    SimpleDateFormat format = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss z");
	    
	    public DateRenderer() { super(); 
	    format.setTimeZone (TimeZone.getDefault());}

	    public String changeFormat(Object value) {
	        if (formatter==null) {
	            formatter = DateFormat.getDateInstance();
	        }
	        return format.format(value);
	    }
	}
	
	static DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

	static DateTimeFormatter MDY_FORMAT = DateTimeFormatter.ofPattern("M/d/yyyy");

	public String calendar(ZonedDateTime dt) {
	    StringBuilder sb = new StringBuilder();
	    // check difference in days from today, considering just the date (ignoring the hours) 
	    long days = ChronoUnit.DAYS.between(LocalDate.now(), dt.toLocalDate());
	    if (days == 0) { // today
	        sb.append("Today ");
	    } else if (days == 1) { // tomorrow
	        sb.append("Tomorrow ");
	    } else if (days == -1) { // yesterday
	        sb.append("Yesterday ");
	    } else if (days > 0 && days < 7) { // next week
	        sb.append(dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(" ");
	    } else if (days < 0 && days > -7) { // last week
	        sb.append("Last ").append(dt.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH)).append(" ");
	    }

	    if (Math.abs(days) < 7) {  // difference is less than a week, append current time
	        sb.append("at ").append(dt.format(HOUR_FORMAT));
	    } else { // more than a week of difference
	        sb.append(dt.format(MDY_FORMAT));
	    }

	    return sb.toString();
	}
}
