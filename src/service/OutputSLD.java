//package service;
//
//import com.esri.arcgis.display.ISymbol;
//
//public class OutputSLD
//{
//    private MainFormProcess mainFormProcess;
//    private ArcMapSymbols m_objData;
//    private ArcMapSymbols.StructProject m_strDataSavings;
//    private XMLHandle m_objXMLHandle;
//    private String m_cFilename;
//    private String m_cFile;
//    private String m_cPath;
//    private boolean m_bSepFiles;
//    public OutputSLD(MainFormProcess mainFormProcess, ArcMapSymbols Analize, String Filename)
//    {
//        this.mainFormProcess = mainFormProcess;
//        m_cFilename = Filename;
//        m_bSepFiles = mainFormProcess.ismSeparateFiles();
//        m_cFile = String.valueOf(mainFormProcess.getmSLDFile());
//        m_cPath = String.valueOf(mainFormProcess.getmSLDPath());
//        m_objData = Analize;
//        m_strDataSavings = m_objData.m_StrProject;
//        CentralProcessingFunc();
//    }
//
//    public OutputSLD()
//    {
//        //m_cFilename = "C:\Krimskrams\bla.sld"      'Zu Testzwecken
//        CentralProcessingFunc();
//    }
//
//    private boolean CentralProcessingFunc()
//    {
//        boolean bSuccess;
//        bSuccess = false;
//        LogRecord.infoMsg("The Output in SLD");
//        LogRecord.infoMsg("the stored data is beeing processed");
//        // m_objXMLHandle = New XMLHandle
//        //CreateSLD()
//        if (WriteToSLD() == true)
//        {
//            bSuccess = true;
//        }
//        LogRecord.infoMsg("Ready");
//        if (bSuccess == true)
//        {
//            LogRecord.infoMsg("The file has been generated.");
//
////            if (frmMotherForm.chkValidate.Checked == true)
////            {
////                ValidateSLD ValSLD = new ValidateSLD(frmMotherForm);
////            }
////            else
////            {
////                frmMotherForm.CHLabelSmall("");
////            }
//        }
//        else
//        {
//            LogRecord.infoMsg("Could't generate the file");
//            LogRecord.infoMsg("");
//
//        }
//        return bSuccess;
//    }
//
//
//    //************************************************************************************************
//    //Creates SLD Doc: Document Description, Root-Node
//    //************************************************************************************************
//    private boolean CreateSLD(String FileName)
//    {
//        m_objXMLHandle = new XMLHandle(FileName);
//        m_objXMLHandle.CreateNewFile(true);
//        return true;
//    }
//
//
//    //************************************************************************************************
//    //Extracts the data from the structs and writes to SLD
//    //Firstly is decided, if the Layers are written in one or in separate files
//    //************************************************************************************************
//    public final boolean WriteToSLD()
//    {
//        int i = 0;
//        int j = 0;
//        int l = 0;
//        String cLayerName = "";
//        java.util.ArrayList objFieldValues = null;
//        boolean bDoOneLayer = false;
//        double dummy = 0; //to buffer the double coming from the layer list
//
//        //the decision if separate Layers or one Layer
//        if (m_bSepFiles == true)
//        {
//            bDoOneLayer = false;
//        }
//        else
//        {
//            bDoOneLayer = true;
//            //creation of the SLD with only one File using the user defined filename
//            CreateSLD(m_cFilename);
//        }
//
//
//        try
//        {
//            for (i = 0; i <= m_strDataSavings.LayerCount - 1; i++)
//            {
//                cLayerName = m_strDataSavings.LayerList.get(i).getName();
//                LogRecord.infoMsg("processing layer " + cLayerName);
//                //Creation of several SLD with that format: /UserDefinedPath/UserDefinedName_LayerName.sld
//                if (bDoOneLayer == false)
//                {
//                    CreateSLD(m_cFilename + "_" + cLayerName +".sld");
//                }
//
//                //XML-Schreibanweisungen auf Projektebene und Layerebene
//                m_objXMLHandle.CreateElement("NamedLayer");
//                m_objXMLHandle.CreateElement("LayerName");
//                m_objXMLHandle.SetElementText(m_strDataSavings.LayerList.get(i).DatasetName);
//                m_objXMLHandle.CreateElement("UserStyle");
//                m_objXMLHandle.CreateElement("StyleName");
//                m_objXMLHandle.SetElementText("Style1");
//                m_objXMLHandle.CreateElement("FeatureTypeStyle");
//                m_objXMLHandle.CreateElement("FeatureTypeName");
//                m_objXMLHandle.SetElementText(m_strDataSavings.LayerList(i).DatasetName);
//
//                //XML-Schreibanweisungen auf Layerebene und auf Symbolebene
//                java.util.ArrayList objSymbols = null; //Die ArrayList mit den Symbolen eines Layers
//                objSymbols = (java.util.ArrayList)(m_strDataSavings.LayerList(i).SymbolList);
//                for (j = 0; j <= objSymbols.size() - 1; j++) //IN DER SCHLEIFE AUF SYMBOLEBENE objSymbols(j) repr鋝entiert 1 Symbol!!!
//                {
//                    if (frmMotherForm.m_enumLang == Motherform.Language.Deutsch)
//                    {
//                        frmMotherForm.CHLabelSmall("Symbol " + (j + 1).toString() + " von " + objSymbols.size().toString());
//                    }
//                    else if (frmMotherForm.m_enumLang == Motherform.Language.English)
//                    {
//                        frmMotherForm.CHLabelSmall("Symbol " + (j + 1).toString() + " of " + objSymbols.size().toString());
//                    }
//
//                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                    //HIER DIE UNTERSCHEIDUNGEN NACH DEN EINZELNEN RENDERERN: UNIQUEVALUERENDERER
//                    if (m_strDataSavings.LayerList(i) instanceof StructUniqueValueRenderer)
//                    {
//                        StructUniqueValueRenderer objStructUVR = null;
//                        objStructUVR = m_strDataSavings.LayerList(i); //Zuweisung des StructsUVR. Repr鋝entiert je einen Layer!!!
//                        m_objXMLHandle.CreateElement("Rule");
//                        m_objXMLHandle.CreateElement("RuleName");
//                        m_objXMLHandle.SetElementText(objSymbols.get(j).Label);
//                        m_objXMLHandle.CreateElement("Title");
//                        m_objXMLHandle.SetElementText(objSymbols.get(j).Label);
//                        m_objXMLHandle.CreateElement("Filter");
//                        if (frmMotherForm.chkScale.Checked == true)
//                        {
//                            m_objXMLHandle.CreateElement("MinScale");
//                            m_objXMLHandle.SetElementText(frmMotherForm.cboLowScale.getText());
//                            m_objXMLHandle.CreateElement("MaxScale");
//                            m_objXMLHandle.SetElementText(frmMotherForm.cboHighScale.getText());
//                        }
//                        objFieldValues = (java.util.ArrayList)(objSymbols.get(j).Fieldvalues);
//                        if (objStructUVR.FieldCount > 1) //Nur wenn nach mehr als 1 Feld klassifiziert wurde, wird der <AND>-Tag gesetzt
//                        {
//                            m_objXMLHandle.CreateElement("And");
//                            for (l = 0; l <= objStructUVR.FieldCount - 1; l++) //Die Schleife ist nur daf黵 da, falls nach mehreren Feldern klassifiziert wurde
//                            {
//                                m_objXMLHandle.CreateElement("PropertyIsEqualTo"); //Sie schreibt pro Feld nach dem klass. wurde das <PropertyIsEqualTo> und alle Kinder
//                                m_objXMLHandle.CreateElement("PropertyName");
//                                m_objXMLHandle.SetElementText(objStructUVR.FieldNames(l));
//                                m_objXMLHandle.CreateElement("Fieldvalue");
//                                m_objXMLHandle.SetElementText(objFieldValues.get(l));
//                            }
//                        }
//                        else if (objStructUVR.FieldCount == 1)
//                        {
//                            if (objFieldValues.size() > 1)
//                            {
//                                m_objXMLHandle.CreateElement("Or");
//                            }
//                            for (l = 0; l <= objFieldValues.size() - 1; l++) //If multiple values grouped in same class
//                            {
//                                m_objXMLHandle.CreateElement("PropertyIsEqualTo"); //Sie schreibt pro Feld nach dem klass. wurde das <PropertyIsEqualTo> und alle Kinder
//                                m_objXMLHandle.CreateElement("PropertyName");
//                                m_objXMLHandle.SetElementText(objStructUVR.FieldNames(0));
//                                m_objXMLHandle.CreateElement("Fieldvalue");
//                                m_objXMLHandle.SetElementText(objFieldValues.get(l));
//                            }
//                        }
//                        //UNTERSCHEIDUNG NACH FEATURECLASS DES BETREFFENDEN SYMBOLS
//                        if (objStructUVR.FeatureCls == FeatureClass.PointFeature)
//                        {
//                            WritePointFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructUVR.FeatureCls == FeatureClass.LineFeature)
//                        {
//                            WriteLineFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructUVR.FeatureCls == FeatureClass.PolygonFeature)
//                        {
//                            WritePolygonFeatures(objSymbols.get(j));
//                        }
//                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                        //HIER DIE UNTERSCHEIDUNGEN NACH DEN EINZELNEN RENDERERN: CLASSBREAKSRENDERER
//                    }
//                    else if (m_strDataSavings.LayerList(i) instanceof StructClassBreaksRenderer)
//                    {
//                        StructClassBreaksRenderer objStructCBR = null;
//                        objStructCBR = m_strDataSavings.LayerList(i);
//                        m_objXMLHandle.CreateElement("Rule");
//                        m_objXMLHandle.CreateElement("RuleName");
//                        m_objXMLHandle.SetElementText(objSymbols.get(j).Label);
//                        m_objXMLHandle.CreateElement("Title");
//                        m_objXMLHandle.SetElementText(objSymbols.get(j).Label);
//                        m_objXMLHandle.CreateElement("Filter");
//                        if (frmMotherForm.chkScale.Checked == true)
//                        {
//                            m_objXMLHandle.CreateElement("MinScale");
//                            m_objXMLHandle.SetElementText(frmMotherForm.cboLowScale.getText());
//                            m_objXMLHandle.CreateElement("MaxScale");
//                            m_objXMLHandle.SetElementText(frmMotherForm.cboHighScale.getText());
//                        }
//                        m_objXMLHandle.CreateElement("PropertyIsBetween");
//                        m_objXMLHandle.CreateElement("PropertyName");
//                        m_objXMLHandle.SetElementText(objStructCBR.FieldName);
//                        m_objXMLHandle.CreateElement("LowerBoundary");
//                        m_objXMLHandle.CreateElement("Fieldvalue");
//                        dummy = Double.parseDouble(objSymbols.get(j).LowerLimit); //As ArrayList member the type is no more recognized from compiler. If saving in a dummy double its recognized again
//                        m_objXMLHandle.SetElementText(CommaToPoint(dummy));
//                        m_objXMLHandle.CreateElement("UpperBoundary");
//                        m_objXMLHandle.CreateElement("Fieldvalue");
//                        dummy = Double.parseDouble(objSymbols.get(j).UpperLimit); //As ArrayList member the type is no more recognized from compiler. If saving in a dummy double its recognized again
//                        m_objXMLHandle.SetElementText(CommaToPoint(dummy));
//                        if (objStructCBR.FeatureCls == FeatureClass.PointFeature)
//                        {
//                            WritePointFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructCBR.FeatureCls == FeatureClass.LineFeature)
//                        {
//                            WriteLineFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructCBR.FeatureCls == FeatureClass.PolygonFeature)
//                        {
//                            WritePolygonFeatures(objSymbols.get(j));
//                        }
//                        //HIER DIE UNTERSCHEIDUNGEN NACH DEN EINZELNEN RENDERERN: SIMPLERENDERER
//                    }
//                    else if (m_strDataSavings.LayerList(i) instanceof StructSimpleRenderer)
//                    {
//                        StructSimpleRenderer objStructSR = null;
//                        objStructSR = m_strDataSavings.LayerList(i);
//                        m_objXMLHandle.CreateElement("Rule");
//                        m_objXMLHandle.CreateElement("RuleName");
//                        m_objXMLHandle.SetElementText(m_strDataSavings.LayerList(i).DatasetName);
//                        m_objXMLHandle.CreateElement("Title");
//                        m_objXMLHandle.SetElementText(m_strDataSavings.LayerList(i).DatasetName);
//                        if (objStructSR.FeatureCls == FeatureClass.PointFeature)
//                        {
//                            WritePointFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructSR.FeatureCls == FeatureClass.LineFeature)
//                        {
//                            WriteLineFeatures(objSymbols.get(j));
//                        }
//                        else if (objStructSR.FeatureCls == FeatureClass.PolygonFeature)
//                        {
//                            WritePolygonFeatures(objSymbols.get(j));
//                        }
//                        WriteAnnotation(m_strDataSavings.LayerList(i).Annotation);
//                    }
//                }
//                if (bDoOneLayer == false)
//                {
//                    m_objXMLHandle.SaveDoc(); //If separate layer, the files have to be saved here
//                }
//            }
//            if (bDoOneLayer == true)
//            {
//                m_objXMLHandle.SaveDoc(); //else the file has to be saved here
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Konnte die SLD nicht schreiben", ex.getMessage(), ex.StackTrace, "WriteToSLD");
//            return false;
//        }
//    }
//
//    private boolean WriteAnnotation(StructAnnotation Annotation)
//    {
//        if (Annotation.IsSingleProperty && ! Annotation.PropertyName.equals(""))
//        {
//            m_objXMLHandle.CreateElement("TextSymbolizer");
//            m_objXMLHandle.CreateElement("TextLabel");
//            m_objXMLHandle.CreateElement("TextLabelProperty");
//            m_objXMLHandle.SetElementText(Annotation.PropertyName);
//            m_objXMLHandle.CreateElement("TextFont");
//            m_objXMLHandle.CreateElement("TextFontCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("font-family");
//            m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextFont", Annotation.TextSymbol));
//            if (!GetValueFromSymbolstruct("TextFontAlt", Annotation.TextSymbol).equals(""))
//            {
//                m_objXMLHandle.CreateElement("TextFontCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("font-family");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextFontAlt", Annotation.TextSymbol));
//            }
//            m_objXMLHandle.CreateElement("TextFontCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("font-size");
//            m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextFontSize", Annotation.TextSymbol));
//            m_objXMLHandle.CreateElement("TextFontCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("font-style");
//            m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextFontStyle", Annotation.TextSymbol));
//            m_objXMLHandle.CreateElement("TextFontCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("font-weight");
//            m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextFontWeight", Annotation.TextSymbol));
//            m_objXMLHandle.CreateElement("TextFill");
//            m_objXMLHandle.CreateElement("TextFillCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("fill");
//            m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("TextColor", Annotation.TextSymbol));
//            m_objXMLHandle.CreateElement("TextFillCssParameter");
//            m_objXMLHandle.CreateAttribute("name");
//            m_objXMLHandle.SetAttributeValue("fill-opacity");
//            m_objXMLHandle.SetElementText("1.0");
//        }
//        return true;
//    }
//
//    //************************************************************************************************
//    //This function writes the SLD features for a PointSymbolizer based on the passed (point)symbol.
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WritePointFeatures(Object Symbol)
//    {
//        try
//        {
//            int layerIdx = 0;
//            int maxLayerIdx = 1;
//
//            if (Symbol instanceof StructMultilayerMarkerSymbol)
//            {
//                StructMultilayerMarkerSymbol objTempStruct = null;
//                objTempStruct = Symbol;
//                maxLayerIdx = Integer.parseInt(objTempStruct.LayerCount);
//            }
//            for (layerIdx = 0; layerIdx <= maxLayerIdx - 1; layerIdx++)
//            {
//                m_objXMLHandle.CreateElement("PointSymbolizer");
//                m_objXMLHandle.CreateElement("PointGraphic");
//                m_objXMLHandle.CreateElement("Mark");
//                m_objXMLHandle.CreateElement("PointWellKnownName");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("WellKnownName", Symbol, layerIdx));
//                if (!GetValueFromSymbolstruct("PointColor", Symbol, layerIdx).equals(""))
//                {
//                    m_objXMLHandle.CreateElement("PointFill");
//                    m_objXMLHandle.CreateElement("PointFillCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("fill");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointColor", Symbol, layerIdx));
//                    m_objXMLHandle.CreateElement("PointFillCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("fill-opacity");
//                    m_objXMLHandle.SetElementText("1.0");
//                }
//                if (!GetValueFromSymbolstruct("PointOutlineColor", Symbol, layerIdx).equals("") && !GetValueFromSymbolstruct("PointOutlineSize", Symbol, layerIdx).equals("0"))
//                {
//                    m_objXMLHandle.CreateElement("PointStroke");
//                    m_objXMLHandle.CreateElement("PointStrokeCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointOutlineColor", Symbol, layerIdx));
//                    m_objXMLHandle.CreateElement("PointStrokeCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke-width");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointOutlineSize", Symbol, layerIdx));
//                    m_objXMLHandle.CreateElement("PointStrokeCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                    m_objXMLHandle.SetElementText("1.0");
//                }
//                m_objXMLHandle.CreateElement("PointSize");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointSize", Symbol, layerIdx));
//                m_objXMLHandle.CreateElement("PointRotation");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointRotation", Symbol, layerIdx));
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Schraffur", ex.getMessage(), ex.StackTrace, "WritePointFeatures");
//            return false;
//        }
//    }
//
//    //************************************************************************************************
//    //This function writes the SLD features for a LineSymbolizer based on the passed (line)symbol.
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WriteLineFeatures(Object Symbol)
//    {
//        try
//        {
//            int layerIdx = 0;
//            int maxLayerIdx = 1;
//
//            if (Symbol instanceof StructMultilayerLineSymbol)
//            {
//                StructMultilayerLineSymbol objTempStruct = null;
//                objTempStruct = Symbol;
//                maxLayerIdx = Integer.parseInt(objTempStruct.LayerCount);
//            }
//            for (layerIdx = 0; layerIdx <= maxLayerIdx - 1; layerIdx++)
//            {
//                if (!GetValueFromSymbolstruct("LineColor", Symbol, layerIdx).equals("") && !GetValueFromSymbolstruct("LineWidth", Symbol, layerIdx).equals("0"))
//                {
//                    m_objXMLHandle.CreateElement("LineSymbolizer");
//                    m_objXMLHandle.CreateElement("LineStroke");
//                    m_objXMLHandle.CreateElement("LineCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("LineColor", Symbol, layerIdx));
//                    m_objXMLHandle.CreateElement("LineCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke-width");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("LineWidth", Symbol, layerIdx));
//                    m_objXMLHandle.CreateElement("LineCssParameter");
//                    m_objXMLHandle.CreateAttribute("name");
//                    m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                    m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("LineOpacity", Symbol, layerIdx));
//                    if (!GetValueFromSymbolstruct("LineDashArray", Symbol, layerIdx).equals(""))
//                    {
//                        m_objXMLHandle.CreateElement("LineCssParameter");
//                        m_objXMLHandle.CreateAttribute("name");
//                        m_objXMLHandle.SetAttributeValue("stroke-dasharray");
//                        m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("LineDashArray", Symbol, layerIdx));
//                    }
//                }
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Schraffur", ex.getMessage(), ex.StackTrace, "WriteLineFeatures");
//            return false;
//        }
//    }
//
//    //************************************************************************************************
//    //Die Funktion 黚ernimmt das aktuelle Symbol und verteilt es auf die Unterfunktionen zum schreiben
//    //Bei MultilayerFillsymbol rekursiver Aufruf
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WritePolygonFeatures(Object Symbol)
//    {
//        // WriteSolidFill(Symbol)
//        short i = 0;
//        short iSecure = 0;
//        iSecure = (short) 0;
//        try
//        {
//            if (Symbol instanceof StructSimpleFillSymbol)
//            {
//                WriteSolidFill(Symbol);
//            }
//            else if (Symbol instanceof StructMarkerFillSymbol)
//            {
//                WriteMarkerFill(Symbol);
//            }
//            else if (Symbol instanceof StructLineFillSymbol)
//            {
//                //Die Winkel der Schraffuren Original:(Schr鋑/Horizontal,Vertikal)-SLD(Kreuzschraffur schr鋑/Kreuzschraffur Achsparallel)
//                if (Symbol.Angle > 22.5 && Symbol.Angle < 67.5)
//                {
//                    WriteSlopedHatching(Symbol);
//                }
//                else if (Symbol.Angle > 67.5 && Symbol.Angle < 112.5)
//                {
//                    WritePerpendicularHatching(Symbol);
//                }
//                else if (Symbol.Angle > 112.5 && Symbol.Angle < 157.5)
//                {
//                    WriteSlopedHatching(Symbol);
//                }
//                else if (Symbol.Angle > 157.5 && Symbol.Angle < 202.5)
//                {
//                    WritePerpendicularHatching(Symbol);
//                }
//                else if (Symbol.Angle > 202.5 && Symbol.Angle < 247.5)
//                {
//                    WriteSlopedHatching(Symbol);
//                }
//                else if (Symbol.Angle > 247.5 && Symbol.Angle < 292.5)
//                {
//                    WritePerpendicularHatching(Symbol);
//                }
//                else if (Symbol.Angle > 292.5 && Symbol.Angle < 337.5)
//                {
//                    WriteSlopedHatching(Symbol);
//                }
//                else if (Symbol.Angle > 337.5 && Symbol.Angle <= 360.0 || Symbol.Angle >= 0.0 && Symbol.Angle < 22.5)
//                {
//                    WritePerpendicularHatching(Symbol);
//                }
//            }
//            else if (Symbol instanceof StructDotDensityFillSymbol)
//            {
//                WriteMarkerFill(Symbol); //Ist z.Zt. sowieso nicht m鰃lich, Dichte mit Punktf黮lungen auszugeben
//            }
//            else if (Symbol instanceof StructPictureFillSymbol)
//            {
//                //TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
//            }
//            else if (Symbol instanceof StructGradientFillSymbol)
//            {
//                //TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO TODO
//            }
//            else if (Symbol instanceof StructMultilayerFillSymbol)
//            {
//                StructMultilayerFillSymbol MFS = null;
//                MFS = Symbol;
//                boolean bSwitch; //Wenn mehr als 3 Symbollayer sind, und einer davon ist ein SimpleFill
//                bSwitch = false;
//                //Hier muss aufgepasst werden: Manche Mapserver k鰊nen nur 2 Symbollayer 黚ereinander abbilden. Deshalb werden derzeit nur 2 Symbollayer gebildet
//                if (MFS.LayerCount == 1)
//                {
//                    WritePolygonFeatures(MFS.MultiFillLayers(0));
//                }
//                else if (MFS.LayerCount == 2)
//                {
//                    for (i = MFS.LayerCount - 1; i >= 0; i--)
//                    {
//                        WritePolygonFeatures(MFS.MultiFillLayers(i)); //hier rekursiver Aufruf
//                    }
//                }
//                else if (MFS.LayerCount > 2)
//                {
//                    for (i = MFS.LayerCount - 1; i >= 0; i--)
//                    {
//                        if (iSecure <= 1)
//                        {
//                            WritePolygonFeatures(MFS.MultiFillLayers(i));
//                        }
//                        iSecure++;
//                    }
//                }
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Schraffur", ex.getMessage(), ex.StackTrace, "WritePolygonFeatures");
//            return false;
//        }
//    }
//
//
//    //************************************************************************************************
//    //Die Funktion schreibt die SLD-Anweisung f黵 eine SOLID COLOR FL腃HENF躄LUNG
//    //und 黚ernimmt die Eigenschaften aus der SimpleFill-Datenstruktur
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WriteSolidFill(Object Symbol)
//    {
//        try
//        {
//            m_objXMLHandle.CreateElement("PolygonSymbolizer");
//            if (!GetValueFromSymbolstruct("PolygonColor", Symbol).equals(""))
//            {
//                m_objXMLHandle.CreateElement("Fill");
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonColor", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill-opacity");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonOpacity", Symbol));
//            }
//            if (!GetValueFromSymbolstruct("PolygonBorderColor", Symbol).equals("") && !GetValueFromSymbolstruct("PolygonBorderWidth", Symbol).equals("0"))
//            {
//                m_objXMLHandle.CreateElement("PolygonStroke");
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderColor", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-width");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderWidth", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderOpacity", Symbol));
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Fl鋍henf黮lung", ex.getMessage(), ex.StackTrace, "WriteSimpleFill");
//            return false;
//        }
//
//    }
//
//    //************************************************************************************************
//    //Die Funktion schreibt die SLD-Anweisung f黵 eine GEPUNKTETE FL腃HENF躄LUNG
//    //und 黚ernimmt die Eigenschaften aus der MarkerFill-Datenstruktur (und z.Zt. der DotDensity-Strukt.)
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WriteMarkerFill(Object Symbol)
//    {
//        try
//        {
//            m_objXMLHandle.CreateElement("PolygonSymbolizer");
//            if (!GetValueFromSymbolstruct("PointColor", Symbol).equals(""))
//            {
//                m_objXMLHandle.CreateElement("Fill");
//                m_objXMLHandle.CreateElement("PolygonGraphicFill");
//                m_objXMLHandle.CreateElement("PolygonGraphic");
//                m_objXMLHandle.CreateElement("PolygonMark");
//                m_objXMLHandle.CreateElement("PolygonSize");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointSize", Symbol));
//                m_objXMLHandle.CreateElement("PolygonWellKnownName");
//                m_objXMLHandle.SetElementText("circle");
//                m_objXMLHandle.CreateElement("PolygonGraphicParamFill");
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PointColor", Symbol));
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill-opacity");
//                m_objXMLHandle.SetElementText("1.0");
//            }
//            if (!GetValueFromSymbolstruct("PolygonBorderColor", Symbol).equals("") && !GetValueFromSymbolstruct("PolygonBorderWidth", Symbol).equals("0"))
//            {
//                m_objXMLHandle.CreateElement("PolygonStroke");
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderColor", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-width");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderWidth", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderOpacity", Symbol));
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Punktf黮lung", ex.getMessage(), ex.StackTrace, "WriteMarkerFill");
//            return false;
//        }
//
//    }
//
//    //************************************************************************************************
//    //Die Funktion schreibt die SLD-Anweisung f黵 eine SCHR腉E KREUZSCHRAFFUR-FL腃HENF躄LUNG
//    //und 黚ernimmt die Eigenschaften aus der LineFill-Datenstruktur.
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WriteSlopedHatching(Object Symbol)
//    {
//        double dDummy = 0;
//        try
//        {
//            m_objXMLHandle.CreateElement("PolygonSymbolizer");
//            if ( ! Symbol.Color.equals(""))
//            {
//                m_objXMLHandle.CreateElement("Fill");
//                m_objXMLHandle.CreateElement("PolygonGraphicFill");
//                m_objXMLHandle.CreateElement("PolygonGraphic");
//                //.SetElementText(GetValueFromSymbolstruct("LineWidth", Symbol))
//                m_objXMLHandle.CreateElement("PolygonMark");
//                m_objXMLHandle.CreateElement("PolygonSize");
//                //Schraffurgr鲞e
//                dDummy = Double.parseDouble(Symbol.Separation + 5);
//                m_objXMLHandle.SetElementText(CommaToPoint(dDummy));
//                m_objXMLHandle.CreateElement("PolygonWellKnownName");
//                m_objXMLHandle.SetElementText("x"); //Macht die schr鋑e Kreuzschraffur
//                m_objXMLHandle.CreateElement("PolygonGraphicParamFill");
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill");
//                //Die Schraffurfarbe
//                m_objXMLHandle.SetElementText(Symbol.Color);
//                //.SetElementText(GetValueFromSymbolstruct("LineColor", Symbol))
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill-opacity");
//                m_objXMLHandle.SetElementText("1.0");
//            }
//            if (!GetValueFromSymbolstruct("PolygonBorderColor", Symbol).equals("") && !GetValueFromSymbolstruct("PolygonBorderWidth", Symbol).equals("0"))
//            {
//                m_objXMLHandle.CreateElement("PolygonStroke");
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderColor", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-width");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderWidth", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderOpacity", Symbol));
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Schraffur", ex.getMessage(), ex.StackTrace, "WriteSlopedHatching");
//            return false;
//        }
//    }
//
//
//    //************************************************************************************************
//    //Die Funktion schreibt die SLD-Anweisung f黵 eine SENKRECHTE KREUZSCHRAFFUR-FL腃HENF躄LUNG
//    //und 黚ernimmt die Eigenschaften aus der LineFill-Datenstruktur.
//    //Parameter:
//    //           Symbol=Die Symbolstruktur des aktuellen Symbols aus der Symbolsammlung
//    //************************************************************************************************
//    private boolean WritePerpendicularHatching(ISymbol Symbol)
//    {
//        double dDummy = 0;
//        try
//        {
//            m_objXMLHandle.CreateElement("PolygonSymbolizer");
//            if ( ! Symbol.Color.equals(""))
//            {
//                m_objXMLHandle.CreateElement("Fill");
//                m_objXMLHandle.CreateElement("PolygonGraphicFill");
//                m_objXMLHandle.CreateElement("PolygonGraphic");
//                //.SetElementText(GetValueFromSymbolstruct("LineWidth", Symbol))
//                m_objXMLHandle.CreateElement("PolygonMark");
//                m_objXMLHandle.CreateElement("PolygonSize");
//                //Schraffurgr鲞e
//                dDummy = Double.parseDouble(Symbol.Separation + 5);
//                m_objXMLHandle.SetElementText(CommaToPoint(dDummy));
//                m_objXMLHandle.CreateElement("PolygonWellKnownName");
//                m_objXMLHandle.SetElementText("cross");
//                m_objXMLHandle.CreateElement("PolygonGraphicParamFill");
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill");
//                //Die Schraffurfarbe
//                m_objXMLHandle.SetElementText(Symbol.Color);
//                //.SetElementText(GetValueFromSymbolstruct("LineColor", Symbol))
//                m_objXMLHandle.CreateElement("PolygonGraphicCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("fill-opacity");
//                m_objXMLHandle.SetElementText("1.0");
//            }
//            if (!GetValueFromSymbolstruct("PolygonBorderColor", Symbol).equals("") && !GetValueFromSymbolstruct("PolygonBorderWidth", Symbol).equals("0"))
//            {
//                m_objXMLHandle.CreateElement("PolygonStroke");
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderColor", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-width");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderWidth", Symbol));
//                m_objXMLHandle.CreateElement("PolyCssParameter");
//                m_objXMLHandle.CreateAttribute("name");
//                m_objXMLHandle.SetAttributeValue("stroke-opacity");
//                m_objXMLHandle.SetElementText(GetValueFromSymbolstruct("PolygonBorderOpacity", Symbol));
//            }
//            return true;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Fehler beim Schreiben der Punktf黮lung", ex.getMessage(), ex.StackTrace, "WritePerpendicularHatching");
//            return false;
//        }
//    }
//
//    private String GetValueFromSymbolstruct(String ValueNameOfValueYouWant, Object SymbolStructure)
//    {
//        return GetValueFromSymbolstruct(ValueNameOfValueYouWant, SymbolStructure, 0);
//    }
//    private String GetValueFromSymbolstruct(String ValueNameOfValueYouWant, Object SymbolStructure, int LayerIdx)
//    {
//        String cReturn = "";
//        boolean bSwitch;
//        bSwitch = false; //(ben鰐igt f黵 Multilayersymbole)der Schalter wird umgelegt, wenn es kein simple.. Symbol gibt. Dann wird der Wert des ersten Symbols genommen
//        cReturn = "0"; //Wenn keiner der 黚ergebenen ValueNames passt, wird 0 zur點kgegeben
//        try
//        {
//            if (SymbolStructure instanceof ArcMapSymbols.StructSimpleMarkerSymbol)
//            {
//                cReturn = GetMarkerValue(ValueNameOfValueYouWant, SymbolStructure);
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructCharacterMarkerSymbol)
//            {
//                cReturn = GetMarkerValue(ValueNameOfValueYouWant, SymbolStructure);
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructPictureMarkerSymbol)
//            {
//                cReturn = GetMarkerValue(ValueNameOfValueYouWant, SymbolStructure);
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructArrowMarkerSymbol)
//            {
//                cReturn = GetMarkerValue(ValueNameOfValueYouWant, SymbolStructure);
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructSimpleLineSymbol)
//            {
//                ArcMapSymbols.StructSimpleLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructSimpleLineSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Width);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                    //Case "PointRotation".ToUpper
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineDashArray")).toUpperCase()))
//                {
//                    if (objTempStruct.publicStyle.equals("esriSLSDash"))
//                    {
//                        cReturn = "10.0 10.0";
//                    }
//                    else if (objTempStruct.publicStyle.equals("esriSLSDashDot"))
//                    {
//                        cReturn = "10.0 10.0 1.0 10.0";
//                    }
//                    else if (objTempStruct.publicStyle.equals("esriSLSDashDotDot"))
//                    {
//                        cReturn = "10.0 10.0 1.0 10.0 1.0 10.0";
//                    }
//                    else if (objTempStruct.publicStyle.equals("esriSLSDot"))
//                    {
//                        cReturn = "1.0 5.0";
//                    }
//                    else
//                    {
//                        cReturn = "";
//                    }
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructCartographicLineSymbol)
//            {
//                ArcMapSymbols.StructCartographicLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructCartographicLineSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Width);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineDashArray")).toUpperCase()))
//                {
//                    int dashIdx = 0;
//                    double size = 0;
//                    cReturn = "";
//                    for (dashIdx = 0; dashIdx <= objTempStruct.DashArray.size() - 1; dashIdx++)
//                    {
//                        if (dashIdx > 0)
//                        {
//                            cReturn = cReturn + " ";
//                        }
//                        size = Double.parseDouble(objTempStruct.DashArray.get(dashIdx).toString());
//                        cReturn = cReturn + CommaToPoint(size);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//                }
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructHashLineSymbol)
//            {
//                ArcMapSymbols.StructHashLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructHashLineSymbol) SymbolStructure;
//                if ((ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase())) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineDashArray")).toUpperCase())))
//                {
//                    if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructCartographicLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.HashSymbol_CartographicLine);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.HashSymbol_MarkerLine);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.HashSymbol_MultiLayerLines, LayerIdx);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.HashSymbol_PictureLine);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.HashSymbol_SimpleLine);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructMarkerLineSymbol)
//            {
//                ArcMapSymbols.StructMarkerLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructMarkerLineSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase()))
//                {
//                    InfoMsg("Abfrage von Linienbreite der Markerlines ist im Augenblick nicht implementiert", "GetValueFromSymbolstruct");
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructPictureLineSymbol)
//            {
//                ArcMapSymbols.StructPictureLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructPictureLineSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Width);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.BackgroundColor);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.BackgroundTransparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructSimpleFillSymbol)
//            {
//                ArcMapSymbols.StructSimpleFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructSimpleFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructMarkerFillSymbol)
//            {
//                ArcMapSymbols.StructMarkerFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructMarkerFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency =objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructArrowMarkerSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.MarkerSymbol_ArrowMarker.Size);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructCharacterMarkerSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.MarkerSymbol_CharacterMarker.Size);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructMultilayerMarkerSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("PointSize", objTempStruct.MarkerSymbol_MultilayerMarker);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructPictureMarkerSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.MarkerSymbol_PictureMarker.Size);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructSimpleMarkerSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.MarkerSymbol_SimpleMarker.Size);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructArrowMarkerSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.MarkerSymbol_ArrowMarker.Color);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructCharacterMarkerSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.MarkerSymbol_CharacterMarker.Color);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructMultilayerMarkerSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("PointColor", objTempStruct.MarkerSymbol_MultilayerMarker);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructPictureMarkerSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.MarkerSymbol_PictureMarker.Color);
//                    }
//                    else if (objTempStruct.kindOfMarkerStruct == ArcMapSymbols.MarkerStructs.StructSimpleMarkerSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.MarkerSymbol_SimpleMarker.Color);
//                    }
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructLineFillSymbol)
//            {
//                ArcMapSymbols.StructLineFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructLineFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructCartographicLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.LineSymbol_CartographicLine.Width);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.LineSymbol_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.LineSymbol_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.LineSymbol_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.LineSymbol_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.LineSymbol_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructCartographicLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.LineSymbol_CartographicLine.Color);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.LineSymbol_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.LineSymbol_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.LineSymbol_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.LineSymbol_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.LineSymbol_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructCartographicLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.LineSymbol_CartographicLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency =objTempStruct.LineSymbol_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency =objTempStruct.LineSymbol_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.LineSymbol_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.LineSymbol_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfLineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.LineSymbol_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructDotDensityFillSymbol)
//            {
//                ArcMapSymbols.StructDotDensityFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructDotDensityFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.BackgroundColor);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.BackgroundTransparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency =objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.DotSize);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructPictureFillSymbol)
//            {
//                ArcMapSymbols.StructPictureFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructPictureFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.BackgroundColor);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = objTempStruct.BackgroundTransparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructGradientFillSymbol)
//            {
//                ArcMapSymbols.StructGradientFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructGradientFillSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency =objTempStruct.Transparency;
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_HashLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_MarkerLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineWidth", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_PictureLine.Width);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = CommaToPoint(objTempStruct.Outline_SimpleLine.Width);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))
//                {
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_HashLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_MarkerLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        cReturn = GetValueFromSymbolstruct("LineColor", objTempStruct.Outline_MultiLayerLines);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_PictureLine.Color);
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Outline_SimpleLine.Color);
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase()))
//                {
//                    double tmpTransparency = 255.0;
//                    if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructHashLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_HashLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMarkerLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_MarkerLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructMultilayerLineSymbol)
//                    {
//                        tmpTransparency = 255 * Double.parseDouble(GetValueFromSymbolstruct("LineOpacity", objTempStruct.Outline_MultiLayerLines));
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructPictureLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_PictureLine.Transparency;
//                    }
//                    else if (objTempStruct.kindOfOutlineStruct == ArcMapSymbols.LineStructs.StructSimpleLineSymbol)
//                    {
//                        tmpTransparency = objTempStruct.Outline_SimpleLine.Transparency;
//                    }
//                    cReturn = CommaToPoint(tmpTransparency / 255.0);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructBarChartSymbol)
//            {
//                ArcMapSymbols.StructBarChartSymbol objTempStruct;
//                objTempStruct = (ArcMapSymbols.StructBarChartSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructPieChartSymbol)
//            {
//                ArcMapSymbols.StructPieChartSymbol objTempStruct;
//                objTempStruct = (ArcMapSymbols.StructPieChartSymbol) SymbolStructure;
////C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
////				switch (ValueNameOfValueYouWant.ToUpper())
////ORIGINAL LINE: case "":
//                if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
////ORIGINAL LINE: case "":
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
////ORIGINAL LINE: case "":
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructStackedChartSymbol)
//            {
//                ArcMapSymbols.StructStackedChartSymbol objTempStruct;
//                objTempStruct = (ArcMapSymbols.StructStackedChartSymbol) SymbolStructure;
////C# TO JAVA CONVERTER NOTE: The following 'switch' operated on a string member and was converted to Java 'if-else' logic:
////				switch (ValueNameOfValueYouWant.ToUpper())
////ORIGINAL LINE: case "":
//                if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
////ORIGINAL LINE: case "":
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
////ORIGINAL LINE: case "":
//                else if (ValueNameOfValueYouWant.toUpperCase().equals(""))
//                {
//
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructTextSymbol)
//            {
//                ArcMapSymbols.StructTextSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructTextSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextFont")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Font);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextFontAlt")).toUpperCase()))
//                {
//                    if ((((((( objTempStruct.Font.toUpperCase().equals("ARIAL")) || (objTempStruct.Font.toUpperCase().equals("ARIAL BLACK"))) || ( objTempStruct.Font.toUpperCase().equals("HELVETICA"))) || ( objTempStruct.Font.toUpperCase().equals("LUCIDA SANS UNICODE"))) || ( objTempStruct.Font.toUpperCase().equals("MICROSOFT SANS SERIF"))) || ( objTempStruct.Font.toUpperCase().equals("TAHOMA"))) || ( objTempStruct.Font.toUpperCase().equals("VERDANA")))
//                    {
//                        cReturn = "Sans-Serif";
//                    }
//                    else if ((( objTempStruct.Font.toUpperCase().equals("COURIER")) || ( objTempStruct.Font.toUpperCase().equals("COURIER NEW"))) || ( objTempStruct.Font.toUpperCase().equals("LUCIDA CONSOLE")))
//                    {
//                        cReturn = "Monospaced";
//                    }
//                    else if ((( objTempStruct.Font.toUpperCase().equals("PALATINO LINOTYPE")) || ( objTempStruct.Font.toUpperCase().equals("TIMES"))) || ( objTempStruct.Font.toUpperCase().equals("TIMES NEW ROMAN")))
//                    {
//                        cReturn = "Serif";
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextFontSize")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Size);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextFontStyle")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Style);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("TextFontWeight")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Weight);
//                }
//                return cReturn;
//                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                //Die Multilayer-Symbolstructs
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructMultilayerMarkerSymbol)
//            {
//                ArcMapSymbols.StructMultilayerMarkerSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructMultilayerMarkerSymbol) SymbolStructure;
//                cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.MultiMarkerLayers.get(LayerIdx));
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructMultilayerLineSymbol)
//            {
//                ArcMapSymbols.StructMultilayerLineSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructMultilayerLineSymbol) SymbolStructure;
//                short i = 0;
//                if (objTempStruct.LayerCount > 1)
//                {
//                    for (i = 0; i <= objTempStruct.LayerCount - 1; i++)
//                    {
//                        if (objTempStruct.MultiLineLayers.get(i) instanceof ArcMapSymbols.StructSimpleLineSymbol)
//                        {
//                            ArcMapSymbols.StructSimpleLineSymbol SLFS = null;
//                            SLFS = (ArcMapSymbols.StructSimpleLineSymbol) objTempStruct.MultiLineLayers.get(i);
//                            cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, SLFS);
//                            bSwitch = true;
//                        }
//                    }
//                    if (bSwitch == false)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.MultiLineLayers.get(LayerIdx));
//                    }
//                }
//                else
//                {
//                    cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.MultiLineLayers.get(LayerIdx));
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructMultilayerFillSymbol)
//            {
//                ArcMapSymbols.StructMultilayerFillSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructMultilayerFillSymbol) SymbolStructure;
//                short i = 0;
//                if (objTempStruct.LayerCount > 1)
//                {
//                    for (i = 0; i <= objTempStruct.LayerCount - 1; i++)
//                    {
//                        if (((((ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonColor")).toUpperCase())) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonOpacity")).toUpperCase()))) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderWidth")).toUpperCase()))) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderColor")).toUpperCase()))) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PolygonBorderOpacity")).toUpperCase())))
//                        {
//                            if (objTempStruct.MultiFillLayers.get(i) instanceof ArcMapSymbols.StructSimpleFillSymbol)
//                            {
//                                ArcMapSymbols.StructSimpleFillSymbol SSFS = null;
//                                SSFS = (ArcMapSymbols.StructSimpleFillSymbol) objTempStruct.MultiFillLayers.get(i);
//                                cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, SSFS);
//                                bSwitch = true;
//                            }
//                        }
//                        else if ((ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase())) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase())))
//                        {
//                            if (objTempStruct.MultiFillLayers.get(i) instanceof ArcMapSymbols.StructMarkerFillSymbol)
//                            {
//                                ArcMapSymbols.StructMarkerFillSymbol SMFS = null;
//                                SMFS = (ArcMapSymbols.StructMarkerFillSymbol) objTempStruct.MultiFillLayers.get(i);
//                                cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, SMFS);
//                                bSwitch = true;
//                            }
//                        }
//                        else if (((ValueNameOfValueYouWant.toUpperCase().equals((new String("LineWidth")).toUpperCase())) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineColor")).toUpperCase()))) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("LineOpacity")).toUpperCase())))
//                        {
//                            if (objTempStruct.MultiFillLayers.get(i) instanceof ArcMapSymbols.StructLineFillSymbol)
//                            {
//                                ArcMapSymbols.StructLineFillSymbol SLFS = null;
//                                SLFS = (ArcMapSymbols.StructLineFillSymbol) objTempStruct.MultiFillLayers.get(i);
//                                cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, SLFS);
//                                bSwitch = true;
//                            }
//                        }
//                    }
//                    if (bSwitch == false)
//                    {
//                        cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.MultiFillLayers.get(LayerIdx));
//                    }
//                }
//                else
//                {
//                    cReturn = GetValueFromSymbolstruct(ValueNameOfValueYouWant, objTempStruct.MultiFillLayers.get(LayerIdx));
//                }
//                return cReturn;
//            }
//            return cReturn;
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Konnte den Wert aus der SymbolStruct nicht auswerten.", ex.getMessage(), ex.getStackTrace(), "GetValueFromSymbolstruct");
//        }
//        return "";
//    }
//    private String GetMarkerValue(String ValueNameOfValueYouWant, Object SymbolStructure)
//    {
//        String cReturn = "";
//        String cColor = "";
//        String cOutlineColor = "";
//        boolean bSwitch;
//        bSwitch = false; //(ben鰐igt f黵 Multilayersymbole)der Schalter wird umgelegt, wenn es kein simple.. Symbol gibt. Dann wird der Wert des ersten Symbols genommen
//        cReturn = "0"; //Wenn keiner der 黚ergebenen ValueNames passt, wird 0 zur點kgegeben
//        try
//        {
//            if (SymbolStructure instanceof ArcMapSymbols.StructSimpleMarkerSymbol)
//            {
//                ArcMapSymbols.StructSimpleMarkerSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructSimpleMarkerSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("WellKnownName")).toUpperCase()))
//                {
//                    if (objTempStruct.Style.equals("esriSMSCircle"))
//                    {
//                        cReturn = "circle";
//                    }
//                    else if (objTempStruct.Style.equals("esriSMSSquare"))
//                    {
//                        cReturn = "square";
//                    }
//                    else if (objTempStruct.Style.equals("esriSMSCross"))
//                    {
//                        cReturn = "cross";
//                    }
//                    else if (objTempStruct.Style.equals("esriSMSX"))
//                    {
//                        cReturn = "x";
//                    }
//                    else if (objTempStruct.Style.equals("esriSMSDiamond"))
//                    {
//                        cReturn = "triangle";
//                    }
//                    else
//                    {
//                        cReturn = "star";
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                {
//                    if (objTempStruct.Filled)
//                    {
//                        cReturn = String.valueOf(objTempStruct.Color);
//                    }
//                    else
//                    {
//                        cReturn = "";
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Size);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointRotation")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Angle);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineColor")).toUpperCase()))
//                {
//                    if (objTempStruct.Outline)
//                    {
//                        cReturn = String.valueOf(objTempStruct.OutlineColor);
//                    }
//                    else
//                    {
//                        cReturn = "";
//                    }
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.OutlineSize);
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructCharacterMarkerSymbol)
//            {
//                ArcMapSymbols.StructCharacterMarkerSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructCharacterMarkerSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("WellKnownName")).toUpperCase()))
//                {
//                    cReturn = "circle"; //Default
//                    if (objTempStruct.Font.toUpperCase().equals("ESRI DEFAULT MARKER"))
//                    {
//                        if ((((((((((((((((int) objTempStruct.CharacterIndex == 33) || ((int) objTempStruct.CharacterIndex == 40)) || ((int) objTempStruct.CharacterIndex == 46)) || ((int) objTempStruct.CharacterIndex == 53)) || (objTempStruct.CharacterIndex >= 60 && objTempStruct.CharacterIndex <= 67)) || ((int) objTempStruct.CharacterIndex == 72)) || (objTempStruct.CharacterIndex >= 79 && objTempStruct.CharacterIndex <= 82)) || (objTempStruct.CharacterIndex >= 90 && objTempStruct.CharacterIndex <= 93)) || ((int) objTempStruct.CharacterIndex == 171)) || ((int) objTempStruct.CharacterIndex == 172)) || ((int) objTempStruct.CharacterIndex == 183)) || ((int) objTempStruct.CharacterIndex == 196)) || ((int) objTempStruct.CharacterIndex == 199)) || ((int) objTempStruct.CharacterIndex == 200)) || ((int) objTempStruct.CharacterIndex == 8729))
//                        {
//                            cReturn = "circle";
//                        }
//                        else if (((((((((((((((((int) objTempStruct.CharacterIndex == 34) || ((int) objTempStruct.CharacterIndex == 41)) || ((int) objTempStruct.CharacterIndex == 47)) || ((int) objTempStruct.CharacterIndex == 54)) || ((int) objTempStruct.CharacterIndex == 74)) || ((int) objTempStruct.CharacterIndex == 83)) || ((int) objTempStruct.CharacterIndex == 84)) || ((int) objTempStruct.CharacterIndex == 104)) || ((int) objTempStruct.CharacterIndex == 174)) || ((int) objTempStruct.CharacterIndex == 175)) || ((int) objTempStruct.CharacterIndex == 179)) || ((int) objTempStruct.CharacterIndex == 190)) || ((int) objTempStruct.CharacterIndex == 192)) || ((int) objTempStruct.CharacterIndex == 194)) || ((int) objTempStruct.CharacterIndex == 198)) || ((int) objTempStruct.CharacterIndex == 201))
//                        {
//                            cReturn = "square";
//                        }
//                        else if (((((((((int) objTempStruct.CharacterIndex == 35) || ((int) objTempStruct.CharacterIndex == 42)) || ((int) objTempStruct.CharacterIndex == 48)) || ((int) objTempStruct.CharacterIndex == 55)) || ((int) objTempStruct.CharacterIndex == 73)) || ((int) objTempStruct.CharacterIndex == 86)) || ((int) objTempStruct.CharacterIndex == 184)) || ((int) objTempStruct.CharacterIndex == 185))
//                        {
//                            cReturn = "triangle";
//                        }
//                        else if ((int) objTempStruct.CharacterIndex == 68)
//                        {
//                            cReturn = "X";
//                        }
//                        else if (((((int) objTempStruct.CharacterIndex == 69) || ((int) objTempStruct.CharacterIndex == 70)) || ((int) objTempStruct.CharacterIndex == 71)) || (objTempStruct.CharacterIndex >= 203 && objTempStruct.CharacterIndex <= 211))
//                        {
//                            cReturn = "cross";
//                        }
//                        else if (((((((int) objTempStruct.CharacterIndex == 94) || ((int) objTempStruct.CharacterIndex == 95)) || ((int) objTempStruct.CharacterIndex == 96)) || ((int) objTempStruct.CharacterIndex == 106)) || ((int) objTempStruct.CharacterIndex == 107)) || ((int) objTempStruct.CharacterIndex == 108))
//                        {
//                            cReturn = "star";
//                        }
//                    }
//                    else if (objTempStruct.Font.toUpperCase().equals("ESRI IGL FONT22"))
//                    {
//                        if (((((objTempStruct.CharacterIndex >= 65 && objTempStruct.CharacterIndex <= 69) || (objTempStruct.CharacterIndex >= 93 && objTempStruct.CharacterIndex <= 96)) || ((int) objTempStruct.CharacterIndex == 103)) || ((int) objTempStruct.CharacterIndex == 105)) || ((int) objTempStruct.CharacterIndex == 106))
//                        {
//                            cReturn = "circle";
//                        }
//                        else if (((((int) objTempStruct.CharacterIndex == 70) || ((int) objTempStruct.CharacterIndex == 71)) || (objTempStruct.CharacterIndex >= 88 && objTempStruct.CharacterIndex <= 92)) || (objTempStruct.CharacterIndex >= 118 && objTempStruct.CharacterIndex <= 121))
//                        {
//                            cReturn = "square";
//                        }
//                        else if (((((((((int) objTempStruct.CharacterIndex == 72) || ((int) objTempStruct.CharacterIndex == 73)) || ((int) objTempStruct.CharacterIndex == 75)) || ((int) objTempStruct.CharacterIndex == 81)) || ((int) objTempStruct.CharacterIndex == 85)) || ((int) objTempStruct.CharacterIndex == 86)) || (objTempStruct.CharacterIndex >= 99 && objTempStruct.CharacterIndex <= 102)) || ((int) objTempStruct.CharacterIndex == 104))
//                        {
//                            cReturn = "triangle";
//                        }
//                        else if (objTempStruct.CharacterIndex >= 114 && objTempStruct.CharacterIndex <= 117)
//                        {
//                            cReturn = "X";
//                        }
//                    }
//                    else if (objTempStruct.Font.toUpperCase().equals("ESRI GEOMETRIC SYMBOLS"))
//                    {
//                        if ((((((((((((((((((((((objTempStruct.CharacterIndex >= 33 && objTempStruct.CharacterIndex <= 35) || (objTempStruct.CharacterIndex >= 39 && objTempStruct.CharacterIndex <= 41)) || ((int) objTempStruct.CharacterIndex == 47)) || ((int) objTempStruct.CharacterIndex == 48)) || (objTempStruct.CharacterIndex >= 56 && objTempStruct.CharacterIndex <= 58)) || ((int) objTempStruct.CharacterIndex == 65)) || (objTempStruct.CharacterIndex >= 68 && objTempStruct.CharacterIndex <= 71)) || (objTempStruct.CharacterIndex >= 74 && objTempStruct.CharacterIndex <= 77)) || ((int) objTempStruct.CharacterIndex == 82)) || ((int) objTempStruct.CharacterIndex == 83)) || (objTempStruct.CharacterIndex >= 86 && objTempStruct.CharacterIndex <= 89)) || (objTempStruct.CharacterIndex >= 92 && objTempStruct.CharacterIndex <= 95)) || (objTempStruct.CharacterIndex >= 98 && objTempStruct.CharacterIndex <= 101)) || (objTempStruct.CharacterIndex >= 104 && objTempStruct.CharacterIndex <= 107)) || (objTempStruct.CharacterIndex >= 110 && objTempStruct.CharacterIndex <= 113)) || (objTempStruct.CharacterIndex >= 116 && objTempStruct.CharacterIndex <= 125)) || ((int) objTempStruct.CharacterIndex == 161)) || ((int) objTempStruct.CharacterIndex == 171)) || (objTempStruct.CharacterIndex >= 177 && objTempStruct.CharacterIndex <= 186)) || ((int) objTempStruct.CharacterIndex == 244)) || (objTempStruct.CharacterIndex >= 246 && objTempStruct.CharacterIndex <= 249)) || ((int) objTempStruct.CharacterIndex == 8729))
//                        {
//                            cReturn = "circle";
//                        }
//                        else if ((((((((((((((((((((((((int) objTempStruct.CharacterIndex == 37) || ((int) objTempStruct.CharacterIndex == 42)) || ((int) objTempStruct.CharacterIndex == 43)) || ((int) objTempStruct.CharacterIndex == 50)) || ((int) objTempStruct.CharacterIndex == 55)) || ((int) objTempStruct.CharacterIndex == 67)) || ((int) objTempStruct.CharacterIndex == 73)) || ((int) objTempStruct.CharacterIndex == 79)) || ((int) objTempStruct.CharacterIndex == 85)) || ((int) objTempStruct.CharacterIndex == 91)) || ((int) objTempStruct.CharacterIndex == 97)) || ((int) objTempStruct.CharacterIndex == 103)) || ((int) objTempStruct.CharacterIndex == 109)) || ((int) objTempStruct.CharacterIndex == 115)) || ((int) objTempStruct.CharacterIndex == 170)) || ((int) objTempStruct.CharacterIndex == 172)) || (objTempStruct.CharacterIndex >= 200 && objTempStruct.CharacterIndex <= 205)) || ((int) objTempStruct.CharacterIndex == 208)) || ((int) objTempStruct.CharacterIndex == 209)) || ((int) objTempStruct.CharacterIndex == 210)) || (objTempStruct.CharacterIndex >= 226 && objTempStruct.CharacterIndex <= 241)) || ((int) objTempStruct.CharacterIndex == 243)) || ((int) objTempStruct.CharacterIndex == 250))
//                        {
//                            cReturn = "square";
//                        }
//                        else if (((((((((((((((((((((int) objTempStruct.CharacterIndex == 36) || ((int) objTempStruct.CharacterIndex == 46)) || ((int) objTempStruct.CharacterIndex == 49)) || ((int) objTempStruct.CharacterIndex == 66)) || ((int) objTempStruct.CharacterIndex == 72)) || ((int) objTempStruct.CharacterIndex == 78)) || ((int) objTempStruct.CharacterIndex == 84)) || ((int) objTempStruct.CharacterIndex == 90)) || ((int) objTempStruct.CharacterIndex == 96)) || ((int) objTempStruct.CharacterIndex == 102)) || ((int) objTempStruct.CharacterIndex == 108)) || ((int) objTempStruct.CharacterIndex == 114)) || ((int) objTempStruct.CharacterIndex == 162)) || ((int) objTempStruct.CharacterIndex == 168)) || ((int) objTempStruct.CharacterIndex == 169)) || ((int) objTempStruct.CharacterIndex == 175)) || ((int) objTempStruct.CharacterIndex == 176)) || (objTempStruct.CharacterIndex >= 186 && objTempStruct.CharacterIndex <= 190)) || (objTempStruct.CharacterIndex >= 213 && objTempStruct.CharacterIndex <= 220)) || ((int) objTempStruct.CharacterIndex == 245))
//                        {
//                            cReturn = "triangle";
//                        }
//                        else if (((objTempStruct.CharacterIndex >= 195 && objTempStruct.CharacterIndex <= 199) || ((int) objTempStruct.CharacterIndex == 206)) || ((int) objTempStruct.CharacterIndex == 207))
//                        {
//                            cReturn = "X";
//                        }
//                    }
//                }
//                else if ((ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase())) || (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineColor")).toUpperCase())))
//                {
//                    cColor = String.valueOf(objTempStruct.Color); //Default
//                    cOutlineColor = ""; //Default
//                    if (objTempStruct.Font.toUpperCase().equals("ESRI DEFAULT MARKER"))
//                    {
//                        if ((((((((((((((((((((((((((objTempStruct.CharacterIndex >= 33 && objTempStruct.CharacterIndex <= 39) || (objTempStruct.CharacterIndex >= 67 && objTempStruct.CharacterIndex <= 69)) || ((int) objTempStruct.CharacterIndex == 71)) || ((int) objTempStruct.CharacterIndex == 81)) || ((int) objTempStruct.CharacterIndex == 88)) || (objTempStruct.CharacterIndex >= 97 && objTempStruct.CharacterIndex <= 103)) || ((int) objTempStruct.CharacterIndex == 107)) || ((int) objTempStruct.CharacterIndex == 113)) || ((int) objTempStruct.CharacterIndex == 116)) || ((int) objTempStruct.CharacterIndex == 118)) || ((int) objTempStruct.CharacterIndex == 161)) || ((int) objTempStruct.CharacterIndex == 163)) || ((int) objTempStruct.CharacterIndex == 165)) || ((int) objTempStruct.CharacterIndex == 167)) || ((int) objTempStruct.CharacterIndex == 168)) || ((int) objTempStruct.CharacterIndex == 172)) || ((int) objTempStruct.CharacterIndex == 174)) || ((int) objTempStruct.CharacterIndex == 175)) || ((int) objTempStruct.CharacterIndex == 179)) || (objTempStruct.CharacterIndex >= 182 && objTempStruct.CharacterIndex <= 186)) || ((int) objTempStruct.CharacterIndex == 190)) || (objTempStruct.CharacterIndex >= 192 && objTempStruct.CharacterIndex <= 201)) || (objTempStruct.CharacterIndex >= 203 && objTempStruct.CharacterIndex <= 211)) || ((int) objTempStruct.CharacterIndex == 215)) || ((int) objTempStruct.CharacterIndex == 219)) || ((int) objTempStruct.CharacterIndex == 8729))
//                        {
//                            cColor = String.valueOf(objTempStruct.Color);
//                            cOutlineColor = "";
//                        }
//                        else
//                        {
//                            cColor = "";
//                            cOutlineColor = String.valueOf(objTempStruct.Color);
//                        }
//                    }
//                    else if (objTempStruct.Font.toUpperCase().equals("ESRI IGL FONT22"))
//                    {
//                        if (((objTempStruct.CharacterIndex >= 72 && objTempStruct.CharacterIndex <= 80) || ((int) objTempStruct.CharacterIndex == 100)) || (objTempStruct.CharacterIndex >= 118 && objTempStruct.CharacterIndex <= 121))
//                        {
//                            cColor = String.valueOf(objTempStruct.Color);
//                            cOutlineColor = "";
//                        }
//                        else
//                        {
//                            cColor = "";
//                            cOutlineColor = String.valueOf(objTempStruct.Color);
//                        }
//                    }
//                    else if (objTempStruct.Font.toUpperCase().equals("ESRI GEOMETRIC SYMBOLS"))
//                    {
//                        if (((((((((((objTempStruct.CharacterIndex >= 34 && objTempStruct.CharacterIndex <= 40) || ((int) objTempStruct.CharacterIndex == 120)) || (objTempStruct.CharacterIndex >= 161 && objTempStruct.CharacterIndex <= 167)) || ((int) objTempStruct.CharacterIndex == 187)) || ((int) objTempStruct.CharacterIndex == 188)) || (objTempStruct.CharacterIndex >= 194 && objTempStruct.CharacterIndex <= 200)) || (objTempStruct.CharacterIndex >= 202 && objTempStruct.CharacterIndex <= 215)) || ((int) objTempStruct.CharacterIndex == 217)) || ((int) objTempStruct.CharacterIndex == 218)) || (objTempStruct.CharacterIndex >= 221 && objTempStruct.CharacterIndex <= 229)) || (objTempStruct.CharacterIndex >= 231 && objTempStruct.CharacterIndex <= 249))
//                        {
//                            cColor = String.valueOf(objTempStruct.Color);
//                            cOutlineColor = "";
//                        }
//                        else
//                        {
//                            cColor = "";
//                            cOutlineColor = String.valueOf(objTempStruct.Color);
//                        }
//                    }
//                    if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                    {
//                        cReturn = cColor;
//                    }
//                    else
//                    {
//                        cReturn = cOutlineColor;
//                    }
//                    return cReturn;
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Size);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointRotation")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Angle);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineSize")).toUpperCase()))
//                {
//                    cReturn = "1";
//                    if ( objTempStruct.Font.toUpperCase().equals("ESRI GEOMETRIC SYMBOLS"))
//                    {
//                        if ((((((((((int) objTempStruct.CharacterIndex == 85) || ((int) objTempStruct.CharacterIndex == 88)) || ((int) objTempStruct.CharacterIndex == 89)) || ((int) objTempStruct.CharacterIndex == 91)) || ((int) objTempStruct.CharacterIndex == 94)) || ((int) objTempStruct.CharacterIndex == 95)) || ((int) objTempStruct.CharacterIndex == 97)) || ((int) objTempStruct.CharacterIndex == 98)) || ((int) objTempStruct.CharacterIndex == 100))
//                        {
//                            cReturn = "2";
//                        }
//                        else if (((((((((((int) objTempStruct.CharacterIndex == 41) || (objTempStruct.CharacterIndex >= 65 && objTempStruct.CharacterIndex <= 83)) || (objTempStruct.CharacterIndex >= 122 && objTempStruct.CharacterIndex <= 125)) || ((int) objTempStruct.CharacterIndex == 168)) || ((int) objTempStruct.CharacterIndex == 169)) || ((int) objTempStruct.CharacterIndex == 188)) || ((int) objTempStruct.CharacterIndex == 189)) || ((int) objTempStruct.CharacterIndex == 216)) || ((int) objTempStruct.CharacterIndex == 230)) || ((int) objTempStruct.CharacterIndex == 250))
//                        {
//                            cReturn = "3";
//                        }
//                        else if ((((int) objTempStruct.CharacterIndex == 161) || (objTempStruct.CharacterIndex >= 170 && objTempStruct.CharacterIndex <= 178)) || ((int) objTempStruct.CharacterIndex == 186))
//                        {
//                            cReturn = "4";
//                        }
//                    }
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructPictureMarkerSymbol)
//            {
//                ArcMapSymbols.StructPictureMarkerSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructPictureMarkerSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("WellKnownName")).toUpperCase()))
//                {
//                    cReturn = "circle"; //TODO
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.BackgroundColor);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Size);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointRotation")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Angle);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineColor")).toUpperCase()))
//                {
//                    cReturn = ""; //TODO
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineSize")).toUpperCase()))
//                {
//                    cReturn = "0"; //TODO
//                }
//                return cReturn;
//            }
//            else if (SymbolStructure instanceof ArcMapSymbols.StructArrowMarkerSymbol)
//            {
//                ArcMapSymbols.StructArrowMarkerSymbol objTempStruct = null;
//                objTempStruct = (ArcMapSymbols.StructArrowMarkerSymbol) SymbolStructure;
//                if (ValueNameOfValueYouWant.toUpperCase().equals((new String("WellKnownName")).toUpperCase()))
//                {
//                    cReturn = "triangle";
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointColor")).toUpperCase()))
//                {
//                    cReturn = String.valueOf(objTempStruct.Color);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointSize")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Size);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointRotation")).toUpperCase()))
//                {
//                    cReturn = CommaToPoint(objTempStruct.Angle);
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineColor")).toUpperCase()))
//                {
//                    cReturn = ""; //Never an outline
//                }
//                else if (ValueNameOfValueYouWant.toUpperCase().equals((new String("PointOutlineSize")).toUpperCase()))
//                {
//                    cReturn = "0"; //Never an outline
//                }
//                return cReturn;
//            }
//        }
//        catch (RuntimeException ex)
//        {
//            ErrorMsg("Konnte den Wert aus der SymbolStruct nicht auswerten.", ex.getMessage(), ex.getStackTrace(), "GetValueFromSymbolstruct");
//        }
//        return cReturn;
//    }
//    private String CommaToPoint(double value)
//    {
//        String cReturn = "";
//        cReturn = (new Double(value)).toString();
//        cReturn = cReturn.replace(",", ".");
//        return cReturn;
//    }
//    private String CommaToPoint(String value)
//    {
//        String cReturn = "";
//        cReturn = value;
//        cReturn = cReturn.replace(",", ".");
//        return cReturn;
//    }
//    private Object ErrorMsg(String message, String exMessage, Object stack, String functionname)
//    {
////        JOptionPane.showConfirmDialog(null, message +"." + "\r\n" + exMessage + "\r\n" + stack, "ArcGIS_SLD_Converter | Output_SLD | " + functionname, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
//       return MyTermination();
//    }
//
//    //************************************************************************************************
//    //********************************* Die zentrale Infomeldung ***********************************
//    //************************************************************************************************
//    private Object InfoMsg(String message, String functionname)
//    {
////        JOptionPane.showConfirmDialog(null, message, "ArcGIS_SLD_Converter | Output_SLD | " + functionname, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
//      return  null;
//    }
//
//    public final Object MyTermination()
//    {
//        m_objData.MyTermination();
//        return null;
//    }
//
//
//}