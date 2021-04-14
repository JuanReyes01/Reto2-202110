package model.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.IArregloDinamico;
import model.data_structures.ILista;
import model.data_structures.ITablaSimbolos;
import model.data_structures.ListaEncadenada;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.TablaHashSeparateChaining;
import model.data_structures.TablaSimbolos;
import model.utils.Ordenamiento;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	private static final String VIDEO = "./data/videos-all.csv";
	/**
	 * Atributos del modelo del mundo
	 */
	private ILista<Categoria> categorias;
	private ILista<YoutubeVideo> datos;
	private ITablaSimbolos<String, ILista<YoutubeVideo>> tabla;
	private Ordenamiento<YoutubeVideo> o;
    private TablaHashSeparateChaining<String, ILista <YoutubeVideo>> TablaSeparateVideos;
	private TablaHashLinearProbing<String, ILista<YoutubeVideo>> TablaLinearProbingVideos;
	

	
	public Modelo(int capacidad)
	{
		datos = new ArregloDinamico<YoutubeVideo>();
		categorias = new ArregloDinamico<Categoria>();
		tabla = new TablaSimbolos<String,ILista<YoutubeVideo>>();
		o = new Ordenamiento<YoutubeVideo>();
		TablaLinearProbingVideos = new TablaHashLinearProbing<String,ILista<YoutubeVideo>>(capacidad);
		TablaSeparateVideos = new TablaHashSeparateChaining<String, ILista<YoutubeVideo>>(capacidad);
		
	}	
	
	public void cargarDatosConLinearProbing() throws IOException, ParseException{
		Reader in = new FileReader(VIDEO);
		int c = 0;
		long total = 0;
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);	
		for (CSVRecord record : records) {
		    String id = record.get(0);
		    String trending = record.get(1);
		    String titulo = record.get(2);
		    String canal = record.get(3);
		    String YoutubeVideo = record.get(4);
		    String fechaP = record.get(5);
		    String tags = record.get(6);
		    String vistas = record.get(7);
		    String likes  = record.get(8);
		    String dislikes = record.get(9);
		    String coment = record.get(10);
		    String foto = record.get(11);
		    String nComent = record.get(12);
		    String rating = record.get(13);
		    String vidErr = record.get(14);
		    String descripcion = record.get(15);
		    String pais = record.get(16);
		    //--------------------------------------------------------------------
		    if(!id.equals("video_id")){
		    SimpleDateFormat formato = new SimpleDateFormat("yyy/MM/dd");
		    String[] aux = trending.split("\\.");
		    Date fechaT = formato.parse(aux[0]+"/"+aux[2]+"/"+aux[1]);
		    SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");		   
		    Date fechaPu = formato2.parse(fechaP);
		    YoutubeVideo nuevo = new YoutubeVideo(id, fechaT, titulo, canal, Integer.parseInt(YoutubeVideo), fechaPu, tags, Integer.parseInt(vistas), Integer.parseInt(likes), Integer.parseInt(dislikes), Integer.parseInt(coment), foto, (nComent.equals("FALSE")?false:true), (rating.equals("FALSE")?false:true), (vidErr.equals("FALSE")?false:true), descripcion, pais);
		    String cat = darNombreCategoria(nuevo.darId_categoria());
		    String key = nuevo.darPais().trim()+"-"+cat.trim();
		    int aux2 = tabla.keySet().isPresent(key);
		    ILista<YoutubeVideo> lista = TablaLinearProbingVideos.get(key);
		    if(lista != null){ boolean x = false;
		    for(int i = 1; i <= lista.size() && !x; i++){
		    	if(lista.getElement(i).darTitulo().compareToIgnoreCase(titulo)==0){ x = true;
		    } }
		    	if(!x){
		    		lista.addLast(nuevo);
		    		TablaLinearProbingVideos.cambiarValor(key, lista);;
		    		c++;
		    	}
		    }
		    else{
		    	ArregloDinamico<YoutubeVideo> valor = new ArregloDinamico<YoutubeVideo>();
		    	valor.addLast(nuevo);
		    	long miliI = System.currentTimeMillis();
		    	TablaLinearProbingVideos.put(key, valor);
		    	long miliF = System.currentTimeMillis();
		    	total += (miliF-miliI);
		    	//long miliI = System.currentTimeMillis();
		    	//tabla.put(key, valor);
		    	//long miliF = System.currentTimeMillis();
		    	//total	 += (miliF-miliI); 
		    }
		    c++;
		    }
		    System.out.println("Se han cargado los datos \n " + "Video Totales" + c);
		    System.out.println("Tiempo de ejecuci�n promedio en milisegundos :" + (total/(c-1)));
		    System.out.println("Tamanio de la tabla de hash :" + TablaLinearProbingVideos.size());
		}
		//float f = (float) ((total*1.0)/tabla.size());
		//return "Tiempo de ejecuci�n promedio: "+f+" milisegundos, \nTotal llaves: "+ tabla.size()+" \nTotal datos cargados: "+c ;
	}
	
	public void cargarDatosConSeparateChaining() throws IOException, ParseException{
		Reader in = new FileReader(VIDEO);
		int c = 0;
		long total = 0;
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);	
		for (CSVRecord record : records) {
		    String id = record.get(0);
		    String trending = record.get(1);
		    String titulo = record.get(2);
		    String canal = record.get(3);
		    String YoutubeVideo = record.get(4);
		    String fechaP = record.get(5);
		    String tags = record.get(6);
		    String vistas = record.get(7);
		    String likes  = record.get(8);
		    String dislikes = record.get(9);
		    String coment = record.get(10);
		    String foto = record.get(11);
		    String nComent = record.get(12);
		    String rating = record.get(13);
		    String vidErr = record.get(14);
		    String descripcion = record.get(15);
		    String pais = record.get(16);
		    //--------------------------------------------------------------------
		    if(!id.equals("video_id")){
		    SimpleDateFormat formato = new SimpleDateFormat("yyy/MM/dd");
		    String[] aux = trending.split("\\.");
		    Date fechaT = formato.parse(aux[0]+"/"+aux[2]+"/"+aux[1]);
		    SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");		   
		    Date fechaPu = formato2.parse(fechaP);
		    YoutubeVideo nuevo = new YoutubeVideo(id, fechaT, titulo, canal, Integer.parseInt(YoutubeVideo), fechaPu, tags, Integer.parseInt(vistas), Integer.parseInt(likes), Integer.parseInt(dislikes), Integer.parseInt(coment), foto, (nComent.equals("FALSE")?false:true), (rating.equals("FALSE")?false:true), (vidErr.equals("FALSE")?false:true), descripcion, pais);
		    String cat = darNombreCategoria(nuevo.darId_categoria());
		    String key = nuevo.darPais().trim()+"-"+cat.trim();
		    int aux2 = tabla.keySet().isPresent(key);
		    ILista<YoutubeVideo> lista = TablaSeparateVideos.get(key);
		    if(lista != null){ boolean x = false;
		    for(int i = 1; i <= lista.size() && !x; i++){
		    	if(lista.getElement(i).darTitulo().compareToIgnoreCase(titulo)==0){ x = true;
		    } }
		    	if(!x){
		    		lista.addLast(nuevo);
		    		TablaSeparateVideos.cambiarValor(key, lista);
		    		c++;
		    	}
		    }
		    else{
		    	ArregloDinamico<YoutubeVideo> valor = new ArregloDinamico<YoutubeVideo>();
		    	valor.addLast(nuevo);
		    	long miliI = System.currentTimeMillis();
		    	TablaSeparateVideos.put(key, valor);
		    	long miliF = System.currentTimeMillis();
		    	total += (miliF-miliI);
		    	//long miliI = System.currentTimeMillis();
		    	//tabla.put(key, valor);
		    	//long miliF = System.currentTimeMillis();
		    	//total	 += (miliF-miliI); 
		    }
		    c++;
		    }
		    System.out.println("Se han cargado los datos \n " + "Video Totales" + c);
		    System.out.println("Tiempo de ejecuci�n promedio en milisegundos :" + (total/(c-1)));
		    System.out.println("Tamanio de la tabla de hash :" + TablaSeparateVideos.size());
		}
		//float f = (float) ((total*1.0)/tabla.size());
		//return "Tiempo de ejecuci�n promedio: "+f+" milisegundos, \nTotal llaves: "+ tabla.size()+" \nTotal datos cargados: "+c ;
	}

	public void cargarId() throws IOException, FileNotFoundException{
		Reader in = new FileReader("./data/category-id.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);	
		for (CSVRecord record : records) {
			String n = record.get(0);
			if(!n.equals("id	name")){
				String[] x = n.split("	 ");
				Categoria nueva =  new Categoria(Integer.parseInt(x[0]), x[1].trim());
				agregarCategoria(nueva);
			}
		}
		
	}
	
	public ILista<Categoria> darCategorias(){
		return categorias;
	}
	
	public void agregarCategoria(Categoria elem){
		categorias.addLast(elem);
	}
	
	/**
	 * Da el nombre de la categoria segun el numero
	 * @param categoria, numero de la categoria
	 * @return el nombre de la categoria
	 */
	public String darNombreCategoria(int categoria){
		for(int i=1; i<=categorias.size();i++){
			if(categorias.getElement(i).darId()==(categoria)){
				return categorias.getElement(i).darNombre(); 
			}
		}
		return null;
	}
	
	//Busqueda binaria, para practicar
	/**
	 * Metodo que sobreescribe la busqueda que realiza arreglo dinamico con una busqueda binaria
	 * Esto es posible porque la lista de categorias esta ordenada desde que se carga 
	 */
	public Categoria buscarCategoriaBin(int pos){
		int i = 1;
		int f = categorias.size();
		int elem = -1;
		boolean encontro = false;
		while ( i <= f && !encontro )
		{
		int m = (i + f) / 2;
		if ( categorias.getElement(m).darId() == pos )
		{
		elem = m;
		encontro = true;
		}
		else if ( categorias.getElement(m).darId() > pos )
		{
		f = m - 1;
		}
		else
		{
		i = m + 1;
		}
		}
		return categorias.getElement(elem);
	}
	
	public int darTamanioSeparateChaining()
	{
		return TablaSeparateVideos.size();
	}

	public int darTamanoLinearProbing()
	{
		return TablaLinearProbingVideos.size();
	}
	
	//-------------------------------------------------------------------------------------------------------------
	// Requerimientos
	//-------------------------------------------------------------------------------------------------------------

	
	public ILista<YoutubeVideo> req1(String pais, int num, String categoria){
		ILista<YoutubeVideo> x = new ArregloDinamico<YoutubeVideo>(num);
		int z = 0;
		boolean stop = false;
		Comparator<YoutubeVideo> y = new YoutubeVideo.ComparadorXViews();
		ILista<YoutubeVideo> videosDeLaCategoria = TablaSeparateVideos.get(categoria);
		//o.ordenarQuickSort(videosDeLaCategoria, y,false);
		//Determinar el id de la categoria O(N) 
		for(int i=1; i<=categorias.size()&&!stop;i++){
			Categoria actual = categorias.getElement(i);
			if(actual.darNombre().compareToIgnoreCase(categoria)==0){
				if (z >= num){stop = true;}
				else
				{
					if(videosDeLaCategoria.getElement(i).darPais().compareToIgnoreCase(pais)==0)
					{
						z++;
						x.addLast(videosDeLaCategoria.getElement(i));
						o.ordenarQuickSort(x, y, false);}
				}
			}
		}
		return x;
		}
		
		
	
	 public String req2(String pais){    
		 return null;
	 }
	 
	 /**
		 * Busca el video que ha sido mas tendencia en una categoria especifica.
		 * @param categoria Categoria especifica en la que estan los videos.
		 * @return Como respuesta deben aparecer el video con mayor tendencia de la categoria.  
		 */
		public String req3 (String categoria){
			ILista<YoutubeVideo> videosDeLaCategoria = TablaSeparateVideos.get(categoria.toLowerCase());
            Comparator<YoutubeVideo> y = new YoutubeVideo.ComparadorXViews();
            int repeticiones = 1;
			YoutubeVideo repetidoMayorVeces = videosDeLaCategoria.getElement(1); 
			int actual = 1; 
            for (int i = 2; i < videosDeLaCategoria.size(); i++) 
			{ if (y.compare(videosDeLaCategoria.getElement(i),videosDeLaCategoria.getElement(i-1)) == 0) actual++; 
				else 
				{ 
					if (actual > repeticiones) 
					{ 
						repeticiones = actual; 
						repetidoMayorVeces = videosDeLaCategoria.getElement(i-1);
					} 
					actual = 1; 
				} 
			} 
            if (actual > repeticiones) 
			{ 
				repeticiones = actual;
				repetidoMayorVeces = videosDeLaCategoria.getElement(videosDeLaCategoria.size() - 1);
			} 

			
			return (repetidoMayorVeces != null)? " Titulo: "+ repetidoMayorVeces.darTitulo()+"\n Chanel_Title: "+ repetidoMayorVeces.darCanal()+"\n country: "+ repetidoMayorVeces.darPais()+" \n Dias: "+ repetidoMayorVeces:"";
			}
		
		
		

		/**
		 * Busca los n videos con mas views que son tendencia en un determinado pais y que posean la etiqueta designada.
		 * @param num Numero de videos que se desean ver. num > 0
		 * @param etiqueta Tag especifica que tienen los videos. != " " y != null
		 * @return Como respuesta deben aparecer los n videos que cumplen las caracteristicas y su respectiva informacion.  	
		 */
		public ILista<YoutubeVideo> req4(String pais, int num, String etiqueta) {
			ILista<YoutubeVideo> listaVideosFiltrados = new ArregloDinamico<>(10);
			for(int i = 1; i <= categorias.size();i++)
			{ILista<YoutubeVideo> videosDeLaCategoria = TablaSeparateVideos.get(categorias.getElement(i).darNombre().toLowerCase());
				if(videosDeLaCategoria != null)
				{for(int j = 1; j <= videosDeLaCategoria.size();j++)
					{YoutubeVideo actual = videosDeLaCategoria.getElement(j);
						if(actual.darTags().contains(etiqueta))
						{String[] etiquetas = actual.darTags().split("\\|");
							for(int k = 0; k <etiquetas.length-1;k++)
							{if(etiquetas[k].compareToIgnoreCase(etiqueta)==0)
								{listaVideosFiltrados.addLast(actual);
								}
							}
						}
					}
				}
			}
			
			if(!listaVideosFiltrados.isEmpty())
			{
				Comparator<YoutubeVideo> y = new YoutubeVideo.ComparadorXViews();
				o.ordenarQuickSort(listaVideosFiltrados, y, false);
				}
			return listaVideosFiltrados.sublista(num);
		}
			
		
	

public int rand(){
	return (int) (Math.random() * (datos.size())+1);
}

public float pruebaGet(){
	int i = 0;
	long total = 0;
	ILista<String> llaves = tabla.keySet();
	long miliI = 0;
	long miliF = 0;
	while(i<700){
		String key = llaves.getElement(rand());
		miliI = System.currentTimeMillis();
		tabla.get(key);
		miliF = System.currentTimeMillis();
		total+=(miliF-miliI);
		i++;
	}
	

	while(i<1000){
		String key = (char)rand()+""+(char)rand();
		miliI = System.currentTimeMillis();
		tabla.get(key);
		miliF = System.currentTimeMillis();
		total+=(miliF-miliI);
		i++;
	}
	return (float) ((total*1.0)/i);
}
public long pruebaGetSeparateChaining()
{
	long total = 0;
	String[] paises =new String[5];
	paises[0] ="japan";
	paises[1]="germany";
	paises[2]="india";
	paises[3]="canada";
	paises[4]="france";
	for(int i=0;i<1000;i++)
	{
		long start = 0;
		long stop = 0;
		if (i <700)
		{
			int n1 = (int) (Math.random()*5);
			int n2 = (int)(Math.random()*(31)+1);
			String key = (paises[n1] + categorias.getElement(n2).darNombre()).toLowerCase();
			start = System.currentTimeMillis();
			TablaSeparateVideos.get(key);
			stop = System.currentTimeMillis();
			
		}
		else
		{
			int n2 = (int)(Math.random()*(31)+1);
			String key = ("lakaaiaana" + categorias.getElement(n2).darNombre()).toLowerCase();
			
			start = System.currentTimeMillis();
			TablaSeparateVideos.get(key);
			stop = System.currentTimeMillis();
		}
		total += (stop-start);
	}
	return(total);
}

public long pruebaGetLinearProbing()
{
	long total = 0;
	String[] paises =new String[5];
	paises[0] ="japan";
	paises[1]="germany";
	paises[2]="india";
	paises[3]="canada";
	paises[4]="france";
	for(int i=0;i<1000;i++)
	{
		long start = 0;
		long stop = 0;
		if (i <700)
		{
			int n1 = (int) (Math.random()*5);
			int n2 = (int)(Math.random()*(31)+1);
			String key = (paises[n1]+categorias.getElement(n2).darNombre()).toLowerCase();
			start = System.currentTimeMillis();
			TablaLinearProbingVideos.get(key);
			stop = System.currentTimeMillis();
			
		}
		else
		{
			int n2 = (int)(Math.random()*(31)+1);
			String key = ("hola mundo"+categorias.getElement(n2).darNombre()).toLowerCase();
			
			start = System.currentTimeMillis();
			TablaLinearProbingVideos.get(key);
			stop = System.currentTimeMillis();
		}
		total += (stop-start);
	}
	return(total);
}
}



		