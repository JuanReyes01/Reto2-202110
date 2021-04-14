package model.data_structures;

import java.util.Map;
import java.util.Queue;
import java.util.Random;

import jdk.nashorn.internal.runtime.arrays.NumericElements;
import model.logic.YoutubeVideo;

public class TablaHashSeparateChaining<K extends Comparable<K>, V> implements ITablaSimbolos <K, V> {
	
	private Random r = new Random();
	private int n;   //Numero de de parejas K-V
	private int m;   //Tamanio de la tabla
    private ArregloDinamico[] tablaHash; //Tabla de simbolos desordenada 
    
    /*
     * Metodos enviados por el profesor
     */
    
    // Function that returns true if n
    // is prime else returns false
    static boolean isPrime(int n)
    {
        // Corner cases
        if (n <= 1) return false;
        if (n <= 3) return true;   
        // This is checked so that we can skip
        // middle five numbers in below loop
        if (n % 2 == 0 || n % 3 == 0) return false;       
        for (int i = 5; i * i <= n; i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0)
                return false;       
        return true;
    }

    // Function to return the smallest
    // prime number greater than N
    static int nextPrime(int N)
    {   
        // Base case
        if (N <= 1)
            return 2;  
        int prime = N;
        boolean found = false;

        // Loop continuously until isPrime returns
        // true for a number greater than n

        while (!found)
        {
            prime++;
            if (isPrime(prime))
                found = true;
        }
        return prime;
    }
   //-----------------------------------------------------------------------------

    /**
     * Inicializa una tabla de simbolos vacia.
     */
    public TablaHashSeparateChaining(int capacidad, double factorDeCarga)  {
    	m = (int)((int) capacidad/factorDeCarga);
    	m = (!isPrime(m))?nextPrime(m):m;
    	n = capacidad;
    	tablaHash = new ArregloDinamico[m];
    	for(int i=0;i<m;i++){
    		ArregloDinamico<NodoTS<K, V>> nuevo = new ArregloDinamico<NodoTS<K, V>>();
    		tablaHash[i] = nuevo; 
    	}
    }
    
    public void put(K key, V val) {
    	//Hash
    	int pos = hash(key);
    	//info de la posicion
    	ArregloDinamico<NodoTS<K, V>> info = tablaHash[pos];
       //Si la posicion es null    	
    	if(info.isEmpty()){
    		NodoTS<K, V> nodo = new NodoTS<K,V>(key, val);
    		info.addLast(nodo);
    		tablaHash[pos] = info;
    	}
       //Si ya hay un bucket en la posicion
    	else{
    		boolean stop = false;
    		for(int i=1;i<=info.size()&&!stop;i++){
    			//Ese objeto es el que ya tengo
    			NodoTS<K, V> actual = info.getElement(i);
    			if(actual.darLlave().equals(key)){
    				actual.asignarValor(val);
    				stop=true;
    			}
    		}
    		//Es un objeto nuevo
    		if(!stop){
    			NodoTS<K, V> nodo = new NodoTS<K,V>(key, val);
    			info.addLast(nodo);
    		}
    			
    	}
    	
    }

    /**
     * @param  key la llave a buscar
     * @return el valor asociado con la llave en la tabal de simbolos, null si no la encuentra
     * @throws IllegalArgumentException si la llave es null
     */
    public V get(K key) {
    	//hash
    	int i = hash(key);
        ArregloDinamico<NodoTS<K,V>> lista = tablaHash[i];
        for(int j=1; j<=lista.size();j++){
        	if(lista.getElement(j).darLlave().equals(key)){
        		return (V) lista.getElement(j).darValor();
        	}
        }
    	return null;
    } 
    
    
    /**
     * Borra la llave especifica y su valor asociado de la tabla.    
     * (si la llave esta en la tabla).  
     * @param  key la llave a remover
     * @throws IllegalArgumentException si la llave es null
     */
    public V remove(K key) {
    	int i = hash(key);
    	ArregloDinamico<NodoTS<K,V>> lista = tablaHash[i];
    	if(get(key)!=null){
    		for(int j=1; j<=lista.size();j++){
    			if(lista.getElement(j).darLlave().equals(key)){
    				V b = (V) lista.getElement(j).darValor();
    				lista.deleteElement(j);
    				return b;
    			}
    		}
    	}
    	return null;
    }
    
    /**
     * @param  key la llave a buscar
     * @return {@code true si el simbolo contiene la llave};
     *         {@code false de lo contrario.
     * @throws IllegalArgumentException si la llave es null
     */
    public boolean contains(K key) {
    	return (get(key)==null)?false:true;
    } 
    
 /*   public boolean contains(K k) 
	{
		boolean x = false;
		int posicion = hash(k);
		ILista<NodoTS<K,V>> listaSeparateChaining = listaNodos.getElement(posicion);
		if(listaSeparateChaining != null)
		{
			for(int i = 1; i <= listaSeparateChaining.size() && !x; i++)
			{
				if(listaSeparateChaining.getElement(i).darLlave().compareTo(k)==0)
				{
					x = true;
				}
			}
		}
		return x;
	}*/

    /**
     * @return el numero de parejas K-V en la tabla de simbolos.
     *
     */
    public int size() {
        return n;
    } 

    /**
     * @return {@code true si la tabla de simbolos esta vacia;
     *         {@code false de lo contrario.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    public ArregloDinamico<K> keySet(){
    	ArregloDinamico<K> llaves = new ArregloDinamico<K>();
    	for(int i=0; i<tablaHash.length;i++){
    		ArregloDinamico<NodoTS<K, V>> actual = tablaHash[i];
    		for(int j=1; j<=actual.size();j++){
    			NodoTS<K, V> nodo = actual.getElement(j);
    			if(nodo!=null)
    				llaves.addLast(nodo.darLlave());
    		}
    	}
    	return llaves;
    }

	@Override
	public ILista<V> valueSet() {
		ArregloDinamico<V> valores = new ArregloDinamico<V>();
    	for(int i=0; i<tablaHash.length;i++){
    		ArregloDinamico<NodoTS<K, V>> actual = tablaHash[i];
    		for(int j=1; j<=actual.size();j++){
    			NodoTS<K, V> nodo = actual.getElement(j);
    			if(nodo!=null)
    				valores.addLast(nodo.darValor());
    		}
    	}
    	return valores;
	}
	/*
	 * public ILista<V> valueSet() 
	{
		ILista<K> values = new ArregloDinamico<>(n);
		for(int i = 1; i <= n;i++){
			if(tablaHash.getElement(i) != null)
			{values.addLast(tablaHash.getElement(i).darValor());}
		}
		return values;
	}*/
	

	
	public int hash(K key) {
		int p = nextPrime(tablaHash.length);
		int m = tablaHash.length;
		int h = key.hashCode();
		
		int a = r.nextInt(p-1);
		int b = r.nextInt(p);
		return (Math.abs(h)%m);
	}
	

	/*
	 * private void rehash()
	{
		//ILista<NodoTS<K,V>> nodos = darNodos();
		n = 0;
		m = nextPrime(m);
		tablaHash = new ArregloDinamico<>(m);
		
		for(int i = 1; i<= tamanoTabla;i++)
		{
			tablaHash.addLast(null);
		}
		
		NodoTS<K, V> actual;
		while((actual = nodos.removeFirst())!= null)
		{
			put(actual.getKey(),actual.getValue());
		}
	
	 */
	


}


