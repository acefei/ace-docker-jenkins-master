import hudson.util.Secret
import java.nio.file.Files
import jenkins.model.Jenkins
import net.sf.json.JSONObject
import org.jenkinsci.plugins.plaincredentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.*

// Read Jenkins Master SSH Key from Jenkins Master HOME
def master_ssh_key
try {
    master_ssh_key = new File(System.getenv()['JENKINS_HOME']+'/.ssh/id_rsa').text
} catch(Exception e) {
    return
}

// parameters
def jenkinsMasterKeyParameters = [
description:  'Jenkins Master SSH Key',
id:           'jenkins-master-key',
userName:     'jenkins',
key:          new BasicSSHUserPrivateKey.DirectEntryPrivateKeySource(master_ssh_key)
]

// get Jenkins instance
Jenkins jenkins = Jenkins.getInstance()

// get credentials domain
def domain = Domain.global()

// get credentials store
def store = jenkins.getExtensionList('com.cloudbees.plugins.credentials.SystemCredentialsProvider')[0].getStore()

// define private key
def privateKey = new BasicSSHUserPrivateKey(
    CredentialsScope.GLOBAL,
    jenkinsMasterKeyParameters.id,
    jenkinsMasterKeyParameters.userName,
    jenkinsMasterKeyParameters.key,
    jenkinsMasterKeyParameters.secret,
    jenkinsMasterKeyParameters.description
)

// add credential to store
store.addCredentials(domain, privateKey)

// save to disk
jenkins.save()

