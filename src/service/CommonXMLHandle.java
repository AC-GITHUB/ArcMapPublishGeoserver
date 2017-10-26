package service;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CommonXMLHandle
{
    private String cXMLFileName;
    private Document objDOC;
    private boolean bDocIsOpen;
    private Element objActiveRoot;
    private Element objActiveSection;
    private Element objActiveEntry;
    private Element objActiveSubEntry;
    private Attribute objActiveAttribute;
    private Attribute objActiveSubentryAttribute;
    private String cMessage;
    private XmlMode DocMode = XmlMode.forValue(0);

    private enum XmlMode
    {
        xmlDocClosed(0),
        xmlDocOpen(1),
        xmlSectionSelected(2),
        xmlEntrySelected(3),
        xmlRootSelected(4);

        private int intValue;
        private static java.util.HashMap<Integer, XmlMode> mappings;
        private synchronized static java.util.HashMap<Integer, XmlMode> getMappings()
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, XmlMode>();
            }
            return mappings;
        }

        private XmlMode(int value)
        {
            intValue = value;
            XmlMode.getMappings().put(value, this);
        }

        public int getValue()
        {
            return intValue;
        }

        public static XmlMode forValue(int value)
        {
            return getMappings().get(value);
        }
    }

    public CommonXMLHandle()
    {
        bDocIsOpen = false;
        DocMode = XmlMode.xmlDocClosed;
    }

    // Name des XML-Dokuments
    public final String getXMLfilename()
    {
        String returnValue = "";
        returnValue = cXMLFileName;
        return returnValue;
    }
    public final void setXMLfilename(String value)
    {
        cXMLFileName = value;
    }

    // Auftretende Fehlermeldungen
    public final String getErrorMessage()
    {
        String returnValue = "";
        returnValue = cMessage;
        return returnValue;
    }

    // 謋fnen des XML-Dokuments
    public final boolean OpenDoc()
    {
        try
        {
            File file=new File(cXMLFileName);
            SAXReader saxReader=new SAXReader();
            objDOC =saxReader.read(file);
            bDocIsOpen = true;
            DocMode = XmlMode.xmlDocOpen;
        }
        catch (Exception ex)
        {
            cMessage = "Fehler beim 謋fnen des Dokuments (" + ex.getMessage().toString() + ")";
            return false;
        }
        return true;
    }
    // Speichern des XML-Dokuments
    public final boolean SaveDoc()
    {
        try
        {
            OutputFormat format = new OutputFormat("    ",true);
            format.setEncoding("utf-8");//设置编码格式
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(cXMLFileName),format);

            xmlWriter.write(objDOC);
            xmlWriter.close();
        }
        catch (Exception ex)
        {
            cMessage = "Fehler beim Speichern des Dokuments (" + ex.getMessage().toString() + ")";
            return false;
        }
        return true;
    }
    public final boolean FindRoot(String RootName)
    {
        if (bDocIsOpen == false)
        {
            cMessage = "Dokument ist nicht ge鰂fnet!";
            return false;
        }
        int i = 0;

        Element objNodeRoot = null;
        objNodeRoot = objDOC.getRootElement();
//        if (objNodeRoot instanceof XmlDeclaration)
//        {
//            objNodeRoot = objDOC.SelectSingleNode(RootName);
//        }
        Iterator<Element> iterator= objNodeRoot.elementIterator();
        do{
            if (RootName.toUpperCase().equals(objNodeRoot.getName().toUpperCase()))
            {
                objActiveRoot = objNodeRoot;
                DocMode = XmlMode.xmlRootSelected;
                cMessage = "";
                return true;
            }
        } while (iterator.hasNext()&&(objNodeRoot = iterator.next())!=null);
        cMessage = "Root-Knoten nicht vorhanden!";
        return false;

    }
    public final boolean FindSection(String Section)
    {
        if (bDocIsOpen == false)
        {
            cMessage = "Dokument ist nicht ge鰂fnet!";
            return false;
        }
        int i = 0;

        Element objNodeSection = null;
        if (objActiveRoot==null)
        {
            objActiveRoot = objDOC.getRootElement();
        }
        Iterator<Element> iterator= objActiveRoot.elementIterator();
        while (iterator.hasNext())
        {
            objNodeSection = iterator.next();
            if (Section.toUpperCase().equals(objNodeSection.getName().toUpperCase()))
            {
                objActiveSection = objNodeSection;
                DocMode = XmlMode.xmlSectionSelected;
                cMessage = "";
                return true;
            }

        }
        cMessage = "Sektion nicht vorhanden!";
        return false;

    }
    public final boolean FindSectionAttribute(String SectionAttributeName)
    {
        if (!(DocMode == XmlMode.xmlSectionSelected))
        {
            cMessage = "Keine Eintrag ausgew鋒lt!";
            return false;
        }
        int i = 0;

        Attribute objSectionAttribute = null;
        if (objActiveSection.attributes().size() == 0)
        {
            cMessage = "Keine Attribute vorhanden!";
            return false;
        }
        objSectionAttribute = objActiveEntry.attributes().get(0);
        for (Attribute tempLoopVar_objSectionAttribute : objActiveSection.attributes())
        {
            objSectionAttribute = tempLoopVar_objSectionAttribute;
            if (SectionAttributeName.toUpperCase().equals(objSectionAttribute.getName().toUpperCase()))
            {
                objActiveAttribute = objSectionAttribute;
                DocMode = XmlMode.xmlSectionSelected;
                cMessage = "";
                return true;
            }
        }
        cMessage = "Attribut nicht vorhanden!";
        return false;

    }
    public final boolean FindAnySection()
    {
        try
        {
            if (bDocIsOpen == false)
            {
                cMessage = "Dokument ist nicht ge鰂fnet!";
                return false;
            }
            if (objActiveRoot==null)
            {
                objActiveRoot = objDOC.getRootElement();
            }
            if (objActiveRoot.elements().get(0)==null)
            {
                return false;
            }
            else
            {
                objActiveSection = objActiveRoot.elements().get(0);
                return true;
            }

        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler in Funktion FindAnySection";
        }
        return false;
    }

    public final boolean FindEntry(String Entry)
    {
        if (DocMode == XmlMode.xmlDocClosed | DocMode == XmlMode.xmlDocOpen)
        {
            cMessage = "Keine Sektion ausgew鋒lt!";
            return false;
        }
        int i = 0;

        Element objNodeEntry = null;
        Iterator<Element> iterator= objActiveSection.elementIterator();
        while (iterator.hasNext())
        {
            objNodeEntry = iterator.next();
            if (Entry.toUpperCase().equals(objNodeEntry.getName().toUpperCase()))
            {
                objActiveEntry = objNodeEntry;
                DocMode = XmlMode.xmlEntrySelected;
                cMessage = "";
                return true;
            }

        }
        cMessage = "Eintrag nicht vorhanden!";
        return false;

    }
    public final boolean FindAttribute(String AttributeName)
    {
        if (!(DocMode == XmlMode.xmlEntrySelected))
        {
            cMessage = "Keine Eintrag ausgew鋒lt!";
            return false;
        }
        int i = 0;

        Attribute objAttribute = null;
        if (objActiveEntry.attributes().size() == 0)
        {
            cMessage = "Keine Attribute vorhanden!";
            return false;
        }
        objAttribute = objActiveEntry.attributes().get(0);
        for (Attribute tempLoopVar_objAttribute : objActiveEntry.attributes())
        {
            objAttribute = tempLoopVar_objAttribute;
            if (AttributeName.toUpperCase().equals(objAttribute.getName().toUpperCase()))
            {
                objActiveAttribute = objAttribute;
                DocMode = XmlMode.xmlEntrySelected;
                cMessage = "";
                return true;
            }
        }
        cMessage = "Attribut nicht vorhanden!";
        return false;

    }
    public final String getEntryValue()
    {
        try
        {
            cMessage = "";
            return objActiveEntry.getTextTrim();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Wert gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final String getEntryValue(String Entryname)
    {
        try
        {
            cMessage = "";
            FindEntry(Entryname);
            return objActiveEntry.getTextTrim();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Wert gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final String getEntryName()
    {
        try
        {
            cMessage = "";
            return objActiveEntry.getName();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Name gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final String getSectionName()
    {
        try
        {
            cMessage = "";
            return objActiveSection.getName();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Name gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }

    public final String getSectionValue()
    {
        try
        {
            cMessage = "";
            return objActiveSection.getTextTrim();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Name gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }

    public final String getAttributeValue()
    {
        try
        {
            cMessage = "";
            return objActiveAttribute.getValue();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Wert gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final String getAttributeValue(String AttributName)
    {
        try
        {
            cMessage = "";
            FindAttribute(AttributName);
            return objActiveAttribute.getValue();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Wert gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final String getAttributeName()
    {
        try
        {
            cMessage = "";
            return objActiveAttribute.getName();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Kein Name gefunden (" + ex.getMessage().toString() + ")";
            return "";
        }
    }
    public final boolean SetSectionValue(String NewValue)
    {
        try
        {
            cMessage = "";
            objActiveSection.setText(NewValue);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Schreiben (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean SetEntryValue(String NewValue)
    {
        try
        {
            cMessage = "";
            objActiveEntry.setText(NewValue);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Schreiben (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean SetAttributeValue(String NewValue)
    {
        try
        {
            cMessage = "";
            objActiveAttribute.setValue(NewValue);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Schreiben (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }


    public final boolean DeleteSection()
    {
        try
        {
            objActiveRoot.remove(objActiveSection);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim L鰏chen des Entrys (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean DeleteEntry()
    {
        try
        {
            objActiveSection.remove(objActiveEntry);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim L鰏chen des Eintrags (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean DeleteAttribute()
    {
        try
        {
            objActiveEntry.attributes().remove(objActiveAttribute);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim L鰏chen des Attributs (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }


    public final boolean CreateSection(String Section)
    {
        try
        {
            //Dim objRoot As XmlElement = objDOC.FirstChild
            if (objActiveRoot== null)
            {
                objActiveRoot = objDOC.getRootElement();
            }
//            Element objNewSection = DocumentHelper.createElement(Section);
            objActiveSection = objActiveRoot.addElement(Section);;
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen einer neuen Sektion (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean CreateSectionAttribute(String AttributeName)
    {
        try
        {
            Attribute objNewSectionAttribute = null;
//            objNewSectionAttribute = objDOC.CreateAttribute(AttributeName);
//            objActiveSection.Attributes.Append(objNewSectionAttribute);
//            objActiveAttribute = objNewSectionAttribute;
            objActiveAttribute=objActiveSection.addAttribute(AttributeName,"").
                    attribute(AttributeName);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen eines neuen Attributs (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }


    public final boolean CreateEntry(String Entry)
    {
        try
        {
            Element objNewEntry = null;
//            objNewEntry = objDOC.CreateElement(Entry);
//            objActiveSection.AppendChild(objNewEntry);
//            objActiveEntry = objNewEntry;
            objActiveEntry=objActiveSection.addElement(Entry);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen einer neuen Sektion (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }
    public final boolean CreateAttribute(String AttributeName)
    {
        try
        {
            Attribute objNewAttribute = null;
//            objNewAttribute = objDOC.CreateAttribute(AttributeName);
//            objActiveEntry.Attributes.Append(objNewAttribute);
//            objActiveAttribute = objNewAttribute;
            objActiveAttribute=objActiveEntry.addAttribute(AttributeName,"").
                    attribute(AttributeName);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen eines neuen Attributs (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }
    protected void finalize() throws Throwable
    {
        //base.Finalize();
    }
    public final int getEntryCount()
    {
        if (objActiveEntry==null)
        {
            return -1;
        }
        else
        {
            return objActiveEntry.elements().size();
        }
    }

    public final String[] getEntryArray()
    {
        try
        {

            int nNodeCount = objActiveSection.elements().size();
            String[] tmpArray =new String[nNodeCount];

            Element objNodeEntry = null;
            Iterator<Element> iterator= objActiveSection.elementIterator();
            int i = 0;
            while (iterator.hasNext())
            {
                objNodeEntry=iterator.next();
                objActiveEntry = objNodeEntry;
                tmpArray[i]=objNodeEntry.getName();
                i++;
            }
            return (tmpArray);

        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
            return null;
        }

    }
    public final String[] getEntryArray(String EntryName)
    {
        try
        {
            FindEntry(EntryName);
            int nNodeCount = objActiveSection.elements().size();
            String[] tmpArray =new String[nNodeCount];
            Element objNodeEntry = null;
            Iterator<Element> iterator= objActiveSection.elementIterator();
            int i = 0;
            while (iterator.hasNext())
            {
                objNodeEntry=iterator.next();
                objActiveEntry = objNodeEntry;
                tmpArray[i]=objNodeEntry.getName();
                i++;
            }
            return (tmpArray);

        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
            return null;
        }

    }

    public final String[] getInnerTextArray()
    {
        try
        {

            int nNodeCount = objActiveSection.elements().size();
            String[] tmpArray =new String[nNodeCount];
            Element objNodeEntry = null;
            Iterator<Element> iterator= objActiveSection.elementIterator();
            int i = 0;
            while (iterator.hasNext())
            {
                objNodeEntry=iterator.next();
                objActiveEntry = objNodeEntry;
                tmpArray[i]=objNodeEntry.getTextTrim();
                i++;
            }
            return (tmpArray);

        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
            return null;
        }

    }


    public final String[] GetSectionArray()
    {
        try
        {
            Element objNodeRoot = null;
            objNodeRoot = objDOC.getRootElement();
            if (objActiveRoot== null)
            {
                objActiveRoot = objNodeRoot;
            }
            int nNodeCount = objActiveSection.elements().size();
            String[] tmpArray =new String[nNodeCount];
            Element objNodeSection = null;
            Iterator<Element> iterator= objActiveSection.elementIterator();

            int i = 0;
            while (iterator.hasNext())
            {
                objNodeSection = iterator.next();
                objActiveSection = objNodeSection;
                tmpArray[i]=objNodeSection.getName();
                i++;

            }
            return (tmpArray);
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
        }
        return null;
    }

    public final String[] getAttributeNameArray()
    {
        try
        {

            int nAttributeCount = objActiveEntry.attributes().size();
            String[] tmpArray =new String[nAttributeCount];

            Attribute objEntryAttribute = null;

            if (nAttributeCount > 0)
            {
                int i = 0;
                for (Attribute tempLoopVar_objEntryAttribute : objActiveEntry.attributes())
                {
                    objEntryAttribute = tempLoopVar_objEntryAttribute;
                    tmpArray[i]=objEntryAttribute.getName();
                    i++;
                }
            }
            return (tmpArray);

        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
            return null;
        }

    }


    public final String[] getAttributeArray()
    {
        try
        {

            int nAttributeCount = objActiveEntry.attributes().size();
            String[] tmpArray =new String[nAttributeCount];
            Attribute objEntryAttribute = null;

            if (nAttributeCount > 0)
            {
                int i = 0;
                for (Attribute tempLoopVar_objEntryAttribute : objActiveEntry.attributes())
                {
                    objEntryAttribute = tempLoopVar_objEntryAttribute;
                    tmpArray[i]=objEntryAttribute.getValue();
                    i++;
                }
            }
            return (tmpArray);

        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Erstellen des Arrays (" + ex.getMessage().toString() + ")";
            return null;
        }

    }

    public final boolean CreateNewFile(String RootName, boolean OverWrite)
    {
        try
        {
            if (RootName.equals("") || RootName== null)
            {
                cMessage = "Fehler beim Anlegen der XML-Datei (Knotenname muss angegeben werden)";
                return false;

            }
            File file=new File(cXMLFileName);
            if (file.exists())
            {
                // cXMLfilenameTMP = AppDomain.CurrentDomain.BaseDirectory & "\UMGIS_ADR_Control.CFG"
                if (OverWrite == false)
                {
                    cMessage = "Fehler beim Anlegen der XML-Datei (Datei besteht bereits)";
                    return false;
                }
                file.delete();
            }

            // If File.Exists(cXMLfilenameTMP) = False Then
            // XML-datei anlegen

            Element objRoot = DocumentHelper.createElement(RootName);
            Document objXMLdoc =DocumentHelper.createDocument(objRoot);

            objActiveRoot = objRoot;
            OutputFormat format = new OutputFormat("   ",true);
            format.setEncoding("utf-8");//设置编码格式
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(cXMLFileName),format);

            xmlWriter.write(objXMLdoc);
            xmlWriter.close();

            objDOC = objXMLdoc;
            bDocIsOpen = true;
            DocMode = XmlMode.xmlDocOpen;

            return true;

        }
        catch (Exception ex)
        {
            cMessage = "Fehler beim Anlegen der XML-Datei (" + ex.getMessage().toString() + ")";
            return false;
        }
    }
    public final boolean CreateNewFile(String RootName, boolean OverWrite, String XMLVersion, String XMLEncoding, String XMLStandAlone) {
        try {
            if (RootName.equals("") || RootName == null) {
                cMessage = "Fehler beim Anlegen der XML-Datei (Knotenname muss angegeben werden)";
                return false;

            }
            File file = new File(cXMLFileName);
            if (file.exists()) {
                // cXMLfilenameTMP = AppDomain.CurrentDomain.BaseDirectory & "\UMGIS_ADR_Control.CFG"
                if (OverWrite == false) {
                    cMessage = "Fehler beim Anlegen der XML-Datei (Datei besteht bereits)";
                    return false;
                }
                file.delete();
            }


            Element objRoot = DocumentHelper.createElement(RootName);
            Document objXMLdoc = DocumentHelper.createDocument(objRoot);

            objActiveRoot = objRoot;
            OutputFormat format = new OutputFormat("   ", true);
            format.setEncoding("utf-8");//设置编码格式
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(cXMLFileName), format);

            xmlWriter.write(objXMLdoc);
            xmlWriter.close();

            objDOC = objXMLdoc;
            bDocIsOpen = true;
            DocMode = XmlMode.xmlDocOpen;

            return true;

        } catch (Exception ex) {
            cMessage = "Fehler beim Anlegen der XML-Datei (" + ex.getMessage().toString() + ")";
            return false;
        }
    }
    public final boolean SetSectionEntryAttributeActive(String cAttributeValue)
    {
        try
        {
            String[] SectionArray = null;
            String[] EntryArray = null;
            String[] AttributeNameArray = null;
            int i = 0;
            int j = 0;
            int k = 0;
            boolean bSwitch;
            bSwitch = false;

            SectionArray = GetSectionArray();
            for (i = 0; i <= SectionArray.length; i++)
            {
                FindSection(SectionArray[i]);
                EntryArray = getEntryArray();
                for (j = 0; j <= EntryArray.length; j++)
                {
                    FindEntry(EntryArray[j]);
                    AttributeNameArray = getAttributeNameArray();
                    for (k = 0; k <= AttributeNameArray.length; k++)
                    {
                        FindAttribute(AttributeNameArray[k]);
                        if (getAttributeValue().equals(cAttributeValue))
                        {
                            bSwitch = true;
                            return true;
                        }
                    }
                }
            }
            if (bSwitch == false)
            {
                return false;
            }
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler bei der Auswahl der Section, Entry, Attribute(" + ex.getMessage().toString() + ")";
            return false;
        }
        return false;
    }
    public final int getAttributeCount()
    {
        if (objActiveEntry== null)
        {
            return -1;
        }
        else
        {
            return objActiveEntry.attributes().size();
        }
    }

    public final  List<String> GetAllEntryValues()
    {
        List<String> Al = new ArrayList();
        Element oNode = null;
        try
        {
            if (objActiveSection!=null)
            {
                Iterator<Element> iterator= objActiveSection.elementIterator();
                while (iterator.hasNext())
                {
                    oNode=iterator.next();
                    Al.add(oNode.getTextTrim());
                }
            }
            else
            {
                cMessage = "Section wurde nicht aktiv gesetzt";
                return null;
            }
            Al.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            return Al;
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler im CommonXMLHandle/GetAllEntryInnerText";
        }
        return null;
    }

    public final boolean FindSubEntry(String SubEntry)
    {
        if (!(DocMode == XmlMode.xmlEntrySelected))
        {
            cMessage = "Keine Eintrag ausgew鋒lt!";
            return false;
        }
        int i = 0;

        Element objNodeSubEntry = null;
        Iterator<Element> iterator= objActiveSection.elementIterator();
        while (iterator.hasNext())
        {
            objNodeSubEntry =iterator.next();
            if (SubEntry.toUpperCase().equals(objNodeSubEntry.getName().toUpperCase()))
            {
                objActiveSubEntry = objNodeSubEntry;
                DocMode = XmlMode.xmlEntrySelected;
                cMessage = "";
                return true;
            }

        }
        cMessage = "Eintrag nicht vorhanden!";
        return false;

    }

    public final boolean CreateSubEntry(String SubEntry)
    {
        try
        {
            Element objNewSubEntry = null;
            objActiveSubEntry = objActiveEntry.addElement(SubEntry);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen einer neuen Sektion (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }


    public final boolean CreateSubEntryAttribute(String AttributeName)
    {
        try
        {
            Attribute objNewSubEntryAttribute = null;
            objActiveSubentryAttribute= objActiveSubEntry.addAttribute(AttributeName,"").attribute(AttributeName);
//            objNewSubEntryAttribute = objDOC.CreateAttribute();
//            objActiveSubEntry.Attributes.Append(objNewSubEntryAttribute);
//            objActiveSubentryAttribute = objNewSubEntryAttribute;
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Anlegen eines neuen Attributs (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }

    public final boolean FindSubEntryAttribute(String SubEntryAttributeName)
    {
        if (!(DocMode == XmlMode.xmlEntrySelected))
        {
            cMessage = "Keine Eintrag ausgew鋒lt!";
            return false;
        }
        int i = 0;

        Attribute objSubEntryAttribute = null;
        if (objActiveSubEntry.attributes().size() == 0)
        {
            cMessage = "Keine Attribute vorhanden!";
            return false;
        }
        objSubEntryAttribute = objActiveSubEntry.attributes().get(0);
        for (Attribute tempLoopVar_objSubEntryAttribute : objActiveSubEntry.attributes())
        {
            objSubEntryAttribute = tempLoopVar_objSubEntryAttribute;
            if (SubEntryAttributeName.toUpperCase().equals(objSubEntryAttribute.getName().toUpperCase()))
            {
                objActiveSubentryAttribute = objSubEntryAttribute;
                DocMode = XmlMode.xmlEntrySelected;
                cMessage = "";
                return true;
            }
        }
        cMessage = "Attribut nicht vorhanden!";
        return false;

    }

    public final boolean SetSubEntryAttribute(String SubEntryAttributeValue)
    {
        //If Not DocMode = XmlMode.xmlEntrySelected Then
        //    cMessage = ("Kein Subentryattribut ausgew鋒lt!")
        //    Return False
        //End If

        try
        {
            if (objActiveSubentryAttribute!= null)
            {
                objActiveSubentryAttribute.setValue(SubEntryAttributeValue);
                cMessage = "";
                return true;
            }
            else
            {
                cMessage = "Es ist kein aktives Attribut vorhanden";
            }
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler bei 躡ergabe an das Active Attribute";
        }
        return false;
    }

    public final boolean SetSubEntryValue(String NewSubEntryValue)
    {
        try
        {
            cMessage = "";
            objActiveSubEntry.setText(NewSubEntryValue);
            this.SaveDoc();
        }
        catch (RuntimeException ex)
        {
            cMessage = "Fehler beim Schreiben (" + ex.getMessage().toString() + ")";
            return false;
        }
        cMessage = "";
        return true;
    }
    public final String[] GetSubEntryArray()
    {
        if (!(DocMode == XmlMode.xmlEntrySelected))
        {
            cMessage = "Kein Subentryattribut ausgew鋒lt!";
            return null;
        }
        try
        {
            int nNodeCount = objActiveEntry.elements().size();
            String[] tmpArray=new String[nNodeCount];

            Element objNodeSubEntry = null;
            Iterator<Element> iterator= objActiveEntry.elementIterator();
            int i = 0;
            while (iterator.hasNext())
            {
                objNodeSubEntry = iterator.next();
                objActiveSubEntry = objNodeSubEntry;
                tmpArray[i]=objNodeSubEntry.getName();
                i++;

            }
            return (tmpArray);
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler bei 躡ergabe an das Active Attribute";
        }
       return null;
    }
    public final String GetSubentryAttribute()
    {
        try
        {
            if (objActiveSubentryAttribute!=null)
            {
                cMessage = "";
                return objActiveSubentryAttribute.getValue();
            }
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler bei Erhalt des Active Attribute";
        }
        return "";
    }

    public final String GetSubentryValue()
    {
        try
        {
            if (objActiveSubEntry!=null)
            {
                cMessage = "";
                return objActiveSubEntry.getTextTrim();
            }
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler bei Erhalt des Aktiven Eintrags";
        }
        return "";
    }

    public final String GetSubentryName()
    {
        try
        {
            if (objActiveSubEntry!=null)
            {
                cMessage = "";
                return objActiveSubEntry.getName();
            }
        }
        catch (RuntimeException e)
        {
            cMessage = "Fehler bei Erhalt des Aktiven Eintragsnamens";
        }
        return "";
    }
}
