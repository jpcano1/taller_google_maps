package model.maps;

import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.Polygon;
import com.teamdev.jxmaps.swing.MapView;
import model.data_structures.HashTable;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Maps extends MapView
{
    //-------------------------
    // Atributos
    //------------------------

    /**
     *
     */
    private Map map;

    /**
     *
     */
    private HashTable<String, LatLng> table;

    /**
     *
     */
    private LatLng[] locs;

    //-----------------------
    // Constructores
    //------------------------

    /**
     *
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
                    initMap(map);

//                    Marker marker = new Marker(map);
//                    marker.setPosition(map.getCenter());
//
//                    InfoWindow infoWindow = new InfoWindow(map);
//                    infoWindow.setContent("Hola Uniandes");
//                    infoWindow.open(map, new LatLng(4.6012, -74.0657));


                    int i = 0;
                    for(String universidad: table)
                    {
                        LatLng loc = table.get(universidad);
                        Circle circle = new Circle(map);
                        circle.setCenter(loc);
                        circle.setOptions(co);
                        circle.setRadius(10);
                        LatLng loc = table.get(universidad);
                        Marker marker = new Marker(map);
                        marker.setPosition(loc);
                        i++;
                    }
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    public void drawMarker(Map map)
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
     *
     * @param map
     */
    public void drawPolygon(Map map)
    {
        PolygonOptions po = new PolygonOptions();
        // Color de relleno
        po.setFillColor("#FF0000");
        // Opacidad de relleno
        po.setFillOpacity(0.35);

        LatLng[] path = new LatLng[2];
        int i = 0;

        for(String universidad : table)
        {
            path[0] = locs[i];
            path[1] = locs[i+1];
            LatLng loc = table.get(universidad);
            Marker marker = new Marker(map);
            marker.setPosition(loc);

            InfoWindow infoWindow = new InfoWindow(map);
            infoWindow.setContent("Hola "+ universidad);
            infoWindow.open(map, marker);

            Polygon polygon = new Polygon(map);
            polygon.setPath(path);
            polygon.setOptions(po);
            i++;
        }
    }

    public void drawCircle(Map map)
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
    }

    /**
     * Inicialia el mapa de JxMaps
     * @param map El mapa que será inicializado
     */
    public void initMap(Map map)
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

    public static void main(String[] args)
    {
        Maps maps = new Maps();
        maps.initFrame("Hola mundo");
    }
}
