package hw2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import sun.awt.image.ImageFetchable;

public class statistic_block {

//	public static void main(String[] args) {
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
//		} catch (Exception ex) {
//			/* handle the error */
//		}
//
//		System.out.println("SQL connection succeed");
//		statistic_block blk = new statistic_block(new statistic_block.CorrelationManager(),
//				new statistic_block.FetchDataManager());
//		double res = blk.safeValue("CompanyX", 2020);
//		System.out.println("Safe value for CompanyX is " + res);
//
//	}

	private double Threshold = 0.8;
	private double[] d1, d2;

	private ICorrelationManager manager;

	private IDataSourceManager dsManager;

	public statistic_block(ICorrelationManager manager, IDataSourceManager dsManager) {
		this.manager = manager;
		this.dsManager = dsManager;
	}

	public double safeValue(String ID, int year) {
		d1 = takeData(ID, year);
		d2 = takeData(ID, year - 1);
		double res = this.manager.correlation(d1, d2);
		Arrays.sort(d1);

		if (res < Threshold) {
			return d1[19] - (std(d1) + std(d2)) / 2;
		} else {
			return d1[19] - std(d1);
		}
	}

	public static void main(String[] args) {
		double arr[] = new double[20];
		double arr2[] = new double[20];

		for (int i = 0; i < 10; i++) {
			arr[i] = 3;
			arr2[i] = 1;
		}
		for (int i = 10; i < 20; i++) {
			arr[i] = 4;
			arr2[i] = 6;
		}
		System.out.println(std(arr));
		System.out.println(std(arr2));
	}

	public static double std(double numArray[]) {
		double sum = 0.0, standardDeviation = 0.0;
		int length = numArray.length;

		for (double num : numArray) {
			sum += num;
		}

		double mean = sum / length;

		for (double num : numArray) {
			standardDeviation += Math.pow(num - mean, 2);
		}

		return Math.sqrt(standardDeviation / length);
	}

	public double[] takeData(String ID, int year) {
		double d[] = new double[20];
		float result[] = dsManager.fetchData(ID, year);
		for (int i = 0; i < 20; i++) {
			d[i] = result[i];
		}
		return d;
	}

	public static class CorrelationManager implements ICorrelationManager {
		@Override
		public double correlation(double[] arg0, double[] arg1) {
			SpearmansCorrelation sc = new SpearmansCorrelation();
			return sc.correlation(arg0, arg1);
		}
	}

	public static class FetchDataManager implements IDataSourceManager {
		@Override
		public float[] fetchData(String ID, int year) {
			float results[] = new float[20];
			try {

				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://simaan-saada.com:3306/bedkot_hw2?serverTimezone=IST", "bdekot",
						"simaanMajd123456789");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT statistics.feature FROM bedkot_hw2.statistics WHERE ID= \""
						+ ID + "\" AND year=" + year + ";");
				for (int i = 0; i < 20; i++) {
					rs.next();
					results[i] = rs.getFloat(1);
				}

				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return results;
		}

	}

}
