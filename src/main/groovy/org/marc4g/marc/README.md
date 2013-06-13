# marc4g - MARC record handling for Groovy/Java

A clean and simple interface to 
create, store and edit MARC data. This is intended to modernize marc4j and make 
available many of the original marc4j features in a more powerful Groovy context.


### Reading records ###
The {@link org.marc4g.marc.Record}&nbsp;interface provides access to the 
    leader and fields for each record.

The following example retrieves all control fields (tags 001 through 009):
```
fields = record.getControlFields()
```
This method retuns the fields as a {@link java.util.List}, thus enabling the use of the standard Groovy collections 
framework to iterate over collections of records, fields or subfields. 
The following code snippet prints the tag and data for each control field to standard output:
```groovy
fields.each {
  println "Tag: " + it.tag + " Data: " + it.data
}
```

The `getDataFields()` method returns all data fields (tags 010 through 999). 
An {@link org.marc4g.marc.DataField}&nbsp;provides access to the tag, the indicators and the subfields.
For example to write all the data field information to standard output:
```groovy
fields = record.getDataFields()
fields.each { field ->
    println "Tag: " + field.tag + " ind1: " + field.ind1 + " ind2: " + field.ind2
    field.subfields.each { subfield ->
        println "Subfield code: " + subfield.code + "Data: " + subfield.data
    }
}
```

If you want to retrieve specific fields you can use one of the following methods:
```groovy
// get the first field occurence for a given tag
title = record.getField("245")
title = record.fields.find{ it.tag == "245"}

// get all occurences for a particular tag
subjects = record.getFields("650")
subjects = record.fields.grep{ it.tag == "650"}

// get all occurences for a given list of tags
tags = ["010", "100", "245", "250", "260", "300"]
fields = record.fields.grep{ it.tag in tags }
```
In addition you can use simple searches using the <code>find()</code> methods 
to retrieve fields that meet certain criteria. The search capabilities are very 
limited, but they can be useful when processing records. The following code snippet 
provides some examples:
```groovy
// find any field containing 'Chabon'
fields = record.find("Chabon")

// find 'Summerland' in a title field
fields = record.find("245", "Summerland")

// find 'Graham, Paul' in main or added entries for a personal name:
fields = record.find(["100", "600"], "Graham, Paul")  
```
The find method is also useful if you want to retrieve records that meet 
certain criteria, such as a specific control number, title words or a particular publisher 
or subject. The example below checks if the cataloging agency is DLC. The example also shows 
how you can extend the find capailities to specific subfields, a feature not directly available 
in MARC4J, since it is easy to accomplish using the record model together with the standard Java API's.
```groovy
reader = new MarcStreamReader(new FileInputStream("file.mrc"))
while (reader.hasNext()) {
  record = reader.next()
  
  // check if the cataloging agency is DLC
  result = record.find("040", "DLC")
  if (result.size) println "Agency for this record is DLC"
  
  // there is no specific find for a specific subfield
  // so to check if it is the orignal cataloging agency
  field = result.get(0)
  agency = field.getSubfield('a').data
  if (agency.matches("DLC")) println "DLC is the original agency"
}
```
The power of Groovy closures largely obviates the need for this package to maintain custom find() methods.

### Creating or updating records
You can also create or update records using the {@link org.marc4g.marc.MarcFactory}. For example:
```groovy
// create a factory instance
factory = MarcFactory.newInstance()

// create a record with leader
record = factory.newRecord("00000cam a2200000 a 4500")

// add a control field
record.addVariableField(factory.newControlField("001", "12883376"))

// add a data field
df = factory.newDataField("245", '1', '0')
df.addSubfield(factory.newSubfield('a', "Summerland /"))
df.addSubfield(factory.newSubfield('c', "Michael Chabon."))
record.addField(df)
```
You can use a {@link org.marc4g.MarcWriter} implementation to serialize your records for example to MARC or MARC XML.
The code snippet below writes a single record in MARC format to standard output:
```groovy
writer = new MarcStreamWriter(System.out)
writer.write(record)
writer.close()
```

## Authors
Based on much original marc4j code by Bas Peters.
Groovy by Joe Atzberger.

