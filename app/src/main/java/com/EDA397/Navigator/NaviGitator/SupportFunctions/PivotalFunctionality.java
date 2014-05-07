package com.EDA397.Navigator.NaviGitator.SupportFunctions;

import android.os.AsyncTask;
import android.util.Log;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalProject;
import com.EDA397.Navigator.NaviGitator.Datatypes.PivotalStory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajfer on 2014-05-01.
 */
public class PivotalFunctionality {

    private static PivotalFunctionality instance;
    private String url = "https://www.pivotaltracker.com/services/v5";
    private String token = "";

    private PivotalFunctionality() {

    }

    /**
     * Return the current instance of PivotalFunctionality
     *
     * @return The current instance of PivotalFunctionality
     */
    public static PivotalFunctionality getInstance() {
        if (instance == null) {
            instance = new PivotalFunctionality();
            Log.d("PivotalFunctionality", "Instance Created");
        }
        Log.d("PivotalFunctionality", "Instance Returned");
        return instance;
    }

    /**
     * Executing the asynctask for pivotal login
     *
     * @param userName
     * @param password
     * @return boolean if user is logged into pivotaltracker
     */
    public Boolean pivotalLogin(String userName, String password) {
        try {
            Log.d("PivotalFunctionality", "Login");
            LoginPivotal task = new LoginPivotal();
            task.execute(userName, password);
            return task.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Executing the asynctask for get pivotaltracker projects
     * @return boolean if getting the pivotaltracker projects are successful
     */
//    public List<PivotalProject> getPivotalProjects() {
//        try{
//            Log.d("PivotalFunctionality", "getprojects");
//            getProjects task = new getProjects();
//            task.execute();
//            return task.get();
//        } catch ( Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * Executing the asynctask for get pivotaltracker stories
     * @return boolean if getting the pivotaltracker stories are successful
     */
//    public Boolean getPivotalStories() {
//        try{
//            Log.d("PivotalFunctionality", "getprojects");
//            getStories task = new getStories();
//            task.execute();
//            return task.get();
//        } catch ( Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    /**
     * Async task to Authenticate a user against Pivotal tracker
     */
    private class LoginPivotal extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... str) {
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet get = new HttpGet(url + "/me");

                ((DefaultHttpClient) httpclient).getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY),
                        new UsernamePasswordCredentials(str[0], str[1]));

                //Response
                HttpResponse resp = httpclient.execute(get);
                Log.d("PivotalFunctionality", "RESPONSE CODE" + resp.getStatusLine().getStatusCode());
                String responseString = new BasicResponseHandler().handleResponse(resp);
                JSONObject jsonObject = new JSONObject(responseString);
                token = jsonObject.getString("api_token");
                if (resp.getStatusLine().getStatusCode() == 200) {
                    Log.d("PivotalFunctionality", "Token: " + token);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("PivotalFunctionality", "Login failed");
                return false;
            }
        }
    }
}

    /**
     * Async task to get the projects from pivotaltracker
     */
//    private class getProjects extends AsyncTask<Void, Void, List<PivotalProject>> {
//
//
//        @Override
//        protected List<PivotalProject> doInBackground(Void... arg0) {
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpGet get = new HttpGet(url + "/projects");
//                get.setHeader("X-TrackerToken", token);
//
//                //Response
//                HttpResponse resp = httpclient.execute(get);
//
//                String responseString = new BasicResponseHandler().handleResponse(resp);
//
//                Log.d("PivotalFunctionality", "RESPONSE FROM GET PROJECTS \n " + responseString);
//                return XMLParser.parseProjects(responseString);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d("PivotalFunctionality", "failed get projects");
//                return null;
//            }
//        }
//    }
//
//    /**
//     * Async task to get the stories from pivotaltracker
//     */
//    private class getStories extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Void... arg0) {
//            InputStream is = null;
//            JSONObject jObj = null;
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpGet get = new HttpGet("https://www.pivotaltracker.com/services/v5/projects/1043912/stories");
//                get.setHeader("X-TrackerToken", token);
//
//                //Response
//                HttpResponse resp = httpclient.execute(get);
//                String responseString = new BasicResponseHandler().handleResponse(resp);
//                JSONObject jsonObject = new JSONObject(responseString);
//                JSONObject myResponse = jsonObject.getJSONObject("MyResponse");
//                JSONArray tsmresponse = (JSONArray) myResponse.get("listTsm");
//
//                ArrayList<String> list = new ArrayList<String>();
//
//                for(int i=0; i<tsmresponse.length(); i++){
//                    list.add(tsmresponse.getJSONObject(i).getString("description"));
//                }                Log.d("PivotalFunctionality", "RESPONSE GET STORIES " + list);
//
//
//                return true;//XMLParser.parseStories(responseString);
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.d("PivotalFunctionality", "failed get stories");
//                return null;
//            }
//
//
//        }
//    }
//
//}
