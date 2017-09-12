# livesearch-android-mysql-php-using-valley
Livesearch in Android with MySQL,PHP,Volley

<br/>
<h2>Database Step</h2>
To create database and table run the commands in SQL Query.txt file
<br/>
<h2>Build.gradle</h2>
<p>Add this code to your Build.gradle(Module:app) file

<table border="0" cellpadding="0" cellspacing="0"><caption>build.gradle</caption><tbody><tr><td><div><div><code>dependencies {</code></div><div><code>&nbsp;&nbsp;&nbsp;&nbsp;</code><code>compile </code><code>'com.android.support:recyclerview-v7:23.3.0'</code></div><div><code>&nbsp;&nbsp;&nbsp;&nbsp;</code><code>compile </code><code>'com.android.volley:volley:1.0.0'}</code></div></tbody></table>

<h2>Permissions</h2>
Add internet permission to your AndroidManifest.xml file.
<br/>
<code>&lt;uses-permission android:name="android.permission.INTERNET"/></code>

<h2>AppConfig</h2>

You must change the LIVE_SEARCH static variable in AppConfig.java file.Modify the URL to your computer ip adress.
You can learn your computer ip adres in Command Prompt(cmd.exe). Execute this command : ipconfig
