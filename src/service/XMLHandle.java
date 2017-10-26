package service;

import org.dom4j.*;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMNodeHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

public class XMLHandle {
    private String m_cXMLFilename;
    private XMLState m_enDocMode;
    private Document m_objDoc;
    private Map<String,String> m_objNameDict;
    private Map<String,String> m_objNamespaceDict;
    private Map<String,List<String>> m_objXPathDict;
    private Element m_objRoot;
    private short m_iLevelCount;
    private Node m_objActiveNode;
    private Attribute m_objActiveAttribute;
    private String m_cXMLVersion;
    private String m_cSLDVersion;
    private String m_cXMLEncoding;
    private Store2Fields m_objNamespaceURL;
    private short iLevelCount = 0;
//    private XmlNamespaceManager m_objNSManager;
    private enum XMLState {
        xmlDocClosed(0),
        xmlDocOpen(1);

        private int intValue;
        private static java.util.HashMap<Integer, XMLState> mappings;

        private synchronized static java.util.HashMap<Integer, XMLState> getMappings() {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, XMLState>();
            }
            return mappings;
        }

        private XMLState(int value) {
            intValue = value;
            XMLState.getMappings().put(value, this);
        }

        public int getValue() {
            return intValue;
        }

        public static XMLState forValue(int value) {
            return getMappings().get(value);
        }
    }
    public XMLHandle(String FileName)
    {
        //m_objParent = Parent
        m_cXMLFilename = FileName;
        start();
    }
    public final void start()
    {
        ReadLUT();
        m_enDocMode = XMLState.xmlDocClosed;
        m_objDoc = new DOMDocument();
        m_iLevelCount = (short) 0;
        if (m_cXMLFilename!=null||m_cXMLFilename!="")
        {
            OpenDoc();
        }

    }
    public final void OpenDoc()
    {
        try
        {
            File file=new File(m_cXMLFilename);
            if (m_enDocMode == XMLState.xmlDocClosed)
            {
                if (file.exists())
                {
                    if (m_cXMLFilename!=null||m_cXMLFilename!="")
                    {
                        SAXReader saxReader=new SAXReader();
                        m_objDoc =saxReader.read(file);
                        m_objRoot = m_objDoc.getRootElement();
                        if (m_objRoot==null)
                        {
                            throw (new RuntimeException("Das Dokument ist leer. Bitte w鋒len Sie eine existierende, g黮tige XML-Datei aus"));
                        }
                        m_enDocMode = XMLState.xmlDocOpen;
                    }
                    else
                    {
                        throw (new RuntimeException("Es wurde noch kein Dateiname/Speicherort f黵 das XML-Dokument angegeben"));
                    }
                }
            }
            else
            {
                return;
            }
        }
        catch (Exception ex)
        {
            ErrorMsg("Konnte XML-Dokument nicht 鰂fnen", ex.getMessage(), ex.getStackTrace(), "OpenDoc");
        }
    }
    public final String getXMLFilename()
    {
        return m_cXMLFilename;
    }
    public final void setXMLFilename(String value)
    {
        m_cXMLFilename = value;
    }

    public final Element getGetRoot()
    {
        m_iLevelCount = (short) 0;
        return m_objRoot;
    }

    public final short getLevelNumber()
    {
        return m_iLevelCount;
    }
    public final boolean NavigateElement(String AliasTagName)
    {
        List<String> objXPathColl = null;
        List<Node> objNodelist = null;
        Node objEvalNode = null;
        Node objTempNode = null;
        Node objTempNode2 = null;
        short i = 0;
        short j;
        short iInsurance = 0;
        iInsurance = (short) 0;
        i = (short) 0;
        boolean bSwitch;
        bSwitch = false;

        try
        {
            if (m_objXPathDict.containsKey(AliasTagName))
            {
                objXPathColl = m_objXPathDict.get(AliasTagName);
                objTempNode = m_objActiveNode;
                while (!bSwitch)
                {
                    List<Element> lstElements= objTempNode.getParent().elements();
                    if (lstElements.size() > 1)
                    {
                        int index= lstElements.indexOf(objTempNode);
                        ListIterator<Element> listIterator= lstElements.listIterator(index);
                        objTempNode2 = objTempNode;
                        while (listIterator.hasPrevious())
                        {
                            for (i = 0; i <= objXPathColl.size() - 1; i++)
                            {
                                objNodelist = m_objActiveNode.selectNodes(objXPathColl.get(i));
                                objEvalNode = objNodelist.get(objNodelist.size() - 1);
                                if (objEvalNode.equals(objTempNode))
                                {
                                    m_objActiveNode = objTempNode;
                                    bSwitch = true;
                                    return true;
                                }
                            }
                            objTempNode = listIterator.previous();
                        }
                        objTempNode = objTempNode2;
                    }
                    else
                    {
                        for (i = 0; i <= objXPathColl.size() - 1; i++)
                        {
                            objNodelist = m_objActiveNode.selectNodes(objXPathColl.get(i));
                            objEvalNode = objNodelist.get(objNodelist.size() - 1);
                            if (objEvalNode.equals(objTempNode))
                            {
                                m_objActiveNode = objTempNode;
                                bSwitch = true;
                                return true;
                            }
                        }
                    }
//                    Node node=DocumentHelper.createElement("/");
//                    if (!(objNav.matches(node)))
//                    {
//                        objTempNode = objTempNode.getParent();
//                    }
                    iInsurance++;
                    if (iInsurance > 100)
                    {
                        throw (new RuntimeException("Kein g黮tiger XPathausdruck f黵 '" + AliasTagName + "' gefunden - Sicherheitsabbruch"));
                    }
                }
            }
            else
            {
                throw (new RuntimeException("Die Datei LUT_sld_mapping_file.xml enth鋖t den Tag-Alias '" + AliasTagName + "' nicht."));
            }
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Der XPath-Ausdruck '" + AliasTagName + "' in der LUT-Datei stimmt nicht; oder Navigieren nicht m鰃lich.", ex.getMessage(), ex.getStackTrace(), "NavigateElement");
            return false;
        }
    }
    public final boolean CreateElement(String AliasTagName)
    {
        Element objNode = null;
        String cNamespacePrefix = "";
        String cNamespaceURL = "";
        try
        {
            if (NavigateElement(AliasTagName) == true)
            {
                cNamespacePrefix = GetNamespacePrefix(AliasTagName);
                cNamespaceURL = GetNamespaceURL(cNamespacePrefix);
                Namespace namespace=DocumentHelper.createNamespace(cNamespacePrefix,cNamespaceURL);
                QName qName=DocumentHelper.createQName(GetOGCName(AliasTagName),namespace);
                objNode = DocumentHelper.createElement(qName);
                ((Element)m_objActiveNode).add(objNode);
                m_objActiveNode = objNode;
                return true;
            }
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Konnte Elementknoten nicht erstellen.", ex.getMessage(), ex.getStackTrace(), "CreateElement");
            return false;
        }
        return false;
    }
    public final boolean SetElementText(String InnerText)
    {
        try
        {
            m_objActiveNode.setText(InnerText);
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Konnte Elementtext nicht schreiben.", ex.getMessage(), ex.getStackTrace(), "SetElementText");
            return false;
        }
    }
    public final boolean CreateAttribute(String AttributeName)
    {
        Attribute objXmlAttribute = null;
        try
        {
            DocumentHelper.createAttribute((Element) m_objActiveNode,AttributeName,"");
            m_objActiveAttribute =((Element) m_objActiveNode).attribute(AttributeName); //Das gerade gemachte Attribut wird aktiv gesetzt
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Konnte Attribut nicht erstellen", ex.getMessage(), ex.getStackTrace(), "CreateAttribute");
            return false;
        }
    }
    public final boolean SetAttributeValue(String AttributeValue)
    {
        try
        {
            m_objActiveAttribute.setValue(AttributeValue);
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Konnte Attribut nicht erstellen", ex.getMessage(), ex.getStackTrace(), "SetAttributeValue");
            return false;
        }
    }

    public final boolean ParseDoc(Element CurrentNode)
    {
        Element objNode = null;
        iLevelCount++;

        try
        {
            if (m_enDocMode == XMLState.xmlDocOpen)
            {
                Iterator<Element> iterator= CurrentNode.elementIterator();
                if (CurrentNode.elements().size()>0)
                {
                    while (iterator.hasNext()){
                        if (objNode.elements().size()>0)
                        {
                            if (objNode.elements().get(0) instanceof Element) //Auch InnerText oder Attribute ist ein ChildNode
                            {
                                ParseDoc(objNode); //Rekursion
                            }
                        }
                    }
                }
            }
            else
            {
//                JOptionPane.showConfirmDialog(null, "Das Dokument ist noch nicht ge鰂fnet");
                return false;
            }
            m_iLevelCount = iLevelCount;
            iLevelCount = (short) 0;
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Das Dokument ist unbrauchbar", ex.getMessage(), ex.getStackTrace(), "ParseDoc");
        }
        return false;
    }
    public final boolean CreateNewFile(boolean OverWrite)
    {
        short i = 0;
        short j = 0;
        String cNamePre = "";

        try
        {
            File file=new File(m_cXMLFilename);
            if (file.exists())
            {
                if (OverWrite == false)
                {
                    throw (new RuntimeException("Fehler beim Anlegen der XML-Datei (Datei besteht bereits)"));

                }
                file.delete();
            }
            cNamePre = GetNamespacePrefix("StyledLayerDescriptor");
            Namespace namespace=DocumentHelper.createNamespace(cNamePre,GetNamespaceURL(cNamePre));
            QName qName= DocumentHelper.createQName(GetOGCName("StyledLayerDescriptor"),namespace);
            m_objRoot = DocumentHelper.createElement(qName);
            m_objActiveNode = m_objRoot;

            CreateAttribute("version");
            SetAttributeValue(m_cSLDVersion);
            for (i = 0; i <= m_objNamespaceURL.getCount() - 1; i++)
            {
                CreateAttribute("xmlns" + ":" + m_objNamespaceURL.getString1ByIndex(i));
                SetAttributeValue(String.valueOf(m_objNamespaceURL.getString2ByIndex(i)));
            }
            SaveDoc();
            m_enDocMode = XMLState.xmlDocOpen;
            for (j = 0; j <= m_objNamespaceURL.getCount() - 1; j++)
            {
                m_objRoot.addNamespace(m_objNamespaceURL.getString1ByIndex(j), m_objNamespaceURL.getString2ByIndex(j));
            }
            return true;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Fehler beim Anlegen der XML-Datei (" + ex.getMessage().toString() + ")", ex.getMessage(), ex.getStackTrace(), "CreateNewFile");
            return false;
        }
    }
    private boolean ReadLUT()
    {
        String cFilename = "";
        Document objLUTDoc = null;
        Element objRoot = null;
        Element objNode = null;
        Element objNode2 = null;
        Element objNode3 = null;
        List<String> objXPathExp = null;
        m_objNameDict = new HashMap<>();
        m_objNamespaceDict = new HashMap<>();
        m_objXPathDict = new HashMap<>();
        m_objNamespaceURL = new Store2Fields();
        File file=new File(cFilename);

        try
        {
            cFilename = "resources\\LUT_sld_mapping_file.xml";
            if (file.exists())
            {
                SAXReader saxReader=new SAXReader();
                objLUTDoc =saxReader.read(file);
                objRoot = objLUTDoc.getRootElement();
                objNode = objRoot.elements().get(0);
                while (objNode!=null)
                {
                    //Die Unterelemente des Knotens sldSyntax werden ausgelesen und abgespeichert
                    if (objNode.getName().equals("sldSyntax"))
                    {
                        Iterator<Element> iterator= objNode.elementIterator();
                        while (iterator.hasNext())
                        {
                            objNode2=iterator.next();
                            m_objNameDict.put(objNode2.getName(), objNode2.attribute("ogcTag").getValue());
                            m_objNamespaceDict.put(objNode2.getName(), objNode2.attribute("Namespace").getValue());
                            objXPathExp = new ArrayList<>();
                            Iterator<Element> iterator1= objNode2.elementIterator();
                            while (iterator1.hasNext())
                            {
                                objNode3=iterator1.next();
                                objXPathExp.add(objNode3.getTextTrim());
                            }
                            m_objXPathDict.put(objNode2.getName(), objXPathExp);
                        }
                        //Die Unterelemente des Knotens "sldConfiguration" werden ausgelesen und abgespeichert
                    }
                    else if (objNode.getName().equals("sldConfiguration"))
                    {
                        Iterator<Element> iterator= objNode.elementIterator();
                        while (iterator.hasNext())
                        {
                            objNode2=iterator.next();
                            if (objNode2.getName().equals("xmlVersion"))
                            {
                                m_cXMLVersion = String.valueOf(objNode2.getTextTrim());
                            }
                            else if (objNode2.getName().equals("xmlEncoding"))
                            {
                                m_cXMLEncoding = String.valueOf(objNode2.getTextTrim());
                            }
                            else if (objNode2.getName().equals("Namespaces"))
                            {
                                Iterator<Element> iterator1= objNode2.elementIterator();
                                while (iterator1.hasNext())
                                {
                                    objNode3=iterator1.next();
                                    m_objNamespaceURL.addStrings(objNode3.getName(), objNode3.getTextTrim());
                                }
                            }
                            else if (objNode2.getName().equals("sldVersion"))
                            {
                                m_cSLDVersion = String.valueOf(objNode2.getTextTrim());
                            }
                        }
                    }
                }
            }
            else
            {
                throw new FileNotFoundException();
            }
            return true;
        }
        catch (Exception ex)
        {
            ErrorMsg("Die Datei LUT_sld_mapping_file.xml muss im Anwendungsverzeichnis stehen. Bitte kopieren SIe die Datei an diese Stelle und starten Sie die Anwendung erneut.", ex.getMessage(), ex.getStackTrace(), "ReadLUT");
        }
        return  false;
    }
    private String GetOGCName(String key)
    {
        String cRightTag = "";
        try
        {
            if (m_objNameDict.containsKey(key) == true)
            {
                cRightTag = m_objNameDict.get(key);
            }
            else
            {
                throw (new RuntimeException("Der Tag " + key + " ist noch nicht in die LUT-Datei aufgenommen"));
            }
            return cRightTag;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Fehler beim Beziehen der OGC-Tagnames", ex.getMessage(), ex.getStackTrace(), "GetOGCName");
        }
        return "";
    }
    private String GetNamespacePrefix(String key)
    {
        String cRightTag = "";
        try
        {
            if (m_objNamespaceDict.containsKey(key) == true)
            {
                cRightTag = m_objNamespaceDict.get(key);
            }
            else
            {
                throw (new RuntimeException("Leider ist dieser Namensraumk黵zel " + key + " noch nicht in die LUT aufgenommen"));
            }
            return cRightTag;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Fehler beim Beziehen der Namensraumk黵zel", ex.getMessage(), ex.getStackTrace(), "GetNamespacePrefix");
        }
        return "";
    }
    private String GetNamespaceURL(Object value)
    {
        String cRightTag = "";

        try
        {
            if (m_objNamespaceURL.ContainsString1(value.toString()))
            {
                cRightTag = String.valueOf(m_objNamespaceURL.getString2ForString1(value.toString()));
            }
            else
            {
                throw new RuntimeException("Leider ist dieser URL " + String.valueOf(value) + " noch nicht in die LUT aufgenommen");

            }
            return cRightTag;
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Fehler beim Beziehen der Namensraum-URL", ex.getMessage(), ex.getStackTrace(), "GetNamespaceURL");
        }
        return "";
    }

    private void ErrorMsg(String message, String exMessage, StackTraceElement[] stack, String functionname)
    {
//        JOptionPane.showConfirmDialog(null, message +"." + "\r\n" + exMessage + "\r\n" + stack, "ArcGIS_SLD_Converter | XMLHandle | " + functionname, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        //WriteToFile()
        MyTermination();
    }
    private void InfoMsg(String message, String functionname)
    {
//        JOptionPane.showConfirmDialog(null, message, "ArcGIS_SLD_Converter | XMLHandle | " + functionname, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }
    public final boolean SaveDoc()
    {
        try
        {
            OutputFormat format = new OutputFormat("    ",true);
            format.setEncoding(m_cXMLEncoding);//设置编码格式
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(m_cXMLFilename),format);

            xmlWriter.write(m_objDoc);
            xmlWriter.close();
        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim speichern der Datei", ex.getMessage(), ex.getStackTrace(), "SaveDoc");
        }
        return false;
    }

    private Object MyTermination()
    {
//        ProjectData.EndApp();
        //oder: application.exit
        return null;
    }




}
