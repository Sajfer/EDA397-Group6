package com.EDA397.Navigator.NaviGitator.Activities;


/**
 * Created by Mei on 2014-03-31.
 * https://developer.github.com/v3/oauth/
 */

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.SearchRepository;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;


public class GitLogin extends AsyncTask<String, Void, Boolean> {


    GitHubClient client;
    private String username;
    private String password;

    @Override
    protected Boolean doInBackground(String... s) {
        this.username = s[0];
        this.password = s[1];
        try {
            this.client = new GitHubClient();
            this.client.setCredentials(this.username, this.password);
            return this.getRepo();
        }
        catch(Exception e){
            return false;
        }
    }

    public Boolean getRepo() {
        RepositoryService service = new RepositoryService(this.client);
        OrganizationService org = new OrganizationService(this.client);
        try{

            List<Repository> l = service.getRepositories();
            List<User> o = org.getOrganizations();
            for (User orgz : o) {
                l.addAll(service.getOrgRepositories(orgz.getLogin()));
            }
            for (Repository repo : l) {
                Log.v("nav", repo.getName());
            }
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean result){
        Log.v("nav", result.toString());
    }
}

