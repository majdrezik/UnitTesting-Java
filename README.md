# UnitTesting-Java
Unit testing and constructor dependency injection


HOMEWORK 2


For the given company and a specific year, 20 statistical features are saved in the table statistics. A characteristic called safe value is calculated by method safeValue(String ID, int year), where ID is a company name. The method calculates the value depending of data for two last years. If data for two last years are similar (>Threshold) up to Spearman’s correlation coefficient, safe value is calculated by the following formula:
〖max┬⁡〖(f〗〗^2020)-avg(std(f^2019 ),std(f^2020 ));
otherwise, the safe value is calculated as
〖max┬⁡〖(f〗〗^2020)-std(f^2020 ),
where f^X is data of the year X, avg(A) and std(A) are the average and standard deviation values of the array A, correspondingly.

Unit testing in Java 
	Class statistic_block depends on component SpearmansCorrelation and on database. Refactor the code of the class statistic_block (from the attached project hw2_unit_testing) to isolate these dependencies. Take care that functionality was not changed! 

	Write a complete Junit test suit for the method safeValue() by using constructor dependency injection approach (assume that you have no real implementation of SpearmansCorrelation; test cases have to be independent from the database).
	Present test case results in a table of the following structure:

Test case name	Description	Result (pass/failed)
		



Don't forget test cases description and code notes about performed changes!

Note: The table statistics is saved in the backup file hw.sql. 


Format of submission:
Homework is submitted on Moodle as a ".zip" file called G<#>-<first submitter ID1>_<second submitter ID2>_HW2.zip or G<#>-<submitter ID>.zip if you are alone.  
It has to include full eclipse project but not single files like <>.java or <>.jar. 

Submission date: 14.06.2020 until 8.00 a.m. 
