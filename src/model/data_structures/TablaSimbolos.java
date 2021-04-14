package model.data_structures;

import java.util.Random;

public class TablaSimbolos<K extends Comparable<K>, V> implements ITablaSimbolos<K, V> {

	private ArregloDinamico<NodoTS<K,V>> tabla;
	
	public TablaSimbolos(int c) {
		tabla = new ArregloDinamico<NodoTS<K,V>>(c);
	}
	
	/**
	 * Agrega un elemento nuevo a la tabla 
	 * Complejidad O(N)
	 */
//	public void put(K k, V v) 
//	 {
//		NodoTS<K,V> nodo = new NodoTS<K,V>(k,v);
//		boolean stop = false;
//		for(int i=1; i<=tabla.size()&&!stop;i++){
//			if(nodo.compareTo(tabla.getElement(i))==0){
//				tabla.getElement(i).asignarValor(v);
//				stop = true;
//			}
//		}
//		if(!stop){
//			tabla.addLast(nodo);
//		}
//	}
	public void put(K k, V v){
		int pos = hash(k);
		NodoTS<K,V> nodo = new NodoTS<K,V>(k,v);
		if(tabla.getElement(pos)==null)
			tabla.insertElement(nodo, pos);
		else
			tabla.getElement(pos).asignarValor(v);
	}

	/**
	 * Metodo que retorna el valor de la llave dada por parametro
	 * Complejidad O(N)
	 */
//	public V get(K k) {
//		boolean stop = false;
//		for(int i=1; i<=tabla.size()&&!stop;i++){
//			if(k.compareTo(tabla.getElement(i).darLlave())==0){
//				return tabla.getElement(i).darValor();
//			}
//		}
//		return null;
//	}
	public V get(K k){
		int pos = hash(k);
		return tabla.getElement(pos).darValor();
	}

	/**
	 * Elimina el valor de una llave K
	 * Complejidad O(N)
	 */
//	public V remove(K k) {
//		V valor = null;
//		for(int i=1; i<=tabla.size();i++){
//			if(k.compareTo(tabla.getElement(i).darLlave())==0){
//				valor = tabla.getElement(i).darValor();
//				tabla.getElement(i).asignarValor(null);
//				return valor;
//			}
//		}
//		return null;
//	}
	public V remove(K k){
		int pos = hash(k);
		V valor = tabla.getElement(pos).darValor();
		tabla.deleteElement(pos);
		return valor;
	}

	/**
	 * Retorna true si la llave esta almacenada
	 * Complejidad O(N)
	 */
	public boolean contains(K k) {
		for(int i=1; i<=tabla.size();i++){
			if(k.compareTo(tabla.getElement(i).darLlave())==0){
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna true si la tabla tiene datos
	 * Complejidad O(K)
	 */
	public boolean isEmpty() {
		return tabla.isEmpty();
	}

	/**
	 * Retorna la cantidad de duplas en la tabla
	 * Complejidad O(K)
	 */
	public int size() {
		return tabla.size();
	}

	/**
	 * 
	 */
	public ILista<K> keySet() {
		ArregloDinamico<K> llaves = new ArregloDinamico<K>(); 
		for(int i=1;i<=tabla.size();i++){
			llaves.addLast(tabla.getElement(i).darLlave());
		}
		return llaves;
	}

	public ILista<V> valueSet() {
		ArregloDinamico<V> valores = new ArregloDinamico<V>(); 
		for(int i=1;i<=tabla.size();i++){  
			valores.addLast(tabla.getElement(i).darValor());
		}
		return valores;
	}

	@Override
	public int hash(K key) {
		int p = nextPrime(tabla.size());
		int m = tabla.size();
		int h = hash(key);
		Random r = new Random();
		int a = r.nextInt(p-1);
		int b = r.nextInt(p);
		return (Math.abs(a*(h)+b)%p)%m;
	}
	
	
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

	


}
