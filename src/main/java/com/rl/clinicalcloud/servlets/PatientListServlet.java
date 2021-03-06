package com.rl.clinicalcloud.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.rl.common.ServiceContext;
import com.rl.mongodb.MongoDAO;

/**
 * @author siva.chinnasamy
 *
 */
public class PatientListServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();		
		HttpSession session = req.getSession(true);
		
		MongoDAO mongoDAO = (MongoDAO) ServiceContext.getBean("mongoDAO");
		
		// Get the Repository name - Database name
		String repository = (String) session.getAttribute("DB_REPOSITORY");
		// Get the Trial & Site ids
		String trialId = (String) session.getAttribute("TRIAL_ID");
		String siteId = (String) session.getAttribute("SITE_ID");
		
		// Build the Collection Name to get all the documents
		String collectionName = getCollectionNameForPatient(req, res, session);
		
		// Get all Patient information for a Trial Id & Site Id
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("TrialId", trialId);
		criteria.put("SiteId", siteId);
				
		String result[] = mongoDAO.getAllDocuments(repository, collectionName, criteria);
		String jsonResult = getJSONString(result);
		
		out.println(jsonResult);
		out.close();
	}
	
	public String getJSONString (String result[]) {
		String jsonResult = "[";
		String commaFlag = "";
		for (String value : result) {
			jsonResult += commaFlag + value;
			commaFlag = ",";
		}
		jsonResult += "]";
		
		System.out.println("\n\n" +jsonResult + "\n\n");
		
		return jsonResult;
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		MongoDAO mongoDAO = (MongoDAO) ServiceContext.getBean("mongoDB");
		
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		//String result[] = mongoDAO.getAllDocuments("test", "PatientVisitVer2");
		
		
		Gson gson = new Gson();
		
		//out.println(gson.toJson(result));
		out.close();
	}	
	
	public String getCollectionNameForPatient(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
			
			// Get the Trial & Site ids
			String trialId = (String) session.getAttribute("TRIAL_ID");
			String siteId = (String) session.getAttribute("SITE_ID");

			String colNameForPatient = trialId + "_" + siteId + "_" + "Patient";
			return colNameForPatient;
	}
}
