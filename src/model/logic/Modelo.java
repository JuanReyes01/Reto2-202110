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


	private ArregloDinamico<Categoria> categoriaArreglo;
	
	public Modelo()
	{    int capacidad = 1001;
		datos = new ArregloDinamico<YoutubeVideo>();
		categorias = new ArregloDinamico<Categoria>();
		o = new Ordenamiento<YoutubeVideo>();
	}	
	
	public void cargar() throws ParseException, IOException{
		Reader in = new FileReader(VIDEO);
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
		    datos.addLast(nuevo);
		    }
		} 
	}
		
	
	
	
	public String cargarDatosConLinearProbing() throws IOException, ParseException{
		return ""+datos.size()+"";
	}
	
	public String cargarDatosConSeparateChaining(int k, String tg) throws IOException, ParseException{
		tabla = new TablaHashSeparateChaining<>(datos.size(), 1.5);
		for(int i=1; i<datos.size();i++){
			YoutubeVideo nuevo = datos.getElement(i);
			String cat = darNombreCategoria(nuevo.darId_categoria());
			
			String key = (k==1)?nuevo.darPais().trim().toLowerCase()+"-"+cat.trim().toLowerCase():
				          (k==2)?nuevo.darPais().trim().toLowerCase():
				        	(k==3)?cat.trim().toLowerCase(): tg;
			ArregloDinamico<YoutubeVideo> aux2 = (ArregloDinamico<YoutubeVideo>) tabla.get(key);
			if(aux2 == null){
				ArregloDinamico<YoutubeVideo> valor = new ArregloDinamico<YoutubeVideo>();
				valor.addLast(nuevo);
				tabla.put(key, valor);
			}
			else{
				aux2.addLast(nuevo); 
			}			
		}

		return "Tamaño de la tabla: "+tabla.size()+"";
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
	
	//-------------------------------------------------------------------------------------------------------------
	// Requerimientos
	//-------------------------------------------------------------------------------------------------------------

	
	public ILista<YoutubeVideo> req1(String pais, int num, String categoria){
		
		long miliI = System.currentTimeMillis();
		pais = pais.toLowerCase();
		categoria = categoria.toLowerCase();
		String k = pais.trim()+"-"+categoria.trim();
		Comparator<YoutubeVideo> y = new YoutubeVideo.ComparadorXViews();
		ILista<YoutubeVideo> videos= tabla.get(k);
		videos = o.ordenarQuickSort(videos, y, false);
		long miliF = System.currentTimeMillis();
    	long tot = (miliF-miliI); 
    	System.out.println("Tiempo de ejecucion req1: " + tot +" -- Tamanio tabla: " +tabla.size());
		return videos.sublista(num);
		
		}
		
	public String req2(String pais){
		long miliI = System.currentTimeMillis();
		int max = 0;
		YoutubeVideo mayor = null;
		if(tabla.keySet().isPresent(pais)==-1)
			return null;
		else{
		 ArregloDinamico<YoutubeVideo> t = (ArregloDinamico<YoutubeVideo>) tabla.get(pais);
		TablaHashSeparateChaining<String, Integer> numRepetidos = new TablaHashSeparateChaining<>(t.size(), 1.5);
		
		for(int i =1; i<=t.size();i++){
			int a = (numRepetidos.get(t.getElement(i).darVideoID())==null)?0:numRepetidos.get(t.getElement(i).darVideoID());
			int aux = 1+a;
			numRepetidos.put(t.getElement(i).darVideoID(), aux);
			if(max<aux){
				mayor = t.getElement(i);
				max = aux;
			}
//		int i= 1;
//		while(i<=t.size()){
//			YoutubeVideo actual = t.getElement(i);
//			int eliminados = 1;
//			for(int j=i+1;j<=t.size();j++){
//				if(actual.darVideoID().equals(t.getElement(j).darVideoID())){
//					t.deleteElement(j);
//					eliminados++;
//				}
//			}
//			t.deleteElement(i);
//			if(eliminados>max){
//				max= eliminados;
//				mayor = actual;
//			}
//			i++;
		}
		long miliF = System.currentTimeMillis();
    	long tot = (miliF-miliI); 
    	System.out.println("Tiempo de ejecucion req2: " + tot+" -- Tamanio tabla: " +tabla.size()+"--otro tamanio:"+numRepetidos.size());
		}
		return "Titulo: "+mayor.darTitulo()+" \nCanal: "+mayor.darCanal()+" \nPais: "+mayor.darPais()+" \nDias: "+ max;
	}
	/**
	 * Busca el video que ha sido mas tendencia en una categoria especifica.
	 * @param categoria Categoria especifica en la que estan los videos.
	 * @return Como respuesta deben aparecer el video con mayor tendencia de la categoria.  
	 */
	public String req3 (String categoria){
		long miliI = System.currentTimeMillis();
		int max = 0;
		YoutubeVideo mayor = null;
		 ArregloDinamico<YoutubeVideo> t = (ArregloDinamico<YoutubeVideo>) tabla.get(categoria);
		 
		 TablaHashSeparateChaining<String, Integer> numRepetidos = new TablaHashSeparateChaining<>(t.size(), 1.5);
			
			for(int i =1; i<=t.size();i++){
				int a = (numRepetidos.get(t.getElement(i).darVideoID())==null)?0:numRepetidos.get(t.getElement(i).darVideoID());
				int aux = 1+a;
				numRepetidos.put(t.getElement(i).darVideoID(), aux);
				if(max<aux){
					mayor = t.getElement(i);
					max = aux;
				}
		 
//		 int i= 1;
//			while(i<=t.size()){
//				YoutubeVideo actual = t.getElement(i);
//				int eliminados = 1;
//				for(int j=i+1;j<=t.size();j++){
//					if(actual.darVideoID().equals(t.getElement(j).darVideoID())){
//						t.deleteElement(j);
//						eliminados++;
//					}
//				}
//				t.deleteElement(i);
//				if(eliminados>max){
//					max= eliminados;
//					mayor = actual;
//				}
//				i++;
			}
			long miliF = System.currentTimeMillis();
	    	long tot = (miliF-miliI); 
	    	System.out.println("Tiempo de ejecucion req3: " + tot+" -- Tamanio tabla: " +tabla.size()+"--otro tamanio:"+numRepetidos.size());
	    	return (mayor != null)? " Titulo: "+ mayor.darTitulo()+"\n Chanel_Title: "+ mayor.darCanal()+"\n categoria: "+ mayor.darId_categoria()+" \n Dias: "+ max:"";
		}
	
	public ArregloDinamico<String> tags(YoutubeVideo y){
		String[] tag = y.darTags().split("\\|");
		ArregloDinamico<String> tags = new ArregloDinamico<String>();
		for(int i=0; i<tag.length;i++){
			//System.out.println(tag[i].replace("\"", ""));
			tags.addLast(tag[i].replace("\"", "").trim().toLowerCase());
		}
		return tags;
	}
	
	/**
	 * Busca los n videos con mas likes que son tendencia en un determinado pais y que posean la etiqueta designada.
	 * @param num Numero de videos que se desean ver. num > 0
	 * @param etiqueta Tag especifica que tienen los videos. != " " y != null
	 * @return Como respuesta deben aparecer los n videos que cumplen las caracteristicas y su respectiva informacion.  	
	 */
	public ILista<YoutubeVideo> req4(String etiqueta) {
		long miliI = System.currentTimeMillis();
		Comparator<YoutubeVideo> y = new YoutubeVideo.ComparadorXLikes();
		TablaHashSeparateChaining<String, ILista<YoutubeVideo>> e = new TablaHashSeparateChaining<>(datos.size(), 1);
		ArregloDinamico<String> tg = new ArregloDinamico<String>();
		for(int i=1;i<=datos.size();i++){
			tg = tags(datos.getElement(i));
			for(int j=1;j< tg.size();j++){
				if(e.get(tg.getElement(j))==null){
					ArregloDinamico<YoutubeVideo> a = new ArregloDinamico<YoutubeVideo>();
					a.addLast(datos.getElement(i));
					e.put(tg.getElement(j), a);
				}
				else{
					ArregloDinamico<YoutubeVideo> a = (ArregloDinamico<YoutubeVideo>) e.get(tg.getElement(j));
					a.addLast(datos.getElement(i));
					e.put(tg.getElement(j), a);
				}
			}
		}
		ArregloDinamico<YoutubeVideo> ord = (ArregloDinamico<YoutubeVideo>) o.ordenarQuickSort(e.get(etiqueta.trim()),y,false);
		long miliF = System.currentTimeMillis();
    	long tot = (miliF-miliI); 
    	System.out.println("Tiempo de ejecucion req4: " + tot+" --Tamanio tabla"+tabla.size()+"--tamanio otra"+e.size());
		return ord;
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
}


		