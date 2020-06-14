import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;


public class statistic_block {
	public static double Threshold=0.8;
	public static Connection conn;
	public static double[] d1,d2;

	public static void main(String[] args)
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}
        
        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/test?serverTimezone=IST","root","Aa123456");
            System.out.println("SQL connection succeed"); 
            
           double res=safeValue("CompanyX", 2020);
           System.out.println("Safe value for CompanyX is "+res);
     	} catch (SQLException ex) 
     	    {
            System.out.println("SQLException: " + ex.getMessage());
            }
   	}
	
	public static double safeValue(String ID, int year)
	{	
		d1=takeData(ID,year);
		d2=takeData(ID,year-1);
		SpearmansCorrelation sc=new SpearmansCorrelation();
		double res=sc.correlation(d1, d2);
		Arrays.sort(d1);
		
		if (res<Threshold)
		{
			return d1[19]-(std(d1)+std(d2))/2;
		}
		else
		{			
			return d1[19]-std(d1);
		}
	}	
		
		public static double std(double numArray[])
	    {
	        double sum = 0.0, standardDeviation = 0.0;
	        int length = numArray.length;

	        for(double num : numArray) {
	            sum += num;
	        }

	        double mean = sum/length;

	        for(double num: numArray) {
	            standardDeviation += Math.pow(num - mean, 2);
	        }

	        return Math.sqrt(standardDeviation/length);
	    }
			
			
	public static double[] takeData(String ID, int year)
	{
		double d[]=new double[20];
		try 
		{   
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT statistics.feature FROM test.statistics WHERE ID= \""+ ID +"\" AND year="+ year +";");
	 		for (int i=0; i<20; i++)
	 		{	rs.next();
	 		    d[i]=rs.getFloat(1);
	 		};	 			 
			rs.close();			
		} catch (SQLException e) {e.printStackTrace();}
		return d;
	}
	
}		

	
	



