Getting started from source or compiled jars.

# Compiling #

  * Go to directory where you would like to checkout source code
  * Checkout from svn
```
svn checkout http://adm-telephony-server.googlecode.com/svn/trunk/ adm-telephony-server-read-only
```

  * Goto to the checked-out directory (adm-telephony-server-read-only)
  * Run ant (ant 1.8 or above should be installed).
```
ant release
```
  * Release files are in dist directory.


# Installing #

  * Create an install directory
  * Unzip adm-telephony-server\_snaphot.zip (obtained from previous step or downloaded)
  * Copy config.xml.sample to config.xml
  * Copy scripts.xml.sample to scripts.xml
  * Modify config.xml and scripts.xml to meet your requirements.
  * Run (make sure the execute bit is set, chmod +x run.sh)
```
./run.sh
```