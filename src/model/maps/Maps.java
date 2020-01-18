package model.maps;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.Polygon;
import com.teamdev.jxmaps.swing.MapView;
import model.data_structures.HashTable;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Esta clase representa los objetos y metodos basicos
 * necesarios para la realizacion de talleres y proyectos
 * que tengan que ver con dibujo de mapas
 * @author Juan Pablo Cano
 */
public class Maps extends MapView
{
    //-------------------------
    // Atributos
    //------------------------

    /**
     * Mapa cargado de JxMaps
     */
    private Map map;

    /**
     * Tabla de Hash que contiene las universidades en llaves
     * y las Latitudes y Longitudes en Valor
     */
    private HashTable<String, LatLng> table;

    /**
     * Arreglo de latitudes y longitudes
     */
    private LatLng[] locs;

    //-----------------------
    // Constructores
    //------------------------

    /**
     * Constructor de la clase de mapas
     */
    public Maps()
    {
        table = new HashTable<>();
        locs = new LatLng[12];
        try
        {
            // El archivo de locaciones
            BufferedReader br = new BufferedReader(new FileReader("locs.txt"));
            String linea = br.readLine();
            int i = 0;
            while(linea != null)
            {
                // Se parte el archivo a partir de ':'
                String[] partes = linea.split(":");
                String universidad = partes[1];
                String[] location = partes[0].split(",");
                double lat = Double.parseDouble(location[0]);
                double lon = Double.parseDouble(location[1]);
                LatLng latLng = new LatLng(lat, lon);
                table.put(universidad, latLng);
                locs[i] = latLng;
                linea = br.readLine();
                i++;
            }
            br.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        // Se espera que JxMaps cargue el mapa
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status)
            {
                if(status == MapStatus.MAP_STATUS_OK)
                {
                    map = getMap();
                    initMap();

                    /*
                     * Puede cambiar la funcion por otra
                     * Por ejemplo drawPolygon()
                     */
                    drawPolylines();
                }
            }
        });
    }

    /**
     * Metodo que se encarga de dibujar un marcador de posicion
     */
    public void drawMarker()
    {
        for(String universidad: table)
        {
            LatLng loc = table.get(universidad);
            Marker marker = new Marker(map);
            marker.setPosition(loc);

            InfoWindow infoWindow = new InfoWindow(map);
            infoWindow.setContent("Hola "+ universidad);
            infoWindow.open(map, marker);
        }
    }

    /**
     * Metodo que se encarga de dibujar una linea con
     * el comportamiento de un poligono
     */
    public void drawPolygon()
    {
        PolygonOptions po = new PolygonOptions();
        // Grosor de la línea
        po.setStrokeWeight(1);
        // Color de relleno
        po.setFillColor("#FF0000");
        // Opacidad de relleno
        po.setFillOpacity(0.35);

        // Los dos extremos del poligono
        LatLng[] path = new LatLng[2];
        int i = 0;

        for(String universidad : table)
        {
            path[0] = locs[i];
            path[1] = locs[i+1];
            // Objeto de latitud y longitud
            LatLng loc = table.get(universidad);
            // Marcador
            Marker marker = new Marker(map);
            marker.setPosition(loc);

            // Ventana de informacion
            InfoWindow infoWindow = new InfoWindow(map);
            infoWindow.setContent("Hola "+ universidad);
            infoWindow.open(map, marker);

            // Poligono
            Polygon polygon = new Polygon(map);
            // Se le agrega el camino, debe ser un arreglo de LatLng
            polygon.setPath(path);
            // Se colocan las opciones
            polygon.setOptions(po);
            i++;
        }
    }

    /**
     * Método que se encarga de dibujar los circulos
     */
    public void drawCircle()
    {
        // Las opciones de creación del círculo
        CircleOptions co = new CircleOptions();
        // Opacidad de relleno
        co.setFillOpacity(0.35);
        // Ancho de línea
        co.setStrokeWeight(1);
        // Opacidad de linea
        co.setStrokeOpacity(0.2);
        // Color de linea
        co.setStrokeColor("00FF00");

        for(String universidad: table)
        {
            // Objeto de latitud y longitud
            LatLng loc = table.get(universidad);

            // Creación del círculo
            Circle circle = new Circle(map);
            // Centro
            circle.setCenter(loc);
            // Opciones
            circle.setOptions(co);
            // Radio
            circle.setRadius(10);
        }
    }

    /**
     * Método que se encarga de dibujar líneas
     */
    public void drawPolylines()
    {
        // Las opciones de creación del círculo
        PolylineOptions po = new PolylineOptions();
        // Grosor de la línea
        po.setStrokeWeight(1);
        // Opacidad de la línea
        po.setStrokeOpacity(1);

        // Los dos extremos del poligono
        LatLng[] path = new LatLng[2];
        int i = 0;

        for(String universidad: table)
        {
            path[0] = locs[i];
            path[1] = locs[i+1];

            // Objeto de latitud y longitud
            LatLng loc = table.get(universidad);
            // Marcador
            Marker marker = new Marker(map);
            marker.setPosition(loc);

            // Ventana de informacion
            InfoWindow infoWindow = new InfoWindow(map);
            infoWindow.setContent("Hola "+ universidad);
            infoWindow.open(map, marker);

            // Línea que será creada
            Polyline polyline = new Polyline(map);
            // Se le coloca el camino a la línea
            polyline.setPath(path);
            // Se le colocan las opciones a la línea
            polyline.setOptions(po);
            i++;
        }
    }

    /**
     * Inicialia el mapa de JxMaps
     */
    public void initMap()
    {
        // Las opciones del mapa
        MapOptions mapOptions = new MapOptions();
        // Los Controles del mapa
        MapTypeControlOptions controlOptions = new MapTypeControlOptions();
        mapOptions.setMapTypeControlOptions(controlOptions);

        // Centro del mapa
        map.setCenter(new LatLng(4.6012, -74.0657));
        // Zoom del Mapa
        map.setZoom(11.0);
    }

    /**
     * Inicializa el marco donde el mapa se va a cargar
     * @param titulo El título del marco
     */
    public void initFrame(String titulo)
    {
        JFrame frame = new JFrame(titulo);
        frame.setSize(700, 500);
        frame.setVisible(true);
        frame.add(this, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method
     * @param args Parametros no necesarios
     */
    public static void main(String[] args)
    {
        Maps maps = new Maps();
        maps.initFrame("Hola mundo");
    }
}
