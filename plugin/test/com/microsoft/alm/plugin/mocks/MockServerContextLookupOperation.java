// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.plugin.mocks;

import com.microsoft.alm.plugin.context.ServerContext;
import com.microsoft.alm.plugin.operations.ServerContextLookupOperation;
import com.microsoft.teamfoundation.core.webapi.model.TeamProjectCollectionReference;
import com.microsoft.teamfoundation.sourcecontrol.webapi.model.GitRepository;

import java.util.ArrayList;
import java.util.List;

public class MockServerContextLookupOperation extends ServerContextLookupOperation {
    final List<TeamProjectCollectionReference> collections = new ArrayList<TeamProjectCollectionReference>();
    final List<GitRepository> gitRepositories = new ArrayList<GitRepository>();
    boolean cancelWhenStarted = false;

    public MockServerContextLookupOperation(List<ServerContext> contextList, ContextScope resultScope) {
        super(contextList, resultScope);
        for (ServerContext context : contextList) {
            collections.add(context.getTeamProjectCollectionReference());
        }
    }

    public void addRepository(GitRepository repository) {
        gitRepositories.add(repository);
    }

    public void cancelWhenStarted() {
        cancelWhenStarted = true;
    }

    @Override
    protected void doLookup(final ServerContext context, final List<TeamProjectCollectionReference> collections) {
        for (final TeamProjectCollectionReference teamProjectCollectionReference : collections) {
            final Runnable projectLookupRunnable = new Runnable() {
                @Override
                public void run() {
                    if (cancelWhenStarted) {
                        cancel();
                    }

                    addRepositoryResults(gitRepositories, context, teamProjectCollectionReference);
                }
            };
            lookup.execute(projectLookupRunnable);
        }
    }

    @Override
    protected void doRestCollectionLookup(final ServerContext context) {
        final Runnable projectCollectionsRunnable = new Runnable() {
            @Override
            public void run() {
                if (isCanceled()) {
                    return;
                }
                doLookup(context, collections);

            }
        };
        lookup.execute(projectCollectionsRunnable);
    }

    @Override
    protected void doSoapCollectionLookup(final ServerContext context) {
        final Runnable projectCollectionsRunnable = new Runnable() {
            @Override
            public void run() {
                if (isCanceled()) {
                    return;
                }
                doLookup(context, collections);

            }
        };
        lookup.execute(projectCollectionsRunnable);
    }
}
