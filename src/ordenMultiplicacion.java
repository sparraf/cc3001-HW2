/**
 * Tarea 2
 * Nombre: Sebastián Parra
 */
import java.util.Scanner;

public class ordenMultiplicacion {
	//Clase de tipo lista enlazada cuyos nodos pueden representar una matriz
	//o un parentesis. Si el nodo es una matriz, su valor sera su indice.
	//Si es un parentesis, si valor sera 0 si corresponde al caracter ')'
	//y -1 si corresponde a '('
	private int val;
	private ordenMultiplicacion sig;
	private ordenMultiplicacion ant;
	
	//Constructor
	public ordenMultiplicacion(int valor, ordenMultiplicacion siguiente, ordenMultiplicacion anterior) {
		val = valor;
		sig = siguiente;
		ant = anterior;
	}
	
	//Metodo de instancia, agrega un nodo con valor igual a 'valor' a la izquierda
	//o derecha del nodo instancado, segun se indique en el String 'lugar'
	//("izq" para agregar a la izquierda, y "der" para agregar a la derecha)
	//(no retorna nada)
	public void agregarElemento(int valor, String lugar) {
		if(lugar.equals("izq")) {
			ordenMultiplicacion anterior = this.ant;
			ordenMultiplicacion nuevo = new ordenMultiplicacion(valor, this, this.ant);
			this.ant = nuevo;
			if(anterior != null) {
				anterior.sig = nuevo;
			}
		}
		else if(lugar.equals("der")) {
			ordenMultiplicacion siguiente = this.sig;
			ordenMultiplicacion nuevo = new ordenMultiplicacion(valor, this.sig, this);
			this.sig = nuevo;
			if(siguiente != null) {
				siguiente.ant = nuevo;
			}
		}
		return;
	}
	
	//Metodo estatico, genera un objeto tipo ordenMultiplicacion con una cantidad n 
	//de matrices y retorna un puntero a la cabecera de la lista
	//(retorna un objeto tipo ordenMultiplicacion)
	public static ordenMultiplicacion agregarMatrices(int n) {
		ordenMultiplicacion cabecera = new ordenMultiplicacion(1, null, null);
		ordenMultiplicacion actual = cabecera;
		for(int i = 1; i < n; ++i) {
		actual.agregarElemento(i+1, "der");
		actual = actual.sig;
		}
		return cabecera;
	}
	
	//INICIO METODO DEL APUNTE
	//Metodo estatico, resuelve utilizando programacion dinamica el problema de
	//encontrar la posicion de parentesis que optimiza la multiplicacion de n
	//matrices y todos sus subproblemas. El arreglo p corresponde a un vector con
	//con las dimensiones de las matrices a multiplicar, la matriz m[i,j] indica el
	//costo minimo de multiplicar de la matriz i a la matriz j, y la matriz s[i,j]
	//indica la posicion que debe tener el parentesis para multiplicar optimamente
	//de la matriz i a a matriz j (no retorna nada)
	public static void multiplicacionCadenaMatrices(int[] p, int[][] m, int[][] s) {
		// Matriz Ai tiene dimensiones p[i-1] x p[i] para i = 1..n
		// El primer indice para p es 0 y el primer indice para m y s es 1
		int n = p.length - 1;
		for (int i = 1; i <= n; i++) {
			m[i][i] = 0;
		}
		for (int l = 2; l <= n; l++) {
			for (int i = 1; i <= n - l + 1; i++) {
				int j = i + l - 1;
				m[i][j] = Integer.MAX_VALUE;
				for (int k = i; k <= j-1; k++) {
					int q = m[i][k] + m[k+1][j] + p[i-1]*p[k]*p[j];
					if (q < m[i][j]) {
						m[i][j] = q;
						s[i][j] = k;
					} 
				}
			}
		}
		return;
	}
	//FIN METODO DEL APUNTE
	
	//Metodo de instancia, determina la posicion optima del parentesis para
	//multiplicar de la matriz inicio a la matriz fin a partir de una matriz de 
	//parentesis s[][], lo agrega a la lista de matrices, y luego resuelve los 
	//subproblemas restantes (no retorna nada)
	public void parentesisOptimo(int inicio, int fin, int[][] s) {
		//Determinar posicion optima del parentesis
		int k = s[inicio][fin];
		
		//Caso base: Si solo es una matriz, no hacer nada
		if(inicio == fin) {
			return;
		}
		else {
			//Agregar los parentesis y resolver los subproblemas
			this.agregarParentesis(inicio, fin, k);
			this.parentesisOptimo(k+1, fin, s);
			this.parentesisOptimo(inicio, k, s);
			return;
		}
	}
	
	//Metodo de instancia, agrega los parentesis necesarios sabiendo que para 
	//multiplicar de manera optima las matrices desde ini hasta fin se 
	//requiere poner un parentesis en la matriz k (no retorna nada)
	public void agregarParentesis(int ini, int fin, int k) {
		//Paso 1: Guardar nodos que tengan las matrices ini, fin y k
		ordenMultiplicacion inicio = this;
		ordenMultiplicacion division = this;
		ordenMultiplicacion termino = this;
		while (termino.val != fin) {
			if (termino.val == ini) {
				inicio = termino;
			}
			if(termino.val == k) {
				division = termino;
			}
			termino = termino.sig;
		}
		
		//Paso 2: Revisar si hay mas de una matriz a la derecha de la division
		if((fin - k) > 1) {
			division.agregarElemento(-1, "der");
			termino.agregarElemento(0, "der");
		}
		//Paso 3: Revisar si hay mas de una matriz a la izquierda de la division
		if(k != ini) {
			division.agregarElemento(0, "der");
			inicio.agregarElemento(-1, "izq");
		}
		return;
	}
	
	//Metodo de instancia, dada una lista tipo ordenMultiplicacion, imprime sus
	//Valores siguiendo la codificación indicada en el enunciado
	public void imprimirString() {
		//Paso 0: Inicializar string con un parentesis izquierdo
		String salida = "(";
		
		//Paso 1: Recorrer lista hasta el primer nodo
		ordenMultiplicacion actual = this;
		while(actual.ant != null) {
			actual = actual.ant;
		}
		
		//Paso 2: Recorrer lista hasta el ultimo valor e ir agregando caracteres
		while (actual != null) {
			if(actual.val == 0) {
				salida += ")";
			}
			else if(actual.val == -1) {
				salida += "(";
			}
			else {
				salida += ".";
			}
			actual = actual.sig;
		}
		
		//Paso 3: Agregar parentesis final
		salida += ")";
		System.out.println(salida);
	}
	
	public static void main(String[] args) {
		//Se pide al usuario ingresar una entrada
		System.out.println("Ingrese secuecias(s) de strings: ");
		Scanner entrada = new Scanner(System.in);
			
		//Se revisan las lineas de la entrada hasta que no quede ninguna
		while(entrada.hasNextLine()) {
			//Leer una linea de strings
			String linea = entrada.nextLine();
				
			//Obtener un arreglo de los strings de las dimensiones de las
			//matrices
			String[] dimensiones = linea.split(" ");
			if(dimensiones.length <= 1) {
				System.out.println("Por favor ingrese una entrada valida");
				continue;
			}
			int[] dims = new int[dimensiones.length];
			
			//Parsear arreglo de strings a enteros
			for(int i = 0; i < dims.length; ++i) {
				dims[i] = Integer.parseInt(dimensiones[i]); 
			}
							
			//Crear lista de matrices
			ordenMultiplicacion listaMatrices = agregarMatrices(dims.length-1);
						
			//Crear matriz que indica el costo optimo de
			//multiplicar de la mariz Ai hasta la Aj en numero de operaciones
			int[][] matrizCosto = new int[dims.length][dims.length];
				
			//Crear matriz que indica la ubicacion del parenesis para
			//multiplicar de manera optima de la mariz Ai hasta la Aj
			int[][] lugarParentesis = new int[dims.length][dims.length];
				
			//Rellenar las matrcies de costo y de parentesis utilizando el
			//del apunte 
			multiplicacionCadenaMatrices(dims, matrizCosto, lugarParentesis);
			
			//Agregar los parentesis a la lista de matrices e imprimir resultado
			listaMatrices.parentesisOptimo(1, dims.length-1, lugarParentesis);
			listaMatrices.imprimirString();
			System.out.println("Ejecucion terminada");
		}
		entrada.close();
	}
}
