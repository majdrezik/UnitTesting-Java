package hw2_unit_testing;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import hw2.*;
import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class test1 {
	private static float var2020 = 3;
	private static float var2019 = 1;
	private static double correlation = var2019 + var2020;
	statistic_block block;

	public static class FakeCorrelationManager implements ICorrelationManager {

		@Override
		public double correlation(double[] arg0, double[] arg1) {
			return correlation;
		}
	}

	public static class FakeFetchDataManager implements IDataSourceManager {
		@Override
		public float[] fetchData(String ID, int year) {
			float results[] = new float[20];
			if (ID == null)
				return results; // array of zeros.
			if (year > 2020) // check if year is not valid
				return results;

			if (ID.equals(" "))
				return results;

			if (!ID.equals("CompanyX") && !ID.equals(" "))
				return results;

			if (year % 2 == 0) {
				for (int i = 0; i < 10; i++) {
					results[i] = var2020; // 3
				}
				for (int i = 10; i < 20; i++) {
					results[i] = var2020 + 1; // 4
				}
			} else {

				for (int i = 0; i < 10; i++) {
					results[i] = var2019;
				}
				for (int i = 10; i < 20; i++) {
					results[i] = var2019 + 5; // 6
				}

			}
			return results;
		}

	}

	@BeforeEach
	void setUp() throws Exception {
		block = new statistic_block(new FakeCorrelationManager(), new FakeFetchDataManager());
	}

	@Test
	void test1() {
		/*
		 * year = 2020 d1 = [3..3|4..4] d2 = [1..1|6..6] std(d1) = 0.5 std(d2) = 2.5
		 * correlation = 4 > 0.8 return : d1[19] (=4) - std(d1) = 4 - 0.5 = 3.5
		 */
		this.correlation = 4;
		assertTrue(block.safeValue("CompanyX", 2020) == 3.5);
	}

	@Test
	void test2() {
		/*
		 * year = 2020 d1 = [3..3|4..4] d2 = [1..1|6..6] std(d1) = 0.5 std(d2) = 2.5
		 * correlation = 0.2 < 0.8 return : d1[19] (=4) - (std(d1) + std(d2))/2 = 4 -
		 * 1.5 = 2.5
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue("CompanyX", 2020) == 2.5);
	}

	@Test
	void test3() {
		/*
		 * year = 2019 d1 = [1..1|6..6] d2 = [3..3|4..4] std(d1) = 2.5 std(d2) = 0.5
		 * correlation = 0.2 < 0.8 return : d1[19] (=6) - (std(d1) + std(d2))/2 = 6 -
		 * 1.5 = 4.5
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue("CompanyX", 2019) == 4.5);
	}

	@Test
	void test4() {
		/*
		 * year = 2019 d1 = [1..1|6..6] d2 = [3..3|4..4] std(d1) = 2.5 std(d2) = 0.5
		 * correlation = 4 > 0.8 return : d1[19] (=6) - std(d1) = 6 - 2.5 = 3.5
		 */
		this.correlation = 4;
		assertTrue(block.safeValue("CompanyX", 2019) == 3.5);
	}

	@Test
	void test5() {
		/*
		 * year = 2029 correlation = 4 > 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue("CompanyX", 2029) == 0);
	}

	@Test
	void test6() {
		/*
		 * year = 2029 correlation = 0.2 < 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 =
		 * 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue("CompanyX", 2029) == 0);
	}

	@Test
	void test7() {
		/*
		 * year = 2019 correlation = 0.2 < 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 =
		 * 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue(null, 2019) == 0);
	}

	@Test
	void test8() {
		/*
		 * year = 2019 correlation = 4 > 0.8 return : 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue(null, 2019) == 0);
	}

	@Test
	void test9() {
		/*
		 * year = 2030 correlation = 4 > 0.8 return : d1[19] (=6) - 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue(null, 2030) == 0);
	}

	@Test
	void test10() {
		/*
		 * year = 2030 correlation = 0.2 < 0.8 return : d1[19] (=0) - 0 - 0 = 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue(null, 2030) == 0);
	}

	@Test
	void test11() {
		/*
		 * year = 2030 correlation = 0.2 < 0.8 return : d1[19] (=0) - 0 - 0 = 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue(" ", 2030) == 0);
	}

	@Test
	void test12() {
		/*
		 * year = 2019 correlation = 0.2 < 0.8 return : d1[19] (=0) - 0 - 0 = 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue(" ", 2019) == 0);
	}

	@Test
	void test13() {
		/*
		 * year = 2030 correlation = 4 > 0.8 return : d1[19] (=0) - 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue(" ", 2030) == 0);
	}

	@Test
	void test14() {
		/*
		 * year = 2019 correlation = 4 > 0.8 return : d1[19] (=0) - 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue(" ", 2019) == 0);
	}

	@Test
	void test15() {
		/*
		 * year = 2029 correlation = 0.2 < 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 =
		 * 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue("CompanyY", 2029) == 0);
	}

	@Test
	void test16() {
		/*
		 * year = 2029 correlation = 4 > 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue("CompanyY", 2029) == 0);
	}

	@Test
	void test17() {
		/*
		 * year = 2019 correlation = 4 > 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 = 0
		 */
		this.correlation = 4;
		assertTrue(block.safeValue("CompanyY", 2019) == 0);
	}

	@Test
	void test18() {
		/*
		 * year = 2019 correlation = 4 > 0.8 return : d1[19] (=0) - std(d1) = 0 - 0 = 0
		 */
		this.correlation = 0.2;
		assertTrue(block.safeValue("CompanyY", 2019) == 0);
	}

}
