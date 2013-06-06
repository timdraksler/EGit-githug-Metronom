package tim.draksler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;
public class dodajPesem extends Activity {
	private EditText ime;
	String ime1,hitrost1,tempo1;
	private EditText hitrost;
	private Spinner takt;

	static int id = 0;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj);
	
		
		
		ime = (EditText) findViewById(R.id.editText_Ime);
		hitrost = (EditText) findViewById(R.id.editText_Hitrost);
		takt = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.takti, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		takt.setAdapter(adapter);

	}
	
	public void shrani(View v) 
	{
		int i=0;
		i=(takt.getSelectedItemPosition())+1;
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
	        	
	        	String s2="INSERT INTO projekt(Ime,Takt,Tempo) VALUES ("+"'"+ime.getText().toString()+"','"+i+"','"+ hitrost.getText().toString()+"')";
	        	 System.out.println("complete: "+s2);
	        	 Statement s = conn.createStatement();
	        	 s.executeUpdate(s2); 
	        	   s.close();
	        	   System.out.println ("Uspeï¿½no vnesel");
	        	   

	        }catch(Exception c)
	        {
	        	/* èe nebi delalo :D*/
	        	 System.err.println ("insert ne dela");
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
	        finish();
		

	}

	public void zapri(View v) {

				finish();

	}
}
