package com.example;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;
import com.example.detector.permissions.PermissionDetector;
import com.example.detector.permissions.LogDetector;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class PermissionIssueRegistry extends IssueRegistry{
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                PermissionDetector.ISSUE,
                LogDetector.ISSUE
        );
    }
}
