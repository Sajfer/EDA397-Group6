package com.EDA397.Navigator.NaviGitator.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;
import com.EDA397.Navigator.NaviGitator.Activities.RepositoryActivity;
import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuattroX on 2014-04-10.
 * Fragment for displaying commits to the chosen repo/branch.
 */
public class CommitsFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private GitFunctionality git;
    private List<RepositoryCommit> repoCommits;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_commits, container, false);
        git = GitFunctionality.getInstance();
        TextView tv = (TextView) view.findViewById(R.id.commit_label);
        if(git.getCurrentBranch() != null){
            String [] temp = git.getCurrentBranch().getName().split("/");
            tv.setText("Currrent Branch: " +  temp[temp.length-1]);
        }
        else{
            tv.setText("Currrent Branch: master");
        }
        repoCommits = git.getRepoCommits();
        ArrayList<String> commitMsg = new ArrayList<String>();

        for(RepositoryCommit repComm: repoCommits){
            String title = repComm.getCommit().getMessage();
            if(title.length() > 40){
                title = title.substring(0,36) + "...";
            }
            String temp = "Author: " + repComm.getCommit().getAuthor().getName();
            if(repComm.getCommit().getCommentCount() > 0){
                temp += " CM: " + repComm.getCommit().getCommentCount();
            }
            commitMsg.add(temp + "\nDate: " + repComm.getCommit().getAuthor().getDate().toString() +
                    "\n" + title);
        }
        listView = (ListView) view.findViewById(R.id.repoCommit_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        //Consider making a custom adapter for commits (if we want to extend the ListItems to hold multiple things).
        listView.setAdapter(new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, commitMsg.toArray()));

        return view;
    }
    /**
     * Navigates to comments tab after a commit has been selected.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        git = GitFunctionality.getInstance();
        git.setCurrentCommit(repoCommits.get(position));
        RepositoryActivity r = (RepositoryActivity) getActivity();
        r.viewPager.setCurrentItem(3);

    }
}


