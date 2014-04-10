package com.EDA397.Navigator.NaviGitator.Activities;

import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.CommitComment;
import org.eclipse.egit.github.core.CommitFile;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryCommitCompare;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class containing functionality to communicate with GitHub
 */
public class GitFunctionality {

    private static GitFunctionality instance;

    private static GitHubClient client;
    private static String username;
    private static Repository current;

    private GitFunctionality() {
            client = new GitHubClient();
            username = "";
    }

    /**
     * Function to return the GitHub client
     * @return The GitHub client
     */
    protected GitHubClient getClient(){ return client; }

    /**
     * Function to return the username
     * @return The current username
     */
    protected String getUserName(){ return username; }

    /**
     * Return the current instance of GitFunctionality
     * @return The current instance of GitFunctionality
     */
    public static GitFunctionality getInstance() {
        if (instance == null) {
            instance = new GitFunctionality();
            Log.d("GitFunctionality", "Instance Created");
        }
        Log.d("GitFunctionality", "Instance Returned");
        return instance;
    }
    protected Repository getCurrent(){ return current; }

    /**
     * Function to login to GitHub
     * @param userName The username to be logged in
     * @param password The users password
     * @return the result of the login
     */
    public Boolean gitLogin(String userName, String password) {
        try{
            Log.d("GitFunctionality", "Login");
            username = userName;
            Authenticate task = new Authenticate();
            task.execute(userName, password);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function to get a list of all the repositories connected to the current user.
     * @return A list with the repositories connected to the current user.
     */
    public List<Repository> getRepos() {
        try{
            Log.d("GitFunctionality", "Repos");
            getRepos task = new getRepos();
            task.execute();
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all commits for a repository
     * @param repo The repository to get the commits for
     * @return A list of all commits for the selected repository
     */
    public List<RepositoryCommit> getRepoCommits(Repository repo) {
        try{
            Log.d("GitFunctionality", "RepoCommits");
            current = repo;
            getRepoCommits task = new getRepoCommits();
            task.execute(repo);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getFileNames(RepositoryCommit r) {
        try{
            Log.d("GitFunctionality", "FileNames");
            getFileNames task = new getFileNames();
            task.execute(r);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<String> getCommitComments(RepositoryCommit r) {
        try{
            Log.d("GitFunctionality", "CommitComments");
            getCommitComments task = new getCommitComments();
            task.execute(r);
            return task.get();
        } catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Async task to Authenticate a user against GitHub
     */
    private class Authenticate extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... str) {
            try {
                getClient().setCredentials(str[0], str[1]);
                OAuthService oAuth = new OAuthService(client);
                oAuth.getAuthorizations();
                Log.d("GitFunctionality", "User logged in");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("GitFunctionality", "Login failed");
                return false;
            }
        }
    }

    /**
     * Async task to get all the repositories for the current user
     */
    private class getRepos extends AsyncTask<Void, Void, List<Repository>> {
        @Override
        protected List<Repository> doInBackground(Void... arg0) {
            try {
                Log.d("GitFunctionality", "Repo thread");
                GitFunctionality git = GitFunctionality.getInstance();
                RepositoryService repoService = new RepositoryService(git.getClient());
                try {
                    OrganizationService org = new OrganizationService(git.getClient());
                    //repos user owns/is member of.
                    List<Repository> repos = repoService.getRepositories();
                    //repos owned by organizations this user is a member of.
                    List<User> organisations = org.getOrganizations();

                    for (User orgz : organisations) {
                        repos.addAll(repoService.getOrgRepositories(orgz.getLogin()));
                    }
                    for (Repository repo : repos) {
                        Log.d("GitFunctionality", repo.getName());
                                //repo.getName());
                    }
                    return repos;
                }
                catch (Exception e){
                    //repos user owns/is member of.
                    List<Repository> repos = repoService.getRepositories();

                    for (Repository repo : repos) {
                        Log.d("GitFunctionality", repo.getName());
                    }
                    return repos;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Async task to get all the commits for a selected repository
     */
    private class getRepoCommits extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
        @Override
        protected List<RepositoryCommit> doInBackground(Repository... repo) {
            try {
                Log.d("GitFunctionality", "Commit thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                List<RepositoryCommit> commits = commitService.getCommits(repo[0]);
                for (RepositoryCommit comm : commits) {
                    Log.d("GitFunctionality", comm.getCommit().getAuthor().getName() + " : " + comm.getCommit().getMessage());
                }
                return commits;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            }
        }
    private class getFileNames extends AsyncTask<RepositoryCommit,  Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(RepositoryCommit... r) {
            try {
                Log.d("GitFunctionality", "FileName thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                ArrayList <String> names = new ArrayList<String>();
                for(CommitFile cf : commitService.getCommit(current, r[0].getSha()).getFiles()){
                    names.add(cf.getFilename());
                    Log.d("GitFunctionality", cf.getFilename());
                }
                return names;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    private class getCommitComments extends AsyncTask<RepositoryCommit,  Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(RepositoryCommit... r) {
            try {
                Log.d("GitFunctionality", "CommitComments thread");
                GitFunctionality git = GitFunctionality.getInstance();
                CommitService commitService = new CommitService(git.getClient());

                ArrayList <String> comments = new ArrayList<String>();
                for(CommitComment cc : commitService.getComments(current, r[0].getSha())){
                    comments.add(cc.getUser().getLogin() + ":\n" + cc.getBody());
                    Log.d("GitFunctionality", cc.getUser().getLogin());
                }
                return comments;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    }