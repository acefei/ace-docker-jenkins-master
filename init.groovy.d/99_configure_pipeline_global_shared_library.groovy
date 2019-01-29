#!groovy

// imports
import hudson.scm.SCM
import jenkins.model.Jenkins
import jenkins.plugins.git.GitSCMSource
import org.jenkinsci.plugins.workflow.libs.*
import org.jenkinsci.plugins.workflow.libs.LibraryConfiguration
import org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever

def remote = System.getenv("PIPELINE_SHARED_LIB_REMOTE")
if (remote == null) {
        return
}

def cred_id = System.getenv("PIPELINE_SHARED_LIB_CRED_ID") ?: ""

// parameters
def globalLibrariesParameters = [
    name:           "common-shared-library",
    branch:         "master",
    implicit:       false,
    credentialsId:  cred_id,
    repository:     remote
]

// define global library
def gitSCMSource = new GitSCMSource(globalLibrariesParameters.repository)
gitSCMSource.credentialsId = globalLibrariesParameters.credentialsId 

// define retriever
def sCMSourceRetriever = new SCMSourceRetriever(gitSCMSource)

// define new library configuration
def libraryConfiguration = new LibraryConfiguration(globalLibrariesParameters.name, sCMSourceRetriever)
libraryConfiguration.setDefaultVersion(globalLibrariesParameters.branch)
libraryConfiguration.setImplicit(globalLibrariesParameters.implicit)

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get Jenkins Global Libraries
def globalLibraries = jenkins.getDescriptor("org.jenkinsci.plugins.workflow.libs.GlobalLibraries")

// set new Jenkins Global Library
globalLibraries.get().setLibraries([libraryConfiguration])

// save current Jenkins state to disk
jenkins.save()
