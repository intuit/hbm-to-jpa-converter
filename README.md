# hbm-to-jpa-converter

### **Context**

Many applications are utilizing the legacy version of Hibernate and relying on the legacy HBM XML files to store their
named queries. However, transitioning to the latest versions of Hibernate (5+) can be challenging, as legacy HBM XML
files have been deprecated. This means they need to be migrated to the JPA Standard ORM to be compatible with the latest
versions of Hibernate.

### **Purpose**

The purpose of hbm-to-jpa-converter tool is to automate the conversion of legacy hbm XML files
to JPA Standard ORM files to eliminate manual and error-prone conversion.

### **How to use this tool?**

Command:

`java -jar yourJarFile.jar srcDir fileEndsWith destDirectory
`

Sample usage:

`java -jar yourJarFile.jar "/tmp/hbm/" "query.hbm.xml" "/tmp/orm/"`

### Args

This tool requires three input parameters to be provided:

* Source Directory - The directory path where the source file is located.
* File Ends With - Specifies the file extension of legacy xml file.
* Destination Directory - The directory path where the converted ORM file will be written to.

### Success metrics

* This tool converts ~300 queries across ~50 HBM files to ORM in ~1s in Payroll Services Platform (PSP).
* This tool has effectively eliminated 10 man-days of manual work in PSP.