package cn.edu.pku.sei.SnowView.servlet;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import docsearcher.DocDistScorer;
import docsearcher.DocSearchResult;
import docsearcher.DocSearcher;
import graphsearcher.GraphSearcher;
import graphsearcher.SearchResult;
import solr.SolrKeeper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/26.
 */
public class RankServlet extends HttpServlet {
	DocSearcher docSearcher;
	public void init(ServletConfig config) throws ServletException{
		GraphDatabaseService graphDb = Config.getGraphDB();
		GraphSearcher graphSearcher = new GraphSearcher(graphDb);
		SolrKeeper keeper = new SolrKeeper(Config.getSolrUrl());
		docSearcher = new DocSearcher(graphDb, graphSearcher, keeper);
	
	}
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String query = request.getParameter("query");
        List<DocSearchResult> resultList = docSearcher.search(query);
        JSONObject searchResult = new JSONObject();
        JSONArray results = new JSONArray();
        for (DocSearchResult doc : resultList){
        	JSONObject obj = new JSONObject();
        	obj.put("answerId", doc.getId());
        	Pair<String, String> pair = docSearcher.getContent(doc.getId());
        	if (pair.getLeft().length() > 110) obj.put("title", pair.getLeft().substring(0, 100) + "......"); else
        		obj.put("title", pair.getLeft());
        	obj.put("body", pair.getRight());
        	obj.put("finalRank", doc.getNewRank());
        	obj.put("solrRank", doc.getIrRank());
        	obj.put("relevance", 0);
        	results.put(obj);
        }
        searchResult.put("query", query);
        searchResult.put("rankedResults", results);
        searchResult.put("solrResults", new JSONArray());
        
        response.getWriter().print(searchResult.toString());
    }

}