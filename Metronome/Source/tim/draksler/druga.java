package tim.draksler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class druga extends ListActivity    {
	public Metronome metronom;
	
	public static final int DRUGA_ACTIVITY_ID = 2;
	Button next;
	TickPlayer tp;
	String naslov;
	SharedPreferences prefs;
	
	 public ArrayList<HashMap<String,String>> list = 
	    	new ArrayList<HashMap<String,String>>(); 
	 
	    String a,b,h;
		
		
	Cursor c;
	Metronome m;
	static String name="Ni izbrana pesem" ;
	private static final String KLJUCNI_TEMPO = "METRONOME_TEMPO";
	private static final String KLJUCNA_PERIODA = "METRONOME_PERIOD";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		 setContentView(R.layout.main2);
	        
		 SimpleAdapter adapter = new SimpleAdapter(
	        		this,
	        		list,
	        		R.layout.custom_row_view,
	        		new String[] {"Ime","Tempo","Takt"},
	        		new int[] {R.id.text1,R.id.text2,R.id.text3});
	        list.clear();//poèistimo podatke da se ne ponavljajo
	        povezi();
	        setListAdapter(adapter); 
	}



	public void povezi()
	{
		 list.clear();//poèistimo podatke
		HashMap<String,String> temp = new HashMap<String,String>();
        Connection conn = null;
        try
		{
			 String userName = "rain";
             String password = "vaje2012";
             String url = "jdbc:mysql://studsrv.uni-mb.si/e1046883";
             	
			Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection (url,userName,password);
            System.out.println ("Database connection established");
        }
        catch (Exception e1)
        {
        	 e1.printStackTrace();
            System.err.println ("Cannot connect to database server");
        }
        
        try
        {
        	 Statement s = conn.createStatement ();
        	 s.executeQuery ("SELECT * FROM projekt");
        	
        	 ResultSet rs = s.getResultSet ();
        	 rs.first();
        	
        	while(rs.next())
        	{
        		 
        		b=rs.getString("Ime");
        	     a=rs.getString("Tempo");
        	     h=rs.getString("Takt");
        	    temp = new HashMap<String,String>();
       		 temp.put("Tempo",a);
       		 temp.put("Ime",b);
       		 temp.put("Takt",h);
        		 
        		   list.add(temp);
        		
        		   
        	}
        	rs.beforeFirst();
        	   
        	   s.close ();
        	   System.out.println ("Uspeï¿½no shranil ");
        	
        }catch(Exception c)
        {
        	/* ï¿½e nebi delalo :D*/
        	 System.err.println ("select ne dela");
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close ();
                    System.out.println ("Database connection terminated");
                }
                catch (Exception e) { /* ignore close errors */ }
            }  
        }
     
	}
	
	

	public void dodaj(View v)
	{
		Intent myIntent = new Intent(this.getApplicationContext(), dodajPesem.class);
		startActivityForResult(myIntent, 0);

	}

	public void preklici(View v)
	{
		//        Intent myIntent = new Intent(this.getApplicationContext(), Metronome.class);
		//        startActivityForResult(myIntent, 0);
		povezi();
		finish();
	}


	@Override
	protected void onResume() {
		
		super.onResume();
		povezi();
	}



	@SuppressWarnings("unused")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		final String PREFS = "metronome.prefs";

		
		// TODO Auto-generated method stub






		// pass the id to NewActivity. You can retrieve this information in
		// the Activity's onCreate() method with something like:
		// long id = getIntent().getLongExtra(SampleActivity.ID_TAG, -1);

		

		SharedPreferences settings = getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();

		//editor.putInt(KLJUCNA_PERIODA, Integer.parseInt(values.get(position).getTakt()));//tu notr nastaviï¿½ takt
		editor.putInt(KLJUCNI_TEMPO, Integer.parseInt(list.get(position).get("Tempo")));//tu nastavi tempo
		editor.putInt(KLJUCNA_PERIODA, Integer.parseInt(list.get(position).get("Takt")));//tu nastavi tempo
		
		
		name=list.get(position).get("Ime");//nastavljamo ime pesmi
		editor.commit();
		
	
		finish();
		
	
		
		
	}

}