package service;
import com.esri.arcgis.arcmapui.IMxApplication;
import com.esri.arcgis.arcmapui.IMxDocument;
import com.esri.arcgis.carto.*;
import com.esri.arcgis.display.*;
import com.esri.arcgis.framework.AppROT;
import com.esri.arcgis.framework.IAppROT;
import com.esri.arcgis.framework.IApplication;
import com.esri.arcgis.framework.IObjectFactory;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.support.ms.stdole.IEnumVARIANT;
import com.esri.arcgis.support.ms.stdole.IPicture;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArcMapSymbols
{
    private IMxDocument m_ObjDoc;
    private IApplication m_ObjApp;
    private IAppROT m_ObjAppROT;
    private IObjectFactory m_ObjObjectCreator;
    private IMap m_ObjMap;
    private MainFormProcess mainFormProcess;
    public StructProject m_StrProject;
    private java.util.ArrayList m_al1;
    private java.util.ArrayList m_al2;
    private java.util.ArrayList m_al3;
    private List<ArrayList> m_alClassifiedFields;
    private String m_cFilename;
    //Die FeatureClass wird hier festgelegt; (ob Punkt-, Linien-, oder Polygonfeature)
    public enum FeatureClass
    {
        PointFeature(0),
        LineFeature(1),
        PolygonFeature(2);

        private int intValue;
        private static java.util.HashMap<Integer, FeatureClass> mappings;
        private synchronized static java.util.HashMap<Integer, FeatureClass> getMappings()
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, FeatureClass>();
            }
            return mappings;
        }

        private FeatureClass(int value)
        {
            intValue = value;
            FeatureClass.getMappings().put(value, this);
        }

        public int getValue()
        {
            return intValue;
        }

        public static FeatureClass forValue(int value)
        {
            return getMappings().get(value);
        }
    }

    public enum MarkerStructs
    {
        StructSimpleMarkerSymbol,
        StructCharacterMarkerSymbol,
        StructPictureMarkerSymbol,
        StructArrowMarkerSymbol,
        StructMultilayerMarkerSymbol;

        public int getValue()
        {
            return this.ordinal();
        }

        public static MarkerStructs forValue(int value)
        {
            return values()[value];
        }
    }

    public enum LineStructs
    {
        StructSimpleLineSymbol(0),
        StructMarkerLineSymbol(1),
        StructHashLineSymbol(2),
        StructPictureLineSymbol(3),
        StructMultilayerLineSymbol(4),
        StructCartographicLineSymbol(5);

        private int intValue;
        private static java.util.HashMap<Integer, LineStructs> mappings;
        private synchronized static java.util.HashMap<Integer, LineStructs> getMappings()
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, LineStructs>();
            }
            return mappings;
        }

        private LineStructs(int value)
        {
            intValue = value;
            LineStructs.getMappings().put(value, this);
        }

        public int getValue()
        {
            return intValue;
        }

        public static LineStructs forValue(int value)
        {
            return getMappings().get(value);
        }
    }
    public final static class StructProject
    {
        public List<Object> LayerList; //Hier stecken alle Layer als StructLayer-Sammlung drin
        public int LayerCount; //Anzahl der Layer

        public StructProject clone()
        {
            StructProject varCopy = new StructProject();

            varCopy.LayerList = this.LayerList;
            varCopy.LayerCount = this.LayerCount;

            return varCopy;
        }
    }
    public final static class StructUniqueValueRenderer
    {
        public FeatureClass FeatureCls = FeatureClass.forValue(0); //Ob Punkt-, Linien-, oder Polygonfeature
        public String LayerName; //Der Layername (ist nicht der Name, nach dem klassifiziert wird)
        public String DatasetName; //Der Datasetname (Der Name, nach dem klassifiziert wird)
        public int ValueCount; //Die Anzahl der Wertefelder bzw. Symbole des Layers basierend auf der aktuellen Klassifizierung
        public List<Object> SymbolList; //Die Sammlung der einzelnen Attributauspr鋑ungen (Symbole) als Struct*Symbol...
        public int FieldCount; //Die Anzahl der Tabellenfelder, nach denen klassifiziert wird (0: kein Feld; 1 Feld; 2 Felder; 3 Felder max.)
        public List<String> FieldNames; //Die Tabellenfelder, nach denen Klassifiziert wird (max. 3 Felder) als Strings
        public String StylePath; //Der Pfad zur Stildefinition oder Stildatei (entspr. Layer Propertys->Symbology->Categorys->Match to Symbol in a Style in ArcMap!)
        public StructAnnotation Annotation; //Annotation label based on feature

        public StructUniqueValueRenderer clone()
        {
            StructUniqueValueRenderer varCopy = new StructUniqueValueRenderer();

            varCopy.FeatureCls = this.FeatureCls;
            varCopy.LayerName = this.LayerName;
            varCopy.DatasetName = this.DatasetName;
            varCopy.ValueCount = this.ValueCount;
            varCopy.SymbolList = this.SymbolList;
            varCopy.FieldCount = this.FieldCount;
            varCopy.FieldNames = this.FieldNames;
            varCopy.StylePath = this.StylePath;
            varCopy.Annotation = this.Annotation.clone();

            return varCopy;
        }
    }
    public final static class StructClassBreaksRenderer
    {
        public FeatureClass FeatureCls = FeatureClass.forValue(0); //Ob Punkt-, Linien-, oder Polygonfeature
        public String LayerName; //Der Layername (ist nicht der Name, nach dem klassifiziert wird)
        public String DatasetName; //Der Datasetname (Der Name, nach dem klassifiziert wird)
        public int BreakCount; //Die Anzahl der Wertefelder bzw. Symbole des Layers basierend auf der aktuellen Klassifizierung
        public String FieldName; //Das Tabellenfeld, nach dem Klassifiziert wird
        public String NormFieldName; //Das Tabellenfeld, nach dem normalisiert wird
        public List<Object> SymbolList; //Die Sammlung der einzelnen Attributauspr鋑ungen (Symbole) als Struct*Symbol...
        public StructAnnotation Annotation; //Annotation label based on feature

        public StructClassBreaksRenderer clone()
        {
            StructClassBreaksRenderer varCopy = new StructClassBreaksRenderer();

            varCopy.FeatureCls = this.FeatureCls;
            varCopy.LayerName = this.LayerName;
            varCopy.DatasetName = this.DatasetName;
            varCopy.BreakCount = this.BreakCount;
            varCopy.FieldName = this.FieldName;
            varCopy.NormFieldName = this.NormFieldName;
            varCopy.SymbolList = this.SymbolList;
            varCopy.Annotation = this.Annotation.clone();

            return varCopy;
        }
    }
    public final static class StructSimpleRenderer
    {
        public FeatureClass FeatureCls = FeatureClass.forValue(0); //Ob Punkt-, Linien-, oder Polygonfeature
        public String LayerName; //Der Layername (ist nicht der Name, nach dem klassifiziert wird)
        public String DatasetName; //Der Datasetname (Der Name, nach dem klassifiziert wird)
        public List<Object> SymbolList; //Die Sammlung der einzelnen Attributauspr鋑ungen (Symbole) als Struct*Symbol... In diesem Fall jew nur 1 Symbol
        public StructAnnotation Annotation; //Annotation label based on feature

        public StructSimpleRenderer clone()
        {
            StructSimpleRenderer varCopy = new StructSimpleRenderer();

            varCopy.FeatureCls = this.FeatureCls;
            varCopy.LayerName = this.LayerName;
            varCopy.DatasetName = this.DatasetName;
            varCopy.SymbolList = this.SymbolList;
            varCopy.Annotation = this.Annotation.clone();

            return varCopy;
        }
    }
    public final static class StructSimpleMarkerSymbol
    {
        public double Angle; //Der Winkel des Symbols
        public boolean Filled; //Whether fill color or not
        public String Color; //Die Farbe des Symbols in der Webschreibweise als #ByteByteByte
        public boolean Outline; //Outline des Symbols
        public String OutlineColor; //Die Farbe der Outline in der Webschreibweise als #ByteByteByte
        public double OutlineSize; //die Dicke der Outline
        public double Size; //Die Symbolgr鲞e
        public String Style; //der esriSimpleMarkerStyle
        public double XOffset; //der Offset zur Koordinate des Sy<mbols
        public double YOffset; //der Offset zur Koordinate des Sy<mbols
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructSimpleMarkerSymbol clone()
        {
            StructSimpleMarkerSymbol varCopy = new StructSimpleMarkerSymbol();

            varCopy.Angle = this.Angle;
            varCopy.Filled = this.Filled;
            varCopy.Color = this.Color;
            varCopy.Outline = this.Outline;
            varCopy.OutlineColor = this.OutlineColor;
            varCopy.OutlineSize = this.OutlineSize;
            varCopy.Size = this.Size;
            varCopy.Style = this.Style;
            varCopy.XOffset = this.XOffset;
            varCopy.YOffset = this.YOffset;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructCharacterMarkerSymbol
    {
        public double Angle; //Winkel des "Buchstabens"
        public int CharacterIndex; //Stelle des Buchstabens in der ASCII (ANSII)-Tabelle des zugeh鰎igen Fonts
        public String Color; //Die Farbe
        public String Font; //Der Font des Zeichens (Buchstabens)
        public double Size; //Die Schriftgr鲞e
        public double XOffset; //der Offset zur Koordinate des Sy<mbols
        public double YOffset; //der Offset zur Koordinate des Sy<mbols
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructCharacterMarkerSymbol clone()
        {
            StructCharacterMarkerSymbol varCopy = new StructCharacterMarkerSymbol();

            varCopy.Angle = this.Angle;
            varCopy.CharacterIndex = this.CharacterIndex;
            varCopy.Color = this.Color;
            varCopy.Font = this.Font;
            varCopy.Size = this.Size;
            varCopy.XOffset = this.XOffset;
            varCopy.YOffset = this.YOffset;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructPictureMarkerSymbol
    {
        public double Angle; //Winkel
        public String BackgroundColor; //Die Farbe des BGColor in der Webschreibweise als #ByteByteByte
        public String Color; //Keine Ahnung welche Farbe das ist
        public IPicture Picture; //Das Bild
        public double Size; //Die Bildgr鲞e
        public double XOffset; //der Offset zur Koordinate des Sy<mbols
        public double YOffset; //der Offset zur Koordinate des Sy<mbols
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructPictureMarkerSymbol clone()
        {
            StructPictureMarkerSymbol varCopy = new StructPictureMarkerSymbol();

            varCopy.Angle = this.Angle;
            varCopy.BackgroundColor = this.BackgroundColor;
            varCopy.Color = this.Color;
            varCopy.Picture = this.Picture;
            varCopy.Size = this.Size;
            varCopy.XOffset = this.XOffset;
            varCopy.YOffset = this.YOffset;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructArrowMarkerSymbol
    {
        public double Angle; //Der Winkel
        public String Color; //Die Farbe
        public double Length; //Die Pfeill鋘ge
        public double Size; //Die Die Pfeilgr鲞e
        public String Style; //Der Esrieigene Pfeilstyle
        public double Width; //Die Pfeilbreite
        public double XOffset; //der Offset zur Koordinate des Sy<mbols
        public double YOffset; //der Offset zur Koordinate des Sy<mbols
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructArrowMarkerSymbol clone()
        {
            StructArrowMarkerSymbol varCopy = new StructArrowMarkerSymbol();

            varCopy.Angle = this.Angle;
            varCopy.Color = this.Color;
            varCopy.Length = this.Length;
            varCopy.Size = this.Size;
            varCopy.Style = this.Style;
            varCopy.Width = this.Width;
            varCopy.XOffset = this.XOffset;
            varCopy.YOffset = this.YOffset;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }

    public final static class StructSimpleLineSymbol
    {
        public String Color; //Die Farbe des Symbols in der Webschreibweise als #ByteByteByte
        public byte Transparency; //Transparancy of the color.
        public String publicStyle; //der esriSimpleLineStyle
        public double Width; //die Linienbreite
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructSimpleLineSymbol clone()
        {
            StructSimpleLineSymbol varCopy = new StructSimpleLineSymbol();

            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.publicStyle = this.publicStyle;
            varCopy.Width = this.Width;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }

    public final static class StructCartographicLineSymbol
    {
        public String Color; //Die Farbe des Symbols in der Webschreibweise als #ByteByteByte
        public byte Transparency; //Transparancy of the color.
        public double Width; //Die Strichbreite
        public String Join; //sagt aus, wie die Linien verbunden sind
        public double MiterLimit; //Schwellenwert, ab welchem Abstand Zwickel angezeigt werden
        public String Cap; //Die Form des Linienendes
        public java.util.ArrayList DashArray; //The dasharray ("Template" in terms of ESRI) for dashed lines
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructCartographicLineSymbol clone()
        {
            StructCartographicLineSymbol varCopy = new StructCartographicLineSymbol();

            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Width = this.Width;
            varCopy.Join = this.Join;
            varCopy.MiterLimit = this.MiterLimit;
            varCopy.Cap = this.Cap;
            varCopy.DashArray = this.DashArray;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructHashLineSymbol
    {
        public double Angle; //Der Winkel
        public String Color; //Die Farbe der Linie
        public byte Transparency; //Transparancy of the color.
        public double Width; //der Abstand der einzelnen Striche
        //____________________________________________________
        public LineStructs kindOfLineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol HashSymbol_SimpleLine;
        public StructCartographicLineSymbol HashSymbol_CartographicLine;
        public StructMarkerLineSymbol HashSymbol_MarkerLine;
        //Public HashSymbol_HashLine as StructHashLineSymbol        geht nicht,da eine Struktur sich nicht selbst enthalten kann (s. StructHashLineSymbol)
        public StructPictureLineSymbol HashSymbol_PictureLine;
        public StructMultilayerLineSymbol HashSymbol_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructHashLineSymbol clone()
        {
            StructHashLineSymbol varCopy = new StructHashLineSymbol();

            varCopy.Angle = this.Angle;
            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Width = this.Width;
            varCopy.kindOfLineStruct = this.kindOfLineStruct;
            varCopy.HashSymbol_SimpleLine = this.HashSymbol_SimpleLine.clone();
            varCopy.HashSymbol_CartographicLine = this.HashSymbol_CartographicLine.clone();
            varCopy.HashSymbol_MarkerLine = this.HashSymbol_MarkerLine.clone();
            varCopy.HashSymbol_PictureLine = this.HashSymbol_PictureLine.clone();
            varCopy.HashSymbol_MultiLayerLines = this.HashSymbol_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructMarkerLineSymbol
    {
        public String Color; //Die Farbe
        public byte Transparency; //Transparancy of the color.
        public double Width; //Der Abstand der einzelnen Marker
        //____________________________________________________
        public MarkerStructs kindOfMarkerStruct = MarkerStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleMarkerSymbol MarkerSymbol_SimpleMarker;
        public StructCharacterMarkerSymbol MarkerSymbol_CharacterMarker;
        public StructPictureMarkerSymbol MarkerSymbol_PictureMarker;
        public StructArrowMarkerSymbol MarkerSymbol_ArrowMarker;
        public StructMultilayerMarkerSymbol MarkerSymbol_MultilayerMarker;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructMarkerLineSymbol clone()
        {
            StructMarkerLineSymbol varCopy = new StructMarkerLineSymbol();

            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Width = this.Width;
            varCopy.kindOfMarkerStruct = this.kindOfMarkerStruct;
            varCopy.MarkerSymbol_SimpleMarker = this.MarkerSymbol_SimpleMarker.clone();
            varCopy.MarkerSymbol_CharacterMarker = this.MarkerSymbol_CharacterMarker.clone();
            varCopy.MarkerSymbol_PictureMarker = this.MarkerSymbol_PictureMarker.clone();
            varCopy.MarkerSymbol_ArrowMarker = this.MarkerSymbol_ArrowMarker.clone();
            varCopy.MarkerSymbol_MultilayerMarker = this.MarkerSymbol_MultilayerMarker.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructPictureLineSymbol
    {
        public String BackgroundColor; //Hintergrundfarbe
        public byte BackgroundTransparency; //Transparancy of the color.
        public String Color; //?
        public byte Transparency; //Transparancy of the color.
        public double Offset; //?
        public IPicture Picture; //Das Bild
        public boolean Rotate; //Bild rotieren
        public double Width; //Gr鲞e
        public double XScale; //Seitenverh鋖tnis X
        public double YScale; //Seitenverh鋖tnis Y
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructPictureLineSymbol clone()
        {
            StructPictureLineSymbol varCopy = new StructPictureLineSymbol();

            varCopy.BackgroundColor = this.BackgroundColor;
            varCopy.BackgroundTransparency = this.BackgroundTransparency;
            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Offset = this.Offset;
            varCopy.Picture = this.Picture;
            varCopy.Rotate = this.Rotate;
            varCopy.Width = this.Width;
            varCopy.XScale = this.XScale;
            varCopy.YScale = this.YScale;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructSimpleFillSymbol
    {
        public String Color; //Die Farbe des Fillsymbols
        public String Style; //esriSimpleFillStyle
        public byte Transparency;
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructSimpleFillSymbol clone()
        {
            StructSimpleFillSymbol varCopy = new StructSimpleFillSymbol();

            varCopy.Color = this.Color;
            varCopy.Style = this.Style;
            varCopy.Transparency = this.Transparency;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructMarkerFillSymbol
    {
        public String Color; //Die F黮lfarbe
        public byte Transparency;
        public double GridAngle; //der Winkel des Grids
        //____________________________________________________
        public MarkerStructs kindOfMarkerStruct = MarkerStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleMarkerSymbol MarkerSymbol_SimpleMarker;
        public StructCharacterMarkerSymbol MarkerSymbol_CharacterMarker;
        public StructPictureMarkerSymbol MarkerSymbol_PictureMarker;
        public StructArrowMarkerSymbol MarkerSymbol_ArrowMarker;
        public StructMultilayerMarkerSymbol MarkerSymbol_MultilayerMarker;
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructMarkerFillSymbol clone()
        {
            StructMarkerFillSymbol varCopy = new StructMarkerFillSymbol();

            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.GridAngle = this.GridAngle;
            varCopy.kindOfMarkerStruct = this.kindOfMarkerStruct;
            varCopy.MarkerSymbol_SimpleMarker = this.MarkerSymbol_SimpleMarker.clone();
            varCopy.MarkerSymbol_CharacterMarker = this.MarkerSymbol_CharacterMarker.clone();
            varCopy.MarkerSymbol_PictureMarker = this.MarkerSymbol_PictureMarker.clone();
            varCopy.MarkerSymbol_ArrowMarker = this.MarkerSymbol_ArrowMarker.clone();
            varCopy.MarkerSymbol_MultilayerMarker = this.MarkerSymbol_MultilayerMarker.clone();
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructLineFillSymbol
    {
        public double Angle; //Der Linienwinkel
        public String Color; //Die Hintergrundfarbe
        public byte Transparency;
        public double Offset; //der Linienversatz bei z.B. versch.-farbigen gegeneinander verschobenen Linien
        public double Separation; //Der Linienabstand
        //____________________________________________________
        public LineStructs kindOfLineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol LineSymbol_SimpleLine;
        public StructCartographicLineSymbol LineSymbol_CartographicLine;
        public StructMarkerLineSymbol LineSymbol_MarkerLine;
        public StructHashLineSymbol LineSymbol_HashLine;
        public StructPictureLineSymbol LineSymbol_PictureLine;
        public StructMultilayerLineSymbol LineSymbol_MultiLayerLines;
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructLineFillSymbol clone()
        {
            StructLineFillSymbol varCopy = new StructLineFillSymbol();

            varCopy.Angle = this.Angle;
            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Offset = this.Offset;
            varCopy.Separation = this.Separation;
            varCopy.kindOfLineStruct = this.kindOfLineStruct;
            varCopy.LineSymbol_SimpleLine = this.LineSymbol_SimpleLine.clone();
            varCopy.LineSymbol_CartographicLine = this.LineSymbol_CartographicLine.clone();
            varCopy.LineSymbol_MarkerLine = this.LineSymbol_MarkerLine.clone();
            varCopy.LineSymbol_HashLine = this.LineSymbol_HashLine.clone();
            varCopy.LineSymbol_PictureLine = this.LineSymbol_PictureLine.clone();
            varCopy.LineSymbol_MultiLayerLines = this.LineSymbol_MultiLayerLines.clone();
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructDotDensityFillSymbol
    {
        public String BackgroundColor; //Die Hintergrundfarbe
        public byte BackgroundTransparency; //Transparancy of the color.
        public String Color;
        public byte Transparency;
        public int DotCount; //Anzahl der Punkte
        public double DotSize; //Symbolgr鲞e
        public double DotSpacing; //Distanz zwischen den Mittelpunkten der Symbole
        public boolean FixedPlacement; //Bei true, werdfen die Symbole immer an der gleichen Stelle plaziert, andernfalls random verteilt
        public java.util.ArrayList SymbolList; //Das Array mit den einzelnen Marker-Symbolen des DotDensityFillSymbol
        public int SymbolCount; //Die Anzahl der unterschiedlichen Symbole
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructDotDensityFillSymbol clone()
        {
            StructDotDensityFillSymbol varCopy = new StructDotDensityFillSymbol();

            varCopy.BackgroundColor = this.BackgroundColor;
            varCopy.BackgroundTransparency = this.BackgroundTransparency;
            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.DotCount = this.DotCount;
            varCopy.DotSize = this.DotSize;
            varCopy.DotSpacing = this.DotSpacing;
            varCopy.FixedPlacement = this.FixedPlacement;
            varCopy.SymbolList = this.SymbolList;
            varCopy.SymbolCount = this.SymbolCount;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructPictureFillSymbol
    {
        public double Angle; //Der Winkel der Bilddrehung
        public String BackgroundColor; //die Hintergrundfarbe
        public byte BackgroundTransparency; //Transparancy of the color.
        public String Color;
        public byte Transparency;
        //        public IPictureDisp Picture; //Das Bild
        public double XScale; //Seitenverh鋖tnis X
        public double YScale; //Seitenverh鋖tnis Y
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructPictureFillSymbol clone()
        {
            StructPictureFillSymbol varCopy = new StructPictureFillSymbol();

            varCopy.Angle = this.Angle;
            varCopy.BackgroundColor = this.BackgroundColor;
            varCopy.BackgroundTransparency = this.BackgroundTransparency;
            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
//            varCopy.Picture = this.Picture;
            varCopy.XScale = this.XScale;
            varCopy.YScale = this.YScale;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructGradientFillSymbol
    {
        public String Color;
        public byte Transparency;
        public java.util.ArrayList Colors; //die einzelnen Farben der ColorRamp as string
        public double GradientAngle; //Neigungswinkel f黵 das F黮lmuster
        public double GradientPercentage; //Zahl zw. 0 und 1. 1=Farbverlauf 黚er die gesamte Fl鋍he. 0.5=H鋖fte der Fl鋍he wird mit Farbverlauf dargestellt
        public int IntervallCount; //Anzahl der Farben im Farbverlauf
        public String Style; //der esriGradientFillStyle
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructGradientFillSymbol clone()
        {
            StructGradientFillSymbol varCopy = new StructGradientFillSymbol();

            varCopy.Color = this.Color;
            varCopy.Transparency = this.Transparency;
            varCopy.Colors = this.Colors;
            varCopy.GradientAngle = this.GradientAngle;
            varCopy.GradientPercentage = this.GradientPercentage;
            varCopy.IntervallCount = this.IntervallCount;
            varCopy.Style = this.Style;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }

    public final static class StructBarChartSymbol
    {
        public boolean ShowAxes; //...
        public double Spacing; //Der Platz zwischen den Balken
        public boolean VerticalBars; //...
        public double Width; //...
        //____________________________________________________
        public LineStructs kindOfAxeslineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Axes_SimpleLine;
        public StructCartographicLineSymbol Axes_CartographicLine;
        public StructMarkerLineSymbol Axes_MarkerLine;
        public StructHashLineSymbol Axes_HashLine;
        public StructPictureLineSymbol Axes_PictureLine;
        public StructMultilayerLineSymbol Axes_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructBarChartSymbol clone()
        {
            StructBarChartSymbol varCopy = new StructBarChartSymbol();

            varCopy.ShowAxes = this.ShowAxes;
            varCopy.Spacing = this.Spacing;
            varCopy.VerticalBars = this.VerticalBars;
            varCopy.Width = this.Width;
            varCopy.kindOfAxeslineStruct = this.kindOfAxeslineStruct;
            varCopy.Axes_SimpleLine = this.Axes_SimpleLine.clone();
            varCopy.Axes_CartographicLine = this.Axes_CartographicLine.clone();
            varCopy.Axes_MarkerLine = this.Axes_MarkerLine.clone();
            varCopy.Axes_HashLine = this.Axes_HashLine.clone();
            varCopy.Axes_PictureLine = this.Axes_PictureLine.clone();
            varCopy.Axes_MultiLayerLines = this.Axes_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructPieChartSymbol
    {
        public boolean Clockwise; //Uhrzeigersinn oder nicht
        public boolean UseOutline; //...
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructPieChartSymbol clone()
        {
            StructPieChartSymbol varCopy = new StructPieChartSymbol();

            varCopy.Clockwise = this.Clockwise;
            varCopy.UseOutline = this.UseOutline;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructStackedChartSymbol
    {
        public boolean Fixed; //...
        public boolean UseOutline; //...
        public boolean VerticalBar;
        public double Width; //...
        //____________________________________________________
        public LineStructs kindOfOutlineStruct = LineStructs.forValue(0); //steht drin, welcher struct im jeweiligen Fall benutzt wurde
        public StructSimpleLineSymbol Outline_SimpleLine;
        public StructCartographicLineSymbol Outline_CartographicLine;
        public StructMarkerLineSymbol Outline_MarkerLine;
        public StructHashLineSymbol Outline_HashLine;
        public StructPictureLineSymbol Outline_PictureLine;
        public StructMultilayerLineSymbol Outline_MultiLayerLines;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructStackedChartSymbol clone()
        {
            StructStackedChartSymbol varCopy = new StructStackedChartSymbol();

            varCopy.Fixed = this.Fixed;
            varCopy.UseOutline = this.UseOutline;
            varCopy.VerticalBar = this.VerticalBar;
            varCopy.Width = this.Width;
            varCopy.kindOfOutlineStruct = this.kindOfOutlineStruct;
            varCopy.Outline_SimpleLine = this.Outline_SimpleLine.clone();
            varCopy.Outline_CartographicLine = this.Outline_CartographicLine.clone();
            varCopy.Outline_MarkerLine = this.Outline_MarkerLine.clone();
            varCopy.Outline_HashLine = this.Outline_HashLine.clone();
            varCopy.Outline_PictureLine = this.Outline_PictureLine.clone();
            varCopy.Outline_MultiLayerLines = this.Outline_MultiLayerLines.clone();
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructTextSymbol
    {
        public double Angle; //Der Textwinkel
        public String Color; //...
        public String Font; //...
        public String Style; //Font style in CSS notation, e.g. italic or normal
        public String Weight; //Font weight in CSS notation, e.g. bold or normal
        public String HorizontalAlignment; // die Horizontale Textausrichtung als esriTextHorizontalAlignment
        public boolean RightToLeft;
        public double Size; //...
        public String Text; //...
        public String VerticalAlignment; //die Verticale Textausrichtung als esriTextVerticalAlignment
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructTextSymbol clone()
        {
            StructTextSymbol varCopy = new StructTextSymbol();

            varCopy.Angle = this.Angle;
            varCopy.Color = this.Color;
            varCopy.Font = this.Font;
            varCopy.Style = this.Style;
            varCopy.Weight = this.Weight;
            varCopy.HorizontalAlignment = this.HorizontalAlignment;
            varCopy.RightToLeft = this.RightToLeft;
            varCopy.Size = this.Size;
            varCopy.Text = this.Text;
            varCopy.VerticalAlignment = this.VerticalAlignment;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }

    public final static class StructMultilayerMarkerSymbol
    {
        public java.util.ArrayList MultiMarkerLayers; //Das Symbol kann aus mehreren Markersymbollayern zusammengestzt sein
        public int LayerCount; //Anzahl der Layer
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructMultilayerMarkerSymbol clone()
        {
            StructMultilayerMarkerSymbol varCopy = new StructMultilayerMarkerSymbol();

            varCopy.MultiMarkerLayers = this.MultiMarkerLayers;
            varCopy.LayerCount = this.LayerCount;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructMultilayerLineSymbol
    {
        public java.util.ArrayList MultiLineLayers; //Das Symbol kann aus mehreren Linesymbollayern zusammengestzt sein
        public int LayerCount; //Anzahl der Layer
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructMultilayerLineSymbol clone()
        {
            StructMultilayerLineSymbol varCopy = new StructMultilayerLineSymbol();

            varCopy.MultiLineLayers = this.MultiLineLayers;
            varCopy.LayerCount = this.LayerCount;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }

    public final static class StructMultilayerFillSymbol
    {
        public java.util.ArrayList MultiFillLayers; //Das Symbol kann aus mehreren Fillsymbollayern zusammengestzt sein
        public int LayerCount; //Anzahl der Layer
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Daten ermittelt auf Rendererlevel:
        public String Label; //Die Feldwertbezeichnung (Label) [alle Renderer]
        public java.util.ArrayList Fieldvalues; //Der Feldwert(e), nach dem Klassifiziert und dieses Symbol zugewiesen wird [Alle Renderer]. Die Reihenfolge der Werte entspricht auch der Reihenfolge der Felder (Spalten), wie sie im Renderereobjekt unter 'FieldNames as ArrayList' zu finden ist
        public double UpperLimit; //Obergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer
        public double LowerLimit; //Untergrenze der das aktuelle Symbol betr. Range beim ClassBreaksRenderer

        public StructMultilayerFillSymbol clone()
        {
            StructMultilayerFillSymbol varCopy = new StructMultilayerFillSymbol();

            varCopy.MultiFillLayers = this.MultiFillLayers;
            varCopy.LayerCount = this.LayerCount;
            varCopy.Label = this.Label;
            varCopy.Fieldvalues = this.Fieldvalues;
            varCopy.UpperLimit = this.UpperLimit;
            varCopy.LowerLimit = this.LowerLimit;

            return varCopy;
        }
    }
    public final static class StructAnnotation
    {
        public boolean IsSingleProperty;
        public String PropertyName;
        public StructTextSymbol TextSymbol;

        public StructAnnotation clone()
        {
            StructAnnotation varCopy = new StructAnnotation();

            varCopy.IsSingleProperty = this.IsSingleProperty;
            varCopy.PropertyName = this.PropertyName;
            varCopy.TextSymbol = this.TextSymbol.clone();

            return varCopy;
        }
    }

    public ArcMapSymbols(MainFormProcess mainFormProcess, String Filename)
    {
        m_cFilename = Filename;
        this.mainFormProcess = mainFormProcess;

        m_ObjApp = null;
        CentralProcessingFunc();
    }
    private void AddOneToLayerNumber()
    {
        m_StrProject.LayerCount++;
    }
    public final Object getGetProjectData()
    {
        return m_StrProject;
    }

    private StructSimpleRenderer StoreStructSimpleRenderer(ISimpleRenderer Renderer, IFeatureLayer Layer)
    {
        StructSimpleRenderer strRenderer = new StructSimpleRenderer();
        ISymbol objFstOrderSymbol = null;
        IDataset objDataset = null;

        strRenderer.SymbolList = new java.util.ArrayList();

        try
        {
            objDataset = (IDataset)Layer.getFeatureClass();
            strRenderer.LayerName = Layer.getName();
            strRenderer.DatasetName =objDataset.getName();
            strRenderer.Annotation = GetAnnotation(Layer).clone();
            objFstOrderSymbol = Renderer.getSymbol(); //Die Zuweisung der jeweiligen einzelnen Symbole
            if (objFstOrderSymbol instanceof ITextSymbol)
            {
                StructTextSymbol strTS = new StructTextSymbol();
                ITextSymbol objSymbol = (ITextSymbol) objFstOrderSymbol;
                strTS = StoreText(objSymbol).clone();
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                strTS.Label = Renderer.getLabel();
                strRenderer.SymbolList.add(strTS.clone());

            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx()
            if (objFstOrderSymbol instanceof IMarkerSymbol)
            {
                strRenderer.FeatureCls = FeatureClass.PointFeature;
                IMarkerSymbol objSymbol = null;
                objSymbol = (IMarkerSymbol) objFstOrderSymbol;
                if (MarkerSymbolScan(objSymbol).equals("ISimpleMarkerSymbol"))
                {
                    ISimpleMarkerSymbol SMS = null;
                    SMS = (ISimpleMarkerSymbol) objSymbol;
                    StructSimpleMarkerSymbol strSMS = new StructSimpleMarkerSymbol();
                    strSMS = StoreSimpleMarker(SMS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strSMS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strSMS.clone());
                }
                else if (MarkerSymbolScan(objSymbol).equals("ICharacterMarkerSymbol"))
                {
                    ICharacterMarkerSymbol CMS = null;
                    CMS = (ICharacterMarkerSymbol) objSymbol;
                    StructCharacterMarkerSymbol strCMS = new StructCharacterMarkerSymbol();
                    strCMS = StoreCharacterMarker(CMS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strCMS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strCMS.clone());
                }
                else if (MarkerSymbolScan(objSymbol).equals("IPictureMarkerSymbol"))
                {
                    IPictureMarkerSymbol PMS = null;
                    PMS = (IPictureMarkerSymbol) objSymbol;
                    StructPictureMarkerSymbol strPMS = new StructPictureMarkerSymbol();
                    strPMS = StorePictureMarker(PMS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strPMS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strPMS.clone());
                }
                else if (MarkerSymbolScan(objSymbol).equals("IArrowMarkerSymbol"))
                {
                    IArrowMarkerSymbol AMS = null;
                    AMS = (IArrowMarkerSymbol) objSymbol;
                    StructArrowMarkerSymbol strAMS = new StructArrowMarkerSymbol();
                    strAMS = StoreArrowMarker(AMS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strAMS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strAMS.clone());
                }
                else if (MarkerSymbolScan(objSymbol).equals("IMultiLayerMarkerSymbol"))
                {
                    IMultiLayerMarkerSymbol MLMS = null;
                    MLMS = (IMultiLayerMarkerSymbol) objSymbol;
                    StructMultilayerMarkerSymbol strMLMS = new StructMultilayerMarkerSymbol();
                    strMLMS = StoreMultiLayerMarker(MLMS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strMLMS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strMLMS.clone());
                }
                else if (MarkerSymbolScan(objSymbol).equals("false"))
                {
                    InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                }
            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            if (objFstOrderSymbol instanceof ILineSymbol)
            {
                strRenderer.FeatureCls = FeatureClass.LineFeature;
                ILineSymbol objSymbol = null;
                objSymbol = (ILineSymbol) objFstOrderSymbol;
                if (LineSymbolScan(objSymbol).equals("ICartographicLineSymbol"))
                {
                    ICartographicLineSymbol CLS = null;
                    CLS = (ICartographicLineSymbol) objSymbol;
                    StructCartographicLineSymbol strCLS = new StructCartographicLineSymbol();
                    strCLS = StoreCartographicLine(CLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strCLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strCLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("IHashLineSymbol"))
                {
                    IHashLineSymbol HLS = null;
                    HLS = (IHashLineSymbol) objSymbol;
                    StructHashLineSymbol strHLS = new StructHashLineSymbol();
                    strHLS = StoreHashLine(HLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strHLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strHLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("IMarkerLineSymbol"))
                {
                    IMarkerLineSymbol MLS = null;
                    MLS = (IMarkerLineSymbol) objSymbol;
                    StructMarkerLineSymbol strMLS = new StructMarkerLineSymbol();
                    strMLS = StoreMarkerLine(MLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strMLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strMLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("ISimpleLineSymbol"))
                {
                    ISimpleLineSymbol SLS = null;
                    SLS = (ISimpleLineSymbol) objSymbol;
                    StructSimpleLineSymbol strSLS = new StructSimpleLineSymbol();
                    strSLS = StoreSimpleLine(SLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strSLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strSLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("IPictureLineSymbol"))
                {
                    IPictureLineSymbol PLS = null;
                    PLS = (IPictureLineSymbol) objSymbol;
                    StructPictureLineSymbol strPLS = new StructPictureLineSymbol();
                    strPLS = StorePictureLine(PLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strPLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strPLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("IMultiLayerLineSymbol"))
                {
                    IMultiLayerLineSymbol MLLS = null;
                    MLLS = (IMultiLayerLineSymbol) objSymbol;
                    StructMultilayerLineSymbol strMLLS = new StructMultilayerLineSymbol();
                    strMLLS = StoreMultilayerLines(MLLS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strMLLS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strMLLS.clone());
                }
                else if (LineSymbolScan(objSymbol).equals("false"))
                {
                    InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                }

            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            if (objFstOrderSymbol instanceof IFillSymbol)
            {
                strRenderer.FeatureCls = FeatureClass.PolygonFeature;
                IFillSymbol objSymbol = null;
                objSymbol = (IFillSymbol) objFstOrderSymbol;
                if (FillSymbolScan(objSymbol).equals("ISimpleFillSymbol"))
                {
                    ISimpleFillSymbol SFS = null;
                    SFS = (ISimpleFillSymbol) objSymbol;
                    StructSimpleFillSymbol strSFS = new StructSimpleFillSymbol();
                    strSFS = StoreSimpleFill(SFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strSFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strSFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("IMarkerFillSymbol"))
                {
                    IMarkerFillSymbol MFS = null;
                    MFS = (IMarkerFillSymbol) objSymbol;
                    StructMarkerFillSymbol strMFS = new StructMarkerFillSymbol();
                    strMFS = StoreMarkerFill(MFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strMFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strMFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("ILineFillSymbol"))
                {
                    ILineFillSymbol LFS = null;
                    LFS = (ILineFillSymbol) objSymbol;
                    StructLineFillSymbol strLFS = new StructLineFillSymbol();
                    strLFS = StoreLineFill(LFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strLFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strLFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("IDotDensityFillSymbol"))
                {
                    IDotDensityFillSymbol DFS = null;
                    DFS = (IDotDensityFillSymbol) objSymbol;
                    StructDotDensityFillSymbol strDFS = new StructDotDensityFillSymbol();
                    strDFS = StoreDotDensityFill(DFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strDFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strDFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("IPictureFillSymbol"))
                {
                    IPictureFillSymbol PFS = null;
                    PFS = (IPictureFillSymbol) objSymbol;
                    StructPictureFillSymbol strPFS = new StructPictureFillSymbol();
                    strPFS = StorePictureFill(PFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strPFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strPFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("IGradientFillSymbol"))
                {
                    IGradientFillSymbol GFS = null;
                    GFS = (IGradientFillSymbol) objSymbol;
                    StructGradientFillSymbol strGFS = new StructGradientFillSymbol();
                    strGFS = StoreGradientFill(GFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strGFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strGFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("IMultiLayerFillSymbol"))
                {
                    IMultiLayerFillSymbol MLFS = null;
                    MLFS = (IMultiLayerFillSymbol) objSymbol;
                    StructMultilayerFillSymbol strMLFS = new StructMultilayerFillSymbol();
                    strMLFS = StoreMultiLayerFill(MLFS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strMLFS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strMLFS.clone());
                }
                else if (FillSymbolScan(objSymbol).equals("false"))
                {
                    InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                }
            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            if (objFstOrderSymbol instanceof I3DChartSymbol)
            {
                I3DChartSymbol objSymbol = null;
                objSymbol = (I3DChartSymbol) objFstOrderSymbol;
                if (IIIDChartSymbolScan(objSymbol).equals("IBarChartSymbol"))
                {
                    IBarChartSymbol BCS = null;
                    BCS = (IBarChartSymbol) objSymbol;
                    StructBarChartSymbol strBCS = new StructBarChartSymbol();
                    strBCS = StoreBarChart(BCS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strBCS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strBCS.clone());
                }
                else if (IIIDChartSymbolScan(objSymbol).equals("IPieChartSymbol"))
                {
                    IPieChartSymbol PCS = null;
                    PCS = (IPieChartSymbol) objSymbol;
                    StructPieChartSymbol strPCS = new StructPieChartSymbol();
                    strPCS = StorePieChart(PCS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strPCS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strPCS.clone());
                }
                else if (IIIDChartSymbolScan(objSymbol).equals("IStackedChartSymbol"))
                {
                    IStackedChartSymbol SCS = null;
                    SCS = (IStackedChartSymbol) objSymbol;
                    StructStackedChartSymbol strSCS = new StructStackedChartSymbol();
                    strSCS = StoreStackedChart(SCS).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strSCS.Label = String.valueOf(Renderer.getLabel());
                    strRenderer.SymbolList.add(strSCS.clone());
                }
                else if (IIIDChartSymbolScan(objSymbol).equals("false"))
                {
                    InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                }
            }
            //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

        }
        catch (Exception ex)
        {

            ErrorMsg("Fehler beim Speichern der Symbole in den Layerstrukturen", ex.getMessage(), ex.getStackTrace(), "StoreStructSimpleRenderer");
        }
        return strRenderer;
    }
    private StructClassBreaksRenderer StoreStructCBRenderer(IClassBreaksRenderer Renderer, IFeatureLayer Layer)
    {
        StructClassBreaksRenderer strRenderer = new StructClassBreaksRenderer(); //Hierin wird das eine Rendererobjekt gespeichert plus zus鋞zliche Layerinformationen
        strRenderer.SymbolList = new java.util.ArrayList();
        int iNumberOfSymbols = 0; //Anzahl aller Symbole des Rendererobjekts


        try
        {
            iNumberOfSymbols = Renderer.getBreakCount(); //Anzahl der Symbole
            strRenderer.BreakCount = Renderer.getBreakCount();
            ISymbol objFstOrderSymbol = null; //Das gerade aktuelle Symbol des durchlaufenen Rendererobjekts
            IDataset objDataset = null;
            objDataset = (IDataset) Layer.getFeatureClass();
            boolean bIsJoined;
            bIsJoined = false;
            strRenderer.LayerName = String.valueOf(Layer.getName());
            strRenderer.DatasetName = String.valueOf(objDataset.getName());
            strRenderer.Annotation = GetAnnotation(Layer).clone();

            //diese Objekte dienen lediglich der 躡erpr黤ung, ob eine andere Tabelle an die Featuretable gejoint ist
            //++++++++++++++++++++++++++++++++++++++++++
            ITable pTable = null;
            IDisplayTable pDisplayTable = null;
            pDisplayTable = (IDisplayTable) Layer;
            pTable = pDisplayTable.getDisplayTable();
            if (pTable instanceof IRelQueryTable) //Dann ist eine Tabelle drangejoint
            {
                bIsJoined = true;
            }
            //++++++++++++++++++++++++++++++++++++++++++

            if (bIsJoined == false) //Wenn eine Tabelle drangejoint wurde, ist sowieso schon der Datasetname mit dabei
            {
//                //Je nachdem, welcher Zielkartendienst gew鋒lt wurde, muss Fieldname ein anderes Format besitzen
//                if (frmMotherform.chkArcIMS.Checked == true)
//                {
//                    strRenderer.FieldName = objDataset.getName() +"." + Renderer.Field;
//                    strRenderer.NormFieldName = objDataset.getName() +"." + Renderer.NormField;
//                }
//                else
//                {
//                    strRenderer.FieldName = String.valueOf(Renderer.Field);
//                    strRenderer.NormFieldName = String.valueOf(Renderer.NormField);
//                }
            }



            //AB HIER BEGINNEN DIE FALLUNTERSCHEIDUNGEN DER SYMBOLE
            int j = 0;
            for (j = 0; j <= iNumberOfSymbols - 1; j++)
            {
//                frmMotherform.CHLabelSmall("Symbol " + (j + 1).toString() + " von " + (new Integer(iNumberOfSymbols)).toString()); //Anzeige auf dem Label
                objFstOrderSymbol = Renderer.getSymbol(j); //Die Zuweisung der jeweiligen einzelnen Symbole
                IClassBreaksUIProperties objClassBreaksProp = null;
                objClassBreaksProp = (IClassBreaksUIProperties) Renderer;
                String cLowerLimit = "";
                String cUpperLimit = "";
                cLowerLimit = objClassBreaksProp.getLowBreak(j)+""; //Die Untergrenze der aktuellen Klasse
                cUpperLimit = Renderer.getBreak(j)+"";

                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof ITextSymbol)
                {
                    StructTextSymbol strTS = new StructTextSymbol();
                    ITextSymbol objSymbol = null;
                    objSymbol = (ITextSymbol) objFstOrderSymbol;
                    strTS = StoreText(objSymbol).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strTS.Label = String.valueOf(Renderer.getLabel(j));
                    strTS.LowerLimit = Double.parseDouble(cLowerLimit);
                    strTS.UpperLimit = Double.parseDouble(cUpperLimit);
                    strRenderer.SymbolList.add(strTS.clone());

                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx()
                if (objFstOrderSymbol instanceof IMarkerSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.PointFeature;
                    IMarkerSymbol objSymbol = null;
                    objSymbol = (IMarkerSymbol) objFstOrderSymbol;
                    if (MarkerSymbolScan(objSymbol).equals("ISimpleMarkerSymbol"))
                    {
                        ISimpleMarkerSymbol SMS = null;
                        SMS = (ISimpleMarkerSymbol) objSymbol;
                        StructSimpleMarkerSymbol strSMS = new StructSimpleMarkerSymbol();
                        strSMS = StoreSimpleMarker(SMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSMS.Label = String.valueOf(Renderer.getLabel(j));
                        strSMS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strSMS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strSMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("ICharacterMarkerSymbol"))
                    {
                        ICharacterMarkerSymbol CMS = null;
                        CMS = (ICharacterMarkerSymbol) objSymbol;
                        StructCharacterMarkerSymbol strCMS = new StructCharacterMarkerSymbol();
                        strCMS = StoreCharacterMarker(CMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strCMS.Label = String.valueOf(Renderer.getLabel(j));
                        strCMS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strCMS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strCMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IPictureMarkerSymbol"))
                    {
                        IPictureMarkerSymbol PMS = null;
                        PMS = (IPictureMarkerSymbol) objSymbol;
                        StructPictureMarkerSymbol strPMS = new StructPictureMarkerSymbol();
                        strPMS = StorePictureMarker(PMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPMS.Label = String.valueOf(Renderer.getLabel(j));
                        strPMS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strPMS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strPMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IArrowMarkerSymbol"))
                    {
                        IArrowMarkerSymbol AMS = null;
                        AMS = (IArrowMarkerSymbol) objSymbol;
                        StructArrowMarkerSymbol strAMS = new StructArrowMarkerSymbol();
                        strAMS = StoreArrowMarker(AMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strAMS.Label = String.valueOf(Renderer.getLabel(j));
                        strAMS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strAMS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strAMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IMultiLayerMarkerSymbol"))
                    {
                        IMultiLayerMarkerSymbol MLMS = null;
                        MLMS = (IMultiLayerMarkerSymbol) objSymbol;
                        StructMultilayerMarkerSymbol strMLMS = new StructMultilayerMarkerSymbol();
                        strMLMS = StoreMultiLayerMarker(MLMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLMS.Label = String.valueOf(Renderer.getLabel(j));
                        strMLMS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strMLMS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strMLMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof ILineSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.LineFeature;
                    ILineSymbol objSymbol = null;
                    objSymbol = (ILineSymbol) objFstOrderSymbol;
                    if (LineSymbolScan(objSymbol).equals("ICartographicLineSymbol"))
                    {
                        ICartographicLineSymbol CLS = null;
                        CLS = (ICartographicLineSymbol) objSymbol;
                        StructCartographicLineSymbol strCLS = new StructCartographicLineSymbol();
                        strCLS = StoreCartographicLine(CLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strCLS.Label = String.valueOf(Renderer.getLabel(j));
                        strCLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strCLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strCLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IHashLineSymbol"))
                    {
                        IHashLineSymbol HLS = null;
                        HLS = (IHashLineSymbol) objSymbol;
                        StructHashLineSymbol strHLS = new StructHashLineSymbol();
                        strHLS = StoreHashLine(HLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strHLS.Label = String.valueOf(Renderer.getLabel(j));
                        strHLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strHLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strHLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IMarkerLineSymbol"))
                    {
                        IMarkerLineSymbol MLS = null;
                        MLS = (IMarkerLineSymbol) objSymbol;
                        StructMarkerLineSymbol strMLS = new StructMarkerLineSymbol();
                        strMLS = StoreMarkerLine(MLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLS.Label = String.valueOf(Renderer.getLabel(j));
                        strMLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strMLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strMLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("ISimpleLineSymbol"))
                    {
                        ISimpleLineSymbol SLS = null;
                        SLS = (ISimpleLineSymbol) objSymbol;
                        StructSimpleLineSymbol strSLS = new StructSimpleLineSymbol();
                        strSLS = StoreSimpleLine(SLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSLS.Label = String.valueOf(Renderer.getLabel(j));
                        strSLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strSLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strSLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IPictureLineSymbol"))
                    {
                        IPictureLineSymbol PLS = null;
                        PLS = (IPictureLineSymbol) objSymbol;
                        StructPictureLineSymbol strPLS = new StructPictureLineSymbol();
                        strPLS = StorePictureLine(PLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPLS.Label = String.valueOf(Renderer.getLabel(j));
                        strPLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strPLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strPLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IMultiLayerLineSymbol"))
                    {
                        IMultiLayerLineSymbol MLLS = null;
                        MLLS = (IMultiLayerLineSymbol) objSymbol;
                        StructMultilayerLineSymbol strMLLS = new StructMultilayerLineSymbol();
                        strMLLS = StoreMultilayerLines(MLLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLLS.Label = String.valueOf(Renderer.getLabel(j));
                        strMLLS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strMLLS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strMLLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }

                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof IFillSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.PolygonFeature;
                    IFillSymbol objSymbol = null;
                    objSymbol = (IFillSymbol) objFstOrderSymbol;
                    if (FillSymbolScan(objSymbol).equals("ISimpleFillSymbol"))
                    {
                        ISimpleFillSymbol SFS = null;
                        SFS = (ISimpleFillSymbol) objSymbol;
                        StructSimpleFillSymbol strSFS = new StructSimpleFillSymbol();
                        strSFS = StoreSimpleFill(SFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSFS.Label = String.valueOf(Renderer.getLabel(j));
                        strSFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strSFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strSFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IMarkerFillSymbol"))
                    {
                        IMarkerFillSymbol MFS = null;
                        MFS = (IMarkerFillSymbol) objSymbol;
                        StructMarkerFillSymbol strMFS = new StructMarkerFillSymbol();
                        strMFS = StoreMarkerFill(MFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMFS.Label = String.valueOf(Renderer.getLabel(j));
                        strMFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strMFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strMFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("ILineFillSymbol"))
                    {
                        ILineFillSymbol LFS = null;
                        LFS = (ILineFillSymbol) objSymbol;
                        StructLineFillSymbol strLFS = new StructLineFillSymbol();
                        strLFS = StoreLineFill(LFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strLFS.Label = String.valueOf(Renderer.getLabel(j));
                        strLFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strLFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strLFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IDotDensityFillSymbol"))
                    {
                        IDotDensityFillSymbol DFS = null;
                        DFS = (IDotDensityFillSymbol) objSymbol;
                        StructDotDensityFillSymbol strDFS = new StructDotDensityFillSymbol();
                        strDFS = StoreDotDensityFill(DFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strDFS.Label = String.valueOf(Renderer.getLabel(j));
                        strDFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strDFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strDFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IPictureFillSymbol"))
                    {
                        IPictureFillSymbol PFS = null;
                        PFS = (IPictureFillSymbol) objSymbol;
                        StructPictureFillSymbol strPFS = new StructPictureFillSymbol();
                        strPFS = StorePictureFill(PFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPFS.Label = String.valueOf(Renderer.getLabel(j));
                        strPFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strPFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strPFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IGradientFillSymbol"))
                    {
                        IGradientFillSymbol GFS = null;
                        GFS = (IGradientFillSymbol) objSymbol;
                        StructGradientFillSymbol strGFS = new StructGradientFillSymbol();
                        strGFS = StoreGradientFill(GFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strGFS.Label = String.valueOf(Renderer.getLabel(j));
                        strGFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strGFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strGFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IMultiLayerFillSymbol"))
                    {
                        IMultiLayerFillSymbol MLFS = null;
                        MLFS = (IMultiLayerFillSymbol) objSymbol;
                        StructMultilayerFillSymbol strMLFS = new StructMultilayerFillSymbol();
                        strMLFS = StoreMultiLayerFill(MLFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLFS.Label = String.valueOf(Renderer.getLabel(j));
                        strMLFS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strMLFS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strMLFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof I3DChartSymbol)
                {
                    I3DChartSymbol objSymbol = null;
                    objSymbol = (I3DChartSymbol) objFstOrderSymbol;
                    if (IIIDChartSymbolScan(objSymbol).equals("IBarChartSymbol"))
                    {
                        IBarChartSymbol BCS = null;
                        BCS = (IBarChartSymbol) objSymbol;
                        StructBarChartSymbol strBCS = new StructBarChartSymbol();
                        strBCS = StoreBarChart(BCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strBCS.Label = String.valueOf(Renderer.getLabel(j));
                        strBCS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strBCS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strBCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("IPieChartSymbol"))
                    {
                        IPieChartSymbol PCS = null;
                        PCS = (IPieChartSymbol) objSymbol;
                        StructPieChartSymbol strPCS = new StructPieChartSymbol();
                        strPCS = StorePieChart(PCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPCS.Label = String.valueOf(Renderer.getLabel(j));
                        strPCS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strPCS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strPCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("IStackedChartSymbol"))
                    {
                        IStackedChartSymbol SCS = null;
                        SCS = (IStackedChartSymbol) objSymbol;
                        StructStackedChartSymbol strSCS = new StructStackedChartSymbol();
                        strSCS = StoreStackedChart(SCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSCS.Label = String.valueOf(Renderer.getLabel(j));
                        strSCS.LowerLimit = Double.parseDouble(cLowerLimit);
                        strSCS.UpperLimit = Double.parseDouble(cUpperLimit);
                        strRenderer.SymbolList.add(strSCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            }

        }
        catch (Exception ex)
        {

            ErrorMsg("Fehler beim Speichern der Symbole in den Layerstrukturen", ex.getMessage(), ex.getStackTrace(), "StoreStructCBRenderer");
        }
        return strRenderer;
    }
    private StructUniqueValueRenderer StoreStructUVRenderer(IUniqueValueRenderer Renderer, IFeatureLayer Layer)
    {
        StructUniqueValueRenderer strRenderer = new StructUniqueValueRenderer(); //Hierin wird das eine Rendererobjekt gespeichert plus zus鋞zliche Layerinformationen
        int iNumberOfSymbols = 0; //Anzahl aller Symbole des Rendererobjekts
        try
        {
            iNumberOfSymbols = Renderer.getValueCount(); //Anzahl der Symbole
            ISymbol objFstOrderSymbol = null; //Das gerade aktuelle Symbol des durchlaufenen Rendererobjekts
            java.util.ArrayList alFieldNames = new java.util.ArrayList(); //Die ArrayLisit mit den Feldnamen
            boolean bNoSepFieldVal; //Wenn nur nach einem Feld klassifiziert wurde muss der Flag auf true gesetzt werden
            ITable objTable = null; //Der FeatureTable, der an den Layer gebunden ist
            IDataset objDataset = null; //Der aktuelle Dataset
            objTable = (ITable) Layer.getFeatureClass();
            objDataset = (IDataset) objTable;
            strRenderer.SymbolList = new java.util.ArrayList();
            bNoSepFieldVal = false; //!!!! Damit nicht in die aufwendige Funktion GimmeSeperateFieldValues(..) gegangen werden mu? wenn nicht n鰐ig
            m_alClassifiedFields = new java.util.ArrayList(); //enth鋖t jeweils wiederum arraylist(s) von den normalisierten Werten aus den Feldern, nach denen klassifiziert wurde
            boolean bIsJoined;
            bIsJoined = false;
            strRenderer.ValueCount = iNumberOfSymbols;
            strRenderer.LayerName = String.valueOf(Layer.getName());
            strRenderer.DatasetName = String.valueOf(objDataset.getName());
            strRenderer.Annotation = GetAnnotation(Layer).clone();
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Hier werden die Informationen 黚er Felder nach denen klassifiziert wurde gesammelt
            int iFieldCount = 0; //Anzahl der Spalten, nach denen klassifiziert wurde
            iFieldCount = Renderer.getFieldCount();
            strRenderer.FieldCount = iFieldCount;
            if (iFieldCount > 1) //!!!! Damit nicht in die aufwendige Funktion GimmeSeperateFieldValues(..) gegangen werden mu? wenn nicht n鰐ig
            {
                bNoSepFieldVal = true; //und damit nicht die aufw鋘digen UniqueValue-Listen erzeugt werden m黶sen, wenn nicht notwendig
            }

            //diese Objekte dienen lediglich der 躡erpr黤ung, ob eine andere Tabelle an die Featuretable gejoint ist
            //++++++++++++++++++++++++++++++++++++++++++
            ITable pTable = null;
            IDisplayTable pDisplayTable = null;
            pDisplayTable = (IDisplayTable) Layer;
            pTable = pDisplayTable.getDisplayTable();
            if (pTable instanceof IRelQueryTable) //Dann ist eine Tabelle drangejoint
            {
                bIsJoined = true;
            }
            //++++++++++++++++++++++++++++++++++++++++++

            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Das ganze nachfolgende Konstrukt dient nur dem Zweck, Unique-Value-Listen aus den Datenbanktabellen zu erstellen,
            //um sp鋞er mit der Funktion GimmeSeperateFieldValues die separaten Spaltenwerte, die zu einem Symbol geh鰎en, zu erhalten,
            //wenn (und nur wenn) nach mehr als 1 Wert klassifiziert wurde (da esri in diesem Fall nur einen aus allen Feldwerten
            //zusammengesetzten String liefert)
            int i = 0;
            if (bNoSepFieldVal == true) //Feldnamen (Spaltennamen) werden nur dann gespeichert, wenn nach mehr als 1 Feld klassifiziert wurde. (da sehr aufw鋘dige Sache!)
            {

                //Wenn der aktuelle Layer aus einem Shapefile stammt, ist das weitere Vorgehen
                //unterschiedlich zu dem Fall, wenn er aus DB's stammt:
                if (objDataset.getWorkspace().getType() == esriWorkspaceType.esriFileSystemWorkspace)
                {
                    for (i = 1; i <= iFieldCount; i++)
                    {
                        alFieldNames.add(Renderer.getField(i - 1)); //Die Spaltennamen werden alle abgespeichert
                    }
                    GimmeUniqueValuesFromShape(objTable, alFieldNames);
                    //GimmeUniqueValuesFromShape(Layer, alFieldNames)
                    strRenderer.FieldNames = alFieldNames;
                }
                else
                {
                    for (i = 1; i <= iFieldCount; i++)
                    {
                        alFieldNames.add(Renderer.getField(i - 1)); //Die Spaltennamen werden alle abgespeichert
                        //wenn eine andere Tabelle an die Featuretable gejoint ist
                        if (pTable instanceof IRelQueryTable)
                        {
                            // ++ Get the list of joined tables
                            IRelQueryTable pRelQueryTable = null;
                            ITable pDestTable = null;
                            IDataset pDataSet = null;
                            //Dim cTable As String
                            java.util.ArrayList alJoinedTableNames = new java.util.ArrayList();
                            while (pTable instanceof IRelQueryTable)
                            {
                                pRelQueryTable = (IRelQueryTable) pTable;
                                pDestTable = pRelQueryTable.getDestinationTable();
                                pDataSet = (IDataset) pDestTable;
                                //cTable = cTable & pDataSet.Name
                                pTable = pRelQueryTable.getSourceTable();
                                alJoinedTableNames.add(pDataSet.getName());
                            }
                            GimmeUniqeValuesForFieldname(objTable, Renderer.getField(i - 1), alJoinedTableNames); //Hier wird die Funktion aufgerufen, um die Unique Values des jew. Feldes abzuspeichern
                            pTable = pDisplayTable.getDisplayTable(); //Zur點ksetzen des pTable, damit beim n鋍hsten Durchlauf wieder abgefragt werden kann
                        }
                        else //Bei nichtgejointer Tabelle nat黵lich Aufruf der Funktion ohne die gejointen Tabellennamen
                        {
                            GimmeUniqeValuesForFieldname(objTable, Renderer.getField(i - 1)); //Hier wird die Funktion aufgerufen, um die Unique Values des jew. Feldes abzuspeichern
                        }
                    }
                    strRenderer.FieldNames = alFieldNames;
                }
            }
            else
            {
                alFieldNames.add(Renderer.getField(iFieldCount - 1));
                strRenderer.FieldNames = alFieldNames;
            }

            //Je nachdem, welcher Zielkartendienst gew鋒lt wurde, muss Fieldname ein anderes Format besitzen
            if (bIsJoined == false) //Wenn eine Tabelle drangejoint wurde, ist sowieso schon der Datasetname mit dabei
            {
                int idummy;
                if (mainFormProcess.isArcSDE)
                {
                    for (i = 0; i <= strRenderer.FieldNames.size() - 1; i++)
                    {
                        strRenderer.FieldNames.set(i, objDataset.getName() +"." + String.valueOf(strRenderer.FieldNames.get(i)));
                    }
                }
            }


            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

            //AB HIER BEGINNEN DIE FALLUNTERSCHEIDUNGEN DER SYMBOLE
            int j = 0;
            for (j = 0; j <= iNumberOfSymbols - 1; j++)
            {
//                frmMotherform.CHLabelSmall("Symbol " + (j + 1).toString() + " von " + (new Integer(iNumberOfSymbols)).toString()); //Anzeige auf dem Label
                objFstOrderSymbol = Renderer.getSymbol(Renderer.getValue(j)); //Die Zuweisung der jeweiligen einzelnen Symbole
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof ITextSymbol)
                {
                    StructTextSymbol strTS = new StructTextSymbol();
                    ITextSymbol objSymbol = null;
                    objSymbol = (ITextSymbol) objFstOrderSymbol;
                    strTS = StoreText(objSymbol).clone();
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    strTS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                    strTS.Fieldvalues = getUVFieldValues(Renderer, j);
                    strRenderer.SymbolList.add(strTS.clone());
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx()
                if (objFstOrderSymbol instanceof IMarkerSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.PointFeature;
                    IMarkerSymbol objSymbol = null;
                    objSymbol = (IMarkerSymbol) objFstOrderSymbol;
                    if (MarkerSymbolScan(objSymbol).equals("ISimpleMarkerSymbol"))
                    {
                        ISimpleMarkerSymbol SMS = null;
                        SMS = (ISimpleMarkerSymbol) objSymbol;
                        StructSimpleMarkerSymbol strSMS = new StructSimpleMarkerSymbol();
                        strSMS = StoreSimpleMarker(SMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSMS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strSMS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strSMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("ICharacterMarkerSymbol"))
                    {
                        ICharacterMarkerSymbol CMS = null;
                        CMS = (ICharacterMarkerSymbol) objSymbol;
                        StructCharacterMarkerSymbol strCMS = new StructCharacterMarkerSymbol();
                        strCMS = StoreCharacterMarker(CMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strCMS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strCMS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strCMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IPictureMarkerSymbol"))
                    {
                        IPictureMarkerSymbol PMS = null;
                        PMS = (IPictureMarkerSymbol) objSymbol;
                        StructPictureMarkerSymbol strPMS = new StructPictureMarkerSymbol();
                        strPMS = StorePictureMarker(PMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPMS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strPMS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strPMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IArrowMarkerSymbol"))
                    {
                        IArrowMarkerSymbol AMS = null;
                        AMS = (IArrowMarkerSymbol) objSymbol;
                        StructArrowMarkerSymbol strAMS = new StructArrowMarkerSymbol();
                        strAMS = StoreArrowMarker(AMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strAMS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strAMS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strAMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("IMultiLayerMarkerSymbol"))
                    {
                        IMultiLayerMarkerSymbol MLMS = null;
                        MLMS = (IMultiLayerMarkerSymbol) objSymbol;
                        StructMultilayerMarkerSymbol strMLMS = new StructMultilayerMarkerSymbol();
                        strMLMS = StoreMultiLayerMarker(MLMS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLMS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strMLMS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strMLMS.clone());
                    }
                    else if (MarkerSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof ILineSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.LineFeature;
                    ILineSymbol objSymbol = null;
                    objSymbol = (ILineSymbol) objFstOrderSymbol;
                    if (LineSymbolScan(objSymbol).equals("ICartographicLineSymbol"))
                    {
                        ICartographicLineSymbol CLS = null;
                        CLS = (ICartographicLineSymbol) objSymbol;
                        StructCartographicLineSymbol strCLS = new StructCartographicLineSymbol();
                        strCLS = StoreCartographicLine(CLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strCLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strCLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strCLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IHashLineSymbol"))
                    {
                        IHashLineSymbol HLS = null;
                        HLS = (IHashLineSymbol) objSymbol;
                        StructHashLineSymbol strHLS = new StructHashLineSymbol();
                        strHLS = StoreHashLine(HLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strHLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strHLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strHLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IMarkerLineSymbol"))
                    {
                        IMarkerLineSymbol MLS = null;
                        MLS = (IMarkerLineSymbol) objSymbol;
                        StructMarkerLineSymbol strMLS = new StructMarkerLineSymbol();
                        strMLS = StoreMarkerLine(MLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strMLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strMLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("ISimpleLineSymbol"))
                    {
                        ISimpleLineSymbol SLS = null;
                        SLS = (ISimpleLineSymbol) objSymbol;
                        StructSimpleLineSymbol strSLS = new StructSimpleLineSymbol();
                        strSLS = StoreSimpleLine(SLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strSLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strSLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IPictureLineSymbol"))
                    {
                        IPictureLineSymbol PLS = null;
                        PLS = (IPictureLineSymbol) objSymbol;
                        StructPictureLineSymbol strPLS = new StructPictureLineSymbol();
                        strPLS = StorePictureLine(PLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strPLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strPLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("IMultiLayerLineSymbol"))
                    {
                        IMultiLayerLineSymbol MLLS = null;
                        MLLS = (IMultiLayerLineSymbol) objSymbol;
                        StructMultilayerLineSymbol strMLLS = new StructMultilayerLineSymbol();
                        strMLLS = StoreMultilayerLines(MLLS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLLS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strMLLS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strMLLS.clone());
                    }
                    else if (LineSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }

                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof IFillSymbol)
                {
                    strRenderer.FeatureCls = FeatureClass.PolygonFeature;
                    IFillSymbol objSymbol = null;
                    objSymbol = (IFillSymbol) objFstOrderSymbol;
                    if (FillSymbolScan(objSymbol).equals("ISimpleFillSymbol"))
                    {
                        ISimpleFillSymbol SFS = null;
                        SFS = (ISimpleFillSymbol) objSymbol;
                        StructSimpleFillSymbol strSFS = new StructSimpleFillSymbol();
                        strSFS = StoreSimpleFill(SFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strSFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strSFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IMarkerFillSymbol"))
                    {
                        IMarkerFillSymbol MFS = null;
                        MFS = (IMarkerFillSymbol) objSymbol;
                        StructMarkerFillSymbol strMFS = new StructMarkerFillSymbol();
                        strMFS = StoreMarkerFill(MFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strMFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strMFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("ILineFillSymbol"))
                    {
                        ILineFillSymbol LFS = null;
                        LFS = (ILineFillSymbol) objSymbol;
                        StructLineFillSymbol strLFS = new StructLineFillSymbol();
                        strLFS = StoreLineFill(LFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strLFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strLFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strLFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IDotDensityFillSymbol"))
                    {
                        IDotDensityFillSymbol DFS = null;
                        DFS = (IDotDensityFillSymbol) objSymbol;
                        StructDotDensityFillSymbol strDFS = new StructDotDensityFillSymbol();
                        strDFS = StoreDotDensityFill(DFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strDFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strDFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strDFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IPictureFillSymbol"))
                    {
                        IPictureFillSymbol PFS = null;
                        PFS = (IPictureFillSymbol) objSymbol;
                        StructPictureFillSymbol strPFS = new StructPictureFillSymbol();
                        strPFS = StorePictureFill(PFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strPFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strPFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IGradientFillSymbol"))
                    {
                        IGradientFillSymbol GFS = null;
                        GFS = (IGradientFillSymbol) objSymbol;
                        StructGradientFillSymbol strGFS = new StructGradientFillSymbol();
                        strGFS = StoreGradientFill(GFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strGFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strGFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strGFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("IMultiLayerFillSymbol"))
                    {
                        IMultiLayerFillSymbol MLFS = null;
                        MLFS = (IMultiLayerFillSymbol) objSymbol;
                        StructMultilayerFillSymbol strMLFS = new StructMultilayerFillSymbol();
                        strMLFS = StoreMultiLayerFill(MLFS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strMLFS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strMLFS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strMLFS.clone());
                    }
                    else if (FillSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                if (objFstOrderSymbol instanceof I3DChartSymbol)
                {
                    I3DChartSymbol objSymbol = null;
                    objSymbol = (I3DChartSymbol) objFstOrderSymbol;
                    if (IIIDChartSymbolScan(objSymbol).equals("IBarChartSymbol"))
                    {
                        IBarChartSymbol BCS = null;
                        BCS = (IBarChartSymbol) objSymbol;
                        StructBarChartSymbol strBCS = new StructBarChartSymbol();
                        strBCS = StoreBarChart(BCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strBCS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strBCS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strBCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("IPieChartSymbol"))
                    {
                        IPieChartSymbol PCS = null;
                        PCS = (IPieChartSymbol) objSymbol;
                        StructPieChartSymbol strPCS = new StructPieChartSymbol();
                        strPCS = StorePieChart(PCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strPCS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strPCS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strPCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("IStackedChartSymbol"))
                    {
                        IStackedChartSymbol SCS = null;
                        SCS = (IStackedChartSymbol) objSymbol;
                        StructStackedChartSymbol strSCS = new StructStackedChartSymbol();
                        strSCS = StoreStackedChart(SCS).clone();
                        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        strSCS.Label = String.valueOf(Renderer.getLabel(Renderer.getValue(j)));
                        strSCS.Fieldvalues = getUVFieldValues(Renderer, j);
                        strRenderer.SymbolList.add(strSCS.clone());
                    }
                    else if (IIIDChartSymbolScan(objSymbol).equals("false"))
                    {
                        InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreStructLayer");
                    }
                }
                //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            }

        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim Speichern der Symbole in den Layerstrukturen", ex.getMessage(), ex.getStackTrace(), "StoreStructUVRenderer");
        }
        return strRenderer;
    }
    private ArrayList getUVFieldValues(IUniqueValueRenderer Renderer, int Index) throws IOException {
        List<String> Fieldvalues = null;
        int iFieldCount = 0; //Anzahl der Spalten, nach denen klassifiziert wurde
        int Index2 = 0;
        iFieldCount = Renderer.getFieldCount();
        if (iFieldCount > 0)
        {
            boolean bNoSepFieldVal; //Wenn nur nach einem Feld klassifiziert wurde muss der Flag auf true gesetzt werden
            String Label;
            String Label2 = "";
            ISymbol objSymbol = null;
            int iNumberOfSymbols = 0; //Anzahl aller Symbole des Rendererobjekts
            iNumberOfSymbols =Renderer.getValueCount(); //Anzahl der Symbole
            bNoSepFieldVal = false;
            if (iFieldCount > 1) //!!!! Damit nicht in die aufwendige Funktion GimmeSeperateFieldValues(..) gegangen werden mu? wenn nicht n鰐ig
            {
                bNoSepFieldVal = true; //und damit nicht die aufw鋘digen UniqueValue-Listen erzeugt werden m黶sen, wenn nicht notwendig
            }

            Label = String.valueOf(Renderer.getLabel(Renderer.getValue(Index)));
            if (bNoSepFieldVal == false)
            {
                Fieldvalues = new java.util.ArrayList();
                Fieldvalues.add(Renderer.getValue(Index)); //Wenn nur nach 1 Feld klassifiziert wurde, muss nicht in GimmeSeperateFieldValues gegangen werden (Zeit!!!)
                //Find grouped values with the same label (values where no symbol defined).
                Index2 = Index + 1;
                while (Index2 < iNumberOfSymbols)
                {
                    objSymbol = Renderer.getSymbol(Renderer.getValue(Index2));
                    Label2 = String.valueOf(Renderer.getLabel(Renderer.getValue(Index2)));
                    if (objSymbol!=null && Label.equals(Label2))
                    {
                        Fieldvalues.add(Renderer.getValue(Index2));
                    }
                    else
                    {
                        break;
                    }
                    Index2++;
                }
            }
            else
            {
                Fieldvalues = (java.util.ArrayList)(GimmeSeperateFieldValues(Renderer.getValue(Index), Renderer.getFieldDelimiter()));
            }
        }
        return (ArrayList)Fieldvalues;
    }

    //************************************************************************************************
    //Die Funktion speichert die SimpleMarker in seiner Datenstruktur
    //************************************************************************************************
    private StructSimpleMarkerSymbol StoreSimpleMarker(ISimpleMarkerSymbol symbol) throws IOException {
        StructSimpleMarkerSymbol StructStorage = new StructSimpleMarkerSymbol();
        StructStorage.Angle =symbol.getAngle();
        StructStorage.Filled = symbol.getColor().getTransparency() != 0;
        StructStorage.Color = String.valueOf(GimmeStringForColor(symbol.getColor()));
        StructStorage.Outline = symbol.isOutline();
        StructStorage.OutlineColor = String.valueOf(GimmeStringForColor(symbol.getOutlineColor()));
        StructStorage.OutlineSize = symbol.getOutlineSize();
        StructStorage.Size = symbol.getSize();
        StructStorage.Style = String.valueOf(symbol.getStyle());
        StructStorage.XOffset = symbol.getXOffset();
        StructStorage.YOffset = symbol.getYOffset();

        return StructStorage;
    }


    //************************************************************************************************
    //Die Funktion speichert die CharacterMarker in seiner Datenstruktur
    //************************************************************************************************
    private StructCharacterMarkerSymbol StoreCharacterMarker(ICharacterMarkerSymbol symbol) throws IOException {
        StructCharacterMarkerSymbol StructStorage = new StructCharacterMarkerSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.CharacterIndex = symbol.getCharacterIndex();
        StructStorage.Color = String.valueOf(GimmeStringForColor(symbol.getColor()));
        StructStorage.Font = String.valueOf(symbol.getFont().getName());
        StructStorage.Size = symbol.getSize();
        StructStorage.XOffset = symbol.getXOffset();
        StructStorage.YOffset = symbol.getYOffset();
        return StructStorage;
    }
    private StructPictureMarkerSymbol StorePictureMarker(IPictureMarkerSymbol symbol) throws IOException {
        StructPictureMarkerSymbol StructStorage = new StructPictureMarkerSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.BackgroundColor = GimmeStringForColor(symbol.getBackgroundColor());
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Picture = (IPicture) symbol.getPicture();
        StructStorage.Size = symbol.getSize();
        StructStorage.XOffset = symbol.getXOffset();
        StructStorage.YOffset = symbol.getYOffset();
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die ArrowMarker in seiner Datenstruktur
//************************************************************************************************
    private StructArrowMarkerSymbol StoreArrowMarker(IArrowMarkerSymbol symbol) throws IOException {
        StructArrowMarkerSymbol StructStorage = new StructArrowMarkerSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Length=(symbol.getLength());
        StructStorage.Size = symbol.getSize();
        StructStorage.Style =String.valueOf(symbol.getStyle()) ;
        StructStorage.Width=(symbol.getWidth());
        StructStorage.XOffset = symbol.getXOffset();
        StructStorage.YOffset = symbol.getYOffset();
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die MultiLayerMarker in ArrayList. Wieviel Layer wird in LayerCount gesp.
//Das MultilayerMarker-Symbol kann nicht selbst nocheinmal aus einem MultilayerMarker bestehen
//************************************************************************************************
    private StructMultilayerMarkerSymbol StoreMultiLayerMarker(IMultiLayerMarkerSymbol symbol) throws IOException {
        StructMultilayerMarkerSymbol StructStorage = new StructMultilayerMarkerSymbol();
        StructStorage.MultiMarkerLayers = new java.util.ArrayList();
        int i = 0;
        StructStorage.LayerCount = symbol.getLayerCount();
        for (i = 0; i <= symbol.getLayerCount() - 1; i++) //Damit alle Layer erfasst werden
        {
            if (MarkerSymbolScan(symbol.getLayer(i)).equals("ISimpleMarkerSymbol"))
            {
                ISimpleMarkerSymbol SMS = null;
                SMS = (ISimpleMarkerSymbol) symbol.getLayer(i);
                StructStorage.MultiMarkerLayers.add(StoreSimpleMarker(SMS).clone()); //Der Layer wird der Arraylist von Multilayern hinzugef黦t
            }
            else if (MarkerSymbolScan(symbol.getLayer(i)).equals("ICharacterMarkerSymbol"))
            {
                ICharacterMarkerSymbol CMS = null;
                CMS = (ICharacterMarkerSymbol) symbol.getLayer(i);
                StructStorage.MultiMarkerLayers.add(StoreCharacterMarker(CMS).clone()); //Der Layer wird der Arraylist von Multilayern hinzugef黦t
            }
            else if (MarkerSymbolScan(symbol.getLayer(i)).equals("IPictureMarkerSymbol"))
            {
                IPictureMarkerSymbol PMS = null;
                PMS = (IPictureMarkerSymbol) symbol.getLayer(i);
                StructStorage.MultiMarkerLayers.add(StorePictureMarker(PMS).clone()); //Der Layer wird der Arraylist von Multilayern hinzugef黦t
            }
            else if (MarkerSymbolScan(symbol.getLayer(i)).equals("IArrowMarkerSymbol"))
            {
                IArrowMarkerSymbol AMS = null;
                AMS = (IArrowMarkerSymbol) symbol.getLayer(i);
                StructStorage.MultiMarkerLayers.add(StoreArrowMarker(AMS).clone()); //Der Layer wird der Arraylist von Multilayern hinzugef黦t
            }
            else if (MarkerSymbolScan(symbol.getLayer(i)).equals("false"))
            {
                InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMultiLayerMarker");
            }
        }
        return StructStorage;
    }
//_______________________________________________________________________________________________________________________

    //************************************************************************************************
//Die Funktion speichert die SimpleLines in seiner Datenstruktur
//************************************************************************************************
    private StructSimpleLineSymbol StoreSimpleLine(ISimpleLineSymbol symbol) throws IOException {
        StructSimpleLineSymbol StructStorage = new StructSimpleLineSymbol();
        if (symbol.getStyle() == esriSimpleLineStyle.esriSLSNull)
        {
            StructStorage.Color = "";
        }
        else
        {
            StructStorage.Color = GimmeStringForColor(symbol.getColor());
        }
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.publicStyle =String.valueOf(symbol.getStyle()) ;
        StructStorage.Width=(symbol.getWidth());
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die SimpleLines in seiner Datenstruktur
//************************************************************************************************
    private StructCartographicLineSymbol StoreCartographicLine(ICartographicLineSymbol symbol) throws IOException {
        StructCartographicLineSymbol StructStorage = new StructCartographicLineSymbol();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.Width=(symbol.getWidth());
        StructStorage.Join =String.valueOf(symbol.getJoin()) ;
        StructStorage.MiterLimit = symbol.getMiterLimit();
        StructStorage.Cap =String.valueOf(symbol.getCap());
        StructStorage.DashArray = new java.util.ArrayList();
        if (symbol instanceof ILineProperties)
        {
            ILineProperties lineProperties = null;
            ITemplate template = null;
            double markLen = 0;
            double gapLen = 0;
            double interval = 0;
            int templateIdx = 0;
            lineProperties = (ILineProperties) symbol;
            if (lineProperties.getTemplate() instanceof ITemplate)
            {
                template = lineProperties.getTemplate();
                interval = template.getInterval();
                for (templateIdx = 0; templateIdx <= template.getPatternElementCount() - 1; templateIdx++)
                {
                    template.getPatternElement(templateIdx,new double[]{markLen}, new double[]{gapLen});
                    StructStorage.DashArray.add(markLen * interval);
                    StructStorage.DashArray.add(gapLen * interval);
                }
            }
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die HashLines in seiner Datenstruktur
//************************************************************************************************
    private StructHashLineSymbol StoreHashLine(IHashLineSymbol symbol) throws IOException {
        StructHashLineSymbol StructStorage = new StructHashLineSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        if (LineSymbolScan(symbol.getHashSymbol()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getHashSymbol();
            StructStorage.HashSymbol_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructCartographicLineSymbol;
        }
//ORIGINAL LINE: case "IMarkerLineSymbol":
        else if (LineSymbolScan(symbol.getHashSymbol()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getHashSymbol();
            StructStorage.HashSymbol_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructMarkerLineSymbol;
        }
//ORIGINAL LINE: case "ISimpleLineSymbol":
        else if (LineSymbolScan(symbol.getHashSymbol()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getHashSymbol();
            StructStorage.HashSymbol_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructSimpleLineSymbol;
        }
//ORIGINAL LINE: case "IPictureLineSymbol":
        else if (LineSymbolScan(symbol.getHashSymbol()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getHashSymbol();
            StructStorage.HashSymbol_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructPictureLineSymbol;
        }
//ORIGINAL LINE: case "IMultiLayerLineSymbol":
        else if (LineSymbolScan(symbol.getHashSymbol()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getHashSymbol();
            StructStorage.HashSymbol_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructMultilayerLineSymbol;
        }
//ORIGINAL LINE: case "false":
        else if (LineSymbolScan(symbol.getHashSymbol()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreHashLine");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die MarkerLines in seiner Datenstruktur
//************************************************************************************************
    private StructMarkerLineSymbol StoreMarkerLine(IMarkerLineSymbol symbol) throws IOException {
        StructMarkerLineSymbol StructStorage = new StructMarkerLineSymbol();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.Width=(symbol.getWidth());
        if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("ISimpleMarkerSymbol"))
        {
            ISimpleMarkerSymbol SMS = null;
            SMS = (ISimpleMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_SimpleMarker = StoreSimpleMarker(SMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructSimpleMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("ICharacterMarkerSymbol"))
        {
            ICharacterMarkerSymbol CMS = null;
            CMS = (ICharacterMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_CharacterMarker = StoreCharacterMarker(CMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructCharacterMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IPictureMarkerSymbol"))
        {
            IPictureMarkerSymbol PMS = null;
            PMS = (IPictureMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_PictureMarker = StorePictureMarker(PMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructPictureMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IArrowMarkerSymbol"))
        {
            IArrowMarkerSymbol AMS = null;
            AMS = (IArrowMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_ArrowMarker = StoreArrowMarker(AMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructArrowMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IMultiLayerMarkerSymbol"))
        {
            IMultiLayerMarkerSymbol MLMS = null;
            MLMS = (IMultiLayerMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_MultilayerMarker = StoreMultiLayerMarker(MLMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructMultilayerMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMarkerLine");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die PictureLines in seiner Datenstruktur
//************************************************************************************************
    private StructPictureLineSymbol StorePictureLine(IPictureLineSymbol symbol) throws IOException {
        StructPictureLineSymbol StructStorage = new StructPictureLineSymbol();
        StructStorage.BackgroundColor = GimmeStringForColor(symbol.getBackgroundColor());
        StructStorage.BackgroundTransparency = symbol.getBackgroundColor().getTransparency();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.Offset = symbol.getOffset();
        StructStorage.Picture = (IPicture) symbol.getPicture();
        StructStorage.Rotate = symbol.isRotate();
        StructStorage.Width=(symbol.getWidth());
        StructStorage.XScale = symbol.getXScale();
        StructStorage.YScale = symbol.getYScale();
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die MultilayerLines in seiner Datenstruktur
//************************************************************************************************
    private StructMultilayerLineSymbol StoreMultilayerLines(IMultiLayerLineSymbol symbol) throws IOException {
        StructMultilayerLineSymbol StructStorage = new StructMultilayerLineSymbol();
        StructStorage.MultiLineLayers = new java.util.ArrayList();
        int i = 0;
        StructStorage.LayerCount = symbol.getLayerCount();
        for (i = 0; i <= symbol.getLayerCount() - 1; i++)
        {
            if (LineSymbolScan(symbol.getLayer(i)).equals("ICartographicLineSymbol"))
            {
                ICartographicLineSymbol CLS = null;
                CLS = (ICartographicLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StoreCartographicLine(CLS).clone());
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("IHashLineSymbol"))
            {
                IHashLineSymbol HLS = null;
                HLS = (IHashLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StoreHashLine(HLS).clone());
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("IMarkerLineSymbol"))
            {
                IMarkerLineSymbol MLS = null;
                MLS = (IMarkerLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StoreMarkerLine(MLS).clone());
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("ISimpleLineSymbol"))
            {
                ISimpleLineSymbol SLS = null;
                SLS = (ISimpleLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StoreSimpleLine(SLS).clone());
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("IPictureLineSymbol"))
            {
                IPictureLineSymbol PLS = null;
                PLS = (IPictureLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StorePictureLine(PLS).clone());
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("IMultiLayerLineSymbol"))
            {
                IMultiLayerLineSymbol MLLS = null;
                MLLS = (IMultiLayerLineSymbol) symbol.getLayer(i);
                StructStorage.MultiLineLayers.add(StoreMultilayerLines(MLLS).clone()); //Hier ist ein rekursiver Aufruf
            }
            else if (LineSymbolScan(symbol.getLayer(i)).equals("false"))
            {
                InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMultilayerLines");
            }
        }
        return StructStorage;
    }
    private StructSimpleFillSymbol StoreSimpleFill(ISimpleFillSymbol symbol) throws IOException {
        StructSimpleFillSymbol StructStorage = new StructSimpleFillSymbol();
        if (symbol.getStyle() == esriSimpleFillStyle.esriSFSHollow)
        {
            StructStorage.Color = "";
        }
        else
        {
            StructStorage.Color = GimmeStringForColor(symbol.getColor());
        }
        StructStorage.Style =String.valueOf(symbol.getStyle()) ;
        StructStorage.Transparency = symbol.getColor().getTransparency();
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreSimpleFill");
        }
        return StructStorage;
    }


    //************************************************************************************************
//Die Funktion speichert die MarkerFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructMarkerFillSymbol StoreMarkerFill(IMarkerFillSymbol symbol) throws IOException {
        StructMarkerFillSymbol StructStorage = new StructMarkerFillSymbol();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.GridAngle = symbol.getGridAngle();
        if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("ISimpleMarkerSymbol"))
        {
            ISimpleMarkerSymbol SMS = null;
            SMS = (ISimpleMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_SimpleMarker = StoreSimpleMarker(SMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructSimpleMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("ICharacterMarkerSymbol"))
        {
            ICharacterMarkerSymbol CMS = null;
            CMS = (ICharacterMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_CharacterMarker = StoreCharacterMarker(CMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructCharacterMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IPictureMarkerSymbol"))
        {
            IPictureMarkerSymbol PMS = null;
            PMS = (IPictureMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_PictureMarker = StorePictureMarker(PMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructPictureMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IArrowMarkerSymbol"))
        {
            IArrowMarkerSymbol AMS = null;
            AMS = (IArrowMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_ArrowMarker = StoreArrowMarker(AMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructArrowMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("IMultiLayerMarkerSymbol"))
        {
            IMultiLayerMarkerSymbol MLMS = null;
            MLMS = (IMultiLayerMarkerSymbol) symbol.getMarkerSymbol();
            StructStorage.MarkerSymbol_MultilayerMarker = StoreMultiLayerMarker(MLMS).clone();
            StructStorage.kindOfMarkerStruct = MarkerStructs.StructMultilayerMarkerSymbol;
        }
        else if (MarkerSymbolScan(symbol.getMarkerSymbol()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMarkerFill");
        }
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMarkerFill");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die LineFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructLineFillSymbol StoreLineFill(ILineFillSymbol symbol) throws IOException {
        StructLineFillSymbol StructStorage = new StructLineFillSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.Offset = symbol.getOffset();
        StructStorage.Separation = symbol.getSeparation();
        if (LineSymbolScan(symbol.getLineSymbol()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getLineSymbol();
            StructStorage.LineSymbol_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfLineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getLineSymbol()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreLineFill");
        }
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreLineFill");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die DotDensityFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructDotDensityFillSymbol StoreDotDensityFill(IDotDensityFillSymbol symbol) throws IOException {
        StructDotDensityFillSymbol StructStorage = new StructDotDensityFillSymbol();
        ISymbolArray objSymbolArray = null;
        StructStorage.SymbolList = new java.util.ArrayList();
        objSymbolArray = (ISymbolArray) symbol;
        int i = 0;
        StructStorage.BackgroundColor = GimmeStringForColor(symbol.getBackgroundColor());
        StructStorage.BackgroundTransparency = symbol.getBackgroundColor().getTransparency();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.FixedPlacement = symbol.isFixedPlacement();
        StructStorage.DotSpacing = symbol.getDotSpacing();
        StructStorage.SymbolCount = objSymbolArray.getSymbolCount();

        for (i = 0; i <= objSymbolArray.getSymbolCount() - 1; i++)
        {
            if (objSymbolArray.getSymbol(i) instanceof IMarkerSymbol) //Nur der Marker macht hier als Symbol 黚erhaupt Sinn
            {
                IMarkerSymbol MS = null;
                MS = (IMarkerSymbol) objSymbolArray.getSymbol(i);
                StructStorage.SymbolList = new java.util.ArrayList();
                if (MarkerSymbolScan(MS).equals("ISimpleMarkerSymbol"))
                {
                    ISimpleMarkerSymbol SMS = null;
                    SMS = (ISimpleMarkerSymbol) MS;
                    StructStorage.SymbolList.add(StoreSimpleMarker(SMS).clone());
                    StructStorage.SymbolList.add(symbol.getDotCount(i));
                }
                else if (MarkerSymbolScan(MS).equals("ICharacterMarkerSymbol"))
                {
                    ICharacterMarkerSymbol CMS = null;
                    CMS = (ICharacterMarkerSymbol) MS;
                    StructStorage.SymbolList.add(StoreCharacterMarker(CMS).clone());
                    StructStorage.SymbolList.add(symbol.getDotCount(i));
                }
                else if (MarkerSymbolScan(MS).equals("IPictureMarkerSymbol"))
                {
                    IPictureMarkerSymbol PMS = null;
                    PMS = (IPictureMarkerSymbol) MS;
                    StructStorage.SymbolList.add(StorePictureMarker(PMS).clone());
                    StructStorage.SymbolList.add(symbol.getDotCount(i));
                }
                else if (MarkerSymbolScan(MS).equals("IArrowMarkerSymbol"))
                {
                    IArrowMarkerSymbol AMS = null;
                    AMS = (IArrowMarkerSymbol) MS;
                    StructStorage.SymbolList.add(StoreArrowMarker(AMS).clone());
                    StructStorage.SymbolList.add(symbol.getDotCount(i));
                }
                else if (MarkerSymbolScan(MS).equals("IMultiLayerMarkerSymbol"))
                {
                    IMultiLayerMarkerSymbol MLMS = null;
                    MLMS = (IMultiLayerMarkerSymbol) MS;
                    StructStorage.SymbolList.add(StoreMultiLayerMarker(MLMS).clone());
                    StructStorage.SymbolList.add(symbol.getDotCount(i));
                }
                else if (MarkerSymbolScan(MS).equals("false"))
                {
                    InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreDotDensityFill");
                }
            }
        }

        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreLineFill");
        }

        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die PictureFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructPictureFillSymbol StorePictureFill(IPictureFillSymbol symbol) throws IOException {
        StructPictureFillSymbol StructStorage = new StructPictureFillSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.BackgroundColor = this.GimmeStringForColor(symbol.getBackgroundColor());
        StructStorage.BackgroundTransparency = symbol.getBackgroundColor().getTransparency();
        StructStorage.Color = this.GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        //objPicture = Microsoft.VisualBasic.Compatibility.VB6.IPictureDispToImage(symbol.Picture) 'doesn't work with esri
        //StructStorage.Picture = symbol.Picture.
        StructStorage.XScale = symbol.getXScale();
        StructStorage.YScale = symbol.getYScale();
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StorePictureFill");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die GradientFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructGradientFillSymbol StoreGradientFill(IGradientFillSymbol symbol) throws IOException {
        StructGradientFillSymbol StructStorage = new StructGradientFillSymbol();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Transparency = symbol.getColor().getTransparency();
        StructStorage.Colors = GimmeArrayListForColorRamp(symbol.getColorRamp());
        StructStorage.GradientAngle = symbol.getGradientAngle();
        StructStorage.GradientPercentage = symbol.getGradientPercentage();
        StructStorage.IntervallCount = symbol.getIntervalCount();
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StorePictureFill");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die MultiLayerFillSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructMultilayerFillSymbol StoreMultiLayerFill(IMultiLayerFillSymbol symbol) throws IOException {
        StructMultilayerFillSymbol StructStorage = new StructMultilayerFillSymbol();
        StructStorage.LayerCount = symbol.getLayerCount();
        StructStorage.MultiFillLayers = new java.util.ArrayList();
        int i = 0;
        for (i = 0; i <= symbol.getLayerCount() - 1; i++)
        {
            if (FillSymbolScan(symbol.getLayer(i)).equals("ISimpleFillSymbol"))
            {
                ISimpleFillSymbol SFS = null;
                SFS = (ISimpleFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StoreSimpleFill(SFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("IMarkerFillSymbol"))
            {
                IMarkerFillSymbol MFS = null;
                MFS = (IMarkerFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StoreMarkerFill(MFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("ILineFillSymbol"))
            {
                ILineFillSymbol LFS = null;
                LFS = (ILineFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StoreLineFill(LFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("IPictureFillSymbol"))
            {
                IPictureFillSymbol PFS = null;
                PFS = (IPictureFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StorePictureFill(PFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("IDotDensityFillSymbol"))
            {
                IDotDensityFillSymbol DFS = null;
                DFS = (IDotDensityFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StoreDotDensityFill(DFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("IGradientFillSymbol"))
            {
                IGradientFillSymbol GFS = null;
                GFS = (IGradientFillSymbol) symbol.getLayer(i);
                StructStorage.MultiFillLayers.add(StoreGradientFill(GFS).clone());
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("IMultiLayerFillSymbol"))
            {
                IMultiLayerFillSymbol MLFS = null;
                MLFS = symbol;
                StructStorage.MultiFillLayers.add(StoreMultiLayerFill(MLFS).clone()); //Hier ist ein rekursiver Aufruf
            }
            else if (FillSymbolScan(symbol.getLayer(i)).equals("false"))
            {
                InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreMultilayerFill");
            }
        }
        return StructStorage;
    }

//_______________________________________________________________________________________________________________________

    //************************************************************************************************
//Die Funktion speichert die BarChartSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructBarChartSymbol StoreBarChart(IBarChartSymbol symbol) throws IOException {
        StructBarChartSymbol StructStorage = new StructBarChartSymbol();
        StructStorage.ShowAxes = symbol.isShowAxes();
        StructStorage.Spacing = symbol.getSpacing();
        StructStorage.VerticalBars = symbol.isVerticalBars();
        StructStorage.Width=(symbol.getWidth());
        if (LineSymbolScan(symbol.getAxes()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getAxes();
            StructStorage.Axes_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getAxes();
            StructStorage.Axes_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getAxes();
            StructStorage.Axes_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getAxes();
            StructStorage.Axes_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getAxes();
            StructStorage.Axes_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getAxes();
            StructStorage.Axes_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfAxeslineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getAxes()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StoreBarChart");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die PieChartSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructPieChartSymbol StorePieChart(IPieChartSymbol symbol) throws IOException {
        StructPieChartSymbol StructStorage = new StructPieChartSymbol();
        StructStorage.Clockwise = symbol.isClockwise();
        StructStorage.UseOutline = symbol.isUseOutline();
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StorePictureFill");
        }
        return StructStorage;
    }

    //************************************************************************************************
//Die Funktion speichert die StackedChartSymbols in seiner Datenstruktur
//************************************************************************************************
    private StructStackedChartSymbol StoreStackedChart(IStackedChartSymbol symbol) throws IOException {
        StructStackedChartSymbol StructStorage = new StructStackedChartSymbol();
        StructStorage.Fixed = symbol.isFixed();
        StructStorage.UseOutline = symbol.isUseOutline();
        StructStorage.VerticalBar = symbol.isVerticalBar();
        StructStorage.Width=(symbol.getWidth());
        if (LineSymbolScan(symbol.getOutline()).equals("ICartographicLineSymbol"))
        {
            ICartographicLineSymbol CLS = null;
            CLS = (ICartographicLineSymbol) symbol.getOutline();
            StructStorage.Outline_CartographicLine = StoreCartographicLine(CLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructCartographicLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMarkerLineSymbol"))
        {
            IMarkerLineSymbol MLS = null;
            MLS = (IMarkerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MarkerLine = StoreMarkerLine(MLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMarkerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IHashLineSymbol"))
        {
            IHashLineSymbol HLS = null;
            HLS = (IHashLineSymbol) symbol.getOutline();
            StructStorage.Outline_HashLine = StoreHashLine(HLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructHashLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("ISimpleLineSymbol"))
        {
            ISimpleLineSymbol SLS = null;
            SLS = (ISimpleLineSymbol) symbol.getOutline();
            StructStorage.Outline_SimpleLine = StoreSimpleLine(SLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructSimpleLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IPictureLineSymbol"))
        {
            IPictureLineSymbol PLS = null;
            PLS = (IPictureLineSymbol) symbol.getOutline();
            StructStorage.Outline_PictureLine = StorePictureLine(PLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructPictureLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("IMultiLayerLineSymbol"))
        {
            IMultiLayerLineSymbol MLLS = null;
            MLLS = (IMultiLayerLineSymbol) symbol.getOutline();
            StructStorage.Outline_MultiLayerLines = StoreMultilayerLines(MLLS).clone();
            StructStorage.kindOfOutlineStruct = LineStructs.StructMultilayerLineSymbol;
        }
        else if (LineSymbolScan(symbol.getOutline()).equals("false"))
        {
            InfoMsg("Seit Erstellen der Programmversion ist eine neue Symbolvariante zu den esri-Symbolen hinzugekommen", "StorePictureFill");
        }
        return StructStorage;
    }

//_______________________________________________________________________________________________________________________

    //************************************************************************************************
//Die Funktion speichert TextSymbol in seiner Datenstruktur
//************************************************************************************************
    private StructTextSymbol StoreText(ITextSymbol symbol) throws IOException {
        StructTextSymbol StructStorage = new StructTextSymbol();
        StructStorage.Angle = symbol.getAngle();
        StructStorage.Color = GimmeStringForColor(symbol.getColor());
        StructStorage.Font = symbol.getFont().getName();
        StructStorage.Style = "normal";
        if (symbol.getFont().getItalic())
        {
            StructStorage.Style = "italic";
        }
        StructStorage.Weight = "normal";
        if (symbol.getFont().getBold())
        {
            StructStorage.Weight = "bold";
        }
        StructStorage.HorizontalAlignment =String.valueOf(symbol.getHorizontalAlignment()) ;
        StructStorage.RightToLeft = symbol.isRightToLeft();
        StructStorage.Size = symbol.getSize();
        StructStorage.Text=(symbol.getText());
        StructStorage.VerticalAlignment = String.valueOf(symbol.getVerticalAlignment());
        return StructStorage;
    }

    private boolean CentralProcessingFunc()
    {
        LogRecord.infoMsg("Analysis of the ArcMap-Project is running");
        boolean blnAnswer = false;
        OutputSLD objOutputSLD;

        if (GetProcesses() == false)
        {
            MyTermination();
            return false;
        }
        if (GetApplication() == false)
        {
            MyTermination();
            return false;
        }
        if (GetMap() == false)
        {
            MyTermination();
            return false;
        }
        if (AnalyseLayerSymbology() == false)
        {
            MyTermination();
            return false;
        }
        if (m_cFilename== null || m_cFilename.equals(""))
        {
            LogRecord.infoMsg("Analysis of the ArcMap-Project has finished");
            LogRecord.showMsg("You haven't specified an SLD store location until now. If you don't specify a location"+ ",the application will be terminated." + "\r\n" + "Do you want to specify a location now?");

            if (blnAnswer)
            {
                File file=new File(mainFormProcess.getmSLDFilename());
                if(file.exists()){
                    file.delete();
                    objOutputSLD = new OutputSLD(mainFormProcess, this, m_cFilename);
                }
                else
                {
                    MyTermination();
                }
            }
            else
            {
                MyTermination();
            }
        }
        else
        {
            objOutputSLD = new OutputSLD(mainFormProcess, this, m_cFilename); //Aufruf der Ausgabeklasse
        }
        mainFormProcess.ReadBackValues(); //Liest die Benutzerdefinierten Einstellungen in die XML-Datei zur點k
        return true;
    }

    private boolean GetProcesses()
    {
        boolean bSwitch = false; //der Schalter ist notwendig, weil eine ganze Anzahl Prozesse durchlaufen wird
        LogRecord.infoMsg("Searching an ArcMap process");
        try
        {
            if (ProcessTool.hasProcessRunByName("ARCMAP"))
            {
                return true;
            }
            else
            {
                LogRecord.showMsg("You must open ArcMap first");
                return false;
            }
        }
        catch (RuntimeException ex)
        {
            ErrorMsg("Fehler beim Durchsehen der laufenden Prozesse auf dem System", String.valueOf(ex.getMessage()), ex.getStackTrace(), "GetProcesses");
            return false;
        }
    }

    private boolean GetApplication()
    {
        long Zahl = 0;
        m_ObjApp = null;
        LogRecord.infoMsg("get reference on running ArcMap-session");
        try
        {
            m_ObjAppROT = new AppROT();
            Zahl = m_ObjAppROT.getCount();

            if (Zahl > 1)
            {
                LogRecord.showMsg("You started several ArcMap-sessions at one time."+ "Please close all sessions except of that, you want to analyse and" + " start the application again!");
            }
            else
            {
                if (m_ObjAppROT.getItem(0) instanceof IMxApplication) //躡erpr黤ung, ob das richtige Objekt erhalten wurde
                {
                    m_ObjApp = m_ObjAppROT.getItem(0);
                    m_ObjDoc = (IMxDocument) m_ObjApp.getDocument();
                    m_ObjObjectCreator = (IObjectFactory) m_ObjApp;
                    return true;
                }
            }

        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim Referenzieren auf die ArcMap-Instanz ", String.valueOf(ex.getMessage()), ex.getStackTrace(), "GetApplication");
            return false;
        }
        return true;
    }
    private boolean GetMap()
    {
        LogRecord.infoMsg("Reference on the current Session");
        try
        {
            if (m_ObjDoc.getMaps().getCount() > 1) //Wenn mehr es mehr als eine Karte in dem Dokument gibt
            {
//                 if (frmMotherform.m_enumLang == Motherform.Language.English)
//                {
//                    if (JOptionPane.showConfirmDialog(null, "Is that current map this one you want to turn into SLD? " + "if yes push 'yes' if no push 'no' and choose the right map in ArcMap " + "and use that procedure again", "Choice of the right map", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
//                    {
//                        m_ObjMap = m_ObjDoc.FocusMap;
//                        return true;
//                    }
//                    else
//                    {
//                        return false;
//                    }
//                }
            }
            else
            {
                m_ObjMap = m_ObjDoc.getFocusMap();
                return true;
            }
        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim Erhalten des aktuellen Kartenobjekts ", String.valueOf(ex.getMessage()), ex.getStackTrace(), "GetMap");
            return false;
        }
        return true;
    }
    private boolean AnalyseLayerSymbology()
    {
        ILayer objLayer = null; //Die Schnittstelle zum aktuellen Layer
        int iNumberLayers = 0; //Die Anzahl aller Layer
        String cLayerName = ""; //Der Name des aktuellen Layers
        ISymbol objFstOrderSymbol; //Das ISymbol des entsprechenden Renderers
        m_StrProject = new StructProject(); //Projektstruct wird hier initialisiert
        m_StrProject.LayerList = new java.util.ArrayList();
        try
        {
            iNumberLayers = m_ObjMap.getLayerCount();
            int i = 0;
            for (i = 0; i <= iNumberLayers - 1; i++)
            {
                objLayer = m_ObjMap.getLayer(i);
                cLayerName = String.valueOf(objLayer.getName());
                if (mainFormProcess.ismAllLayers() == false && objLayer.isVisible() == false)
                {
                    LogRecord.infoMsg("Layer " + cLayerName + " is skipped, because it is not visible");
                }
                else
                {
                    LogRecord.infoMsg("Layer " + cLayerName + " is beeing analysed");
                    SpreadLayerStructure(objLayer);
                }
                LogRecord.infoMsg("");
            }
            return true;
        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler bei der Analyse des esri-Projekts", String.valueOf(ex.getMessage()), ex.getStackTrace(), "AnalyseLayerSymbology");
            ErrorMsg("Exception in ArcMap project analysis", String.valueOf(ex.getMessage()), ex.getStackTrace(), "AnalyseLayerSymbology");
            return false;
        }
    }
    private boolean SpreadLayerStructure(ILayer objLayer)
    {

        try
        {
            //recursive call if the current layer is a group layer. Solution is that Group Layers will be ignored
            if (objLayer instanceof IGroupLayer)
            {
                int j = 0;
                IGroupLayer objGRL = null;
                ICompositeLayer objCompLayer = null;
                objGRL = (IGroupLayer) objLayer;
                objCompLayer = (ICompositeLayer) objGRL;
                for (j = 0; j <= objCompLayer.getCount() - 1; j++)
                {
                    //'Because of this if clause group Layer will be ignored
                    //If TypeOf objCompLayer.Layer(j) Is IFeatureLayer Then

                    //End If
                    SpreadLayerStructure(objCompLayer.getLayer(j)); //recursive call

                }
            }
            else if (objLayer instanceof IFeatureLayer)
            {
                if (objLayer instanceof IGeoFeatureLayer)
                {
                    IGeoFeatureLayer objGFL = null;
                    objGFL = (IGeoFeatureLayer) objLayer;
                    //Hier die Unterscheidung der Renderertypen
                    if (objGFL.getRenderer() instanceof IUniqueValueRenderer)
                    {
                        IUniqueValueRenderer objRenderer = null;
                        objRenderer = (IUniqueValueRenderer) objGFL.getRenderer();
                        m_StrProject.LayerList.add(StoreStructUVRenderer(objRenderer, (IFeatureLayer)objLayer).clone());
                        AddOneToLayerNumber();
                    }
                    if (objGFL.getRenderer() instanceof ISimpleRenderer)
                    {
                        ISimpleRenderer objRenderer = null;
                        objRenderer = (ISimpleRenderer) objGFL.getRenderer();
                        m_StrProject.LayerList.add(StoreStructSimpleRenderer(objRenderer, (IFeatureLayer)objLayer).clone());
                        AddOneToLayerNumber();
                    }
                    if (objGFL.getRenderer() instanceof IClassBreaksRenderer)
                    {
                        IClassBreaksRenderer objRenderer = null;
                        objRenderer = (IClassBreaksRenderer) objGFL.getRenderer();
                        m_StrProject.LayerList.add(StoreStructCBRenderer(objRenderer, (IFeatureLayer) objLayer).clone());
                        AddOneToLayerNumber();
                    }

                }

            }
            else
            {
                    InfoMsg("The kind of Layer you use in your ArcMap project is currently not beeing supported.", "SpreadLayerStructure");

                MyTermination();
            }
        }
        catch (Exception e)
        {
            InfoMsg("Unexpected Error during storing in layerstructs", "SpreadLayerStructure");

        }

        return true;

    }


    private String MarkerSymbolScan(IMarkerSymbol Symbol)
    {
        String cValue = "";
        if (Symbol instanceof ISimpleMarkerSymbol)
        {
            cValue = "ISimpleMarkerSymbol";
            return cValue;
        }
        else if (Symbol instanceof ICartographicMarkerSymbol)
        {
            ICartographicMarkerSymbol ICMS = null;
            ICMS = (ICartographicMarkerSymbol) Symbol;
            if (ICMS instanceof ICharacterMarkerSymbol)
            {
                cValue = "ICharacterMarkerSymbol";
                return cValue;
            }
            else if (ICMS instanceof IPictureMarkerSymbol)
            {
                cValue = "IPictureMarkerSymbol";
                return cValue;
            }
        }
        else if (Symbol instanceof IArrowMarkerSymbol)
        {
            cValue = "IArrowMarkerSymbol";
            return cValue;
        }
        else if (Symbol instanceof IMultiLayerMarkerSymbol)
        {
            cValue = "IMultiLayerMarkerSymbol";
            return cValue;
        }
        else
        {
            cValue = "false";
            return cValue;
        }
        return cValue;
    }
    private String LineSymbolScan(ILineSymbol Symbol)
    {
        String cValue = "";
        boolean bSwitch;
        bSwitch = false;
        if (Symbol instanceof ICartographicLineSymbol)
        {
            ICartographicLineSymbol ICLS = null;
            ICLS = (ICartographicLineSymbol) Symbol;
            if (ICLS instanceof IHashLineSymbol)
            {
                cValue = "IHashLineSymbol";
                bSwitch = true;
                return cValue;
            }
            else if (ICLS instanceof IMarkerLineSymbol)
            {
                cValue = "IMarkerLineSymbol";
                bSwitch = true;
                return cValue;
            }
            if (bSwitch == false)
            {
                cValue = "ICartographicLineSymbol";
                return cValue;
            }
        }
        else if (Symbol instanceof ISimpleLineSymbol)
        {
            cValue = "ISimpleLineSymbol";
            return cValue;
        }
        else if (Symbol instanceof IPictureLineSymbol)
        {
            cValue = "IPictureLineSymbol";
            return cValue;
        }
        else if (Symbol instanceof IMultiLayerLineSymbol)
        {
            cValue = "IMultiLayerLineSymbol";
            return cValue;
        }
        else
        {
            cValue = "false";
            return cValue;
        }
        return cValue;
    }


    //************************************************************************************************
//Wenn das aktuelle Symbol ein FillSymbol ist, wird das FillSymbol, und alle darunter liegenden
//Objekte (Symbolobjekte) durchgesucht
//************************************************************************************************
    private String FillSymbolScan(IFillSymbol Symbol)
    {
        String cValue = "";
        if (Symbol instanceof ISimpleFillSymbol)
        {
            cValue = "ISimpleFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof IMarkerFillSymbol)
        {
            cValue = "IMarkerFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof ILineFillSymbol)
        {
            cValue = "ILineFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof IDotDensityFillSymbol)
        {
            cValue = "IDotDensityFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof IPictureFillSymbol)
        {
            cValue = "IPictureFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof IGradientFillSymbol)
        {
            cValue = "IGradientFillSymbol";
            return cValue;
        }
        else if (Symbol instanceof IMultiLayerFillSymbol)
        {
            cValue = "IMultiLayerFillSymbol";
            return cValue;
        }
        else
        {
            cValue = "false";
            return cValue;
        }
    }


    //************************************************************************************************
//Wenn das aktuelle Symbol ein 3DChartSymbol ist, wird das 3DChartSymbol, und alle darunter liegenden
//Objekte (Symbolobjekte) durchgesucht
//************************************************************************************************
    private String IIIDChartSymbolScan(I3DChartSymbol Symbol)
    {
        String cValue = "";
        if (Symbol instanceof IBarChartSymbol)
        {
            cValue = "IBarChartSymbol";
            return cValue;
        }
        else if (Symbol instanceof IPieChartSymbol)
        {
            cValue = "IPieChartSymbol";
            return cValue;
        }
        else if (Symbol instanceof IStackedChartSymbol)
        {
            cValue = "IStackedChartSymbol";
            return cValue;
        }
        else
        {
            cValue = "false";
            return cValue;
        }
    }
    private boolean GimmeUniqeValuesForFieldname(ITable Table, String FieldName)
    {
        IQueryDef pQueryDef = null;
        IRow pRow = null;
        ICursor pCursor = null;
        IFeatureCursor pFeatureCursor;
        IFeatureWorkspace pFeatureWorkspace = null;
        IDataset pDataset = null;
        java.util.ArrayList alUniqueVal = new java.util.ArrayList();


        try
        {
            pDataset = (IDataset) Table;
            pFeatureWorkspace = (IFeatureWorkspace) pDataset.getWorkspace();
            pQueryDef = pFeatureWorkspace.createQueryDef();
            pQueryDef.setTables(pDataset.getName());
            pQueryDef.setSubFields("DISTINCT(" + FieldName + ")");
            pCursor = pQueryDef.evaluate();
            //Hier wird die erhaltene Spalte durchlaufen und in der Arraylist abgespeichert
            pRow = pCursor.nextRow();
            while (pRow!=null)
            {
                alUniqueVal.add(pRow.getValue(0));
                pRow = pCursor.nextRow();
            }


            m_alClassifiedFields.add(alUniqueVal);

        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim Generieren der UniqueValues", String.valueOf(ex.getMessage()), String.valueOf(ex.getStackTrace()), "GimmeUniqeValuesForFieldname");
        }
        return true;
    }


    //************************************************************************************************
//Die Funktion gibt eine Liste mit UniqeValues zu dem angegebenen Feld aus
//Parameter:
//           Table: Das FeatureTable-Objekt
//           FieldName: Der Spaltenname der betroffenen Spalte, nach der klassifiziert wurde
//           JoinedTables: Eine Arraylist mit den Namen aller an die Haupttabelle drangejointen Tabellen
//************************************************************************************************
    private boolean GimmeUniqeValuesForFieldname(ITable Table, String FieldName, java.util.ArrayList JoinedTables)
    {
        IQueryDef pQueryDef = null;
        IRow pRow = null;
        ICursor pCursor = null;
        IFeatureWorkspace pFeatureWorkspace = null;
        IDataset pDataset = null;
        java.util.ArrayList alUniqueVal = new java.util.ArrayList();


        try
        {
            String cMember = "";
            for (Object tempLoopVar_cMember : JoinedTables)
            {
                cMember = tempLoopVar_cMember.toString();
                cMember = "," + cMember;
            }

            pDataset = (IDataset) Table;
            pFeatureWorkspace = (IFeatureWorkspace) pDataset.getWorkspace();
            pQueryDef = pFeatureWorkspace.createQueryDef();
            pQueryDef.setTables(pDataset.getName() + cMember);
            pQueryDef.setSubFields("DISTINCT(" + FieldName + ")");
            pCursor = pQueryDef.evaluate();
            //Hier wird die erhaltene Spalte durchlaufen und in der Arraylist abgespeichert
            pRow = pCursor.nextRow();
            while (pRow!=null)
            {
                alUniqueVal.add(pRow.getValue(0));
                pRow = pCursor.nextRow();
            }
            m_alClassifiedFields.add(alUniqueVal);
            return true;
        }
        catch (Exception ex)
        {
//            JOptionPane.showConfirmDialog(null, ex.getMessage() + " " + ex.Source + " " + ex.StackTrace);
        }
        return true;
    }

    //************************************************************************************************
//Die Funktion gibt eine Liste mit UniqeValues zu dem angegebenen Feld aus f黵 Shape-based Projekte
//Parameter:
//           Table: Das FeatureTable-Objekt
//           FieldName: Der Spaltenname der betroffenen Spalte, nach der klassifiziert wurde
//************************************************************************************************
    private boolean GimmeUniqueValuesFromShape(ITable Table, java.util.ArrayList FieldNames)
    {
        IQueryFilter pQueryFilter = null;

        ICursor pCursor = null;
        IDataStatistics pData = null;

        short i = 0;
        short bla;
        IEnumVARIANT objEnum = null;
        java.util.ArrayList al = null;
        IEnumVARIANT[] iEnumVARIANTS=null;

        try {
            pQueryFilter = new QueryFilter();
            pData = new DataStatistics();
            for (i = 0; i <= FieldNames.size() - 1; i++) {
                LogRecord.infoMsg("Please wait - classified Field Nr. " + String.valueOf(i + 1) + " of " + FieldNames.size());
                pData.setField(FieldNames.get(i).toString());
                pQueryFilter.setSubFields(FieldNames.get(i).toString());
                pCursor = Table.ITable_search(pQueryFilter, false);
                pData.setCursorByRef(pCursor);
                objEnum = pData.getUniqueValues();
                al = new java.util.ArrayList();
                objEnum.esri_clone(iEnumVARIANTS);
                for(int j=0;j<iEnumVARIANTS.length;j++){
                    al.add(iEnumVARIANTS[j]);
                }
                al.sort(new Comparator<IEnumVARIANT>() {
                    @Override
                    public int compare(IEnumVARIANT o1, IEnumVARIANT o2) {
                        return 0;
                    }
                });
                m_alClassifiedFields.add(al);
            }

        }
        catch (Exception ex)
        {
            ErrorMsg("Fehler beim Erstellen der UniqueValues", String.valueOf(ex.getMessage()), String.valueOf(ex.getStackTrace()), "GimmeUniqueValuesFromShape");
            MyTermination();
        }
        return true;
    }

    private boolean SortCursorRowValues(java.util.ArrayList al)
    {
        if ((int) al.size() == 2)
        {
            m_al1.add(al.get(0));
            m_al2.add(al.get(1));
        }
        else if ((int) al.size() == 3)
        {
            m_al1.add(al.get(0));
            m_al2.add(al.get(1));
            m_al3.add(al.get(2));
        }
        return true;
    }

    private java.util.ArrayList GimmeSeperateFieldValues(String value, String FieldDelimiter) throws IOException {
        java.util.ArrayList alSepValues = new java.util.ArrayList();
        IFeatureCursor objFeatCurs;
        IQueryFilter objQueryFilter;
        objQueryFilter = new QueryFilter();
        IFeature objFeature;
        String cCompare;
        int zahl;

        try
        {
            if ((int) m_alClassifiedFields.size() == 2)
            {
                java.util.ArrayList al1 = null;
                java.util.ArrayList al2 = null;
                al1 = (java.util.ArrayList)(m_alClassifiedFields.get(0));
                al2 = (java.util.ArrayList)(m_alClassifiedFields.get(1));
                int i = 0;
                int j = 0;
                for (i = 0; i <= al1.size() - 1; i++)
                {
                    for (j = 0; j <= al2.size() - 1; j++)
                    {
                        if (value.equals(((al1.get(i)).toString() + FieldDelimiter + (al2.get(j)).toString())))
                        {
                            alSepValues.add(al1.get(i));
                            alSepValues.add(al2.get(j));
                        }
                    }
                }
            }
            else if ((int) m_alClassifiedFields.size() == 3)
            {
                java.util.ArrayList al1 = null;
                java.util.ArrayList al2 = null;
                java.util.ArrayList al3 = null;
                al1 = (java.util.ArrayList)(m_alClassifiedFields.get(0));
                al2 = (java.util.ArrayList)(m_alClassifiedFields.get(1));
                al3 = (java.util.ArrayList)(m_alClassifiedFields.get(2));
                int i = 0;
                int j = 0;
                int k = 0;
                for (i = 0; i <= m_alClassifiedFields.get(0).size() - 1; i++)
                {
                    for (j = 0; j <= m_alClassifiedFields.get(1).size() - 1; j++)
                    {
                        for (k = 0; k <= m_alClassifiedFields.get(2).size() - 1; k++)
                        {
                            if (value.equals(((al1.get(i)).toString() + FieldDelimiter + (al2.get(j)).toString() + FieldDelimiter + (al3.get(k)).toString())))
                            {
                                alSepValues.add(al1.get(i));
                                alSepValues.add(al2.get(j));
                                alSepValues.add(al3.get(k));
                            }
                        }
                    }
                }
            }
            endOfSelect:
            return alSepValues;
        }
        catch (RuntimeException ex)
        {
//            JOptionPane.showConfirmDialog(null, ex.getMessage());
        }
        return alSepValues;
    }


    //************************************************************************************************
//Die Funktion gibt die Colors einer ColorRamp als Arraylist zur點k
//************************************************************************************************
    private java.util.ArrayList GimmeArrayListForColorRamp(IColorRamp ColorRamp) throws IOException {
        IEnumColors EColors = null;
        java.util.ArrayList AL = null;
        int i = 0;
        EColors = ColorRamp.getColors();
        for (i = 0; i <= ColorRamp.getSize() - 1; i++)
        {
            AL.add(GimmeStringForColor(EColors.next()));
        }
        return AL;
    }
    private String GimmeStringForColor(IColor color) throws IOException {
        String cCol = "";
        String cRed = "";
        String cGreen = "";
        String cBlue = "";
        IRgbColor objRGB = null;

        if (color.getTransparency() == 0)
        {
            cCol = "";
        }
        else
        {
            objRGB = new RgbColor();

            objRGB.setRGB(color.getRGB());
            cRed = CheckDigits(Integer.toHexString(objRGB.getRed()));
            cGreen = CheckDigits(Integer.toHexString(objRGB.getGreen()));
            cBlue = CheckDigits(Integer.toHexString(objRGB.getBlue()));
            cCol = "#" + cRed + cGreen + cBlue;
        }

        return cCol;
    }
    private String CheckDigits(String value)
    {
        String cReturn = "";
        cReturn = value;
        if (cReturn.length() == 1)
        {
            cReturn = "0"+cReturn;
        }
        return cReturn;
    }

    //************************************************************************************************
//Extract annotation style and property name from a layer.
//This initial implementation only handles the most basic annotation: simple expression of a
//single property, applicable to all features/symbols.
//Return a StructAnnotation. The PropertyName is an empty string if there is no annotation.
//************************************************************************************************
    private StructAnnotation GetAnnotation(IFeatureLayer objLayer) throws IOException {
        StructAnnotation annotation = new StructAnnotation();

        annotation.PropertyName = "";
        if (objLayer instanceof IGeoFeatureLayer)
        {
            IGeoFeatureLayer objGFL = null;
            objGFL = (IGeoFeatureLayer) objLayer;

            IAnnotateLayerPropertiesCollection annoPropsColl = null;
            annoPropsColl = objGFL.getAnnotationProperties();
            if (objGFL.isDisplayAnnotation() && annoPropsColl.getCount()> 0)
            {
                IAnnotateLayerProperties annoLayerProps = null;
//                annoPropsColl.queryItem(0, annoLayerProps);
                if (annoLayerProps instanceof ILabelEngineLayerProperties&& annoLayerProps.isDisplayAnnotation())
                {
                    ILabelEngineLayerProperties labelProps = null;
                    labelProps = (ILabelEngineLayerProperties) annoLayerProps;
                    // For the moment only implement the simplest case
                    if (annoLayerProps.getWhereClause().equals("") && labelProps.isExpressionSimple())
                    {
                        annotation.IsSingleProperty = true;
                        annotation.PropertyName = labelProps.getExpression().replace("[", "").replace("]", "");
                        annotation.TextSymbol = StoreText(labelProps.getSymbol()).clone();
                    }
                }
            }
        }
        return annotation;
    }
    private Object ErrorMsg(String Message, String ExMessage, Object Stack, String FunctionName)
    {
//        JOptionPane.showConfirmDialog(null, Message + "\r\n" + ExMessage + "\r\n" + Stack, "ArcGIS_SLD_Converter | Analize_ArcMap_Symbols | " + FunctionName, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        MyTermination();
        return null;
    }
    private Object InfoMsg(String Message, String FunctionName)
    {
//        JOptionPane.showConfirmDialog(null, Message, "ArcGIS_SLD_Converter | Analize_ArcMap_Symbols | " + FunctionName, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        return null;
    }
    public  boolean MyTermination()
    {
        return true;
    }
}
